package com.create.whc.processframework;

import android.util.Log;

/**
 * 单例必须实现接口
 */

public class UserManager implements IUserManager {

    private Person person;
    private static UserManager userManager=null;

    private UserManager(){

    }

    public static synchronized UserManager getInstance(){
        if (userManager==null){
            userManager=new UserManager();
            Log.i("whc", "getInstance: --------------->");
        }
        return userManager;
    }

    @Override
    public Person getPerson() {
        return person;
    }

    @Override
    public void setPerson(Person person) {
        this.person=person;
    }

}
