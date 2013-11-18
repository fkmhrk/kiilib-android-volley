package jp.fkmsoft.libs.kiilib.volley;

import java.util.ArrayList;
import java.util.List;

import jp.fkmsoft.libs.kiilib.BucketOwnable;
import jp.fkmsoft.libs.kiilib.KiiCallback;
import jp.fkmsoft.libs.kiilib.KiiTopic;
import jp.fkmsoft.libs.kiilib.KiiTopicAPI;
import jp.fkmsoft.libs.kiilib.KiiTopicMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.Listener;

class KiiVolleyTopicAPI implements KiiTopicAPI {

    private final KiiVolleyAPI api;
    
    KiiVolleyTopicAPI(KiiVolleyAPI api) {
        this.api = api;
    }
    
    @Override
    public void create(final BucketOwnable owner, final String name, final TopicCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + owner.getResourcePath() + "/topics/" + name;
        
        KiiRequest request = KiiRequest.createPutRequest (url, api.appId, api.appKey, api.accessToken, 
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        callback.onSuccess(null);
                    }
                },
                new KiiErrorListener<TopicCallback>(callback)
        );
        
        api.queue.add(request);
    }

    @Override
    public void subscribe(final KiiTopic topic, final TopicCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + topic.getResourcePath() + "/push/subscriptions/users";
        
        KiiRequest request = KiiRequest.createPostRequest (url, api.appId, api.appKey, api.accessToken, 
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse arg0) {
                        callback.onSuccess(topic);
                    }
                },
                new KiiErrorListener<TopicCallback>(callback)
        );
        
        api.queue.add(request);
    }
    
    @Override
    public void getList(final BucketOwnable owner, final TopicListCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + owner.getResourcePath() + "/topics";
        
        KiiRequest request = KiiRequest.createGetRequest (url, api.appId, api.appKey, api.accessToken, 
                "application/vnd.kii.TopicsRetrievalResponse+json", new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse json) {
                        try {
                            JSONArray array = json.getJSONArray("topics");
                            callback.onSuccess(toList(array));
                        } catch (JSONException e) {
                            
                        }
                        // {"topics":[{"topicID":"test"}]}
                    }
                    private List<KiiTopic> toList(JSONArray array) throws JSONException {
                        List<KiiTopic> list = new ArrayList<KiiTopic>(array.length());
                        for (int i = 0 ; i < array.length() ; ++i) {
                            JSONObject obj = array.getJSONObject(i);
                            list.add(new KiiTopic(owner, obj.getString("topicID")));
                        }
                        return list;
                    }
                },
                new KiiErrorListener<TopicListCallback>(callback)
        );
        
        api.queue.add(request);
    }

    @Override
    public void sendMessage(KiiTopic topic, KiiTopicMessage message, final SendMessageCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + topic.getResourcePath() + "/push/messages";
        
        KiiRequest request = KiiRequest.createPostRequest (url, api.appId, api.appKey, api.accessToken, 
                "application/vnd.kii.SendPushMessageRequest+json", null, message.toJson(), 
                new Listener<KiiResponse>() {
                    @Override
                    public void onResponse(KiiResponse json) {
                        try {
                            String id = json.getString("pushMessageID");
                            callback.onSuccess(id);
                        } catch (JSONException e ) {
                            // 
                        }
                    }
                }, new KiiErrorListener<SendMessageCallback>(callback) {
                    
                    @Override
                    void onError(NetworkResponse response, String body, SendMessageCallback callback) {
                        super.onError(response, body, callback);
                        if (response.statusCode != 201) {
                            callback.onError(response.statusCode, body);
                            return;
                        }
                        // OK
                        try {
                            JSONObject json = new JSONObject(body);
                            String id = json.getString("pushMessageID");
                            callback.onSuccess(id);
                        } catch (JSONException e ) {
                            callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                        }
                    }
        });
        
        api.queue.add(request);
    }
}
