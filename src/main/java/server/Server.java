package server;

import utils.chat.ChatManager;
import utils.command.CommandFactory;
import utils.command.CommandRunner;
import server.mainSocket.MainResponseSocket;
import server.sockets.SocketManager;

import java.io.IOException;

public class Server {

    //Socket, that receives main line command
    private final MainResponseSocket mainSocket;
    //Object for opening and tracking open sockets
    private final SocketManager socketManager;
    //Object for opening and tracking open chats;
    private final ChatManager chatManager;
    //Object environment with chatManager and socketManager to run Command interface
    private final CommandRunner commandRunner;
    //Factory to create Commands from arguments and command name provided in json
    private final CommandFactory commandFactory;

    public Server(){
        this(true);
    }
    public Server(boolean saving) {
        this.commandFactory = new CommandFactory();
        this.socketManager= new SocketManager(commandFactory);
        this.chatManager = new ChatManager(socketManager,commandFactory,saving);
        this.commandRunner= new CommandRunner(socketManager,chatManager);
        this.mainSocket = new MainResponseSocket(ServerConfig.mainSocketPort, commandFactory, commandRunner);
    }

    public void startServer(){
        mainSocket.start();
    }
    public void stopServer(){
        try {
            mainSocket.stopSocket();
            chatManager.closeChats();
        } catch (IOException e) {
            throw new RuntimeException("a problem starting main response socket",e);
        }
    }
}
