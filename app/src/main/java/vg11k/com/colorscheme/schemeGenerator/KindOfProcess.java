package vg11k.com.colorscheme.schemeGenerator;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julien on 06/02/2020.
 */

public enum KindOfProcess {

    LAYER(0, "Layer"),
    AERO(1, "Aero"),
    BRUSH(2, "Brush"),
    LAVIS(3, "Lavis"),
    GLACIS(4, "Glacis"),
    DETAIL(5, "Detail");

    private int value;
    private String name;
    private static Map map = new HashMap<Integer, KindOfProcess>();

    static {
        for(KindOfProcess process : KindOfProcess.values()) {
            map.put(process.value, process);
        }
    }

    private KindOfProcess(int v, String s) {
        value = v;
        name = s;
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



    public interface IKindOfProcessedHolder {

        View getKindOfProcessView();
        void setKindOfProcessOnModel(KindOfProcess k);

    }

}
