package jp.fkmsoft.libs.kiilib;

/**
 * Provides bucket API. To get this instance, Please call {@link AppAPI#bucketAPI()}
 * @author fkm
 *
 */
public interface BucketAPI {
    public interface QueryCallback extends KiiCallback {
        void onSuccess(QueryResult result);
    }
    void query(KiiBucket bucket, QueryCondition condition, QueryCallback callback);
    
    public interface BucketCallback extends KiiCallback {
        void onSuccess(KiiBucket bucket);
    }
    
    void delete(KiiBucket bucket, BucketCallback callback);
}
