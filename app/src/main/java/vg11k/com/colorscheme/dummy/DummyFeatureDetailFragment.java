package vg11k.com.colorscheme.dummy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.view.Display;
import android.graphics.Point;

import vg11k.com.colorscheme.ColorCircle;
import vg11k.com.colorscheme.R;
import vg11k.com.colorscheme.menus.MenuGenerique;
import vg11k.com.colorscheme.menus.MenusContainer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DummyFeatureDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DummyFeatureDetailFragment extends Fragment {


    public static final String FRAGMENT_FEATURE_ID = "dummy_feature_id";
    private MenuGenerique m_menu;
    private static int m_circleRadius = 50;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   // private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public DummyFeatureDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DummyFeatureDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DummyFeatureDetailFragment newInstance(/*String param1, String param2*/) {
        DummyFeatureDetailFragment fragment = new DummyFeatureDetailFragment();
        Bundle args = new Bundle();
       /* args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(getArguments().containsKey(FRAGMENT_FEATURE_ID)){
            m_menu = MenusContainer.FEATURES_MAP.get(getArguments().getString(FRAGMENT_FEATURE_ID));



            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(m_menu.getContent());
            }
        }


        //setContentView(new MyCircle(this));


        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    //LinearLayout tag = (LinearLayout)(inflater.inflate(R.layout.fragment_tag, null)).findViewById(R.id.idOfTag);

        //layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //layout.setOrientation(LinearLayout.HORIZONTAL);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dummy_feature_detail, container, false);

        if(m_menu != null) {
            ((TextView) rootView.findViewById(R.id.fragment_dummy_feature_dtext)).setText("Old textView ! " + m_menu.getContent());
        }



        //MyCircle circleView = (MyCircle) (inflater.inflate(R.layout.fragment_dummy_feature_detail, null)).findViewById(R.id.testView);


        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.layout_test) ;//new LinearLayout(getContext());



        ColorCircle circle = new ColorCircle(getContext(), "#ff9999");
        circle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, m_circleRadius * 2, 1f));
        layout.addView(circle);

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                //pop a menu to select a new color
            }
        });

        TextView oldTextView = rootView.findViewById(R.id.fragment_dummy_feature_dtext);
        layout.removeView(oldTextView);


        Display display = this.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;



        int usableWidth = width - (m_circleRadius * 2) - (layout.getPaddingRight() * 3);


        TextView textDummy = new TextView(getContext());
        textDummy.setText("DUMMY ! " + System.getProperty ("line.separator") + m_menu.getContent());
        textDummy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100));
        //textDummy.setMinimumWidth(usableWidth / 2);
        textDummy.setWidth(usableWidth / 2);

        layout.addView(textDummy);

        TextView textDummy2 = new TextView(getContext());
        textDummy2.setText("DUMMY ! " + System.getProperty ("line.separator") + m_menu.getContent());
        textDummy2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100));
        //textDummy2.setMinimumWidth(usableWidth / 2);
        textDummy2.setWidth(usableWidth / 2);

        layout.addView(textDummy2);

        /*TextView textDummy3 = new TextView(getContext());
        textDummy3.setText("DUMMY ! " + System.getProperty ("line.separator") + m_menu.getContent());
        textDummy3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100, 1f));
        textDummy3.setMinimumWidth(1000);

        layout.addView(textDummy3);*/

        //layout.addView(oldTextView);



        circle.setVisibility(View.VISIBLE);
        textDummy.setVisibility(View.VISIBLE);

        return rootView;//return empty thing if linearLayout. WHY ??
    }

    public static class MyCircle extends View
    {
        Paint paint = null;
        public MyCircle(Context context)
        {
            super(context);
            paint = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            int radius = m_circleRadius;
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#ff9999"));
            canvas.drawCircle(radius, radius, radius, paint);

            //canvas.translate(getWidth()/2f,getHeight()/2f);
            //canvas.drawCircle(0,0, radius, paint);
        }

    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
