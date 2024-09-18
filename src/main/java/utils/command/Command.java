package utils.command;

import utils.chat.ChatManager;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;

public interface Command {
    MainResponse run(ChatManager chatManager, SocketManager socketManager);
    String toJsonString();
}
