package vg11k.com.colorscheme.schemeGenerator;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

import vg11k.com.colorscheme.R;
import vg11k.com.colorscheme.colorConverterTool.ColorConverterToolActivity;

/**
 * Created by Julien on 29/01/2020.
 */

class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;


    public ItemMoveCallback(ItemTouchHelperContract adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;//return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //TODO later to remove a content
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof SchemeGeneratorActivityAdapter.DemoViewHolder) {
                SchemeGeneratorActivityAdapter.DemoViewHolder myViewHolder=
                        (SchemeGeneratorActivityAdapter.DemoViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }

        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);



        if (viewHolder instanceof SchemeGeneratorActivityAdapter.DemoViewHolder) {




            SchemeGeneratorActivityAdapter.DemoViewHolder myViewHolder=
                    (SchemeGeneratorActivityAdapter.DemoViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);

        }


    }

   @Override
    public void onMoved (RecyclerView recyclerView,
                  RecyclerView.ViewHolder viewHolder,
                  int fromPos,
                  RecyclerView.ViewHolder target,
                  int toPos,
                  int x,
                  int y) {


        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(SchemeGeneratorActivityAdapter.DemoViewHolder myViewHolder);
        void onRowClear(SchemeGeneratorActivityAdapter.DemoViewHolder myViewHolder);
        void onDropFocus(@NonNull RecyclerView.ViewHolder selectedView,
                         @NonNull List<RecyclerView.ViewHolder> dropTargets,
                         int curx, int cury);

    }

    @Override public RecyclerView.ViewHolder chooseDropTarget(@NonNull RecyclerView.ViewHolder selectedView,
                                                              @NonNull List<RecyclerView.ViewHolder> dropTargets,
                                                              int curx, int cury) {
        //notify the adapter this drag hover another holder. It's a potential node-drop.
        mAdapter.onDropFocus(selectedView, dropTargets, curx, cury);

        return super.chooseDropTarget(selectedView, dropTargets, curx, cury);
    }
}
