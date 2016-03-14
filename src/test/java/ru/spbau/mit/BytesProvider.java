package ru.spbau.mit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface BytesProvider {
    void writeTo(DataOutputStream s) throws IOException;

    static byte[] getBytes(BytesProvider provider) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(buffer);
        provider.writeTo(stream);
        buffer.close();
        return buffer.toByteArray();
    }
}
