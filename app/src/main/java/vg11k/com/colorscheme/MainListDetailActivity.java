package vg11k.com.colorscheme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import vg11k.com.colorscheme.colorConverterTool.ColorConverterToolActivity;
import vg11k.com.colorscheme.colorPicker.ColorPickerItemFragment;
import vg11k.com.colorscheme.dummy.DummyFeatureDetailFragment;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MainListActivity}.
 */
public class MainListDetailActivity extends AppCompatActivity
        implements ColorPickerItemFragment.OnListFragmentInteractionListener{





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_list_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action ! PLOP", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

        /*Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(myChildToolbar);*/


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            /*arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_menu_detail_container, fragment)
                    .commit();*/

            if(getIntent().getStringExtra(DummyFeatureDetailFragment.FRAGMENT_FEATURE_ID) != null) {

                arguments.putString(DummyFeatureDetailFragment.FRAGMENT_FEATURE_ID,
                        getIntent().getStringExtra(DummyFeatureDetailFragment.FRAGMENT_FEATURE_ID));
                DummyFeatureDetailFragment fragment = new DummyFeatureDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_menu_detail_container, fragment)
                        .commit();
            }
            else if(getIntent().getStringExtra(ColorPickerItemFragment.FRAGMENT_FEATURE_ID) != null) {

                arguments.putString(ColorPickerItemFragment.FRAGMENT_FEATURE_ID,
                        getIntent().getStringExtra(ColorPickerItemFragment.FRAGMENT_FEATURE_ID));

                arguments.putParcelable(DataProvider.m_ID,
                        getIntent().getParcelableExtra(DataProvider.m_ID));

                ColorPickerItemFragment fragment = new ColorPickerItemFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_menu_detail_container, fragment)
                        .commit();
            }
            else if(getIntent().getStringExtra(ColorConverterToolActivity.ACTIVITY_FEATURE_ID) != null) {

                arguments.putString(ColorConverterToolActivity.ACTIVITY_FEATURE_ID,
                        getIntent().getStringExtra(ColorConverterToolActivity.ACTIVITY_FEATURE_ID));

                arguments.putParcelable(DataProvider.m_ID,
                        getIntent().getParcelableExtra(DataProvider.m_ID));

                Intent intent = new Intent(MainListDetailActivity.this, ColorConverterToolActivity.class);

                //ColorConverterToolActivity activity = new ColorConverterToolActivity();

                intent.putExtras(arguments);
                startActivity(intent);
                finish();

            }

        }

    }

    //https://stackoverflow.com/questions/8308695/how-to-add-options-menu-to-fragment-in-android
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home :

                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpTo(this, new Intent(this, MainListActivity.class));
                return true;
            /*case R.id.activity_menu_item:

            // Do Activity menu item stuff here
                return true;
            case R.id.fragment_menu_item:
                return false;*/
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListFragmentInteraction(ColorPickerLine item) {

    }



   public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


}
