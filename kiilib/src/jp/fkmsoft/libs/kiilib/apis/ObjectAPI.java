package jp.fkmsoft.libs.kiilib.apis;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;

import org.json.JSONObject;


public interface ObjectAPI {
    public interface ObjectCallback extends KiiCallback {
        void onSuccess(KiiObject obj);
    };
    
    void getById(KiiBucket bucket, String id, ObjectCallback callback);
    
    void create(KiiBucket bucket, JSONObject obj, ObjectCallback callback);
    
    void update(KiiObject obj, ObjectCallback callback);
    
    void updatePatch(KiiObject obj, JSONObject patch, ObjectCallback callback);
    
    void delete(KiiObject obj, ObjectCallback callback);
}
