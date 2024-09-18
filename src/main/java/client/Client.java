package client;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.command.Command;
import utils.command.chatCommand.ChatCommand;
import utils.command.mainLineCommand.commands.CloseSocketCommand;
import utils.command.mainLineCommand.commands.GetAllChatNamesCommand;
import utils.exceptions.TransmissionException;
import server.mainSocket.MainResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Client {
    //Main line client socket, which sends main line Command to server's MainResponseSocket
    private final MainRequestSocket mainRequestSocket;
    private final String ip;
    //Contains all opened chats
    private Map<Integer,ChatSocket> chatSockets= new HashMap<>();

    public Client(String ip) {
        this.ip=ip;
        this.mainRequestSocket = new MainRequestSocket(ip, ClientConfig.mainSocketPort);
    }

    public void startClient() {
        try {
            mainRequestSocket.startConnection();
        } catch (IOException e) {
            throw new RuntimeException("a problem starting main request socket", e);
        }
        this.chatSockets=getAllChatsMap();
    }
    //receives json with information about all opened chats, and creates map based on it
    private  Map<Integer, ChatSocket> getAllChatsMap(){
        Map<Integer,ChatSocket> chatSockets= new HashMap<>();
        MainResponse mainResponse = sendCommand(new GetAllChatNamesCommand());
        JSONArray data = mainResponse.getData();
        for(int i =0; i<data.length();i++){
            JSONObject chatJson= (JSONObject) data.get(i);
            int port = chatJson.getInt("port");
            ChatSocket chatSocket = new ChatSocket(ip,port);
            try {
                chatSocket.startConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            chatSockets.put(port, chatSocket);
        }
        return chatSockets;
    }
    //send Command on MainResponse socket on server
    public MainResponse sendCommand(Command mainLineCommand) {
        MainResponse response = null;
        try {
            response = mainRequestSocket.sendMessage(mainLineCommand.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException("a problem sending command on main request socket", e);
        }
        return response;
    }

    //stops Thread on server, which had connection to client
    public MainResponse stopClient(){
        return sendCommand(new CloseSocketCommand());
    }
    //Send ChatCommand on ChatSocket based on the port
    public MainResponse sendChatCommand(ChatCommand chatCommand, int port){
        this.chatSockets=getAllChatsMap();

        MainResponse mainResponse;
        try {
            if(!chatSockets.containsKey(port)){
                throw  new TransmissionException(1,"chat with this port doesn't exist");
            }
            mainResponse=chatSockets.get(port).sendMessage(chatCommand.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mainResponse;
    }
}
