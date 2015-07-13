package jp.fkmsoft.libs.kiilib.apis.impl;

import android.test.AndroidTestCase;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiClause;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiItemCallback;
import jp.fkmsoft.libs.kiilib.apis.QueryParams;
import jp.fkmsoft.libs.kiilib.apis.QueryResult;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiBucket;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiObject;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiObjectDTO;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;

public class TestKiiBucketAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    private JSONObject createObjectJson(String id, long createdTime, long modifiedTime, String ownerId) throws Exception {
        JSONObject json = new JSONObject();
        json.put("_id", id);
        json.put("_created", createdTime);
        json.put("_modified", modifiedTime);
        json.put("_owner", ownerId);
        return json;
    }
    
    public void test_0000_query() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        BucketAPI bucketAPI = new KiiBucketAPI(context);
        
        // set mock
        JSONObject respBody = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(createObjectJson("object1234", 1234L, 5678L, "user1234"));
        array.put(createObjectJson("object5678", 5678L, 5678L, "user2345"));
        respBody.put("results", array);
        context.mClient.addResponse(200, respBody, "");
        
        KiiBucket bucket = new BasicKiiBucket(null, "sharedBucket");
        QueryParams params = new QueryParams(KiiClause.all());
        bucketAPI.query(bucket, params, BasicKiiObjectDTO.getInstance(), new BucketAPI.QueryCallback<BasicKiiObject>() {
            @Override
            public void onSuccess(QueryResult<BasicKiiObject> result) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/query", args.url);
                assertEquals(1, args.body.length());
                JSONObject query = args.body.optJSONObject("bucketQuery");
                assertEquals(1, query.length());
                JSONObject clause = query.optJSONObject("clause");
                assertEquals(1, clause.length());
                assertEquals("all", clause.optString("type"));

                // output
                assertEquals(2, result.size());
                BasicKiiObject obj = result.get(0);
                assertEquals("object1234", obj.getId());
                obj = result.get(1);
                assertEquals("object5678", obj.getId());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0100_delete() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        BucketAPI bucketAPI = new KiiBucketAPI(context);

        // set mock
        context.mClient.addResponse(204, null, "");
        
        final KiiBucket bucket = new BasicKiiBucket(null, "sharedBucket");
        bucketAPI.delete(bucket, new KiiItemCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.DELETE, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket", args.url);
                assertNull(args.body);

                // output
                assertEquals("sharedBucket", bucket.getName());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
}
