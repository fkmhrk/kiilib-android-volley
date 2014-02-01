package jp.fkmsoft.libs.kiilib.apis;

import java.io.InputStream;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;

import org.json.JSONObject;


public interface ObjectAPI {
    public interface ObjectCallback extends KiiCallback {
        void onSuccess(KiiObject obj);
    };
    
    public interface PublishCallback extends KiiCallback {
        void onSuccess(String url);
    };
    
    void getById(KiiBucket bucket, String id, ObjectCallback callback);
    
    void create(KiiBucket bucket, JSONObject obj, ObjectCallback callback);
    
    void update(KiiObject obj, ObjectCallback callback);
    
    void updatePatch(KiiObject obj, JSONObject patch, ObjectCallback callback);
    
    void updatePatchIfUnmodified(KiiObject obj, JSONObject patch, ObjectCallback callback);
    
    void updateBody(KiiObject obj, InputStream source, String contentType, ObjectCallback callback);
    
    void publish(KiiObject obj, PublishCallback callback);
    
    void delete(KiiObject obj, ObjectCallback callback);
}
