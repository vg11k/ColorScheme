package vg11k.com.colorscheme.schemeGenerator;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.MainListActivity;
import vg11k.com.colorscheme.MainListDetailActivity;
import vg11k.com.colorscheme.R;
import vg11k.com.colorscheme.SchemeModel;
import vg11k.com.colorscheme.StorageKind;
import vg11k.com.colorscheme.colorPicker.ColorPickerItemFragment;
import vg11k.com.colorscheme.colorPicker.OnColorPickerItemFragmentInteractionListener;
import vg11k.com.colorscheme.grid.GridSchemeActivity;

public class SchemeGeneratorActivity extends AppCompatActivity
        implements SchemeGeneratorFragment.OnSchemeGeneratorFragmentInteractionListener,
        OnColorPickerItemFragmentInteractionListener {


    public static final String ACTIVITY_TITLE = "Scheme generator2";
    public static final String ACTIVITY_FEATURE_ID = "scheme_generator_tool2";

    private FloatingActionButton m_fab;

    private SchemeGeneratorFragment m_generatorFragment;
    private ColorPickerItemFragment m_pickerFragment;

    boolean m_isPickerFragmentDisplayed;
    private DataProvider m_dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scheme_generator);

        //getSupportActionBar().hide();


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        m_fab = (FloatingActionButton) findViewById(R.id.fab);
        m_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action ! PLOP", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            //if(getIntent().getStringExtra(SchemeGeneratorFragment.FRAGMENT_FEATURE_ID) != null) {

            arguments.putString(SchemeGeneratorFragment.FRAGMENT_FEATURE_ID,
                    getIntent().getStringExtra(SchemeGeneratorFragment.FRAGMENT_FEATURE_ID));

            Parcelable editedSchemeParcel = getIntent().getParcelableExtra(SchemeModel.m_ID);
            if(editedSchemeParcel != null) {
                arguments.putParcelable(SchemeModel.m_ID, editedSchemeParcel);
            }

            m_isPickerFragmentDisplayed = false;

            m_dataProvider = getIntent().getParcelableExtra(DataProvider.m_ID);

            arguments.putParcelable(DataProvider.m_ID,m_dataProvider );

            m_generatorFragment = new SchemeGeneratorFragment();
            m_generatorFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_menu_detail_container, m_generatorFragment, "generator")
                    .addToBackStack(null)
                    .commit();
            //}
        }
        else {
            //RESTORE THE FRAGMENTS INSTANCE

            System.out.println("Should restore the generator frag here");


            if (!m_generatorFragment.isInLayout()) {

                m_isPickerFragmentDisplayed = false;

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, m_generatorFragment, "generator")
                        .commit();
            }

        }
    }

    @Override
    public void onSchemeGeneratorFragmentInteraction(Uri uri) {

        Thread.yield();
    }

    @Override
    public void requestColorPickerFragment() {
        // Create fragment and give it an argument specifying the article it should show
        ColorPickerItemFragment newFragment = new ColorPickerItemFragment();
        Bundle args = new Bundle();
        //args.putInt(ColorPickerItemFragment.ARG_POSITION, position);


        args.putParcelable(DataProvider.m_ID,
                getIntent().getParcelableExtra(DataProvider.m_ID));
        newFragment.setArguments(args);

        m_isPickerFragmentDisplayed = true;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.main_menu_detail_container, newFragment, "picker");
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof SchemeGeneratorFragment) {
            ((SchemeGeneratorFragment) fragment).setFragmentListener(this);
        }
        else if(fragment instanceof ColorPickerItemFragment) {
            ((ColorPickerItemFragment) fragment).setFragmentListener(this);
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

            case R.id.action_save_scheme :


                askToSaveData();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scheme_generator, menu);

        menu.findItem(R.id.action_switch_provider).setVisible(false);
        menu.findItem(R.id.action_save_scheme).setVisible(true);
        return true;
    }

    @Override
    public FloatingActionButton getFab() {
        return m_fab;
    }

    @Override
    public void onColorPickerListFragmentInteraction(List<ColorPickerLine> items) {

        SchemeGeneratorFragment generatorFrag =
                (SchemeGeneratorFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.scheme_generator_fragment_content);
        if(generatorFrag != null) {
            //this is a tablet with two panes
            //generatorFrag.updateView(items);
        }
        else {
            //one pane layout, we swap between fragments

            ArrayList<Integer> indexes = new ArrayList<Integer>();
            ArrayList<Integer> providerIndexes = new ArrayList<Integer>();

            for(ColorPickerLine color : items) {
                indexes.add(color.getId());
                providerIndexes.add(color.getProviderIndex());
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_menu_detail_container, m_generatorFragment, "generator");

            transaction.addToBackStack(null);
            transaction.commit();

            m_isPickerFragmentDisplayed = false;

            m_generatorFragment.addColors(indexes, providerIndexes);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "generator", m_generatorFragment);
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();


        //NavUtils.navigateUpTo(this, new Intent(this, MainListActivity.class));
        //NavUtils.navigateUpFromSameTask(this);

        //screw the fragmentManager stack navigation, i can't make it work properly
        if(m_isPickerFragmentDisplayed){
            getFragmentManager().popBackStack();
            m_isPickerFragmentDisplayed = false;

            m_pickerFragment = null;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_menu_detail_container, m_generatorFragment, "generator");

            transaction.addToBackStack(null);
            transaction.commit();

        }
        else {

            Bundle arguments = new Bundle();
            arguments.putString(GridSchemeActivity.ACTIVITY_FEATURE_ID,
                    getIntent().getStringExtra(GridSchemeActivity.ACTIVITY_FEATURE_ID));

            arguments.putParcelable(DataProvider.m_ID,
                    getIntent().getParcelableExtra(DataProvider.m_ID));

            arguments.putInt(StorageKind.m_ID, StorageKind.LOCAL.getValue());

            Intent intent = new Intent(SchemeGeneratorActivity.this, GridSchemeActivity.class);

            intent.putExtras(arguments);
            startActivity(intent);
            finish();

            /*FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStackImmediate();
            super.onBackPressed();*/
            //NavUtils.navigateUpFromSameTask(this);//get back to parent activity
        }

    }

    public void askToSaveData() {


        AlertDialog.Builder builder = new AlertDialog.Builder(m_generatorFragment.getContext());
        builder.setTitle(getResources().getString(R.string.enter_scheme_name));

        final EditText input = new EditText(m_generatorFragment.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = input.getText().toString();

                if(!inputText.isEmpty()) {


                        //TODO generate json & copy image bitmap

                        if(m_dataProvider != null) {

                            ArrayList<AbstractSchemeGeneratorLineModel> listWithoutButton = new ArrayList<AbstractSchemeGeneratorLineModel>();
                            listWithoutButton.addAll(m_generatorFragment.getValues());
                            listWithoutButton.remove(listWithoutButton.size() - 1);
                            SchemeModel scheme = new SchemeModel(inputText, listWithoutButton);
                            m_dataProvider.persistSchemeGeneratorData(m_generatorFragment.getContext(), scheme);
                            Snackbar.make(m_generatorFragment.getView(), getResources().getString(R.string.save_done), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }



                }
                else {
                    Snackbar.make(m_generatorFragment.getView(), getResources().getString(R.string.save_canceled), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Snackbar.make(m_generatorFragment.getView(), getResources().getString(R.string.save_canceled), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        builder.show();
    }

}
