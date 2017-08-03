import java.net.Socket;

class MySocketServer extends SocketServer{
    @Override
    protected SocketSession getSession(Socket socket) {
        return new MySession(socket);
    }
}
