package cr.chat.common.message.bean;

import lombok.Data;

/**
 * @author Beldon
 * @create 2018-01-12 下午2:40
 */
@Data
public class ChatRoomBean {
    /**
     * 房间名称
     */
    private String name;

    /**
     * 房间值
     */
    private String code;
}
