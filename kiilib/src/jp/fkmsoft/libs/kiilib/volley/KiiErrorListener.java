package jp.fkmsoft.libs.kiilib.volley;

import java.io.UnsupportedEncodingException;

import jp.fkmsoft.libs.kiilib.KiiCallback;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

class KiiErrorListener<T extends KiiCallback> implements ErrorListener {

    private T callback;
    
    KiiErrorListener(T callback) {
        this.callback = callback;
    }
    
    @Override
    public void onErrorResponse(VolleyError arg0) {
        NetworkResponse response = arg0.networkResponse;
        if (response == null) {
            callback.onError(KiiCallback.STATUS_GENERAL_EXCEPTION, arg0.getMessage());
            return;
        }
        try {
            String body = new String(response.data, "UTF-8");
            onError(response, body, callback);
        } catch (UnsupportedEncodingException e) {
            callback.onError(KiiCallback.STATUS_UNSUPPORTED_ENCODING_EXCEPTION, arg0.getMessage());
        }
    }
    
    void onError(NetworkResponse response, String body, T callback) {
        callback.onError(response.statusCode, body);
    }
}
