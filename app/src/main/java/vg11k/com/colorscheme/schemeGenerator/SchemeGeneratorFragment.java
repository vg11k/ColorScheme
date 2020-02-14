package vg11k.com.colorscheme.schemeGenerator;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.IFragmentListener;
import vg11k.com.colorscheme.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SchemeGeneratorFragment.OnSchemeGeneratorFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SchemeGeneratorFragment
        extends Fragment
        implements StartDragListener,
            //OnColorPickerItemFragmentInteractionListener,
            IFragmentListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String FRAGMENT_TITLE = "Scheme generator";
    public static final String FRAGMENT_FEATURE_ID = "scheme_generator_fragment";
    public static final String ADDITIONNAL_DATA = "additionnal_data";


    private int mColumnCount = 1;
    private DataProvider m_dataProvider;
    private View m_view;
    private SchemeGeneratorFragmentAdapter m_adapter;
    final ArrayList<ColorPickerLine> m_listOfPickerLines = new ArrayList<ColorPickerLine>();
    ItemTouchHelper mTouchHelper;

    private ArrayList<Integer> m_initialIndexes = new ArrayList<Integer>();
    private ArrayList<Integer> m_initialProviderIndexes = new ArrayList<Integer>();

    private OnSchemeGeneratorFragmentInteractionListener m_fragmentListener;

    private final List<AbstractSchemeGeneratorLineModel> mValues = new ArrayList<AbstractSchemeGeneratorLineModel>();

    public SchemeGeneratorFragment() {
        // Required empty public constructor
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSchemeGeneratorFragmentInteractionListener extends IFragmentListener{
        // TODO: Update argument type and name
        void onSchemeGeneratorFragmentInteraction(Uri uri);
        void requestColorPickerFragment();
    }

    public void setFragmentListener(OnSchemeGeneratorFragmentInteractionListener listener) {
        m_fragmentListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);

        if (getArguments() != null) {

            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

            //String your_variable = getArguments().getString("your_key");
            m_dataProvider = getArguments().getParcelable(DataProvider.m_ID);

            int nbLines = m_dataProvider.getLinesCount() - 1;//we don't count the header
            for (int i = 0; i < nbLines; i++) {

                String[] line = m_dataProvider.getLine(i + 1);
                m_listOfPickerLines.add(new ColorPickerLine(i, line));
            }
            m_initialIndexes = getArguments().getIntegerArrayList("colorIndexes");
            m_initialProviderIndexes = getArguments().getIntegerArrayList("providerIndexes");

            m_view = null;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(m_view == null) {
            m_view = inflater.inflate(R.layout.fragment_scheme_generator, container, false);
            final Context context = m_view.getContext();


            mValues.add(new ImageMiniPreviewLineModel(0));






            HeaderLineModel firstHeader = new HeaderLineModel(1, "Base");
            firstHeader.setDraggable(false);
            mValues.add(firstHeader);

            /*if (m_initialIndexes != null && m_initialProviderIndexes != null) {

                if (m_initialIndexes.size() == m_initialProviderIndexes.size()) {

                    for (int valueIndex = 0; valueIndex < m_initialIndexes.size(); valueIndex++) {

                        mValues.add(new ColorLayerLineModel(mValues.size() + 1,
                                m_initialIndexes.get(valueIndex),
                                m_initialProviderIndexes.get(valueIndex),
                                "#" + m_dataProvider.getColorRGB(m_initialIndexes.get(valueIndex) + 1),
                                m_dataProvider.getColorNameForProvider(m_initialIndexes.get(valueIndex) + 1,
                                        m_initialProviderIndexes.get(valueIndex) + 1)));
                    }
                } else {
                    throw new IllegalStateException("Sizes of indexes and providerIndexes should be the same : " + m_initialIndexes.size() + " and " + m_initialProviderIndexes.size());
                }


            }*/

            mValues.add(new ButtonAddLineModel(mValues.size()));

            // Set the adapter
            if (m_view instanceof RecyclerView) {

                LinearLayoutManager manager = null;
                final RecyclerView recyclerView = (RecyclerView) m_view;

                if (mColumnCount <= 1) {
                    manager = new LinearLayoutManager(context);
                    manager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(manager);
                } else {
                    manager = new GridLayoutManager(context, mColumnCount);
                    recyclerView.setLayoutManager(manager);
                    throw new IllegalStateException("Should be only one column");

                }


                View.OnClickListener addContentLayerClickListener =
                        buildAddContentOnClickListener(context, m_listOfPickerLines);

                /////////
                View.OnClickListener imageMiniPreviewClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
                            //  ,
                            // 2000);
                        }
                        else {
                            startGallery();
                        }
                    }
                };
                ////////


                //drag & drop
                m_adapter = new SchemeGeneratorFragmentAdapter(m_dataProvider, context, m_view, mValues, addContentLayerClickListener, imageMiniPreviewClickListener, this);
                ItemTouchHelper.Callback callback = new ItemMoveCallback(m_adapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(recyclerView);
                mTouchHelper = touchHelper;

                recyclerView.setHasFixedSize(false);
                recyclerView.setAdapter(m_adapter);

                configureOnClickRecyclerView(recyclerView);
            }
        }



        FloatingActionButton fab = m_fragmentListener.getFab();
        fab.hide();

        return m_view;
    }

