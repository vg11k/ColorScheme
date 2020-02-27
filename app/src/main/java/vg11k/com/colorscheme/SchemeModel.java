package vg11k.com.colorscheme;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vg11k.com.colorscheme.schemeGenerator.AbstractSchemeGeneratorLineModel;
import vg11k.com.colorscheme.schemeGenerator.ColorLayerLineModel;
import vg11k.com.colorscheme.schemeGenerator.HeaderLineModel;
import vg11k.com.colorscheme.schemeGenerator.MixHeaderLineModel;
import vg11k.com.colorscheme.schemeGenerator.SchemeViewTypeLine;

/**
 * Created by Julien on 17/02/2020.
 */

public class SchemeModel implements Parcelable {

    @SerializedName("schemeName")
    private String m_schemeName;

    @SerializedName("models")
    private ArrayList<AbstractSchemeGeneratorLineModel> m_models;

    public static transient String m_ID = "Scheme_Model";

    public SchemeModel(String name, ArrayList<AbstractSchemeGeneratorLineModel> models) {
        m_schemeName = name;
        m_models = models;
    }

    public String getName() {
        return m_schemeName;
    }

    public void setName(String s) { m_schemeName = s; }

    protected SchemeModel(Parcel in) {
        m_schemeName = in.readString();
        m_models = new ArrayList<AbstractSchemeGeneratorLineModel>();
        in.readList(m_models, AbstractSchemeGeneratorLineModel.class.getClassLoader());

        HeaderLineModel lastHeader = null;
        MixHeaderLineModel lastMix = null;

        //rebuild all links of the header/mix/tree
        for(AbstractSchemeGeneratorLineModel line : m_models) {
            if(line.getViewType() == SchemeViewTypeLine.VIEW_TYPE_HEADER) {
                lastHeader = (HeaderLineModel) line;
                lastMix = null;
            }
            else if(line.getViewType() == SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER) {
                lastMix = (MixHeaderLineModel) line;
                ((MixHeaderLineModel) line).setHeader(lastHeader);
            }
            else if(line.getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
                ColorLayerLineModel colorLine = (ColorLayerLineModel) line;
                colorLine.setHeader(lastHeader);
                if(colorLine.isMixed()) {
                    colorLine.setMix(lastMix);
                }
                else if(lastMix != null) {
                    lastMix = null;
                }
            }

        }


    }

    public static final Creator<SchemeModel> CREATOR = new Creator<SchemeModel>() {
        @Override
        public SchemeModel createFromParcel(Parcel in) {
            return new SchemeModel(in);
        }

        @Override
        public SchemeModel[] newArray(int size) {
            return new SchemeModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(m_schemeName);
        parcel.writeList(m_models);
    }

    public ArrayList<AbstractSchemeGeneratorLineModel> getLines() {
        return m_models;
    }
}
