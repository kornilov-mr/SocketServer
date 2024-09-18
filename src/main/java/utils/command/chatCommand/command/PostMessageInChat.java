package utils.command.chatCommand.command;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.chat.ChatManager;
import utils.chat.messages.Message;
import utils.command.chatCommand.ChatCommand;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;

public class PostMessageInChat extends ChatCommand {
    private final int chatPort;
    private final Message message;
    private final String userName;

    public PostMessageInChat(int chatPort, Message message,String userName) {
        super(chatPort);
        this.chatPort = chatPort;
        this.message = message;
        this.userName=userName;
    }

    @Override
    public MainResponse run(ChatManager chatManager, SocketManager socketManager) {
        chatManager.getChats().get(chatPort).addMessage(message);
        JSONArray data = new JSONArray();
        for(Message message: chatManager.getChats().get(chatPort).getAllMessages()){
            data.put(message.toJson());
        }
        return new MainResponse(0,"Message sent",data,"postMessageInChat");
    }

    @Override
    public String toJsonString() {
        JSONObject args = new JSONObject();
        args.put("port",chatPort);
        args.put("message",message.toJson());
        args.put("userName",userName);
        JSONObject jsonString = new JSONObject();
        jsonString.put("args",args);
        jsonString.put("commandName","postMessageInChat");
        return jsonString.toString();
    }
}
