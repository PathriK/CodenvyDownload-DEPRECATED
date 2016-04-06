package in.pathri.codenvydownload;

import okhttp3.OkHttpClient;
import okhttp3.JavaNetCookieJar;
import okhttp3.ResponseBody;

import in.pathri.codenvydownload.CodenvyResponse;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class CodenvyClient {
    private static final String BASE_URL = "https://codenvy.com/api/";
    private static Retrofit retrofit;
    private static CodenvyApiService apiService;
    
    public interface CodenvyApiService {
        //Login
        @POST("auth/login")
        Call < CodenvyResponse > postWithJson(
        @Body LoginData loginData
        );
        
        //Build
        @POST("builder/{wid}/build")
        Call < CodenvyResponse > buildProj(@Path("wid") String workspaceId, @Query("project") String project);
        
        //Build Status
        @GET("builder/{wid}/status/{id}")
        Call < CodenvyResponse > buildStatus(@Path("wid") String workspaceId, @Path("id") String buildId);
        
        //Download APK
        @GET
        Call < ResponseBody > getAPK(@Url String apkUrl);
        
        //Get Workspace Details
        @GET("workspace/all")
        Call <List<CodenvyResponse>> getWorkspaceDetails();
        
        //Get Project Details
        @GET("project/{wid}")
        Call <List<CodenvyResponse>> getProjectDetails(@Path("wid") String workspaceId);
    }
    
    public static void apiInit() {
        try {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new JavaNetCookieJar(cookieManager))
            .build();
            
            retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
            
            // Service setup
            apiService = retrofit.create(CodenvyApiService.class);
            } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void postLogin(LoginData loginData, Callback < CodenvyResponse > loginResponseHandler) {
        // Prepare the HTTP request
        Call < CodenvyResponse > loginCall = apiService.postWithJson(loginData);
        
        // Asynchronously execute HTTP request
        loginCall.enqueue(loginResponseHandler);
    }
    
    public static void buildProj(String workspaceId, String project, Callback < CodenvyResponse > buildResponseHandler) {
        // Prepare the HTTP request
        Call < CodenvyResponse > buildCall = apiService.buildProj(workspaceId, project);
        
        // Asynchronously execute HTTP request
        buildCall.enqueue(buildResponseHandler);
    }
    
    public static void buildStatus(String workspaceId, String buildId, Callback < CodenvyResponse > statusResponseHandler) {
        // Prepare the HTTP request
        Call < CodenvyResponse > statusCall = apiService.buildStatus(workspaceId, buildId);
        
        // Asynchronously execute HTTP request
        statusCall.enqueue(statusResponseHandler);
    }
    
    public static void getAPK(String apkUrl, Callback < ResponseBody > apkDownloadHandler) {
        // Prepare the HTTP request
        Call < ResponseBody > getApkCall = apiService.getAPK(apkUrl);
        
        // Asynchronously execute HTTP request
        getApkCall.enqueue(apkDownloadHandler);
    }
    
    public static void getWorkspaceDetails(Callback < List<CodenvyResponse> > workspaceResponseHandler){
        Call <List<CodenvyResponse>> workspaceDetailsCall = apiService.getWorkspaceDetails();
        workspaceDetailsCall.enqueue(workspaceResponseHandler);
    }
    
    public static void getProjectDetails(String wid,Callback < List<CodenvyResponse> > projectResponseHandler){
        Call <List<CodenvyResponse>> projectDetailsCall = apiService.getProjectDetails(wid);
        projectDetailsCall.enqueue(projectResponseHandler);
    }
}