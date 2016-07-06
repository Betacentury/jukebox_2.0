/*
 * Copyright (C) 2016 betacentury
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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author betacentury
 */
public class MusicLibrary {
    private static MusicLibraryService service;
    protected final static byte LEGACY = 0, NEW = 1;

    public static MusicLibraryService getInstance(){
        return getInstance(LEGACY);
    }
    public static synchronized MusicLibraryService getInstance(int _n){
        if (service == null) {
            switch (_n){
                case NEW:
                    //singleton = new nuovaPlaylist();
                    break;
                case LEGACY:
                default:
                    service = new LegacyMusicLibrary();
                    break;
            }
        }
        return service;
    }
}
interface MusicLibraryService {
    public int dimLibrary();
    public Piece atIndex(int _i);
    public Piece randomPiece();
    public Queue<Piece> find(String pattern);
}
class LegacyMusicLibrary implements MusicLibraryService{
    private static boolean updating;
    private static Runnable updatingService;
    private static final Path MUSICLIBRARY = Paths.get("/tmp/list");
    
    public LegacyMusicLibrary(){
        updatingService = new Runnable(){
            @Override
            public void run() {
                while (true)
                {
                    updating = true;
                    try {
                        ProcessBuilder pb = new ProcessBuilder("sh", "-c",command());
                        pb.directory(new File(ServerMultimediale.MUSICPATH.toString()));
                        pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(ServerMultimediale.LOGFILE.toString())));
                        pb.redirectOutput(new File(MUSICLIBRARY.toString()));
                        Process p = pb.start();
                        p.waitFor();
                        System.out.println(p.exitValue() == 0 ? "Playlist aggiornata con successo" : "Errore nell'aggiornamento della playlist");
                    } catch (InterruptedException | IOException ex) {
                        Logger.getLogger(LegacyMusicLibrary.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    updating = false;

                    try {
                        Thread.sleep(300000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LegacyMusicLibrary.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            private String command()
            {
                StringBuilder out = new StringBuilder("find | grep '");
                for (int i=0; i<ServerMultimediale.ESTENSIONI.length; i++)
                {
                    out.append(ServerMultimediale.ESTENSIONI[i]).append( (i+1<ServerMultimediale.ESTENSIONI.length)?"$\\|":"$" );
                }
                out.append("'");
                System.out.println(out.toString());
                return out.toString();
            }
        };
        new Thread( updatingService ).start();
    }
    private Queue<String> unnamedMethod(String _command) throws IOException, InterruptedException{
        String buffer;
        ArrayDeque<String> out = new ArrayDeque<>();
        ProcessBuilder pb = new ProcessBuilder("sh", "-c", _command);
        pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(ServerMultimediale.LOGFILE.toString())));
        Process p = pb.start();
        BufferedReader stdin = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while( (buffer = stdin.readLine()) != null )
        {
            out.offer(buffer);
        }
        p.waitFor();
        return out;
    }
    @Override
    public int dimLibrary()
    {
        try {
            while (updating){

            }
            Queue<String> dim = unnamedMethod("cat "+MUSICLIBRARY+" | wc -l");
            return Integer.parseInt(dim.poll());
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(LegacyMusicLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    @Override
    public Piece atIndex(int _i)
    {
        try {
            while (updating){

            }
            Queue<String> piece = unnamedMethod("head -"+_i+" "+MUSICLIBRARY+" | tail -1");
            return new Piece(piece.poll());
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(LegacyMusicLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Piece(null);
    }
    @Override
    public Piece randomPiece()
    {
        return atIndex((int) (Math.random()*dimLibrary()));
    }

    @Override
    public Queue<Piece> find(String pattern) {
        ArrayDeque<Piece> out = null;
        try {
            Queue<String> matches = unnamedMethod("cat " + MUSICLIBRARY + " | grep -i \"" + (pattern != null ? pattern : "") +"\" | sort");
            out = new ArrayDeque<>();
            for (String s : matches){
                out.offer(new Piece(s));
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(LegacyMusicLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }
}