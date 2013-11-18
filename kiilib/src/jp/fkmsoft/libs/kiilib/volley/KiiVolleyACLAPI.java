package jp.fkmsoft.libs.kiilib.volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.ACLSubject;
import jp.fkmsoft.libs.kiilib.AccessControllable;
import jp.fkmsoft.libs.kiilib.KiiACLAPI;
import jp.fkmsoft.libs.kiilib.KiiCallback;
import jp.fkmsoft.libs.kiilib.KiiGroup;
import jp.fkmsoft.libs.kiilib.KiiUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.Listener;

class KiiVolleyACLAPI implements KiiACLAPI {

    private final KiiVolleyAPI api;
    
    KiiVolleyACLAPI(KiiVolleyAPI api) {
        this.api = api;
    }
    
    @Override
    public void get(AccessControllable object, final ACLGetCallback callback) {

        String url = api.baseUrl + "/apps/" + api.appId + object.getResourcePath() + "/acl";
        
        KiiRequest request = KiiRequest.createGetRequest (url, api.appId, api.appKey, api.accessToken, 
                "*", new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse json) {
                        try {
                            Map<String, List<ACLSubject>> map = new HashMap<String, List<ACLSubject>>(json.length());
                            @SuppressWarnings("unchecked")
                            Iterator<String> it = json.keys();
                            while (it.hasNext()) {
                                String key = it.next();
                                JSONArray array = json.getJSONArray(key);
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
                }, new KiiErrorListener<ACLGetCallback>(callback)
        );
        
        api.queue.add(request);
    }
    
    @Override
    public void grant(final AccessControllable object, String action, ACLSubject subject, final ACLCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + object.getResourcePath() + "/acl/" + action + 
                "/" + subject.getSubjectType() + ":" + subject.getId();
        
        KiiRequest request = KiiRequest.createPutRequest(url, api.appId, api.appKey, api.accessToken, 
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse json) {
                        callback.onSuccess(object);
                    }
                }, new KiiErrorListener<ACLCallback>(callback)
        );
        
        api.queue.add(request);
    }

    @Override
    public void revoke(final AccessControllable object, String action, ACLSubject subject, final ACLCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + object.getResourcePath() + "/acl/" + action + 
                "/" + subject.getSubjectType() + ":" + subject.getId();
        
        KiiRequest request = KiiRequest.createDeleteRequest(url, api.appId, api.appKey, api.accessToken, 
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse json) {
                        callback.onSuccess(object);
                    }
                }, new KiiErrorListener<ACLCallback>(callback)
        );
        
        api.queue.add(request);
    }

}
