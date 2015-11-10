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

import java.io.PrintWriter;

/**
 *
 * @author betacentury
 */
class List implements Runnable {

    private final PrintWriter socketOUT;
    private final byte cmd;
    protected final static byte LIST = 0;
    
    public List(PrintWriter _socketOUT) {
        this(_socketOUT, LIST);
    }
    public List(PrintWriter _socketOUT, byte _cmd) {
        cmd = _cmd;
        socketOUT = _socketOUT;
    }

    @Override
    public void run() {
        switch (cmd)
        {
            case LIST:
                for (int i=0; i<ServerMultimediale.playlist.size(); i++)
                {
                    socketOUT.println(ServerMultimediale.playlist.get(i).getPath());
                    socketOUT.flush();
                }
                break;
            default:
                break;
        }
        socketOUT.close();
    }
    
}
