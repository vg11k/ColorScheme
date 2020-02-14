package vg11k.com.colorscheme.schemeGenerator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Julien on 04/02/2020.
 */

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

    protected View m_rowView;
    private AbstractSchemeGeneratorLineModel m_model = null;

    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
        m_rowView = itemView;
    }

    public void setModel(AbstractSchemeGeneratorLineModel model) {

        m_model = model;
        m_model.setHolder(this);
    }

    public AbstractSchemeGeneratorLineModel getModel() {
        return m_model;
    }

    public void setVisibility(int i) {
        m_rowView.setVisibility(i);//EXCEPTION
    }

    public View getRowView() {return m_rowView;}


}