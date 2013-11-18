package jp.fkmsoft.libs.kiilib;

import java.util.List;

public interface QueryResult extends List<KiiObject> {
    String getPaginationKey();
    boolean hasNext();
}
