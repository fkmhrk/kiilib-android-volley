package jp.fkmsoft.libs.kiilib;

import org.json.JSONObject;

/**
 * Provides Kii API
 * @author fkm
 *
 */
public interface KiiAPI {
    public interface LoginCallback extends KiiCallback {
        void onSuccess(String token, KiiUser user);
    }
    void loginAsAdmin(String clientId, String clientSecret, LoginCallback callback);
    
    void loginAsUser(String identifier, String password, LoginCallback callback);
    
    public interface SignupCallback extends KiiCallback {
        void onSuccess(KiiUser user);
    }
    void signup(JSONObject info, String password, SignupCallback callback);
    
    void setAccessToken(String accessToken);
    
    String getAccessToken();
    
    KiiUserAPI userAPI();
    
    KiiGroupAPI groupAPI();
    
    KiiBucketAPI bucketAPI();
    
    KiiObjectAPI objectAPI();
    
    KiiTopicAPI topicAPI();
    
    KiiACLAPI aclAPI();

    

    
}
