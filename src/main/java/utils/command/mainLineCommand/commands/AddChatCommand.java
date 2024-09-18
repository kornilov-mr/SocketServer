package utils.command.mainLineCommand.commands;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.chat.ChatManager;
import utils.command.mainLineCommand.MainLineCommand;
import utils.exceptions.TransmissionException;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;

public class AddChatCommand extends MainLineCommand {
    private final int port;
    private final String name;

    public AddChatCommand(int port, String name) {
        this.name = name;
        this.port = port;
    }

    @Override
    public MainResponse run(ChatManager chatManager, SocketManager socketManager) {
        try {
            chatManager.addChat(name,port);
        }catch (TransmissionException e){
            return new MainResponse(e);
        }
        JSONArray data = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("port", port);
        data.put(jsonObject);
        return new MainResponse(0,"New chat with port: "+port+" has opened.",data,"addNewChat");
    }

    @Override
    public String toJsonString() {
        JSONObject args = new JSONObject();
        args.put("port",port);
        args.put("name",name);
        JSONObject jsonString = new JSONObject();
        jsonString.put("args",args);
        jsonString.put("commandName","addNewChat");
        return jsonString.toString();
    }
}
