package jp.fkmsoft.libs.kiilib.volley;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.impl.KiiAppAPI;
import jp.fkmsoft.libs.kiilib.entities.EntityFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;

import com.android.volley.RequestQueue;


/**
 * Implementation of {@link AppAPI} for volley
 */
public class KiiVolleyAPI extends KiiAppAPI<KiiUser, KiiGroup, KiiBucket, KiiObject, KiiTopic> {

    final RequestQueue mQueue;
    final VolleyHTTPClient mClient;

    public KiiVolleyAPI(RequestQueue queue, String appId, String appKey, String baseUrl) {
        super(appId, appKey, baseUrl);
        
        this.mQueue = queue;
        this.mClient = new VolleyHTTPClient(queue, appId, appKey);
    }

    @Override
    public KiiHTTPClient getHttpClient() {
        return mClient;
    }

    @Override
    protected EntityFactory<KiiUser, KiiGroup, KiiBucket, KiiObject, KiiTopic> getEntityFactory() {
        return new KiiEntityFacctory();
    }

}
