package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.List;

import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicMessage;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;

import org.json.JSONArray;
import org.json.JSONObject;

import android.test.AndroidTestCase;

public class TestKiiTopicAPI extends AndroidTestCase {
    private String appId = "appId";
    private String appKey = "appKey";
    private String baseUrl = "https://api.kii.com/api";
    
    private JSONObject createTopicObject(String topicId) throws Exception {
        JSONObject json = new JSONObject();
        json.put("topicID", topicId);
        return json;
    }
    
    public void test_0000_create() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        TopicAPI topicAPI = api.topicAPI();
        
        // set mock
        api.client.addResponse(204, null, "");
        
        KiiGroup group = new KiiGroup("group1234");
        String name = "myTopic";
        topicAPI.create(group, name, new TopicAPI.TopicCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiTopic topic) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/topics/myTopic", args.url);
                assertNull(args.body);
                
                // output
                assertEquals("myTopic", topic.getName());
            }
        });
    }
    
    public void test_0100_subscribe() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        TopicAPI topicAPI = api.topicAPI();
        
        // set mock
        api.client.addResponse(204, null, "");
        
        KiiGroup group = new KiiGroup("group1234");
        String name = "myTopic";
        KiiTopic topic = new KiiTopic(group, name);
        topicAPI.subscribe(topic, new TopicAPI.TopicCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(KiiTopic topic) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/topics/myTopic/push/subscriptions/users", args.url);
                assertNull(args.body);
                
                // output
                assertEquals("myTopic", topic.getName());
            }
        });
    }
    
    public void test_0200_sendMessage() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        TopicAPI topicAPI = api.topicAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("pushMessageID", "push1234");
        api.client.addResponse(200, respBody, "");
        
        KiiGroup group = new KiiGroup("group1234");
        String name = "myTopic";
        KiiTopic topic = new KiiTopic(group, name);
        
        JSONObject data = new JSONObject();
        data.put("msg", "hey");
        KiiTopicMessage message = new KiiTopicMessage(data, "demo");
        topicAPI.sendMessage(topic, message, new TopicAPI.SendMessageCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(String pushMessageId) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/topics/myTopic/push/messages", args.url);
                assertEquals(12, args.body.length());
                
                // output
                assertEquals("push1234", pushMessageId);
            }
        });
    }
    
    public void test_0300_getList() throws Exception {
        final TestAppAPI api = new TestAppAPI(appId, appKey , baseUrl);
        TopicAPI topicAPI = api.topicAPI();
        
        // set mock
        JSONObject respBody = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(createTopicObject("myTopic1"));
        array.put(createTopicObject("myTopic2"));
        respBody.put("topics", array);
        api.client.addResponse(200, respBody, "");
        
        KiiGroup group = new KiiGroup("group1234");
        
        topicAPI.getList(group, new TopicAPI.TopicListCallback() {
            
            @Override
            public void onError(int status, String body) {
                fail("error status=" + status + " body=" + body);
            }
            
            @Override
            public void onSuccess(List<KiiTopic> list) {
                // args
                Args args = api.client.argsQueue.poll();
                assertEquals(Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/topics", args.url);
                assertNull(args.body);
                
                // output
                assertEquals(2, list.size());
                KiiTopic topic = list.get(0);
                assertEquals("myTopic1", topic.getName());
                topic = list.get(1);
                assertEquals("myTopic2", topic.getName());
                
            }
        });
    }
}
