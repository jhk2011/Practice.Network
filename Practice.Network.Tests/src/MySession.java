import java.net.Socket;

class MySession extends SocketSession{

    public MySession() {
        this.addHandler(new SocketHandler() {
            @Override
            public void close() {

            }

            @Override
            public void received(Object packet) {
                System.out.println("receive,send back");
                MySession.this.send(packet);
            }
        });
    }

    public MySession(Socket socket) {
        super(socket);
    }
}
