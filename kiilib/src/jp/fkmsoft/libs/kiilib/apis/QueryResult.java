package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.KiiObject;

public interface QueryResult extends List<KiiObject> {
    String getPaginationKey();
    boolean hasNext();
}
