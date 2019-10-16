package org.test.websocket;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebsocketListener
 */

@Singleton
@ServerEndpoint("/listener")
public class WebsocketListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketListener.class);

    private static Map<String, WebSocketSessionData> sessionsMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("Websocket onOpen - session info: open {}, secure {}, maxIdleTimeout {}, protocol {}, id {}", session.isOpen(), session.isSecure(), session.getMaxIdleTimeout(),
                session.getProtocolVersion(), session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("Websocket onMessage - session info: open {}, secure {}, maxIdleTimeout {}, protocol {}, id {}", session.isOpen(), session.isSecure(), session.getMaxIdleTimeout(),
                session.getProtocolVersion(), session.getId());
        LOGGER.info("Websocket onMessage - message {}", message);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOGGER.info("Websocket session close invoked from client, reason {} - {} {}", reason.getCloseCode().getCode(), reason.getCloseCode(), reason.getReasonPhrase());
    }

    private static final class WebSocketSessionData implements Serializable {
        private final Session websocketSession;
        private String httpSessionId;
        private String userId;
        private String tenant;
        private String xsrf;

        private WebSocketSessionData(String jsonInput, Session session) {
            websocketSession = session;
            JsonParser parser = new JsonParser();
            JsonObject obj;
            try {
                obj = (JsonObject) parser.parse(jsonInput);
                xsrf = obj.get("xsrf").getAsString();
                userId = obj.get("userId").getAsString();
                httpSessionId = obj.get("sessionId").getAsString();
                tenant = (obj.get("tenant") == null || JsonNull.INSTANCE.equals(obj.get("tenant"))) ? null : obj.get("tenant").getAsString();
            } catch (JsonSyntaxException e) {
                LOGGER.warn("Unable to parse input json", e);
            }
        }

        public Session getWebsocketSession() {
            return websocketSession;
        }

        public String getHttpSessionId() {
            return httpSessionId;
        }

        public String getUserId() {
            return userId;
        }

        public String getTenant() {
            return tenant;
        }

        public String getXsrf() {
            return xsrf;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof WebSocketSessionData))
                return false;

            WebSocketSessionData that = (WebSocketSessionData) o;

            if (!websocketSession.equals(that.websocketSession))
                return false;
            if (!httpSessionId.equals(that.httpSessionId))
                return false;
            if (!userId.equals(that.userId))
                return false;
            return xsrf.equals(that.xsrf);
        }

        @Override
        public int hashCode() {
            int result = websocketSession.hashCode();
            result = 31 * result + httpSessionId.hashCode();
            result = 31 * result + userId.hashCode();
            result = 31 * result + xsrf.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "WebSocketSessionData{" +
                    "websocketSession=" + websocketSession +
                    ", httpSessionId='" + httpSessionId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", tenant='" + tenant + '\'' +
                    ", xsrf='" + xsrf + '\'' +
                    '}';
        }
    }
}
