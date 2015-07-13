package jp.fkmsoft.libs.kiilib.apis.impl;

import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient;

/**
 * Test context
 */
public class TestContext implements KiiContext {
    private final String mAppId;
    private final String mAppKey;
    private final String mBaseUrl;
    private String mToken;

    MockHTTPClient mClient;

    public TestContext(String appId, String appKey, String baseUrl) {
        mAppId = appId;
        mAppKey = appKey;
        mBaseUrl = baseUrl;

        mClient = new MockHTTPClient();
    }

    @Override
    public String getAppId() {
        return mAppId;
    }

    @Override
    public String getAppKey() {
        return mAppKey;
    }

    @Override
    public String getBaseUrl() {
        return mBaseUrl;
    }

    @Override
    public String getAccessToken() {
        return mToken;
    }

    @Override
    public void setAccessToken(String token) {
        mToken = token;
    }

    @Override
    public KiiHTTPClient getHttpClient() {
        return mClient;
    }
}
