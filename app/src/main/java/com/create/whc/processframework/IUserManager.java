package com.create.whc.processframework;

import com.create.whc.baselib.annotion.ClassId;

@ClassId("com.create.whc.processframework.UserManager")
public interface IUserManager {

    Person getPerson();

    void setPerson(Person person);

}
