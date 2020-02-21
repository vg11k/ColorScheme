package vg11k.com.colorscheme.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vg11k.com.colorscheme.colorConverterTool.ColorConverterToolActivity;
import vg11k.com.colorscheme.colorPicker.ColorPickerItemFragment;
import vg11k.com.colorscheme.grid.GridSchemeActivity;
import vg11k.com.colorscheme.schemeGenerator.SchemeGeneratorActivity;

/**
 * Created by Julien on 01/10/2019.
 */

public class MenusContainer {

    public static final List<MenuGenerique> FEATURESLIST = new ArrayList<MenuGenerique>();

    public static final Map<String, MenuGenerique> FEATURES_MAP = new HashMap<String, MenuGenerique>();

    private static final int NB_FEATURE = 2;

    static {
        //addItem(new MenuGenerique("0", MenuGenerique.GENERIC_TITLE));
        //addItem(new MenuGenerique("0", ColorPickerItemFragment.FRAGMENT_TITLE));
        addItem(new MenuGenerique("1", ColorConverterToolActivity.ACTIVITY_TITLE));
        //addItem(new MenuGenerique("2", SchemeGeneratorActivity.ACTIVITY_TITLE));
        //addItem(new MenuGenerique("3",SchemeGeneratorFragment.FRAGMENT_TITLE));
        addItem(new MenuGenerique("4", SchemeGeneratorActivity.ACTIVITY_TITLE));
        addItem(new MenuGenerique("5", GridSchemeActivity.ACTIVITY_TITLE));
        addItem(new MenuGenerique("x", MenuGenerique.GENERIC_TITLE));
    }

    private static void addItem(MenuGenerique feature) {
        FEATURESLIST.add(feature);
        FEATURES_MAP.put(feature.getId(), feature);
    }
}
