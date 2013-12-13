package jp.fkmsoft.libs.kiilib.volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

class VolleyHTTPClient implements KiiHTTPClient {

    private final KiiVolleyAPI api;
    
    VolleyHTTPClient(KiiVolleyAPI api) {
        this.api = api;
    }
    
    @Override
    public void sendJsonRequest(int method, String url,
            Map<String, String> headers, JSONObject body,
            final ResponseHandler handler) {
        KiiRequest request = new KiiRequest(Method.POST, url,
                api.appId, api.appKey, body, new Listener<KiiResponse>() {
            @Override
            public void onResponse(KiiResponse result) {
                handler.onResponse(200, result);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse == null) {
                    handler.onException(error);
                    return;
                }
                // parse response
                try {
                    String body = new String(networkResponse.data, "UTF-8");
                    JSONObject json = new JSONObject(body);
                    handler.onResponse(networkResponse.statusCode, json);
                } catch (JSONException e) {
                    handler.onException(error);
                } catch (UnsupportedEncodingException e) {
                    handler.onException(error);
                }
            }
        });
        
        api.queue.add(request);
    }

}
