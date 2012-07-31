package com.gsta.neva2.business.service.event.database;

import com.gsta.neva2.business.utils.Formatter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: wangsongwei
 * Date: Sep 13, 2011
 * Time: 4:17:34 PM
 * name: PACKAGE_NAME
 */
public class Semantic {

    protected int semantic_id;
    //    protected Map<String, AttributeValue> attrValues = new HashMap<String, AttributeValue>();
    protected Map attrValues = new HashMap();

    public Semantic(int semantic_id) {
        this.semantic_id = semantic_id;
    }

    public void addValue(String name, AttributeValue attrValue) throws CombinerException {

        if (attrValues.containsKey(name)) throw new CombinerException("attribute name already exists.");
        attrValues.put(name, attrValue);
    }

    public void addValue(String name, int value) throws CombinerException {

        this.addValue(name, new AttributeValue(value));
    }

    public void addValue(String name, long value) throws CombinerException {

        this.addValue(name, new AttributeValue(value));
    }


    public void addValue(String name, String value) throws CombinerException {

        this.addValue(name, new AttributeValue(Formatter.NullToEmpty(value)));
    }

    public void addValue(String name, Object value) throws CombinerException {

        if (value instanceof Integer) this.addValue(name, new AttributeValue((Integer) value));
        if (value instanceof Long) this.addValue(name, new AttributeValue((Long) value));
        if (value instanceof String) this.addValue(name, new AttributeValue((String) value));
    }
}
