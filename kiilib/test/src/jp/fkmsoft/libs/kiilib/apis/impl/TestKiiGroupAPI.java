package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.GroupAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;
import android.test.AndroidTestCase;

public class TestKiiGroupAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    private JSONObject createGroupJson(String groupId, String name, String ownerId) throws Exception{
        JSONObject json = new JSONObject();
        json.put("groupID", groupId);
        json.put("name", name);
        json.put("owner", ownerId);
        return json;
    }
    
    private JSONObject createUser(String userId) throws Exception {
        JSONObject json = new JSONObject();
        json.put("userID", userId);
        return json;
    }
    
    public void test_0000_getOwnedGroup() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        GroupAPI groupAPI = api.groupAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(createGroupJson("group1234", "group01", "user1234"));
        array.put(createGroupJson("group5678", "group02", "user3456"));
        respBody.put("groups", array);
        api.client.addResponse(200, respBody, "");
        
        KiiUser user = new KiiUser("user1234");
        groupAPI.getOwnedGroup(user, new GroupAPI.ListCallback() {
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }

            @Override
            public void onSuccess(List<KiiGroup> result) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups?owner=user1234", args.url);
                assertNull(args.body);
                
                // output
                assertEquals(2, result.size());
                KiiGroup group = result.get(0);
                assertEquals("group1234", group.getId());
                assertEquals("group01", group.getName());
                assertEquals("user1234", group.getOwner().getId());
                group = result.get(1);
                assertEquals("group5678", group.getId());
                assertEquals("group02", group.getName());
                assertEquals("user3456", group.getOwner().getId());
            }
        });
    }
    
    public void test_0100_getJoinedGroup() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        GroupAPI groupAPI = api.groupAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(createGroupJson("group1234", "group01", "user1234"));
        array.put(createGroupJson("group5678", "group02", "user3456"));
        respBody.put("groups", array);
        api.client.addResponse(200, respBody, "");
        
        KiiUser user = new KiiUser("user1234");
        groupAPI.getJoinedGroup(user, new GroupAPI.ListCallback() {
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }

            @Override
            public void onSuccess(List<KiiGroup> result) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups?is_member=user1234", args.url);
                assertNull(args.body);
                
                // output
                assertEquals(2, result.size());
                KiiGroup group = result.get(0);
                assertEquals("group1234", group.getId());
                assertEquals("group01", group.getName());
                assertEquals("user1234", group.getOwner().getId());
                group = result.get(1);
                assertEquals("group5678", group.getId());
                assertEquals("group02", group.getName());
                assertEquals("user3456", group.getOwner().getId());
            }
        });
    }
    
    public void test_0200_create() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        GroupAPI groupAPI = api.groupAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("groupID", "group1234");
        api.client.addResponse(201, respBody, "");
        
        String groupName = "group01";
        KiiUser owner = new KiiUser("user1234");
        groupAPI.create(groupName, owner, null, new GroupAPI.GroupCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiGroup group) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups", args.url);
                assertEquals(3, args.body.length());
                assertEquals("group01", args.body.optString("name"));
                assertEquals("user1234", args.body.optString("owner"));
                assertEquals(0, args.body.optJSONArray("members").length());
                
                // output
                assertEquals("group1234", group.getId());
                assertEquals("group01", group.getName());
                assertEquals("user1234", group.getOwner().getId());
            }
        });
    }
    
    public void test_0300_getMembers() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        GroupAPI groupAPI = api.groupAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(createUser("user1234"));
        array.put(createUser("user5678"));
        respBody.put("members", array);
        api.client.addResponse(200, respBody, "");
        
        KiiGroup group = new KiiGroup("group1234");
        groupAPI.getMembers(group, new GroupAPI.MemberCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(List<KiiUser> memberList) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/members", args.url);
                assertNull(args.body);
                
                // output
                assertEquals(2, memberList.size());
                KiiUser member = memberList.get(0);
                assertEquals("user1234", member.getId());
                member = memberList.get(1);
                assertEquals("user5678", member.getId());
            }
        });
    }
    
    public void test_0400_addMember() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        GroupAPI groupAPI = api.groupAPI();
        
        // set mock
        api.client.addResponse(204, null, "'");
        
        KiiGroup group = new KiiGroup("group1234");
        KiiUser user = new KiiUser("user1234");
        groupAPI.addMember(group, user, new GroupAPI.GroupCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiGroup group) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/members/user1234", args.url);
                assertNull(args.body);
                
                // output
                assertEquals(1, group.getMembers().size());
                KiiUser member = group.getMembers().get(0);
                assertEquals("user1234", member.getId());
            }
        });
    }
    
    public void test_0500_changeName() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        GroupAPI groupAPI = api.groupAPI();
        
        // set mock
        api.client.addResponse(204, null, "'");
        
        KiiGroup group = new KiiGroup("group1234");
        String name = "newGroup";
        groupAPI.changeName(group, name, new GroupAPI.GroupCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiGroup group) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/name", args.url);
                assertNull(args.body);
                assertEquals("newGroup", args.plainBody);
                
                // output
                assertEquals("newGroup", group.getName());
            }
        });
    }
}
