import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class ByteArrayView{
    byte[] bytes;
    int offset;
    int size;
}

class ByteArrayContainer {

    byte[] bytes;

    public void add(byte[] bytes){

        if(bytes==null||bytes.length==0)return;

        if(this.bytes==null){
            this.bytes=bytes;
        }else{
            byte[] newBytes = new byte[this.bytes.length+bytes.length];
            for (int i=0;i<this.bytes.length;i++){
                newBytes[i]=this.bytes[i];
            }
            for (int i=0;i<bytes.length;i++){
                newBytes[this.bytes.length+i]=bytes[i];
            }
            this.bytes=newBytes;
        }
    }

    public byte[] take(int n){
        byte[] take = new byte[n];
        for (int i=0;i<n;i++){
            take[i]=bytes[i];
        }
        return take;
    }

    public void remove(int n){
        byte[] newBytes = new byte[bytes.length-n];
        for (int i=0;i<bytes.length-n;i++){
            newBytes[i]=bytes[n+i];
        }
        bytes=newBytes;
    }

    public int size(){
        return bytes.length;
    }
}


class  AsyncSocketConverter implements SocketConverter{

    AsyncSocketSession session;

    SocketChannel channel;

    ByteBuffer buffer = ByteBuffer.allocate(2048);

    ByteArrayContainer container = new ByteArrayContainer();

    public AsyncSocketConverter(AsyncSocketSession session){
        this.session=session;
        this.channel = session.channel;
        if(serializer==null) {
            serializer = new DefaultSerializer();
        }
    }

    @Override
    public SocketSession getSession() {
        return session;
    }

    @Override
    public Object read() {

        try {

            System.out.println("read object");

            int n = channel.read(buffer);

            if(n < 0){
                channel.close();
                throw new RuntimeException("close");
            }

            buffer.flip();

            byte[] bytes = new byte[buffer.limit()];

            buffer.get(bytes);

            buffer.clear();

            container.add(bytes);

            while(container.size()>4){
                bytes = container.take(4);
                int length = new BinaryReader(new ByteArrayInputStream(bytes)).readInt();
                if(container.size()-4<length) {
                    return null;
                }else{
                    container.remove(4);
                    bytes=container.take(length);
                    Object obj = deserialize(bytes);
                    return obj;
                }
            }

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(Object packet) {
        byte[] buffer = serialize(packet);

        ByteBuffer b = ByteBuffer.allocate(buffer.length+4);

        b.putInt(buffer.length);
        b.put(buffer);

        try {
            channel.write(b);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    Serializer serializer;


    private Object deserialize(byte[] bytes){
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return serializer.deserialize(in);
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
