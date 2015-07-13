package jp.fkmsoft.libs.kiilib.apis.impl;

import android.test.AndroidTestCase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.ACLAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.ACLSubject;
import jp.fkmsoft.libs.kiilib.entities.AccessControllable;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiBucket;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiGroupDTO;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUserDTO;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;

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
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        ACLAPI aclAPI = new KiiACLAPI(context);
        
        // set mock
        JSONObject respBody = new JSONObject();
        
        JSONArray queryArray = new JSONArray();
        queryArray.put(createGroupEntry("group1234"));
        respBody.put("QUERY_OBJECTS_IN_BUCKET", queryArray);
        
        JSONArray createArray = new JSONArray();
        createArray.put(createUserEntry("user1234"));
        respBody.put("CREATE_OBJECTS_IN_BUCKET", createArray);
        
        context.mClient.addResponse(200, respBody, "");
        
        KiiBucket bucket = new BasicKiiBucket(null, "testBucket");
        aclAPI.get(bucket, BasicKiiUserDTO.getInstance(), BasicKiiGroupDTO.getInstance(), new ACLAPI.ACLGetCallback() {
            @Override
            public void onSuccess(Map<String, List<ACLSubject>> result) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.GET, args.method);
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

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getBody() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0100_grant() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        ACLAPI aclAPI = new KiiACLAPI(context);

        // set mock
        context.mClient.addResponse(204, null, "");
        
        KiiBucket bucket = new BasicKiiBucket(null, "testBucket");
        KiiUser user = new BasicKiiUser("user1234", null);
        aclAPI.grant(bucket, "QUERY_OBJECTS_IN_BUCKET", user, new ACLAPI.ACLCallback() {
            @Override
            public void onSuccess(AccessControllable object) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/testBucket/acl/QUERY_OBJECTS_IN_BUCKET/UserID:user1234", args.url);
                assertNull(args.body);
                
                // output
                assertNotNull(object);
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0200_revoke() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        ACLAPI aclAPI = new KiiACLAPI(context);

        // set mock
        context.mClient.addResponse(204, null, "");
        
        KiiBucket bucket = new BasicKiiBucket(null, "testBucket");
        KiiUser user = new BasicKiiUser("user1234", null);
        aclAPI.revoke(bucket, "QUERY_OBJECTS_IN_BUCKET", user, new ACLAPI.ACLCallback() {
            @Override
            public void onSuccess(AccessControllable object) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.DELETE, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/testBucket/acl/QUERY_OBJECTS_IN_BUCKET/UserID:user1234", args.url);
                assertNull(args.body);
                
                // output
                assertNotNull(object);
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
}
