package jp.fkmsoft.libs.kiilib.volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

public class KiiRequest extends JsonRequest<KiiResponse> {

    private final Map<String, String> headers = new HashMap<String, String>();
    
    public KiiRequest(int method,
            String url,
            String appId, 
            String appKey,
            JSONObject jsonRequest,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
        setRetryPolicy(new KiiRetryPolicy());
        headers.put("x-kii-appid", appId);
        headers.put("x-kii-appkey", appKey);
    }
    
    public KiiRequest(int method,
            String url,
            String appId, 
            String appKey,
            String accessToken,
            String contentType,
            JSONObject jsonRequest,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
        setRetryPolicy(new KiiRetryPolicy());
        headers.put("x-kii-appid", appId);
        headers.put("x-kii-appkey", appKey);
        if (accessToken != null) {
            headers.put("authorization", "bearer " + accessToken);
        }
        headers.put("content-type", contentType);
    }
    public KiiRequest(int method,
            String url,
            String appId, 
            String appKey,
            String accessToken,
            String contentType,
            Map<String, String> headers,
            JSONObject jsonRequest,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        this(method, url, appId, appKey, accessToken, contentType, headers, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
    }
    
    public KiiRequest(int method,
            String url,
            String appId, 
            String appKey,
            String accessToken,
            String contentType,
            Map<String, String> headers,
            String body,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        super(method, url, body, listener, errorListener);
        setRetryPolicy(new KiiRetryPolicy());
        
        this.headers.put("x-kii-appid", appId);
        this.headers.put("x-kii-appkey", appKey);
        if (accessToken != null) {
            this.headers.put("authorization", "bearer " + accessToken);
        }
        
        if (headers != null) {
            this.headers.putAll(headers);
        }
        
        if (contentType != null) {
            this.headers.put("content-type", contentType);
        }
    }    
    
    public KiiRequest(int method,
            String url,
            String appId, 
            String appKey,
            String accessToken,
            String contentType,
            String body,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        super(method, url, body, listener, errorListener);
        setRetryPolicy(new KiiRetryPolicy());
        headers.put("x-kii-appid", appId);
        headers.put("x-kii-appkey", appKey);
        headers.put("authorization", "bearer " + accessToken);
        headers.put("content-type", contentType);
    }    
    
    public static KiiRequest createGetRequest(String url,
            String appId, 
            String appKey,
            String accessToken,
            String accept,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        KiiRequest request = new KiiRequest(Method.GET, url, appId, appKey, null, listener, errorListener);
        request.setRetryPolicy(new KiiRetryPolicy());
        if (accessToken != null) {
            request.headers.put("authorization", "bearer " + accessToken);
        }
        if (accept != null) {
            request.headers.put("accept", accept);
        }
        
        return request;
    }
    
    public static KiiRequest createPostRequest(String url,
            String appId, 
            String appKey,
            String accessToken,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        KiiRequest request = new KiiRequest(Method.POST, url, appId, appKey, null, listener, errorListener);
        request.headers.put("authorization", "bearer " + accessToken);
        return request;
    }
    
    public static KiiRequest createPostRequest(String url,
            String appId, 
            String appKey,
            String accessToken,
            String contentType,
            Map<String, String> extraHeaders,
            JSONObject body,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        KiiRequest request = new KiiRequest(Method.POST, url, appId, appKey, accessToken, contentType, body, listener, errorListener);
        if (extraHeaders != null) {
            for (Entry<String, String> entry : extraHeaders.entrySet()) {
                request.headers.put(entry.getKey(), entry.getValue());
            }
        }
        return request;
    }    
    
    public static KiiRequest createPutRequest(String url,
            String appId, 
            String appKey,
            String accessToken,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        KiiRequest request = new KiiRequest(Method.PUT, url, appId, appKey, null, listener, errorListener);
        request.headers.put("authorization", "bearer " + accessToken);
        return request;
    }
    
    public static KiiRequest createPutRequest(String url,
            String appId, 
            String appKey,
            String accessToken,
            String body,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        return new KiiRequest(Method.PUT, url, appId, appKey, accessToken, "text/plain", body, listener, errorListener);
    }
    
    public static KiiRequest createDeleteRequest(String url,
            String appId, 
            String appKey,
            String accessToken,
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        return new KiiRequest(Method.DELETE, url, appId, appKey, accessToken, null, (String)null, listener, errorListener);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }
    
    @Override
    protected Response<KiiResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            String etag = response.headers.get("ETag");
            if (jsonString == null || jsonString.length() == 0) {
                return Response.success(null, null);
            }
            return Response.success(new KiiResponse(jsonString, etag), null);
//                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}
