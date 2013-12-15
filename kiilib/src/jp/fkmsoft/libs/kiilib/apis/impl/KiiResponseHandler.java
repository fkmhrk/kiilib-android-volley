package jp.fkmsoft.libs.kiilib.apis.impl;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.apis.KiiCallback;
import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient.ResponseHandler;

abstract class KiiResponseHandler<T extends KiiCallback> implements ResponseHandler {

    private T callback;
    
    KiiResponseHandler(T callback) {
        this.callback = callback;
    }
    
    @Override
    public void onResponse(int status, JSONObject response, String etag) {
        if (status < 300) {
            onSuccess(response, etag, callback);
        } else {
            callback.onError(status, response.toString());
        }
    }

    protected void onSuccess(JSONObject response, String etag, T callback)
    {
        // nop
    }
 
    
    @Override
    public void onException(Exception e) {
        callback.onError(KiiCallback.STATUS_GENERAL_EXCEPTION, e.getMessage());
    }

}
