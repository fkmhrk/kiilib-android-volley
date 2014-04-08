package jp.fkmsoft.libs.kiilib.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;

public class KiiUser extends KiiBaseUser implements Parcelable {

    private static final String FIELD_EMAIL = "emailAddress";
    private static final String FIELD_PHONE = "phoneNumber";
    private static final String FIELD_USERNAME = "loginName";
    
    public KiiUser(String id) {
        super(id);
    }
    
    private KiiUser(String id, String jsonString) throws JSONException {
        super(id, jsonString);
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
        dest.writeString(getId());
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
