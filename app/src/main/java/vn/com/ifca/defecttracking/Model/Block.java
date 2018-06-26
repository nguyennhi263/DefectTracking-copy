package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/18/2018.
 */

public class Block {
    private String BlockID, BlockName, PhaseID;

    public Block(String blockID, String blockName, String phaseID) {
        BlockID = blockID;
        BlockName = blockName;
        PhaseID = phaseID;
    }

    public Block(String blockID, String blockName) {
        BlockID = blockID;
        BlockName = blockName;
    }

    public String getBlockID() {
        return BlockID;
    }

    public void setBlockID(String blockID) {
        BlockID = blockID;
    }

    public String getBlockName() {
        return BlockName;
    }

    public void setBlockName(String blockName) {
        BlockName = blockName;
    }

    public String getPhaseID() {
        return PhaseID;
    }

    public void setPhaseID(String phaseID) {
        PhaseID = phaseID;
    }
}
