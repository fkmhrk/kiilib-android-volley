package jp.fkmsoft.libs.kiilib.volley;

import org.json.JSONException;
import org.json.JSONObject;

class KiiResponse extends JSONObject {
    private final String etag;
    
    public KiiResponse(String jsonString, String etag) throws JSONException {
        super(jsonString);
        this.etag = etag;
    }
    
    public String getEtag() {
        return etag;
    }
}
