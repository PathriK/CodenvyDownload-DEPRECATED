package in.pathri.codenvydownload.responsehandlers;

import android.content.Context;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.HomePageActivity;
import java.util.List;

import okhttp3.ResponseBody;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class ApkDownloadHandler extends ApiResponseHandler<ResponseBody>{
    String buildId;
    
    ApkDownloadHandler(String buildId) {
        super(HomePageActivity.downloadSpinner,ResponseBody.class);
        this.buildId = buildId;
    }
    
    @Override
    void nextStep(ResponseBody rawResponse) {
        String fileName = "CodenvyDownload-" + buildId + ".apk";
        try {
            FileOutputStream fileOutputStream =
            HomePageActivity.context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
            IOUtils.write(rawResponse.bytes(), fileOutputStream);
            this.updateStatusText("Downloaded, Installing..");
            String path = HomePageActivity.context.getFilesDir().getAbsolutePath() + "/" + fileName;
            this.updateStatusText(path);
            HomePageActivity.installAPK(path);
        } catch (IOException e) {
            this.updateStatusText("Error while writing file! " + e.toString());
        }
    }
    
    @Override
    void nextStep(CodenvyResponse codenvyResponses) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
    }
    
    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        HomePageActivity.updateTriggerStatusText("Application Error!!");
    }
    
    @Override
    void updateStatusText(String statusText) {
        HomePageActivity.updateDownloadStatusText(statusText);
    }
}