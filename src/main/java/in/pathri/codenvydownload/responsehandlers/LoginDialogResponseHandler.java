package in.pathri.codenvydownload.responsehandlers;

import okhttp3.ResponseBody;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.preferancehandlers.LoginPreference;
import java.util.List;

public class LoginDialogResponseHandler extends ApiResponseHandler<CodenvyResponse> {
    public LoginDialogResponseHandler(){
        super(LoginPreference.loginSpinner,CodenvyResponse.class);
    }
    
    @Override
    void nextStep(CodenvyResponse codenvyResponses) {
        LoginPreference.updateLoginStatus("NextStep");
        LoginPreference.acceptLogin();
    }
    
    @Override
    void updateStatusText(String statusText) {
        LoginPreference.updateLoginStatus(statusText);
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
        LoginPreference.updateLoginStatus("Application Error!!");
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        LoginPreference.updateLoginStatus("Application Error!!");
    }
}