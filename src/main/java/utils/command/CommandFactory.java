package utils.command;

import org.json.JSONObject;
import utils.chat.messages.Message;
import utils.command.chatCommand.command.GetAllMessagesFromChatCommand;
import utils.command.chatCommand.command.PostMessageInChat;
import utils.command.mainLineCommand.commands.AddChatCommand;
import utils.command.mainLineCommand.commands.CloseSocketCommand;
import utils.command.mainLineCommand.commands.GetAllChatNamesCommand;
import utils.command.mainLineCommand.commands.ShowHelpCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandFactory {
    public Command createCommand(CommandEnum commandEnum, Map<String, Object> args){
        Map<String, Class<?>> argsClasses= commandEnum.getArgsClasses();
        //checks that every argument is exactly expected class
        for(String argName: commandEnum.getArgNames()){
            Class<?> argClass = argsClasses.get(argName);
            if(!argClass.isInstance(args.get(argName))){
                throw new IllegalArgumentException("invalid arguments class");
            }
        }

        if(commandEnum.equals(CommandEnum.ADD_NEW_CHAT)){
            return new AddChatCommand((Integer) args.get("port"),(String) args.get("name"));
        }
        if(commandEnum.equals(CommandEnum.CLOSE_SOCKET)){
            return new CloseSocketCommand();
        }
        if(commandEnum.equals(CommandEnum.GET_ALL_CHAT)){
            return new GetAllChatNamesCommand();
        }
        if(commandEnum.equals(CommandEnum.HELP)){
            return new ShowHelpCommand();
        }
        if(commandEnum.equals(CommandEnum.POST_MESSAGE_IN_CHAT)){
            Message message = (Message) args.get("message");
            message.setUserName((String) args.get("userName"));
            return new PostMessageInChat((Integer) args.get("port"), message,(String) args.get("userName"));
        }
        if(commandEnum.equals(CommandEnum.GET_ALL_MESSAGES_FROM_CHAT)){
            return new GetAllMessagesFromChatCommand((Integer) args.get("port"));
        }
        throw new IllegalArgumentException("invalid command class");
    }
    //create Command from json
    public Command createCommandFromJson(JSONObject jsonObject){
        Map<String, Object> args= new HashMap<>();

        String commandName = jsonObject.getString("commandName");

        JSONObject arguments = jsonObject.getJSONObject("args");

        //reads all parameters from json
        for(String argName: arguments.keySet()){
            Object arg =  arguments.get(argName);
            if(arg instanceof JSONObject){
                JSONObject argJson=(JSONObject) arg;
                String className = argJson.getString("class");
                if(Objects.equals(className,"message")){
                    arg = new Message(argJson);
                }
            }
            args.put(argName, arg);
        }
        //finds Command for CommandEnum based on name
        CommandEnum commandEnum = CommandEnum.findByNameInJson(commandName);
        return createCommand(commandEnum,args);
    }
}
