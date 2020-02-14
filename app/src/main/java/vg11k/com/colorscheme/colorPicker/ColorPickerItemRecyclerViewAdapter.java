package vg11k.com.colorscheme.colorPicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vg11k.com.colorscheme.ColorViewHolder;
import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.DataProvider;

import vg11k.com.colorscheme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ColorPickerLine} and makes a call to the
 * specified {@link OnColorPickerItemFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ColorPickerItemRecyclerViewAdapter extends RecyclerView.Adapter<ColorViewHolder> {

    private final List<ColorPickerLine> mValues;
    private final OnColorPickerItemFragmentInteractionListener mListener;
    private final DataProvider mdataProvider;
    private ViewGroup mParent;
    private int mSelectedProvider;
    private ArrayList<ColorViewHolder> m_holders;


    public ColorPickerItemRecyclerViewAdapter(List<ColorPickerLine> items, OnColorPickerItemFragmentInteractionListener listener, DataProvider dataProvider) {
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
        /*for(int i = 0; i < m_holders.size(); i++) {
            ColorViewHolder holder = m_holders.get(i);

            if(holder.mItem.getId() != i) {
                m_holders.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, 1);
                i--;
                System.out.println("remove " + i);
            }
            else {
                if (holder.mItem.getColorName(mSelectedProvider).isEmpty()) {
                    holder.mItem.setVisible(View.GONE);
                    //holder.setVisibility(View.GONE);
                } else {
                    //why the fuck the activity does not update itself when the model change ?
                    holder.mItem.setCurrentName(mSelectedProvider);
                    //holder.mContentView.setText(holder.mItem.getCurrentName());
                    //holder.setVisibility(View.VISIBLE);
                    //holder.mContentView.setText(mdataProvider.getLine(i+1)[mSelectedProvider + 1]);
                }
            }
        }*/

        for(ColorPickerLine line : mValues) {
            if (line.getColorName(mSelectedProvider).isEmpty()) {
                line.setVisible(View.GONE);
            }
            else {
                line.setCurrentName(mSelectedProvider);
            }
        }

        for(int i = 0; i < m_holders.size(); i++) {
            ColorViewHolder holder = m_holders.get(i);

            if(holder.mItem.getId() != i) {
                m_holders.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, 1);
                i--;
                System.out.println("remove " + i);
            }
            else {

                holder.mContentView.setText(holder.mItem.getCurrentName());
                holder.setVisibility(holder.mItem.getVisible());

            }
        }
    }

    @Override
    public void onBindViewHolder(final ColorViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
        holder.mContentView.setText(mValues.get(position).getColorName());
        holder.mColorCircle.setRGBColor(mValues.get(position).getcolorRGB());
        holder.setVisibility(mValues.get(position).getVisible());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



}
