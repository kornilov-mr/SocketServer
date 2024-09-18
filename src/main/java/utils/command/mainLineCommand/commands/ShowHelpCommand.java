package utils.command.mainLineCommand.commands;

import app.HelpPage;
import org.json.JSONObject;
import utils.chat.ChatManager;
import utils.command.mainLineCommand.MainLineCommand;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;

public class ShowHelpCommand extends MainLineCommand {
    @Override
    public MainResponse run(ChatManager chatManager, SocketManager socketManager) {
        return new MainResponse(0,new HelpPage().toString(),"showHelpCommand");
    }

    @Override
    public String toJsonString() {
        JSONObject args = new JSONObject();
        JSONObject jsonString = new JSONObject();
        jsonString.put("args",args);
        jsonString.put("commandName","showHelpCommand");
        return jsonString.toString();
    }
}
