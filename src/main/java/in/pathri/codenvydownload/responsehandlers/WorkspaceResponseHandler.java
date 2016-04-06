package in.pathri.codenvydownload.responsehandlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.CustomProgressDialog;
import in.pathri.codenvydownload.WorkspaceDetails;
import in.pathri.codenvydownload.preferancehandlers.SetupActivity;

public class WorkspaceResponseHandler extends ApiResponseHandler <List<CodenvyResponse>>{
    public WorkspaceResponseHandler(CustomProgressDialog workspaceProgressDialog){
        super(workspaceProgressDialog);
    }
    
    @Override
    void nextStep(List<CodenvyResponse> apiResponse) {
        List<String> names = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        List < CodenvyResponse > linksList = apiResponse;
        Iterator < CodenvyResponse > iterator = linksList.iterator();
        while (iterator.hasNext()) {
            CodenvyResponse workspaceDetails = iterator.next();
            String name = workspaceDetails.workspaceReference.name;
            String id = workspaceDetails.workspaceReference.id;
            names.add(name);
            ids.add(id);
            SetupActivity.addWorspaceMap(id,name);
        }
        String[] namesArr = names.toArray(new String[names.size()]);
        String[] idsArr= ids.toArray(new String[ids.size()]);
        SetupActivity.updateWorspacePreference(namesArr,idsArr);
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
        SetupActivity.updateWorkspaceSummary(statusText);
    }
}