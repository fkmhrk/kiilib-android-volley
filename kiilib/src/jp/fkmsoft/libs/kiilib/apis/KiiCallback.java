package jp.fkmsoft.libs.kiilib.apis;

/**
 * Base interface for each callback
 * @author fkm
 *
 */
public interface KiiCallback {
    public static final int STATUS_GENERAL_EXCEPTION = -1;
    public static final int STATUS_JSON_EXCEPTION = -2;
    public static final int STATUS_UNSUPPORTED_ENCODING_EXCEPTION = -3;
    
    void onError(int status, String body);
}
