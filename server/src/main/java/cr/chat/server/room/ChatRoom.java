package cr.chat.server.room;

import cr.chat.server.client.ClientDomain;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 房间
 */
@Data
public class ChatRoom {
    /**
     * 房间id
     */
    private String id;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 房间值
     */
    private String code;

    /**
     * 房主
     */
    private ClientDomain master;

    /**
     * 成员
     */
    private List<ClientDomain> members = new ArrayList<>();


    public void join(ClientDomain client) {
        members.add(client);
    }

    public void leave(ClientDomain client) {
        if (client == master) {
            for (ClientDomain member : members) {
                member.leaveRoom(code);
            }
        }else{
            if (members.contains(client)) {
                members.remove(client);
                client.leaveRoom(code);
            }
        }
    }


    public void sendMessage(ClientDomain client,String message) {

    }

}
