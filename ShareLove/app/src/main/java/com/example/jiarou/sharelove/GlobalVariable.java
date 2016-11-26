package com.example.jiarou.sharelove;

import android.app.Application;

/**
 * Created by Peter on 2016/9/19.
 */
public class GlobalVariable extends Application {
    public String userId = "" ;
    public String setUserId(String userId){this.userId = userId;
        return userId;
    }
    public String getUserId(){return userId;}

    public Long zip = 0L;
    public void setZip (Long zip){this.zip = zip;}
    public Long getZip(){return zip;}

    public Integer wow = 0;
    public void setWow(Integer wow){this.wow = wow;}
    public Integer getWow(){return wow;}

    public Integer wow2 = 0;
    public void setWow2(Integer wow2){this.wow2 = wow2;}
    public Integer getWow2(){return wow2;}


}
