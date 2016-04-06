package in.pathri.codenvydownload;

import com.loopj.android.http.*;


public class CodenvyRestClient {
    private static final String    BASE_URL = "https://codenvy.com/api";

    private static AsyncHttpClient client   = new AsyncHttpClient();
//   	 PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
//     client.setCookieStore(myCookieStore);

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postCredential(String url, LoginData credentialStorage, AsyncHttpResponseHandler responseHandler) {
      
   RequestParams credentialsParam = new RequestParams();
   credentialsParam.put("username", credentialStorage.getUsername());
   credentialsParam.put("password", credentialStorage.getPassword());      
      client.addHeader("Content-Type", "application/json");
        client.post(getAbsoluteUrl(url), credentialsParam, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
