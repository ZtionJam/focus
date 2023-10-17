package cn.ztion.focusserver.websocket;

import cn.ztion.focusserver.config.FocusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: ZtionJam
 * @Date: 2023/8/3 15:39
 * @Description: FocusWebSocket
 * @Version 1.0.0
 */
@Component
@ServerEndpoint("/ws/focus/{token}")
@Slf4j
public class FocusWebSocket {
    public static FocusConfig focusConfig;
    private String token;
    private Session session;
    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();
    private static CopyOnWriteArraySet<FocusWebSocket> webSockets = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        if (focusConfig.getCheckToken() && !Arrays.asList(focusConfig.getTokens()).contains(token)) {
            throw new RuntimeException("token is not found");
        }
        webSockets.add(this);
        sessionPool.put(token, session);
        log.info(token + ":ws新链接");
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(token);
            log.info(this.token + ":ws断开连接");
        } catch (Exception e) {
            log.info(this.token + ":ws断开连接报错");
        }
    }

    @OnMessage
    public void onMessage(String message) {
    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误,原因:"+error.getMessage());
    }

    public void sendOneMessage(String token, String message) {
        Session session = sessionPool.get(token);
        if (session != null && session.isOpen()) {
            try {
                log.info("【websocket消息】推送:" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
