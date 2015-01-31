package jp.fkmsoft.libs.kiilib.volley;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

class VolleyHTTPClient implements KiiHTTPClient {

    private final RequestQueue mQueue;
    private final String mAppId;
    private final String mAppKey;


    VolleyHTTPClient(RequestQueue queue, String appId, String appKey) {
        this.mQueue = queue;
        this.mAppId = appId;
        this.mAppKey = appKey;
    }
    
    @Override
    public void sendJsonRequest(int method, String url, String token, 
            String contentType, 
            Map<String, String> headers, JSONObject body,
            final ResponseHandler handler) {
        int volleyMethod = toVolleyMethod(method);
        
        KiiRequest request = new KiiRequest(volleyMethod, url,
                mAppId, mAppKey, token, contentType, headers, body, new Listener<KiiResponse>() {
            @Override
            public void onResponse(KiiResponse result) {
                if (result != null) {
                    handler.onResponse(200, result, result.getEtag());
                } else {
                    handler.onResponse(204, null, null);
                }
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
                    handler.onResponse(networkResponse.statusCode, json, networkResponse.headers.get("ETag"));
                } catch (JSONException e) {
                    handler.onException(error);
                } catch (UnsupportedEncodingException e) {
                    handler.onException(error);
                }
            }
        });
        
        mQueue.add(request);
    }

    @Override
    public void sendPlainTextRequest(int method, String url, String token,
            Map<String, String> headers, String body, final ResponseHandler handler) {
        int volleyMethod = toVolleyMethod(method);
        
        KiiRequest request = new KiiRequest(volleyMethod, url,
                mAppId, mAppKey, token, "text/plain", headers, body, new Listener<KiiResponse>() {
            @Override
            public void onResponse(KiiResponse result) {
                if (result != null) {
                    handler.onResponse(200, result, result.getEtag());
                } else {
                    handler.onResponse(204, null, "");
                }
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
                    handler.onResponse(networkResponse.statusCode, json, networkResponse.headers.get("ETag"));
                } catch (JSONException e) {
                    handler.onException(error);
                } catch (UnsupportedEncodingException e) {
                    handler.onException(error);
                }
            }
        });
        
        mQueue.add(request);
    }

    @Override
    public void sendStreamRequest(int method, String url, String token, String contentType, 
            Map<String, String> headers, InputStream body,
            final ResponseHandler handler) {
        int volleyMethod = toVolleyMethod(method);
        
        KiiStreamRequest request = new KiiStreamRequest(volleyMethod, url,
                mAppId, mAppKey, token, contentType, headers, body, new Listener<KiiResponse>() {
            @Override
            public void onResponse(KiiResponse result) {
                handler.onResponse(200, result, result.getEtag());
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
                    handler.onResponse(networkResponse.statusCode, json, networkResponse.headers.get("ETag"));
                } catch (JSONException e) {
                    handler.onException(error);
                } catch (UnsupportedEncodingException e) {
                    handler.onException(error);
                }
            }
        });
        
        mQueue.add(request);
    }
    
    private int toVolleyMethod(int method) {
        switch (method) {
        case Method.GET: return com.android.volley.Request.Method.GET;
        case Method.POST: return com.android.volley.Request.Method.POST;
        case Method.PUT: return com.android.volley.Request.Method.PUT;
        case Method.DELETE: return com.android.volley.Request.Method.DELETE;
        default: return com.android.volley.Request.Method.GET;
        }
    }
}
