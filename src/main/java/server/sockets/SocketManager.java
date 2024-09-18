package server.sockets;

import utils.chat.ChatManager;
import utils.chat.ChatSocket;
import utils.command.CommandFactory;
import utils.command.CommandRunner;
import utils.exceptions.TransmissionException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketManager {
    private final CommandFactory commandFactory;
    private final Map<Integer, ChatSocket> sockets = new HashMap<>();

    public SocketManager(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    //opens a socket if possible
    public ChatSocket openNewSocket(int port, ChatManager chatManager) {
        //check if socket isn't already in use
        if (!available(port)) {
            throw new TransmissionException(0, "The port is already opened on the server");
        }
        //creates CommandRunner for ChatSocket (separated Thread)
        CommandRunner commandRunner = new CommandRunner(this, chatManager);
        ChatSocket serverSocket = new ChatSocket(port, commandFactory, commandRunner);
        sockets.put(port, serverSocket);
        return serverSocket;

    }

    private boolean available(int port) throws IllegalStateException {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (ConnectException e) {
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Error while trying to check open port", e);
        }
    }

    public Map<Integer, ChatSocket> getSockets() {
        return sockets;
    }


}
