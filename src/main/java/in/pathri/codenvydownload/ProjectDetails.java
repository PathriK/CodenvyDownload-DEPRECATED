package in.pathri.codenvydownload;
import java.util.List;

public class ProjectDetails extends WorkspaceDetails{
    public List<String> projects;
    ProjectDetails(String name,String id,List<String> projects){
        this.name = name;
        this.id = id;
        this.projects = projects;
    }
}