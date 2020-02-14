package vg11k.com.colorscheme.schemeGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julien on 03/02/2020.
 */

public enum SchemeViewTypeLine {

    VIEW_TYPE_PREVIEW_IMAGE(0),
    VIEW_TYPE_ADD_BUTTON(1),
    VIEW_TYPE_CONTENT_ROW(2),
    VIEW_TYPE_HEADER(3),
    VIEW_TYPE_MIXED_HEADER(4);

    private int value;
    private static Map map = new HashMap<Integer, SchemeViewTypeLine>();

    private SchemeViewTypeLine(int v) {
        value = v;
    }

    static {
        for(SchemeViewTypeLine schemeViewTypeLine : SchemeViewTypeLine.values()) {
            map.put(schemeViewTypeLine.value, schemeViewTypeLine);
        }
    }

    public static SchemeViewTypeLine valueOf(int schemeViewType) {
        return (SchemeViewTypeLine) map.get(schemeViewType);
    }

    public int getValue() {
        return value;
    }

    public static int getSize() { return map.size();}
}
