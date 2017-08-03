import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Program {

    public static void main(String[] args) throws IOException, InterruptedException {

        //startServer();

        startAsyncServer();

        Thread.sleep(200);

        startClient();

        System.in.read();
    }

    private static void startServer(){

        MySocketServer server = new MySocketServer();

        server.initialize(1899,100);
        server.start();
    }

    private static void startAsyncServer(){
        MyAsyncSocketServer server = new MyAsyncSocketServer();

        server.initialize(1899,100);
        server.start();
    }

    private static void startClient() {
        SocketClient client = new SocketClient();

        client.addHandler(new SocketHandler() {
            @Override
            public void received(Object packet) {
                System.out.println("client received:"+packet);
            }
        });

        client.connect("127.0.0.1",1899);
        //client.connect("www.baidu.com",80);
        client.start();

        client.send("hello");
    }
}


