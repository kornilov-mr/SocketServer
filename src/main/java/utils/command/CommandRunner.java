package utils.command;

import utils.chat.ChatManager;
import server.mainSocket.MainResponse;
import server.sockets.SocketManager;

public class CommandRunner {
    private final SocketManager socketManager;
    private final ChatManager chatManager;

    public CommandRunner(SocketManager socketManager, ChatManager chatManager) {
        this.socketManager = socketManager;
        this.chatManager = chatManager;
    }
    public MainResponse runCommand(Command command){
        return command.run(chatManager,socketManager);
    }
}
