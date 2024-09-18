package utils.command.mainLineCommand.commands;

import org.json.JSONObject;
import utils.chat.ChatManager;
import utils.command.mainLineCommand.MainLineCommand;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;

public class CloseSocketCommand extends MainLineCommand {
    @Override
    public MainResponse run(ChatManager chatManager, SocketManager socketManager) {
        chatManager.closeChats();
        return new MainResponse(0, "All sockets are closed","closeSocketCommand");
    }

    @Override
    public String toJsonString() {
        JSONObject args = new JSONObject();
        JSONObject jsonString = new JSONObject();
        jsonString.put("args",args);
        jsonString.put("commandName","closeSocketCommand");
        return jsonString.toString();
    }
}
