package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class KiiGroupAPI implements GroupAPI {

    private final KiiAppAPI api;
    
    KiiGroupAPI(KiiAppAPI api) {
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
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/vnd.kii.GroupsRetrievalResponse+json");
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, 
                null, headers, null, new KiiResponseHandler<ListCallback>(callback) {

            @Override
            protected void onSuccess(JSONObject response, String etag, ListCallback callback) {
                try {
                    JSONArray array = response.getJSONArray("groups");
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
        });
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
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken, 
                "application/vnd.kii.GroupCreationRequest+json", null, json, new KiiResponseHandler<GroupCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback callback) {
                try {
                    String id = response.getString("groupID");
                    callback.onSuccess(new KiiGroup(id, groupName, owner));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }

    @Override
    public void getMembers(KiiGroup group, final MemberCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/members";
            
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/vnd.kii.MembersRetrievalResponse+json");
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, 
                null, headers, null, new KiiResponseHandler<MemberCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, MemberCallback callback) {
                try {
                    JSONArray array = response.getJSONArray("members");
                    
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
        });
    }

    @Override
    public void addMember(final KiiGroup group, final KiiUser user, final GroupCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/members/" + user.getId();
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, "", null, null, new KiiResponseHandler<GroupCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback callback) {
                group.getMembers().add(user);
                callback.onSuccess(group);
            }
        });
    }
    
    @Override
    public void changeName(final KiiGroup group, final String name, final GroupCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + group.getResourcePath() + "/name";
        
        api.getHttpClient().sendPlainTextRequest(Method.PUT, url, api.accessToken, 
                null, name, new KiiResponseHandler<GroupCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, GroupCallback callback) {
                callback.onSuccess(new KiiGroup(group.getId(), name, group.getOwner()));
            }
        });
    }
}
