package app;

import org.json.JSONArray;
import utils.chat.messages.Message;
import utils.chat.messages.MessageComparator;
import utils.command.CommandEnum;
import server.mainSocket.MainResponse;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public final class ConsoleWriter {
    private final String paddingLeft = "-----------|";
    private final PrintStream printStream;

    public ConsoleWriter(OutputStream outputStream) {
        this.printStream = new PrintStream(outputStream);
    }
    void writeException(Exception e){
        printStream.println(e.getMessage());
    }
    void writeString(String s){
        printStream.println(s);
    }
    void printStartSequence(){
        writeString("---------------Socket Server---------------");
        writeString("--------------Multi Threading--------------");
        writeString("");
        writeString("");
        writeString("...");
        writeString("...");
        writeString("");
        writeString("");

        writeString("Please enter your name");
    }
    void writeResponse(MainResponse response){
        printStream.println(response.getInfo());
        CommandEnum commandEnum = CommandEnum.findByNameInJson(response.getCommandName());
        if(commandEnum.equals(CommandEnum.GET_ALL_MESSAGES_FROM_CHAT)||commandEnum.equals(CommandEnum.POST_MESSAGE_IN_CHAT)){
            ArrayList<Message> messages = new ArrayList<>();
            JSONArray data = response.getData();
            for(int i=0;i<data.length();i++){
                messages.add(new Message(data.getJSONObject(i)));
            }
            messages.sort(new MessageComparator());

            String s ="";
            for(int i=messages.size()-1;i >=0 && i> messages.size()-1-10;i--){
                Message message = messages.get(i);
                s+="\n";
                s+=paddingLeft+message.getUserName()+"\n";
                s+=paddingLeft+message.getMessageText()+"\n";
            }
            writeString(s);
        }
    }

}
