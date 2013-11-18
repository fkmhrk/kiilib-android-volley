package jp.fkmsoft.libs.kiilib;

import android.annotation.SuppressLint;
import android.os.Parcelable;

/**
 * Describes implemented class can has ACL
 * @author fkm
 *
 */
@SuppressLint("ParcelCreator")
public interface AccessControllable extends Parcelable{
    String getResourcePath();
}
