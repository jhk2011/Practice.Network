import java.io.*;

public class DefaultSerializer implements  Serializer {

    @Override
    public void serialize(OutputStream out, Object obj) {

        try {
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            throw  new SerializeException("write object error",e);
        }
    }

    @Override
    public Object deserialize(InputStream in) {

        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream(in);
            return oin.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw  new SerializeException("read object error",e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw  new SerializeException("read object error,class not found",e);
        }
    }
}
