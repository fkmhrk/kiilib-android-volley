package jp.fkmsoft.libs.kiilib.volley;

import java.util.HashMap;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.KiiBucket;
import jp.fkmsoft.libs.kiilib.KiiCallback;
import jp.fkmsoft.libs.kiilib.KiiObject;
import jp.fkmsoft.libs.kiilib.KiiObjectAPI;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

class KiiVolleyObjectAPI implements KiiObjectAPI {

    private final KiiVolleyAPI api;
    
    KiiVolleyObjectAPI(KiiVolleyAPI api) {
        this.api = api;
    }
    
    @Override
    public void getById(final KiiBucket bucket, String id, final ObjectCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath() + "/objects/" + id;
        KiiRequest request = KiiRequest.createGetRequest(url, api.appId, api.appKey, api.accessToken, null, 
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        try {
                            callback.onSuccess(new KiiObject(bucket, arg0));
                        } catch (JSONException e) {
                            callback.onError(-1, e.getMessage());
                        }
                    }
                }, new KiiErrorListener<ObjectCallback>(callback) {
                    @Override
                    void onError(NetworkResponse response, String body, ObjectCallback callback) {
                        callback.onError(response.statusCode, body);
                    }
        });
        
        api.queue.add(request);        
    }
    
    @Override
    public void create(final KiiObject obj, final ObjectCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + obj.getBucket().getResourcePath() + "/objects";
        KiiRequest request = new KiiRequest(Method.POST, url, api.appId, api.appKey, api.accessToken, 
                "application/json", obj, new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        try {
                            callback.onSuccess(new KiiObject(obj.getBucket(), arg0));
                        } catch (JSONException e) {
                            callback.onError(-1, e.getMessage());
                        }
                    }
                }, new KiiErrorListener<ObjectCallback>(callback) {
                    @Override
                    void onError(NetworkResponse response, String body, ObjectCallback callback) {
                        if (response.statusCode != 201) {
                            callback.onError(response.statusCode, body);
                            return;
                        }
                        // OK
                        try {
                            JSONObject json = new JSONObject(body);
                            obj.setCreatedTime(json.getLong("createdAt"));
                            obj.put("_id", json.getString("objectID"));
                            callback.onSuccess(new KiiObject(obj.getBucket(), obj));
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
        });
        
        api.queue.add(request);
    }

    @Override
    public void update(final KiiObject obj, final ObjectCallback callback) {

        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        KiiRequest request = new KiiRequest(Method.PUT, url, api.appId, api.appKey, api.accessToken, 
                "application/json", obj, new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        Log.v("API", arg0.toString());
                        
                        long modifiedTime = arg0.optLong("modifiedAt", -1);
                        if (modifiedTime != -1) {
                            obj.setModifiedTime(modifiedTime);
                        }
                        obj.setVersion(arg0.getEtag());
                        callback.onSuccess(obj);
                    }
                }, new KiiErrorListener<ObjectCallback>(callback) {
                    
                    @Override
                    void onError(NetworkResponse response, String body, ObjectCallback callback) {
                        super.onError(response, body, callback);
                        if (response.statusCode != 201) {
                            callback.onError(response.statusCode, body);
                            return;
                        }
                        // OK
                        try {
                            JSONObject json = new JSONObject(body);
                            callback.onSuccess(new KiiObject(obj.getBucket(), json));
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
        });
        
        api.queue.add(request);
    }

    @Override
    public void updatePatch(final KiiObject obj, JSONObject patch, final ObjectCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-HTTP-Method-Override", "PATCH");
        KiiRequest request = KiiRequest.createPostRequest(url, api.appId, api.appKey, api.accessToken, 
                "application/json", headers, obj, new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        Log.v("API", arg0.toString());
                        
                        long modifiedTime = arg0.optLong("modifiedAt", -1);
                        if (modifiedTime != -1) {
                            obj.setModifiedTime(modifiedTime);
                        }
                        obj.setVersion(arg0.getEtag());
                        callback.onSuccess(obj);
                    }
                }, new KiiErrorListener<ObjectCallback>(callback) {
                    
                    @Override
                    void onError(NetworkResponse response, String body, ObjectCallback callback) {
                        super.onError(response, body, callback);
                        if (response.statusCode != 201) {
                            callback.onError(response.statusCode, body);
                            return;
                        }
                        // OK
                        try {
                            JSONObject json = new JSONObject(body);
                            callback.onSuccess(new KiiObject(obj.getBucket(), json));
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
        });
        
        api.queue.add(request);        
    }
    
    @Override
    public void delete(final KiiObject obj, final ObjectCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + obj.getResourcePath();
        KiiRequest request = KiiRequest.createDeleteRequest(url, api.appId, api.appKey, api.accessToken, 
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        callback.onSuccess(obj);
                    }
                }, new KiiErrorListener<ObjectCallback>(callback) {
                    @Override
                    void onError(NetworkResponse response, String body, ObjectCallback callback) {
                        super.onError(response, body, callback);
                        if (response.statusCode != 204) {
                            callback.onError(response.statusCode, body);
                            return;
                        }
                        // OK
                        callback.onSuccess(obj);
                    }
        });
        
        api.queue.add(request);
    }

}
