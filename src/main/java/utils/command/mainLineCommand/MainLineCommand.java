package utils.command.mainLineCommand;

import utils.chat.ChatManager;
import utils.command.Command;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;

public abstract class MainLineCommand implements Command {
    public abstract MainResponse run(ChatManager chatManager, SocketManager socketManager);
    public abstract String toJsonString();

}
