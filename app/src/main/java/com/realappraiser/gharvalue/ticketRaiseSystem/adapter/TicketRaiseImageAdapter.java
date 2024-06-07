package com.realappraiser.gharvalue.ticketRaiseSystem.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.model.GetPhoto;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.utils.Singleton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class TicketRaiseImageAdapter extends RecyclerView.Adapter<TicketRaiseImageAdapter.ViewHolder> {

    ArrayList<GetPhoto> getPhotoList = new ArrayList<>();
    public static ArrayList<GetPhoto> getPhotoList_response = new ArrayList<>();
    private Context context;
    private General general;


    public TicketRaiseImageAdapter(General general, Context context, ArrayList<GetPhoto> list_) {
        this.context = context;
        this.getPhotoList = list_;
        this.general = general;
    }
    
    @NonNull
    @Override
    public TicketRaiseImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_imageadapter, parent, false);
        return new TicketRaiseImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketRaiseImageAdapter.ViewHolder holder, int position) {


        holder.edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence txt, int start, int before, int count) {
                if (!general.isEmpty(txt.toString())) {
                    if (position != 0) {
                        // TODO - save the title directly to getPhotoList_response
                        getPhotoList.get(position).setTitle(txt.toString().trim());
                    }
                } else {
                    if(getPhotoList.get(position)!=null)
                        getPhotoList.get(position).setTitle("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        holder.edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // hit_photo_api > true
                //  Singleton.getInstance().hit_photo_api = true;
            }
        });


       holder.close.setOnClickListener(
               new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       try{
                           getPhotoList.remove(position);
                           notifyDataSetChanged();
                       }catch (Exception e){
                           e.getMessage();
                       }
                   }
               }
       );

        if (position == 0 || position == 1) {
            holder.imagedefaultlayout.setVisibility(View.VISIBLE);
            if (position == 0)
                holder.addimage.setText("ADD CAMERA IMAGE");
            if (position == 1)
                holder.addimage.setText("ADD GALLERY IMAGE");
        } else {
            holder.imagedefaultlayout.setVisibility(View.GONE);
            if (getPhotoList.size() > 0) {
                holder.txtvalue.setVisibility(View.VISIBLE);
                
                if(getPhotoList.get(position).getTitle() != null){
                       if (!general.isEmpty(getPhotoList.get(position).getTitle())) {
                    holder.edit.setText(getPhotoList.get(position).getTitle());
                } else 
                    holder.edit.setText("");
                
                }else 
                    holder.edit.setText("");

                final String getLogoo = getPhotoList.get(position).getLogo();
                if (!general.isEmpty(getLogoo)) {

                    byte[] decodedString = Base64.decode(getLogoo, Base64.DEFAULT);
                    final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    // set Bitmap into image view
                    if (decodedByte != null)
                        Glide.with(context).asBitmap().centerCrop().load(stream.toByteArray()).into(holder.imageitem);
                }
            } else {
                holder.txtvalue.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getPhotoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageitem, close;
        private LinearLayout imagedefaultlayout, txtvalue;
        private EditText edit;
        private TextView addimage;

        public ViewHolder(View itemView) {
            super(itemView);
            imageitem = (ImageView) itemView.findViewById(R.id.imageitem);
            imagedefaultlayout = (LinearLayout) itemView.findViewById(R.id.imagedefaultlayout);
            txtvalue = (LinearLayout) itemView.findViewById(R.id.txtvalue);
            edit = (EditText) itemView.findViewById(R.id.text);
            close = (ImageView) itemView.findViewById(R.id.close);
            addimage = (TextView) itemView.findViewById(R.id.addimage);
        }
    }


    public void setphoto_adapter(ArrayList<GetPhoto> list_is) {
        this.getPhotoList = list_is;
        Log.e("getPhotoList", "int: " + getPhotoList.size());
        notifyDataSetChanged();
    }


    public static interface ClickListener {
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
}
