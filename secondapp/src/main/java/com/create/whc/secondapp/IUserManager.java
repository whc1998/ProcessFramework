package com.create.whc.secondapp;

import com.create.whc.baselib.annotion.ClassId;

@ClassId("com.create.whc.processframework.UserManager")
public interface IUserManager {

    Person getPerson();

    void setPerson(Person person);

}
