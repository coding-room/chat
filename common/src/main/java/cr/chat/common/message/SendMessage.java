package cr.chat.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Beldon
 * @create 2018-01-12 上午8:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessage {
    public static final String TYPE_COMMON = "COMMON";
    public static final String TYPE_SYSTEM = "SYS";
    private String type = TYPE_COMMON;
    private String content;
}
