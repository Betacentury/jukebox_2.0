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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author betacentury
 */
public class Volume implements Runnable {

    private final BufferedReader socketIN;
    
    public Volume(BufferedReader _socketIN) {
        socketIN = _socketIN;
    }

    @Override
    public void run() {
        try {
            String buffer = socketIN.readLine();
            ProcessBuilder pb = new ProcessBuilder("sh", "-c","amixer set PCM "+buffer+"%");
            pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(ServerMultimediale.LOGFILE.toString())));
            Process p = pb.start();
            socketIN.close();
            System.out.println("amixer set PCM "+buffer+"%");
        } catch (IOException ex) {
            Logger.getLogger(Volume.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
