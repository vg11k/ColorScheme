package vg11k.com.colorscheme.grid;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vg11k.com.colorscheme.DataProvider;
import vg11k.com.colorscheme.R;
import vg11k.com.colorscheme.StorageKind;

/**
 * Created by Julien on 17/02/2020.
 */

public class GridSchemeFragmentAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context m_context;
    private final ArrayList<String> m_values;
    private ViewGroup mParent;
    private DataProvider m_dataProvider;
    private GridSchemeFragment.OnFragmentInteractionListener m_listenerToCallOnItemInteraction;
    private StorageKind m_storageKind;

    public GridSchemeFragmentAdapter(DataProvider dataProvider,
                                     Context context,
                                     ArrayList<String> strings,
                                     StorageKind storageKind,
                                     GridSchemeFragment.OnFragmentInteractionListener listener) {
        m_context = context;
        m_values = strings;
        m_dataProvider = dataProvider;
        m_listenerToCallOnItemInteraction = listener;
        m_storageKind = storageKind;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mParent = viewGroup;
        View view = LayoutInflater.from(m_context).inflate(R.layout.grid_scheme_item, mParent, false);
        GridItemViewHolder holder = new GridItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        final String s = m_values.get(i);
        GridItemViewHolder dummyHolder = (GridItemViewHolder) viewHolder;
        dummyHolder.mTextView.setText(s);

        if(i > 0) {

           /* String folderToLookat = StorageKind.LOCAL.getFullPath();//m_dataProvider.m_localFolderName;
            if(!m_listenerToCallOnItemInteraction.isLocalMode()) {
                folderToLookat = StorageKind.WEB.getFullPath();//m_dataProvider.m_webFolderName;
            }*/

            Bitmap bitmap = m_dataProvider.getBitmapByName(m_context, s, m_storageKind.getFullPath());//folderToLookat);
            if(bitmap != null) {
                ((GridItemViewHolder) viewHolder).mImageView.setImageBitmap(bitmap);
            }
        }

        ((GridItemViewHolder) viewHolder).mImageView.getLayoutParams().width = 150;
        ((GridItemViewHolder) viewHolder).mImageView.getLayoutParams().height = 150;

        dummyHolder.mImageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 int index = m_values.indexOf(s);
                m_listenerToCallOnItemInteraction.startGeneratorOnExistingScheme(index);

             }
         });

        if(i > 0) {
            dummyHolder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                    builder.setMessage(m_context.getResources().getString(R.string.delete_selected_scheme))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    int index = m_values.indexOf(s);

                                    m_values.remove(s);
                                    m_dataProvider.deleteFile(s + ".json", m_context);
                                    m_dataProvider.deleteFile(s + ".PNG", m_context);

                                    notifyItemRemoved(index);
                                }
                            })
                            .setNegativeButton(m_context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    builder.create().show();

                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return m_values.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class GridItemViewHolder extends RecyclerView.ViewHolder {

        final TextView mTextView;
        final ImageView mImageView;

        GridItemViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.content);
            mImageView = view.findViewById(R.id.mini_preview);
        }
    }
}
