package jp.fkmsoft.libs.kiilib.apis.impl;

import android.test.AndroidTestCase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import jp.fkmsoft.libs.kiilib.apis.KiiException;
import jp.fkmsoft.libs.kiilib.apis.KiiTopicMessage;
import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiGroup;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiTopic;
import jp.fkmsoft.libs.kiilib.entities.basic.BasicKiiTopicDTO;
import jp.fkmsoft.libs.kiilib.mock.MockHTTPClient.Args;

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
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        TopicAPI topicAPI = new KiiTopicAPI(context);
        
        // set mock
        context.mClient.addResponse(204, null, "");
        
        KiiGroup group = new BasicKiiGroup("group1234", null, null);
        String name = "myTopic";
        topicAPI.create(group, name, BasicKiiTopicDTO.getInstance(), new TopicAPI.TopicCallback<BasicKiiTopic>() {
            @Override
            public void onSuccess(BasicKiiTopic topic) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.PUT, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/topics/myTopic", args.url);
                assertNull(args.body);

                // output
//                assertEquals("myTopic", topic.getName());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0100_subscribe() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        TopicAPI topicAPI = new KiiTopicAPI(context);

        // set mock
        context.mClient.addResponse(204, null, "");
        
        KiiGroup group = new BasicKiiGroup("group1234", null, null);
        String name = "myTopic";
        KiiTopic topic = new BasicKiiTopic(group, name);
        topicAPI.subscribe(topic, new TopicAPI.TopicCallback<KiiTopic>() {
            @Override
            public void onSuccess(KiiTopic topic) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/topics/myTopic/push/subscriptions/users", args.url);
                assertNull(args.body);

                // output
//                assertEquals("myTopic", topic.getName());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0200_sendMessage() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        TopicAPI topicAPI = new KiiTopicAPI(context);

        // set mock
        JSONObject respBody = new JSONObject();
        respBody.put("pushMessageID", "push1234");
        context.mClient.addResponse(200, respBody, "");
        
        KiiGroup group = new BasicKiiGroup("group1234", null, null);
        String name = "myTopic";
        KiiTopic topic = new BasicKiiTopic(group, name);
        
        JSONObject data = new JSONObject();
        data.put("msg", "hey");
        KiiTopicMessage message = new KiiTopicMessage(data, "demo");
        topicAPI.sendMessage(topic, message, new TopicAPI.SendMessageCallback() {
            @Override
            public void onSuccess(String pushMessageId) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.POST, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/topics/myTopic/push/messages", args.url);
                assertEquals(12, args.body.length());

                // output
                assertEquals("push1234", pushMessageId);
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
    
    public void test_0300_getList() throws Exception {
        final TestContext context = new TestContext(appId, appKey , baseUrl);
        TopicAPI topicAPI = new KiiTopicAPI(context);

        // set mock
        JSONObject respBody = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(createTopicObject("myTopic1"));
        array.put(createTopicObject("myTopic2"));
        respBody.put("topics", array);
        context.mClient.addResponse(200, respBody, "");
        
        KiiGroup group = new BasicKiiGroup("group1234", null, null);
        
        topicAPI.getList(group, BasicKiiTopicDTO.getInstance(), new TopicAPI.TopicListCallback<BasicKiiTopic>() {
            @Override
            public void onSuccess(List<BasicKiiTopic> list) {
                // args
                Args args = context.mClient.argsQueue.poll();
                assertEquals(KiiHTTPClient.Method.GET, args.method);
                assertEquals("https://api.kii.com/api/apps/appId/groups/group1234/topics", args.url);
                assertNull(args.body);

                // output
                assertEquals(2, list.size());
                KiiTopic topic = list.get(0);
//                assertEquals("myTopic1", topic.getName());
                topic = list.get(1);
//                assertEquals("myTopic2", topic.getName());
            }

            @Override
            public void onError(KiiException e) {
                fail("error status=" + e.getStatus() + " body=" + e.getBody());
            }
        });
    }
}
