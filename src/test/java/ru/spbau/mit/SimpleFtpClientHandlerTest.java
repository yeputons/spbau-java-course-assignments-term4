package ru.spbau.mit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Files;

public class SimpleFtpClientHandlerTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private void runTest(
            ThrowingConsumer<DataOutputStream, IOException> commandsWriter,
            ThrowingConsumer<DataOutputStream, IOException> expectedAnswersWriter
            ) throws IOException {
        SocketTestUtils.checkSocketIO(
                commandsWriter,
                (s) -> {
                    new SimpleFtpClientHandler(temporaryFolder.getRoot().toPath(), s).run();
                },
                expectedAnswersWriter
        );
    }

    @Before
    public void createDirectory() throws IOException {
        temporaryFolder.newFolder("afolder");
        Files.write(temporaryFolder.newFile("test1.txt").toPath(), "Contents of test1".getBytes());
        Files.write(temporaryFolder.newFile("test2.txt").toPath(), "Contents of test2".getBytes());
        temporaryFolder.newFolder("zfolder");
        Files.write(temporaryFolder.newFile("zfolder/test3.txt").toPath(), "Contents of test3".getBytes());
    }

    @Test
    public void testBadCommand() throws IOException {
        // CHECKSTYLE.OFF: MagicNumber
        runTest((c) -> c.writeInt(3), (c) -> { });
        // CHECKSTYLE.ON: MagicNumber
    }

    @Test
    public void testDirectoryList() throws IOException {
        runTest((c) -> {
            c.writeInt(1);
            c.writeUTF(".");
        }, (c) -> {
            // CHECKSTYLE.OFF: MagicNumber
            c.writeInt(4);
            // CHECKSTYLE.ON: MagicNumber
            c.writeUTF("afolder");
            c.writeBoolean(true);
            c.writeUTF("test1.txt");
            c.writeBoolean(false);
            c.writeUTF("test2.txt");
            c.writeBoolean(false);
            c.writeUTF("zfolder");
            c.writeBoolean(true);
        });
    }

    @Test
    public void testSubdirectoryList() throws IOException {
        runTest((c) -> {
            c.writeInt(1);
            c.writeUTF("afolder/../zfolder");
        }, (c) -> {
            c.writeInt(1);
            c.writeUTF("test3.txt");
            c.writeBoolean(false);
        });
    }

    @Test
    public void testFileContents() throws IOException {
        runTest((c) -> {
            c.writeInt(2);
            c.writeUTF("zfolder/test3.txt");
        }, (c) -> {
            final byte[] expected = "Contents of test3".getBytes();
            c.writeInt(expected.length);
            c.write(expected);
        });
    }

    @Test
    public void testListNonExistingDirectory() throws IOException {
        runTest((c) -> {
            c.writeInt(1);
            c.writeUTF("badfolder");
        }, (c) -> {
            c.writeInt(0);
        });
    }

    @Test
    public void testListFile() throws IOException {
        runTest((c) -> {
            c.writeInt(1);
            c.writeUTF("test1.txt");
        }, (c) -> {
            c.writeInt(0);
        });
    }

    @Test
    public void testGetNonExistingFile() throws IOException {
        runTest((c) -> {
            c.writeInt(2);
            c.writeUTF("badfile");
        }, (c) -> {
            c.writeInt(0);
        });
    }

    @Test
    public void testGetDirectory() throws IOException {
        runTest((c) -> {
            c.writeInt(2);
            c.writeUTF("zfolder");
        }, (c) -> {
            c.writeInt(0);
        });
    }
}
