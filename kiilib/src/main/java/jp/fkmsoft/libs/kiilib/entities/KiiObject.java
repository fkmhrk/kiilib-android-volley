package jp.fkmsoft.libs.kiilib.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class KiiObject extends KiiBaseObject<KiiBucket> implements Parcelable {

    public KiiObject(KiiBucket bucket) {
        super(bucket);
    }
    
    public KiiObject(KiiBucket bucket, String id) {
        super(bucket, id);
    }
    
    public KiiObject(KiiBucket bucket, JSONObject from) throws JSONException {
        super(bucket, from);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        KiiBucket bucket = getBucket();
        bucket.writeToParcel(dest, flags);
        dest.writeString(getId());
        dest.writeString(getVersion());
        dest.writeString(this.toString());
    }
    
    public static final Parcelable.Creator<KiiObject> CREATOR = new Parcelable.Creator<KiiObject>() {

        @Override
        public KiiObject createFromParcel(Parcel source) {
            KiiBucket bucket = KiiBucket.fromParcel(source);
            String id = source.readString();
            String version = source.readString();
            String jsonString = source.readString();
            
            KiiObject obj;
            try {
                JSONObject from = new JSONObject(jsonString);
                from.put("_id", id);
                obj = new KiiObject(bucket, from);
            } catch (JSONException e) {
                obj = new KiiObject(bucket, id);
            }
            obj.setVersion(version);
            return obj;
        }

        @Override
        public KiiObject[] newArray(int size) {
            return new KiiObject[size];
        }
    };
}
