package com.gsta.neva2.business.service.event.database;

/**
 * Created by IntelliJ IDEA.
 * User: wangsongwei
 * Date: Sep 14, 2011
 * Time: 4:21:01 PM
 * name: PACKAGE_NAME
 */
public class AttributeValue {

    Object value;

    public AttributeValue(int value) {
        this.value = new Integer(value);
    }

    public AttributeValue(Integer value) {
        this.value = value;
    }

    public AttributeValue(long value) {
        this.value = new Long(value);
    }

    public AttributeValue(Long value) {
        this.value = value;
    }

    public AttributeValue(String value) {
        this.value = value;
    }

    public int getIntValue() {
        return ((Integer) value).intValue();
    }

    public long getLongValue() {
        return ((Long) value).longValue();
    }

    public String getStringValue() {
        return (String) value;
    }
}
