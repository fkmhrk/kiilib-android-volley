package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.AppAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;
import android.test.AndroidTestCase;

public class TestKiiAppAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    private String clientId = "clientId";
    private String clientSecret = "clientSecret";
    
    private String username = "name1234";
    private String password = "password1234";
    
    public void test_0000_loginAsAdmin() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("access_token", "token1234");
        respBody.put("id", "user1234");
        api.client.addResponse(200, respBody, "");
        
        api.loginAsAdmin(clientId, clientSecret, new AppAPI.LoginCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status);
            }
            
            @Override
            public void onSuccess(String token, KiiUser user) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.POST, args.method);
                assertEquals("https://api.kii.com/api/oauth2/token", args.url);
                assertEquals(2, args.body.length());
                assertEquals("clientId", args.body.optString("client_id"));
                assertEquals("clientSecret", args.body.optString("client_secret"));
                
                // output
                assertEquals("token1234", token);
                assertEquals("user1234", user.getId());
            }
        });
    }
    
    public void test_0100_loginAsUser() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("access_token", "token1234");
        respBody.put("id", "user1234");
        api.client.addResponse(200, respBody, "");
        
        api.loginAsUser(username, password, new AppAPI.LoginCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status);
            }
            
            @Override
            public void onSuccess(String token, KiiUser user) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.POST, args.method);
                assertEquals("https://api.kii.com/api/oauth2/token", args.url);
                assertEquals(2, args.body.length());
                assertEquals(username, args.body.optString("username"));
                assertEquals(password, args.body.optString("password"));
                
                // output
                assertEquals("token1234", token);
                assertEquals("user1234", user.getId());
            }
        });
    }
    
    public void test_0200_signup() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        
        JSONObject info = new JSONObject();
        info.put("loginName", username);
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("userID", "user1234");
        api.client.addResponse(200, respBody, "");
        
        api.signup(info, password, new AppAPI.SignupCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status);
            }
            
            @Override
            public void onSuccess(KiiUser user) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users", args.url);
                assertEquals(2, args.body.length());
                assertEquals(username, args.body.optString("loginName"));
                assertEquals(password, args.body.optString("password"));
                
                // output
                assertEquals("user1234", user.getId());
            }
        });
    }
}
