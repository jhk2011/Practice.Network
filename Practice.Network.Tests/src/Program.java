import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Program {

    public static void main(String[] args) throws IOException, InterruptedException {

        startServer();

        Thread.sleep(200);

        startClient();

        System.in.read();
    }

    private static void startServer(){
        SocketServer server = new SocketServer();

        server.initialize(1899,100);
        server.start();
    }

    private static void startClient() {
        SocketClient client = new SocketClient();

        client.addHandler(new SocketHandler() {
            @Override
            public void close() {
                System.out.println("断开连接");
            }

            @Override
            public void received(Object packet) {
                client.send(packet);
            }
        });

        client.connect("127.0.0.1",1899);
        //client.connect("www.baidu.com",80);
        client.start();
        client.send("hello");
    }
}


