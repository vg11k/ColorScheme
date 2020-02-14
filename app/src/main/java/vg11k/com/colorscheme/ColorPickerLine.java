package vg11k.com.colorscheme;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by Julien on 20/01/2020.
 */

public class ColorPickerLine {

    private final String m_colorRGB;
    private final ArrayList<String> m_names;
    private String m_currentName;
    private int m_providerId;
    private final int m_id;
    private boolean m_selected;
    private int m_visible;

    public ColorPickerLine(int id, String[] rawData) {

        m_id = id;
        m_colorRGB = "#" + rawData[0];

        m_names = new ArrayList<String>();
        for(int i = 1; i < rawData.length; i++)
            m_names.add(rawData[i]);

        m_selected = false;
        m_currentName = m_names.get(0);
        if(m_currentName.isEmpty()) {
            m_visible = View.GONE;
        }
        else {
            m_visible = View.VISIBLE;
        }

        m_providerId = 0;
    }

    public String getCurrentName() {return m_currentName;}

    public void setCurrentName(int index) {
        m_currentName = m_names.get(index);
        if(m_currentName.isEmpty()) {
            m_visible = View.GONE;
        }
        else {
            m_visible = View.VISIBLE;
            m_providerId = index;
        }
    }

    public int getProviderIndex() { return m_providerId;}

    public int getId(){ return m_id;}

    public String getcolorRGB() { return m_colorRGB; }

    public String getColorName() { return getColorName(0);}

    public String getColorName(int i) { return m_names.get(i);}

    public boolean isSelected() {return m_selected;}

    public void setSelected(boolean b) { m_selected = b;}

    public int getVisible() { return m_visible;}

    public void setVisible(int v) {m_visible = v;}

}
