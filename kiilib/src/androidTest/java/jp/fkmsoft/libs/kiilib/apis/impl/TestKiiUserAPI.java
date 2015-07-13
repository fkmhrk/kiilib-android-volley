package jp.fkmsoft.libs.kiilib.apis.impl;

import android.test.AndroidTestCase;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.UserAPI;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUser;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiUserDTO;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;

public class TestKiiUserAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    public void test_0000_findUserByUsername() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        UserAPI userAPI = new KiiUserAPI(context);
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("loginName", "name3456");
        respBody.put("userID", "user1234");
        context.mClient.addResponse(200, respBody, "");
        
        String username = "name3456";
        userAPI.findUserByUsername(username, BasicKiiUserDTO.getInstance(), new UserAPI.UserCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser user) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/LOGIN_NAME:name3456", args.url);
                assertNull(args.body);

                // output
                assertEquals("user1234", user.getId());
//                assertEquals("name3456", user.getUserName());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0100_findUserByEmail() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        UserAPI userAPI = new KiiUserAPI(context);

        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("loginName", "name3456");
        respBody.put("userID", "user1234");
        context.mClient.addResponse(200, respBody, "");
        
        String email = "test@fkmsoft.jp";
        userAPI.findUserByEmail(email, BasicKiiUserDTO.getInstance(), new UserAPI.UserCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser user) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/EMAIL:test@fkmsoft.jp", args.url);
                assertNull(args.body);

                // output
                assertEquals("user1234", user.getId());
//                assertEquals("name3456", user.getUserName());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0200_findUserByPhone() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        UserAPI userAPI = new KiiUserAPI(context);

        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("loginName", "name3456");
        respBody.put("userID", "user1234");
        context.mClient.addResponse(200, respBody, "");
        
        String phone = "+818011112222";
        userAPI.findUserByPhone(phone, BasicKiiUserDTO.getInstance(), new UserAPI.UserCallback<BasicKiiUser>() {
            @Override
            public void onSuccess(BasicKiiUser user) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/PHONE:+818011112222", args.url);
                assertNull(args.body);

                // output
                assertEquals("user1234", user.getId());
//                assertEquals("name3456", user.getUserName());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0300_updateEmail() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        UserAPI userAPI = new KiiUserAPI(context);

        // set mock
        context.mClient.addResponse(204, null, "");

        KiiUser user = new BasicKiiUser("user1234", null);
        String newEmail = "new@fkmsoft.jp";
        userAPI.updateEmail(user, newEmail, false, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/user1234/email-address", args.url);
                assertEquals(1, args.body.length());
                assertEquals("new@fkmsoft.jp", args.body.optString("emailAddress"));

                // output
                assertEquals("user1234", user.getId());
//                assertEquals("new@fkmsoft.jp", user.getEmail());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0400_updatePhone() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        UserAPI userAPI = new KiiUserAPI(context);

        // set mock
        context.mClient.addResponse(204, null, "");

        KiiUser user = new BasicKiiUser("user1234", null);
        String newPhone = "+819044445555";
        userAPI.updatePhone(user, newPhone, false, new UserAPI.UserCallback<KiiUser>() {
            @Override
            public void onSuccess(KiiUser user) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/users/user1234/phone-number", args.url);
                assertEquals(1, args.body.length());
                assertEquals("+819044445555", args.body.optString("phoneNumber"));

                // output
                assertEquals("user1234", user.getId());
//                assertEquals("+819044445555", user.getPhone());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
}
