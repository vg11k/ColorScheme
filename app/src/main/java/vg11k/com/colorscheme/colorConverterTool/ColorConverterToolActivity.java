package vg11k.com.colorscheme.colorConverterTool;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vg11k.com.colorscheme.ColorPickerLine;
import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.R;
import vg11k.com.colorscheme.colorPicker.ColorPickerItemFragment;
import vg11k.com.colorscheme.colorPicker.OnListFragmentInteractionListener;
import vg11k.com.colorscheme.utils.ItemClickSupport;

public class ColorConverterToolActivity extends AppCompatActivity
        implements OnListFragmentInteractionListener {

    public static final String ACTIVITY_TITLE = "Color converter tool";
    public static final String ACTIVITY_FEATURE_ID = "color_converter_tool";

    private int mColumnCount = 1;
    private List<ColorPickerLine> m_colorPickerLines;
    private DataProvider m_dataProvider;

    private OnListActivityInteractionListener mListener;

    private View m_view;

    private ColorConverterToolActivityAdapter m_adapter;
    private int m_selectedProvider = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_color_converter_tool);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle b = getIntent().getExtras();
        ArrayList<ColorPickerLine> tmpList = new ArrayList<ColorPickerLine>();
        m_colorPickerLines = new ArrayList<ColorPickerLine>();
        if (b != null) {

            m_dataProvider = b.getParcelable(DataProvider.m_ID);

            tmpList = new ArrayList<ColorPickerLine>();
            int nbLines = m_dataProvider.getLinesCount() - 1;//we don't count the header
            for (int i = 0; i < nbLines; i++) {

                String[] line = m_dataProvider.getLine(i + 1);
                tmpList.add(new ColorPickerLine(i, line));
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] colors = Arrays.copyOfRange(m_dataProvider.getLine(0), 1, m_dataProvider.getLine(0).length);
        toolbar.setTitle(colors[m_selectedProvider]);


        m_view = findViewById(R.id.color_converter_tool_content);
        Context context = m_view.getContext();


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
            }

            m_adapter = new ColorConverterToolActivityAdapter(m_colorPickerLines, mListener, m_dataProvider, context, m_view);
            recyclerView.setAdapter(m_adapter);

            for(ColorPickerLine line : tmpList) {
                m_colorPickerLines.add(line);
                m_adapter.bindViewHolder(
                        m_adapter.createViewHolder((ViewGroup)m_view,m_colorPickerLines.size() - 1),
                        m_colorPickerLines.size() - 1);
            }

            m_adapter.notifyDataSetChanged();


            configureOnClickRecyclerView(recyclerView);


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch_provider:
                switchProvider();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void switchProvider() {

        final String[] colors = Arrays.copyOfRange(m_dataProvider.getLine(0), 1, m_dataProvider.getLine(0).length);

        AlertDialog.Builder builder = new AlertDialog.Builder(findViewById(R.id.color_converter_tool).getContext());
        builder.setTitle("Choose the provider to use");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                m_selectedProvider = which;
                m_adapter.setSelectedProvider(m_selectedProvider);

                ((Toolbar)findViewById(R.id.toolbar)).setTitle(colors[m_selectedProvider]);
                m_adapter.notifyDataSetChanged();

                //Snackbar.make(findViewById(R.id.color_converter_tool), "Fct a developper", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

            }
        });
        builder.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_color_converter_tool, menu);
        return true;
    }

    private void configureOnClickRecyclerView(RecyclerView recyclerView) {
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_colorpickeritem)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        System.out.println("Element touche : " + position);

                        ColorPickerLine colorPicker = m_colorPickerLines.get(position);

                        AlertDialog alertDialog = new AlertDialog.Builder(ColorConverterToolActivity.this).create();
                        alertDialog.setTitle(colorPicker.getcolorRGB() + "\nAlternatives de " + colorPicker.getColorName(m_selectedProvider) );

                        final String[] colors = Arrays.copyOfRange(m_dataProvider.getLine(0), 1, m_dataProvider.getLine(0).length);
                        String messageContent ="";
                        for(int i = 0; i < colors.length; i++ ) {
                            if(m_selectedProvider != i) {
                                messageContent += colors[i] + " : ";
                                String alternativeName = "Pas d'alternative";
                                if(!colorPicker.getColorName(i).isEmpty()) {
                                    alternativeName = colorPicker.getColorName(i);
                                }
                                messageContent += alternativeName;
                                if(i + 1 < colors.length) {
                                    messageContent += "\n";
                                }
                            }
                        }


                        alertDialog.setMessage(messageContent);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }
                });
    }


    public interface OnListActivityInteractionListener {
        // TODO: Update argument type and name
        void onListActivityInteraction(ColorPickerLine item);
    }

    @Override
    public void onListFragmentInteraction(ColorPickerLine item) {

    }

}
