package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.apis.TopicAPI;
import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicMessage;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class KiiTopicAPI implements TopicAPI {

    private final KiiAppAPI api;
    
    KiiTopicAPI(KiiAppAPI api) {
        this.api = api;
    }
    
    @Override
    public void create(final BucketOwnable owner, final String name, final TopicCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + owner.getResourcePath() + "/topics/" + name;
        
        api.getHttpClient().sendJsonRequest(Method.PUT, url, api.accessToken, null, null, null, new KiiResponseHandler<TopicCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicCallback callback) {
                callback.onSuccess(new KiiTopic(owner, name));
            }
        });
    }

    @Override
    public void subscribe(final KiiTopic topic, final TopicCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + topic.getResourcePath() + "/push/subscriptions/users";
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken, null, null, null, new KiiResponseHandler<TopicCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicCallback callback) {
                callback.onSuccess(topic);
            }
        });
    }
    
    @Override
    public void getList(final BucketOwnable owner, final TopicListCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + owner.getResourcePath() + "/topics";
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/vnd.kii.TopicsRetrievalResponse+json");
        
        api.getHttpClient().sendJsonRequest(Method.GET, url, api.accessToken, null, headers, null, new KiiResponseHandler<TopicListCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, TopicListCallback callback) {
                try {
                    JSONArray array = response.getJSONArray("topics");
                    callback.onSuccess(toList(array));
                } catch (JSONException e) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
            
            private List<KiiTopic> toList(JSONArray array) throws JSONException {
                List<KiiTopic> list = new ArrayList<KiiTopic>(array.length());
                for (int i = 0 ; i < array.length() ; ++i) {
                    JSONObject obj = array.getJSONObject(i);
                    list.add(new KiiTopic(owner, obj.getString("topicID")));
                }
                return list;
            }
        });
        
    }

    @Override
    public void sendMessage(KiiTopic topic, KiiTopicMessage message, final SendMessageCallback callback) {
        String url = api.baseUrl + "/apps/" + api.appId + topic.getResourcePath() + "/push/messages";
        
        api.getHttpClient().sendJsonRequest(Method.POST, url, api.accessToken,
                "application/vnd.kii.SendPushMessageRequest+json", null, message.toJson(), new KiiResponseHandler<SendMessageCallback>(callback) {
            @Override
            protected void onSuccess(JSONObject response, String etag, SendMessageCallback callback) {
                try {
                    String id = response.getString("pushMessageID");
                    callback.onSuccess(id);
                } catch (JSONException e ) {
                    callback.onError(KiiCallback.STATUS_JSON_EXCEPTION, e.getMessage());
                }
            }
        });
    }
}
