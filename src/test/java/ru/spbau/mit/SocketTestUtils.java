package ru.spbau.mit;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public final class SocketTestUtils {
    private SocketTestUtils() {}

    static void checkSocketIO(BytesProvider toSend, Consumer<Socket> testedCode, BytesProvider toReceive)
            throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(BytesProvider.getBytes(toSend));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(in);
        when(socket.getOutputStream()).thenReturn(out);
        testedCode.accept(socket);
        verify(socket, times(1)).close();

        byte[] expected = BytesProvider.getBytes(toReceive);
        assertArrayEquals(expected, out.toByteArray());
    }
}
