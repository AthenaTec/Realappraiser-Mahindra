package com.realappraiser.gharvalue.viewtickets.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.TicketTaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.ticketRaiseSystem.adapter.ItemClickListener;
import com.realappraiser.gharvalue.ticketRaiseSystem.adapter.TicketUpdateImageAdapter;
import com.realappraiser.gharvalue.ticketRaiseSystem.adapter.ViewRaisedTicketAdapter;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.GetRaisedTickets;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.TicketImageModel;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.TicketInfo;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.TicketQueryDataModel;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.TicketStatusInfo;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.TicketUpdateModel;
import com.realappraiser.gharvalue.ticketRaiseSystem.service.TicketJsonRequestData;
import com.realappraiser.gharvalue.ticketRaiseSystem.service.TicketWebserviceCommunicator;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.viewtickets.model.ViewTicketModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewRaisedTicketsActivity extends AppCompatActivity {

    private List<ViewTicketModel.Data> dataModel;

    private ViewRaisedTicketAdapter viewRaisedTicketAdapter;

    ItemClickListener itemClickListener;

    private TicketInfo.Data ticketInfoList;

    private List<TicketStatusInfo.Status> ticketStatus = new ArrayList<>();
    private List<TicketImageModel.Data> ticketImageModelData = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_view_raised_ticket)
    RecyclerView rvViewRaisedTicket;

    General general;

    @BindView(R.id.retry)
    Button retry;

    @BindView(R.id.ll_no_internet)
    RelativeLayout rl_no_internet;

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;


    private String fromdate = "";

    private String toDate = "";

    private boolean isFilterSelected = false;
    private boolean isStatusSpinnerSelected = false;

    private String filterBy = "";

    private String statusValue = "";

    private String ticketID = "";

    private  Dialog ticketDialogPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_raised_tickets);
        ButterKnife.bind(this);
        general = new General(this);
        initToolBar();
        itemClickListener = (position, viewTicketData) -> updateTicketPopup(position, viewTicketData);
        getServerData();
        retry.setOnClickListener(view -> {
            getServerData();
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getServerData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void updateTicketPopup(int position, List<ViewTicketModel.Data> viewTicketData) {
        fetchTicketInfo(position, viewTicketData.get(position).getTicketId(), viewTicketData);
    }

    private void getServerData() {
        General.showloading(this);
        if (Connectivity.isConnected(this)) {
            initAdapter();
            fetchRaisedTicket();
            rl_no_internet.setVisibility(View.GONE);
            rvViewRaisedTicket.setVisibility(View.VISIBLE);
        } else {
            General.hideloading();
            rvViewRaisedTicket.setVisibility(View.GONE);
            rl_no_internet.setVisibility(View.VISIBLE);
        }
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
            if (Connectivity.isConnected(ViewRaisedTicketsActivity.this)) {
                String ticketQueryResponse = SettingsUtils.getInstance().getValue(SettingsUtils.FilterStatusQuery, "");
                if (ticketQueryResponse == null || ticketQueryResponse.isEmpty()) {
                    showFilterOption();
                } else {
                    String status = SettingsUtils.getInstance().getValue(SettingsUtils.FilterStatusQuery, "");
                    TicketStatusInfo ticketStatusInfo = new Gson().fromJson(status, TicketStatusInfo.class);
                    ticketStatus.clear();
                    ticketStatus.add(0, new TicketStatusInfo.Status("Select"));
                    ticketStatus.addAll(ticketStatusInfo.getDataList());
                    showFilterDialog();
                }
            } else {
                General.customToast("Internet Connection Is Required", ViewRaisedTicketsActivity.this);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterOption() {
        General.showloading(ViewRaisedTicketsActivity.this);
        String url = general.ApiBaseUrl() + SettingsUtils.fetchTicketStatus;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        String token = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, "");
        requestData.setAuthToken(token);
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(ViewRaisedTicketsActivity.this, requestData, SettingsUtils.GET_TOKEN);

        Log.e("***Ticket Status req", new Gson().toJson(requestData));


        webserviceTask.setFetchMyData((TaskCompleteListener<JsonRequestData>) requestData1 -> {
            try {
                Log.e("***Ticket Status res", "onTaskComplete: " + new Gson().toJson(requestData1.getResponse()));

                TicketStatusInfo ticketStatusInfo = new Gson().fromJson(requestData1.getResponse(), TicketStatusInfo.class);
                if (ticketStatusInfo.getStatus() == 1) {
                    SettingsUtils.getInstance().putValue(SettingsUtils.FilterStatusQuery, requestData1.getResponse());
                    ticketStatus.add(0, new TicketStatusInfo.Status("Select"));
                    ticketStatus.addAll(ticketStatusInfo.getDataList());
                    General.hideloading();
                    showFilterDialog();
                } else {
                    General.customToast("Unable to connect Server", ViewRaisedTicketsActivity.this);
                }
                General.hideloading();
            } catch (Exception e) {
                e.getMessage();
                General.hideloading();
            }


        });
        webserviceTask.execute();
    }


    private void showFilterDialog() {
        final Dialog dialog = new Dialog(ViewRaisedTicketsActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.filter_view_ticket);

        Button search = dialog.findViewById(R.id.btnSearch);
        Button clear = dialog.findViewById(R.id.btnClear);

        EditText etFromDate = dialog.findViewById(R.id.etFromDate);
        EditText etToDate = dialog.findViewById(R.id.etToDate);

        etFromDate.setOnClickListener(view1 -> showCalender(false, etFromDate, etToDate));
        etToDate.setOnClickListener(view1 -> showCalender(true, etFromDate, etToDate));

        Spinner spFilter = dialog.findViewById(R.id.spinner_filter);
        Spinner spStatus = dialog.findViewById(R.id.spinner_status);
        EditText editText = dialog.findViewById(R.id.et_search);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ticketID = editable.toString().trim();
            }
        });
        search.setOnClickListener(v -> {
            if (validateFilter(editText, spStatus)) {
                General.showloading(this);
                if (Connectivity.isConnected(this)) {
                    customRaiseTicket(dialog);
                    rl_no_internet.setVisibility(View.GONE);
                    rvViewRaisedTicket.setVisibility(View.VISIBLE);
                } else {
                    if (dialog != null) {
                        dialog.cancel();
                    }
                    General.hideloading();
                    rvViewRaisedTicket.setVisibility(View.GONE);
                    rl_no_internet.setVisibility(View.VISIBLE);
                }
            }
        });
        ArrayList<TicketQueryDataModel.Data> ticketData = new ArrayList();
        ticketData.add(new TicketQueryDataModel.Data("Select"));
        ticketData.add(new TicketQueryDataModel.Data("Ticket ID"));
        ticketData.add(new TicketQueryDataModel.Data("STATUS"));

        ArrayAdapter<TicketQueryDataModel.Data> arrayAdapter3 = new ArrayAdapter<>(getApplicationContext(), R.layout.row_spinner_item_, ticketData);
        arrayAdapter3.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spFilter.setAdapter(arrayAdapter3);

        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    editText.setVisibility(View.GONE);
                    spStatus.setVisibility(View.GONE);
                    isFilterSelected = false;
                    filterBy = "";
                } else if (i == 1) {
                    editText.setVisibility(View.VISIBLE);
                    spStatus.setVisibility(View.GONE);
                    isFilterSelected = true;
                    filterBy = "ticketid";
                } else if (i == 2) {
                    isFilterSelected = true;
                    editText.setVisibility(View.GONE);
                    spStatus.setVisibility(View.VISIBLE);
                    filterBy = "status";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<TicketStatusInfo.Status> ticketStatusSpinner = new ArrayAdapter<>(getApplicationContext(), R.layout.row_spinner_item_, ticketStatus);
        ticketStatusSpinner.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spStatus.setAdapter(ticketStatusSpinner);

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    isStatusSpinnerSelected = false;
                } else {
                    isStatusSpinnerSelected = true;
                }
                statusValue = ticketStatus.get(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        clear.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Raised Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initAdapter() {

        dataModel = new ArrayList<>();
        viewRaisedTicketAdapter = new ViewRaisedTicketAdapter(dataModel, ViewRaisedTicketsActivity.this, itemClickListener);
        rvViewRaisedTicket.setAdapter(viewRaisedTicketAdapter);
        rvViewRaisedTicket.setItemAnimator(new DefaultItemAnimator());
        rvViewRaisedTicket.setNestedScrollingEnabled(false);
        viewRaisedTicketAdapter.notifyDataSetChanged();
    }

    private void fetchRaisedTicket() {
        GetRaisedTickets getRaisedTickets = new GetRaisedTickets();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String fDate = sdf1.format(new Date());
        getRaisedTickets.setTodate(currentDateandTime + " 00:00:00");
        getRaisedTickets.setFromdate(fDate + "-01T00:00:00.000Z");

        getRaisedTickets.setFillterby(null);
        getRaisedTickets.setTicketId(null);
        getRaisedTickets.setStatus(null);
        //getRaisedTickets.setRoleId(4);
        getRaisedTickets.setEmpId(Integer.parseInt(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_EMPID, "")));
         getTicket(getRaisedTickets, null, false);
    }

    private void customRaiseTicket(Dialog dialog) {
        GetRaisedTickets getRaisedTickets = new GetRaisedTickets();

        getRaisedTickets.setTodate(toDate);
        getRaisedTickets.setFromdate(fromdate);
        getRaisedTickets.setFillterby(filterBy);
        if (filterBy.equalsIgnoreCase("status")) {
            getRaisedTickets.setStatus(statusValue);
            getRaisedTickets.setTicketId(null);
        } else {
            getRaisedTickets.setTicketId(ticketID);
            getRaisedTickets.setStatus(null);
        }
        getRaisedTickets.setEmpId(Integer.parseInt(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_EMPID, "")));
        getTicket(getRaisedTickets, dialog, true);
    }

    private void getTicket(GetRaisedTickets getRaisedTickets, Dialog dialog, boolean b) {
        String url = general.ApiBaseUrl() + SettingsUtils.fetchRaisedTicket;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);

        String token = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, "");
        requestData.setAuthToken(token);
        requestData.setMainJson(new Gson().toJson(getRaisedTickets));
        requestData.setRequestBody(RequestParam.SaveCaseInspectionRequestParams(requestData));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(ViewRaisedTicketsActivity.this, requestData, SettingsUtils.PUT_TOKEN);

        Log.e("ViewRaisedTicket Req", new Gson().toJson(requestData));


        webserviceTask.setFetchMyData((TaskCompleteListener<JsonRequestData>) requestData1 -> {

            Log.e("ViewRaisedTicket Res", requestData1.getResponse());

            if (dialog != null && b) {
                dialog.cancel();
            }

            try {

                ViewTicketModel viewTicketModel = new Gson().fromJson(requestData1.getResponse(), ViewTicketModel.class);
                if (viewTicketModel.getStatus() == 1 && viewTicketModel.getData().size() > 0) {
                    rvViewRaisedTicket.setVisibility(View.VISIBLE);
                    rl_no_internet.setVisibility(View.GONE);
                    dataModel = viewTicketModel.getData();
                    viewRaisedTicketAdapter.updateAdapter(dataModel);
                    General.hideloading();
                } else {
                    General.hideloading();

                    if (viewTicketModel.getStatus() == 2) {
                        General.customToast("Unable to reach Server", ViewRaisedTicketsActivity.this);
                    }
                    if (viewTicketModel.getData().size() == 0) {
                        dataModel.clear();
                        viewRaisedTicketAdapter.updateAdapter(dataModel);
                        rvViewRaisedTicket.setVisibility(View.GONE);
                        rl_no_internet.setVisibility(View.GONE);
                        General.customToast("No Ticket Found", ViewRaisedTicketsActivity.this);
                    }
                }
            } catch (Exception e) {
                e.getMessage();
                General.hideloading();
            }


        });
        webserviceTask.execute();
    }


    private void fetchTicketInfo(int itemPosition, int ticketId, List<ViewTicketModel.Data> viewTicketData) {
        General.showloading(ViewRaisedTicketsActivity.this);

        String url = general.ApiBaseUrl() + SettingsUtils.fetchTicketInfo;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setTicketId(ticketId);
        requestData.setUrl(RequestParam.getTicketInfo(requestData));
        requestData.setEmpId(String.valueOf(ticketId));
        String token = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, "");
        requestData.setAuthToken(token);
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(ViewRaisedTicketsActivity.this, requestData, SettingsUtils.GET_TOKEN);

        Log.e("***Fetch Ticket Info***", new Gson().toJson(requestData));


        webserviceTask.setFetchMyData((TaskCompleteListener<JsonRequestData>) requestData1 -> {
            try {
                Log.e("ticketInfoList", "onTaskComplete: " + new Gson().toJson(requestData1.getResponse()));

                TicketInfo ticketInfo = new Gson().fromJson(requestData1.getResponse(), TicketInfo.class);
                if (ticketInfo.getStatus() == 1) {
                    General.hideloading();
                    ticketInfoList = ticketInfo.getData();
                    fetchTicketImage(itemPosition, ticketId, viewTicketData);
                } else {
                    General.customToast("Unable to connect Server", ViewRaisedTicketsActivity.this);
                    General.hideloading();
                }

            } catch (Exception e) {
                e.getMessage();
                General.hideloading();
            }


        });
        webserviceTask.execute();
    }

    private void fetchTicketImage(int itemPosition, int ticketId, List<ViewTicketModel.Data> viewTicketData) {


        General.showloading(this);

        String url = general.ApiBaseUrl() + SettingsUtils.fetchTicketImage;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setTicketId(ticketId);
        requestData.setUrl(RequestParam.getTicketInfo(requestData));
        requestData.setEmpId(String.valueOf(ticketId));
        String token = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, "");
        requestData.setAuthToken(token);
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(ViewRaisedTicketsActivity.this, requestData, SettingsUtils.GET_TOKEN);

        Log.e("***Fetch Ticket Image**", new Gson().toJson(requestData));


        webserviceTask.setFetchMyData((TaskCompleteListener<JsonRequestData>) requestData1 -> {
            try {
                Log.e("ticketImageModelData", "onTaskComplete: " + new Gson().toJson(requestData1.getResponse()));

                TicketImageModel ticketImageModel = new Gson().fromJson(requestData1.getResponse(), TicketImageModel.class);
                if (ticketImageModel.getStatus() == 1) {

                    if (ticketImageModel.getData() != null) {
                        General.hideloading();
                        ticketImageModelData = ticketImageModel.getData();
                        showViewTicketPopup(itemPosition, ticketId, viewTicketData, ticketImageModelData, false);
                    } else {
                        showViewTicketPopup(itemPosition, ticketId, viewTicketData, ticketImageModelData, true);
                    }

                } else {
                    General.customToast("Unable to connect Server", ViewRaisedTicketsActivity.this);
                    General.hideloading();
                }

            } catch (Exception e) {
                e.getMessage();
                General.hideloading();
            }


        });
        webserviceTask.execute();
    }

    private void showViewTicketPopup(int itemPosition, int ticketId, List<ViewTicketModel.Data> viewTicketData, List<TicketImageModel.Data> ticketModel, boolean showImage) {
        try {

            if( ticketDialogPopup !=null &&  ticketDialogPopup.isShowing()){
                ticketDialogPopup.dismiss();
            }

            ticketDialogPopup = new Dialog(ViewRaisedTicketsActivity.this, R.style.raiseTicket);
            ticketDialogPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            ticketDialogPopup.setCancelable(false);
            ticketDialogPopup.setCanceledOnTouchOutside(false);
            ticketDialogPopup.setContentView(R.layout.update_ticket_raise_system);
            ticketDialogPopup.setCanceledOnTouchOutside(true);

            if (!showImage) {
                ticketDialogPopup.findViewById(R.id.ll_ticketImage).setVisibility(View.VISIBLE);
                initUpdateImageAdapter(ticketDialogPopup);
            } else {
                ticketDialogPopup.findViewById(R.id.ll_ticketImage).setVisibility(View.GONE);
            }


            if (viewTicketData.get(itemPosition).getStatus().equalsIgnoreCase("Completed")) {
                ticketDialogPopup.findViewById(R.id.ll_ticket_status).setVisibility(View.VISIBLE);
                ticketDialogPopup.findViewById(R.id.ll_comment).setVisibility(View.VISIBLE);
            } else {
                ticketDialogPopup.findViewById(R.id.ll_ticket_status).setVisibility(View.GONE);
                ticketDialogPopup.findViewById(R.id.ll_comment).setVisibility(View.GONE);
            }


            TextView title = ticketDialogPopup.findViewById(R.id.txt_title);
            title.setText("Ticket No #" + ticketId);

            EditText etQuery = ticketDialogPopup.findViewById(R.id.etQuery);
            etQuery.setText(viewTicketData.get(itemPosition).getQuery());

            final EditText etOther = ticketDialogPopup.findViewById(R.id.et_other);

            etOther.setClickable(false);
            etOther.setFocusable(false);
            etOther.setFocusableInTouchMode(false);

            if (ticketInfoList.getQueryType() == 10) {
                etOther.setText(ticketInfoList.getOtherQueries());
                etOther.setVisibility(View.VISIBLE);
            } else {
                etOther.setVisibility(View.GONE);
            }

            EditText desc = ticketDialogPopup.findViewById(R.id.etNotes);
            EditText ll_comments = ticketDialogPopup.findViewById(R.id.et_comments);
            ll_comments.setText("");
            EditText contactNumber = ticketDialogPopup.findViewById(R.id.et_contactNumber);
            desc.setText(ticketInfoList.getDescription());
            contactNumber.setText(ticketInfoList.getContactNumber());


            Button closeTicket = ticketDialogPopup.findViewById(R.id.closeTicket);

            ImageView backBtn = ticketDialogPopup.findViewById(R.id.btn_back);

            backBtn.setOnClickListener(view -> ticketDialogPopup.cancel());

            Button reCreateTicket = ticketDialogPopup.findViewById(R.id.btnSubmit);

            reCreateTicket.setOnClickListener(view -> {
                if (ll_comments.getText().toString().trim().isEmpty()) {
                    General.customToast("Please Enter Comments", ViewRaisedTicketsActivity.this);
                } else {
                    initReCreateTicket(ticketId, ll_comments.getText().toString().trim(), ticketDialogPopup, true);
                }
            });

            closeTicket.setOnClickListener(view -> {
                if (ll_comments.getText().toString().trim().isEmpty()) {
                    General.customToast("Please Enter Comments", ViewRaisedTicketsActivity.this);
                } else {
                    initReCreateTicket(ticketId, ll_comments.getText().toString().trim(), ticketDialogPopup, false);
                }
            });
            ticketDialogPopup.show();
            General.hideloading();
           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    General.hideloading();
                }
            }, 1000);*/

        } catch (Exception e) {
            General.hideloading();
            e.getMessage();
        }
    }

    private void initUpdateImageAdapter(Dialog dialog) {
        TicketUpdateImageAdapter ticketUpdateImageAdapter = new TicketUpdateImageAdapter(this, ticketImageModelData, general);
        final RecyclerView recyclerView = dialog.findViewById(R.id.rv_image);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(ticketUpdateImageAdapter);
        ticketUpdateImageAdapter.notifyDataSetChanged();
    }

    private void showCalender(boolean isFromDate, EditText etFromDate, EditText etToDate) {

        final Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog;
        int mYear = newCalendar.get(Calendar.YEAR);
        int mMonth = newCalendar.get(Calendar.MONTH);
        int mDay = newCalendar.get(Calendar.DAY_OF_MONTH);

        newCalendar.add(Calendar.YEAR, 0);
        long upperLimit = newCalendar.getTimeInMillis();

        datePickerDialog = new DatePickerDialog(ViewRaisedTicketsActivity.this, (view, year, month, dayOfMonth) -> {

            newCalendar.set(Calendar.YEAR, year);
            newCalendar.set(Calendar.MONTH, month);
            newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SimpleDateFormat sdfWeekReport = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


            SimpleDateFormat selectedDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDateandTime = selectedDateFormat.format(newCalendar.getTime());
            String fDate = selectedDateFormat.format(newCalendar.getTime());

            if (!isFromDate) {
                fromdate = fDate + "T00:00:00Z";
                etFromDate.setText(sdf.format(newCalendar.getTime()));
            } else {
                toDate = currentDateandTime + "T00:00:00Z";
                etToDate.setText(sdf.format(newCalendar.getTime()));
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        datePickerDialog.getDatePicker().setMaxDate(upperLimit);
        datePickerDialog.show();
    }

    private boolean validateFilter(EditText editText, Spinner spStatus) {
        if (!isFilterSelected) {
            General.customToast("Please Select any Filter options", this);
            return false;
        } else {
            if (editText.getVisibility() == View.VISIBLE && editText.getText().toString().trim().isEmpty()) {
                General.customToast("Please provide any Ticket ID", this);
                return false;
            } else if (spStatus.getVisibility() == View.VISIBLE && !isStatusSpinnerSelected) {
                General.customToast("Please Select any Ticket Status options", this);
                return false;
            }
        }

        if (fromdate == null || fromdate.isEmpty() || toDate == null || toDate.isEmpty()) {
            General.customToast("Please Select Date", this);
            return false;
        }
        return true;
    }


    private void initReCreateTicket(int ticketId, String comments, Dialog dialog, boolean b) {


        String url = general.ApiBaseUrl() + SettingsUtils.UpdateStatusbyId;
        TicketJsonRequestData requestData = new TicketJsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setTicketId(ticketId);
        requestData.setComments(comments);
        if (b) {
            requestData.setStatusId(1);
        } else {
            requestData.setStatusId(6);
        }

        requestData.setUrl(RequestParam.getUpdateTicketInfo(requestData));
        requestData.setRequestBody(RequestParam.getUpdateTicketResponse(requestData));
        String token = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, "");
        requestData.setAuthToken(token);
        TicketWebserviceCommunicator webserviceTask = new TicketWebserviceCommunicator(ViewRaisedTicketsActivity.this,
                requestData, SettingsUtils.PUT_TOKEN);

        Log.e("Update Ticket  Req", new Gson().toJson(requestData));


        webserviceTask.setFetchMyData((TicketTaskCompleteListener<TicketJsonRequestData>) requestData1 -> {
            try {
                Log.e("close Ticket  Res", "onTaskComplete: " + new Gson().toJson(requestData1.getResponse()));

                TicketUpdateModel ticketUpdateModel = new Gson().fromJson(requestData1.getResponse(), TicketUpdateModel.class);
                if (ticketUpdateModel != null && ticketUpdateModel.getStatus() == 1) {
                    General.customToastLong("Status Updated Succesfullly", ViewRaisedTicketsActivity.this);
                } else {
                    General.customToast("Unable to connect Server", ViewRaisedTicketsActivity.this);
                }

                General.hideloading();
                if (dialog != null) dialog.cancel();
                getServerData();



            } catch (Exception e) {
                if (dialog != null) dialog.cancel();
                e.getMessage();
                General.hideloading();
            }


        });
        webserviceTask.execute();
    }

}