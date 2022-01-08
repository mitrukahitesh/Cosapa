package com.skywalkers.cosapa.utility;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketObject {
    private static Socket socket;
    private static final String URL = "https://cosapa-server.herokuapp.com";

    public static Socket getSocket() throws URISyntaxException {
        if (socket == null) {
            socket = IO.socket(URL);
            socket.connect();
        }
        return socket;
    }
}
