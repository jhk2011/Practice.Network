import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SocketServer {
    List<SocketSession> sessions = Collections.synchronizedList(new ArrayList<SocketSession>());

    ServerSocket socket;

    Selector selector;

    ServerSocketChannel channel;


    public void initialize(int port,int backlog){
        try {
            socket = new ServerSocket(port,backlog);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Socket client= socket.accept();

                        SocketSession session = getSession(client);

                        sessions.add(session);

                        session.start();

                        onAccept(session);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }
            }
        });
        thread.start();
    }

    protected SocketSession getSession(Socket socket){
        return new SocketSession(socket);
    }

    protected void onAccept(SocketSession session){
        System.out.println("accept");
    }

}
