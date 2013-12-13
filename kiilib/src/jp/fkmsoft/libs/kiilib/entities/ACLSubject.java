package jp.fkmsoft.libs.kiilib.entities;

import android.annotation.SuppressLint;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public interface ACLSubject extends Parcelable {
    String getSubjectType();
    String getId();
}
