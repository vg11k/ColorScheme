package vg11k.com.colorscheme.schemeGenerator;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import vg11k.com.colorscheme.R;


/**
 * Created by Julien on 05/02/2020.
 */

public class HeaderLineViewHolder
        extends AbstractDraggableViewHolder
        implements AbstractDraggableViewHolder.ICollapsableViewHolder {


    public final ImageView m_collapseIcon;
    public final ImageView m_expandIcon;

    public HeaderLineViewHolder(@NonNull View itemView) {
        super(itemView);

        m_collapseIcon = itemView.findViewById(R.id.collapseImageView);
        m_expandIcon = itemView.findViewById(R.id.expandImageView);
    }

    @Override
    public ImageView getCollapseIcon() {
        return m_collapseIcon;
    }

    @Override
    public ImageView getExpandIcon() {
        return m_expandIcon;
    }

    @Override
    public AbstractSchemeGeneratorLineModel.ICollapsableModel getCollapsableModel() {
        return (HeaderLineModel) getModel();
    }
}
