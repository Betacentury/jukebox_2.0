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
public class Play implements Runnable {

    private final Brano brano;
    
    public Play(Brano _brano) {
        brano = _brano;
    }

    @Override
    public void run() {
        try {
            System.out.println("Riproduzione di: "+brano);
            ProcessBuilder pb = new ProcessBuilder("sh", "-c","mplayer \""+brano.getPath()+"\"");
            pb.directory(new File(ServerMultimediale.musicPath));
            pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(ServerMultimediale.logFile)));
            pb.redirectOutput(new File(ServerMultimediale.lastPlay));
            Process p = pb.start();
            p.waitFor();
            System.out.println( brano + (p.exitValue() == 0 ? " riprodotto con successo" : " - errore nella riproduzione") );
        } catch (IOException ex) {
            Logger.getLogger(ShareList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}