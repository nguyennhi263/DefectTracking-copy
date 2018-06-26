package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/25/2018.
 */

public class Project {
    private  String ProjectID, ProjectName;

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public Project(String projectID, String projectName) {
        ProjectID = projectID;
        ProjectName = projectName;
    }
}
