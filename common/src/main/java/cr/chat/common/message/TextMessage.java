package cr.chat.common.message;

import lombok.Data;

/**
 * @author Beldon
 * @create 2018-01-12 上午11:01
 */
@Data
public class TextMessage extends BaseMessage {
    private String roomCode;
    private String message;
}
