package in.pathri.codenvydownload.preferancehandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import in.pathri.codenvydownload.CodenvyClient;
import in.pathri.codenvydownload.CustomProgressDialog;
import in.pathri.codenvydownload.HomePageActivity;
import in.pathri.codenvydownload.ProjectDetails;
import in.pathri.codenvydownload.R;
import in.pathri.codenvydownload.preferancehandlers.SetupActivity.SetupFragment;
import in.pathri.codenvydownload.responsehandlers.WorkspaceResponseHandler;
import in.pathri.codenvydownload.responsehandlers.ProjectResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

public class SetupActivity extends PreferenceActivity  {
  public static final String USER_NAME = "username";
  public static final String PASSWORD = "password";
  public static final String IS_LOGGED_IN = "is_logged_in";
  public static final String WORKSPACE = "workspace";
  public static final String PROJECT = "project";
  private static Context mContext = HomePageActivity.context;
  private static ListPreference worspacePrefs,projectPrefs;
  private static Preference userCred;
//   private static List<ProjectDetails> projectDetails;
  private static Map<String,String> workspaceDetails = new HashMap<String,String>();
  private static CharSequence[] workspaceEntries;
  private static CharSequence[] workspaceValues;
  private static Map<String,CharSequence[]> projectDetails= new HashMap<String,CharSequence[]>();
  private static List<CharSequence> wids;
  private static SharedPreferences sharedPreferences;
  
  
//   private static Context sfContext;
//   public static TextView refreshStatus;
  public static CustomProgressDialog workspaceProgressDialog;
  public static CustomProgressDialog projectProgressDialog;
private static SetupFragment setupFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setupFragment = new SetupFragment();
      
		getFragmentManager().beginTransaction().replace(android.R.id.content, setupFragment).commit();
    }
  
  public static class SetupFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
    {
        

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
       
            addPreferencesFromResource(R.xml.preferences);
//            onSharedPreferenceChanged(null, "");
            worspacePrefs = (ListPreference) findPreference("workspace");
    			projectPrefs = (ListPreference) findPreference("project");
          userCred = findPreference(IS_LOGGED_IN);
        }
    
    @Override
    public void onAttach(Activity activity){
                workspaceProgressDialog = new CustomProgressDialog(getActivity(),"Workspace List Refresh","Please Wait...");
          projectProgressDialog = new CustomProgressDialog(getActivity(),"Project List Refresh","Please Wait...");
      
//       refreshStatus = (TextView) findViewById(R.id.refresh_status);

		super.onAttach(activity);
    }
    
    @Override
    public void onResume() {
        super.onResume();
//         Set up a listener whenever a key changes
        sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
          
          String temp = sharedPreferences.getString(USER_NAME, "");
          if(!temp.equals("")){
            userCred.setSummary("Logged In User: " + sharedPreferences.getString(USER_NAME, "-NONE-"));
            if(workspaceDetails.isEmpty()){
              CodenvyClient.getWorkspaceDetails(new WorkspaceResponseHandler());
            }else{
              updateWorspacePreference();
            }
//             temp = sharedPreferences.getString(WORKSPACE, "");
//             if(!temp.equals("")){
// //               worspacePrefs.setSummary(temp);
// //               worspacePrefs.setEnabled(true);
//               if(projectDetails.isEmpty()){
//                 getProjectLists(workspaceValues);
//               }else{
                
//               }
// //               temp = sharedPreferences.getString(PROJECT, "");
// //               if(!temp.equals("")){
// //             		projectPrefs.setSummary(temp);
// //                 projectPrefs.setEnabled(true);
// //               }
//             }
            
          }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    
    
//     private void refreshData(){
//      new Thread(){
//         @Override
//         public void run(){
//           Object workspaceList[] = refreshWorkspace();
//           updateWorspacePreference(workspaceList);
//           Object projectList[] = refreshProject();
//           updateProjectPreference(projectList);
//           toastThis("Updated Workspace &amp; Project Details");          
//         }
//       }.start();
//     }
    
//     private Object[] refreshWorkspace(){
//       ProgressDialog pd = showProgressDialog("Workspace List Refresh","Please Wait...");
//      Object[] result = CodenvyClient.getWorkspaceDetails(refreshStatus);
//       hideProgressDialog(pd);        
//       return result;
//     }

//         private Object[] refreshProject(){
//       ProgressDialog pd = showProgressDialog("Project List Refresh","Please Wait...");
//       Object[] result = CodenvyClient.getProjectDetails(refreshStatus);
//       hideProgressDialog(pd);        
//       return result;
//     }

 
    
    private void toastThis(String msg){
      Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }



  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
        Toast.makeText(getActivity(), "Preference Change" + key, Toast.LENGTH_SHORT).show();
        if (key.equals(USER_NAME)) {
//             Preference userCred = findPreference(IS_LOGGED_IN);
            userCred.setSummary("Logged In User: " + sharedPreferences.getString(USER_NAME, "-NONE-"));
//           CodenvyClient.getWorkspaceDetails(new WorkspaceResponseHandler(workspaceProgressDialog));
                    CodenvyClient.getWorkspaceDetails(new WorkspaceResponseHandler());
        }else if(key.equals(WORKSPACE)){
//             Preference workspace = (ListPreference)findPreference(key);
//             workspace.setSummary(sharedPreferences.getString(key, "-NONE-"));       
            worspacePrefs.setSummary(worspacePrefs.getEntry());
// 			CodenvyClient.getWorkspaceDetails(new WorkspaceResponseHandler(workspaceProgressDialog));          
// 			CodenvyClient.getProjectDetails(worspacePrefs.getValue(),new ProjectResponseHandler(projectProgressDialog));
			
        }else if(key.equals(PROJECT)){
//             ListPreference project = (ListPreference)findPreference(key);
//             project.setSummary(sharedPreferences.getString(key, "-NONE-"));                  
            projectPrefs.setSummary(projectPrefs.getEntry());
        }
    }
    


    }
  
  public static void updateWorspacePreference(){
          worspacePrefs.setEntries(workspaceEntries);
      worspacePrefs.setEntryValues(workspaceValues);
      worspacePrefs.setDefaultValue(0);
         CharSequence tempEntry = worspacePrefs.getEntry();         
         if(tempEntry == null){
           tempEntry = "Please Select a Value";
         }
         updateWorkspaceSummary(tempEntry.toString());
         worspacePrefs.setEnabled(true);  
    getProjectLists(workspaceValues);
  }
     public static void updateWorspacePreference(CharSequence[] namesArr,CharSequence[] idsArr){
       if(namesArr.length > 0){
         workspaceEntries = namesArr;
         workspaceValues = idsArr;
       }else{
         updateWorkspaceSummary("No Workspace Available for this User");
       }
       updateWorspacePreference();
    }

  
