package jp.fkmsoft.libs.kiilib.volley;

import org.json.JSONException;
import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.entities.BucketOwnable;
import jp.fkmsoft.libs.kiilib.entities.EntityFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiGroupFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiTopic;
import jp.fkmsoft.libs.kiilib.entities.KiiTopicFactory;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;
import jp.fkmsoft.libs.kiilib.entities.KiiUserFactory;

/**
 * Enitity factory for this library
 */
class KiiEntityFacctory implements EntityFactory<KiiUser, KiiGroup, KiiBucket, KiiObject, KiiTopic> {
    private KiiUserFactory<KiiUser> mUserFactory = new KiiUserFactory<KiiUser>() {
        @Override
        public KiiUser create(String id) {
            return new KiiUser(id);
        }
    };
    private KiiGroupFactory<KiiUser, KiiGroup> mGroupFactory = new KiiGroupFactory<KiiUser, KiiGroup>() {
        @Override
        public KiiGroup create(String id, String name, KiiUser owner) {
            return new KiiGroup(id, name, owner);
        }
    };
    private KiiObjectFactory<KiiBucket, KiiObject> mObjectFactory = new KiiObjectFactory<KiiBucket, KiiObject>() {
        @Override
        public KiiObject create(KiiBucket bucket, JSONObject json) {
            try {
                return new KiiObject(bucket, json);
            } catch (JSONException e) {
                return new KiiObject(bucket);
            }
        }
    };
    private KiiTopicFactory<KiiTopic> mTopicFactory = new KiiTopicFactory<KiiTopic>() {
        @Override
        public KiiTopic create(BucketOwnable owner, String name) {
            return new KiiTopic(owner, name);
        }
    };

    @Override
    public KiiUserFactory<KiiUser> getKiiUserFactory() {
        return mUserFactory;
    }

    @Override
    public KiiGroupFactory<KiiUser, KiiGroup> getKiiGroupFactory() {
        return mGroupFactory;
    }

    @Override
    public KiiObjectFactory<KiiBucket, KiiObject> getKiiObjectFactory() {
        return mObjectFactory;
    }

    @Override
    public KiiTopicFactory<KiiTopic> getKiitopicFactory() {
        return mTopicFactory;
    }
}
