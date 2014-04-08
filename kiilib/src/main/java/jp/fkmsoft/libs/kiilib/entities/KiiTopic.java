package jp.fkmsoft.libs.kiilib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describes topic
 * @author fkm
 *
 */
public class KiiTopic extends KiiBaseTopic implements Parcelable {

    private static BucketOwnable APP_SCOPE = new KiiApp();
    
    public KiiTopic(BucketOwnable owner, String name) {
        super(owner, name);
    }
    
    static KiiTopic fromParcel(Parcel in) {
        int ownerType = in.readInt();
        BucketOwnable owner;
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
        return new KiiTopic(owner, name);
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
    
    public static final Parcelable.Creator<KiiTopic> CREATOR = new Parcelable.Creator<KiiTopic>() {

        @Override
        public KiiTopic createFromParcel(Parcel source) {
            return KiiTopic.fromParcel(source);
        }

        @Override
        public KiiTopic[] newArray(int size) {
            return new KiiTopic[size];
        }
    };
}