//   public static void getProjectLists(List<String> ids){
//    wids = new ArrayList<String>(ids) ;
//           Iterator < String > widIterator = wids.iterator();
//       while (widIterator.hasNext()) {
//        String wid = widIterator.next();

//     CodenvyClient.getProjectDetails(wid,new ProjectResponseHandler(wid));
//       }
    
//   }
  
  
    public static void getProjectLists(CharSequence[] ids){
   wids = new LinkedList<CharSequence>(Arrays.asList(ids));
          Iterator < CharSequence > widIterator = wids.iterator();
      while (widIterator.hasNext()) {
       CharSequence wid = widIterator.next();
String tempWid = wid.toString();
    CodenvyClient.getProjectDetails(tempWid,new ProjectResponseHandler(tempWid));
      }
    
  }

  
  public static void addProjectMap(String wid,CharSequence[] projectsArr){
//     final CharSequence[] projectsArr = projects.toArray(new CharSequence[projects.size()]);
    projectDetails.put(wid,projectsArr);
    popWids(wid);
    if(wids.isEmpty()){
    	String temp = sharedPreferences.getString(WORKSPACE, "");
      if(!temp.equals("")){
        CharSequence[] projectsList = projectDetails.get(temp);
        if(projectsList != null){
          projectPrefs.setEntries(projectsList);
          projectPrefs.setEntryValues(projectsList);
        }
        
      }
    }
    
    
  }
  
  public static void addWorspaceMap(String wid,String name){
    workspaceDetails.put(wid,name);
  }
  
  public static void popWids(String wid){
    wids.remove(wid);
  }
//   private void hashmaptest()
// {
//     //create test hashmap
//     HashMap<String, String> testHashMap = new HashMap<String, String>();
//     testHashMap.put("key1", "value1");
//     testHashMap.put("key2", "value2");

//     //convert to string using gson
//     Gson gson = new Gson();
//     String hashMapString = gson.toJson(testHashMap);

//     //save in shared prefs
//     SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);
//     prefs.edit().putString("hashString", hashMapString).apply();

//     //get from shared prefs
//     String storedHashMapString = prefs.getString("hashString", "oopsDintWork");
//     java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
//     HashMap<String, String> testHashMap2 = gson.fromJson(storedHashMapString, type);

//     //use values
//     String toastString = testHashMap2.get("key1") + " | " + testHashMap2.get("key2");
//     Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
// }
    
        public static void updateProjectPreference(CharSequence projectList[]){
          if(projectList.length > 0){
      projectPrefs.setEntries(projectList);
      projectPrefs.setEntryValues(projectList);
            projectPrefs.setEnabled(true);
                 String temp = sharedPreferences.getString(PROJECT, "");
          if(temp.equals("")){
            updateProjectSummary("Please Select a Value");
            projectPrefs.setDefaultValue(0); 
          }else{
            updateProjectSummary(temp);
          }

      
            
		
         
       }else{
         updateProjectSummary("No Workspace Available for this User");
       }

      
    }
      
  
//   private static void updateAvailableProjs(){
//     CodenvyClient.getProjectDetails(worspacePrefs.getValue(),new ProjectResponseHandler(projectProgressDialog));
//   }
  
        public static void updateWorkspaceSummary(String statusText){
//       if(worspacePrefs == null){
//         Toast.makeText(mContext, "Worspace Pref is null - updateworkspacesummary", Toast.LENGTH_SHORT).show();
//       }          
    	worspacePrefs.setSummary(statusText);
  }
  
        public static void updateProjectSummary(String statusText){
    	projectPrefs.setSummary(statusText);
  }
  
  
}