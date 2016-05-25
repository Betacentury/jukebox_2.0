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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author betacentury
 */
public class UpdateList implements Runnable{

    @Override
    public void run() {
        while (true)
        {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "-c",command());
                pb.directory(new File(ServerMultimediale.musicPath.toString()));
                pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(ServerMultimediale.logFile.toString())));
                pb.redirectOutput(new File(ServerMultimediale.musicLibrary.toString()));
                Process p = pb.start();
                p.waitFor();
                System.out.println(p.exitValue() == 0 ? "Playlist aggiornata con successo" : "Errore nell'aggiornamento della playlist");
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(UpdateList.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(300000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UpdateList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static String command()
    {
        StringBuilder out = new StringBuilder("find | grep '");
        for (int i=0; i<ServerMultimediale.estensioni.length; i++)
        {
            out.append(ServerMultimediale.estensioni[i]).append( (i+1<ServerMultimediale.estensioni.length)?"$\\|":"$" );
        }
        out.append("'");
        System.out.println(out.toString());
        return out.toString();
    }
}