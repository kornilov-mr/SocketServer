package utils.command.chatCommand;

import utils.chat.ChatManager;
import utils.command.Command;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;
//Command for ChatSocket
public abstract class ChatCommand implements Command {
    protected final int port;

    protected ChatCommand(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public abstract MainResponse run(ChatManager chatManager, SocketManager socketManager);
    public abstract String toJsonString();
}
