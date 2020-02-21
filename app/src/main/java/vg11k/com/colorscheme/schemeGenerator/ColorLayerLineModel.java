package vg11k.com.colorscheme.schemeGenerator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Julien on 03/02/2020.
 */

public class ColorLayerLineModel
        extends AbstractSchemeGeneratorLineModel
        implements HeaderLineModel.IHaveAHeaderModel {

    //by default it is a standard color layer


    @SerializedName("spaceVisibility")
    @Expose
    protected int m_spaceVisibility;

    @SerializedName("kindOfProcessName")
    @Expose
    protected String m_kindOfProcessName = KindOfProcess.LAYER.getName();

    @SerializedName("kindOfProcess")
    @Expose
    protected KindOfProcess m_kindOfProcess = KindOfProcess.LAYER;

    @SerializedName("kindOfProcessVisibility")
    @Expose
    protected int m_kindOfProcessVisibility;//a mixed color does not have any process

    @SerializedName("currentColorIndex")
    @Expose
    protected int m_currentColorIndex = -1;

    @SerializedName("colorName")
    @Expose
    protected String m_colorName = "none";

    @SerializedName("colorRGB")
    @Expose
    protected String m_colorRGB = "#000000";

    @SerializedName("currentProviderIndex")
    @Expose
    protected int m_currentProviderIndex = -1;

    @SerializedName("isMixed")
    @Expose
    protected boolean m_isMixed = false;

    //can't be null.
    protected transient HeaderLineModel m_header;

    //can be null
    protected transient MixHeaderLineModel m_mix;

    @SerializedName("mixedDefaultBackgroundColor")
    @Expose
    protected int m_mixedDefaultBackgroundColor;
    

    /*protected ColorLayerLineModel(int layerIndex) {
        super(layerIndex, SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW);
        setDraggable(true);
        m_spaceVisibility = View.GONE;
        m_kindOfProcessVisibility = View.VISIBLE;
    }*/


    protected ColorLayerLineModel(Resources resources, int layerIndex, int currentColorIndex, int currentProviderIndex, String colorRGB, String colorName, HeaderLineModel header) {
        super(layerIndex, SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW);
        m_currentColorIndex = currentColorIndex;
        m_currentProviderIndex = currentProviderIndex;
        m_colorRGB = colorRGB;
        m_colorName = colorName;

        setDraggable(true);
        m_spaceVisibility = View.GONE;
        m_kindOfProcessVisibility = View.VISIBLE;

        m_header = header;
        m_header.addContent(this);

        m_mixedDefaultBackgroundColor = Color.parseColor("#ccffe6");
        m_kindOfProcessName = resources.getString(m_kindOfProcess.getStringId());
    }

    public void setColorName(String s) {
        m_colorName = s;
    }

    public void setColorRGB(String s) {
        m_colorRGB = s;
    }

    public void setCurrentColorIndex(int i) {
        m_currentColorIndex = i;
    }

    public void setCurrentProviderIndex(int i) { m_currentProviderIndex = i;}

    public void setKindOfProcess(KindOfProcess k, Resources resources) {
        m_kindOfProcess = k;
        m_kindOfProcessName = resources.getString(k.getStringId());
    }

    public String getColorName() {
        return m_colorName;
    }

    public String getColorRGB() {
        return m_colorRGB;
    }

    public KindOfProcess getKindOfProcess() { return m_kindOfProcess;}

    public String getKindOfProcessName() {
        return m_kindOfProcessName;
    }

    public int getCurrentColorIndex() {
        return m_currentColorIndex;
    }

    public int getSpaceVisibility() {
        return m_spaceVisibility;
    }

    public int getKindOfProcessVisibility() {
        return m_kindOfProcessVisibility;
    }

    public int getCurrentProviderIndex() { return m_currentProviderIndex; }

    public boolean isMixed() {return m_isMixed;}


    public HeaderLineModel getHeader() {
        return m_header;
    }

    public MixHeaderLineModel getMix() {
        return m_mix;
    }

    public void setHeader(HeaderLineModel m_header) {
        this.m_header = m_header;
        m_header.addContent(this);
    }

    public void setMix(MixHeaderLineModel mix) {
        if(m_mix != null) {
            m_mix.getChildrens().remove(this);
        }

        m_mix = mix;
        if(m_mix != null) {
            m_kindOfProcessVisibility = View.GONE;
            m_spaceVisibility = View.VISIBLE;
            m_mix.addMixedColor(this);
            m_isMixed = true;
        }
        else {
            m_spaceVisibility = View.GONE;
            m_kindOfProcessVisibility = View.VISIBLE;
            m_isMixed = false;
        }
    }

    @Override
    public int getDefaultBackgroundColor() {
        if(m_mix != null) {
            return m_mixedDefaultBackgroundColor;
        }
        return m_defaultBackgroundColor;
    }

    /*
    @SerializedName("kindOfProcess")
    protected KindOfProcess m_kindOfProcess = KindOfProcess.LAYER;

    @SerializedName("kindOfProcessVisibility")
    protected int m_kindOfProcessVisibility;//a mixed color does not have any process

    @SerializedName("currentColorIndex")
    protected int m_currentColorIndex = -1;

    @SerializedName("colorName")
    protected String m_colorName = "none";

    @SerializedName("colorRGB")
    protected String m_colorRGB = "#000000";

    @SerializedName("currentProviderIndex")
    protected int m_currentProviderIndex = -1;


    //can't be null.
    protected HeaderLineModel m_header;

    //can be null
    protected MixHeaderLineModel m_mix;

    @SerializedName("mixedDefaultBackgroundColor")
    protected int m_mixedDefaultBackgroundColor;
     */

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeInt(m_spaceVisibility);
        dest.writeString(m_kindOfProcessName);
        dest.writeInt(m_kindOfProcess.getValue());
        dest.writeInt(m_kindOfProcessVisibility);
        dest.writeInt(m_currentColorIndex);
        dest.writeString(m_colorName);
        dest.writeString(m_colorRGB);
        dest.writeInt(m_currentProviderIndex);
        dest.writeInt(m_mixedDefaultBackgroundColor);
        dest.writeByte((byte)(m_isMixed ? 1 : 0));
    }

    protected ColorLayerLineModel(Parcel in) {
        super(in);
        m_spaceVisibility = in.readInt();
        m_kindOfProcessName = in.readString();
        m_kindOfProcess = KindOfProcess.valueOf(in.readInt());
        m_kindOfProcessVisibility = in.readInt();
        m_currentColorIndex = in.readInt();
        m_colorName = in.readString();
        m_colorRGB = in.readString();
        m_currentProviderIndex = in.readInt();
        m_mixedDefaultBackgroundColor = in.readInt();
        m_isMixed = in.readByte() != 0;
    }



    public static final Parcelable.Creator<ColorLayerLineModel> CREATOR = new Parcelable.Creator<ColorLayerLineModel>() {
        public ColorLayerLineModel createFromParcel(Parcel in) {
            return new ColorLayerLineModel (in);
        }

        public ColorLayerLineModel [] newArray(int size) {
            return new ColorLayerLineModel[size];
        }
    };
}
