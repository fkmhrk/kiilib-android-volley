package jp.fkmsoft.libs.kiilib.volley;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.impl.KiiAppAPI;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;

import com.android.volley.RequestQueue;


/**
 * Implementation of {@link AppAPI} for volley
 */
public class KiiVolleyAPI extends KiiAppAPI {

    final RequestQueue queue;
    final VolleyHTTPClient client;
    String appId;
    String appKey;
    
    public KiiVolleyAPI(RequestQueue queue, String appId, String appKey, String baseUrl) {
        super(appId, appKey, baseUrl);
        
        this.queue = queue;
        this.client = new VolleyHTTPClient(this);
        
        this.appId = appId;
        this.appKey = appKey;
        
    }

    @Override
    public KiiHTTPClient getHttpClient() {
        return client;
    }
    
}
