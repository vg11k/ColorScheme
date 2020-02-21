package vg11k.com.colorscheme.schemeGenerator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Julien on 03/02/2020.
 */

public class ButtonAddLineModel  extends AbstractSchemeGeneratorLineModel{

    protected ButtonAddLineModel(int index) {
        super(index, SchemeViewTypeLine.VIEW_TYPE_ADD_BUTTON);
    }

    protected ButtonAddLineModel(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<ButtonAddLineModel> CREATOR = new Parcelable.Creator<ButtonAddLineModel>() {
        public ButtonAddLineModel createFromParcel(Parcel in) {
            return new ButtonAddLineModel (in);
        }

        public ButtonAddLineModel [] newArray(int size) {
            return new ButtonAddLineModel[size];
        }
    };
}
