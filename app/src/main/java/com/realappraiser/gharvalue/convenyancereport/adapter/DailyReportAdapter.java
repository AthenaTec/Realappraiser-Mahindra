package com.realappraiser.gharvalue.convenyancereport.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.model.DailyActivityData;
import com.realappraiser.gharvalue.utils.General;

import java.util.ArrayList;


public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.DailyReportViewHolder> {

    private ArrayList
            <DailyActivityData> dataModels;
    private final Activity mContext;

    private General general;

    public DailyReportAdapter(Activity mContext, ArrayList<DailyActivityData> dataModels){
        this.mContext = mContext;
        this.dataModels = dataModels;
        general = new General(mContext);
    }

    @NonNull
    @Override
    public DailyReportAdapter.DailyReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_report_adapter, parent, false);
        return new DailyReportAdapter.DailyReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyReportAdapter.DailyReportViewHolder holder, int position) {

        holder.activityType.setText(dataModels.get(position).getActivity());
        holder.addressTxt.setText(dataModels.get(position).getAddress());

        if(dataModels.get(position).getCaseId()!= null && !dataModels.get(position).getCaseId().isEmpty()){
            holder.llCase.setVisibility(View.VISIBLE);
            holder.caseTxt.setText(dataModels.get(position).getCaseId());
        }else{
            holder.llCase.setVisibility(View.GONE);
        }


        holder.latTxt.setText(""+dataModels.get(position).getLatitude());
        holder.longTxt.setText(""+dataModels.get(position).getLongitude());
        holder.timeTxt.setText(""+dataModels.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public  class DailyReportViewHolder extends RecyclerView.ViewHolder {

        TextView activityType;
        TextView caseTxt;
        TextView latTxt;
        TextView longTxt;
        TextView addressTxt;

        TextView timeTxt;

        LinearLayout llCase;
        public DailyReportViewHolder(View view){
            super(view);

             activityType = (TextView) view.findViewById(R.id.activity_type_txt);
             caseTxt = (TextView) view.findViewById(R.id.case_txt);
             llCase = (LinearLayout)view.findViewById(R.id.ll_case);
             latTxt = (TextView) view.findViewById(R.id.lat_txt);
             longTxt = (TextView) view.findViewById(R.id.long_txt);
             addressTxt = (TextView) view.findViewById(R.id.address_txt);
             timeTxt = (TextView) view.findViewById(R.id.time_txt);

            activityType.setTypeface(general.regulartypeface());
            caseTxt.setTypeface(general.regulartypeface());
            latTxt.setTypeface(general.regulartypeface());
            longTxt.setTypeface(general.regulartypeface());
            addressTxt.setTypeface(general.regulartypeface());
            timeTxt.setTypeface(general.regulartypeface());
        }
    }

}


