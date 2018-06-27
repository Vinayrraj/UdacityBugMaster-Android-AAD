package com.google.developer.bugmaster.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.bugmaster.R;

/**
 * RecyclerView adapter extended with project-specific required methods.
 */

public class InsectRecyclerAdapter extends
        RecyclerView.Adapter<InsectRecyclerAdapter.InsectHolder> {

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;
    private Cursor mCursor;
    private InsectRecyclerAdapterOnClickHandler mClickHandler;

    public interface InsectRecyclerAdapterOnClickHandler {
        void onItemClick(int id);
    }

    public InsectRecyclerAdapter(@NonNull Context context, InsectRecyclerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }


    @Override
    public InsectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_insect, parent, false);
        return new InsectHolder(view);
    }

    @Override
    public void onBindViewHolder(InsectHolder holder, int position) {
        mCursor.moveToPosition(position);


    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    /**
     * Return the {@link Insect} represented by this item in the adapter.
     *
     * @param position Adapter item position.
     * @return A new {@link Insect} filled with this position's attributes
     * @throws IllegalArgumentException if position is out of the adapter's bounds.
     */
    public Insect getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor.moveToPosition(position)) {
            return new Insect(mCursor);
        }
        return null;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /* ViewHolder for each insect item */
    public class InsectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        final ImageView iconView;
//
//        final TextView dateView;

        public InsectHolder(View view) {
            super(view);
//            iconView = (ImageView) view.findViewById(R.id.weather_icon);
//            dateView = (TextView) view.findViewById(R.id.date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int id = mCursor.getInt(InsectContract.ProjectionIndex.INDEX_ID);
            mClickHandler.onItemClick(id);
        }
    }
}
