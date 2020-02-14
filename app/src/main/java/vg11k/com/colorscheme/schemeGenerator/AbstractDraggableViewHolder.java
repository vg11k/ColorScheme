package vg11k.com.colorscheme.schemeGenerator;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import vg11k.com.colorscheme.R;

/**
 * Created by Julien on 05/02/2020.
 */

public abstract class AbstractDraggableViewHolder extends AbstractViewHolder {

    public final TextView mContentView;
    public final ImageView m_dragImage;

    public AbstractDraggableViewHolder(@NonNull View itemView) {
        super(itemView);

        mContentView = (TextView) itemView.findViewById(R.id.content);
        m_dragImage = (ImageView) itemView.findViewById(R.id.dragImageView);
    }

    public ImageView getDragImage() {
        return m_dragImage;
    }



    public interface ICollapsableViewHolder {
        ImageView getCollapseIcon();
        ImageView getExpandIcon();
        AbstractSchemeGeneratorLineModel.ICollapsableModel getCollapsableModel();
    }

}

