package in.pathri.codenvydownload.responsehandlers;

import okhttp3.ResponseBody;
import in.pathri.codenvydownload.CodenvyClient;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.HomePageActivity;
import java.util.List;

public class LoginResponseHandler extends ApiResponseHandler<CodenvyResponse> {
    String wid,project;
    
    public LoginResponseHandler(String wid,String project){
        super(HomePageActivity.loginSpinner,CodenvyResponse.class);
        this.wid = wid;
        this.project =project;
    }
    
    @Override
    void nextStep(CodenvyResponse codenvyResponses) {
        CodenvyClient.buildProj(wid, project, new BuildResponseHandler(wid));
    }
    
    @Override
    void updateStatusText(String statusText) {
        HomePageActivity.updateLoginStatusText(statusText);
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
        HomePageActivity.updateLoginStatusText("Application Error!!");
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
    }
}