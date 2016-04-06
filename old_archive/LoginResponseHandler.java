package in.pathri.codenvydownload.responsehandlers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

import in.pathri.codenvydownload.CodenvyClient;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.HomePageActivity;

 public class LoginResponseHandlerOld implements Callback < CodenvyResponse > {
  @Override
  public void onFailure(Call < CodenvyResponse > arg0, Throwable arg1) {
   HomePageActivity.updateLoginStatusText("Connection Error");
  }

  @Override
  public void onResponse(Call < CodenvyResponse > request, Response < CodenvyResponse > response) {
//    HomePageActivity.updateStatusText(String.valueOf(response.code()));
   if (response.isSuccessful()) {
    CodenvyResponse k = response.body();
    if (k == null) {
     HomePageActivity.updateLoginStatusText("Empty Response");
    } else {
     HomePageActivity.updateLoginStatusText("Status Code: " + k.value);
     CodenvyClient.buildProj("workspacevcpucno8mrpb6h0p", "CodenvyDownload", new BuildResponseHandler());
//      CodenvyClient.buildProj("workspacevcpucno8mrpb6h0p", "NightMode", new buildResponseHandler());
    }
   } else {
    ResponseBody k = response.errorBody();
    if (k == null) {
     HomePageActivity.updateLoginStatusText("Empty Error Response");
    }
    try {
     HomePageActivity.updateLoginStatusText("Error: " + k.string());
    } catch (Exception e) {
     e.printStackTrace();
    }
   }


  }

 }