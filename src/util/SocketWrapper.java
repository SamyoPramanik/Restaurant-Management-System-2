package util;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketWrapper {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public SocketWrapper(Socket socket) throws Exception {
        System.out.println("doneSocket");

        this.socket = socket;
        System.out.println("doneSocket");

        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("doneSocket");

        oos = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("done");
    }

    public SocketWrapper(String ip, int port) throws Exception {
        this.socket = new Socket(ip, port);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    public Object read() throws Exception {
        return ois.readUnshared();
    }

    public void write(Object obj) throws Exception {
        oos.writeUnshared(obj);
    }
}
