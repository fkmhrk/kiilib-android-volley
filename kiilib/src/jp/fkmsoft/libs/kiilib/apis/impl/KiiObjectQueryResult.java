package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.ArrayList;

import jp.fkmsoft.libs.kiilib.apis.QueryResult;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;

class KiiObjectQueryResult extends ArrayList<KiiObject> implements QueryResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String paginationKey;
    
    KiiObjectQueryResult(int capacity) {
        super(capacity);
    }
    
    @Override
    public String getPaginationKey() {
        return paginationKey;
    }
    
    void setPaginationKey(String value) {
        paginationKey = value;
    }

    @Override
    public boolean hasNext() {
        return (paginationKey != null);
    }

}
