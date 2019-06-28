package q.app.dashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class WebApp implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer appCode;
    private String appName;
    private String appSecret;
    private boolean active;



    @JsonIgnore
    public String getWebAppLLogoName(){
        try{
            if(appName.equals("Qetaa.com")){
                return "qetaa-logo.jpg";
            }
            if(appName.equals("Q.Parts")){
                return "q-logo.jpg";
            }
            if(appName.equals("Postman")){
                return "q-logo.jpg";
            }
            throw new NullPointerException();
        }catch (NullPointerException ex){
            return "";
        }
    }

    public Integer getAppCode() {
        return appCode;
    }

    public void setAppCode(Integer appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appCode == null) ? 0 : appCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WebApp other = (WebApp) obj;
        if (appCode == null) {
            if (other.appCode != null)
                return false;
        } else if (!appCode.equals(other.appCode))
            return false;
        return true;
    }


}