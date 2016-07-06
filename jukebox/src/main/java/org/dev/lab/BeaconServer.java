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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 *
 * @author betacentury
 */
public class BeaconServer implements Runnable{
    @Override
    public void run() {
        
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket();
            serverSocket.setBroadcast(true);
        } catch (SocketException ex) {
            Logger.getLogger(BeaconServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] sendData;
        
        while (serverSocket != null) {
            try {
                sendData = new NetConfig("Vaddacavadda", ServerMultimediale.VERSION, ServerMultimediale.PORT).toString().getBytes();
                DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8000);
                serverSocket.send(sendPacket);
                sleep(5000);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(BeaconServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
