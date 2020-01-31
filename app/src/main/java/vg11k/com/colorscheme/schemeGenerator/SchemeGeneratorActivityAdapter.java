package vg11k.com.colorscheme.schemeGenerator;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.R;

/**
 * Created by Julien on 29/01/2020.
 */

//drag & drop from https://www.journaldev.com/23208/android-recyclerview-drag-and-drop

// useful to look at :
    //http://kyogs.blogspot.com/2014/06/the-use-of-getviewtypecount-and.html
    //https://blog.stylingandroid.com/material-part-6/
    //https://therubberduckdev.wordpress.com/2017/10/24/android-recyclerview-drag-and-drop-and-swipe-to-dismiss/


class SchemeGeneratorActivityAdapter extends RecyclerView.Adapter<SchemeGeneratorActivityAdapter.DemoViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract  {

    private final List<String> mValues;
    private Context m_context;
    private View m_view;
    private final DataProvider mdataProvider;
    private ViewGroup mParent;

    private int holderCounter = 0;

    StartDragListener  mStartDragListener;

    private List<DemoViewHolder> m_holders;
    private DemoViewHolder m_hoveredHolder = null;

    public SchemeGeneratorActivityAdapter(DataProvider dataProvider, Context context, View view, final List<String> values, StartDragListener dragListener) {

        mdataProvider = dataProvider;
        m_context = context;
        m_view = view;
        mValues = values;

        m_holders = new ArrayList<DemoViewHolder>();

        mStartDragListener = dragListener;
    }

    @NonNull
    @Override
    public DemoViewHolder onCreateViewHolder(@NonNull ViewGroup parentViewGroup, int i) {
        View view = LayoutInflater.from(m_context).inflate(R.layout.item_list_content, parentViewGroup, false);
        mParent = parentViewGroup;

        DemoViewHolder holder =  new DemoViewHolder(view);
        m_holders.add(holder);
        return holder;
    }

    public Context getContext() {
        return m_context;
    }

    @Override
    public void onBindViewHolder(@NonNull final DemoViewHolder holder, int position) {

        holder.mIdView.setText("DRAGME!!" + String.valueOf(position));
        holder.mContentView.setText(mValues.get(position));

        holder.mIdView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    mStartDragListener.requestDrag(holder);
                }
                return false;
            }

        } );

    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {

        //the move has been completed : it's not a drop-node so we cancel it.
        if(m_hoveredHolder != null) {
            m_hoveredHolder.rowView.setBackgroundColor(Color.WHITE);
            m_hoveredHolder = null;
        }

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mValues, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mValues, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(DemoViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(DemoViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

        if(m_hoveredHolder != null) {
            m_hoveredHolder.rowView.setBackgroundColor(Color.WHITE);
            Snackbar.make(m_view, m_hoveredHolder.mContentView.getText() + " devient folder de " + myViewHolder.mContentView.getText(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onDropFocus(@NonNull RecyclerView.ViewHolder selectedView, @NonNull List<RecyclerView.ViewHolder> dropTargets, int curx, int cury) {

        if(m_hoveredHolder != null) {
            m_hoveredHolder.rowView.setBackgroundColor(Color.WHITE);
        }

        m_hoveredHolder = ((DemoViewHolder) dropTargets.get(0));
        m_hoveredHolder.rowView.setBackgroundColor(Color.LTGRAY);

        //assert only one will be fine
        /*for(RecyclerView.ViewHolder viewHolder : dropTargets) {
            DemoViewHolder viewTarget = (DemoViewHolder) viewHolder;
        }*/
    }

    @Override
    public int getItemViewType(int position) {
        return 0; //TODO default value to replace !
    }

    //@Override //for arrayAdapter à priori :thinking:
    public int getViewTypeCount() {
        return 1; //TODO default value to replace !
    }


    class DemoViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        View rowView;

        DemoViewHolder(View view) {
            super(view);
            rowView = view;
            mIdView = (TextView) view.findViewById(R.id.id_text);


            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
