import java.io.*;

public interface Serializer {
    void serialize(OutputStream out,Object obj);
    Object deserialize(InputStream in);
}


