package vg11k.com.colorscheme.schemeGenerator;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Julien on 05/02/2020.
 */

public class HeaderLineModel
        extends AbstractSchemeGeneratorLineModel
        implements AbstractSchemeGeneratorLineModel.ICollapsableModel {

    @SerializedName("headerContent")
    @Expose
    private String m_headerContent ="none";

    private transient ArrayList<AbstractSchemeGeneratorLineModel> m_childrenLayerLines;

    @SerializedName("isCollapsed")
    @Expose
    private boolean m_isCollapsed;

    @SerializedName("collapseIconState")
    @Expose
    private int m_collapseIconState;

    @SerializedName("expandIconState")
    @Expose
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

    /*    private String m_headerContent ="none";
    private ArrayList<AbstractSchemeGeneratorLineModel> m_childrenLayerLines;

    private boolean m_isCollapsed;
    private int m_collapseIconState;
    private int m_expandIconState;*/


    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(m_headerContent);
        dest.writeByte((byte) (m_isCollapsed ? 1 : 0));
        dest.writeInt(m_collapseIconState);
        dest.writeInt(m_expandIconState);
    }

    protected HeaderLineModel(Parcel in) {
        super(in);
        m_headerContent = in.readString();
        m_isCollapsed = in.readByte() != 0;
        m_collapseIconState = in.readInt();
        m_expandIconState = in.readInt();

        m_childrenLayerLines = new ArrayList<AbstractSchemeGeneratorLineModel>();
    }



    public static final Parcelable.Creator<HeaderLineModel> CREATOR = new Parcelable.Creator<HeaderLineModel>() {
        public HeaderLineModel createFromParcel(Parcel in) {
            return new HeaderLineModel (in);
        }

        public HeaderLineModel [] newArray(int size) {
            return new HeaderLineModel[size];
        }
    };

}
