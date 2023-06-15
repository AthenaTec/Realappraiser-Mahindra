package com.realappraiser.gharvalue.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.model.PropertyDocModel;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PropertyDocumentNewFileAdapter extends RecyclerView.Adapter<PropertyDocumentNewFileAdapter.Holder> {

    private ArrayList<PropertyDocModel> arrayList;
    private Activity mContext;
    private General general;

    public PropertyDocumentNewFileAdapter(ArrayList<PropertyDocModel> arrayList, Activity mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        general = new General(mContext);
    }

    @Override
    public int getItemCount() {
        if (arrayList == null)
            return 0;
        else
            return arrayList.size();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_doc_listitems, parent, false);
        Holder viewHolder = new Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        PropertyDocModel model = arrayList.get(position);

        holder.tvFileName.setText(model.getFilleName());

        if (model.getFilleName().contains(".docx") ||
                model.getFilleName().contains(".doc") ||
                model.getFilleName().contains(".docs")) {
            Glide.with(mContext)
                    .load(R.drawable.doc)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.doc)
                    .error(R.drawable.ic_doc)
                    .into(holder.imgPreview);
        } else if (model.getFilleName().contains("pdf")) {
            Glide.with(mContext)
                    .load(R.drawable.pdf)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.pdf)
                    .error(R.drawable.ic_pdf)
                    .into(holder.imgPreview);
        } else
            Glide.with(mContext)
                    .load("file://" + model.getFilePath())
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.drawable.holder)
                    .error(R.drawable.holder)
                    .into(holder.imgPreview);

        holder.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreview(model);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    private void showPreview(PropertyDocModel model) {
        if (model.getFilleName().contains("jpg") || model.getFilleName().contains("jpeg") ||
                model.getFilleName().contains("png") || model.getFilleName().contains("PNG") ||
                model.getFilleName().contains("JPEG") || model.getFilleName().contains("JPG")) {
            imagePreview(model);
        } else if (model.getFilleName().contains("pdf")) {
            String tittle = model.getFilleName();
            String ps2 = model.getFile();
            byte[] byteData = Base64.decode(ps2, Base64.DEFAULT);
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            } else {
                directory = new File(String.valueOf(mContext.getExternalFilesDir("")));
            }
            File mypath = new File(directory, tittle);


            BufferedOutputStream writer = null;
            try {
                writer = new BufferedOutputStream(new FileOutputStream(mypath));
                writer.write(byteData);
                writer.flush();
                writer.close();

                File newPath = new File(directory, tittle);

                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                Uri path = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    path = Uri.fromFile(mypath);
                } else {
                    path = FileProvider.getUriForFile(mContext, SettingsUtils.FILE_PROVIDER, mypath);
                }
                if (path != null) {
                    pdfOpenintent.setDataAndType(path, "application/pdf");
                }
                pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                pdfOpenintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    mContext.startActivity(pdfOpenintent);
                } catch (ActivityNotFoundException e) {
                    callAlert();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (model.getFilleName().contains("docx")) {
            String tittle = model.getFilleName();
            String ps2 = model.getFile();
            byte[] byteData = Base64.decode(ps2, Base64.DEFAULT);
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            } else {
                directory = new File(String.valueOf(mContext.getExternalFilesDir("")));
            }
            File mypath = new File(directory, tittle);

            Uri newpath = Uri.parse(directory + "/" + tittle);

            BufferedOutputStream writer = null;
            try {
                writer = new BufferedOutputStream(new FileOutputStream(mypath));
                writer.write(byteData);
                writer.flush();
                writer.close();

                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                Uri path = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    path = Uri.fromFile(mypath);
                } else {
                    path = FileProvider.getUriForFile(mContext, SettingsUtils.FILE_PROVIDER, mypath);
                }

                if (path != null) {
                    pdfOpenintent.setDataAndType(path, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                }
                pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                pdfOpenintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    mContext.startActivity(pdfOpenintent);
                } catch (ActivityNotFoundException e) {
                    callAlert();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (model.getFilleName().contains("doc")) {
            String tittle = model.getFilleName();
            String ps2 = model.getFile();
            byte[] byteData = Base64.decode(ps2, Base64.DEFAULT);
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            } else {
                directory = new File(String.valueOf(mContext.getExternalFilesDir("")));
            }
            File mypath = new File(directory, tittle);

            Uri newpath = Uri.parse(directory + "/" + tittle);

            BufferedOutputStream writer = null;
            try {
                writer = new BufferedOutputStream(new FileOutputStream(mypath));
                writer.write(byteData);
                writer.flush();
                writer.close();

                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                Uri path = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    path = Uri.fromFile(mypath);
                } else {
                    path = FileProvider.getUriForFile(mContext, SettingsUtils.FILE_PROVIDER, mypath);
                }
                if (path != null) {
                    pdfOpenintent.setDataAndType(path, "application/msword");
                }
                pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                pdfOpenintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    mContext.startActivity(pdfOpenintent);
                } catch (ActivityNotFoundException e) {
                    callAlert();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void callAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
        builder.setMessage(mContext.getResources().getString(R.string.document_alert))
                .setCancelable(false)
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void imagePreview(PropertyDocModel model) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.document_reader);
        TextView popuptitle = (TextView) dialog.findViewById(R.id.title);
        TextView titletext = (TextView) dialog.findViewById(R.id.titletext);
        ImageView document_image = (ImageView) dialog.findViewById(R.id.document_read_image);
        Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
        Button noBtn = (Button) dialog.findViewById(R.id.noBtn);
        yesBtn.setTypeface(general.mediumtypeface());
        noBtn.setTypeface(general.mediumtypeface());
        popuptitle.setTypeface(general.mediumtypeface());
        titletext.setTypeface(general.mediumtypeface());
        popuptitle.setText("Image Viewer for the Case Id: " + model.getCaseId());
        titletext.setText("");

        Glide.with(mContext)
                .load("file://" + model.getFilePath())
                .skipMemoryCache(true)
                .centerCrop()
                .placeholder(R.drawable.holder)
                .error(R.drawable.holder)
                .into(document_image);

        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(document_image);
        pAttacher.update();

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvFileName)
        TextView tvFileName;
        @BindView(R.id.imgPreview)
        ImageView imgPreview;
        @BindView(R.id.imgDelete)
        ImageView imgDelete;
        @BindView(R.id.llView)
        CardView llView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
