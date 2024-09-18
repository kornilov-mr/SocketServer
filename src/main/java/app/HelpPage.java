package app;

import utils.command.CommandEnum;

public class HelpPage {
    //Displays information about all Commands from CommandEnum
    @Override
    public String toString() {
        String s = "";
        for(CommandEnum commandEnum: CommandEnum.values()){
            s+=commandEnum.getNameInConsole()+" || params: ";
            int i=0;
            for(String argName: commandEnum.getArgsClasses().keySet()){
                s+=argName+"="+commandEnum.getArgsClasses().get(argName).getSimpleName()+ " ("+commandEnum.getExplanations().get(i)+") ";
                i+=1;
            }
            s+="\n";
        }
        return s;
    }
}
