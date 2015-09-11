package com.team1.moa_member_upload;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

/**
 * Created by songmho on 15. 9. 12.
 */
public class Moa_member_upload extends Application {

    String APPLICATION_ID="ybnO5IVDSPijWZhGfM3KeuvAmXUEWJBNyCEdmFxG";
    String CLIENT_KEY="klsb4BdMzT1JUsm14PeFpc8Rv9uOZCuljXpbadDT";
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
