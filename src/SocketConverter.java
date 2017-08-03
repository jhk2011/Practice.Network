interface SocketConverter{
    SocketSession getSession();
    Object read();
    void write(Object packet);
}
