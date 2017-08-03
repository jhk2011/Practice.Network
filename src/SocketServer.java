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

    Thread thread;

    public void initialize(int port,int backlog){
        try {
            socket = new ServerSocket(port,backlog);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void start(){

        if(thread!=null)throw  new RuntimeException("already start");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                accept();
            }
        });

        thread.start();
    }

    private void accept() {
        while (true){
            try {

                System.out.println("accept");

                Socket client= socket.accept();

                System.out.println("accept ok:"+ client.getRemoteSocketAddress());

                SocketSession session = getSession(client);

                sessions.add(session);

                onAccept(session);

                session.start();

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    protected SocketSession getSession(Socket socket){
        return new SocketSession(socket);
    }

    protected void onAccept(SocketSession session){

    }
}

