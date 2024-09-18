package utils.command.mainLineCommand.commands;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.chat.Chat;
import utils.chat.ChatManager;
import utils.command.mainLineCommand.MainLineCommand;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;

import java.util.Map;

public class GetAllChatNamesCommand extends MainLineCommand {
    @Override
    public MainResponse run(ChatManager chatManager, SocketManager socketManager) {
        Map<Integer, Chat> chats = chatManager.getChats();
        String response = "";
        JSONArray data = new JSONArray();
        for(Integer port: chats.keySet()){
            Chat chat = chats.get(port);
            response+=chat.getName()+" with port:"+port+"\n";
            data.put(chat.toJsonOnlyArgs());
        }
        return new MainResponse(0,response,data,"getAllChatCommand");
    }

    @Override
    public String toJsonString() {
        JSONObject args = new JSONObject();
        JSONObject jsonString = new JSONObject();
        jsonString.put("args",args);
        jsonString.put("commandName","getAllChatCommand");
        return jsonString.toString();
    }
}
