package utils.chat;

import utils.command.CommandFactory;
import utils.exceptions.TransmissionException;
import server.sockets.SocketManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ChatManager {
    private volatile ConcurrentMap<Integer,Chat> chats;
    private final SocketManager socketManager;
    private final CommandFactory commandFactory;
    //Object, that loads and saves chats in json format
    private final ChatSaverLoader saverLoader;
    //if true, server will load and save in json. If false, won't save and load form json
    private final boolean saving;

    public ChatManager(SocketManager socketManager,CommandFactory commandFactory, boolean saving) {
        this.socketManager = socketManager;
        this.commandFactory=commandFactory;
        this.saverLoader= new ChatSaverLoader(socketManager,this);
        this.chats= createChatsMap();
        this.saving=saving;
    }
    //loads Chats from json
    private ConcurrentMap<Integer,Chat> createChatsMap(){
        ConcurrentMap<Integer,Chat> chatsMap = new ConcurrentHashMap<>();
        ArrayList<Chat> chats = new ArrayList<>();

        if(saving){
            chats = saverLoader.loadChats();
        }
        for(Chat chat: chats){
            chat.openChat();
            chatsMap.put(chat.getPort(),chat);
        }
        return chatsMap;
    }
    //Creates chat with name and port and starts it
    public synchronized void addChat(String name, int port){
        if(!chats.containsKey(port)){
            ChatSocket chatSocket = socketManager.openNewSocket(port,this);
            Chat chat = new Chat(port,name,chatSocket);
            chat.openChat();
            chats.put(port,chat);
        }else{
            throw new TransmissionException(1,"chat with That port already exist");
        }
    }
    //Close all Chats
    public void closeChats(){
        for(Chat chat : chats.values()){
            chat.closeChat();
        }
        if(saving){
            saverLoader.saveChats(chats.values());
        }
    }
    public void deleteAllChats(){
        ConcurrentMap<Integer,Chat> chats= new ConcurrentHashMap<>();
        closeChats();
        saverLoader.deleteChats();
    }
    public Map<Integer, Chat> getChats() {
        return chats;
    }
}
