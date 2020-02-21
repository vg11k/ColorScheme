package vg11k.com.colorscheme.schemeGenerator;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


class SchemeGeneratorFragmentAdapter extends RecyclerView.Adapter<AbstractViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract  {

    private final List<AbstractSchemeGeneratorLineModel> mValues;
    private Context m_context;
    private View m_view;
    private final DataProvider m_dataProvider;
    private ViewGroup mParent;

    private int holderCounter = 0;

    private View.OnClickListener mClickListener;
    private View.OnClickListener mImageClickListener;
    private StartDragListener  mStartDragListener;

    private List<AbstractViewHolder> m_holders;
    private AbstractDraggableViewHolder m_hoveredHolder = null;

    private Set<Integer> m_notificationsIndex;


    private static final int PICK_IMAGE = 100;
    Uri imageUri;


    public SchemeGeneratorFragmentAdapter(DataProvider dataProvider,
                                          Context context,
                                          View view,
                                          final List<AbstractSchemeGeneratorLineModel> values,
                                          View.OnClickListener clickListener,
                                          View.OnClickListener imageClickListener,
                                          StartDragListener dragListener) {

        m_dataProvider = dataProvider;
        m_context = context;
        m_view = view;
        mValues = values;

        m_holders = new ArrayList<AbstractViewHolder>();

        mStartDragListener = dragListener;
        mClickListener = clickListener;
        mImageClickListener = imageClickListener;

        m_notificationsIndex = new HashSet<Integer>();
    }





