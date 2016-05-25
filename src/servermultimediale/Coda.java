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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author betacentury
 */
public class Coda implements Runnable {
    
    protected final static boolean autopilota = true;
    @Override
    public void run() {
        while (ServerMultimediale.playlist.size() > 0 || autopilota)
        {
            new Play( ServerMultimediale.playlist.size() > 0 ? ServerMultimediale.playlist.remove(0) : randomMusic() ).run();
        }
    }
    private static int dimLibrary()
    {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c","cat "+ServerMultimediale.musicLibrary+" | wc -l");
            pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(ServerMultimediale.logFile.toString())));
            Process p = pb.start();
            BufferedReader stdin = new BufferedReader(new InputStreamReader(p.getInputStream()));
            return Integer.parseInt(stdin.readLine());
        } catch (IOException ex) {
            Logger.getLogger(Coda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    private static Brano atIndex(int _i)
    {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c","head -"+_i+" "+ServerMultimediale.musicLibrary+" | tail -1");
            pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(ServerMultimediale.logFile.toString())));
            Process p = pb.start();
            BufferedReader stdin = new BufferedReader(new InputStreamReader(p.getInputStream()));
            return new Brano(stdin.readLine());
        } catch (IOException ex) {
            Logger.getLogger(Coda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Brano(null);
    }
    private static Brano randomMusic()
    {
        return atIndex((int) (Math.random()*dimLibrary()));
    }
}
