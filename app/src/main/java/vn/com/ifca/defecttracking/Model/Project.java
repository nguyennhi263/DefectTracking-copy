package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/25/2018.
 */

public class Project {
    private  String ProjectID, ProjectName, report_Defect_Close, report_Defect_Open;

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

    public Project(String projectName, String report_Defect_Close, String report_Defect_Open) {
        ProjectName = projectName;
        this.report_Defect_Close = report_Defect_Close;
        this.report_Defect_Open = report_Defect_Open;
    }

    public String getReport_Defect_Close() {
        return report_Defect_Close;
    }

    public void setReport_Defect_Close(String report_Defect_Close) {
        this.report_Defect_Close = report_Defect_Close;
    }

    public String getReport_Defect_Open() {
        return report_Defect_Open;
    }

    public void setReport_Defect_Open(String report_Defect_Open) {
        this.report_Defect_Open = report_Defect_Open;
    }
}
