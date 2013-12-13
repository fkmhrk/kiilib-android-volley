package jp.fkmsoft.libs.kiilib.entities;

import android.os.Parcel;
import android.os.Parcelable;

class KiiApp implements BucketOwnable {

    KiiApp() {
    }
    
    KiiApp(Parcel in) {
        
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // nop
    }

    public static final Parcelable.Creator<KiiApp> CREATOR = new Parcelable.Creator<KiiApp>() {

        @Override
        public KiiApp createFromParcel(Parcel source) {
            return new KiiApp(source);
        }

        @Override
        public KiiApp[] newArray(int size) {
            return new KiiApp[size];
        }
    };
    
    @Override
    public String getResourcePath() {
        return "";
    }

    @Override
    public int getType() {
        return BucketOwnable.TYPE_APP;
    }
}
