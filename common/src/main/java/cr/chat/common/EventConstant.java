package cr.chat.common;

/**
 * 事件
 *
 * @author Beldon
 * @create 2018-01-12 下午3:03
 */
public interface EventConstant {
    /**
     * 创建名字
     */
    String CREATE_NAME = "CREATE_NAME";

    /**
     * 创建房间
     */
    String CREATE_ROOM = "CREATE_ROOM";
    /**
     * 加入房间
     */
    String JOIN_ROOM = "JOIN_ROOM";

    /**
     * 离开房间
     */
    String LEAVE_ROOM = "LEAVE_ROOM";

    /**
     * 列出房间
     */
    String LIST_ROOM = "LIST_ROOM";


    /**
     * 发送消息
     */
    String SEND_MESSAGE = "SEND_MSG";

    /**
     * 错误新消息
     */
    String ERROR = "ERROR";
}
