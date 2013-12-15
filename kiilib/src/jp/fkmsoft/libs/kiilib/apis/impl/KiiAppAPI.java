package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.apis.ACLAPI;
import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Implementation of {@link AppAPI}
 */
public abstract class KiiAppAPI implements AppAPI {

    final String appId;
    final String appKey;
    final String baseUrl;
    
    String accessToken;
    
    private final UserAPI userAPI;
    private final GroupAPI groupAPI;
    private final BucketAPI bucketAPI;
    private final ObjectAPI objectAPI;
    private final TopicAPI topicAPI;
    private final ACLAPI aclAPI;
    
    public KiiAppAPI(String appId, String appKey, String baseUrl) {
        this.appId = appId;
        this.appKey = appKey;
        this.baseUrl = baseUrl;
        
        userAPI = new KiiUserAPI(this);
        groupAPI = new KiiGroupAPI(this);
        bucketAPI = new KiiBucketAPI(this);
        objectAPI = new KiiObjectAPI(this);
        topicAPI = new KiiTopicAPI(this);
        aclAPI = new KiiACLAPI(this);
    }

    @Override
    public void loginAsAdmin(String clientId, String clientSecret, final LoginCallback callback) {
        String url = baseUrl + "/oauth2/token";
        
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", clientId);
            json.put("client_secret", clientSecret);
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null, 
                "application/json", null, json, new ResponseHandler() {
            
            @Override
            public void onResponse(int status, JSONObject response, String etag) {
                if (status < 300) {
                    success(response);
                } else {
                    callback.onError(status, response.toString());
                }
            }
            
            private void success(JSONObject response) {
                try {
                    String token = response.getString("access_token");
                    String userId = response.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }

            @Override
            public void onException(Exception e) {
                callback.onError(KiiCallback.STATUS_GENERAL_EXCEPTION, e.getMessage());
            }
        });
    }

    @Override
    public void loginAsUser(String identifier, String password, final LoginCallback callback) {
        String url = baseUrl + "/oauth2/token";
        
        JSONObject json = new JSONObject();
        try {
            json.put("username", identifier);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null, "application/json", null, json, new KiiResponseHandler<LoginCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, LoginCallback callback) {
                try {
                    String token = response.getString("access_token");
                    String userId = response.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }
    
    @Override
    public void signup(JSONObject info, String password, final SignupCallback callback) {
        String url = baseUrl + "/apps/" + appId + "/users";
        
        try {
            info.put("password", password);
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        
        getHttpClient().sendJsonRequest(Method.POST, url, null,
                "application/json", null, info, new KiiResponseHandler<SignupCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, SignupCallback callback) {
                try {
                    String userId = response.getString("userID");
                    callback.onSuccess(new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }
    
    /*
     * (non-Javadoc)
     * @see com.kii.cloud.KiiAPI#setAccessToken(java.lang.String)
     */
    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public UserAPI userAPI() {
        return userAPI;
    }

    @Override
    public GroupAPI groupAPI() {
        return groupAPI;
    }

    @Override
    public BucketAPI bucketAPI() {
        return bucketAPI;
    }

    @Override
    public ObjectAPI objectAPI() {
        return objectAPI;
    }

    @Override
    public TopicAPI topicAPI() {
        return topicAPI;
    }

    @Override
    public ACLAPI aclAPI() {
        return aclAPI;
    }
    
    public abstract KiiHTTPClient getHttpClient();
}
