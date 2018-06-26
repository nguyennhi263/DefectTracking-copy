package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/18/2018.
 */

public class Phase {
    private  String PhaseID, PhaseDesc, ProjectID;

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getPhaseID() {
        return PhaseID;
    }

    public void setPhaseID(String phaseID) {
        PhaseID = phaseID;
    }

    public String getPhaseDesc() {
        return PhaseDesc;
    }

    public void setPhaseDesc(String phaseDesc) {
        PhaseDesc = phaseDesc;
    }

    public Phase(String phaseID, String phaseDesc) {
        PhaseID = phaseID;
        PhaseDesc = phaseDesc;
    }

    public Phase(String phaseID, String phaseDesc, String projectID) {
        PhaseID = phaseID;
        PhaseDesc = phaseDesc;
        ProjectID = projectID;
    }
}
