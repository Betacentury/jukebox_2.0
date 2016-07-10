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
import java.io.PrintWriter;
import java.util.Collection;
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
            switch (mode){
                case LIST:
                    Collection<Piece> pieces = MusicLibrary.getInstance().find(socketIN.readLine());
                    for (Piece p: pieces){
                        System.out.println("\t"+p.getPath());
                        socketOUT.println(p.getPath());
                        socketOUT.flush();
                    }
                    break;
                case LAST:
                        socketOUT.println(Coda.getLast().getPath());
                        socketOUT.flush();
                    break;
            }
            socketIN.close();
            socketOUT.close();
        } catch (IOException ex) {
            Logger.getLogger(ShareList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
