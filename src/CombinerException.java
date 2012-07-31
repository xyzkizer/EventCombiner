package com.gsta.neva2.business.service.event.database;

/**
 * Created by IntelliJ IDEA.
 * User: wangsongwei
 * Date: Sep 15, 2011
 * Time: 4:27:36 PM
 * name: PACKAGE_NAME
 */
public class CombinerException extends Exception {

    public CombinerException(String s) {
        super(s);
    }

    public CombinerException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CombinerException(Throwable throwable) {
        super(throwable);
    }
}
