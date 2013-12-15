package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.apis.BucketAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.QueryParams;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class KiiBucketAPI implements BucketAPI {

    private final KiiAppAPI api;
    
    KiiBucketAPI(KiiAppAPI api) {
        this.api = api;
    }

    @Override
    public void query(final KiiBucket bucket, QueryParams condition, final QueryCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath() + "/query";
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken, 
                "application/vnd.kii.QueryRequest+json", null, condition.toJson(), new KiiResponseHandler<QueryCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, QueryCallback callback) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    
                    KiiObjectQueryResult result = new KiiObjectQueryResult(array.length());
                    for (int i = 0 ; i < array.length() ; ++i) {
                        result.add(new KiiObject(bucket, array.getJSONObject(i)));
                    }
                    
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }

    @Override
    public void delete(final KiiBucket bucket, BucketCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + bucket.getResourcePath();
        
        api.getHttpClient().sendJsonRequest(Method.DELETE, url, api.accessToken, 
                null, null, null, new KiiResponseHandler<BucketCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, BucketCallback callback) {
                callback.onSuccess(bucket);
            }
        });
    }
}
