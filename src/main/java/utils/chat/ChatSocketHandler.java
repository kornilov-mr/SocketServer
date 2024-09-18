package utils.chat;

import org.json.JSONObject;
import utils.command.Command;
import utils.command.CommandFactory;
import utils.command.CommandRunner;
import utils.command.mainLineCommand.commands.CloseSocketCommand;
import server.mainSocket.MainResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//Same as MainResponseHandler
public class ChatSocketHandler extends Thread{
    private final Socket clientSocket;
    private final CommandFactory commandFactory;
    private final CommandRunner commandRunner;
    //PrintWriter to write response to a client
    private PrintWriter out;
    //BufferedReader to receive requests from a client
    private BufferedReader in;

    ChatSocketHandler(Socket socket, CommandFactory commandFactory, CommandRunner commandRunner) {
        this.clientSocket = socket;
        this.commandFactory = commandFactory;
        this.commandRunner = commandRunner;
    }
    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(true){

                //receives a command from a client in json format
                //then creates a command from the json received
                String inputLine= in.readLine();
                JSONObject jsonObject = new JSONObject(inputLine);

                Command mainLineCommand =null;
                try{
                    mainLineCommand = commandFactory.createCommandFromJson(jsonObject);
                }catch (RuntimeException e){
                    out.println(new MainResponse(e).toJsonString());
                    continue;
                }

                if (mainLineCommand instanceof CloseSocketCommand){
                    break;
                }
                //runs command in commandRunner environment
                MainResponse response = commandRunner.runCommand(mainLineCommand);
                out.println(response.toJsonString());

            }
            //closes all Streams and the socket

            out.println(new MainResponse(0,"chat socket closed").toJsonString());
            in.close();
            out.close();
            clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        run();
    }
}
