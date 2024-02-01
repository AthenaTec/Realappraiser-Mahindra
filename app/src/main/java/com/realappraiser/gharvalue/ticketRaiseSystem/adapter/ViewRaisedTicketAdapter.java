package com.realappraiser.gharvalue.ticketRaiseSystem.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.viewtickets.model.ViewTicketModel;

import java.util.List;

public class ViewRaisedTicketAdapter extends RecyclerView.Adapter<ViewRaisedTicketAdapter.ViewTicketHolder> {

    private Context context;

    private List<ViewTicketModel.Data> viewTicketData;

    private String ticketStatus = "";

    private ItemClickListener itemClickListener;

    public ViewRaisedTicketAdapter(List<ViewTicketModel.Data> viewTicketData, Context context, ItemClickListener itemClickListener) {
        this.viewTicketData = viewTicketData;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }


    public class ViewTicketHolder extends RecyclerView.ViewHolder {

        private TextView txt_ticket;
        private TextView ticketQuery;
        private TextView ticketStatus;
        private TextView ticketRaisedDate;
        private ImageView imgEdit;

        public ViewTicketHolder(@NonNull View itemView) {
            super(itemView);
            txt_ticket = (TextView) itemView.findViewById(R.id.txt_ticket);
            ticketQuery = (TextView) itemView.findViewById(R.id.txt_query);
            ticketStatus = (TextView) itemView.findViewById(R.id.txt_ticket_status);
            ticketRaisedDate = (TextView) itemView.findViewById(R.id.txt_ticket_raised_date);
            imgEdit = (ImageView) itemView.findViewById(R.id.txt_ticket_edit);

        }
    }

    @NonNull
    @Override
    public ViewRaisedTicketAdapter.ViewTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ticket, parent, false);
        return new ViewTicketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewRaisedTicketAdapter.ViewTicketHolder holder, int position) {

        holder.ticketQuery.setText(viewTicketData.get(position).getQuery());
        holder.txt_ticket.setText( "Ticket ID #"+viewTicketData.get(position).getTicketId() + "");
        holder.ticketStatus.setText(viewTicketData.get(position).getStatus());
        holder.imgEdit.setVisibility(View.GONE);
        String assigned_date = General.AssignedDate(viewTicketData.get(position).getDate());
        String assigned_time = General.AssignedTime(viewTicketData.get(position).getDate());

        holder.ticketRaisedDate.setText(assigned_date + " | " + assigned_time);
        holder.itemView.setOnClickListener(view -> {
            itemClickListener.onClick(position,viewTicketData);
        });
    }


    @Override
    public int getItemCount() {
        return viewTicketData.size();
    }

    public void updateAdapter(List<ViewTicketModel.Data> viewTicketData) {
        this.viewTicketData.clear();
        this.viewTicketData = viewTicketData;
        notifyDataSetChanged();

    }
}
