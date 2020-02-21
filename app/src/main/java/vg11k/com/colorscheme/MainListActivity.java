package vg11k.com.colorscheme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
 * lead to a {@link MainListDetailActivity} representing
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

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, MenusContainer.FEATURESLIST, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final MainListActivity mParentActivity;
        private final List<MenuGenerique> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuGenerique feature = (MenuGenerique) view.getTag();

                if (mTwoPane) {

                    //TODO LATER
                    /*Bundle arguments = new Bundle();
                    arguments.putString(DummyFeatureDetailFragment.FRAGMENT_FEATURE_ID, feature.getId());
                    DummyFeatureDetailFragment fragment = new DummyFeatureDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_menu_detail_container, fragment)
                            .commit();*/
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MainListDetailActivity.class);



                    if(ColorPickerItemFragment.FRAGMENT_TITLE.equals(feature.getContent())) {
                        intent.putExtra(ColorPickerItemFragment.FRAGMENT_FEATURE_ID, feature.getId());
                    }
                    else if(ColorConverterToolActivity.ACTIVITY_TITLE.equals(feature.getContent())) {
                        intent.putExtra(ColorConverterToolActivity.ACTIVITY_FEATURE_ID, feature.getId());
                    }
                    /*else if(SchemeGeneratorActivity.ACTIVITY_TITLE.equals(feature.getContent())) {
                        intent.putExtra(SchemeGeneratorActivity.ACTIVITY_FEATURE_ID, feature.getId());
                    }
                    else if(SchemeGeneratorFragment.FRAGMENT_TITLE.equals(feature.getContent())) {
                        intent.putExtra(SchemeGeneratorFragment.FRAGMENT_FEATURE_ID, feature.getId());
                    }*/
                    else if(SchemeGeneratorActivity.ACTIVITY_TITLE.equals(feature.getContent())) {
                        intent.putExtra(SchemeGeneratorActivity.ACTIVITY_FEATURE_ID, feature.getId());
                    }
                    else if(GridSchemeActivity.ACTIVITY_TITLE.equals(feature.getContent())) {
                        intent.putExtra(GridSchemeActivity.ACTIVITY_FEATURE_ID, feature.getId());
                    }

                    intent.putExtra(DataProvider.m_ID, m_provider);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(MainListActivity parent,
                                      List<MenuGenerique> features,
                                      boolean twoPane) {
            mValues = features;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getId());
            holder.mContentView.setText(mValues.get(position).getContent());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    public void initializeData() {
        if(m_provider == null) {
            m_provider = new DataProvider(this);
        }
    }
}
