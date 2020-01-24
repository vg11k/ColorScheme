package vg11k.com.colorscheme;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import vg11k.com.colorscheme.ColorCircle;
import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.R;

/**
 * Created by Julien on 22/01/2020.
 */

public class ColorViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    public final TextView mIdView;
    public final TextView mContentView;
    public final ColorCircle mColorCircle;

    public ColorPickerLine mItem;

    public ColorViewHolder(View view, DataProvider provider) {
        super(view);

        mView = view;
        mIdView = (TextView) view.findViewById(R.id.id);
        mContentView = (TextView) view.findViewById(R.id.content);
        mColorCircle = (ColorCircle) view.findViewById(R.id.circle);

    }

    public void setVisibility(int v){
        mView.setVisibility(v);
        mColorCircle.setVisibility(v);
        mContentView.setVisibility(v);
        mIdView.setVisibility(v);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }
}