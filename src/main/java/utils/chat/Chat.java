package utils.chat;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.chat.messages.Message;

import java.io.IOException;
import java.util.ArrayList;

public class Chat {
    private final ChatSocket chatSocket;
    private final String name;
    private final int port;
    private final ArrayList<Message> messages= new ArrayList<>();

    public Chat(int port, String name, ChatSocket chatSocket) {
        this.port=port;
        this.name = name;
        this.chatSocket= chatSocket;
    }
    public final void addMessage(Message message){
        messages.add(message);
    }

    public void openChat(){
        chatSocket.start();
    }
    public String getName() {
        return name;
    }
    public void closeChat(){
        try {
            chatSocket.stopSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Message> getAllMessages(){
        return messages;
    }
    public JSONObject toJsonOnlyArgs(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("port",port);
        jsonObject.put("name",name);
        return jsonObject;
    }
    public JSONObject toJsonForSaving(){
        JSONObject args = toJsonOnlyArgs();
        JSONArray messagesJson = new JSONArray();
        for(Message message : messages){
            messagesJson.put(message.toJson());
        }
        args.put("messages",messagesJson);
        return args;
    }

    public int getPort() {
        return port;
    }
}
