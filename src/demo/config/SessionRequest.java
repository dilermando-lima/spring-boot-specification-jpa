package demo.config;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import demo.model.UserProfile;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionRequest {

    private String accountId;
    private String userId;
    private UserProfile userProfile;

    @Override
    public String toString() {
        return "SessionRequest [" + (accountId != null ? "accountId=" + accountId + ", " : "")
                + (userId != null ? "userId=" + userId + ", " : "")
                + (userProfile != null ? "userProfile=" + userProfile : "") + "]";
    }

    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public UserProfile getUserProfile() {
        return userProfile;
    }
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}
