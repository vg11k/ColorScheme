package vg11k.com.colorscheme.schemeGenerator;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

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
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {

        if(viewHolder1 instanceof AbstractDraggableViewHolder) {
            if(((AbstractDraggableViewHolder) viewHolder1).getModel().isDraggable()) {
                mAdapter.onRowMoved(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof AbstractDraggableViewHolder) {
            mAdapter.onRowSwiped(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof AbstractDraggableViewHolder) {
                AbstractDraggableViewHolder myViewHolder=
                        (AbstractDraggableViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
            /*else if(viewHolder instanceof HeaderLineViewHolder) {
                HeaderLineViewHolder myViewHolder=
                        (HeaderLineViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }*/

        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof AbstractDraggableViewHolder) {

            AbstractDraggableViewHolder myViewHolder=
                    (AbstractDraggableViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);

        }
        /*else if(viewHolder instanceof HeaderLineViewHolder) {
            HeaderLineViewHolder myViewHolder=
                    (HeaderLineViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }*/


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

    @Override public RecyclerView.ViewHolder chooseDropTarget(@NonNull RecyclerView.ViewHolder selectedView,
                                                              @NonNull List<RecyclerView.ViewHolder> dropTargets,
                                                              int curx, int cury) {
        //notify the adapter this drag hover another holder. It's a potential node-drop.
        mAdapter.onDropFocus(selectedView, dropTargets, curx, cury);
        return super.chooseDropTarget(selectedView, dropTargets, curx, cury);
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float alpha = 1 - (Math.abs(dX) / recyclerView.getWidth());
            viewHolder.itemView.setAlpha(alpha);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(AbstractViewHolder myViewHolder);
        void onRowClear(AbstractViewHolder myViewHolder);
        void onDropFocus(@NonNull RecyclerView.ViewHolder selectedView,
                         @NonNull List<RecyclerView.ViewHolder> dropTargets,
                         int curx, int cury);

        void onRowSwiped(int position);

    }


}
