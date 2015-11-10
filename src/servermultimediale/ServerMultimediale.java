/*
 * Copyright (C) 2015 betacentury
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package servermultimediale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author betacentury
 */
public class ServerMultimediale {

    protected static final int PORT = 54637;
    protected static final String musicPath = "./music",
            logFile = "/tmp/log",
            musicLibrary = "/tmp/list",
            lastPlay = "/tmp/nowPlaying",
            version = "1.0";
    protected static final String [] estensioni = {"mp3","mp4","m4a","ogg"};
    protected final static ArrayList<Brano> playlist = new ArrayList<>();

    public static void main(String args[]) {
        
        new Thread(new UpdateList() ).start();
        new Thread(new BeaconServer()).start();
        
        ServerSocket serverSocket = null;
        Socket socket;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(serverSocket);
        if (Coda.autopilota)
        {
            new SendCommand(SendCommand.PLAY, "").run();
        }
        while (serverSocket != null) {
            try {
                socket = serverSocket.accept();
                System.out.println("\tConnessione in arrivo da "+socket.getInetAddress().toString());
                BufferedReader socketIN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter socketOUT = new PrintWriter( new PrintStream (socket.getOutputStream() ));
                String buffer = socketIN.readLine();
                switch ( buffer != null ? buffer.toLowerCase() : "" )
                {
                    case "get":
                        new Thread(new ShareList( socketIN, socketOUT ) ).start();
                        break;
                    case "play":
                        new Thread(new AggiungiAllaPlaylist( socketIN ) ).start();
                        break;
                    case "remove":
                        new Thread(new AggiungiAllaPlaylist( socketIN, AggiungiAllaPlaylist.REMOVE ) ).start();
                        break;
                    case "top":
                        new Thread(new AggiungiAllaPlaylist( socketIN, AggiungiAllaPlaylist.TOP ) ).start();
                        break;
                    case "kill":
                        new Thread(new Kill() ).start();
                        socket.close();
                        break;
                    case "list":
                        new Thread(new List( socketOUT ) ).start();
                        break;
                    case "volume":
                        new Thread(new Volume( socketIN ) ).start();
                        break;
                    case "last":
                        new Thread(new ShareList( socketIN, socketOUT, ShareList.LAST ) ).start();
                        break;
                    case "shuffle":
                        new Thread(new Shuffle(playlist) ).start();
                    default:
                        socket.close();
                        break;
                }
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
        }
    }
}