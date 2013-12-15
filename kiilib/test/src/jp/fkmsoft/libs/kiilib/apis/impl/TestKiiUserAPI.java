package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;
import android.test.AndroidTestCase;

public class TestKiiUserAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    public void test_0000_findUserByUsername() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        UserAPI userAPI = api.userAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("loginName", "name3456");
        respBody.put("userID", "user1234");
        api.client.addResponse(200, respBody, "");
        
        String username = "name3456";
        userAPI.findUserByUsername(username, new UserAPI.UserCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiUser user) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/LOGIN_NAME:name3456", args.url);
                assertNull(args.body);
                
                // output
                assertEquals("user1234", user.getId());
                assertEquals("name3456", user.getUserName());
            }
        });
    }
    
    public void test_0100_findUserByEmail() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        UserAPI userAPI = api.userAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("loginName", "name3456");
        respBody.put("userID", "user1234");
        api.client.addResponse(200, respBody, "");
        
        String email = "test@fkmsoft.jp";
        userAPI.findUserByEmail(email, new UserAPI.UserCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiUser user) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/EMAIL:test@fkmsoft.jp", args.url);
                assertNull(args.body);
                
                // output
                assertEquals("user1234", user.getId());
                assertEquals("name3456", user.getUserName());
            }
        });
    }
    
    public void test_0200_findUserByPhone() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        UserAPI userAPI = api.userAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("loginName", "name3456");
        respBody.put("userID", "user1234");
        api.client.addResponse(200, respBody, "");
        
        String phone = "+818011112222";
        userAPI.findUserByPhone(phone, new UserAPI.UserCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiUser user) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/PHONE:+818011112222", args.url);
                assertNull(args.body);
                
                // output
                assertEquals("user1234", user.getId());
                assertEquals("name3456", user.getUserName());
            }
        });
    }
    
    public void test_0300_updateEmail() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        UserAPI userAPI = api.userAPI();
        
        // set mock
        api.client.addResponse(204, null, "");

        KiiUser user = new KiiUser("user1234");
        String newEmail = "new@fkmsoft.jp";
        userAPI.updateEmail(user, newEmail, false, new UserAPI.UserCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiUser user) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/user1234/email-address", args.url);
                assertEquals(1, args.body.length());
                assertEquals("new@fkmsoft.jp", args.body.optString("emailAddress"));
                
                // output
                assertEquals("user1234", user.getId());
                assertEquals("new@fkmsoft.jp", user.getEmail());
            }
        });
    }
    
    public void test_0400_updatePhone() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        UserAPI userAPI = api.userAPI();
        
        // set mock
        api.client.addResponse(204, null, "");

        KiiUser user = new KiiUser("user1234");
        String newPhone = "+819044445555";
        userAPI.updatePhone(user, newPhone, false, new UserAPI.UserCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiUser user) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/user1234/phone-number", args.url);
                assertEquals(1, args.body.length());
                assertEquals("+819044445555", args.body.optString("phoneNumber"));
                
                // output
                assertEquals("user1234", user.getId());
                assertEquals("+819044445555", user.getPhone());
            }
        });
    }    
}
