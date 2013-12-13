package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.entities.ACLSubject;
import jp.fkmsoft.libs.kiilib.entities.AccessControllable;


/**
 * Provides ACL API.
 * @author fkm
 *
 */
public interface ACLAPI {
    public interface ACLGetCallback extends KiiCallback {
        void onSuccess(Map<String, List<ACLSubject>> result);
    }
    
    void get(AccessControllable object, ACLGetCallback callback);
    
    public interface ACLCallback extends KiiCallback {
        void onSuccess(AccessControllable object);
    }
    void grant(AccessControllable object, String action, ACLSubject subject, ACLCallback callback);
    void revoke(AccessControllable object, String action, ACLSubject subject, ACLCallback callback);
}
