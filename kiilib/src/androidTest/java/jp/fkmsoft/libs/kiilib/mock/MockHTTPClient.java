package jp.fkmsoft.libs.kiilib.mock;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.json.JSONObject;

import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;

public class MockHTTPClient implements KiiHTTPClient {

    public Queue<Args> argsQueue = new LinkedList<>();
    private Queue<Response> responseQueue = new LinkedList<>();
    
    @Override
    public void sendJsonRequest(int method, String url, String token,
            String contentType, Map<String, String> headers, JSONObject body,
            ResponseHandler handler) {
        argsQueue.add(new Args(method, url, token, contentType, headers, body));
        
        Response response = responseQueue.poll();
        if (response.exception == null) {
            handler.onResponse(response.status, response.body, response.etag);
        } else {
            handler.onException(response.exception);
        }
    }
    
    @Override
    public void sendPlainTextRequest(int method, String url, String token,
            Map<String, String> headers, String body, ResponseHandler handler) {
        argsQueue.add(new Args(method, url, token, headers, body));
        
        Response response = responseQueue.poll();
        if (response.exception == null) {
            handler.onResponse(response.status, response.body, response.etag);
        } else {
            handler.onException(response.exception);
        }
    }
    
    @Override
    public void sendStreamRequest(int method, String url, String token, String contentType, 
            Map<String, String> headers, InputStream body,
            ResponseHandler handler) {
    }
    
    public void addResponse(int status, JSONObject body, String etag) {
        responseQueue.add(new Response(status, body, etag));
    }
    
    public void addResponse(Exception e) {
        responseQueue.add(new Response(e));
    }
    
    public static class Args {
        public int method;
        public String url;
        public String token;
        public String contentType;
        public Map<String, String> headers;
        public JSONObject body;
        public String plainBody;
        
        Args(int method, String url, String token, String contentType,
                Map<String, String> headers, JSONObject body) {
            this.method = method;
            this.url = url;
            this.token = token;
            this.contentType = contentType;
            this.headers = headers;
            this.body = body;
        }
        
        Args(int method, String url, String token, Map<String, String> headers, String body) {
            this.method = method;
            this.url = url;
            this.token = token;
            this.headers = headers;
            this.plainBody = body;
        }
    }
    
    private static class Response {
        public int status;
        public JSONObject body;
        public String etag;
        
        public Exception exception;
        Response(int status, JSONObject body, String etag) {
            this.status = status;
            this.body = body;
            this.etag = etag;
            this.exception = null;
        }
        
        Response(Exception e) {
            this.exception = e;
        }
        
    }

}
