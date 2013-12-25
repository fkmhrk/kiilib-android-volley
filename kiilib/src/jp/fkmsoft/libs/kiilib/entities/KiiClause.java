package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class KiiClause implements Parcelable {

    public static KiiClause all() {
        return new KiiClause(TYPE_ALL);
    }
    
    public static KiiClause equals(String field, String value) {
        KiiClause clause = new KiiClause(TYPE_EQUAL);
        try {
            clause.json.put(FIELD_FIELD, field);
            clause.json.put(FIELD_VALUE, value);
        } catch (JSONException e) {
            // nop
        }
        
        return clause;
    }
    
    public static KiiClause and(KiiClause... clauses) {
        KiiClause clause = new KiiClause(TYPE_AND);
        
        try {
            clause.json.put(FIELD_CLAUSES, toClauseArray(clauses));
        } catch (JSONException e) {
            // nop
        }
        
        return clause;
    }
    
    public static KiiClause or(KiiClause... clauses) {
        KiiClause clause = new KiiClause(TYPE_OR);
        
        try {
            clause.json.put(FIELD_CLAUSES, toClauseArray(clauses));
        } catch (JSONException e) {
            // nop
        }
        
        return clause;
    }    
    
    private static JSONArray toClauseArray(KiiClause[] clauses) {
        JSONArray array = new JSONArray();
        for (KiiClause item : clauses) {
            array.put(item.toJson());
        }
        return array;
    }
    
    public JSONObject toJson() {
        return json;
    }
    
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_FIELD = "field";
    private static final String FIELD_VALUE = "value";
    private static final String FIELD_CLAUSES = "clauses";
    
    private static final String TYPE_ALL = "all";
    private static final String TYPE_EQUAL = "eq";
    private static final String TYPE_AND = "and";
    private static final String TYPE_OR = "or";

    
    
    private JSONObject json = new JSONObject();
    
    private KiiClause(String type) {
        try {
            json.put(FIELD_TYPE, type);
        } catch (JSONException e) {
            /* nop */
        }
    }
    
    private KiiClause(Parcel in) {
        String jsonStr = in.readString();
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException e) {
            json = new JSONObject();
        }
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(json.toString());
    }
    
    public static final Parcelable.Creator<KiiClause> CREATOR = new Parcelable.Creator<KiiClause>() {

        @Override
        public KiiClause createFromParcel(Parcel source) {
            return new KiiClause(source);
        }

        @Override
        public KiiClause[] newArray(int size) {
            return new KiiClause[size];
        }
    };
}
