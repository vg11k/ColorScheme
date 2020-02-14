package vg11k.com.colorscheme.schemeGenerator;

import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Julien on 06/02/2020.
 */

public class MixHeaderLineModel
        extends AbstractSchemeGeneratorLineModel
        implements HeaderLineModel.IHaveAHeaderModel,
        AbstractSchemeGeneratorLineModel.ICollapsableModel{

    private String m_headerContent;
    private KindOfProcess m_kindOfProcess = KindOfProcess.LAYER;
    private String m_kindOfProcessName = KindOfProcess.LAYER.getName();
    private ArrayList<AbstractSchemeGeneratorLineModel> m_mixedColorLayerLines;
    private HeaderLineModel m_header;

    private boolean m_isCollapsed;
    private int m_collapseIconState;
    private int m_expandIconState;

    protected MixHeaderLineModel(int index, HeaderLineModel header) {
        this(index, header,"mix");
    }

    protected MixHeaderLineModel(int index, HeaderLineModel header, String s) {
        super(index, SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER);
        m_headerContent = s;
        setDraggable(true);
        m_mixedColorLayerLines = new ArrayList<AbstractSchemeGeneratorLineModel>();
        setHeader(header);
        expand();

        m_defaultBackgroundColor = Color.parseColor("#ccf2ff");
        m_movedBackgroundColor = Color.parseColor("#ccd2ff");
        m_hoveredBackgroundColor = Color.parseColor("#cce6ff");

        m_currentBackgroundColor = m_defaultBackgroundColor;
    }

    public String getHeaderContent() { return m_headerContent; }

    public void setHeaderContent(String s) { m_headerContent = s; }

    public void setCollapsed(boolean b) { m_isCollapsed = b;}

    @Override
    public ArrayList<AbstractSchemeGeneratorLineModel> getChildrens() {
        return m_mixedColorLayerLines;
    }

    public void addContent(ArrayList<AbstractSchemeGeneratorLineModel> linesToAdd) {
        m_mixedColorLayerLines.addAll(linesToAdd);
    }

    public void addMixedColor(ColorLayerLineModel model) {
        m_mixedColorLayerLines.add(model);
    }

    public KindOfProcess getKindOfProcess() {
        return m_kindOfProcess;
    }

    public void setKindOfProcess(KindOfProcess k) {
        m_kindOfProcess = k;
        m_kindOfProcessName = k.getName();
    }

    public String getKindOfProcessName() {
        return m_kindOfProcessName;
    }

    public void setHeader(HeaderLineModel header) {
        m_header = header;
        m_header.addContent(this);
    }

    public HeaderLineModel getHeader() {
        return m_header;
    }

    @Override
    public void collapse() {
        m_collapseIconState = View.GONE;
        m_expandIconState = View.VISIBLE;
        m_isCollapsed = true;

        for(AbstractSchemeGeneratorLineModel children : m_mixedColorLayerLines) {

            ColorLayerLineModel colorModel = (ColorLayerLineModel) children;
            colorModel.setVisibility(View.GONE);
        }
    }

    @Override
    public void expand() {
        m_expandIconState = View.GONE;
        m_collapseIconState = View.VISIBLE;
        m_isCollapsed = false;

        for(AbstractSchemeGeneratorLineModel children : m_mixedColorLayerLines) {

            ColorLayerLineModel colorModel = (ColorLayerLineModel) children;
            colorModel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean isCollapsed() {
        return m_isCollapsed;
    }

    @Override
    public int getExpandIconState() {
        return m_expandIconState;
    }

    @Override
    public int getCollapsedIconState() {
        return m_collapseIconState;
    }


}
