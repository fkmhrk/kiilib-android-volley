package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;

import org.json.JSONException;
import org.json.JSONObject;

class KiiObjectAPI implements ObjectAPI {

    private final KiiAppAPI api;
    
    KiiObjectAPI(KiiAppAPI api) {
        this.api = api;
    }
    
    @Override
    public void getById(final KiiBucket bucket, String id, ObjectCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath() + "/objects/" + id;
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, 
                null, null, null, new KiiResponseHandler<ObjectCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback callback) {
                try {
                    callback.onSuccess(new KiiObject(bucket, response));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }
    
    @Override
    public void create(final KiiBucket bucket, final JSONObject obj, final ObjectCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath() + "/objects";
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken,
                "application/json", null, obj, new KiiResponseHandler<ObjectCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback callback) {
                try {
                    long createdTime = response.getLong("createdAt");
                    String id = response.getString("objectID");
                    obj.put("_id", id);
                    
                    KiiObject kiiObj = new KiiObject(bucket, obj);
                    kiiObj.setCreatedTime(createdTime);
                    
                    callback.onSuccess(kiiObj);
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }

    @Override
    public void update(final KiiObject obj, final ObjectCallback callback) {

        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken,
                "application/json", null, obj, new KiiResponseHandler<ObjectCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback callback) {
                long modifiedTime = response.optLong("modifiedAt", -1);
                if (modifiedTime != -1) {
                    obj.setModifiedTime(modifiedTime);
                }
                obj.setVersion(etag);
                callback.onSuccess(obj);
            }
        });
    }

    @Override
    public void updatePatch(final KiiObject obj, final JSONObject patch, final ObjectCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-HTTP-Method-Override", "PATCH");
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken,
                "application/json", headers, patch, new KiiResponseHandler<ObjectCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback callback) {
                // copy to obj
                @SuppressWarnings("unchecked")
                Iterator<String> keys = patch.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        obj.put(key, patch.opt(key));
                    } catch (JSONException e) {
                        // nop
                    }
                }
                long modifiedTime = response.optLong("modifiedAt", -1);
                if (modifiedTime != -1) {
                    obj.setModifiedTime(modifiedTime);
                }
                obj.setVersion(etag);
                callback.onSuccess(obj);
            }
        });
    }
    
    @Override
    public void delete(final KiiObject obj, final ObjectCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        
        api.getHttpClient().sendJsonRequest(Method.DELETE, url, api.accessToken, 
                null, null, null, new KiiResponseHandler<ObjectCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ObjectCallback callback) {
                callback.onSuccess(obj);
            }
        });
    }
}
