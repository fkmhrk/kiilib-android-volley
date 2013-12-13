package jp.fkmsoft.libs.kiilib.entities;

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
