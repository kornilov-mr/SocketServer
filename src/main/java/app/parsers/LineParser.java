package app.parsers;

import utils.command.CommandEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LineParser {
    //parser String with arguments like (port=7778, name=test), into a map with names as keys, and Object as values
    public Map<String, Object> parseArgsFromLine(String argumentLine, CommandEnum commandEnum){
        Map<String,Object> args = new HashMap<>();

        String[] arguments = argumentLine.split("=");
        if(arguments.length==1){
            return args;
        }
        String[] argumentNames = new String[arguments.length-1];
        String[] argumentString = new String[arguments.length-1];
        for(int i=0;i<arguments.length;i++){
            if(i==0){
                argumentNames[0]=arguments[0].replaceAll(" ","");
                continue;
            } else if (i==arguments.length-1){
                argumentString[i-1]=arguments[i];
                continue;
            }
            String[] argumentsSegments= arguments[i].split(" ");
            argumentNames[i]=argumentsSegments[argumentsSegments.length-1].replaceAll(" ","");

            String s = String.join("",argumentsSegments).replace(argumentNames[i],"");
            argumentString[i-1]= s;
        }
        for(int i=0;i<argumentNames.length;i++){
            if(commandEnum.getArgsClasses().containsKey(argumentNames[i])){
                Function<String, Object> parser = Parsers.classesToParsers.get(commandEnum.getArgsClasses().get(argumentNames[i]));
                args.put(argumentNames[i],parser.apply(argumentString[i]));
            }
        }
        return args;
    }
}
