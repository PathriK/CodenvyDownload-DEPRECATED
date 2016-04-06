package in.pathri.codenvydownload.responsehandlers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import okhttp3.ResponseBody;

import android.view.View;
import android.widget.ProgressBar;

import in .pathri.codenvydownload.CodenvyResponse;
import in.pathri.codenvydownload.CustomProgressDialog;
import in .pathri.codenvydownload.HomePageActivity;

abstract class ApiResponseHandler < T > implements Callback < T > {
    final static String START = "start";
    final static String END = "end";
    ProgressBar spinner = null;
    CustomProgressDialog pd = null;
    
    private enum ResponseType{
        POJO,BINARY,ARRAY
    }
    ResponseType responseType = ResponseType.BINARY;
    
    ApiResponseHandler(ProgressBar spinner, Class < T > clazz) {
        this.spinner = spinner;
        updateProgress(START);
        if (CodenvyResponse.class.equals(clazz)) {
            this.responseType = ResponseType.POJO;
        }
    }
    
    ApiResponseHandler(CustomProgressDialog pd){
        this.pd = pd;
        updateProgress(START);
        this.responseType = ResponseType.ARRAY;
    }
    
    abstract void updateStatusText(String statusText);
    
    abstract void nextStep(CodenvyResponse codenvyResponse);
    
    abstract void nextStep(List<CodenvyResponse> codenvyResponse);
    
    abstract void nextStep(ResponseBody responseBody);
    
    @Override
    public void onFailure(Call < T > arg0, Throwable t) {
        this.updateStatusText("Connection Error" + HomePageActivity.getStackTraceString(t));
        updateProgress(END);
    }
    
    @Override
    public void onResponse(Call < T > request, Response < T > response) {
        if (response.isSuccessful()) {
            if (200 == response.code()) {
                switch (responseType){
                    case POJO:
                      CodenvyResponse apiResponse = (CodenvyResponse) response.body();
                      if (apiResponse == null) {
                          this.updateStatusText("Empty Response");
                          } else {
                          this.updateStatusText("Success");
                          this.nextStep(apiResponse);
                      }
                      break;
                    case BINARY:
                      ResponseBody apiResponseBinary = (ResponseBody) response.body();
                      if (apiResponseBinary == null) {
                          this.updateStatusText("Empty Response");
                          } else {
                          this.updateStatusText("Success");
                          this.nextStep(apiResponseBinary);
                      }
                      break;
                    case ARRAY:
                      List<CodenvyResponse> apiResponseArr = (List<CodenvyResponse>) response.body();
                      if (apiResponseArr == null) {
                          this.updateStatusText("Empty Response");
                          } else {
                          this.updateStatusText("Success");
                          this.nextStep(apiResponseArr);
                      }
                      break;
                }
            } else {
                this.updateStatusText("Not Success - " + response.code());
            }
        }
        else {
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
        updateProgress(END);
    }
    
    private void updateProgress(String status){
        if(START.equalsIgnoreCase(status)){
            if(spinner != null){
                this.spinner.setVisibility(View.VISIBLE);
            }
            if(pd != null){
                pd.show();
            }
            }else{
            if(spinner != null){
                this.spinner.setVisibility(View.GONE);
            }
            if(pd != null){
                pd.dismiss();
            }
        }
    }
}