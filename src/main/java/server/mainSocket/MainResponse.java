package server.mainSocket;

import org.json.JSONArray;
import org.json.JSONObject;
//MainResponse consists of 4 parts
//exception code
//info (information about execution, that will be shown to user)
//data (JsonArray with all data from the command)
//commandName (name of command, which helps consoleWriter display data)
public class MainResponse {
    private final int code;
    private final String info;
    private final JSONArray data;
    private final String commandName;
    public MainResponse(RuntimeException e){
        this(-1,e.getMessage(), new JSONArray(),"exception");
    }
    public MainResponse(String commandName){
        this(0, "", new JSONArray(),commandName);
    }
    public MainResponse(int code, String commandName){
        this(code, "",new JSONArray(),commandName);
    }
    public MainResponse(JSONObject jsonObject){
        this(jsonObject.getInt("code"), jsonObject.getString("info"),jsonObject.getJSONArray("data"),jsonObject.getString("commandName"));
    }
    public MainResponse(int code, String explanation, String commandName) {
        this(code,explanation, new JSONArray(), commandName);
    }
    public MainResponse(int code, String explanation, JSONArray data, String commandName) {
        this.code = code;
        this.info = explanation;
        this.data = data;
        this.commandName=commandName;
    }
    public String toJsonString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("info",info);
        jsonObject.put("data",data);
        jsonObject.put("commandName",commandName);
        return jsonObject.toString();
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getCommandName() {
        return commandName;
    }

    public JSONArray getData() {
        return data;
    }
}
