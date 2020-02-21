package vg11k.com.colorscheme.grid;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.R;
import vg11k.com.colorscheme.SchemeModel;
import vg11k.com.colorscheme.StorageKind;
import vg11k.com.colorscheme.schemeGenerator.SchemeGeneratorActivity;

public class GridSchemeActivity
        extends AppCompatActivity
        implements GridSchemeFragment.OnFragmentInteractionListener {

    public static final String ACTIVITY_TITLE = "Grid scheme";
    public static final String ACTIVITY_FEATURE_ID = "grid_scheme_tool";

    private GridSchemeFragment m_gridFragment;
    private DataProvider m_dataProvider;
    private StorageKind m_storageKind;

    private boolean m_isLocal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_scheme);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            arguments.putString(GridSchemeFragment.FRAGMENT_FEATURE_ID,
                    getIntent().getStringExtra(GridSchemeFragment.FRAGMENT_FEATURE_ID));

            m_storageKind = StorageKind.valueOf(getIntent().getIntExtra(StorageKind.m_ID, 1));
            arguments.putInt(StorageKind.m_ID, m_storageKind.getValue());

            m_dataProvider = getIntent().getParcelableExtra(DataProvider.m_ID);
            arguments.putParcelable(DataProvider.m_ID, m_dataProvider
                    );

            m_gridFragment = new GridSchemeFragment();

            //startActivityListener

            //m_gridFragment.addStartActivityListener(startActivityListener);

            m_gridFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_menu_detail_container, m_gridFragment, "grid")
                    .addToBackStack(null)
                    .commit();

        }
        else {
            //RESTORE THE FRAGMENTS INSTANCE
            System.out.println("Should restore the grid frag here");
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof GridSchemeFragment) {
            ((GridSchemeFragment) fragment).setFragmentListener(this);
        }
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();

        //NavUtils.navigateUpTo(this, new Intent(this, MainListActivity.class));
        //NavUtils.navigateUpFromSameTask(this);

        //screw the fragmentManager stack navigation, i can't make it work properly
        NavUtils.navigateUpFromSameTask(this);//get back to parent activity
    }

    public void startGeneratorOnExistingScheme(int selectedSchemeIndexToEdit) {

        Bundle arguments = new Bundle();
        arguments.putString(SchemeGeneratorActivity.ACTIVITY_FEATURE_ID,
                SchemeGeneratorActivity.ACTIVITY_TITLE);

        //0 is the add new Scheme
        if(selectedSchemeIndexToEdit > 0) {
            SchemeModel schemeModel = m_dataProvider.getSchemeGeneratorsData(this, m_gridFragment.getNameOfItem(selectedSchemeIndexToEdit));
            arguments.putParcelable(SchemeModel.m_ID, schemeModel);
        }

        arguments.putParcelable(DataProvider.m_ID, m_dataProvider);

        Intent intent = new Intent(GridSchemeActivity.this, SchemeGeneratorActivity.class);

        intent.putExtras(arguments);
        startActivity(intent);//reset the APP. WTF
        finish();
    }

}
