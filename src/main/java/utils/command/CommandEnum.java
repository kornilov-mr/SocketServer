package utils.command;

import utils.chat.messages.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum CommandEnum {
    //Enum, that contains name in console, which user will see in the console, nameInJson which is how command contains in json
    //Arraylist of arguments' names
    //ArrayList of arguments' classes
    //ArrayList with explanation for every argument for a user
    GET_ALL_MESSAGES_FROM_CHAT("openChat", "getAllMessagesFromChat",
            new ArrayList<>(Arrays.asList("port")),
            new ArrayList<>(Arrays.asList(Integer.class)),
            new ArrayList<>(Arrays.asList("port"))),
    POST_MESSAGE_IN_CHAT("postMessage", "postMessageInChat",
            new ArrayList<>(Arrays.asList("port","message","userName")),
            new ArrayList<>(Arrays.asList(Integer.class,Message.class,String.class)),
            new ArrayList<>(Arrays.asList("port","text of the message","user name (can be hidden)"))),
    CLOSE_SOCKET("exit", "closeSocketCommand", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
    HELP("help", "showHelpCommand", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
    GET_ALL_CHAT("allChats", "getAllChatCommand", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
    ADD_NEW_CHAT("createChat", "addNewChat",
            new ArrayList<>(Arrays.asList("port","name")),
            new ArrayList<>(Arrays.asList(Integer.class,String.class)),
            new ArrayList<>(Arrays.asList("port","chat name")));
    private final String nameInConsole;
    private final String nameInJson;
    private final Map<String, Class<?>> argsClasses;
    private final ArrayList<String> argNames;
    private final ArrayList<Class<?>> argClasses;
    private final ArrayList<String> explanations;

    CommandEnum(String nameInConsole, String nameInJson, ArrayList<String> argNames, ArrayList<Class<?>> argClasses, ArrayList<String> explanations) {
        this.nameInConsole = nameInConsole;
        this.nameInJson = nameInJson;
        this.argNames = argNames;
        this.argClasses = argClasses;
        this.explanations=explanations;

        this.argsClasses = createArgsMap();
    }
    private Map<String, Class<?>> createArgsMap(){
        Map<String, Class<?>> argsMap = new HashMap<>();
        for(int i=0;i<argNames.size();i++){
            argsMap.put(argNames.get(i),argClasses.get(i));
        }
        return argsMap;
    }

    public Map<String, Class<?>> getArgsClasses() {
        return argsClasses;
    }

    private static final Map<String, CommandEnum> nameInConsoleToCommand;

    static {
        nameInConsoleToCommand = new HashMap<String, CommandEnum>();
        for (CommandEnum v : CommandEnum.values()) {
            nameInConsoleToCommand.put(v.nameInConsole.toLowerCase(), v);
        }
    }

    private static final Map<String, CommandEnum> nameInJsonToCommand;

    static {
        nameInJsonToCommand = new HashMap<String, CommandEnum>();
        for (CommandEnum v : CommandEnum.values()) {
            nameInJsonToCommand.put(v.nameInJson, v);
        }
    }

    public static CommandEnum findByNameInJson(String name) {
        return nameInJsonToCommand.get(name);
    }

    public static CommandEnum findByNameInConsole(String name) {
        return nameInConsoleToCommand.get(name.toLowerCase());
    }

    public String getNameInConsole() {
        return nameInConsole;
    }

    public String getNameInJson() {
        return nameInJson;
    }

    public ArrayList<String> getExplanations() {
        return explanations;
    }

    public ArrayList<String> getArgNames() {
        return argNames;
    }
}
