package com.gsta.neva2.business.service.event.database;

/**
 * Created by IntelliJ IDEA.
 * User: wangsongwei
 * Date: Sep 28, 2011
 * Time: 10:16:03 AM
 * name: PACKAGE_NAME
 */
public class AttributeType {

    public static final AttributeType Integer32 = new AttributeType(1);
    public static final AttributeType Integer64 = new AttributeType(2);
    public static final AttributeType STRING = new AttributeType(3);

    private AttributeType(int type) {
        this.type_value = type;
    }

    private int type_value;

    public static boolean exists(int type) {

        switch (type) {
            case 1:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttributeType that = (AttributeType) o;

        return type_value == that.type_value;
    }

    public int hashCode() {
        return type_value;
    }
}
