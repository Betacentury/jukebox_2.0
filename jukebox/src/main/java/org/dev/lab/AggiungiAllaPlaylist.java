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
package org.dev.lab;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author betacentury
 */
public class AggiungiAllaPlaylist implements Runnable {

    private final BufferedReader socketIN;
    private static Thread nowPlaying;
    protected final static byte ADD = 0 ,REMOVE = 1,TOP = 2;
    private final byte add;
    
    public AggiungiAllaPlaylist(BufferedReader _socketIN) {
        this(_socketIN, ADD);
    }
    AggiungiAllaPlaylist(BufferedReader _socketIN, byte _add) {
        socketIN = _socketIN;
        add = _add;
    }

    @Override
    public void run() {
        try {
            String buffer;
            //System.out.println(socket.toString() + "\t" + buffer);
            while ( (buffer = socketIN.readLine()) != null)
            {
                Piece brano = new Piece ( buffer );
                switch (add)
                {
                    case ADD:
                        int i;
                        if ( ( i = Coda.addPiece(brano) ) > -1 )
                            System.out.println("aggiunta di "+brano+" alla posizione "+i);
                        break;
                    case TOP:
                        Coda.addPiece( 0, brano );
                    case REMOVE:
                        Coda.remove(brano);
                        break;
                    default:
                        break;
                }
            }
            System.out.println("Stato della coda: " + (nowPlaying != null ? nowPlaying.getState() : "null") );
            if (nowPlaying == null || nowPlaying.getState() != Thread.State.WAITING)
            {
                nowPlaying = new Thread(new Coda());
                nowPlaying.start();
            }
            socketIN.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServerMultimediale.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}