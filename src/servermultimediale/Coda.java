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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author betacentury
 */
public class Coda implements Runnable {
    
    protected final static boolean AUTOPILOTA = true;
    private final static ArrayList<Piece> PLAYLIST = new ArrayList<>();
    
    @Override
    public void run() {
        while (PLAYLIST.size() > 0 || AUTOPILOTA)
        {
            new Play( PLAYLIST.size() > 0 ? PLAYLIST.remove(0) : MusicLibrary.getInstance().randomPiece() ).run();
        }
    }
    public static Piece getLast() { return Play.nowPlaying() ; }
    public static synchronized boolean addPiece(Collection <Piece> _add){ return PLAYLIST.addAll(_add); }
    public static synchronized int addPiece(Piece _add){ return PLAYLIST.add(_add) ? PLAYLIST.size() : -1 ; }
    public static synchronized void addPiece(int _i, Piece _add){ PLAYLIST.add(_i , _add); }
    public static synchronized void shuffle(){ Collections.shuffle(PLAYLIST); }
    public static synchronized void remove(Collection <Piece> _remove){ PLAYLIST.removeAll(_remove); }
    public static synchronized Piece remove(Piece _brano) { return PLAYLIST.remove(PLAYLIST.lastIndexOf(_brano)); }
    public static synchronized Collection<Piece> getPlaylist() { return PLAYLIST; }
}
