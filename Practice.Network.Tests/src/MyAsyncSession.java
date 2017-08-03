import java.nio.channels.SocketChannel;

class MyAsyncSession extends AsyncSocketSession{

    public MyAsyncSession(SocketChannel channel) {
        super(channel);
        this.addHandler(new SocketHandler() {

            @Override
            public void received(Object packet) {
                System.out.println("receive,send back");
                MyAsyncSession.this.send(packet);
            }
        });
    }
}
