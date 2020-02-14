package vg11k.com.colorscheme.schemeGenerator;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import vg11k.com.colorscheme.R;

/**
 * Created by Julien on 06/02/2020.
 */

public class MixHeaderLineViewHolder
        extends AbstractDraggableViewHolder
        implements KindOfProcess.IKindOfProcessedHolder,
        AbstractDraggableViewHolder.ICollapsableViewHolder {

    public final TextView m_kindOfProcess;

    public final ImageView m_collapseIcon;
    public final ImageView m_expandIcon;

    public MixHeaderLineViewHolder(@NonNull View itemView) {
        super(itemView);
        m_kindOfProcess = (TextView) itemView.findViewById(R.id.kindOfProcess);
        m_collapseIcon = itemView.findViewById(R.id.collapseImageView);
        m_expandIcon = itemView.findViewById(R.id.expandImageView);
    }

    public TextView getKindOfProcessView() {
        return m_kindOfProcess;
    }

    @Override
    public void setKindOfProcessOnModel(KindOfProcess k) {
        ((MixHeaderLineModel)getModel()).setKindOfProcess(k);
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
        return (MixHeaderLineModel) getModel();
    }
}
