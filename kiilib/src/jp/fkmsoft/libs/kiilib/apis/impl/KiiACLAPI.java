package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.ACLAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.entities.ACLSubject;
import jp.fkmsoft.libs.kiilib.entities.AccessControllable;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class KiiACLAPI implements ACLAPI {

    private final KiiAppAPI api;
    
    KiiACLAPI(KiiAppAPI api) {
        this.api = api;
    }
    
    @Override
    public void get(AccessControllable object, final ACLGetCallback callback) {

        String url = api.baseUrl + "/apps/" + api.appId + object.getResourcePath() + "/acl";
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "*");
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, null, headers, null, new KiiResponseHandler<ACLGetCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ACLGetCallback callback) {
                try {
                    Map<String, List<ACLSubject>> map = new HashMap<String, List<ACLSubject>>(response.length());
                    @SuppressWarnings("unchecked")
                    Iterator<String> it = response.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        JSONArray array = response.getJSONArray(key);
                        List<ACLSubject> list = toSubjectList(array);
                        map.put(key, list);
                    }
                    callback.onSuccess(map);
                } catch (JSONException e) {
                     callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
            
            private List<ACLSubject> toSubjectList(JSONArray array) throws JSONException {
                List<ACLSubject> list = new ArrayList<ACLSubject>(array.length());
                for (int i = 0 ; i < array.length() ; ++i) {
                    JSONObject item = array.getJSONObject(i);
                    if (item.has("userID")) {
                        String id = item.getString("userID");
                        list.add(new KiiUser(id));
                    } else if (item.has("groupID")) {
                        String id = item.getString("groupID");
                        list.add(new KiiGroup(id, "", null));
                    }
                }
                return list;
            }
        });
    }
    
    @Override
    public void grant(final AccessControllable object, String action, ACLSubject subject, final ACLCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + object.getResourcePath() + "/acl/" + action + 
                "/" + subject.getSubjectType() + ":" + subject.getId();
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, null, null, null, new KiiResponseHandler<ACLCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ACLCallback callback) {
                callback.onSuccess(object);
            }
        });
    }

    @Override
    public void revoke(final AccessControllable object, String action, ACLSubject subject, final ACLCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + object.getResourcePath() + "/acl/" + action + 
                "/" + subject.getSubjectType() + ":" + subject.getId();
        
        api.getHttpClient().sendJsonRequest(Method.DELETE, url, api.accessToken, null, null, null, new KiiResponseHandler<ACLCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, ACLCallback callback) {
                callback.onSuccess(object);
            }
        });
    }
}
