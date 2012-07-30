package com.gsta.neva2.business.service.event.database;

/**
 * Created by IntelliJ IDEA.
 * User: wangsongwei
 * Date: Sep 13, 2011
 * Time: 5:18:38 PM
 * name: PACKAGE_NAME
 */
public class Attribute {

    protected int code;
    protected String name;
    protected AttributeType type;

    public Attribute(int code, String name, int type) throws CombinerException {
        this.code = code;
        this.name = name;
        switch (type) {
            case 1:
                this.type = AttributeType.Integer32;
                break;
            case 2:
                this.type = AttributeType.Integer64;
                break;
            case 3:
                this.type = AttributeType.STRING;
                break;
            default:
                throw new CombinerException("attribute type is unknown.");
        }
    }
}
