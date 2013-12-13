package jp.fkmsoft.libs.kiilib.apis;

import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Provides User related APIs. To get this instance, Please call {@link AppAPI#userAPI()}
 * @author fkm
 *
 */
public interface UserAPI {
    public interface UserCallback extends KiiCallback {
        void onSuccess(KiiUser user);
    }
    
    void findUserByUsername(String username, UserCallback callback);
    
    void findUserByEmail(String email, UserCallback callback);
    
    void findUserByPhone(String phone, UserCallback callback);
    
    void updateEmail(KiiUser user, String newEmail, boolean verified, UserCallback callback);
    
    void updatePhone(KiiUser user, String newPhone, boolean verified, UserCallback callback);
}
