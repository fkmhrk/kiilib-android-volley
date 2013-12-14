package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient;

public class TestAppAPI extends KiiAppAPI {

    public MockHTTPClient client = new MockHTTPClient();
    
    public TestAppAPI(String appId, String appKey, String baseUrl) {
        super(appId, appKey, baseUrl);
    }

    @Override
    public KiiHTTPClient getHttpClient() {
        return client;
    }

}
