package ru.spbau.mit;

import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class SimpleFtpClientTest {
    @Test
    public void testList() throws IOException {
        SocketTestUtils.checkSocketIO((ans) -> {
            // CHECKSTYLE.OFF: MagicNumber
            ans.writeInt(3);
            // CHECKSTYLE.ON: MagicNumber
            ans.writeUTF("file2");
            ans.writeBoolean(false);
            ans.writeUTF("dir");
            ans.writeBoolean(true);
            ans.writeUTF("file3");
            ans.writeBoolean(false);
        }, (s) -> {
            try (SimpleFtpClient client = new SimpleFtpClient(s)) {
                assertArrayEquals(new DirectoryItem[]{
                        new DirectoryItem("file2", false),
                        new DirectoryItem("dir", true),
                        new DirectoryItem("file3", false)
                }, client.list("some-dir"));
            }
        }, (DataOutputStream command) -> {
            command.writeInt(1);
            command.writeUTF("some-dir");
        });
    }

    @Test
    public void testGet() throws IOException {
        // CHECKSTYLE.OFF: MagicNumber
        final byte[] contents = new byte[1024 * 1024 + 100];
        // CHECKSTYLE.ON: MagicNumber
        for (int i = 0; i < contents.length; i++) {
            contents[i] = (byte) (i * i * i);
        }
        SocketTestUtils.checkSocketIO((ans) -> {
            ans.writeInt(contents.length);
            ans.write(contents, 0, contents.length);
        }, (s) -> {
            try (SimpleFtpClient client = new SimpleFtpClient(s)) {
                assertArrayEquals(contents, client.get("some-file"));
            }
        }, (DataOutputStream command) -> {
            command.writeInt(2);
            command.writeUTF("some-file");
        });
    }
}
