package com.gsta.neva2.business.service.event.database;

import com.gsta.neva2.business.utils.Logger;
import com.gsta.neva2.business.utils.StringFormatter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: wangsongwei
 * Date: Sep 15, 2011
 * Time: 11:37:30 AM
 * name: PACKAGE_NAME
 */
public class Dictionary {

    private Map codeMap = new HashMap();
    //    public static Map<Integer, Attribute> codeMap;
    private Map nameMap = new HashMap();
//    public static Map<String, Attribute> nameMap;


    public synchronized static void addAttribute(int code, String name, int type)
            throws CombinerException {

        if (code < 0 || code > 65535) throw new CombinerException("code is not between 0 and 65535.");
        if (name == null || name.equals("")) throw new CombinerException("name cant be null.");
        if (!AttributeType.exists(type)) throw new CombinerException("unknown attribute type.");

        Attribute attribute = new Attribute(code, name, type);
        Dictionary.instance().codeMap.put(new Integer(code), attribute);
        Dictionary.instance().nameMap.put(name, attribute);
    }

    public static Attribute getAttribute(String name) throws CombinerException {
        if (!Dictionary.instance().nameMap.containsKey(name))
            throw new CombinerException("Unknown attribute -> " + name);
        return (Attribute) Dictionary.instance().nameMap.get(name);
    }

    public static Dictionary instance() {
        return Inner.dictionary;
    }

    private static class Inner {
        static Dictionary dictionary = new Dictionary();

        static {
            try {
                List dicts = new DirectoryDAO().queryDicts(null);
                if (dicts == null || dicts.isEmpty()) throw new CombinerException("Attribute directory is empty!");
                Iterator it = dicts.iterator();
                while (it.hasNext()) {
                    AttributeDirectory ad = (AttributeDirectory) it.next();
                    Dictionary.addAttribute(ad.getAttribute_id(), ad.getAttribute_name(), ad.getType());
                    Logger.getInstance().info(
                            new StringFormatter(10, StringFormatter.LEFT).format("    " + String.valueOf(ad.getAttribute_id())).append(" ::   ")
                                    .append(new StringFormatter(40, StringFormatter.LEFT).
                                            format(String.valueOf(ad.getAttribute_name()))), "DictionaryINIT");
                }
            } catch (Exception e) {
                Logger.getInstance().info(e.getCause(), "DictionaryINIT");
                e.printStackTrace();
            }
        }
    }

    private Dictionary() {
    }
}
