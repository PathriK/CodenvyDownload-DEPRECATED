package in.pathri.codenvydownload.preferancehandlers;

import in.pathri.codenvydownload.HomePageActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;

public class DynamicListPreference extends ListPreference{
  String category = "Null";
private FrameLayout progress;
  CharSequence myEntries[];
  CharSequence myEntryValues[];
  Context context;
ProgressDialog pd;
  
  
  public DynamicListPreference (Context context, AttributeSet attrs) {      
        super(context, attrs);      
    this.context = context;
//     this.context = HomePageActivity.context;
    		category = attrs.getAttributeValue(null, "category");
    if(category == null){
      category = "Null";
    }
//         setEntries(new String[]{"oNe"});         
//         setEntryValues(new String[]{"oNeV"});         
//         setValueIndex(0);       
    }  
  
   
   @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) 
    {
//       showProgressDialog();
      new Handler().postDelayed(new Runnable() {
    public void run() {
//       hideProgressDialog();
    }
  }, 5000);
        setEntries(entries(category));         
        setEntryValues(entryValues(category));         
        setValueIndex(0);        
        super.onPrepareDialogBuilder(builder);  
    }
  


    public DynamicListPreference (Context context) {      
        this(context, null);  
    }  

    private CharSequence[] entries(String category) {      
        //action to provide entry data in char sequence array for list          
        String myEntries[] = {category+"one", category+"two", category+"three", category+"four", category+"five"};         
try {
    Thread.sleep(5);
} catch (InterruptedException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
        return myEntries;  
    }  

    private CharSequence[] entryValues(String category) {      
        //action to provide value data for list           
     try {
    Thread.sleep(5);
} catch (InterruptedException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
        String myEntryValues[] = {category+"ten", category+"twenty", category+"thirty", category+"forty", category+"fifty"};
        return myEntryValues;
   }
  
}
