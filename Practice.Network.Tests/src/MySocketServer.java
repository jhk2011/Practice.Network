import java.net.Socket;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

class MySocketServer extends SocketServer{
    @Override
    protected SocketSession getSession(Socket socket) {
        return new MySession(socket);
    }
}

