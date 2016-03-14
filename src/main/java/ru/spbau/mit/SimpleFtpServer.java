package ru.spbau.mit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleFtpServer {
    private final Path rootPath;
    private final Thread listenThread;
    private final ExecutorService clientThreads;
    private final ServerSocket serverSocket;

    public SimpleFtpServer(int port) throws IOException {
        this(port, Paths.get(""));
    }

    public SimpleFtpServer(int port, Path rootPath) throws IOException {
        this.rootPath = rootPath;
        clientThreads = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(port);
        listenThread = new Thread(new ListenRunnable());
        listenThread.start();
    }

    private class ListenRunnable implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                Socket client;
                try {
                    client = serverSocket.accept();
                } catch (IOException e) {
                    break;
                }
                clientThreads.submit(new SimpleFtpClientHandler(rootPath, client));
            }
            try {
                if (!serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops accepting new connections and closes socket,
     * unclosed connections are still processed
     */
    public void shutdown() throws InterruptedException {
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        listenThread.interrupt();
        listenThread.join();
        clientThreads.shutdown();
    }
}
