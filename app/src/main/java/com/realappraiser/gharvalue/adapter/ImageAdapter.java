package com.realappraiser.gharvalue.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;

import java.util.ArrayList;

/**
 * @author Paresh Mayani (@pareshmayani)
 */
@SuppressWarnings("ALL")
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private ArrayList<String> mImagesList;
    private Context mContext;
    private SparseBooleanArray mSparseBooleanArray;
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private int selectedPhotoSize;
    private boolean photolanlat;

    public ImageAdapter(Context context, ArrayList<String> imageList,int selectedPhotoSize,boolean photolanlat) {
        mContext = context;
        mSparseBooleanArray = new SparseBooleanArray();
        mImagesList = new ArrayList<String>();
        this.mImagesList = imageList;
        this.selectedPhotoSize = selectedPhotoSize;
        this.photolanlat = photolanlat;
    }

    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArry = new ArrayList<String>();
        for (int i = 0; i < mImagesList.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                mTempArry.add(mImagesList.get(i));
            }
        }

        Log.e(TAG, "getCheckedItems: "+new Gson().toJson(mTempArry));
        return mTempArry;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(photolanlat){
                photoLanlatImage(buttonView, isChecked);
            }else{
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }

        }
    };

    private void photoLanlatImage(CompoundButton buttonView, boolean isChecked){
        int numOfSelectedPhoto = selectedPhotoSize + getCheckedItems().size();
        if ( numOfSelectedPhoto <= 5) {
            buttonView.setChecked(isChecked);
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
        } else {
            buttonView.setChecked(false);
            mSparseBooleanArray.put((Integer) buttonView.getTag(), false);
            if ( numOfSelectedPhoto >= 6) {
                Toast.makeText(buttonView.getContext(), "Please Upload Only 6 Pictures", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_multiphoto_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String imageUrl = mImagesList.get(position);


        if (imageUrl.contains(".docx") || imageUrl.contains("doc") || imageUrl.contains("docx") || imageUrl.contains(".docs")) {
            Glide.with(mContext)
                    .load(R.drawable.doc)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.doc)
                    .error(R.drawable.ic_doc)
                    .into(holder.imageView);
            holder.imageView.setAlpha(0.5f);
        } else if (imageUrl.contains("pdf")) {
            Glide.with(mContext)
                    .load(R.drawable.pdf)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.pdf)
                    .error(R.drawable.ic_pdf)
                    .into(holder.imageView);
            holder.imageView.setAlpha(0.5f);
        } else
            Glide.with(mContext)
                    .load("file://" + imageUrl)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.drawable.holder)
                    .error(R.drawable.holder)
                    .into(holder.imageView);

        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(mSparseBooleanArray.get(position));
        holder.checkBox.setOnCheckedChangeListener(mCheckedChangeListener);

        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        holder.tvFileName.setText(filename);
    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox;
        public ImageView imageView;
        public RelativeLayout parentImageLay;
        public TextView tvFileName;


        public MyViewHolder(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
            imageView = (ImageView) view.findViewById(R.id.imageView1);
            parentImageLay = (RelativeLayout) view.findViewById(R.id.parentImageLay);
            tvFileName = view.findViewById(R.id.tvFileName);
        }
    }

    private int getCheckedImage() {

        ArrayList<Boolean> mTempArry = new ArrayList<Boolean>();

        for (int i = 0; i < mSparseBooleanArray.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                mTempArry.add(mSparseBooleanArray.get(i));
            }
        }
        return mTempArry.size();
    }

}
