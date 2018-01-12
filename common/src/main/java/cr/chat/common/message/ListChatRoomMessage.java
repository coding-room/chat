package cr.chat.common.message;

import cr.chat.common.message.bean.ChatRoomBean;
import lombok.Data;

import java.util.List;

/**
 * @author Beldon
 * @create 2018-01-12 下午2:38
 */
@Data
public class ListChatRoomMessage extends BaseMessage {
    private List<ChatRoomBean> chatRooms;
}
