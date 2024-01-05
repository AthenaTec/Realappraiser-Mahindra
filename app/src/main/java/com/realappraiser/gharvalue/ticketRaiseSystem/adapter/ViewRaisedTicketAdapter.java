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
import com.realappraiser.gharvalue.activities.HomeActivity;
import com.realappraiser.gharvalue.viewtickets.model.ViewTicketData;

import java.util.List;

public class ViewRaisedTicketAdapter extends RecyclerView.Adapter<ViewRaisedTicketAdapter.ViewTicketHolder> {

    private Context context;

    private List<ViewTicketData> viewTicketData;

    public ViewRaisedTicketAdapter(List<ViewTicketData> viewTicketData, Context context){
        this.viewTicketData = viewTicketData;
        this.context = context;
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
            holder.ticketRaisedDate.setText(viewTicketData.get(position).getDate());
            holder.txt_ticket.setText(viewTicketData.get(position).getTicketID());
            holder.ticketStatus.setText(viewTicketData.get(position).getStatus());
            if(viewTicketData.get(position).isEdit()){
                holder.imgEdit.setVisibility(View.VISIBLE);
            }else{
                holder.imgEdit.setVisibility(View.GONE);
            }


            holder.imgEdit.setOnClickListener(view -> {
                updateTicketPopup();
            });
    }

    private void updateTicketPopup(){
        final Dialog dialog = new Dialog(context, R.style.raiseTicket);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.update_ticket_raise_system);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return viewTicketData.size();
    }
}
