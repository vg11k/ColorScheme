package vg11k.com.colorscheme;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julien on 21/02/2020.
 */

public enum StorageKind {

    ROOT(0, "root"),
    LOCAL(1, "local"),
    WEB(2, "web");

    public static String m_ID = "STORAGE_KIND";

    private int value;
    private static Map map = new HashMap<Integer, StorageKind>();

    private String folderName;
    private static String PATH;
    private static boolean HAS_BEEN_INITIALIZED = false;

    private StorageKind(int i, String s) {
        value = i;
        folderName = s;
    }

    static {
        for(StorageKind kind : StorageKind.values()) {
            map.put(kind.value, kind);
        }
    }

    public int getValue() {return value;}

    public static StorageKind valueOf(int i) {
        return (StorageKind) map.get(i);
    }

    public static int getSize() {return map.size();}

    public static void initializePath(Context context) {

        String rootDirectory = context.getFilesDir().toString();
        File rootFolder = new File(rootDirectory);
        if(!rootFolder.exists())
            rootFolder.mkdirs();

        PATH = rootFolder.getAbsolutePath();

        for(int i = 1; i < getSize(); i++) {
            File subFolder = new File(PATH + "/" + valueOf(i).folderName);
            if(!subFolder.exists())
                subFolder.mkdirs();
        }

        HAS_BEEN_INITIALIZED = true;
    }

    public static boolean hasBeenInitialized(){
        return HAS_BEEN_INITIALIZED;
    }

    public String getFullPath() {
        if(value == 0) {
            return PATH + "/";
        }
        return PATH + "/" + folderName + "/";
    }

    public String getFolderName() {
        return folderName;
    }

}
