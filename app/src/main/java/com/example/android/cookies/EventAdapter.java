package com.example.android.cookies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.view.View.*;

/**
 * {@link EventAdapter} exposes a list of concerts/events to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    private String[] eventData;
    private final AdapterOnClickHandler clickHandler;

    /**
     * Creates a EventAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public EventAdapter(AdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void setEventData(String[] eventData) {
        this.eventData = eventData;
        notifyDataSetChanged();
    }

    @Override
    public EventAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.event_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new EventAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventAdapterViewHolder holder, int position) {
        holder.mTextView.setText(eventData[position]);
    }

    @Override
    public int getItemCount() {
        return eventData != null ? eventData.length : 0;
    }


    /**
     * Cache of the children views for a event list item.
     */
    public class EventAdapterViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {

        public final TextView mTextView;

        public EventAdapterViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_artist);
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param view The View that was clicked
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String event = eventData[adapterPosition];
            clickHandler.onClick(event);
        }
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface AdapterOnClickHandler {
        void onClick(String event);
    }
}
