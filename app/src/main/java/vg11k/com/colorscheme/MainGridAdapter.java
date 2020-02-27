package vg11k.com.colorscheme;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vg11k.com.colorscheme.colorConverterTool.ColorConverterToolActivity;
import vg11k.com.colorscheme.colorPicker.ColorPickerItemFragment;
import vg11k.com.colorscheme.grid.GridSchemeActivity;
import vg11k.com.colorscheme.menus.MenuGenerique;
import vg11k.com.colorscheme.schemeGenerator.SchemeGeneratorActivity;
import vg11k.com.colorscheme.userEditor.UserEditorActivity;

/**
 * Created by Julien on 26/02/2020.
 */

public class MainGridAdapter
            extends RecyclerView.Adapter<MainGridAdapter.ViewHolder> {

        private final MainListActivity mParentActivity;
        private final List<MenuGenerique> mValues;
        private final boolean mTwoPane;
        DataProvider m_provider;

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
                    if(ColorConverterToolActivity.ACTIVITY_TITLE.equals(feature.getContent())) {
                        mParentActivity.startNewActivity(ColorConverterToolActivity.ACTIVITY_FEATURE_ID);
                    }
                    else if(GridSchemeActivity.ACTIVITY_TITLE.equals(feature.getContent())) {
                        mParentActivity.startNewActivity(GridSchemeActivity.ACTIVITY_FEATURE_ID);
                    }
                    else {
                        Snackbar.make(view, mParentActivity.getResources().getString(R.string.feature_not_developped_yet), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        };

        MainGridAdapter(MainListActivity parent,
                        List<MenuGenerique> features,
                        boolean twoPane,
                        DataProvider provider) {
            mValues = features;
            mParentActivity = parent;
            mTwoPane = twoPane;
            m_provider = provider;
        }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.mIdView.setText(mValues.get(position).getId());
        holder.mContentView.setText(mValues.get(position).getContent());

        if(ColorConverterToolActivity.ACTIVITY_TITLE.equals(mValues.get(position).getContent())) {
            holder.mImageView.setImageDrawable(mParentActivity.getResources().getDrawable(R.drawable.translate_black_48dp));
        }
        if(GridSchemeActivity.ACTIVITY_TITLE.equals(mValues.get(position).getContent())) {
            holder.mImageView.setImageDrawable(mParentActivity.getResources().getDrawable(R.drawable.color_lens_black_48dp));
        }
        else if(UserEditorActivity.ACTIVITY_TITLE.equals(mValues.get(position).getContent())) {
            holder.mImageView.setImageDrawable(mParentActivity.getResources().getDrawable(R.drawable.account_box_gray_48dp));
        }
        else if("Online".equals(mValues.get(position).getContent())) {
            holder.mImageView.setImageDrawable(mParentActivity.getResources().getDrawable(R.drawable.cloud_circle_gray_48dp));
        }

        holder.mImageView.getLayoutParams().width = 150;
        holder.mImageView.getLayoutParams().height = 150;

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView mImageView;
        //final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            //mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.imageIcon);
        }
    }
}
