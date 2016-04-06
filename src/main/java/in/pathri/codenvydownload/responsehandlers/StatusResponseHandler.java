package in.pathri.codenvydownload.responsehandlers;

import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;

import in.pathri.codenvydownload.CodenvyClient;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.HomePageActivity;
import in.pathri.codenvydownload.ResourceLinks;

public class StatusResponseHandler extends ApiResponseHandler<CodenvyResponse> {
    String wid;
    StatusResponseHandler(String wid){
        super(HomePageActivity.buildSpinner,CodenvyResponse.class);
        this.wid = wid;
    }
    @Override
    void nextStep(CodenvyResponse codenvyResponse) {
        final CodenvyResponse currentResponse = codenvyResponse;
        if ("IN_QUEUE".equals(currentResponse.status) || "IN_PROGRESS".equals(currentResponse.status)) {
            this.updateStatusText(currentResponse.status);
            HomePageActivity.statusHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CodenvyClient.buildStatus(wid, currentResponse.taskId, new StatusResponseHandler(wid));
                }
            }, 5000);
            } else if ("FAILED".equals(currentResponse.status)) {
            this.updateStatusText(currentResponse.status);
            } else if ("SUCCESSFUL".equals(currentResponse.status)) {
            this.updateStatusText(currentResponse.status);
            List < ResourceLinks > linksList = currentResponse.links;
            Iterator < ResourceLinks > iterator = linksList.iterator();
            while (iterator.hasNext()) {
                ResourceLinks resourceLink = iterator.next();
                if ("download result".equals(resourceLink.rel)) {
                    String downloadURL = resourceLink.href;
                    CodenvyClient.getAPK(downloadURL, new ApkDownloadHandler(currentResponse.taskId));
                    break;
                }
            }
            } else {
            this.updateStatusText("Build Status Unknown" + currentResponse.status);
        }
    }
    
    @Override
    void nextStep(ResponseBody arg0) {
        HomePageActivity.updateStatusText("Application Error!!");
    }
    
    @Override
    void updateStatusText(String statusText) {
        HomePageActivity.updateBuildStatusText(statusText);
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
    }
}