//https://stackoverflow.com/questions/39547728/how-to-get-image-from-gallery-on-fragment
    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {


            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {

                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;

                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);



                } catch (IOException e) {
                    e.printStackTrace();
                }

                int h = ((ImageMiniPreviewLineModel)mValues.get(0)).getBitMap().getHeight();
                int w = ((ImageMiniPreviewLineModel)mValues.get(0)).getBitMap().getWidth();

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapImage, w, h, false);

                ((ImageMiniPreviewLineModel)mValues.get(0)).setBitMap(scaledBitmap);
                m_adapter.notifyItemChanged(0);

            }
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (m_fragmentListener != null) {
            m_fragmentListener.onSchemeGeneratorFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSchemeGeneratorFragmentInteractionListener) {
            m_fragmentListener = (OnSchemeGeneratorFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_fragmentListener = null;
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        mTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void requestSwipe(RecyclerView.ViewHolder viewHolder) {
        mTouchHelper.startSwipe(viewHolder);
    }

    @Override
    public void requestMerge(AbstractDraggableViewHolder movedHolder, AbstractDraggableViewHolder targetHolder) {




        //cas interdits :
        // - headers vers quoi que se soit
        if(movedHolder.getItemViewType() == SchemeViewTypeLine.VIEW_TYPE_HEADER.getValue()) {
            return;
        }

        //cas autorises :

        // - color vers mix
        // - color vers color (cree un mix)
        //    - color vers mixedColor (integre le mix)
        // - mixed color vers color (retire du mix et cree un nouveau mix)
        // - mixed color vers mixed color (integre le nouveau mix)
        // - mixed color vers header (quitte le mix)
        // - mixed color vers mix
        // - mix vers header (deplace toutes les couleurs)
        // - mix vers mix (fusionne les mix)
        // - mix vers color SI color est dans un header



        // - color vers header





        HeaderLineModel targetHeader;


        if(targetHolder.getItemViewType() == SchemeViewTypeLine.VIEW_TYPE_HEADER.getValue()) {
            targetHeader = (HeaderLineModel) targetHolder.getModel();
            throw new IllegalStateException("Should not get in here anymore.");
        }
        else {
            targetHeader = ((HeaderLineModel.IHaveAHeaderModel)targetHolder.getModel()).getHeader();
        }


        // - color vers color (cree ou rejoint un mix)
        if(movedHolder.getItemViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW.getValue() &&
                targetHolder.getItemViewType() == SchemeViewTypeLine.VIEW_TYPE_CONTENT_ROW.getValue() ) {

            ColorLayerLineModel movedModel = (ColorLayerLineModel) movedHolder.getModel();
            ColorLayerLineModel targetModel = (ColorLayerLineModel) targetHolder.getModel();


            if(movedModel.getMix() != null) {
                movedModel.getMix().getChildrens().remove(movedModel);
                movedModel.setMix(null);
            }

            MixHeaderLineModel mixModel = null;

            if(targetModel.getMix() != null) {
                mixModel = targetModel.getMix();
            }
            else {
                mixModel = new MixHeaderLineModel(targetHolder.getAdapterPosition(), targetHeader);
                appendAt(mixModel, targetHolder.getAdapterPosition());
                targetModel.setMix(mixModel);
            }

            movedModel.setMix(mixModel);

            if(targetHolder.getAdapterPosition() > movedHolder.getAdapterPosition()) {
                //la ligne deplace va toujours en-dessous de la ligne cible.
                m_adapter.onRowMoved(movedHolder.getAdapterPosition(), targetHolder.getAdapterPosition());
            }

            m_adapter.notifyItemChanged(movedHolder.getAdapterPosition());
            m_adapter.notifyItemChanged(targetHolder.getAdapterPosition());
        }











        /*
        Snackbar.make(m_view, targetHolder.mContentView.getText() + " devient folder de " + movedHolder.mContentView.getText(), Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();//*/

    }

    @Override
    public FloatingActionButton getFab() {
        return null;
    }

    /*@Override
    public void onColorPickerListFragmentInteraction(List<ColorPickerLine> items) {
        ColorPickerLine pickerLine = items.get(0);

        ColorLayerLineModel model = new ColorLayerLineModel(mValues.size() - 1);
        model.setColorName(pickerLine.getColorName());
        model.setColorRGB(pickerLine.getcolorRGB());
        model.setCurrentColorIndex(pickerLine.getId());

        appendLast(model);
    }*/



    private View.OnClickListener buildAddContentOnClickListener(final Context context, final ArrayList<ColorPickerLine> tmpList) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Ajouter un element au Schema")
                        .setItems(R.array.addElementsArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // The 'which' argument contains the index position
                                // of the selected item

                                if(which == 0) {

                                    //startColorPickerLineFragment();
                                    m_fragmentListener.requestColorPickerFragment();

                                }
                                else if(which == 1) {
                                    //maybe better to add from the adapter ?

                                    askForNewHeader(context);
                                }

                            }
                        });
                builder.create().show();
            }
        };
    }

    public void askForNewHeader(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Saisir nom de la section");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = input.getText().toString();

                if(!inputText.isEmpty()) {
                    HeaderLineModel model = new HeaderLineModel(mValues.size() - 1, inputText);
                    appendLast(model);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void appendLast(AbstractSchemeGeneratorLineModel model) {
        appendAt(model, mValues.size() - 1);
    }

    public void appendAt(AbstractSchemeGeneratorLineModel model, int index) {

        model.setLineIndex(index);
        mValues.add(index, model);

        for(int i = 1; i + index < mValues.size(); i++ )
            mValues.get(index + i).setLineIndex(index + i);

        m_adapter.notifyItemInserted(index);
        m_adapter.notifyDataSetChanged();

        RecyclerView recyclerView = (RecyclerView) m_view;
        recyclerView.scrollToPosition(index + 1);

    }

    private void configureOnClickRecyclerView(RecyclerView recyclerView) {

    }

    public void addColors(ArrayList<Integer> indexes, ArrayList<Integer> providerIndexes) {

        if(indexes.size() == 0) {
            return;
        }

        HeaderLineModel headerToAttachNewColors = null;

        for(int i = mValues.size() - 1; i > -1; i--) {
            if(mValues.get(i).getViewType() == SchemeViewTypeLine.VIEW_TYPE_HEADER) {
                headerToAttachNewColors = (HeaderLineModel) mValues.get(i);
                i = -1;
            }
        }
        MixHeaderLineModel mixModel = null;


        //add it as new mix
        if(indexes.size() > 1) {
            mixModel = new MixHeaderLineModel(mValues.size() - 1, headerToAttachNewColors);
            appendLast(mixModel);
        }

        for(int valueIndex = 0; valueIndex < indexes.size(); valueIndex++) {

            ColorLayerLineModel color =
                new ColorLayerLineModel(
                    mValues.size() - 1,
                    indexes.get(valueIndex),
                    providerIndexes.get(valueIndex),
                    "#" + m_dataProvider.getColorRGB(indexes.get(valueIndex) + 1),
                    m_dataProvider.getColorNameForProvider(indexes.get(valueIndex) + 1,
                            providerIndexes.get(valueIndex) + 1),
                        headerToAttachNewColors);

            appendLast(color);
            color.setMix(mixModel);
            m_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            //restore !
        }
    }

    @Override
    public  void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //save the fragment here !
    }




    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}
