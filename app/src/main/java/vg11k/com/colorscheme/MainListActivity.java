package vg11k.com.colorscheme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vg11k.com.colorscheme.colorConverterTool.ColorConverterToolActivity;
import vg11k.com.colorscheme.colorPicker.ColorPickerItemFragment;
import vg11k.com.colorscheme.grid.GridSchemeActivity;
import vg11k.com.colorscheme.menus.MenuGenerique;
import vg11k.com.colorscheme.menus.MenusContainer;
import vg11k.com.colorscheme.schemeGenerator.SchemeGeneratorActivity;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link } representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MainListActivity extends AppCompatActivity {

    //MAIN_ACTIVITY

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    static private DataProvider m_provider = null;
    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        initializeData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //turned invisible in the xml layout
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if (findViewById(R.id.main_menu_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mainView = findViewById(R.id.item_list);
        assert mainView != null;

        final RecyclerView recyclerView = (RecyclerView) mainView;
        GridLayoutManager manager = new GridLayoutManager(mainView.getContext(), 2);
        recyclerView.setLayoutManager(manager);

        ((RecyclerView)mainView).setAdapter(new MainGridAdapter(this, MenusContainer.FEATURESLIST, mTwoPane, m_provider));
    }


    public void initializeData() {
        if(m_provider == null) {
            m_provider = new DataProvider(this);
        }
    }

    public void startNewActivity (String featureId) {

        if(GridSchemeActivity.ACTIVITY_FEATURE_ID.equals(featureId)) {

            Bundle arguments = new Bundle();
            arguments.putString(GridSchemeActivity.ACTIVITY_FEATURE_ID,
                    getIntent().getStringExtra(GridSchemeActivity.ACTIVITY_FEATURE_ID));

            arguments.putParcelable(DataProvider.m_ID,
                    getIntent().getParcelableExtra(DataProvider.m_ID));

            arguments.putInt(StorageKind.m_ID, StorageKind.LOCAL.getValue());



            Intent intent = new Intent(MainListActivity.this, GridSchemeActivity.class);

            intent.putExtras(arguments);
            intent.putExtra(DataProvider.m_ID, m_provider);
            startActivity(intent);
        }
        else if(ColorConverterToolActivity.ACTIVITY_FEATURE_ID.equals(featureId)) {

            Bundle arguments = new Bundle();
            arguments.putString(ColorConverterToolActivity.ACTIVITY_FEATURE_ID,
                    getIntent().getStringExtra(ColorConverterToolActivity.ACTIVITY_FEATURE_ID));

            arguments.putParcelable(DataProvider.m_ID,
                    getIntent().getParcelableExtra(DataProvider.m_ID));

            Intent intent = new Intent(MainListActivity.this, ColorConverterToolActivity.class);

            //ColorConverterToolActivity activity = new ColorConverterToolActivity();

            intent.putExtras(arguments);
            intent.putExtra(DataProvider.m_ID, m_provider);
            startActivity(intent);
        }



        /*
         arguments.putString(ColorConverterToolActivity.ACTIVITY_FEATURE_ID,
                        getIntent().getStringExtra(ColorConverterToolActivity.ACTIVITY_FEATURE_ID));

                arguments.putParcelable(DataProvider.m_ID,
                        getIntent().getParcelableExtra(DataProvider.m_ID));

                Intent intent = new Intent(MainListDetailActivity.this, ColorConverterToolActivity.class);

                //ColorConverterToolActivity activity = new ColorConverterToolActivity();

                intent.putExtras(arguments);
                startActivity(intent);
         */
    }
}
