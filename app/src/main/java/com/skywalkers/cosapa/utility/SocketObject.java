package com.skywalkers.cosapa.utility;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketObject {
    private static Socket socket;
    private static final String URL = "http://192.168.1.68:3000";

    public static Socket getSocket() throws URISyntaxException {
        if (socket == null) {
            socket = IO.socket(URL);
            socket.connect();
        }
        return socket;
    }
}
