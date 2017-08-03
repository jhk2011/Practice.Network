import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class AsyncSocketSession extends SocketSession{

    SocketChannel channel;

    ByteBuffer buffer = ByteBuffer.allocate(100);

    public AsyncSocketSession(SocketChannel channel){
        super(null);
        this.channel=channel;
    }

    @Override
    protected void onStart() {
        converter = new AsyncSocketConverter(this);
    }

    @Override
    public void start() {
        onStart();
    }

    void receive() throws Exception{

       Object obj= converter.read();

       while (obj!=null){
           onReceived(obj);
           obj = converter.read();
       }

       /*

        int n = channel.read(buffer);

        if (n > 0) {
            byte[] arr = buffer.array();
            onReceived(arr);
            System.out.println(channel.getRemoteAddress() + "发来数据: "+ new String(arr));
            buffer.flip();
        }
        else {
            channel.close();
            onClose();
        }
        buffer.clear();
        */
    }
}
