package in.pathri.codenvydownload.responsehandlers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

import in.pathri.codenvydownload.HomePageActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import android.os.Environment;

 public class ApkDownloadHandlerOld implements Callback < ResponseBody > {
  String buildId;
  ApkDownloadHandler(String buildId) {
   this.buildId = buildId;
  }

  @Override
  public void onFailure(Call < ResponseBody > arg0, Throwable arg1) {
   HomePageActivity.updateStatusText("Connection Error");
  }

  @Override
  public void onResponse(Call < ResponseBody > request, Response < ResponseBody > response) {
   HomePageActivity.updateStatusText(String.valueOf(response.code()));
   if (response.isSuccessful()) {
    String fileName = "CodenvyDownload-" + buildId + ".apk";
    try {
     File path = Environment.getExternalStorageDirectory();
      path = new File(path,"CodenvyAPKs");
     File file = new File(path, fileName);
      path.mkdir();
     FileOutputStream fileOutputStream = new FileOutputStream(file);      
     IOUtils.write(response.body().bytes(), fileOutputStream);
      
	  HomePageActivity.installAPK(file.getAbsolutePath());
    } catch (IOException e) {
     HomePageActivity.updateStatusText("Error while writing file!");
     HomePageActivity.updateStatusText(e.toString());
    }
   }

  }
 }