package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkUtil {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;

    public NetworkUtil(Socket socket) throws IOException {
        this.socket = socket;
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    public NetworkUtil(String s, int port) throws IOException {
        this.socket = new Socket(s, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public Object read() throws ClassNotFoundException, IOException {
        return ois.readUnshared();
    }

    public void write(Object o) throws IOException, ClassNotFoundException {
        oos.writeUnshared(o);
    }
}
