package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/18/2018.
 */

public class User {
    String userId,userPosition,fullname,level,email, recordStatus;

    public User(String userId, String userPosition, String fullname, String level) {
        this.userId = userId;
        this.userPosition = userPosition;
        this.fullname = fullname;
        this.level = level;
    }

    public User(String userId, String fullname) {
        this.userId = userId;
        this.fullname = fullname;
    }

    public User(String userId, String userPosition, String fullname, String email, String recordStatus) {
        this.userId = userId;
        this.userPosition = userPosition;
        this.fullname = fullname;
        this.email = email;
        this.recordStatus = recordStatus;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return userId+ " : "+recordStatus;
    }
}
