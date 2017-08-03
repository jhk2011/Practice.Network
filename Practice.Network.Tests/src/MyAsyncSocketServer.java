import java.nio.channels.SocketChannel;

class MyAsyncSocketServer extends  AsyncSocketServer{

    @Override
    protected AsyncSocketSession getSession(SocketChannel channel) {
        return new MyAsyncSession(channel);
    }

}
