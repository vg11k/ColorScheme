package vg11k.com.colorscheme.schemeGenerator;

import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Julien on 05/02/2020.
 */

public class HeaderLineModel
        extends AbstractSchemeGeneratorLineModel
        implements AbstractSchemeGeneratorLineModel.ICollapsableModel {

    private String m_headerContent ="none";
    private ArrayList<AbstractSchemeGeneratorLineModel> m_childrenLayerLines;

    private boolean m_isCollapsed;
    private int m_collapseIconState;
    private int m_expandIconState;

    protected HeaderLineModel(int index) {
        this(index, "none");
    }

    protected HeaderLineModel(int index, String s) {
        super(index, SchemeViewTypeLine.VIEW_TYPE_HEADER);
        m_childrenLayerLines = new ArrayList<AbstractSchemeGeneratorLineModel>();
        m_headerContent = s;
        setDraggable(true);
        expand();

        m_defaultBackgroundColor = Color.parseColor("#ccf2ff");
        m_movedBackgroundColor = Color.parseColor("#ccd2ff");
        m_hoveredBackgroundColor = Color.parseColor("#cce6ff");

        m_currentBackgroundColor = m_defaultBackgroundColor;

    }

    public String getHeaderContent() { return m_headerContent; }

    public void setHeaderContent(String s) { m_headerContent = s; }


    @Override
    public void setDraggable(boolean b) {
        super.setDraggable(b);
    }

    @Override
    public ArrayList<AbstractSchemeGeneratorLineModel> getChildrens() {
        return m_childrenLayerLines;
    }

    public void addContent(AbstractSchemeGeneratorLineModel model) {
        m_childrenLayerLines.add(model);
    }

    public interface IHaveAHeaderModel {
        HeaderLineModel getHeader();
        void setHeader(HeaderLineModel header);
    }

    @Override
    public void collapse() {
        m_collapseIconState = View.GONE;
        m_expandIconState = View.VISIBLE;
        m_isCollapsed = true;

        for(AbstractSchemeGeneratorLineModel children : m_childrenLayerLines) {
            children.setVisibility(View.GONE);
        }
    }

    @Override
    public void expand() {
        m_expandIconState = View.GONE;
        m_collapseIconState = View.VISIBLE;
        m_isCollapsed = false;

        for(AbstractSchemeGeneratorLineModel children : m_childrenLayerLines) {

            if(children.getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
                ColorLayerLineModel colorModel = (ColorLayerLineModel) children;
                if(!(colorModel.getMix() != null && colorModel.getMix().isCollapsed())) {
                    colorModel.setVisibility(View.VISIBLE);
                }
                else {
                    colorModel.setVisibility(View.GONE);
                }
            }
            else {
                children.setVisibility(View.VISIBLE);
            }
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
