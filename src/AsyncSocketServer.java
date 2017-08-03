import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class AsyncSocketServer {

    List<SocketSession> sessions = Collections.synchronizedList(new ArrayList<SocketSession>());

    ServerSocket socket;

    Selector selector;

    ServerSocketChannel channel;

    ByteBuffer buffer = ByteBuffer.allocate(1024);

    public void initialize(int port,int backlog){
        try {

            /*初始化一个Selector*/
            selector = Selector.open();
           /*打开通道*/
            channel = ServerSocketChannel.open();
            /*非阻塞模式*/
            channel.configureBlocking(false);

            socket = channel.socket();

            System.out.println(InetAddress.getLocalHost());

            socket.bind(new InetSocketAddress("0.0.0.0",port));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void start(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        accept();
                    }catch (Exception ex ){
                        throw new RuntimeException(ex);
                    }
                }
            }).start();
    }

    private void accept() throws Exception {

    /*注册接收事件*/
        channel.register(selector, SelectionKey.OP_ACCEPT);

        /*无限循环*/
        while (selector.select()>0) {

            System.out.println("select ok");


            /*轮询事件*/
            Iterator iter = selector.selectedKeys().iterator();

            while (iter.hasNext()) {

                SelectionKey key =  (SelectionKey)iter.next();

                iter.remove();

                /*事件分类处理*/
                if (key.isAcceptable()) {

                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();

                    SocketChannel channel = serverSocketChannel.accept();

                    System.out.println("新终端已连接:"+ channel.getRemoteAddress());


                    channel.configureBlocking(false);

                    channel.register(selector, SelectionKey.OP_READ);

                    AsyncSocketSession session = getSession(channel);

                    session.channel = channel;

                    sessions.add(session);

                    session.start();
                }
                else if (key.isReadable()) {

                    SocketChannel channel = (SocketChannel)key.channel();

                    for(SocketSession session:sessions){

                        AsyncSocketSession asyncSocketSession = (AsyncSocketSession)session;

                        if(asyncSocketSession.channel==channel){
                            asyncSocketSession.receive();
                        }
                    }

                } else {

                }
            }
        }
    }

    protected AsyncSocketSession getSession(SocketChannel channel) {
        return new AsyncSocketSession(channel);
    }


    protected SocketSession getSession(Socket socket){
        return new SocketSession(socket);
    }

    protected void onAccept(SocketSession session){

    }
}
