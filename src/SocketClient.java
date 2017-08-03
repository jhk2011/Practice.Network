import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

class SocketClient extends SocketSession{


    public SocketClient() {
        super(null);
    }

    public void connect(String host, int port)  {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host,port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
