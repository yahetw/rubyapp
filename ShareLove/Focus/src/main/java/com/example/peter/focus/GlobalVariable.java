package com.example.peter.focus;

import android.app.Application;

/**
 * Created by Peter on 2016/9/19.
 */
public class GlobalVariable extends Application {
    public String userId = "" ;
    public void setUserId(String userId){this.userId = userId;}
    public String getUserId(){return userId;}

    public Long zip = 0L;
    public void setZip (Long zip){this.zip = zip;}
    public Long getZip(){return zip;}

    /*
    public ArrayList<String> collectedVendor = new ArrayList<>();
    public void setCollectedVendor(String key){this.collectedVendor.add(key);}
    public ArrayList<String> getCollectedVendor(){return collectedVendor;}
    */
}
