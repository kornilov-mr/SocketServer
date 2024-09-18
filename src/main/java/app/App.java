package app;

import app.command.ConsoleCommandFactory;
import client.Client;
import utils.command.Command;
import utils.command.chatCommand.ChatCommand;
import utils.command.mainLineCommand.MainLineCommand;
import utils.command.mainLineCommand.commands.CloseSocketCommand;
import utils.exceptions.TransmissionException;
import server.mainSocket.MainResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
//Client wrapper with console input
public class App implements Runnable{
    //Object, that sends tcp requests
    private final Client client;
    private final String ip;
    //Object for visualization of received data from server
    private final ConsoleWriter consoleWriter;
    //Arguments, that are being stored inside App Class for example (userName, most recent chat's port)
    private final Map<String, Object> hiddenArgs = new HashMap<>();
    //Object, which create A command based on input console line
    private final ConsoleCommandFactory commandFactory;
    //Reads System.in
    private final BufferedReader bufferedReader;
    public App(String ip){
        this(ip, System.out);
    }
    public App(String ip, OutputStream outputStream) {
        this.client=new Client(ip);
        this.consoleWriter=new ConsoleWriter(outputStream);
        this.ip=ip;
        this.commandFactory=new ConsoleCommandFactory();
        this.bufferedReader=new BufferedReader(new InputStreamReader(System.in));
    }
    //display greeting message
    private void start(){
        consoleWriter.printStartSequence();

        try {
            String name = bufferedReader.readLine();
            hiddenArgs.put("userName",name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        consoleWriter.writeString("Starting the client...");
        client.startClient();

        consoleWriter.writeString("");
        consoleWriter.writeString("type 'help'  For Information about commands");
    }
    public void run(){
        start();
        while(true){
            try {

                //Reads line from console and creates command based on it
                String line = bufferedReader.readLine();
                Command command =null;
                try {
                    command = commandFactory.getCommandFromConsoleLine(line,hiddenArgs);
                } catch (IllegalArgumentException e){
                    consoleWriter.writeException(e);
                    continue;
                }

                //sends on the MainLineSocket or on a ChatSocket
                MainResponse response = null;
                if(command instanceof CloseSocketCommand){
                    response=client.sendCommand(command);
                    consoleWriter.writeResponse(response);
                    return;
                }
                if(command instanceof ChatCommand){
                    ChatCommand chatCommand = (ChatCommand) command;
                    try {
                        response = client.sendChatCommand(chatCommand,chatCommand.getPort());
                    } catch (TransmissionException e){
                        consoleWriter.writeException(e);
                        continue;
                    }
                }
                if(command instanceof MainLineCommand){
                    response=client.sendCommand(command);
                }
                consoleWriter.writeResponse(response);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
