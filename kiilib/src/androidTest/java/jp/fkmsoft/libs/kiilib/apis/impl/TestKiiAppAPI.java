package jp.fkmsoft.libs.kiilib.apis.impl;

import android.test.AndroidTestCase;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.SignupInfo;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUserDTO;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;

public class TestKiiAppAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    private String clientId = "clientId";
    private String clientSecret = "clientSecret";
    
    private String username = "name1234";
    private String password = "password1234";
    
    public void test_0000_loginAsAdmin() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        AppAPI api = new KiiAppAPI(context);

        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("access_token", "token1234");
        respBody.put("id", "user1234");
        context.mClient.addResponse(200, respBody, "");
        
        api.loginAsAdmin(clientId, clientSecret, BasicKiiUserDTO.getInstance(), new AppAPI.LoginCallback<BasicKiiUser>() {

            @Override
            public void onSuccess(String token, BasicKiiUser user) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.POST, args.method);
                assertEquals("https://api.kii.com/api/oauth2/token", args.url);
                assertEquals(2, args.body.length());
                assertEquals("clientId", args.body.optString("client_id"));
                assertEquals("clientSecret", args.body.optString("client_secret"));

                // output
                assertEquals("token1234", token);
                assertEquals("user1234", user.getId());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus());
            }
        });
    }
    
    public void test_0100_loginAsUser() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        AppAPI api = new KiiAppAPI(context);

        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("access_token", "token1234");
        respBody.put("id", "user1234");
        context.mClient.addResponse(200, respBody, "");
        
        api.loginAsUser(username, password, BasicKiiUserDTO.getInstance(), new AppAPI.LoginCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(String token, BasicKiiUser user) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.POST, args.method);
                assertEquals("https://api.kii.com/api/oauth2/token", args.url);
                assertEquals(2, args.body.length());
                assertEquals(username, args.body.optString("username"));
                assertEquals(password, args.body.optString("password"));

                // output
                assertEquals("token1234", token);
                assertEquals("user1234", user.getId());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus());
            }
        });
    }
    
    public void test_0200_signup() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        AppAPI api = new KiiAppAPI(context);

        SignupInfo info = SignupInfo.UserWithUsername(username);

        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("userID", "user1234");
        context.mClient.addResponse(200, respBody, "");

        api.signup(info, password, BasicKiiUserDTO.getInstance(), new AppAPI.SignupCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser user) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users", args.url);
                assertEquals(2, args.body.length());
                assertEquals(username, args.body.optString("loginName"));
                assertEquals(password, args.body.optString("password"));

                // output
                assertEquals("user1234", user.getId());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus());
            }
        });
    }
}
