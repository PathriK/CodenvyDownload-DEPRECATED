package in.pathri.codenvydownload.preferancehandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.pathri.codenvydownload.CodenvyClient;
import in.pathri.codenvydownload.CustomProgressDialog;
import in.pathri.codenvydownload.HomePageActivity;
import in.pathri.codenvydownload.ProjectDetails;
import in.pathri.codenvydownload.R;
import in.pathri.codenvydownload.preferancehandlers.SetupActivity.SetupFragment;
import in.pathri.codenvydownload.responsehandlers.WorkspaceResponseHandler;
import in.pathri.codenvydownload.responsehandlers.ProjectResponseHandler;

public class SetupActivity extends PreferenceActivity  {
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String IS_LOGGED_IN = "is_logged_in";
    public static final String WORKSPACE = "workspace";
    public static final String PROJECT = "project";
    public static final String WORKSPACE_NAME = "workspace_name";
    private static Context mContext = HomePageActivity.context;
    private static ListPreference worspacePrefs,projectPrefs;
    private static Preference userCred;
    private static Map<String,String> workspaceDetails = new HashMap<String,String>();
    private static String[] workspaceEntries;
    private static String[] workspaceValues;
    private static Map<String,String[]> projectDetails= new HashMap<String,String[]>();
    private static List<String> wids;
    private static SharedPreferences sharedPreferences;
    public static CustomProgressDialog workspaceProgressDialog;
    public static CustomProgressDialog projectProgressDialog;
    private static SetupFragment setupFragment;
    private static Gson gson;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragment = new SetupFragment();
        gson = new Gson();
        getFragmentManager().beginTransaction().replace(android.R.id.content, setupFragment).commit();
    }
    
    public static class SetupFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);            
            addPreferencesFromResource(R.xml.preferences);
            worspacePrefs = (ListPreference) findPreference("workspace");
            projectPrefs = (ListPreference) findPreference("project");
            userCred = findPreference(IS_LOGGED_IN);
        }
        
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

          Button btn = new Button(getActivity().getApplicationContext());
          btn.setText("Refresh");

          v.addView(btn);
          btn.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                if("-NONE-".equals(sharedPreferences.getString(USER_NAME, "-NONE-"))){
                	toastThis("Please Login before refresh");
                }else{
                	CodenvyClient.getWorkspaceDetails(new WorkspaceResponseHandler(workspaceProgressDialog));  
                }
              }
          });

          return v;
      }
      
        @Override
        public void onAttach(Activity activity){
            workspaceProgressDialog = new CustomProgressDialog(getActivity(),"Workspace List Refresh","Please Wait...");
            projectProgressDialog = new CustomProgressDialog(getActivity(),"Project List Refresh","Please Wait...");            
            super.onAttach(activity);
        }
        
        @Override
        public void onResume() {
            super.onResume();
            sharedPreferences = getPreferenceScreen().getSharedPreferences();
            getMapPrefs(sharedPreferences);
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);            
            String temp = sharedPreferences.getString(USER_NAME, "");
            if(!temp.equals("")){
                userCred.setSummary("Logged In User: " + temp);
                if(workspaceDetails.isEmpty()){
                    CodenvyClient.getWorkspaceDetails(new WorkspaceResponseHandler(workspaceProgressDialog));
                    }else{
                    updateWorspacePreference();
                    updateProjectPreference();
                }                
            }
        }
        
        @Override
        public void onPause() {
            super.onPause();
            // Set up a listener whenever a key changes
            sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            updateMapPrefs(sharedPreferences);
        }
        
        private void updateMapPrefs(SharedPreferences sharedPreferences){            
            SharedPreferences.Editor editor = sharedPreferences.edit();            
            String workspaceDetailsString = gson.toJson(workspaceDetails);
            editor.putString("workspace_details", workspaceDetailsString);            
            String workspaceEntriesString = gson.toJson(workspaceEntries);
            editor.putString("workspace_entries", workspaceEntriesString);            
            String workspaceValuesString = gson.toJson(workspaceValues);
            editor.putString("workspace_values", workspaceValuesString);            
            String projectDetailsString = gson.toJson(projectDetails);            
            editor.putString("project_details", projectDetailsString);
            editor.commit();
         }
        
        private void getMapPrefs(SharedPreferences sharedPreferences){
            java.lang.reflect.Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
            java.lang.reflect.Type charseqType = new TypeToken<String[]>(){}.getType();
            java.lang.reflect.Type mapcharseqType = new TypeToken<Map<String,String[]>>(){}.getType();
            String workspaceDetailsString = sharedPreferences.getString("workspace_details", "");
            if(workspaceDetailsString != ""){
                workspaceDetails = gson.fromJson(workspaceDetailsString, mapType);
            }
            String projectDetailsString = sharedPreferences.getString("project_details", "");
            if(projectDetailsString != ""){
                projectDetails = gson.fromJson(projectDetailsString, mapcharseqType);
            }
            String workspaceEntriesString = sharedPreferences.getString("workspace_entries", "");
            if(workspaceEntriesString != ""){
                workspaceEntries = gson.fromJson(workspaceEntriesString, charseqType);
            }
            String workspaceValuesString = sharedPreferences.getString("workspace_values", "");
            if(workspaceValuesString != ""){
                workspaceValues = gson.fromJson(workspaceValuesString, charseqType);
            }           
        }
        
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
            if (key.equals(USER_NAME)) {
                userCred.setSummary("Logged In User: " + sharedPreferences.getString(USER_NAME, "-NONE-"));
                CodenvyClient.getWorkspaceDetails(new WorkspaceResponseHandler(workspaceProgressDialog));
                }else if(key.equals(WORKSPACE)){
                worspacePrefs.setSummary(worspacePrefs.getEntry());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String workspaceName = worspacePrefs.getEntry().toString();
                editor.putString("workspace_name", workspaceName);
              	 editor.remove(PROJECT);
                editor.commit();
                updateProjectPreference();
                }else if(key.equals(PROJECT)){
                projectPrefs.setSummary(projectPrefs.getEntry());
            }
        }
      
        private void toastThis(String msg){
      		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
//         if(projectDetails.isEmpty()){
//             getProjectLists(workspaceValues);
//             }else{
//             updateProjectPreference();
//         }
    }
    
    public static void updateWorspacePreference(String[] namesArr,String[] idsArr){
        if(namesArr.length > 0){
            workspaceEntries = namesArr;
            workspaceValues = idsArr;
            }else{
            updateWorkspaceSummary("No Workspace Available for this User");
        }
        	updateWorspacePreference();
      	getProjectLists(workspaceValues);
      	updateProjectPreference();
    }
    
    public static void getProjectLists(String[] ids){
        wids = new LinkedList<String>(Arrays.asList(ids));
        Iterator < String > widIterator = wids.iterator();
        projectProgressDialog.show();
        while (widIterator.hasNext()) {
            String wid = widIterator.next();
            String tempWid = wid.toString();
            CodenvyClient.getProjectDetails(tempWid,new ProjectResponseHandler(tempWid));
        }
    }
    
    public static void addProjectMap(String wid,String[] projectsArr){
        projectDetails.put(wid,projectsArr);
        popWids(wid);
        if(wids.isEmpty()){
            projectProgressDialog.dismiss();
            updateProjectPreference();
        }
    }
    
    public static void addWorspaceMap(String wid,String name){
        workspaceDetails.put(wid,name);
    }
    
    public static void popWids(String wid){
        wids.remove(wid);
    }
    
    private static void updateProjectPreference(){
        String temp = sharedPreferences.getString(WORKSPACE, "");
        if(!temp.equals("")){
            String[] projectsList = projectDetails.get(temp);
            if(projectsList != null && projectsList.length != 0){
                projectPrefs.setEntries(projectsList);
                projectPrefs.setEntryValues(projectsList);
                projectPrefs.setEnabled(true);
                temp = sharedPreferences.getString(PROJECT, "");
                if(temp.equals("")){
                    updateProjectSummary("Please Select a Value");
                    projectPrefs.setDefaultValue(0);
                    }else{
                    updateProjectSummary(temp);
                }
                }else{
                updateProjectSummary("No Project Available for the selected Workspace");
                projectPrefs.setEnabled(false);
            }
        }
    }
    
    public static void updateWorkspaceSummary(String statusText){
        worspacePrefs.setSummary(statusText);
    }
    
    public static void updateProjectSummary(String statusText){
        projectPrefs.setSummary(statusText);
    }
  

}