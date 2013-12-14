package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describes query condition
 * @author fkm
 *
 */
public class QueryParams {
    
    private static final String FIELD_BUCKET_QUERY = "bucketQuery";
    
    private static final String FIELD_CLAUSE = "clause";
    private static final String FIELD_ORDER_BY = "orderBy";
    private static final String FIELD_DESCENDING = "descending";
    
    private static final String FIELD_PAGINATION_KEY = "paginationKey";
    
    private JSONObject json = new JSONObject();
    private JSONObject queryJson = new JSONObject();
    
    public QueryParams(KiiClause clause) {
        try {
            queryJson.put(FIELD_CLAUSE, clause.toJson());
        } catch (JSONException e) {
            return;
        }
    }
    
    public void sortByAsc(String field) {
        setSortBy(field, false);
    }
    
    public void sortByDesc(String field) {
        setSortBy(field, true);
    }
    
    private void setSortBy(String field, boolean descending) {
        try {
            queryJson.put(FIELD_ORDER_BY, field);
            queryJson.put(FIELD_DESCENDING, descending);
        } catch (JSONException e) {
            // nop
        }
    }

    public void setPaginationKey(String key) {
        try {
            json.put(FIELD_PAGINATION_KEY, key);
        } catch (JSONException e) {
            // nop
        }
    }
    
    public JSONObject toJson() {
        try {
            json.put(FIELD_BUCKET_QUERY, queryJson);
        } catch (JSONException e) { /* */ }
        
        return json;
    }
}
