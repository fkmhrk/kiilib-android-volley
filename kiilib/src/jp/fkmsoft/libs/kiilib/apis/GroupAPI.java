package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.KiiGroup;
import jp.fkmsoft.libs.kiilib.entities.KiiUser;

/**
 * Provides group API. To get this instance, Please call {@link AppAPI#groupAPI()}
 * @author fkm
 *
 */
public interface GroupAPI {
    public interface ListCallback extends KiiCallback {
        void onSuccess(List<KiiGroup> result);
    }
    void getOwnedGroup(KiiUser user, ListCallback callback);
    
    void getJoinedGroup(KiiUser user, ListCallback callback);
    
    public interface GroupCallback extends KiiCallback {
        void onSuccess(KiiGroup group);
    }
    
    void create(String groupName, KiiUser owner, List<KiiUser> memberList, GroupCallback callback);
    
    public interface MemberCallback extends KiiCallback {
        void onSuccess(List<KiiUser> memberList);
    }
    
    void getMembers(KiiGroup group, MemberCallback callback);
    
    public interface AddCallback extends KiiCallback {
        void onSuccess(KiiUser user);
    }
    
    void addMember(KiiGroup group, KiiUser user, GroupCallback callback);
    
    void changeName(KiiGroup group, String name, GroupCallback callback);
}
