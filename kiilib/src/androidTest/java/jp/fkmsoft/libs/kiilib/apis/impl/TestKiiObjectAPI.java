package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.ObjectAPI;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiBucket;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiObject;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiObjectDTO;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;
import android.test.AndroidTestCase;

public class TestKiiObjectAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    public void test_0000_getById() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        ObjectAPI objectAPI = new KiiObjectAPI(context);
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("_id", "object1234");
        respBody.put("name", "fkm");
        respBody.put("score", 1122);
        context.mClient.addResponse(200, respBody, "");
        
        KiiBucket bucket = new BasicKiiBucket(null, "sharedBucket");
        String id = "object1234";
        objectAPI.getById(bucket, id, BasicKiiObjectDTO.getInstance(), new ObjectAPI.ObjectCallback<BasicKiiObject>() {
            @Override
            public void onSuccess(BasicKiiObject obj) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects/object1234", args.url);
                assertNull(args.body);

                // output
                assertEquals("object1234", obj.getId());
                assertEquals("fkm", obj.optString("name"));
                assertEquals(1122, obj.optLong("score"));
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0100_create() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        ObjectAPI objectAPI = new KiiObjectAPI(context);

        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("objectID", "object1234");
        respBody.put("createdAt", 1234);
        context.mClient.addResponse(201, respBody, "");
        
        KiiBucket bucket = new BasicKiiBucket(null, "sharedBucket");
        JSONObject obj = new JSONObject();
        obj.put("name", "fkm");
        obj.put("score", 1122);
        objectAPI.create(bucket, obj, BasicKiiObjectDTO.getInstance(), new ObjectAPI.ObjectCallback<BasicKiiObject>() {
            @Override
            public void onSuccess(BasicKiiObject obj) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects", args.url);
                assertEquals(3, args.body.length());
                assertEquals("fkm", args.body.optString("name"));
                assertEquals(1122, args.body.optInt("score"));
                assertEquals("object1234", args.body.optString("_id"));

                // output
                assertEquals("object1234", obj.getId());
                assertEquals("fkm", obj.optString("name"));
                assertEquals(1122, obj.optLong("score"));
//                assertEquals(1234L, obj.getCreatedTime());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0200_update() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        ObjectAPI objectAPI = new KiiObjectAPI(context);

        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("modifiedAt", 2345);
        context.mClient.addResponse(200, respBody, "");
        
        KiiBucket bucket = new BasicKiiBucket(null, "sharedBucket");
        BasicKiiObject obj = new BasicKiiObject(bucket, "object1234", "1", null);
        obj.put("name", "fkm");
        obj.put("score", 2233);
        objectAPI.save(obj, new ObjectAPI.ObjectCallback<KiiObject>() {
            @Override
            public void onSuccess(KiiObject obj) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects/object1234", args.url);
                assertEquals(3, args.body.length());
                assertEquals("fkm", args.body.optString("name"));
                assertEquals(2233, args.body.optInt("score"));
                assertEquals(2345, args.body.optLong("_modified"));

                // output
                assertEquals("object1234", obj.getId());
                assertEquals("fkm", ((BasicKiiObject) obj).optString("name"));
                assertEquals(2233, ((BasicKiiObject) obj).optLong("score"));
//                assertEquals(2345L, obj.getModifiedTime());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0300_updatePatch() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        ObjectAPI objectAPI = new KiiObjectAPI(context);

        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("modifiedAt", 2345);
        context.mClient.addResponse(200, respBody, "");
        
        KiiBucket bucket = new BasicKiiBucket(null, "sharedBucket");
        BasicKiiObject obj = new BasicKiiObject(bucket, "object1234", "1", null);
        obj.put("name", "fkm");
        obj.put("score", 2233);
        JSONObject patch = new JSONObject();
        patch.put("level", 2);
        objectAPI.updatePatch(obj, patch, new ObjectAPI.ObjectCallback<KiiObject>() {
            @Override
            public void onSuccess(KiiObject obj) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects/object1234", args.url);
                assertEquals(1, args.body.length());
                assertEquals(2, args.body.optInt("level"));

                // output
                assertEquals("object1234", obj.getId());
                assertEquals("fkm", ((BasicKiiObject) obj).optString("name"));
                assertEquals(2233, ((BasicKiiObject) obj).optLong("score"));
                assertEquals(2, ((BasicKiiObject) obj).optInt("level"));
//                assertEquals(2345L, obj.getModifiedTime());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }

        });
    }
    
    public void test_0400_delete() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        ObjectAPI objectAPI = new KiiObjectAPI(context);

        // set mock
        context.mClient.addResponse(204, null, "");
        
        KiiBucket bucket = new BasicKiiBucket(null, "sharedBucket");
        final KiiObject obj = new BasicKiiObject(bucket, "object1234", "1", null);
        objectAPI.delete(obj, new KiiItemCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.DELETE, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/objects/object1234", args.url);
                assertNull(args.body);

                // output
                assertEquals("object1234", obj.getId());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
}
