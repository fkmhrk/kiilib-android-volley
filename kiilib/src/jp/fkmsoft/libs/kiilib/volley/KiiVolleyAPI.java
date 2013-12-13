package jp.fkmsoft.libs.kiilib.volley;

import jp.fkmsoft.libs.kiilib.apis.ACLAPI;
import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;


/**
 * Implementation of {@link AppAPI} for volley
 */
public class KiiVolleyAPI implements AppAPI {

    final RequestQueue queue;
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
    
    public KiiVolleyAPI(RequestQueue queue, String appId, String appKey, String baseUrl) {
        this.queue = queue;
        this.appId = appId;
        this.appKey = appKey;
        this.baseUrl = baseUrl;
        
        userAPI = new KiiVolleyUserAPI(this);
        groupAPI = new KiiVolleyGroupAPI(this);
        bucketAPI = new KiiVolleyBucketAPI(this);
        objectAPI = new KiiVolleyObjectAPI(this);
        topicAPI = new KiiVolleyTopicAPI(this);
        aclAPI = new KiiVolleyACLAPI(this);
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
        
        KiiRequest request = new KiiRequest(Method.POST, url, appId, appKey, json, new Listener<KiiResponse>() {
            @Override
            public void onResponse(KiiResponse result) {
                try {
                    String token = result.getString("access_token");
                    String userId = result.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        }, new KiiErrorListener<LoginCallback>(callback)
        );
        
        queue.add(request);
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
        
        KiiRequest request = new KiiRequest(Method.POST, url, appId, appKey, json, new Listener<KiiResponse>() {
            @Override
            public void onResponse(KiiResponse result) {
                try {
                    String token = result.getString("access_token");
                    String userId = result.getString("id");
                    accessToken = token;
                    callback.onSuccess(token, new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        }, new KiiErrorListener<LoginCallback>(callback)
        );
        
        queue.add(request);
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
        
        KiiRequest request = new KiiRequest(Method.POST, url, appId, appKey, info, new Listener<KiiResponse>() {
            @Override
            public void onResponse(KiiResponse result) {
                try {
                    String userId = result.getString("userID");
                    callback.onSuccess(new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        }, new KiiErrorListener<SignupCallback>(callback) {

            @Override
            void onError(NetworkResponse response, String body, SignupCallback callback) {
                super.onError(response, body, callback);
                if (response.statusCode != 201) {
                    callback.onError(response.statusCode, body);
                    return;
                }
                try {
                    JSONObject result = new JSONObject(body);
                    String userId = result.getString("userID");
                    callback.onSuccess(new KiiUser(userId));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
            
        }
        );
        
        queue.add(request);
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
}
