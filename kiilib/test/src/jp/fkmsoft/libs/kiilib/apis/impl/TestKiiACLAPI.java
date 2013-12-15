package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.ACLAPI;
import jp.fkmsoft.libs.kiilib.entities.ACLSubject;
import jp.fkmsoft.libs.kiilib.entities.AccessControllable;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;
import android.test.AndroidTestCase;

public class TestKiiACLAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    private JSONObject createUserEntry(String userId) throws Exception {
        JSONObject json = new JSONObject();
        json.put("userID", userId);
        return json;
    }
    
    private JSONObject createGroupEntry(String groupId) throws Exception {
        JSONObject json = new JSONObject();
        json.put("groupID", groupId);
        return json;
    }
    
    public void test_0000_get() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        ACLAPI aclAPI = api.aclAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        
        JSONArray queryArray = new JSONArray();
        queryArray.put(createGroupEntry("group1234"));
        respBody.put("QUERY_OBJECTS_IN_BUCKET", queryArray);
        
        JSONArray createArray = new JSONArray();
        createArray.put(createUserEntry("user1234"));
        respBody.put("CREATE_OBJECTS_IN_BUCKET", createArray);
        
        api.client.addResponse(200, respBody, "");
        
        KiiBucket bucket = new KiiBucket(null, "testBucket");
        aclAPI.get(bucket, new ACLAPI.ACLGetCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(Map<String, List<ACLSubject>> result) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/testBucket/acl", args.url);
                assertNull(args.body);
                
                // output
                assertEquals(2, result.size());
                List<ACLSubject> list = result.get("QUERY_OBJECTS_IN_BUCKET");
                assertEquals(1, list.size());
                ACLSubject subject = list.get(0);
                assertTrue(subject instanceof KiiGroup);
                
                list = result.get("CREATE_OBJECTS_IN_BUCKET");
                assertEquals(1, list.size());
                subject = list.get(0);
                assertTrue(subject instanceof KiiUser);
                
            }
        });
        
    }
    
    public void test_0100_grant() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        ACLAPI aclAPI = api.aclAPI();
        
        // set mock
        api.client.addResponse(204, null, "");
        
        KiiBucket bucket = new KiiBucket(null, "testBucket");
        KiiUser user = new KiiUser("user1234");
        aclAPI.grant(bucket, "QUERY_OBJECTS_IN_BUCKET", user, new ACLAPI.ACLCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(AccessControllable object) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/testBucket/acl/QUERY_OBJECTS_IN_BUCKET/UserID:user1234", args.url);
                assertNull(args.body);
                
                // output
                assertNotNull(object);
            }
        });
    }
    
    public void test_0200_revoke() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        ACLAPI aclAPI = api.aclAPI();
        
        // set mock
        api.client.addResponse(204, null, "");
        
        KiiBucket bucket = new KiiBucket(null, "testBucket");
        KiiUser user = new KiiUser("user1234");
        aclAPI.revoke(bucket, "QUERY_OBJECTS_IN_BUCKET", user, new ACLAPI.ACLCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(AccessControllable object) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.DELETE, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/testBucket/acl/QUERY_OBJECTS_IN_BUCKET/UserID:user1234", args.url);
                assertNull(args.body);
                
                // output
                assertNotNull(object);
            }
        });
    }
}
