package q.app.dashboard.beans.notifications;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.helper.WebsocketLinks;

import javax.annotation.PostConstruct;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@Named
@SessionScoped
public class NotificationBean implements Serializable {

    private int liveQuotations;
    private int quotingQuotations;
    private WebSocketClient quotationClient;
    private int index;



    @Inject
    @Push(channel = "notificationChannel")
    private PushContext channel;

    @Inject
    private LoginBean loginBean;


    @PostConstruct
    private void init(){
        index = 0;
        this.initQuotationWebSocket();
    }

    private void initQuotationWebSocket() {
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
                System.out.println("Session closed");
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
                switch (function) {
                    case "pendingQuotations":
                        liveQuotations = value;
                        break;
                    case "quotingQuotations":
                        quotingQuotations = value;
                        break;
                }
                if(index == 0){
                    TimeUnit.SECONDS.sleep(3);
                }
                channel.send("rerender");
                index++;
            }
        } catch (Exception ignored) {
        }
    }

    private String getQuotationsWSLink() {
        return WebsocketLinks.getNotificationsLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
    }


    public int getLiveQuotations() {
        return liveQuotations;
    }

    public int getQuotingQuotations() {
        return quotingQuotations;
    }
}
