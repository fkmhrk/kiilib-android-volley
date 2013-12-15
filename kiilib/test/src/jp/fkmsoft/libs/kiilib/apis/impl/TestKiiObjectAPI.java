package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;
import android.test.AndroidTestCase;

public class TestKiiObjectAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    public void test_0000_getById() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        ObjectAPI objectAPI = api.objectAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("_id", "object1234");
        respBody.put("name", "fkm");
        respBody.put("score", 1122);
        api.client.addResponse(200, respBody, "");
        
        KiiBucket bucket = new KiiBucket(null, "sharedBucket");
        String id = "object1234";
        objectAPI.getById(bucket, id, new ObjectAPI.ObjectCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiObject obj) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects/object1234", args.url);
                assertNull(args.body);
                
                // output
                assertEquals("object1234", obj.getId());
                assertEquals("fkm", obj.optString("name"));
                assertEquals(1122, obj.optLong("score"));
            }
        });
    }
    
    public void test_0100_create() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        ObjectAPI objectAPI = api.objectAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("objectID", "object1234");
        respBody.put("createdAt", 1234);
        api.client.addResponse(201, respBody, "");
        
        KiiBucket bucket = new KiiBucket(null, "sharedBucket");
        JSONObject obj = new JSONObject();
        obj.put("name", "fkm");
        obj.put("score", 1122);
        objectAPI.create(bucket, obj, new ObjectAPI.ObjectCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiObject obj) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects", args.url);
                assertEquals(3, args.body.length());
                assertEquals("fkm", args.body.optString("name"));
                assertEquals(1122, args.body.optInt("score"));
                assertEquals("object1234", args.body.optString("_id"));
                
                // output
                assertEquals("object1234", obj.getId());
                assertEquals("fkm", obj.optString("name"));
                assertEquals(1122, obj.optLong("score"));
                assertEquals(1234L, obj.getCreatedTime());
            }
        });
    }
    
    public void test_0200_update() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        ObjectAPI objectAPI = api.objectAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("modifiedAt", 2345);
        api.client.addResponse(200, respBody, "");
        
        KiiBucket bucket = new KiiBucket(null, "sharedBucket");
        KiiObject obj = new KiiObject(bucket, "object1234");
        obj.put("name", "fkm");
        obj.put("score", 2233);
        objectAPI.update(obj, new ObjectAPI.ObjectCallback() {
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiObject obj) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects/object1234", args.url);
                assertEquals(3, args.body.length());
                assertEquals("fkm", args.body.optString("name"));
                assertEquals(2233, args.body.optInt("score"));
                assertEquals(2345, args.body.optLong("_modified"));
                
                // output
                assertEquals("object1234", obj.getId());
                assertEquals("fkm", obj.optString("name"));
                assertEquals(2233, obj.optLong("score"));
                assertEquals(2345L, obj.getModifiedTime());
            }
        });
    }
    
    public void test_0300_updatePatch() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        ObjectAPI objectAPI = api.objectAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("modifiedAt", 2345);
        api.client.addResponse(200, respBody, "");
        
        KiiBucket bucket = new KiiBucket(null, "sharedBucket");
        KiiObject obj = new KiiObject(bucket, "object1234");
        obj.put("name", "fkm");
        obj.put("score", 2233);
        JSONObject patch = new JSONObject();
        patch.put("level", 2);
        objectAPI.updatePatch(obj, patch, new ObjectAPI.ObjectCallback() {
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiObject obj) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects/object1234", args.url);
                assertEquals(1, args.body.length());
                assertEquals(2, args.body.optInt("level"));
                
                // output
                assertEquals("object1234", obj.getId());
                assertEquals("fkm", obj.optString("name"));
                assertEquals(2233, obj.optLong("score"));
                assertEquals(2, obj.optInt("level"));
                assertEquals(2345L, obj.getModifiedTime());
            }
        });
    }
    
    public void test_0400_delete() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        ObjectAPI objectAPI = api.objectAPI();
        
        // set mock
        api.client.addResponse(204, null, "");
        
        KiiBucket bucket = new KiiBucket(null, "sharedBucket");
        KiiObject obj = new KiiObject(bucket, "object1234");
        objectAPI.delete(obj, new ObjectAPI.ObjectCallback() {
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiObject obj) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.DELETE, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects/object1234", args.url);
                assertNull(args.body);
                
                // output
                assertEquals("object1234", obj.getId());
            }
        });
    }
}
