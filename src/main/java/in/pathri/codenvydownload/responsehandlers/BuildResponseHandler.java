package in.pathri.codenvydownload.responsehandlers;

import okhttp3.ResponseBody;
import in.pathri.codenvydownload.CodenvyClient;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.HomePageActivity;
import java.util.List;

public class BuildResponseHandler extends ApiResponseHandler<CodenvyResponse>{
    String wid;
    BuildResponseHandler(String wid){      
        super(HomePageActivity.triggerSpinner,CodenvyResponse.class);
        this.wid = wid;      
    }
    
    @Override
    void nextStep(CodenvyResponse codenvyResponse) {
        this.updateStatusText("TaskID:" + codenvyResponse.taskId);
        CodenvyClient.buildStatus(wid, codenvyResponse.taskId, new StatusResponseHandler(wid));
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
    }
    
    
    @Override
    void updateStatusText(String statusText) {
        HomePageActivity.updateTriggerStatusText(statusText);
    }
}
