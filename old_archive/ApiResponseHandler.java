package in.pathri.codenvydownload.responsehandlers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import okhttp3.ResponseBody;

import android.view.View;
import android.widget.ProgressBar;
import in.pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.HomePageActivity;

abstract class ApiResponseHandler implements Callback < CodenvyResponse > {
  ProgressBar spinner;
ApiResponseHandler(ProgressBar spinner){
  this.spinner = spinner;
  spinner.setVisibility(View.VISIBLE);
}
  
abstract void updateStatusText(String statusText);
  
  abstract void nextStep(CodenvyResponse codenvyResponse,okhttp3.Response rawResponse);
  
    @Override
  public void onFailure(Call < CodenvyResponse > arg0, Throwable t) {
   this.updateStatusText("Connection Error" + HomePageActivity.getStackTraceString(t));
    this.spinner.setVisibility(View.GONE);
  }

  @Override
  public void onResponse(Call < CodenvyResponse > request, Response < CodenvyResponse > response) {
//    HomePageActivity.updateStatusText(String.valueOf(response.code()));
   if (response.isSuccessful()) {
    CodenvyResponse codenvyResponse = response.body();
     okhttp3.Response rawResponse = response.raw();
    if (codenvyResponse == null) {
     this.updateStatusText("Empty Response");
    } else {
      if(200 == response.code()){
     this.updateStatusText("Success");
      this.nextStep(codenvyResponse,rawResponse);
      }else{
        this.updateStatusText("Not Success - " + response.code());
      }
    }
   } else {
    ResponseBody k = response.errorBody();
    if (k == null) {
     this.updateStatusText("Empty Error Response");
    }
    try {
     this.updateStatusText("Error: " + k.string());
    } catch (Exception e) {
     e.printStackTrace();
    }
   }

	this.spinner.setVisibility(View.GONE);
  }
}
