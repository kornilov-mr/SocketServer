package utils.command.chatCommand.command;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.chat.ChatManager;
import utils.chat.messages.Message;
import utils.command.chatCommand.ChatCommand;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;

import java.util.ArrayList;

public class GetAllMessagesFromChatCommand extends ChatCommand {
    public GetAllMessagesFromChatCommand(int port) {
        super(port);
    }

    @Override
    public MainResponse run(ChatManager chatManager, SocketManager socketManager) {
        ArrayList<Message> messages = chatManager.getChats().get(port).getAllMessages();
        JSONArray data = new JSONArray();
        for(Message message: messages){
            data.put(message.toJson());
        }
        return new MainResponse(0,"chat with port "+port+" is opened",data,"getAllMessagesFromChat");
    }

    @Override
    public String toJsonString() {
        JSONObject args = new JSONObject();
        args.put("port",port);
        JSONObject jsonString = new JSONObject();
        jsonString.put("args",args);
        jsonString.put("commandName","getAllMessagesFromChat");
        return jsonString.toString();
    }
}
