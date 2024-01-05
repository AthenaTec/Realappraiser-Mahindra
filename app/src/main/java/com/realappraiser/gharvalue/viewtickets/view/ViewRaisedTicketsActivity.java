package com.realappraiser.gharvalue.viewtickets.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.activities.HomeActivity;
import com.realappraiser.gharvalue.activities.LoginActivity;
import com.realappraiser.gharvalue.activities.PhotoLatLngTab;
import com.realappraiser.gharvalue.adapter.OpenCaseAdapter;
import com.realappraiser.gharvalue.ticketRaiseSystem.adapter.ViewRaisedTicketAdapter;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.viewtickets.model.ViewTicketData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewRaisedTicketsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_view_raised_ticket)
    RecyclerView rvViewRaisedTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_raised_tickets);
        ButterKnife.bind(this);
        initToolBar();
        initAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.logout);
        MenuItem item1 = menu.findItem(R.id.refresh);
        MenuItem item2 = menu.findItem(R.id.ic_home);
        MenuItem item3 = menu.findItem(R.id.versionname);
        MenuItem item4 = menu.findItem(R.id.gooffline_case);
        MenuItem item5 = menu.findItem(R.id.create);
        MenuItem item6 = menu.findItem(R.id.noncaseactivity);
        MenuItem item7 = menu.findItem(R.id.convyencereport);
        MenuItem item8 = menu.findItem(R.id.filter);
        MenuItem item9 = menu.findItem(R.id.dropdown_menu);



        item.setVisible(false);
        item1.setVisible(false);
        item2.setVisible(false);
        item3.setVisible(false);
        item4.setVisible(false);
        item5.setVisible(false);
        item6.setVisible(false);
        item7.setVisible(false);
        item8.setVisible(true);
        item9.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            showFilterOption();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterOption(){
       /* final Dialog dialog = new Dialog(ViewRaisedTicketsActivity.this, R.style.raiseTicket);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.filter_view_ticket);
        // dialog.setContentView(R.layout.activity_ticket_raise_system);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();*/
        final Dialog dialog = new Dialog(ViewRaisedTicketsActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.filter_view_ticket);

        Button search = dialog.findViewById(R.id.btnSearch);
        Button clear = dialog.findViewById(R.id.btnClear);

        search.setOnClickListener(v -> dialog.dismiss());

        clear.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    private  void initToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Raised Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initAdapter(){

        List<ViewTicketData> dataModel = new ArrayList<ViewTicketData>();

        ViewTicketData viewTicketData = new ViewTicketData();
        viewTicketData.setTicketID("#12003");
        viewTicketData.setDate("06-03-2024");
        viewTicketData.setEdit(true);
        viewTicketData.setQuery("Report generation failed");
        viewTicketData.setStatus("Open");

        dataModel.add(viewTicketData);

        ViewTicketData viewTicketData1 = new ViewTicketData();
        viewTicketData1.setTicketID("#12005");
        viewTicketData1.setDate("07-03-2024");
        viewTicketData1.setEdit(false);
        viewTicketData1.setQuery("Approver not showing");
        viewTicketData1.setStatus("InProgress");
        dataModel.add(viewTicketData1);

        ViewTicketData viewTicketData2 = new ViewTicketData();
        viewTicketData2.setTicketID("#12007");
        viewTicketData2.setDate("19-07-2024");
        viewTicketData2.setEdit(true);
        viewTicketData2.setQuery("Approver not showing");
        viewTicketData2.setStatus("Pending");
        dataModel.add(viewTicketData2);


        ViewRaisedTicketAdapter viewRaisedTicketAdapter = new ViewRaisedTicketAdapter(dataModel,ViewRaisedTicketsActivity.this);
        rvViewRaisedTicket.setAdapter(viewRaisedTicketAdapter);
        rvViewRaisedTicket.setItemAnimator(new DefaultItemAnimator());
        rvViewRaisedTicket.setNestedScrollingEnabled(false);
        viewRaisedTicketAdapter.notifyDataSetChanged();
    }
}