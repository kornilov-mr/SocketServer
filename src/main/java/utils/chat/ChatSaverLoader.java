package utils.chat;

import org.json.JSONArray;
import org.json.JSONObject;
import server.sockets.SocketManager;
import utils.chat.messages.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class ChatSaverLoader {
    private final File dataFolder = new File("src/main/java/server/data");
    private final String saveFileName = "chats.json";
    private final File saveFile = dataFolder.toPath().resolve(saveFileName).toFile();
    private final SocketManager socketManager;
    private final ChatManager chatManager;

    public ChatSaverLoader(SocketManager socketManager,ChatManager chatManager) {
        this.socketManager = socketManager;
        this.chatManager = chatManager;
    }
    //check if file and folder exist, if not creates them with empty jsonArray
    private void checkIfSaveFileExist(){
        if (!dataFolder.exists()){
            dataFolder.mkdir();
        }
        if(!saveFile.exists()){
            try {
                saveFile.createNewFile();
                PrintWriter pw = new PrintWriter(saveFile);
                pw.println("[]");
                pw.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    protected ArrayList<Chat> loadChats(){
        ArrayList<Chat> chats = new ArrayList<>();
        checkIfSaveFileExist();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(saveFile)))){
            String JSONString = br.readLine();
            JSONArray jsonArray = new JSONArray(JSONString);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject chatJson = jsonArray.getJSONObject(i);
                int port = chatJson.getInt("port");
                String name = chatJson.getString("name");
                ChatSocket chatSocket = socketManager.openNewSocket(port,chatManager);
                Chat chat = new Chat(port,name,chatSocket);

                JSONArray messagesJson = chatJson.getJSONArray("messages");
                for(int j=0;j< messagesJson.length();j++){
                    JSONObject messageJson= (JSONObject) messagesJson.get(i);
                    Message message = new Message(messageJson);
                    chat.addMessage(message);
                }
                chats.add(chat);

            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chats;
    }
    protected void saveChats(Collection<Chat> chats){
        checkIfSaveFileExist();
        JSONArray chatsJson = new JSONArray();
        for(Chat chat : chats){
            chatsJson.put(chat.toJsonForSaving());
        }
        try(PrintWriter pw = new PrintWriter(saveFile)){
            pw.println(chatsJson.toString());
        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
    }
    protected void deleteChats(){
        checkIfSaveFileExist();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(saveFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pw.println("[]");
        pw.flush();
    }
}
