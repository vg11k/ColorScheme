package vg11k.com.colorscheme.schemeGenerator;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import vg11k.com.colorscheme.R;

/**
 * Created by Julien on 06/02/2020.
 */

public enum KindOfProcess {

    LAYER(0, "Layer", R.string.layer),
    AERO(1, "Aero", R.string.aero),
    BRUSH(2, "Brush",R.string.brush),
    LAVIS(3, "Lavis", R.string.lavis),
    GLACIS(4, "Glacis", R.string.glacis),
    DETAIL(5, "Detail", R.string.detail);

    private int value;
    private String name;
    private int stringId;
    private static Map map = new HashMap<Integer, KindOfProcess>();

    static {
        for(KindOfProcess process : KindOfProcess.values()) {
            map.put(process.value, process);
        }
    }

    private KindOfProcess(int v, String s, int displayId) {
        value = v;
        name = s;
        stringId = displayId;
    }

    public static KindOfProcess valueOf(int process) {
        return (KindOfProcess) map.get(process);
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getStringId() {
        return stringId;
    }



    public interface IKindOfProcessedHolder {

        View getKindOfProcessView();
        void setKindOfProcessOnModel(KindOfProcess k, Resources resources);

    }

}
