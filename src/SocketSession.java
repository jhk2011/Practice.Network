import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class SocketSession {

    protected Socket socket;

    SocketConverter converter;

    List<SocketHandler> handlers = new ArrayList<SocketHandler>();

    Thread thread;

    public SocketSession(Socket socket){
        this.socket=socket;
    }

    public void addHandler(SocketHandler handler){
        handlers.add(handler);
    }

    public void start(){

        if(thread!=null)throw new RuntimeException("already start");

        onStart();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                receive();
            }
        });

        thread.start();
    }

    private void receive() {
        while (true){
            try {

                System.out.println("start receive");

                Object packet = converter.read();

                System.out.println("receive ok");

                onReceived(packet);

            } catch (Exception e) {
               onClose();
               break;
            }
        }
    }

    protected void onReceived(Object packet){
        for (SocketHandler handler:handlers) {
            handler.received(packet);
        }
    }

    public void send(Object packet)
    {
        converter.write(packet);
    }

    protected void onClose(){
        for (SocketHandler handler:handlers) {
            handler.close();
        }
    }

    protected void onStart(){
        this.converter = new DefaultSocketConverter(this,null);
        for (SocketHandler handler:handlers) {
            handler.start();
        }
    }
}


