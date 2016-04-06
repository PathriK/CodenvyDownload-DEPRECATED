package in.pathri.codenvydownload.preferancehandlers;

import android.os.Handler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.pathri.codenvydownload.R;
import in.pathri.codenvydownload.CodenvyClient;
import in.pathri.codenvydownload.LoginData;
import in.pathri.codenvydownload.responsehandlers.LoginDialogResponseHandler;

public class LoginPreference extends DialogPreference {
    
    private Context mContext;
    private static TextView loginStatus;
    public static ProgressBar loginSpinner;
    EditText loginField,passwordField;
    static AlertDialog d;
    public static Handler statusHandler;
    static SharedPreferences settings;
    static String username,password;
    static boolean isLoggedIn;
    
    public LoginPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.login_view);
        mContext = context;
        statusHandler = new Handler();
    }
    
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle(R.string.login_title);
        builder.setPositiveButton("Login", this);
        builder.setNegativeButton("Cancel", this);
        settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        isLoggedIn = false;
        super.onPrepareDialogBuilder(builder);
    }
    
    @Override
    protected void showDialog(Bundle state)
    {
        super.showDialog(state);    //Call show on default first so we can override the handlers
        
        d = (AlertDialog) getDialog();
        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {                
                loginStatus = (TextView) d.findViewById(R.id.login_dialog_status);
                loginSpinner = (ProgressBar) d.findViewById(R.id.login_dialog_progress);                
                loginField = (EditText) d.findViewById(R.id.login_text);
                passwordField = (EditText) d.findViewById(R.id.password_text);
                username = loginField.getText().toString();
                password = passwordField.getText().toString();
                
                LoginData loginData = new LoginData(username, password);
                CodenvyClient.postLogin(loginData, new LoginDialogResponseHandler());
            }
        });
    }
    
    public static void acceptLogin(){
        
        final SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        
        isLoggedIn = true;
        statusHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                d.dismiss();
                editor.commit();
        }},1000);
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {        
        persistBoolean(isLoggedIn);
    }
    
    public static void updateLoginStatus(String statusText){
        loginStatus.setText(statusText);
    }
    
}