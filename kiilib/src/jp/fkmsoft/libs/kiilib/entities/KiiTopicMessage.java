package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describes topic message
 * @author fkm
 *
 */
public class KiiTopicMessage {
    private static final String FIELD_DATA = "data";
    private static final String FIELD_SEND_TO_DEVELOPMENT = "sendToDevelopment";
    private static final String FIELD_SEND_TO_PRODUCTION = "sendToProduction";
    private static final String FIELD_PUSH_MESSAGE_TYPE = "pushMessageType";
    private static final String FIELD_SEND_APP_ID = "sendAppID";
    private static final String FIELD_SEND_SENDER = "sendSender";
    private static final String FIELD_SEND_WHEN = "sendWhen";
    private static final String FIELD_SEND_ORIGIN = "sendOrigin";
    private static final String FIELD_SEND_OBJECT_SCOPE = "sendObjectScope";
    private static final String FIELD_SEND_TOPIC_ID = "sendTopicID";
    private static final String FIELD_GCM = "gcm";
    private static final String FIELD_APNS = "apns";
    
    private JSONObject data;
    private boolean sendToDevelopment = true;
    private boolean sendToProduction = true;
    private String pushMessageType;
    private boolean sendAppID = false;
    private boolean sendSender = true;
    private boolean sendWhen = false;
    private boolean sendOrigin = false;
    private boolean sendObjectScope = true;
    private boolean sendTopicID = true;
    private GCM gcm = new GCM();
    private APNs apns = new APNs();
    
    public KiiTopicMessage(JSONObject data, String type) {
        this.data = data;
        this.pushMessageType = type;
    }
    
    public KiiTopicMessage sendToDevelopment(boolean value) {
        this.sendToDevelopment = value;
        return this;
    }

    public KiiTopicMessage sendToProduction(boolean value) {
        this.sendToProduction = value;
        return this;
    }

    public KiiTopicMessage sendAppID(boolean value) {
        this.sendAppID = value;
        return this;
    }

    public KiiTopicMessage sendSender(boolean value) {
        this.sendSender = value;
        return this;
    }

    public KiiTopicMessage sendWhen(boolean value) {
        this.sendWhen = value;
        return this;
    }

    public KiiTopicMessage sendOrigin(boolean value) {
        this.sendOrigin = value;
        return this;
    }

    public KiiTopicMessage sendObjectScope(boolean value) {
        this.sendObjectScope = value;
        return this;
    }

    public KiiTopicMessage sendTopicID(boolean value) {
        this.sendTopicID = value;
        return this;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(FIELD_DATA, data);
            json.put(FIELD_SEND_TO_DEVELOPMENT, sendToDevelopment);
            json.put(FIELD_SEND_TO_PRODUCTION, sendToProduction);
            json.put(FIELD_PUSH_MESSAGE_TYPE, pushMessageType);
            json.put(FIELD_SEND_APP_ID, sendAppID);
            json.put(FIELD_SEND_SENDER, sendSender);
            json.put(FIELD_SEND_WHEN, sendWhen);
            json.put(FIELD_SEND_ORIGIN, sendOrigin);
            json.put(FIELD_SEND_OBJECT_SCOPE, sendObjectScope);
            json.put(FIELD_SEND_TOPIC_ID, sendTopicID);
            json.put(FIELD_GCM, gcm.toJson());
            json.put(FIELD_APNS, apns.toJson());
        } catch (JSONException e) {
            // nop
        }
        return json;
    }
    
    public static class GCM {
        private static final String FIELD_ENABLED = "enabled";
        private boolean enabled = true;
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            try {
                json.put(FIELD_ENABLED, enabled);
            } catch (JSONException e) {
                // nop
            }
            return json;
        }
    }
    
    public static class APNs {
        private static final String FIELD_ENABLED = "enabled";
        private boolean enabled = true;
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            try {
                json.put(FIELD_ENABLED, enabled);
            } catch (JSONException e) {
                // nop
            }
            return json;
        }
    }
    
}
