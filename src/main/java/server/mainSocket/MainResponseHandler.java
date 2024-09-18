package server.mainSocket;

import org.json.JSONObject;
import utils.command.Command;
import utils.command.CommandFactory;
import utils.command.CommandRunner;
import utils.command.mainLineCommand.commands.CloseSocketCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainResponseHandler extends Thread {
    private final Socket clientSocket;
    private final CommandFactory commandFactory;
    private final CommandRunner commandRunner;
    //PrintWriter to write response to a client
    private PrintWriter out;
    //BufferedReader to receive requests from a client
    private BufferedReader in;


    public MainResponseHandler(Socket socket, CommandFactory commandFactory, CommandRunner commandRunner) {
        this.clientSocket = socket;
        this.commandFactory = commandFactory;
        this.commandRunner = commandRunner;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                Thread.sleep(10);


                //receives a command from a client in json format
                //then creates a command from the json received
                String inputLine = null;
                try {
                    inputLine = in.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                JSONObject jsonObject = new JSONObject(inputLine);
                Command command = null;
                try {
                    command = commandFactory.createCommandFromJson(jsonObject);
                } catch (IllegalArgumentException e) {
                    out.println(new MainResponse(e).toJsonString());
                    continue;
                }

                //runs command in commandRunner environment
                MainResponse response = commandRunner.runCommand(command);
                out.println(response.toJsonString());
                if (command instanceof CloseSocketCommand) {
                    break;
                }
            } catch (InterruptedException e) {
                return;
            }
        }
        try {
            //closes all Streams and the socket
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void stopThread() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.interrupt();
    }
}
