package vg11k.com.colorscheme.schemeGenerator;

import android.graphics.Color;
import android.view.View;

/**
 * Created by Julien on 03/02/2020.
 */

public class ColorLayerLineModel
        extends AbstractSchemeGeneratorLineModel
        implements HeaderLineModel.IHaveAHeaderModel {

    //by default it is a standard color layer


    protected int m_spaceVisibility;


    protected String m_kindOfProcessName = KindOfProcess.LAYER.getName();
    protected KindOfProcess m_kindOfProcess = KindOfProcess.LAYER;
    protected int m_kindOfProcessVisibility;//a mixed color does not have any process

    protected int m_currentColorIndex = -1;
    protected String m_colorName = "none";
    protected String m_colorRGB = "#000000";
    protected int m_currentProviderIndex = -1;


    //can't be null.
    protected HeaderLineModel m_header;

    //can be null
    protected MixHeaderLineModel m_mix;

    protected int m_mixedDefaultBackgroundColor;
    

    /*protected ColorLayerLineModel(int layerIndex) {
        super(layerIndex, SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW);
        setDraggable(true);
        m_spaceVisibility = View.GONE;
        m_kindOfProcessVisibility = View.VISIBLE;
    }*/


    protected ColorLayerLineModel(int layerIndex, int currentColorIndex, int currentProviderIndex, String colorRGB, String colorName, HeaderLineModel header) {
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

    public void setKindOfProcess(KindOfProcess k) {
        m_kindOfProcess = k;
        m_kindOfProcessName = k.getName();
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
        }
        else {
            m_spaceVisibility = View.GONE;
            m_kindOfProcessVisibility = View.VISIBLE;
        }
    }

    @Override
    public int getDefaultBackgroundColor() {
        if(m_mix != null) {
            return m_mixedDefaultBackgroundColor;
        }
        return m_defaultBackgroundColor;
    }
}
