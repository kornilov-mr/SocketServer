package utils.chat.messages;

import org.json.JSONObject;

import java.util.Date;

public class Message {
    private volatile String messageText;
    private final long sendTime;
    private long receiveTime=0;
    private String userName;
    public Message(JSONObject jsonObject){
        this(jsonObject.getJSONObject("args").getString("text"),
                jsonObject.getJSONObject("args").getLong("sendTime"),
                jsonObject.getJSONObject("args").getLong("receiveTime"),
                jsonObject.getJSONObject("args").getString("userName"));
    }

    public Message(String messageText) {
        this.messageText = messageText;
        this.sendTime= new Date().getTime();
    }
    public Message(String messageText, String userName) {
        this.messageText = messageText;
        this.userName=userName;
        this.sendTime= new Date().getTime();
    }
    public Message(String messageText, long sendTime, long receiveTime, String userName) {
        this.messageText = messageText;
        this.sendTime=sendTime;
        this.userName=userName;
        if(receiveTime==0){
            this.receiveTime=new Date().getTime();
        }else{
            this.receiveTime=receiveTime;
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        JSONObject args = new JSONObject();
        args.put("text",messageText);
        args.put("sendTime",sendTime);
        args.put("receiveTime", receiveTime);
        args.put("userName", userName);
        jsonObject.put("class","message");
        jsonObject.put("args",args);
        return jsonObject;
    }

    public String getMessageText() {
        return messageText;
    }

    public long getSendTime() {
        return sendTime;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public String getUserName() {
        return userName;
    }
}
