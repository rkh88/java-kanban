package service;

import server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
