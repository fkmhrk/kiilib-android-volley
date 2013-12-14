package jp.fkmsoft.libs.kiilib.entities;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describes group
 * @author fkm
 *
 */
public class KiiGroup implements BucketOwnable, Parcelable, AccessControllable, ACLSubject {
    private final String id;
    private final String name;
    private final KiiUser owner;
    private final List<KiiUser> members;
    
    public KiiGroup(String id) {
        this(id, null, null);
    }
    
    public KiiGroup(String id, String name, KiiUser owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.members = new ArrayList<KiiUser>();
    }

    public static KiiGroup fromParcel(Parcel source) {
        String id = source.readString();
        String name = source.readString();
        KiiUser owner = KiiUser.fromParcel(source);

        return new KiiGroup(id, name, owner);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public KiiUser getOwner() {
        return owner;
    }
    
    public List<KiiUser> getMembers() {
        return members;
    }
    
    @Override
    public String getResourcePath() {
        return "/groups/" + id;
    }

    @Override
    public int getType() {
        return BucketOwnable.TYPE_GROUP;
    }
    
    public static final Parcelable.Creator<KiiGroup> CREATOR = new Parcelable.Creator<KiiGroup>() {

        @Override
        public KiiGroup createFromParcel(Parcel source) {
            return KiiGroup.fromParcel(source);
        }

        @Override
        public KiiGroup[] newArray(int size) {
            return new KiiGroup[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        owner.writeToParcel(dest, flags);
    }

    @Override
    public String getSubjectType() {
        return "GroupID";
    }
}
