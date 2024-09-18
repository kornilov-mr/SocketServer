import app.App;
import app.HelpPage;
import client.Client;
import org.junit.Assert;
import org.junit.Test;
import server.Server;
import server.mainSocket.MainResponse;
import utils.chat.messages.Message;
import utils.command.chatCommand.command.PostMessageInChat;
import utils.command.mainLineCommand.commands.AddChatCommand;
import utils.command.mainLineCommand.commands.GetAllChatNamesCommand;
import utils.command.mainLineCommand.commands.ShowHelpCommand;

import java.util.Objects;

public class Tests {

    @Test
    public void exitTest(){
        Server server = new Server(false);
        server.startServer();

        Client client = new Client("127.0.0.1");
        client.startClient();
        MainResponse response = client.stopClient();

        server.stopServer();
        Assert.assertTrue(Objects.equals("All sockets are closed", response.getInfo()));
        System.out.println();
    }
    @Test
    public void helpCommandTest(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Server server = new Server(false);
        server.startServer();

        Client client = new Client("127.0.0.1");
        client.startClient();
        MainResponse response = client.sendCommand(new ShowHelpCommand());

        server.stopServer();

        Assert.assertTrue(Objects.equals(new HelpPage().toString(), response.getInfo()));
    }
    @Test
    public void createChatCommandTest(){
        int port =7781;
        String chatName = "test";
        Server server = new Server(false);
        server.startServer();

        Client client = new Client("127.0.0.1");
        client.startClient();
        MainResponse response = client.sendCommand(new AddChatCommand(port,chatName));

        server.stopServer();
        Assert.assertTrue(Objects.equals("New chat with port: "+port+" has opened.", response.getInfo()));
    }
    @Test
    public void getAllChatNamesCheck(){
        int port =7779;
        String chatName = "test";
        Server server = new Server(false);
        server.startServer();

        Client client = new Client("127.0.0.1");
        client.startClient();
        client.sendCommand(new AddChatCommand(port,chatName));

        MainResponse response = client.sendCommand(new GetAllChatNamesCommand());
        server.stopServer();

        Assert.assertTrue(Objects.equals(chatName+" with port:"+port+"\n", response.getInfo()));
    }
    @Test
    public void postMessageTest(){
        int port =7780;
        String chatName = "test";
        Server server = new Server(false);
        server.startServer();

        Client client = new Client("127.0.0.1");
        client.startClient();
        client.sendCommand(new AddChatCommand(port,chatName));


        MainResponse response = client.sendChatCommand(new PostMessageInChat(port,new Message("test","testUser"),"testUser"),port);
        server.stopServer();

        Assert.assertTrue(Objects.equals("Message sent", response.getInfo()));
    }
}
