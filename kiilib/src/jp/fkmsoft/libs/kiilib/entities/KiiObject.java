package jp.fkmsoft.libs.kiilib.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class KiiObject extends JSONObject implements AccessControllable, Parcelable {
    private static final String FIELD_ID = "_id";
    private static final String FIELD_VERSION = "_version";
    private static final String FIELD_MODIFIED = "_modified";
    private static final String FIELD_CREATED = "_created";
    
    private static final String FIELD_OBJECT_ID = "objectID";
    
    
    private final KiiBucket bucket;
    private String id;
    private String version;
    
    public KiiObject(KiiBucket bucket) {
        this.bucket = bucket;
        id = "";
        version = "";
    }
    
    public KiiObject(KiiBucket bucket, String id) {
        this.bucket = bucket;
        this.id = id;
        version = "";
    }
    
    public KiiObject(KiiBucket bucket, JSONObject from) throws JSONException {
        this.bucket = bucket;
        replace(from);
        id = from.optString(FIELD_ID, null);
        if (id == null) {
            id = from.optString(FIELD_OBJECT_ID, "");
        }
        version = from.optString(FIELD_VERSION, "");
    }
    
    public void replace(JSONObject source) throws JSONException {
        clear();
        @SuppressWarnings("unchecked")
        Iterator<String> it = source.keys();
        while (it.hasNext()) {
            String name = it.next();
            put(name, source.get(name));
        }
    }
    
    public void clear() {
        List<String> keyList = new ArrayList<String>(this.length());
        @SuppressWarnings("unchecked")
        Iterator<String> it = this.keys();
        while (it.hasNext()) {
            keyList.add(it.next());
        }
        for (String key : keyList) {
            remove(key);
        }
    }
    
    public KiiBucket getBucket() {
        return bucket;
    }

    public String getId() {
        return id;
    }
    
    public String getResourcePath() {
        return bucket.getResourcePath() + "/objects/" + id;
    }
    
    public void setVersion(String etag) {
        version = etag;
    }

    public void setModifiedTime(long value) {
        try {
            put(FIELD_MODIFIED, value);
        } catch (JSONException e) {/* */ }
    }

    public void setCreatedTime(long value) {
        try {
            put(FIELD_CREATED, value);
        } catch (JSONException e) {/* */ }
    }
    
    public long getCreatedTime() {
        return optLong(FIELD_CREATED, 0);
    }
    
    public long getModifiedTime() {
        return optLong(FIELD_MODIFIED, 0);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        bucket.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(version);
        dest.writeString(this.toString());
    }
    
    public static final Parcelable.Creator<KiiObject> CREATOR = new Parcelable.Creator<KiiObject>() {

        @Override
        public KiiObject createFromParcel(Parcel source) {
            KiiBucket bucket = KiiBucket.CREATOR.createFromParcel(source);
            String id = source.readString();
            String version = source.readString();
            String jsonString = source.readString();
            
            KiiObject obj;
            try {
                JSONObject from = new JSONObject(jsonString);
                obj = new KiiObject(bucket, from);
            } catch (JSONException e) {
                obj = new KiiObject(bucket);
            }
            obj.id = id;
            obj.version = version;
            return obj;
        }

        @Override
        public KiiObject[] newArray(int size) {
            return new KiiObject[size];
        }
    };
}
