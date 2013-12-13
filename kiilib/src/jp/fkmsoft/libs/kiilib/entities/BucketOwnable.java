package jp.fkmsoft.libs.kiilib.entities;

import android.annotation.SuppressLint;
import android.os.Parcelable;

/**
 * Describes implemented class can own buckets
 * @author fkm
 *
 */
@SuppressLint("ParcelCreator")
public interface BucketOwnable extends Parcelable {
    public static final int TYPE_APP = 0;
    public static final int TYPE_GROUP = 1;
    public static final int TYPE_USER = 2;
    
    String getResourcePath();
    
    int getType();
    
}
