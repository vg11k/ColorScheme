package vg11k.com.colorscheme.schemeGenerator;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import vg11k.com.colorscheme.ColorCircle;
import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.R;

/**
 * Created by Julien on 03/02/2020.
 */

public class ColorLayerLineViewHolder
        extends AbstractDraggableViewHolder
        implements KindOfProcess.IKindOfProcessedHolder {


    //public final View mView;
    public final Space mSpace;
    //public final TextView mContentView;
    public final TextView m_kindOfProcess;
    public final ColorCircle mColorCircle;
    public final ImageView mMixImage;
    //public final ImageView m_dragImage;


    public ColorPickerLine mItem;

    public ColorLayerLineViewHolder(@NonNull View itemView) {
        super(itemView);

        //mView = itemView;
        mSpace = (Space) itemView.findViewById(R.id.mixSpace);
        //mContentView = (TextView) itemView.findViewById(R.id.content);
        m_kindOfProcess = (TextView) itemView.findViewById(R.id.kindOfProcess);
        mColorCircle = (ColorCircle) itemView.findViewById(R.id.circle);
        mMixImage = (ImageView) itemView.findViewById(R.id.mixImage);
        //m_dragImage = (ImageView) itemView.findViewById(R.id.dragImageView);
    }

    @Override
    public View getKindOfProcessView() {
        return m_kindOfProcess;
    }

    @Override
    public void setKindOfProcessOnModel(KindOfProcess k) {
        ((ColorLayerLineModel)getModel()).setKindOfProcess(k);
    }

    /*public ImageView getDragImage() {
        return m_dragImage;
    }*/

}
