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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author betacentury
 */
public class Play implements Runnable {

    private static Piece nowPlaying;
    private final static Path LASTPLAY = Paths.get("/tmp/nowPlaying");
    
    public Play(Piece _brano) {
        nowPlaying = _brano;
    }

    @Override
    public void run() {
        try {
            System.out.println("Riproduzione di: "+nowPlaying);
            ProcessBuilder pb = new ProcessBuilder("sh", "-c","mplayer \""+nowPlaying.getPath()+"\"");
            pb.directory(new File(ServerMultimediale.MUSICPATH.toString()));
            pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(ServerMultimediale.LOGFILE.toString())));
            pb.redirectOutput(new File(LASTPLAY.toString()));
            Process p = pb.start();
            p.waitFor();
            System.out.println(nowPlaying + (p.exitValue() == 0 ? " riprodotto con successo" : " - errore nella riproduzione") );
        } catch (IOException ex) {
            Logger.getLogger(ShareList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static Piece nowPlaying() { return nowPlaying; }
}
