package cr.chat.common.message;

import lombok.Data;

/**
 * 创建房间
 */
@Data
public class CreateRoom extends BaseMessage {
    private String name;
    private String code;

}
