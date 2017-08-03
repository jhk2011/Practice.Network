import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;

import java.io.*;
import java.nio.channels.SocketChannel;


class DefaultSocketConverter implements  SocketConverter{

    InputStream in;
    OutputStream out;
    SocketSession session;
    Serializer serializer;

    BinaryWriter writer;
    BinaryReader reader;

    public DefaultSocketConverter(SocketSession session,Serializer serializer){

        this.session = session;

        if(serializer==null){
            serializer = new DefaultSerializer();
        }

        this.serializer = serializer;

        try {

            this.in = session.socket.getInputStream();
            this.out = session.socket.getOutputStream();
            this.reader = new BinaryReader(in);
            this.writer = new BinaryWriter(out);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public SocketSession getSession() {
        return session;
    }

    @Override
    public Object read() {

        int length = reader.readInt();

        System.out.println("read length");

        byte[] bytes = reader.readBytes(length);

        Object obj= deserialize(bytes);

        System.out.println("read object:"+obj.getClass().getName());

        return obj;
    }

    private Object deserialize(byte[] bytes){
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return serializer.deserialize(in);
    }


    @Override
    public void write(Object packet) {

        byte[] buffer =serialize(packet);

        int length = buffer.length;

        writer.writeInt(length);
        writer.writeBytes(buffer);
        writer.flush();

        System.out.println("write object:"+packet.getClass().getName());
    }

    private byte[] serialize(Object obj){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serializer.serialize(out,obj);
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return out.toByteArray();
    }
}
