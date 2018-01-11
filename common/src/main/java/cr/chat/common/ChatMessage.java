package cr.chat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage implements Serializable {
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

}
