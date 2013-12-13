package jp.fkmsoft.libs.kiilib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describes topic
 * @author fkm
 *
 */
public class KiiTopic implements Parcelable, AccessControllable {

    private final BucketOwnable owner;
    private final String name;
    
    private static BucketOwnable APP_SCOPE = new KiiApp();
    
    public KiiTopic(BucketOwnable owner, String name) {
        if (owner == null) {
            owner = APP_SCOPE;
        }
        this.owner = owner;
        this.name = name;
    }
    
    private KiiTopic(Parcel in) {
        int ownerType = in.readInt();
        switch (ownerType) {
        case BucketOwnable.TYPE_APP:
            this.owner = APP_SCOPE;
            break;
        case BucketOwnable.TYPE_USER:
            this.owner = KiiUser.fromParcel(in);
            break;
        case BucketOwnable.TYPE_GROUP:
            this.owner = KiiGroup.fromParcel(in);
            break;
        default:
            this.owner = APP_SCOPE;
            break;
        }
        this.name = in.readString();
    }
    
    public String getResourcePath() {
        return owner.getResourcePath() + "/topics/" + name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(owner.getType());
        owner.writeToParcel(dest, flags);
        dest.writeString(name);
    }
    
    public static final Parcelable.Creator<KiiTopic> CREATOR = new Parcelable.Creator<KiiTopic>() {

        @Override
        public KiiTopic createFromParcel(Parcel source) {
            return new KiiTopic(source);
        }

        @Override
        public KiiTopic[] newArray(int size) {
            return new KiiTopic[size];
        }
    };
}
