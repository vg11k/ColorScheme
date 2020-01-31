package vg11k.com.colorscheme.schemeGenerator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.R;

public class SchemeGeneratorActivity extends AppCompatActivity implements StartDragListener {

    public static final String ACTIVITY_TITLE = "Scheme generator";
    public static final String ACTIVITY_FEATURE_ID = "scheme_generator_tool";

    private DataProvider m_dataProvider;
    private View m_view;

    private SchemeGeneratorActivityAdapter m_adapter;


    private int mColumnCount = 1;

    ItemTouchHelper mTouchHelper;

    private final List<String> mValues = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_generator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Scheme Generator");

        Bundle b = getIntent().getExtras();
        if (b != null) {

            m_dataProvider = b.getParcelable(DataProvider.m_ID);

            /*int nbLines = m_dataProvider.getLinesCount() - 1;//we don't count the header
            for (int i = 0; i < nbLines; i++) {
                String[] line = m_dataProvider.getLine(i + 1);
            }*/
        }


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_view = findViewById(R.id.scheme_generator_content);
        Context context = m_view.getContext();


        mValues.add("Dummy value 1");
        mValues.add("Dummy value 2");
        mValues.add("Dummy value 3");



        // Set the adapter
        if (m_view instanceof RecyclerView) {

            RecyclerView.LayoutManager manager = null;
            final RecyclerView recyclerView = (RecyclerView) m_view;

            if (mColumnCount <= 1) {
                manager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(manager);
            } else {
                manager = new GridLayoutManager(context, mColumnCount);
                recyclerView.setLayoutManager(manager);
                throw new IllegalStateException("Should be only one column");

            }

            m_adapter = new SchemeGeneratorActivityAdapter(m_dataProvider, context, m_view, mValues, this);
            ItemTouchHelper.Callback callback = new ItemMoveCallback(m_adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
            mTouchHelper = touchHelper;

            recyclerView.setAdapter(m_adapter);
            
            configureOnClickRecyclerView(recyclerView);
        }

    }

    private void configureOnClickRecyclerView(RecyclerView recyclerView) {
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        mTouchHelper.startDrag(viewHolder);
    }
}
