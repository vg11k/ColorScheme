package vg11k.com.colorscheme.colorConverterTool;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.ColorViewHolder;
import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.R;
import vg11k.com.colorscheme.colorPicker.ColorPickerItemFragment;

/**
 * Created by Julien on 22/01/2020.
 */

public class ColorConverterToolActivityAdapter extends RecyclerView.Adapter<ColorViewHolder> {


    private final List<ColorPickerLine> mValues;
    private final ColorConverterToolActivity.OnListActivityInteractionListener mListener;
    private final DataProvider mdataProvider;
    private ViewGroup mParent;
    private int mSelectedProvider;
    private ArrayList<ColorViewHolder> m_holders;
    private Context m_context;
    private View m_view;


    public ColorConverterToolActivityAdapter(List<ColorPickerLine> items,
                                             ColorConverterToolActivity.OnListActivityInteractionListener listener,
                                             DataProvider dataProvider,
                                             Context context,
                                             View view) {
        mValues = items;
        mListener = listener;
        mdataProvider = dataProvider;
        mSelectedProvider = 0;
        m_holders = new ArrayList<ColorViewHolder>();
        m_context = context;
        m_view = view;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(m_context).inflate(R.layout.fragment_colorpickeritem, parent, false);
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

            /*if(holder.mItem.getId() != i) {
                m_holders.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, 1);
                i--;
                System.out.println("remove " + i);
            }
            else {*/


                if (holder.mItem.getColorName(mSelectedProvider).isEmpty()) {
                    //holder.setVisibility(View.GONE);
                    holder.mItem.setVisible(View.GONE);
                } else {
                    //holder.setVisibility(View.VISIBLE);
                    //holder.mContentView.setText(holder.mItem.getColorName(mSelectedProvider));
                    holder.mItem.setCurrentName(mSelectedProvider);
                }
            //}
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ColorViewHolder holder, int position) {



        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
       // holder.mContentView.setText(mValues.get(position).getColorName());
        holder.mContentView.setText(mValues.get(position).getCurrentName());
        holder.mColorCircle.setRGBColor(mValues.get(position).getcolorRGB());

        holder.setVisibility(mValues.get(position).getVisible());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListActivityInteraction(holder.mItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}