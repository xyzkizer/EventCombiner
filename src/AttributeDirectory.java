package com.gsta.neva2.business.service.event.database;

import com.gsta.neva2.business.bo.CommonEntity;
import com.gsta.neva2.business.utils.Formatter;
import com.gsta.neva2.business.utils.Logger;
import com.gsta.neva2.dal.exception.InvalidParameterException;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: wangsongwei
 * Date: 11-11-20
 * Time: 下午2:41
 */
public class AttributeDirectory extends CommonEntity {

    private int attribute_id;
    private String attribute_name;
    private int type;


    public boolean buildFromProperties(Properties value) {
        try {
            this.setAttribute_id(Integer.parseInt(value.getProperty("ATTRIBUTE_ID")));
            this.setAttribute_name(value.getProperty("NAME"));
            this.setType(Integer.parseInt(value.getProperty("TYPE")));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public Properties toProperties() throws InvalidParameterException {
        Properties vo = new Properties();
        try {
            vo.setProperty("ATTRIBUTE_ID", String.valueOf(this.attribute_id));
            vo.setProperty("NAME", Formatter.NullToEmpty(this.attribute_name));
            vo.setProperty("TYPE", String.valueOf(this.type));
        } catch (NullPointerException npe) {
            Logger.getInstance().error("NullPointerException", npe);
            throw new InvalidParameterException("转换为property出现了空对象");
        } catch (NumberFormatException nfe) {
            Logger.getInstance().error("NumberFormatException", nfe);
            throw new InvalidParameterException("转换为property无法转换数字");
        } catch (Exception e) {
            Logger.getInstance().error("Exception", e);
            throw new InvalidParameterException("转换为property出现异常");
        }
        return vo;
    }

    public int getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(int attribute_id) {
        this.attribute_id = attribute_id;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
