package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

class KiiUserAPI implements UserAPI {

    private final KiiAppAPI api;
    
    KiiUserAPI(KiiAppAPI api) {
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
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, 
                null, null, null, new KiiResponseHandler<UserCallback>(callback) {
            
            @Override
            protected void onSuccess(JSONObject response, String etag, UserCallback callback) {
                try {
                    String id = response.getString("userID");
                    KiiUser user = new KiiUser(id);
                    user.replace(response);
                    callback.onSuccess(user);
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
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
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, 
                "application/vnd.kii.EmailAddressModificationRequest+json", null, json, new KiiResponseHandler<UserCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, UserCallback callback) {
                try {
                    user.put("emailAddress", newEmail);
                    callback.onSuccess(user);
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
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
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, 
                "application/vnd.kii.PhoneNumberModificationRequest+json", null, json, new ResponseHandler() {
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
                    user.put("phoneNumber", newPhone);
                    callback.onSuccess(user);
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
}
