import java.net.Socket;
import java.nio.channels.SocketChannel;

class MySession extends SocketSession{


    public MySession(Socket socket) {
        super(socket);
        this.addHandler(new SocketHandler() {

            @Override
            public void received(Object packet) {
                System.out.println("receive,send back");
                MySession.this.send(packet);
            }
        });
    }
}


