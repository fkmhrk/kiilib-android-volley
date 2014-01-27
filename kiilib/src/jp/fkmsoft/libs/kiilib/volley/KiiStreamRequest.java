package jp.fkmsoft.libs.kiilib.volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class KiiStreamRequest extends Request<KiiResponse> {

    private final Map<String, String> headers = new HashMap<String, String>();
    
    private final Listener<KiiResponse> mListener;
    private InputStream mStream;
    
    public KiiStreamRequest(int method,
            String url,
            String appId, 
            String appKey,
            String accessToken,
            String contentType,
            Map<String, String> headers,
            InputStream bodyStream, 
            Listener<KiiResponse> listener,
            ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mStream = bodyStream;
        
        setRetryPolicy(new KiiRetryPolicy());
        this.headers.put("x-kii-appid", appId);
        this.headers.put("x-kii-appkey", appKey);
        if (accessToken != null) {
            this.headers.put("authorization", "bearer " + accessToken);
        }
        this.headers.put("content-type", contentType);
        if (headers != null) {
            this.headers.putAll(headers);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }
    
    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int count;
        try {
            while ((count = mStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, count);
            }
        } catch (IOException e) {
            // nop
        } finally {
            try {
                out.close();
                mStream.close();
            } catch (IOException e) {
                // nop
            }
        }
        return out.toByteArray();
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

    
    @Override
    protected void deliverResponse(KiiResponse response) {
        mListener.onResponse(response);
    }
}
