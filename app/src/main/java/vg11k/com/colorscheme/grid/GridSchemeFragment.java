package vg11k.com.colorscheme.grid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.R;
import vg11k.com.colorscheme.StorageKind;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridSchemeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class GridSchemeFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static final String FRAGMENT_FEATURE_ID = "grid_scheme_fragment";

    private View m_view;

    private OnFragmentInteractionListener m_fragmentListener;

    private OnFragmentInteractionListener mListener;
    private GridSchemeFragmentAdapter m_adapter;

    private ArrayList<String> m_values = new ArrayList<String>();
    private DataProvider m_dataProvider;

    private StorageKind m_storageKind;


    public GridSchemeFragment() {
        // Required empty public constructor
    }

   /* // TODO: Rename and change types and number of parameters
    public static GridSchemeFragment newInstance(String param1, String param2) {
        GridSchemeFragment fragment = new GridSchemeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            m_dataProvider = getArguments().getParcelable(DataProvider.m_ID);
            m_storageKind = StorageKind.valueOf(getArguments().getInt(StorageKind.m_ID));

            //data initialization need a context

            m_view = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(m_view == null) {

            m_view = inflater.inflate(R.layout.fragment_grid_scheme, container, false);
            final Context context = m_view.getContext();

            ArrayList<String> tmp = m_dataProvider.getLocalJson(context);
            m_values.add(getResources().getString(R.string.new_scheme));
            for(String fileName : tmp) {
                if(fileName.contains(".json")) {
                    m_values.add(fileName.replace(".json", ""));
                }
            }

            if (m_view instanceof RecyclerView) {
                final RecyclerView recyclerView = (RecyclerView) m_view;

                LinearLayoutManager manager = null;
                int mColumnCount = 2;

                if (mColumnCount <= 1) {
                    manager = new LinearLayoutManager(context);
                    manager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(manager);
                } else {
                    manager = new GridLayoutManager(context, mColumnCount);
                    recyclerView.setLayoutManager(manager);
                    //throw new IllegalStateException("Should be only one column");
                }

                /*View.OnLongClickListener deleteListener = new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        @Override
                        public boolean onLongClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                            builder.setMessage("Delete selected Scheme ?")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            int index = m_values.indexOf(s);

                                            m_dataProvider.removeFile()
                                            m_values.remove(s);


                                            File fdelete = new File(uri.getPath());
                                            if (fdelete.exists()) {
                                                if (fdelete.delete()) {
                                                    System.out.println("file Deleted :" + uri.getPath());
                                                } else {
                                                    System.out.println("file not Deleted :" + uri.getPath());
                                                }
                                            }
                                            m_adapter.notifyItemRemoved(index);
                                        }
                                    })
                                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                            builder.create().show();

                            return false;
                        }
                    }
                };*/

                m_adapter = new GridSchemeFragmentAdapter(m_dataProvider, context, m_values, m_storageKind, mListener);
                recyclerView.setAdapter(m_adapter);
            }
        }

        // Inflate the layout for this fragment
        return m_view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setFragmentListener(OnFragmentInteractionListener listener) {
        m_fragmentListener = listener;
    }

    public String getNameOfItem(int index) {
        if(!(index > 0 && index < m_values.size())) {
            throw new IllegalStateException("Error : can't get the index " + index + " between 0 and " + m_values.size());
        }


        return m_values.get(index);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void startGeneratorOnExistingScheme(int selectedSchemeIndexToEdit);
    }

}
