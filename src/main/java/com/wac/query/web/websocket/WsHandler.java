package com.wac.query.web.websocket;

import org.slf4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author huangjinsheng on 2015/6/18.
 */
public class WsHandler extends TextWebSocketHandler {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(WsHandler.class);
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("=====ws message:{}",message);
        TextMessage returnMessage = new TextMessage(message.getPayload()+" received at server "+System.currentTimeMillis());
        session.sendMessage(returnMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("====handleTransportError:{}"+exception);
       super.handleTransportError(session,exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("connect to the websocket success......");
        super.afterConnectionEstablished(session);
        TextMessage returnMessage = new TextMessage("hello...................");
        session.sendMessage(returnMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug("connect to the afterConnectionClosed success......");
        super.afterConnectionClosed(session, status);
    }
}
