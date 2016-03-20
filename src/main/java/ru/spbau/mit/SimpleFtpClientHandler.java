package ru.spbau.mit;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class SimpleFtpClientHandler implements Runnable {
    private final Path rootPath;
    private final Socket client;

    SimpleFtpClientHandler(Path rootPath, Socket client) {
        this.rootPath = rootPath;
        this.client = client;
    }

    private DataInputStream in;
    private DataOutputStream out;

    private void handleList() throws IOException {
        Path dir = rootPath.resolve(in.readUTF());
        if (!Files.isDirectory(dir)) {
            out.writeInt(0);
            return;
        }
        List<Path> content =
                Files.list(dir)
                        .sorted(Comparator.comparing(Path::getFileName))
                        .collect(Collectors.toList());
        out.writeInt(content.size());
        for (Path inside : content) {
            out.writeUTF(inside.getFileName().toString());
            out.writeBoolean(Files.isDirectory(inside));
        }
    }

    private void handleGet() throws IOException {
        Path file = rootPath.resolve(in.readUTF());
        if (Files.isDirectory(file)) {
            out.writeInt(0);
            return;
        }
        try (InputStream fin = Files.newInputStream(file)) {
            out.writeInt((int) Files.size(file));
            IOUtils.copyLarge(fin, out);
        } catch (NoSuchFileException | UnsupportedOperationException | AccessDeniedException e) {
            out.writeInt(0);
        }
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            while (true) {
                int command = in.readInt();
                switch (command) {
                    case 1:
                        handleList();
                        break;
                    case 2:
                        handleGet();
                        break;
                    default:
                        throw new RuntimeException("Invalid command received from client");
                }
            }
        } catch (EOFException e) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!client.isClosed()) {
                try {
                    client.shutdownInput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    client.shutdownOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
