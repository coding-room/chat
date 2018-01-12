package cr.chat.server.handler;

import cr.chat.server.client.ClientDomain;

/**
 * @author Beldon
 * @create 2018-01-12 上午11:03
 */
public interface MessageHandler<T> {
    void handle(ClientDomain client, T msg);

}
