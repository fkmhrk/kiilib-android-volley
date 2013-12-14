package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.QueryResult;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiClause;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.QueryParams;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;
import android.test.AndroidTestCase;

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
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        BucketAPI bucketAPI = api.bucketAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(createObjectJson("object1234", 1234L, 5678L, "user1234"));
        array.put(createObjectJson("object5678", 5678L, 5678L, "user2345"));
        respBody.put("results", array);
        api.client.addResponse(200, respBody, "");
        
        KiiBucket bucket = new KiiBucket(null, "sharedBucket");
        QueryParams params = new QueryParams(KiiClause.all());
        bucketAPI.query(bucket, params, new BucketAPI.QueryCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(QueryResult result) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket/query", args.url);
                assertEquals(1, args.body.length());
                JSONObject query = args.body.optJSONObject("bucketQuery"); 
                assertEquals(1, query.length());
                JSONObject clause = query.optJSONObject("clause");
                assertEquals(1, clause.length());
                assertEquals("all", clause.optString("type"));
                
                // output
                assertEquals(2, result.size());
                KiiObject obj = result.get(0);
                assertEquals("object1234", obj.getId());
                obj = result.get(1);
                assertEquals("object5678", obj.getId());
            }
        });
    }
    
    public void test_0100_delete() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        BucketAPI bucketAPI = api.bucketAPI();
        
        // set mock
        api.client.addResponse(204, null, "");
        
        KiiBucket bucket = new KiiBucket(null, "sharedBucket");
        bucketAPI.delete(bucket, new BucketAPI.BucketCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiBucket bucket) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.DELETE, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/buckets/sharedBucket", args.url);
                assertNull(args.body);
                
                // output
                assertEquals("sharedBucket", bucket.getName());
            }
        });
    }
}
