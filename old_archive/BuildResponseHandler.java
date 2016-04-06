package in.pathri.codenvydownload.responsehandlers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

import in.pathri.codenvydownload.CodenvyClient;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.HomePageActivity;

 public class BuildResponseHandlerOld implements Callback < CodenvyResponse > {
   @Override
   public void onFailure(Call < CodenvyResponse > arg0, Throwable arg1) {
    HomePageActivity.updateBuildStatusText("Connection Error");
   }

   @Override
   public void onResponse(Call < CodenvyResponse > arg0, Response < CodenvyResponse > response) {
//     HomePageActivity.updateStatusText(String.valueOf(response.code()));

    if (response.isSuccessful()) {
     CodenvyResponse k = response.body();
     if (k == null) {
      HomePageActivity.updateBuildStatusText("Empty Response");
     } else {
      //           updateStatusText("Yet to get taskId");
      HomePageActivity.updateBuildStatusText("TaskID:" + k.taskId);
      CodenvyClient.buildStatus("workspacevcpucno8mrpb6h0p", k.taskId, new StatusResponseHandler());
     }


    } else {
     ResponseBody k = response.errorBody();
     if (k == null) {
      HomePageActivity.updateBuildStatusText("Empty Error Response");
     }
     try {
      HomePageActivity.updateBuildStatusText("Error: " + k.string());
     } catch (Exception e) {
      e.printStackTrace();
     }
    }


   }

  }
