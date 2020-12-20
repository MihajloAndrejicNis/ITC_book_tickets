package com.example.rezervacijakarata.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rezervacijakarata.R;
import com.example.rezervacijakarata.datamodels.Event;
import com.example.rezervacijakarata.listeners.OnEventClickListener;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> events;
    private Context context;
    private OnEventClickListener onEventClickListener;

    public void setOnEventClickListener(OnEventClickListener onEventClickListener) {
        this.onEventClickListener = onEventClickListener;
    }

    public EventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);

        return new ViewHolder(view, onEventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.pair.setText(events.get(position).getTeams().get(0).getName() + "   vs   " +
                events.get(position).getTeams().get(1).getName());
        holder.time.setText(events.get(position).getStart());
        holder.time_end.setText(events.get(position).getEnd());
        holder.type.setText(events.get(position).getSport());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pair;
        TextView time;
        TextView time_end;
        TextView type;

        public ViewHolder(@NonNull View itemView, OnEventClickListener onEventClickListener) {
            super(itemView);

            pair = itemView.findViewById(R.id.pair);
            time = itemView.findViewById(R.id.time);
            type = itemView.findViewById(R.id.type);
            time_end = itemView.findViewById(R.id.time_end);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEventClickListener.onClick(events.get(getAdapterPosition()).getId());
                }
            });
        }
    }
}