    public Context getContext() {
        return m_context;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
        /*int cpt = 0;
        for(AbstractSchemeGeneratorLineModel holder : mValues) {
            if(holder.getVisibility() == View.VISIBLE) {
                cpt++;
            }
        }
        return cpt;*/
    }





    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {

        mParent = parentViewGroup;
        AbstractViewHolder holder = null;

        if(viewType == SchemeViewTypeLine.VIEW_TYPE_PREVIEW_IMAGE.getValue()) {
            View view = LayoutInflater.from(m_context).inflate(R.layout.scheme_generator_preview_item, mParent, false);
            holder = new ImageViewHolder(view);
        }
        else if(viewType == SchemeViewTypeLine.VIEW_TYPE_ADD_BUTTON.getValue()){
            View view = LayoutInflater.from(m_context).inflate(R.layout.scheme_generator_button_item, mParent, false);
            holder = new ButtonViewHolder(view);
        }
        else if(viewType == SchemeViewTypeLine.VIEW_TYPE_HEADER.getValue()) {
            View view = LayoutInflater.from(m_context).inflate(R.layout.scheme_generator_header_item, mParent, false);
            holder = new HeaderLineViewHolder(view);
        }
        else if(viewType == SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER.getValue()) {
            View view = LayoutInflater.from(m_context).inflate(R.layout.scheme_generator_mix_header_item, mParent, false);
            holder = new MixHeaderLineViewHolder(view);
        }
        else { // if(viewType == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW.getValue()) {
            View view = LayoutInflater.from(m_context).inflate(R.layout.scheme_generator_layer_item, mParent, false);
            holder = new ColorLayerLineViewHolder(view);
        }

        m_holders.add(holder);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final AbstractViewHolder holder, int position) {


        AbstractSchemeGeneratorLineModel abModel = mValues.get(position);

        holder.setModel(abModel);


        if(abModel.getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
            ColorLayerLineViewHolder viewHolder = (ColorLayerLineViewHolder) holder;

            ColorLayerLineModel colorModel = (ColorLayerLineModel) abModel;
            viewHolder.mContentView.setText(colorModel.getColorName());
            viewHolder.mColorCircle.setRGBColor(colorModel.getColorRGB());
            viewHolder.m_kindOfProcess.setText(colorModel.getKindOfProcessName());

            viewHolder.mSpace.setVisibility(colorModel.getSpaceVisibility());
            viewHolder.m_kindOfProcess.setVisibility(colorModel.getKindOfProcessVisibility());

            viewHolder.getRowView().setVisibility(colorModel.getVisibility());

            viewHolder.getRowView().getLayoutParams().height = colorModel.getHeight();

            colorModel.setCurrentBackgroundColor(colorModel.getDefaultBackgroundColor());
            viewHolder.getRowView().setBackgroundColor(colorModel.getCurrentBackgroundColor());

            initiateDraggableListeners(viewHolder);
            initiateKindOfProcessListener(viewHolder);
            initiateSwitchProviderListener(viewHolder);
        }
        else if(abModel.getViewType() == SchemeViewTypeLine.VIEW_TYPE_HEADER) {
            HeaderLineViewHolder viewHolder = (HeaderLineViewHolder) holder;
            HeaderLineModel headerModel = (HeaderLineModel) abModel;

            viewHolder.mContentView.setText(headerModel.getHeaderContent());
            viewHolder.getDragImage().setVisibility(headerModel.getDragImageVisibility());

            initiateDraggableListeners(viewHolder);
            initiateCollapseAndExpandListeners(viewHolder);

            viewHolder.getCollapseIcon().setVisibility(headerModel.getCollapsedIconState());
            viewHolder.getExpandIcon().setVisibility(headerModel.getExpandIconState());

            viewHolder.getRowView().setBackgroundColor(headerModel.getCurrentBackgroundColor());


        }
        else if(abModel.getViewType() == SchemeViewTypeLine.VIEW_TYPE_ADD_BUTTON) {

            ButtonViewHolder viewHolder = (ButtonViewHolder) holder;
            viewHolder.mView.setOnClickListener(mClickListener);
        }
        else if(abModel.getViewType() == SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER) {
            MixHeaderLineViewHolder viewHolder = (MixHeaderLineViewHolder) holder;
            MixHeaderLineModel mixModel = (MixHeaderLineModel) abModel;

            viewHolder.mContentView.setText(mixModel.getHeaderContent());
            viewHolder.m_kindOfProcess.setText(mixModel.getKindOfProcessName());
            viewHolder.getCollapseIcon().setVisibility(mixModel.getCollapsedIconState());
            viewHolder.getExpandIcon().setVisibility(mixModel.getExpandIconState());


            viewHolder.getRowView().setVisibility(mixModel.getVisibility());
            viewHolder.getRowView().getLayoutParams().height = mixModel.getHeight();

            viewHolder.getRowView().setBackgroundColor(mixModel.getCurrentBackgroundColor());


            initiateCollapseAndExpandListeners(viewHolder);
            initiateDraggableListeners(viewHolder);
            initiateKindOfProcessListener(viewHolder);
        }
        else if(abModel.getViewType() == SchemeViewTypeLine.VIEW_TYPE_PREVIEW_IMAGE) {

            ImageMiniPreviewLineModel imModel = (ImageMiniPreviewLineModel) abModel;


            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;

            if(imModel.getBitMap() == null) {
                imModel.setBitMap(SchemeGeneratorFragment.drawableToBitmap(imageViewHolder.mView.getDrawable()));
            }

            imageViewHolder.mView.setImageBitmap(imModel.getBitMap());
            imageViewHolder.mView.setOnClickListener(mImageClickListener);

        }
        else {
            System.out.println("NOTHING TO DO FOR NOW");
        }

    }




