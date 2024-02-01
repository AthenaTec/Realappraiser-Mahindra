package com.realappraiser.gharvalue.ticketRaiseSystem.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.TicketImageModel;
import com.realappraiser.gharvalue.utils.General;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TicketUpdateImageAdapter extends RecyclerView.Adapter<TicketUpdateImageAdapter.ViewHolder> {

    List<TicketImageModel.Data> ticketImageModelData;
    Context context;
    General general;

    public TicketUpdateImageAdapter(Context context, List<TicketImageModel.Data> ticketImageModelData, General general) {
        this.context = context;
        this.ticketImageModelData = ticketImageModelData;
        this.general = general;
    }

    @NonNull
    @Override
    public TicketUpdateImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageclose, parent, false);
        return new TicketUpdateImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketUpdateImageAdapter.ViewHolder holder, int position) {

        holder.imageitem.setVisibility(View.VISIBLE);

        holder.close.setVisibility(View.GONE);

        holder.text.setVisibility(View.GONE);

                final String getLogoo = ticketImageModelData.get(position).getImage();
                if (!General.isEmpty(getLogoo)) {

                    byte[] decodedString = Base64.decode(getLogoo, Base64.DEFAULT);
                    final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    // set Bitmap into image view
                    if (decodedByte != null)
                        Glide.with(context).asBitmap().centerCrop().load(stream.toByteArray()).into(holder.imageitem);
                }
        }


    @Override
    public int getItemCount() {
        return ticketImageModelData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageitem,close;
        private EditText text;

        public ViewHolder(View itemView) {
            super(itemView);
            imageitem = (ImageView) itemView.findViewById(R.id.imageitem);
            close = (ImageView) itemView.findViewById(R.id.close);
            text = (EditText) itemView.findViewById(R.id.text);
        }
    }

    public void setphoto_adapter(ArrayList<TicketImageModel.Data> list_is) {
        this.ticketImageModelData = list_is;
        Log.e("getPhotoList", "int: " + ticketImageModelData.size());
        notifyDataSetChanged();
    }

    public static interface ClickListener {
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
}