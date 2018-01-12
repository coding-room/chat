package cr.chat.common.message;

import lombok.Data;

/**
 * @author Beldon
 * @create 2018-01-12 上午11:00
 */
@Data
public class JoinRoom extends BaseMessage {
    private String code;
}
