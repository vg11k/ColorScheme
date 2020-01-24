package vg11k.com.colorscheme.colorPicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vg11k.com.colorscheme.ColorCircle;
import vg11k.com.colorscheme.ColorViewHolder;
import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.GenericOnListInteractionListener;
import vg11k.com.colorscheme.colorConverterTool.ColorConverterToolActivity;
import vg11k.com.colorscheme.colorPicker.ColorPickerItemFragment.OnListFragmentInteractionListener;

import vg11k.com.colorscheme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ColorPickerLine} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ColorPickerItemRecyclerViewAdapter extends RecyclerView.Adapter<ColorViewHolder> {

    private final List<ColorPickerLine> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final DataProvider mdataProvider;
    private ViewGroup mParent;
    private int mSelectedProvider;
    private ArrayList<ColorViewHolder> m_holders;


    public ColorPickerItemRecyclerViewAdapter(List<ColorPickerLine> items, OnListFragmentInteractionListener listener, DataProvider dataProvider) {
        mValues = items;
        mListener = listener;
        mdataProvider = dataProvider;
        mSelectedProvider = 0;
        m_holders = new ArrayList<ColorViewHolder>();
    }





    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_colorpickeritem, parent, false);
        mParent = parent;

        ColorViewHolder holder = new ColorViewHolder(view, mdataProvider);
        m_holders.add(holder);
        System.out.println("Holder size : " + m_holders.size());
        return holder;
    }

    public void setSelectedProvider(int selectedIndex) {
        mSelectedProvider = selectedIndex;
        updateAllColorPickerLineView();
    }

    public void updateAllColorPickerLineView() {
        for(int i = 0; i < m_holders.size(); i++) {
            ColorViewHolder holder = m_holders.get(i);
            if(holder.mItem.getColorName(mSelectedProvider).isEmpty()) {
                holder.setVisibility(View.GONE);
            }
            else {
                holder.setVisibility(View.VISIBLE);
                holder.mContentView.setText(holder.mItem.getColorName(mSelectedProvider));
                //holder.mContentView.setText(mdataProvider.getLine(i+1)[mSelectedProvider + 1]);
            }
        }
    }

    @Override
    public void onBindViewHolder(final ColorViewHolder holder, int position) {

        ColorPickerLine itemToDisplay = holder.mItem;

        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
        holder.mContentView.setText(mValues.get(position).getColorName());
        holder.mColorCircle.setRGBColor(mValues.get(position).getcolorRGB());

        if(holder.mItem.getColorName(mSelectedProvider).isEmpty()) {
            holder.setVisibility(View.GONE);
        }
        else {
            holder.setVisibility(View.VISIBLE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



}
