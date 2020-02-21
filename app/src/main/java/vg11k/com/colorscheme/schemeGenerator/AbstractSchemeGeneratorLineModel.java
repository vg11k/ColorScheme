package vg11k.com.colorscheme.schemeGenerator;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Julien on 03/02/2020.
 */

public abstract class AbstractSchemeGeneratorLineModel implements Parcelable {

    @SerializedName("lineIndex")
    @Expose
    private int m_lineIndex;

    @SerializedName("viewType")
    @Expose
    private SchemeViewTypeLine m_viewType;

    @SerializedName("canBeDragged")
    @Expose
    protected boolean m_canBeDragged = false;

    @SerializedName("dragImageVisibility")
    @Expose
    protected int m_dragImageVisibility;

    @SerializedName("visibility")
    @Expose
    protected int m_visibility;

    @SerializedName("height")
    @Expose
    protected int m_height;

    @SerializedName("defaultBackgroundColor")
    @Expose
    protected int m_defaultBackgroundColor;

    @SerializedName("movedBackgroundColor")
    @Expose
    protected int m_movedBackgroundColor;

    @SerializedName("hoveredBackgroundColor")
    @Expose
    protected int m_hoveredBackgroundColor;

    @SerializedName("currentBackgroundColor")
    @Expose
    protected int m_currentBackgroundColor;

    private transient AbstractViewHolder  m_holder;

    public AbstractSchemeGeneratorLineModel(int lineIndex, SchemeViewTypeLine viewType, boolean canBeDragged, int dragImageVisibility,
                                            int visibility, int height, int defaultBackgroundColor, int movedBackgroundColor,
                                            int hoveredBackgroundColor, int currentBackgroundColor) {
        m_lineIndex = lineIndex;
        m_viewType = viewType;
        m_canBeDragged = canBeDragged;
        m_dragImageVisibility = dragImageVisibility;
        m_visibility = visibility;
        m_height = height;
        m_defaultBackgroundColor = defaultBackgroundColor;
        m_movedBackgroundColor = movedBackgroundColor;
        m_hoveredBackgroundColor = hoveredBackgroundColor;
        m_currentBackgroundColor = currentBackgroundColor;
    }

    protected AbstractSchemeGeneratorLineModel(int index, SchemeViewTypeLine viewType) {
        m_lineIndex = index;
        m_viewType = viewType;
        m_dragImageVisibility = View.GONE;
        m_height = ViewGroup.LayoutParams.WRAP_CONTENT;

        m_defaultBackgroundColor = Color.WHITE;
        m_movedBackgroundColor = Color.GRAY;
        m_hoveredBackgroundColor = Color.LTGRAY;
        m_currentBackgroundColor = m_defaultBackgroundColor;
    }

    public int getLineIndex() {
        return m_lineIndex;
    }

    public SchemeViewTypeLine getViewType() {
        return m_viewType;
    }

    public void setLineIndex(int m_lineIndex) {
        this.m_lineIndex = m_lineIndex;
    }

    public void setViewType(SchemeViewTypeLine m_viewType) {
        this.m_viewType = m_viewType;
    }

    public boolean isDraggable() {
        return m_canBeDragged;
    }

    public void setDraggable(boolean b) {
        m_canBeDragged = b;
        if(b) {
            m_dragImageVisibility = View.VISIBLE;
        }
        else {
            m_dragImageVisibility = View.INVISIBLE;
        }
    }

    public int getDragImageVisibility() {
        return m_dragImageVisibility;
    }

    public int getVisibility() { return m_visibility;}

    public void setVisibility(int i) {
        m_visibility = i;
        if(m_visibility == View.GONE) {
            setHeight(0);
        }
        else {
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public interface ICollapsableModel {
        void collapse();
        void expand();
        boolean isCollapsed();
        int getExpandIconState();
        int getCollapsedIconState();
        ArrayList<AbstractSchemeGeneratorLineModel> getChildrens();
    }

    public AbstractViewHolder getHolder() {
        return m_holder;
    }

    public void setHolder(AbstractViewHolder holder) {
        m_holder = holder;
    }

    public void setHeight(int i) {
        m_height = i;
    }

    public int getHeight() {
        return m_height;
    }

    public int getDefaultBackgroundColor() {
        return m_defaultBackgroundColor;
    }

    public int getMovedBackgroundColor() {
        return m_movedBackgroundColor;
    }

    public int getHoveredBackgroundColor() {
        return m_hoveredBackgroundColor;
    }

    public int getCurrentBackgroundColor() {
        return m_currentBackgroundColor;
    }

    public void setCurrentBackgroundColor(int colori) {
        m_currentBackgroundColor = colori;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(m_viewType.getValue());
        dest.writeByte((byte) (m_canBeDragged ? 1 : 0));
        dest.writeInt(m_dragImageVisibility);
        dest.writeInt(m_visibility);
        dest.writeInt(m_height);
        dest.writeInt(m_defaultBackgroundColor);
        dest.writeInt(m_movedBackgroundColor);
        dest.writeInt(m_hoveredBackgroundColor);
        dest.writeInt(m_currentBackgroundColor);
    }

    protected AbstractSchemeGeneratorLineModel(Parcel in) {

        m_viewType = SchemeViewTypeLine.valueOf(in.readInt());
        m_canBeDragged = in.readByte() != 0;
        m_dragImageVisibility = in.readInt();
        m_visibility = in.readInt();
        m_height = in.readInt();
        m_defaultBackgroundColor = in.readInt();
        m_movedBackgroundColor = in.readInt();
        m_hoveredBackgroundColor = in.readInt();
        m_currentBackgroundColor = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /*public static final Creator<AbstractSchemeGeneratorLineModel> CREATOR = new Creator<AbstractSchemeGeneratorLineModel>() {
        @Override
        public AbstractSchemeGeneratorLineModel createFromParcel(Parcel in) {
            return AbstractSchemeGeneratorLineModel.getConcreteClass(in);//new AbstractSchemeGeneratorLineModel(in);
        }

        @Override
        public AbstractSchemeGeneratorLineModel[] newArray(int size) {
            return new AbstractSchemeGeneratorLineModel[size];
        }
    };*/


}
