package jp.fkmsoft.libs.kiilib.volley;

import jp.fkmsoft.libs.kiilib.KiiCallback;
import jp.fkmsoft.libs.kiilib.KiiUser;
import jp.fkmsoft.libs.kiilib.KiiUserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

class KiiVolleyUserAPI implements KiiUserAPI {

    private final KiiVolleyAPI api;
    
    KiiVolleyUserAPI(KiiVolleyAPI api) {
        this.api = api;
    }
    
    @Override
    public void findUserByUsername(String username, UserCallback callback) {
        findUser("LOGIN_NAME:" + username, callback);
    }

    @Override
    public void findUserByEmail(String email, UserCallback callback) {
        findUser("EMAIL:" + email, callback);
    }

    @Override
    public void findUserByPhone(String phone, UserCallback callback) {
        findUser("PHONE:" + phone, callback);
    }
    
    private void findUser(String identifier, final UserCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/users/" + identifier;
        
        KiiRequest request = new KiiRequest(Method.GET, url, api.appId, api.appKey, api.accessToken, 
                null, (String)null, new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        Log.v("API", arg0.toString());
                        try {
                            String id = arg0.getString("userID");
                            KiiUser user = new KiiUser(id);
                            user.replace(arg0);
                            callback.onSuccess(user);
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
                }, new KiiErrorListener<KiiCallback>(callback)
        );
        
        api.queue.add(request);
    }

    @Override
    public void updateEmail(final KiiUser user, final String newEmail, boolean verified, final UserCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + user.getResourcePath() + "/email-address";
        JSONObject json = new JSONObject();
        try {
            json.put("emailAddress", newEmail);
            if (verified) {
                json.put("verified", true);
            }
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        KiiRequest request = new KiiRequest(Method.PUT, url, api.appId, api.appKey, api.accessToken, 
                "application/vnd.kii.EmailAddressModificationRequest+json", json, new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        try {
                            user.put("emailAddress", newEmail);
                            callback.onSuccess(user);
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
                }, new KiiErrorListener<UserCallback>(callback) {
                    @Override
                    void onError(NetworkResponse response, String body, UserCallback callback) {
                        if (response.statusCode != 204) {
                            callback.onError(response.statusCode, body);
                            return;
                        }
                        // OK
                        try {
                            user.put("emailAddress", newEmail);
                            callback.onSuccess(user);
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
        });
        
        api.queue.add(request);
    }

    @Override
    public void updatePhone(final KiiUser user, final String newPhone, boolean verified, final UserCallback callback) {

        String url = api.baseUrl + "/apps/" + api.appId + user.getResourcePath() + "/phone-number";
        JSONObject json = new JSONObject();
        try {
            json.put("phoneNumber", newPhone);
            if (verified) {
                json.put("verified", true);
            }
        } catch (JSONException e) {
            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
            return;
        }
        KiiRequest request = new KiiRequest(Method.PUT, url, api.appId, api.appKey, api.accessToken, 
                "application/vnd.kii.PhoneNumberModificationRequest+json", json, new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        try {
                            user.put("phoneNumber", newPhone);
                            callback.onSuccess(user);
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
                }, new KiiErrorListener<UserCallback>(callback) {
                    @Override
                    void onError(NetworkResponse response, String body, UserCallback callback) {
                        super.onError(response, body, callback);
                        if (response.statusCode != 204) {
                            callback.onError(response.statusCode, body);
                            return;
                        }
                        // OK
                        try {
                            user.put("phoneNumber", newPhone);
                            callback.onSuccess(user);
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
        });
        
        api.queue.add(request);
    }

}
