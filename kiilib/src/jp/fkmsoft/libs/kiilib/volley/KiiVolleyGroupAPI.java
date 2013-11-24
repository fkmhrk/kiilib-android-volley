package jp.fkmsoft.libs.kiilib.volley;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.fkmsoft.libs.kiilib.KiiCallback;
import jp.fkmsoft.libs.kiilib.KiiGroup;
import jp.fkmsoft.libs.kiilib.GroupAPI;
import jp.fkmsoft.libs.kiilib.KiiUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

class KiiVolleyGroupAPI implements GroupAPI {

    private final KiiVolleyAPI api;
    
    KiiVolleyGroupAPI(KiiVolleyAPI api) {
        this.api = api;
    }
    
    @Override
    public void getOwnedGroup(KiiUser user, final ListCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/groups?owner=" + user.getId();
        getGroup(url, callback);
    }

    @Override
    public void getJoinedGroup(KiiUser user, ListCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + "/groups?is_member=" + user.getId();
        getGroup(url, callback);
    }
    
    private void getGroup(String url, final ListCallback callback) {
        KiiRequest request = KiiRequest.createGetRequest(url, api.appId, api.appKey, api.accessToken, 
                "application/vnd.kii.GroupsRetrievalResponse+json", new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        try {
                            JSONArray array = arg0.getJSONArray("groups");
                            List<KiiGroup> result = new ArrayList<KiiGroup>(array.length());
                            for (int i = 0 ; i < array.length() ; ++i) {
                                JSONObject json = array.getJSONObject(i);
                                String id = json.getString("groupID");
                                String name = json.getString("name");
                                String owner = json.getString("owner");
                                result.add(new KiiGroup(id, name, new KiiUser(owner)));
                            }
                            
                            callback.onSuccess(result);
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
                },
                new KiiErrorListener<ListCallback>(callback)
        );
        
        api.queue.add(request);
    }

    @Override
    public void create(final String groupName, final KiiUser owner, List<KiiUser> memberList, final GroupCallback callback) {
        if (memberList == null) { memberList = Collections.emptyList(); }
        
        JSONObject json = new JSONObject();
        try {
            json.put("name", groupName);
            json.put("owner", owner.getId());
            
            JSONArray memberArray = new JSONArray();
            for (KiiUser member : memberList) {
                memberArray.put(member.getId());
            }
            json.put("members", memberArray);
        } catch (JSONException e) {
            return;
        }
        
        String url = api.baseUrl + "/apps/" + api.appId + "/groups";
        KiiRequest request = new KiiRequest(Method.POST, url, api.appId, api.appKey, api.accessToken, 
                "application/vnd.kii.GroupCreationRequest+json", json, new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        Log.v("API", arg0.toString());
                        
                        try {
                            String id = arg0.getString("groupID");
                            callback.onSuccess(new KiiGroup(id, groupName, owner));
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
                }, new KiiErrorListener<GroupCallback>(callback) {
                    @Override
                    void onError(NetworkResponse response, String body, GroupCallback callback) {
                        super.onError(response, body, callback);
                        if (response.statusCode != 201) {
                            callback.onError(response.statusCode, body);
                            return;
                        }
                        // OK
                        try {
                            JSONObject json = new JSONObject(body);
                            String id = json.getString("groupID");
                            callback.onSuccess(new KiiGroup(id, groupName, owner));
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
        });
        
        api.queue.add(request);
    }

    @Override
    public void getMembers(KiiGroup group, final MemberCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/members";
        
        KiiRequest request = KiiRequest.createGetRequest(url, api.appId, api.appKey, api.accessToken, 
                "application/vnd.kii.MembersRetrievalResponse+json", new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        try {
                            JSONArray array = arg0.getJSONArray("members");
                            List<KiiUser> result = new ArrayList<KiiUser>(array.length());
                            for (int i = 0 ; i < array.length() ; ++i) {
                                JSONObject json = array.getJSONObject(i);
                                String id = json.getString("userID");
                                result.add(new KiiUser(id));
                            }
                            
                            callback.onSuccess(result);
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
                }, new KiiErrorListener<MemberCallback>(callback)
        );
        
        api.queue.add(request);
    }

    @Override
    public void addMember(KiiGroup group, final KiiUser user, final AddCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/members/" + user.getId();
        KiiRequest request = KiiRequest.createPutRequest(url, api.appId, api.appKey, api.accessToken, 
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        callback.onSuccess(user);
                    }
                }, new KiiErrorListener<AddCallback>(callback) {
                    @Override
                    void onError(NetworkResponse response, String body, AddCallback callback) {
                        super.onError(response, body, callback);
                        if (response.statusCode == 200 || response.statusCode == 201) {
                            // OK
                            callback.onSuccess(user);
                            return;
                        }
                        callback.onError(response.statusCode, body);
                    }
        });
        
        api.queue.add(request);
    }
    
    @Override
    public void changeName(final KiiGroup group, final String name, final GroupCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/name";
        KiiRequest request = KiiRequest.createPutRequest(url, api.appId, api.appKey, api.accessToken,name,  
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        callback.onSuccess(new KiiGroup(group.getId(), name, group.getOwner()));
                    }
                }, new KiiErrorListener<GroupCallback>(callback) {
                    @Override
                    void onError(NetworkResponse response, String body, GroupCallback callback) {
                        super.onError(response, body, callback);
                        if (response.statusCode != 200) {
                            callback.onError(response.statusCode, body);
                            return;
                        }
                        callback.onSuccess(new KiiGroup(group.getId(), name, group.getOwner()));
                    }
        });
        
        api.queue.add(request);
    }

}
