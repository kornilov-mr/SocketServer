package app.parsers;

import utils.chat.messages.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Parsers {
    public static Map<Class<?>, Function<String, Object>> classesToParsers;
    static {
        classesToParsers= new HashMap<>();
        classesToParsers.put(Integer.class,Parsers::parseInt);
        classesToParsers.put(Double.class,Parsers::parseDouble);
        classesToParsers.put(String.class,Parsers::parseString);
        classesToParsers.put(Message.class,Parsers::parseMessage);
    }
    public static Object parseInt(String s){
        Integer ans = null;
        try{
            ans=Integer.parseInt(s);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("wrong argumentType: Integer expected, but given "+s);
        }
        return ans;
    }
    public static Object parseDouble(String s){
        Double ans = null;
        try{
            ans=Double.parseDouble(s);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("wrong argumentType: Double expected, but given "+s);
        }
        return ans;
    }
    public static Object parseString(String s){
        return s;
    }
    public static Object parseMessage(String s){
        return new Message(s);
    }
}
