package jp.fkmsoft.libs.kiilib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describes group
 * @author fkm
 *
 */
public class KiiGroup extends KiiBaseGroup<KiiUser> implements Parcelable {

    public KiiGroup(String id) {
        this(id, null, null);
    }
    
    public KiiGroup(String id, String name, KiiUser owner) {
        super(id, name, owner);
    }

    public static KiiGroup fromParcel(Parcel source) {
        String id = source.readString();
        String name = source.readString();
        KiiUser owner = KiiUser.fromParcel(source);

        return new KiiGroup(id, name, owner);
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
        dest.writeString(getId());
        dest.writeString(getName());
        getOwner().writeToParcel(dest, flags);
    }
}
