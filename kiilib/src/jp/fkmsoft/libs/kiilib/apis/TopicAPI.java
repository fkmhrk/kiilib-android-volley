package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicMessage;

/**
 * Provides topic API.
 * @author fkm
 *
 */
public interface TopicAPI {
    public interface TopicCallback extends KiiCallback {
        void onSuccess(KiiTopic topic);
    }
    void create(BucketOwnable owner, String name, TopicCallback callback);
    
    void subscribe(KiiTopic topic, TopicCallback callback);
    
    public interface SendMessageCallback extends KiiCallback {
        void onSuccess(String pushMessageId);
    }
    
    void sendMessage(KiiTopic topic, KiiTopicMessage message, SendMessageCallback callback);
    
    public interface TopicListCallback extends KiiCallback {
        void onSuccess(List<KiiTopic> list);
    }
    
    void getList(BucketOwnable owner, TopicListCallback callback);
}
