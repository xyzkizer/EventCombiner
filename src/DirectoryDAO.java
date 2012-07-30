package com.gsta.neva2.business.service.event.database;

import com.gsta.neva2.dal.DAOObject;
import com.gsta.neva2.dal.DBManager;
import com.gsta.neva2.dal.EntityContainer;
import com.gsta.neva2.dal.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: wangsongwei
 * Date: 11-11-20
 * Time: ÏÂÎç2:19
 */
public class DirectoryDAO extends DAOObject {

    public DirectoryDAO() {
        super(DBManager.getInstance().getDB(DBManager.ORACLE));
    }

    public List queryDicts(Session session) throws Exception {
        String methodName = "queryDicts";
        EntityContainer ec = new EntityContainer("com.gsta.neva2.business.service.event.database.AttributeDirectory");
        ec.setMethodName(methodName);
        Properties param = new Properties();

        if (this.find(ec, param, session) > 0)
            return ec.getEntities();
        return new ArrayList();
    }
}
