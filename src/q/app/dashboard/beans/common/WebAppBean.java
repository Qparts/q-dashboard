package q.app.dashboard.beans.common;

import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.model.WebApp;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class WebAppBean implements Serializable {

    private List<WebApp> webApps;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init(){
        webApps = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.GET_WEB_APPS);
        if(r.getStatus() == 200){
            webApps = r.readEntity(new GenericType<List<WebApp>>(){});
        }
    }


    public WebApp getWebAppById(int appCode){
        for(var webApp : webApps){
            if(webApp.getAppCode().equals(appCode)){
                return webApp;
            }
        }
        return new WebApp();
    }


    public List<WebApp> getWebApps() {
        return webApps;
    }

    public void setWebApps(List<WebApp> webApps) {
        this.webApps = webApps;
    }
}
