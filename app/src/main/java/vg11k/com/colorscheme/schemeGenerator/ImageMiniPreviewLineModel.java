package vg11k.com.colorscheme.schemeGenerator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

/**
 * Created by Julien on 03/02/2020.
 */

public class ImageMiniPreviewLineModel extends AbstractSchemeGeneratorLineModel {

    private transient Bitmap mBitMap = null;

    private transient String m_pathname  = "none";

    protected ImageMiniPreviewLineModel(int index) {
        super(index, SchemeViewTypeLine.VIEW_TYPE_PREVIEW_IMAGE);
    }

    public void setBitMap(Bitmap bitmap) {
        mBitMap = bitmap;
    }

    public void setPathName(String s) {
        m_pathname = s;
    }

    public Bitmap getBitMap() {
        return mBitMap;
    }

    public ImageMiniPreviewLineModel(Parcel in) {
        super(in);

        m_pathname = in.readString();
        //mBitMap = in.readParcelable(Bitmap.class.getClassLoader());

        int size = in.readInt();
        byte[] byteArray = new byte[size];
        in.readByteArray(byteArray);
        mBitMap = BitmapFactory.decodeByteArray(byteArray, 0, size);

    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(m_pathname);

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        mBitMap.compress(Bitmap.CompressFormat.PNG, 100, bs);
        byte[] array = bs.toByteArray();

        dest.writeInt(array.length);
        dest.writeByteArray(array);
        //dest.writeParcelable(mBitMap, flags);
        //
    }

    public static final Parcelable.Creator<ImageMiniPreviewLineModel> CREATOR = new Parcelable.Creator<ImageMiniPreviewLineModel>() {
        public ImageMiniPreviewLineModel createFromParcel(Parcel in) {
            return new ImageMiniPreviewLineModel (in);
        }

        public ImageMiniPreviewLineModel [] newArray(int size) {
            return new ImageMiniPreviewLineModel[size];
        }
    };
}
