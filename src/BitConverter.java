public class BitConverter {

    public int toInt(byte[] bytes){

        if(bytes.length<4) {
            throw new IllegalArgumentException("bytes");
        }

        int value=0;

        for(int i=0;i<4;i++){
            value|=(bytes[i]&0xFF)<<i*8;
        }
        return value;
    }

    public short toShort(byte[] bytes){

        if(bytes.length<2) {
            throw new IllegalArgumentException("bytes");
        }

        short value=0;

        for(int i=0;i<2;i++){
            value|=(bytes[i]&0xFF)<<i*8;
        }
        return value;
    }

    public long toLong(byte[] bytes){

        if(bytes.length<2) {
            throw new IllegalArgumentException("bytes");
        }

        long value=0;

        for(int i=0;i<2;i++){
            value|=(long)((bytes[i]&0xFF)<<i*8);
        }
        return value;
    }

    public byte[] getBytes(int value){
        byte[] bytes = new byte[4];
        for(int i=0;i<4;i++){
            bytes[i]=(byte)(value>>>i*8&0xFF);
        }
        return bytes;
    }

    public byte[] getBytes(short value){
        byte[] bytes = new byte[2];
        for(int i=0;i<2;i++){
            bytes[i]=(byte)(value>>>i*8&0xFF);
        }
        return bytes;
    }

    public byte[] getBytes(long value){
        byte[] bytes = new byte[8];
        for(int i=0;i<8;i++){
            bytes[i]=(byte)(value>>>i*8&0xFF);
        }
        return bytes;
    }
}
