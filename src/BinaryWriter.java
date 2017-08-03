import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class BinaryWriter implements AutoCloseable {

    OutputStream out;
    boolean leaveOpen;

    public BinaryWriter(OutputStream out, boolean leaveOpen) {
        this.out = out;
        this.leaveOpen = leaveOpen;
    }

    public BinaryWriter(OutputStream out) {
        this(out, false);
    }

    public void writeBytes(byte[] bytes) {

        try {
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("write error", e);
        }
    }

    public void writeByte(byte value) {
        writeBytes(new byte[]{value});
    }

    public void writeShort(short value) {
        byte[] bytes = new byte[2];
        for (int i = 0; i < 2; i++) {
            bytes[i] = (byte) (value >>> i * 8 & 0xFF);
        }
        writeBytes(bytes);
    }

    public void writeInt(int value) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (value >>> i * 8 & 0xFF);
        }
        writeBytes(bytes);
    }

    public void writeLong(long value) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (value >>> i * 8 & 0xFF);
        }
        writeBytes(bytes);
    }

    public void writeFloat(Float value) {
        writeInt(Float.floatToIntBits(value));
    }

    public void writeDouble(double value) {
        writeLong(Double.doubleToLongBits(value));
    }

    public void writeUUID(UUID value) {
        writeLong(value.getMostSignificantBits());
        writeLong(value.getLeastSignificantBits());
    }

    public void writeBytesSection(byte[] bytes) {
        if (bytes == null) {
            writeInt(-1);
        } else {
            writeInt(bytes.length);
            writeBytes(bytes);
        }
    }

    public void writeStringSection(String s) {
        try {
            writeBytesSection(s == null ? null : s.getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void flush() {
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("flush error", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (!leaveOpen) {
            out.close();
        }
    }
}
