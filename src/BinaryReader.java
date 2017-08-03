import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class BinaryReader implements AutoCloseable {

    InputStream in;
    boolean leaveOpen;

    public BinaryReader(InputStream in, boolean leaveOpen) {
        this.in = in;
        this.leaveOpen = leaveOpen;
    }

    public BinaryReader(InputStream in) {
        this(in, false);
    }

    public short readShort() {
        byte[] bytes = readBytes(2);

        short value = 0;

        for (int i = 0; i < 2; i++) {
            value |= ((short) (bytes[i] & 0xFF)) << (8 * i);
        }

        return value;
    }

    public int readInt() {
        byte[] bytes = readBytes(4);

        int value = 0;

        for (int i = 0; i < 4; i++) {
            value |= (bytes[i] & 0xFF) << (8 * i);
        }

        return value;
    }

    public long readLong() {
        byte[] bytes = readBytes(8);

        long value = 0;

        for (int i = 0; i < 8; i++) {
            value |= ((long) (bytes[i] & 0xFF)) << (8 * i);
        }

        return value;
    }

    public byte readByte() {
        byte[] bytes = readBytes(1);
        return bytes[0];
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public byte[] readBytes(int count) {
        byte[] bytes = new byte[count];
        int offset = 0;
        try {
            while (offset < count) {
                int n = in.read(bytes, offset, count-offset);
                if (n <= 0) throw new RuntimeException("read error eof");
                offset += n;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("read error", e);
        }
        return bytes;
    }

    void reverse(byte[] bytes, int start, int n) {

        for (int l = start, r = start + n - 1; l < r; l++, r--) {
            byte b = bytes[l];
            bytes[l] = bytes[r];
            bytes[r] = b;
        }
    }

    public UUID readUUID() {

        byte[] bytes = readBytes(16);

        assert bytes.length == 16 : "data must be 16 bytes in length";

        reverse(bytes, 0, 4);
        reverse(bytes, 4, 2);
        reverse(bytes, 6, 2);

        long msb = 0;
        long lsb = 0;

        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (bytes[i] & 0xff);

        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (bytes[i] & 0xff);

        return new UUID(msb, lsb);

        //return new UUID(readLong(),readLong());
    }

    public byte[] readBytesSection() {
        int n = readInt();
        if (n == -1) {
            return null;
        } else {
            return readBytes(n);
        }
    }


    public String readStringSection() {
        byte[] bytes = readBytesSection();
        if (bytes == null) return null;

        try {
            return new String(bytes, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (!leaveOpen) {
            in.close();
        }
    }
}
