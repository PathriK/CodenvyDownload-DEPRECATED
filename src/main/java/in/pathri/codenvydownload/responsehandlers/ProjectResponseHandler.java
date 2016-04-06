package in.pathri.codenvydownload.responsehandlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.CustomProgressDialog;
import in.pathri.codenvydownload.WorkspaceDetails;
import in.pathri.codenvydownload.preferancehandlers.SetupActivity;

public class ProjectResponseHandler extends ApiResponseHandler <List<CodenvyResponse>>{
    private String wid;
    
    public ProjectResponseHandler(String wid){
        super(null);
        this.wid = wid;
    }
    
    @Override
    void nextStep(List<CodenvyResponse> apiResponse) {
        List<String> names = new ArrayList<String>();
        List < CodenvyResponse > linksList = apiResponse;
        Iterator < CodenvyResponse > iterator = linksList.iterator();
        while (iterator.hasNext()) {
            CodenvyResponse projectDetails = iterator.next();
            String name = projectDetails.name;
            names.add(name);
        }
        final String[] namesArr = names.toArray(new String[names.size()]);
        SetupActivity.addProjectMap(wid,namesArr);
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
        updateStatusText("Application Error!!");
    }
    
    @Override
    void nextStep(CodenvyResponse arg0) {
        updateStatusText("Application Error!!");
    }
    
    @Override
    void updateStatusText(String statusText) {
        SetupActivity.updateProjectSummary(statusText);
    }
}