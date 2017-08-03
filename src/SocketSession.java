import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class  SocketSession{

    protected Socket socket;

    SocketConverter converter;

    List<SocketHandler> handlers = new ArrayList<SocketHandler>();

    public SocketSession(){
        this(null);
    }

    public SocketSession(Socket socket){
        this.socket=socket;
    }

    public void addHandler(SocketHandler handler){

        handlers.add(handler);
    }

    public void start(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    try {

                        Object packet = converter.read();

                        if(packet==null){
                            onClose();
                            break;
                        }

                        onReceived(packet);

                    } catch (Exception e) {
                        break;
                    }
                }

            }
        });

        thread.start();
    }

    protected void onReceived(Object packet){
        for (SocketHandler handler:handlers) {
            handler.received(packet);
        }
        //write(packet);
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

}
