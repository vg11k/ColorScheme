package vg11k.com.colorscheme.schemeGenerator;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Julien on 03/02/2020.
 */

public abstract class AbstractSchemeGeneratorLineModel {

    private int m_lineIndex;
    private SchemeViewTypeLine m_viewType;
    protected boolean m_canBeDragged = false;
    protected int m_dragImageVisibility;
    protected int m_visibility;
    protected int m_height;

    protected int m_defaultBackgroundColor;
    protected int m_movedBackgroundColor;
    protected int m_hoveredBackgroundColor;

    protected int m_currentBackgroundColor;

    private AbstractViewHolder m_holder;

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




}
