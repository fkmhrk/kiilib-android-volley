package jp.fkmsoft.libs.kiilib.volley;

import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.QueryCondition;

import org.json.JSONArray;
import org.json.JSONException;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

class KiiVolleyBucketAPI implements BucketAPI {

    private final KiiVolleyAPI api;
    
    KiiVolleyBucketAPI(KiiVolleyAPI api) {
        this.api = api;
    }

    @Override
    public void query(final KiiBucket bucket, QueryCondition condition, final QueryCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath() + "/query";
        
        KiiRequest request = new KiiRequest(Method.POST, url, api.appId, api.appKey, api.accessToken, 
                "application/vnd.kii.QueryRequest+json", condition.toJson(), new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        try {
                            JSONArray array = arg0.getJSONArray("results");
                            KiiObjectQueryResult result = new KiiObjectQueryResult(array.length());
                            for (int i = 0 ; i < array.length() ; ++i) {
                                result.add(new KiiObject(bucket, array.getJSONObject(i)));
                            }
                            
                            callback.onSuccess(result);
                        } catch (JSONException e) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
                },
                new KiiErrorListener<QueryCallback>(callback)
        );
        
        api.queue.add(request);
    }

    @Override
    public void delete(final KiiBucket bucket, final BucketCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath();
        
        KiiRequest request = KiiRequest.createDeleteRequest (url, api.appId, api.appKey, api.accessToken, 
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        callback.onSuccess(bucket);
                    }
                }, new KiiErrorListener<BucketCallback>(callback) {

                    @Override
                    void onError(NetworkResponse response, String body, BucketCallback callback) {
                        super.onError(response, body, callback);
                        if (response.statusCode == 204) {
                            // OK
                            callback.onSuccess(bucket);
                            return;
                        }
                        callback.onError(response.statusCode, body);
                    }
        });
        
        api.queue.add(request);
    }

}
