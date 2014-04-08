package jp.fkmsoft.libs.kiilib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describes bucket
 * @author fkm
 *
 */
public class KiiBucket extends KiiBaseBucket implements Parcelable {

    private static final BucketOwnable APP_SCOPE = new KiiApp();

    public KiiBucket(BucketOwnable owner, String name) {
        super(owner, name);
    }
    
    static KiiBucket fromParcel(Parcel in) {
        BucketOwnable owner;

        int ownerType = in.readInt();
        switch (ownerType) {
        case BucketOwnable.TYPE_APP:
            owner = APP_SCOPE;
            break;
        case BucketOwnable.TYPE_USER:
            owner = KiiUser.fromParcel(in);
            break;
        case BucketOwnable.TYPE_GROUP:
            owner = KiiGroup.fromParcel(in);
            break;
        default:
            owner = APP_SCOPE;
            break;
        }

        String name = in.readString();
        return new KiiBucket(owner, name);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        BucketOwnable owner = getOwner();
        if (owner instanceof KiiApp) {
            dest.writeInt(BucketOwnable.TYPE_APP);
        } else if (owner instanceof KiiGroup) {
            dest.writeInt(BucketOwnable.TYPE_GROUP);
            KiiGroup group = (KiiGroup) owner;
            group.writeToParcel(dest, flags);
        } else if (owner instanceof KiiUser) {
            dest.writeInt(BucketOwnable.TYPE_USER);
            KiiUser user = (KiiUser) owner;
            user.writeToParcel(dest, flags);
        }

        dest.writeString(getName());
    }
    
    public static final Parcelable.Creator<KiiBucket> CREATOR = new Parcelable.Creator<KiiBucket>() {

        @Override
        public KiiBucket createFromParcel(Parcel source) {
            return KiiBucket.fromParcel(source);
        }

        @Override
        public KiiBucket[] newArray(int size) {
            return new KiiBucket[size];
        }
    };
}
