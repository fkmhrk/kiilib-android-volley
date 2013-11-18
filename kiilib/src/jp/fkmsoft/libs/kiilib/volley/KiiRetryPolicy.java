package jp.fkmsoft.libs.kiilib.volley;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

public class KiiRetryPolicy implements RetryPolicy {

    @Override
    public int getCurrentRetryCount() {
        return 0;
    }

    @Override
    public int getCurrentTimeout() {
        return 0;
    }

    @Override
    public void retry(VolleyError arg0) throws VolleyError {
        throw arg0;
    }

}
