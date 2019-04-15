package q.app.dashboard.beans.notifications;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.WebsocketLinks;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.net.URI;

@Named
@SessionScoped
public class NotificationBean implements Serializable {

    private int liveQuotations;

    @Inject
    @Push(channel = "notificationQuotationsChannel")
    private PushContext channel;


    private WebSocketClient quotationClient;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    private void init(){
        this.initWebSocket();
    }


    public int getLiveQuotations() {
        return liveQuotations;
    }

    public void setLiveQuotations(int liveQuotations) {
        this.liveQuotations = liveQuotations;
    }


    private void initWebSocket() {
        quotationClient = new WebSocketClient(URI.create(this.getQuotationsWSLink()), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
            }

            @Override
            public void onMessage(String s){
                changeOccured(s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {

            }

            @Override
            public void onError(Exception e) {

            }
        };
        quotationClient.connect();
    }

    private void changeOccured(String data){
        try {
            if (data != null) {
                String[] messages = data.split(",");
                String function = messages[0];
                Integer value = Integer.parseInt(messages[1]);
                String clientMessage = "";
                switch (function) {
                    case "pendingQuotations":
                        liveQuotations = value;
                        break;
                }
                System.out.println("pushing now " + liveQuotations);
                channel.send("rerender");
            }

    } catch (Exception ignored) {

    }
    }

    private String getQuotationsWSLink() {
        return WebsocketLinks.getNotificationsLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
    }

}
