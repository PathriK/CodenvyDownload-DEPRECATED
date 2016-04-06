package in.pathri.codenvydownload.responsehandlers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

import java.util.Iterator;
import java.util.List;

import in.pathri.codenvydownload.CodenvyClient;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.HomePageActivity;
import in.pathri.codenvydownload.ResourceLinks;

 public class StatusResponseHandlerOld implements Callback < CodenvyResponse > {
  @Override
  public void onFailure(Call < CodenvyResponse > arg0, Throwable arg1) {
   HomePageActivity.updateStatusText("Connection Error");
  }

  @Override
  public void onResponse(Call < CodenvyResponse > request, Response < CodenvyResponse > response) {
   HomePageActivity.updateStatusText(String.valueOf(response.code()));
   if (response.isSuccessful()) {
    final CodenvyResponse k = response.body();
    if (k == null) {
     HomePageActivity.updateStatusText("response success - parsing null");
    } else {
     if ("IN_QUEUE".equals(k.status) || "IN_PROGRESS".equals(k.status)) {
      HomePageActivity.updateStatusText("Build Status" + k.status);
      //                 Thread.sleep(10000);
      //               	 CodenvyClient.buildStatus("workspacevcpucno8mrpb6h0p",k.taskId,new statusResponseHandler());
     HomePageActivity.statusHandler.postDelayed(new Runnable() {
       @Override
       public void run() {
        CodenvyClient.buildStatus("workspacevcpucno8mrpb6h0p", k.taskId, new StatusResponseHandler());
       }
      }, 5000);
     } else if ("FAILED".equals(k.status)) {
      HomePageActivity.updateStatusText("Build Status Failed" + k.status);
     } else if ("SUCCESSFUL".equals(k.status)) {
      HomePageActivity.updateStatusText("Build Status Success" + k.status);

      List < ResourceLinks > linksList = k.links;
      Iterator < ResourceLinks > iterator = linksList.iterator();
      while (iterator.hasNext()) {
       ResourceLinks resourceLink = iterator.next();
       //   updateStatusText("URL HREF : " + resourceLink.href);
       //   updateStatusText("URL REL : " + resourceLink.rel);
       if ("download result".equals(resourceLink.rel)) {
        String downloadURL = resourceLink.href;
        CodenvyClient.getAPK(downloadURL, new ApkDownloadHandler(k.taskId));
        break;
       }
      }

     } else {
      HomePageActivity.updateStatusText("Build Status Unknown" + k.status);
     }
     //           CodenvyClient.buildProj("workspacevcpucno8mrpb6h0p","CodenvyDownload",new buildResponseHandler());
    }


   } else {
    ResponseBody k = response.errorBody();
    if (k == null) {
     HomePageActivity.updateStatusText("response error parsing null");
    }
    try {
     HomePageActivity.updateStatusText(k.string());
    } catch (Exception e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
    }
   }


  }

 }
