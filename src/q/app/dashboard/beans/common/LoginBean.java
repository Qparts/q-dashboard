package q.app.dashboard.beans.common;

import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.user.User;
import q.app.dashboard.model.user.UserHolder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Named(value="loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private UserHolder userHolder;


    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        userHolder = new UserHolder();
        userHolder.setUser(new User());
    }

    public void checkAccessRedirectHomeL(List<String> ids) {
        if(!hasAccessL(ids)) {
            Helper.redirect("home");
        }
    }

    public void checkAccessRedirectHome(int id) {
        if(!hasAccess(id)) {
            Helper.redirect("home");
        }
    }

    public boolean hasAccess(int id) {
        return userHolder.hasAccess(id);
    }

    public boolean hasAccessL(List<String> ids) {
        boolean found = false;
        for(String s : ids) {
            if(userHolder.hasAccess(Integer.parseInt(s))) {
                found = true;
                break;
            }
        }
        return found;
    }

    public boolean hasAccess(String id) {
        return userHolder.hasAccess(Integer.parseInt(id));
    }


    public int getLoggedUserId() {
        return this.getUserHolder().getUser().getId();
    }

    public void login(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("username", username);
        map.put("code", password);
        System.out.println("Requesting " + AppConstants.USER_LOGIN);
        Response r = reqs.postSecuredRequest(AppConstants.USER_LOGIN, map);
        System.out.println("response status:" + r.getStatus());
        if(r.getStatus() == 200){
            this.userHolder = r.readEntity(UserHolder.class);
            doLogin();
        }else{
            System.out.println("Login failed");
        }
    }

    public boolean isLogged() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user") != null;
    }

    private void doLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("user", userHolder);
        Helper.redirect("/app/home.xhtml");
    }

    public void doLogout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        Helper.redirect("/login");
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public UserHolder getUserHolder() {
        return userHolder;
    }

    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

}
