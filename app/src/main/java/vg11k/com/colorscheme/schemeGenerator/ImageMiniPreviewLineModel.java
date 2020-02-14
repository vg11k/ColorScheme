package vg11k.com.colorscheme.schemeGenerator;

import android.graphics.Bitmap;

/**
 * Created by Julien on 03/02/2020.
 */

public class ImageMiniPreviewLineModel extends AbstractSchemeGeneratorLineModel {

    private Bitmap mBitMap = null;

    protected ImageMiniPreviewLineModel(int index) {
        super(index, SchemeViewTypeLine.VIEW_TYPE_PREVIEW_IMAGE);
    }

    public void setBitMap(Bitmap bitmap) {
        mBitMap = bitmap;
    }

    public Bitmap getBitMap() {
        return mBitMap;
    }
}
