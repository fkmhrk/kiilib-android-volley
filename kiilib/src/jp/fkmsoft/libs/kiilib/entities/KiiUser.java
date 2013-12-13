package jp.fkmsoft.libs.kiilib.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class KiiUser extends JSONObject implements BucketOwnable, Parcelable, ACLSubject {

    private static final String FIELD_EMAIL = "emailAddress";
    private static final String FIELD_PHONE = "phoneNumber";
    private static final String FIELD_USERNAME = "loginName";
    
    private final String id;
    
    public KiiUser(String id) {
        super();
        this.id = id;
    }
    
    private KiiUser(String id, String jsonString) throws JSONException {
        super(jsonString);
        this.id = id;
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
    
    public String getId() {
        return id;
    }
    
    public String getUserName() {
        return optString(FIELD_USERNAME, "");
    }
    
    public String getEmail() {
        return optString(FIELD_EMAIL, "");
    }
    
    public String getPhone() {
        return optString(FIELD_PHONE, "");
    }
    
    @Override
    public String getResourcePath() {
        return "/users/" + id;
    }

    @Override
    public String getSubjectType() {
        return "UserID";
    }
    
    @Override
    public int getType() {
        return BucketOwnable.TYPE_USER;
    }
    
    public static final Parcelable.Creator<KiiUser> CREATOR = new Parcelable.Creator<KiiUser>() {

        @Override
        public KiiUser createFromParcel(Parcel source) {
            return KiiUser.fromParcel(source);
        }

        @Override
        public KiiUser[] newArray(int size) {
            return new KiiUser[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(this.toString());
    }

    public static KiiUser fromParcel(Parcel in) {
        String id = in.readString();
        String jsonString = in.readString();
        try {
            return new KiiUser(id, jsonString);
        } catch (JSONException e) {
            return new KiiUser(id);
        }
    }

}
