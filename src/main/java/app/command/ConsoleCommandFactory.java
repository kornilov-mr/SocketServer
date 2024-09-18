package app.command;

import app.parsers.LineParser;
import utils.command.Command;
import utils.command.CommandEnum;
import utils.command.CommandFactory;

import java.util.Map;
import java.util.Objects;

public class ConsoleCommandFactory {
    public final CommandFactory commandFactory;
    public final LineParser lineParser;

    public ConsoleCommandFactory() {
        this.commandFactory = new CommandFactory();
        this.lineParser= new LineParser();
    }
    //Converts console line in a Command
    public Command getCommandFromConsoleLine(String line,Map<String, Object> hiddenArgs ) throws IllegalArgumentException{
        if(Objects.equals(line,"")){
            throw new IllegalArgumentException("You passed an empty string");
        }
        String[] commandArgs = line.split(" ");
        String commandName= commandArgs[0];
        CommandEnum commandEnum = CommandEnum.findByNameInConsole(commandName);

        if(commandEnum ==null){
            throw new IllegalArgumentException("Unknown command name");
        }
        String argumentLine = line.replaceFirst(commandName,"");
        Map<String, Object> args = lineParser.parseArgsFromLine(argumentLine,commandEnum);


        int i=0;
        for(String argName : hiddenArgs.keySet()){
            args.put(argName,hiddenArgs.get(argName));
        }
        //checks if every argument needed for the command exists
        for (String argName: commandEnum.getArgNames()){
            if(!args.containsKey(argName)){
                throw new IllegalArgumentException("Amount of arguments provided doesn't fit" +"\n"+
                                                    "There is no "+argName+" argument");
            }
        }
        return commandFactory.createCommand(commandEnum,args);
    }
}