    public void initiateDraggableListeners(final AbstractDraggableViewHolder draggableViewHolder) {

        draggableViewHolder.getDragImage().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    AbstractSchemeGeneratorLineModel model = draggableViewHolder.getModel();
                    if(model.isDraggable()) {
                        mStartDragListener.requestDrag(draggableViewHolder);
                    }
                }
                return false;
            }

        } );

        draggableViewHolder.mContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    AbstractSchemeGeneratorLineModel model = draggableViewHolder.getModel();
                    if(model.isDraggable()) {
                        mStartDragListener.requestSwipe(draggableViewHolder);
                    }
                }
                return false;
            }

        } );

    }

    public void initiateKindOfProcessListener(final KindOfProcess.IKindOfProcessedHolder holderWithProcess) {
        holderWithProcess.getKindOfProcessView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

               // final ArrayList<String> kindOfProcessNames = new ArrayList<String>();
                final String[] kindOfProcessArray = m_context.getResources().getStringArray(R.array.selectKindOfProcessArray);/*= new ArrayList<String>();
                for(KindOfProcess k : KindOfProcess.values()) {
                    kindOfProcessNames.add(k.getName());
                }

                kindOfProcessNames.add(m_context.getResources().getString(R.string.cancel));*/



                AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                builder.setTitle(m_context.getResources().getString(R.string.select_process_to_apply))
                        .setItems(kindOfProcessArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if(which != kindOfProcessArray.length - 1) {

                                    holderWithProcess.setKindOfProcessOnModel(KindOfProcess.valueOf(which), m_context.getResources());
                                    notifyDataSetChanged();
                                }

                            }
                        });
                builder.create().show();

                return false;
            }
        });
    }

    public void initiateSwitchProviderListener(final ColorLayerLineViewHolder holder) {
        holder.getColorCircle().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final ColorLayerLineModel colorModel = (ColorLayerLineModel) holder.getModel();

                int initialProviderIndex = colorModel.getCurrentProviderIndex();
                final int initialColorIndex = colorModel.getCurrentColorIndex();
                if(initialProviderIndex == -1) {
                    throw new IllegalStateException("Error initiateSwitchProviderListener, index should be initialized");
                }

                String[] allColorNames = m_dataProvider.getLine(initialColorIndex);
                final ArrayList<String> candidatesColorNames = new ArrayList<String>();
                final ArrayList<Integer> candidatesColorIndexes =  new ArrayList<Integer>();
                final ArrayList<String> candidatesDisplayed = new ArrayList<String>();

                for(int i = 0; i < m_dataProvider.getProviderCount(); i++) {
                    if(i != initialProviderIndex) {
                        String colorNameOfProvider = m_dataProvider.getColorNameForProvider(initialColorIndex, i);
                        if(!colorNameOfProvider.isEmpty()) {
                            candidatesColorNames.add(colorNameOfProvider);
                            candidatesColorIndexes.add(i);
                            candidatesDisplayed.add(m_dataProvider.getColorProvider(i) + " : " + colorNameOfProvider);
                        }
                    }
                }

                String currentProviderName = m_dataProvider.getColorProvider(initialProviderIndex);


                candidatesDisplayed.add(m_context.getResources().getString(R.string.keep) + " " + currentProviderName + ".");

                String title = m_context.getResources().getString(R.string.select_another_provider_for)  +
                        "\n" + colorModel.getColorName() + " (" + colorModel.getColorRGB() + ")";

                AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                builder.setTitle(title)
                        .setItems((String[]) candidatesDisplayed.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if(which != candidatesDisplayed.size() - 1) {

                                    String selectedColorName = candidatesColorNames.get(which);
                                    int selectedProviderIndex = candidatesColorIndexes.get(which);

                                    //debug
                                    //String onProviderValue = m_dataProvider.getColorNameForProvider(initialColorIndex + 1, selectedProviderIndex + 1);

                                    colorModel.setColorName(selectedColorName);
                                    colorModel.setCurrentProviderIndex(selectedProviderIndex);

                                    notifyDataSetChanged();
                                }

                            }
                        });
                builder.create().show();


                return false;
            }




        });


    }

    public void initiateCollapseAndExpandListeners (final AbstractDraggableViewHolder.ICollapsableViewHolder viewHolder) {

        viewHolder.getCollapseIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AbstractSchemeGeneratorLineModel.ICollapsableModel model = viewHolder.getCollapsableModel();
                model.collapse();
                int viewHolderPosition = ((AbstractDraggableViewHolder) viewHolder).getModel().getLineIndex();//.getAdapterPosition();
                notifyItemChanged(viewHolderPosition);
                notifyDataSetChanged();
            }
        });

        viewHolder.getExpandIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AbstractSchemeGeneratorLineModel.ICollapsableModel model = viewHolder.getCollapsableModel();
                model.expand();
                int viewHolderPosition = ((AbstractDraggableViewHolder) viewHolder).getAdapterPosition();
                notifyItemChanged(viewHolderPosition);
                notifyDataSetChanged();
            }
        });
    }



    @Override
    public void onRowSwiped(int position) {

        for(int i = position + 1; i < mValues.size(); i++) {
            mValues.get(i).setLineIndex(i - 1);
        }

        mValues.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {

        //the move has been completed : it's not a drop-node so we cancel it.
        if(m_hoveredHolder != null) {
            //m_hoveredHolder.m_rowView.setBackgroundColor(Color.WHITE);
            m_hoveredHolder.getModel().setCurrentBackgroundColor(m_hoveredHolder.getModel().getDefaultBackgroundColor());
            m_hoveredHolder.getRowView().setBackgroundColor(m_hoveredHolder.getModel().getCurrentBackgroundColor());

            //notifyItemChanged(m_hoveredHolder.getModel().getLineIndex());

            m_hoveredHolder = null;
        }

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mValues, i, i + 1);

                mValues.get(i).setLineIndex(i+1);
                mValues.get(i+1).setLineIndex(i);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mValues, i, i - 1);
                mValues.get(i).setLineIndex(i-1);
                mValues.get(i-1).setLineIndex(i);
            }
        }

        notifyItemMoved(fromPosition, toPosition);

        //moved item now at toPosition and the other at fromPosition

        /*if(mValues.get(toPosition).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
            revalidateMixs(toPosition);
        }
        else if(mValues.get(toPosition).getViewType() == SchemeViewTypeLine.VIEW_TYPE_HEADER ||
                mValues.get(toPosition).getViewType() == SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER) {
            revalidateMixs(fromPosition);
        }*/
    }


    //TODO manage to change the concept from swap 1-1 to 1-x.
    //verification des mixs
    public void revalidateMixs(int currentPosition) {


        if(mValues.get(currentPosition).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {

            ColorLayerLineModel currentMoved = (ColorLayerLineModel) mValues.get(currentPosition);
            MixHeaderLineModel currentMovedMix = currentMoved.getMix();

            //si le current est une couleur mix devenu orpheline, elle a quitte le mix
            if(currentMovedMix != null) {

                MixHeaderLineModel mixOfPrevious = null;
                if(mValues.get(currentPosition - 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
                    mixOfPrevious = ((ColorLayerLineModel)mValues.get(currentPosition - 1)).getMix();
                }

                if(mValues.get(currentPosition - 1) != currentMovedMix && currentMovedMix != mixOfPrevious) {
                    currentMoved.setMix(null);
                    //notifyDataSetChanged();
                    //notifyItemChanged(currentPosition);
                    m_notificationsIndex.add(currentPosition);
                }
            }
            else {//si le current est une couleur non mix encerclee de mixHeader/couleur mix du meme mix, elle rejoint le mix

                //si le precedent est une couleur d'un mix...
                if(mValues.get(currentPosition - 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
                    currentMovedMix = ((ColorLayerLineModel)mValues.get(currentPosition - 1)).getMix();
                }//ou si le precedent est le header d'un mix...
                else if(mValues.get(currentPosition - 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER) {
                    currentMovedMix = ((MixHeaderLineModel)mValues.get(currentPosition - 1));
                }

                if(currentMovedMix != null) {
                    //et que le suivant est une couleur du meme mix
                    if(mValues.get(currentPosition + 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
                        if(currentMovedMix == ((ColorLayerLineModel)mValues.get(currentPosition + 1)).getMix()) {
                            currentMoved.setMix(currentMovedMix);
                            m_notificationsIndex.add(currentPosition);
                        }
                    }
                }
            }
        }
    }

    public void revalidateMixs() {
        for(int i = 2;  i < mValues.size() - 1; i++) {
            if(mValues.get(i).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {

                ColorLayerLineModel currentMoved = (ColorLayerLineModel) mValues.get(i);
                MixHeaderLineModel currentMovedMix = currentMoved.getMix();

                //si le current est une couleur non mix encerclee de mixHeader/couleur mix du meme mix, elle rejoint le mix
                if(currentMovedMix == null){

                    //si le precedent est une couleur d'un mix...
                    if(mValues.get(i - 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
                        currentMovedMix = ((ColorLayerLineModel)mValues.get(i - 1)).getMix();
                    }//ou si le precedent est le header d'un mix...
                    else if(mValues.get(i - 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER) {
                        currentMovedMix = ((MixHeaderLineModel)mValues.get(i - 1));
                    }

                    if(currentMovedMix != null) {
                        //et que le suivant est une couleur du meme mix
                        if(mValues.get(i + 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
                            if(currentMovedMix == ((ColorLayerLineModel)mValues.get(i + 1)).getMix()) {
                                currentMoved.setMix(currentMovedMix);
                                m_notificationsIndex.add(i);
                            }
                        }
                    }
                }
            }
        }

        for(int i = 2;  i < mValues.size() - 1; i++) {
            if (mValues.get(i).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {

                ColorLayerLineModel currentMoved = (ColorLayerLineModel) mValues.get(i);
                MixHeaderLineModel currentMovedMix = currentMoved.getMix();

                //si le current est une couleur mix devenu orpheline, elle a quitte le mix
                if(currentMovedMix != null) {

                    MixHeaderLineModel mixOfPrevious = null;
                    if(mValues.get(i - 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {
                        mixOfPrevious = ((ColorLayerLineModel)mValues.get(i - 1)).getMix();
                    }

                    if(mValues.get(i - 1) != currentMovedMix && currentMovedMix != mixOfPrevious) {
                        currentMoved.setMix(null);
                        //notifyDataSetChanged();
                        //notifyItemChanged(currentPosition);
                        m_notificationsIndex.add(i);
                    }
                }
            }
        }
    }

    //verification des headers
    public void revalidateHeaders() {

        //ne pas prendre la preview, le header de base & le bouton add en compte
        for(int i = 2;  i < mValues.size() - 1; i++) {

            if(mValues.get(i).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW ||
                    mValues.get(i).getViewType() == SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER)
            {
                //si je suis une couleur ou un mix...
                HeaderLineModel.IHaveAHeaderModel model = (HeaderLineModel.IHaveAHeaderModel)mValues.get(i);

                //... soit je suis apres mon header...
                if(mValues.get(i - 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_HEADER) {
                    if (model.getHeader() != mValues.get(i - 1)) {
                        model.getHeader().getChildrens().remove(model);
                        model.setHeader((HeaderLineModel)mValues.get(i - 1));
                    }
                }
                //...soit j'ai le meme header que mon predecesseur
                else if(mValues.get(i - 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW ||
                        mValues.get(i - 1).getViewType() == SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER){

                    HeaderLineModel.IHaveAHeaderModel previousModel = (HeaderLineModel.IHaveAHeaderModel)mValues.get(i - 1);

                    if (model.getHeader() != previousModel.getHeader()) {
                        model.getHeader().getChildrens().remove(model);
                        model.setHeader(previousModel.getHeader());
                    }
                }
                else {
                    throw new IllegalStateException("Should not be possible");
                }

                //un melange vide n'a pas de raison d'exister
                if(mValues.get(i).getViewType() == SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER) {
                    MixHeaderLineModel collapsableModel =
                            (MixHeaderLineModel) mValues.get(i);

                    //suicide
                    if(collapsableModel.getChildrens().isEmpty()) {
                        collapsableModel.getHeader().getChildrens().remove(collapsableModel);
                        mValues.remove(i);
                        notifyItemRemoved(i);
                    }
                }

            }
        }
    }

    @Override
    public void onRowSelected(AbstractViewHolder myViewHolder) {

        if(myViewHolder instanceof AbstractDraggableViewHolder) {
            myViewHolder.getModel().setCurrentBackgroundColor(myViewHolder.getModel().getMovedBackgroundColor());
            myViewHolder.getRowView().setBackgroundColor(myViewHolder.getModel().getCurrentBackgroundColor());

            //notifyItemChanged(myViewHolder.getModel().getLineIndex());
        }
    }

    @Override
    public void onRowClear(AbstractViewHolder myViewHolder) {


        //revalidateMixs(myViewHolder.getModel().getLineIndex());
        //revalidateMixs(myViewHolder.getModel().getLineIndex() - 1);
        //revalidateMixs(myViewHolder.getModel().getLineIndex() + 1);

        revalidateMixs();


        ArrayList<Integer> tmpList = new ArrayList<Integer>(m_notificationsIndex);
        while(!tmpList.isEmpty()) {
            notifyItemChanged(tmpList.get(0));

            if(tmpList.size() == 1)
            {
                notifyDataSetChanged();
            }
            tmpList.remove(0);
        }

        m_notificationsIndex.clear();

        revalidateHeaders();

        if(myViewHolder instanceof AbstractDraggableViewHolder) {

            final AbstractDraggableViewHolder holder = (AbstractDraggableViewHolder) myViewHolder;




            //holder.m_rowView.setBackgroundColor(Color.WHITE);
            holder.getModel().setCurrentBackgroundColor(holder.getModel().getDefaultBackgroundColor());
            holder.getRowView().setBackgroundColor(holder.getModel().getCurrentBackgroundColor());

            //notifyItemChanged(holder.getModel().getLineIndex());

            if (m_hoveredHolder != null) {
                //m_hoveredHolder.m_rowView.setBackgroundColor(Color.WHITE);
                m_hoveredHolder.getModel().setCurrentBackgroundColor(m_hoveredHolder.getModel().getDefaultBackgroundColor());
                m_hoveredHolder.getRowView().setBackgroundColor(m_hoveredHolder.getModel().getCurrentBackgroundColor());
                //notifyItemChanged(m_hoveredHolder.getModel().getLineIndex());


                if(m_hoveredHolder.getModel().getViewType() != SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW ||
                        holder.getModel().getViewType() != SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW) {

                    //pas de merge avec autre chose que des couleurs. Point.
                    return;
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                builder.setMessage(m_context.getResources().getString(R.string.mix) + " " +
                        m_hoveredHolder.mContentView.getText() + "\n " +
                        m_context.getResources().getString(R.string.and) + " " + holder.mContentView.getText() + " ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                mStartDragListener.requestMerge(holder, m_hoveredHolder);
                                /*Snackbar.make(m_view, m_hoveredHolder.mContentView.getText() + " devient folder de " + holder.mContentView.getText(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();*/
                            }
                        })
                        .setNegativeButton(m_context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                builder.create().show();
            }
        }
    }




    @Override
    public void onDropFocus(@NonNull RecyclerView.ViewHolder selectedView, @NonNull List<RecyclerView.ViewHolder> dropTargets, int curx, int cury) {

        if(m_hoveredHolder != null) {
            m_hoveredHolder.getModel().setCurrentBackgroundColor(m_hoveredHolder.getModel().getDefaultBackgroundColor());
            m_hoveredHolder.getRowView().setBackgroundColor(m_hoveredHolder.getModel().getCurrentBackgroundColor());
            //notifyItemChanged(m_hoveredHolder.getModel().getLineIndex());
        }

        if(dropTargets.get(0) instanceof AbstractDraggableViewHolder) {
            m_hoveredHolder = ((AbstractDraggableViewHolder) dropTargets.get(0));

            m_hoveredHolder.getModel().setCurrentBackgroundColor(m_hoveredHolder.getModel().getHoveredBackgroundColor());
            m_hoveredHolder.getRowView().setBackgroundColor(m_hoveredHolder.getModel().getCurrentBackgroundColor());

            //notifyItemChanged(m_hoveredHolder.getModel().getLineIndex());
        }

        //revalidateMixs(((AbstractDraggableViewHolder)selectedView).getModel().getLineIndex());
    }

    @Override
    public int getItemViewType(int position) {

        if(mValues.get(position) instanceof ImageMiniPreviewLineModel) {
            return SchemeViewTypeLine.VIEW_TYPE_PREVIEW_IMAGE.getValue();
        }
        else if(mValues.get(position) instanceof ButtonAddLineModel) {
            return SchemeViewTypeLine.VIEW_TYPE_ADD_BUTTON.getValue();
        }
        else if(mValues.get(position) instanceof HeaderLineModel) {
            return SchemeViewTypeLine.VIEW_TYPE_HEADER.getValue();
        }
        else if(mValues.get(position) instanceof MixHeaderLineModel) {
            return SchemeViewTypeLine.VIEW_TYPE_MIXED_HEADER.getValue();
        }
        else
            return SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW.getValue();
    }

    public int getViewTypeCount() {
        return SchemeViewTypeLine.getSize();
    }

    class ImageViewHolder extends AbstractViewHolder {

        final ImageView mView;

        ImageViewHolder(View view) {
            super(view);

            mView = view.findViewById(R.id.cool_mini_preview);
        }
    }

    class ButtonViewHolder extends AbstractViewHolder {

        final ImageButton mView;
        ButtonViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.addContentImage);
        }
    }

}
