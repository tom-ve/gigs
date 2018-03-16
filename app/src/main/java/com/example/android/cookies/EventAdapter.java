package com.example.android.cookies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cookies.Entities.Event;
import com.example.android.cookies.Entities.EventType;

import java.util.List;

import static android.view.View.OnClickListener;

/**
 * {@link EventAdapter} exposes a list of concerts/events to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    private List<Event> eventData;
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

    public void setEventData(List<Event> eventData) {
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
        Event event = eventData.get(position);
        holder.mArtist.setText(event.getArtist());
        holder.mDisplayname.setText(event.getPerformance());
        if (EventType.CONCERT.equals(event.getType())) {
            holder.iconView.setImageResource(R.drawable.ic_concert);
        } else {
            holder.iconView.setImageResource(R.drawable.ic_festival);
        }
        holder.itemView.setTag(event.getId());
    }

    @Override
    public int getItemCount() {
        return eventData != null ? eventData.size() : 0;
    }


    /**
     * Cache of the children views for a event list item.
     */
    public class EventAdapterViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {

        public final TextView mArtist;
        public final ImageView iconView;
        public final TextView mDisplayname;

        public EventAdapterViewHolder(View itemView) {
            super(itemView);
            mArtist = itemView.findViewById(R.id.tv_artist);
            mDisplayname = itemView.findViewById(R.id.tv_display_name);
            iconView = itemView.findViewById(R.id.event_icon);
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
            Event event = eventData.get(adapterPosition);
            clickHandler.onClick(event);
        }
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface AdapterOnClickHandler {
        void onClick(Event event);
    }
}
