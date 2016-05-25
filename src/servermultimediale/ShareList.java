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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author betacentury
 */
class ShareList implements Runnable {

    private final BufferedReader socketIN;
    private final PrintWriter socketOUT;
    private final byte mode;
    protected final static byte LIST = 0, LAST = 1;
    
    public ShareList(BufferedReader _socketIN, PrintWriter _socketOUT) {
        this( _socketIN, _socketOUT, LIST);
    }
    public ShareList(BufferedReader _socketIN, PrintWriter _socketOUT, byte _mode) {
        socketIN = _socketIN;
        socketOUT = _socketOUT;
        mode = _mode;
    }

    @Override
    public void run() {
        try {
            String buffer = mode == LIST ? socketIN.readLine() : null;
            System.out.println("Esecuzione " + cmd(buffer));
            ProcessBuilder pb = new ProcessBuilder("sh", "-c",cmd(buffer));
            pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(ServerMultimediale.logFile.toString())));
            Process p = pb.start();
            BufferedReader stdin = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while( (buffer = stdin.readLine()) != null )
            {
                System.out.println("\t"+buffer);
                socketOUT.println(buffer);
                socketOUT.flush();
            }
            p.waitFor();
            socketIN.close();
            socketOUT.close();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ShareList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String cmd(String buffer)
    {
        StringBuilder out = new StringBuilder("cat ");
        switch (mode)
        {
            case LIST:
                out.append(ServerMultimediale.musicLibrary).append(" | grep -i \"").append(buffer != null ? buffer : "").append("\" | sort");
                break;
            case LAST:
                out.append(ServerMultimediale.lastPlay).append(regex());
                break;
            default:
                break;
        }
        return out.toString();
    }
    private static String regex()
    {
        StringBuilder out = new StringBuilder(" | grep \"^Playing.*$\" | grep -o \"[.]/.*[");
        for (int i=0; i<ServerMultimediale.estensioni.length; i++)
        {
            out.append(ServerMultimediale.estensioni[i]).append( (i+1<ServerMultimediale.estensioni.length)?"\\|":"" );
        }
        out.append("]\"");
        System.out.println(out.toString());
        return out.toString();
    }
}
