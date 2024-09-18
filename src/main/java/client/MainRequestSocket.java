package client;

import org.json.JSONObject;
import server.mainSocket.MainResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//sends Command on MainResponseSocket on the server
public final class MainRequestSocket {
    private final String ip;
    private final int port;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    MainRequestSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;

    }

    public void startConnection() throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public MainResponse sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        JSONObject jsonObject = new JSONObject(resp);
        MainResponse response = new MainResponse(jsonObject);
        return response;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
