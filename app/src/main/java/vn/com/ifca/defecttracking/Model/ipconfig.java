package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/18/2018.
 */

public class ipconfig {
     private String ipconfig= "http://demo.ifca.com.vn:6789/_IFCA_WebServer/process.php";
   // private String ipconfig= "http://192.168.0.19:8080/_IFCA_WebServer/process.php";
    //private String ipconfig= "http://14.161.24.150:8765/_IFCA_WebServer/process.php";
    public String getIpconfig() {
        return ipconfig;
    }
    public ipconfig() {
    }
    public ipconfig(String ipconfig) {
        this.ipconfig = ipconfig;
    }
}
