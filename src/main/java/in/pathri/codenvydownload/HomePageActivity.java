package in.pathri.codenvydownload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.*;

import okhttp3.ResponseBody;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import in.pathri.codenvydownload.preferancehandlers.*;
import in.pathri.codenvydownload.responsehandlers.*;

public class HomePageActivity extends Activity {
    public static final Integer SUCCESS_CODE = 200;
    static TextView loginStatus, buildStatus, statusMsg, downloadStatus, triggerStatus,currentProject;
    public static ProgressBar loginSpinner,buildSpinner,triggerSpinner,downloadSpinner;
    public static Handler statusHandler;
    public static Context context;
    private static String username,password,wid,project,wname;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        loginStatus = (TextView) findViewById(R.id.login_status);
        buildStatus = (TextView) findViewById(R.id.build_status);
        triggerStatus = (TextView) findViewById(R.id.trigger_status);
        downloadStatus = (TextView) findViewById(R.id.download_status);
        statusMsg = (TextView) findViewById(R.id.status_msg);
        currentProject = (TextView) findViewById(R.id.current_project);
        
        loginSpinner = (ProgressBar) findViewById(R.id.login_progress);
        buildSpinner = (ProgressBar) findViewById(R.id.build_progress);
        triggerSpinner = (ProgressBar) findViewById(R.id.trigger_progress);
        downloadSpinner = (ProgressBar) findViewById(R.id.download_progress);
        
        CodenvyClient.apiInit();
        statusHandler = new Handler();
        context = getApplicationContext();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setup:
            Intent i = new Intent(this, SetupActivity.class);
            startActivity(i);
            return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public static void clearStatusTexts(){
        updateLoginStatusText("");
        updateTriggerStatusText("");
        updateBuildStatusText("");
        updateDownloadStatusText("");
    }
    
    public static void updateLoginStatusText(String text) {
        loginStatus.setText(text);
    }
    
    public static void updateTriggerStatusText(String text) {
        triggerStatus.setText(text);
    }
    
    public static void updateBuildStatusText(String text) {
        buildStatus.setText(text);
    }
    
    public static void updateDownloadStatusText(String text) {
        downloadStatus.setText(text);
    }
    
    public static void updateStatusText(String text) {
        String temp = statusMsg.getText().toString();
        statusMsg.setText(temp + text);
    }
    
    
    public void onBuild(View view) {
        clearStatusTexts();
        loginStatus.setText("Logging In");
        doLogin();
    }
    
    private static void doLogin() {
        LoginData loginData = new LoginData(username, password);
        CodenvyClient.postLogin(loginData, new LoginResponseHandler(wid,project));
        
    }
    
    public static void installAPK(String fileRelPath) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        promptInstall.setDataAndType(Uri.fromFile(new File(fileRelPath)), "application/vnd.android.package-archive");
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(promptInstall);
    }
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    
    public static File getCodenvyDownloadDir() {
        // Get the directory for the app's private pictures directory.
        //     File file = new File(context.getExternalFilesDir(null), "CodenvyAPKs");
        File file = new File(context.getFilesDir(), "CodenvyAPKs");
        if (!file.mkdirs()) {
            HomePageActivity.updateStatusText("Unable to Create Directory");
        }
        return file;
    }
    
    public static String getStackTraceString(Throwable t){
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        String s = writer.toString();
        return s;
    }
    
    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        username = myPrefs.getString(SetupActivity.USER_NAME, "");
        password = myPrefs.getString(SetupActivity.PASSWORD, "");
        wid = myPrefs.getString(SetupActivity.WORKSPACE, "");
        wname = myPrefs.getString(SetupActivity.WORKSPACE_NAME, "");
        project = myPrefs.getString(SetupActivity.PROJECT, "");
        
        String statusMsg = "Error!";
        if(username == ""){
            statusMsg = "Please Login and Select a workspace,project in settings page";
            }else if(wid == "" || project == ""){
            statusMsg = "Please Select a workspace,project in settings page";
            }else{
            statusMsg = "Current Project: " + wname + "/" + project;
        }
        currentProject.setText(statusMsg);
    }
}