package cr.chat.common.message;

import lombok.Data;

/**
 * @author Beldon
 * @create 2018-01-12 上午11:02
 */
@Data
public class LeaveRoom extends BaseMessage {
    private String roomCode;
}
