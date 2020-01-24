package vg11k.com.colorscheme.colorPicker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import vg11k.com.colorscheme.ColorCircle;
import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.GenericOnListInteractionListener;
import vg11k.com.colorscheme.R;
import vg11k.com.colorscheme.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ColorPickerItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public static final String FRAGMENT_FEATURE_ID = "color_picker_item_fragment";
    public static final String FRAGMENT_TITLE = "Color picker";

    private DataProvider m_dataProvider;
    private List<ColorPickerLine> m_colorPickerLines;

    private ColorPickerItemRecyclerViewAdapter m_adapter;

    private View m_view;

    private int m_selectedProvider = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ColorPickerItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    /*public static ColorPickerItemFragment newInstance(int columnCount) {
        ColorPickerItemFragment fragment = new ColorPickerItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

            //String your_variable = getArguments().getString("your_key");
            m_dataProvider = getArguments().getParcelable(DataProvider.m_ID);

            //String s = m_dataProvider.getLine(2)[0];
            //System.out.println("valeur lue : " + s);

            m_colorPickerLines = new ArrayList<ColorPickerLine>();

            int nbLines = m_dataProvider.getLinesCount() - 1;//we don't count the header

            for(int i = 0; i < nbLines; i++) {

                String[] line = m_dataProvider.getLine(i+1);
                m_colorPickerLines.add(new ColorPickerLine(i,line));
            }

            //m_colorPickerLines = DummyContent.ITEMS;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colorpickeritem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            m_adapter = new ColorPickerItemRecyclerViewAdapter(m_colorPickerLines, mListener, m_dataProvider);
            recyclerView.setAdapter(m_adapter);
            configureOnClickRecyclerView(recyclerView);
        }

        m_view = view;
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                    System.out.println("settings puched");
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                    System.out.println("favorites puched");
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
            case R.id.action_switch_provider:
                switchProvider();

                return true;
            /*case R.id.activity_menu_item:

                // Not implemented here
                return false;
            case R.id.fragment_menu_item:

                // Do Fragment menu item stuff here
                return true;*/

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void switchProvider() {

        final String[] colors = Arrays.copyOfRange(m_dataProvider.getLine(0),1, m_dataProvider.getLine(0).length);

        AlertDialog.Builder builder = new AlertDialog.Builder(m_view.getContext());
        builder.setTitle("Choose the provider to use");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                m_selectedProvider = which;
                m_adapter.setSelectedProvider(m_selectedProvider);
                //Snackbar.make(m_view, "Select le fournisseur " + colors[which], Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        builder.show();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener/* extends GenericOnListInteractionListener*/ {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ColorPickerLine item);
    }

    private void configureOnClickRecyclerView(RecyclerView recyclerView){
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_colorpickeritem)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        System.out.println("Element touche : " + position);



                        ColorPickerLine colorPicker = m_colorPickerLines.get(position);
                        System.out.println(m_colorPickerLines.get(position).getColorName());

                        int color;

                        if(!colorPicker.isSelected()) {
                            color = Color.parseColor("#00ff00");
                            colorPicker.setSelected(true);
                        }
                        else {
                            color = Color.WHITE;
                            colorPicker.setSelected(false);
                        }

                        ((ColorCircle) v.findViewById(R.id.circle)).setRGBBackgroundColor(color);
                        v.findViewById(R.id.circle).refreshDrawableState();
                        v.setBackgroundColor(color);

                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //MenuItem fav = menu.add("add");
        //fav.setIcon("@android:drawable/btn_star");

        menu.findItem(R.id.action_switch_provider).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);

        //return true;
    }


}