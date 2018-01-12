package cr.chat.common.message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Beldon
 * @create 2018-01-12 上午10:50
 */
@Data
public abstract class BaseMessage {
    /**
     * 消息id
     */
    private String messageId;

    private String type;

    private Map<String, String> headers = new HashMap<>();
}
