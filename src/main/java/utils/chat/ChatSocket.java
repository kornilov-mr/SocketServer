package utils.chat;

import utils.command.CommandFactory;
import utils.command.CommandRunner;

import java.io.IOException;
import java.net.ServerSocket;

//Same as MainResponse Socket
public class ChatSocket extends Thread{
    private final int port;

    private final CommandFactory commandFactory;
    private final CommandRunner commandRunner;
    private ServerSocket serverSocket;

    public ChatSocket(int port, CommandFactory commandFactory, CommandRunner commandRunner) {
        this.port = port;
        this.commandFactory = commandFactory;
        this.commandRunner = commandRunner;
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.start();

    }
    @Override
    public void run(){
        while (true){
            try {
                Thread.sleep(100);
                //Every time, when serverSocket receives connection new thread is created to run commands in parallel
                new ChatSocketHandler(serverSocket.accept(), commandFactory, commandRunner).run();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                return;
            }

        }
    }
    public void stopSocket() throws IOException {
        this.interrupt();
    }
}
