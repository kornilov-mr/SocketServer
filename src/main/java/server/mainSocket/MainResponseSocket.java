package server.mainSocket;

import utils.command.CommandFactory;
import utils.command.CommandRunner;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class MainResponseSocket extends Thread{
    private final int port;

    private final CommandFactory commandFactory;
    private final CommandRunner commandRunner;
    private ServerSocket serverSocket;
    //Threads collection, that runs commands in parallel
    private final ArrayList<MainResponseHandler> handlers = new ArrayList<>();

    public MainResponseSocket(int port, CommandFactory commandFactory, CommandRunner commandRunner) {
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
                Thread.sleep(10);
                //Every time, when serverSocket receives connection new thread is created to run commands in parallel
                MainResponseHandler handler = new MainResponseHandler(serverSocket.accept(), commandFactory, commandRunner);
                handler.run();

            } catch (IOException | InterruptedException e) {
                return;
            }
        }
    }
    public void stopSocket() throws IOException {
        for(MainResponseHandler handler: handlers){
            handler.stopThread();
        }
        serverSocket.close();
        this.interrupt();
    }
}
