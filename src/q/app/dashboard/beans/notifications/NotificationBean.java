package q.app.dashboard.beans.notifications;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.helper.WebsocketLinks;

import javax.annotation.PostConstruct;

import javax.annotation.PreDestroy;
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
    private int noVins;
    private int wireRequets;
    private int processCarts;
    private int index;
    private WebSocketClient customerClient;
    private WebSocketClient quotationClient;
    private WebSocketClient cartClient;

    @Inject
    @Push(channel = "notificationChannel")
    private PushContext channel;

    @Inject
    private LoginBean loginBean;


    @PostConstruct
    private void init(){
        index = 0;
        this.initQuotationWebSocket();
        this.initCustomerWebSocket();
        this.initCartWebSocket();
    }

    @PreDestroy
    public void destroy() {
        customerClient.close();
        quotationClient.close();
    }

    private void initCustomerWebSocket() {
        customerClient = new WebSocketClient(URI.create(this.getCustomerWSLink()), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
            }

            @Override
            public void onMessage(String s){
                changeOccurred(s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
            }

            @Override
            public void onError(Exception e) {

            }
        };
        customerClient.connect();
    }

    private void initCartWebSocket() {
        cartClient = new WebSocketClient(URI.create(this.getCartWSLink()), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
            }

            @Override
            public void onMessage(String s){
                changeOccurred(s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
            }

            @Override
            public void onError(Exception e) {

            }
        };
        cartClient.connect();
    }

    private void initQuotationWebSocket() {
        quotationClient = new WebSocketClient(URI.create(this.getQuotationsWSLink()), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
            }

            @Override
            public void onMessage(String s){
                changeOccurred(s);
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



    private void changeOccurred(String data){
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
                    case "noVins":
                        noVins = value;
                        break;
                    case "wireRequests":
                        wireRequets = value;
                        break;
                    case "processCarts":
                        processCarts = value;
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
        return WebsocketLinks.getQuotationNotificationsLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
    }

    private String getCustomerWSLink() {
        return WebsocketLinks.getCustomerNotificationsLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
    }

    private String getCartWSLink() {
        return WebsocketLinks.getCartNotificationsLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
    }


    public int getLiveQuotations() {
        return liveQuotations;
    }

    public int getQuotingQuotations() {
        return quotingQuotations;
    }

    public int getNoVins() {
        return noVins;
    }

    public int getWireRequets() {
        return wireRequets;
    }

    public int getProcessCarts() {
        return processCarts;
    }
}
