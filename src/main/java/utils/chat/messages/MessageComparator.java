package utils.chat.messages;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message o1, Message o2) {
        if(o1.getReceiveTime()>o2.getReceiveTime()){
            return -1;
        }else if(o1.getReceiveTime()<o2.getReceiveTime()){
            return 1;
        }else{
            return 0;
        }
    }
}
