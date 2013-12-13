package jp.fkmsoft.libs.kiilib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describes bucket
 * @author fkm
 *
 */
public class KiiBucket implements Parcelable, AccessControllable {

    private final BucketOwnable owner;
    private final String name;
    
    private static BucketOwnable APP_SCOPE = new KiiApp();
    
    public KiiBucket(BucketOwnable owner, String name) {
        if (owner == null) {
            owner = APP_SCOPE;
        }
        this.owner = owner;
        this.name = name;
    }
    
    private KiiBucket(Parcel in) {
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
        return owner.getResourcePath() + "/buckets/" + name;
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
    
    public static final Parcelable.Creator<KiiBucket> CREATOR = new Parcelable.Creator<KiiBucket>() {

        @Override
        public KiiBucket createFromParcel(Parcel source) {
            return new KiiBucket(source);
        }

        @Override
        public KiiBucket[] newArray(int size) {
            return new KiiBucket[size];
        }
    };
}
