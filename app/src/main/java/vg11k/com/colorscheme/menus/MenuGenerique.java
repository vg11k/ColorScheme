package vg11k.com.colorscheme.menus;

/**
 * Created by Julien on 01/10/2019.
 */

public class MenuGenerique {

    private final String m_id;
    private final String m_content;

    public static final String GENERIC_TITLE = "Menu vide";

    public MenuGenerique(String id, String content) {
        m_id = id;
        m_content = content;
    }

    public String toString() {return m_content;}

    public String getId(){
        return m_id;
    }

    public String getContent() {
        return m_content;
    }

}
