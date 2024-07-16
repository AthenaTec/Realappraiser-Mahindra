package com.realappraiser.gharvalue.FieldsInspection;

import static com.realappraiser.gharvalue.fragments.PhotoLatLong.latvalue;
import static com.realappraiser.gharvalue.fragments.PhotoLatLong.longvalue;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.realappraiser.gharvalue.AppDatabase;
import com.realappraiser.gharvalue.MyApplication;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.adapter.ComparePropertyRateAdapter;
import com.realappraiser.gharvalue.adapter.InternalFloorNewAdapter;
import com.realappraiser.gharvalue.adapter.PropertyActualUsageAdapter;
import com.realappraiser.gharvalue.adapter.PropertyFloorInternalAdapter;
import com.realappraiser.gharvalue.adapter.PropertyGeneralFloorAdapter;
import com.realappraiser.gharvalue.adapter.ValuationActualAreaAdapter;
import com.realappraiser.gharvalue.adapter.ValuationPermissibleAreaAdapter;
import com.realappraiser.gharvalue.communicator.DataModel;
import com.realappraiser.gharvalue.communicator.DataResponse;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RatePopupApi;
import com.realappraiser.gharvalue.communicator.RatePopupupInterface;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.ResponseParser;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.fragments.FragmentBuilding;
import com.realappraiser.gharvalue.fragments.OtherDetails;
import com.realappraiser.gharvalue.fragments.PhotoLatLong;
import com.realappraiser.gharvalue.model.GetPhoto;
import com.realappraiser.gharvalue.model.GetPropertyCompareDetailsModel;
import com.realappraiser.gharvalue.model.GetPropertyDetailsModel;
import com.realappraiser.gharvalue.model.IndPropertyFloor;
import com.realappraiser.gharvalue.model.IndPropertyFloorsValuation;
import com.realappraiser.gharvalue.model.InternalFloorModel;
import com.realappraiser.gharvalue.model.LatLongDetails;
import com.realappraiser.gharvalue.model.Measurements;
import com.realappraiser.gharvalue.model.OfflineDataModel;
import com.realappraiser.gharvalue.model.OflineCase;
import com.realappraiser.gharvalue.model.PropertyActualUsage;
import com.realappraiser.gharvalue.model.RatePopup;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.DecimalDigitsInputFilter;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.OthersFormListener;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.utils.Singleton;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FSLandBuildingValuation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FSLandBuildingValuation extends Fragment implements View.OnClickListener, RatePopupupInterface {

    public Dialog dialog;

    String selected_value = "";

    public static ArrayList<IndPropertyFloorsValuation> floorsValuation;


    // TODO General class to call typeface and all...
    @SuppressLint("StaticFieldLeak")
    public static General general;

    @SuppressLint("StaticFieldLeak")
    public static FragmentActivity fragmentActivity;

    @SuppressLint("StaticFieldLeak")
    public static EditText editText_remarks;

    @SuppressLint("StaticFieldLeak")
    public static EditText editText_construction_value;

    @SuppressLint("StaticFieldLeak")
    public static EditText editText_construction_rate;

    @SuppressLint("StaticFieldLeak")
    public static EditText editText_aspercompletion;

    @BindView(R.id.textview_floor_head)
    TextView textview_floor_head;
    @BindView(R.id.textview_permissiblearea_head)
    TextView textview_permissiblearea_head;
    @BindView(R.id.textview_rate_head)
    TextView textview_rate_head;
    @BindView(R.id.textview_value_head)
    TextView textview_value_head;

    @BindView(R.id.textview_land_measurement)
    TextView textview_land_measurement;


    @SuppressLint("StaticFieldLeak")
    public static TextView textview_totalpropertyvalue_result;


    @BindView(R.id.textview_construction_details)
    TextView textview_construction_details;


    TextView spinner_realizable;
    TextView spinner_distress;
    TextView spinner_carpet;

    @SuppressLint("StaticFieldLeak")
    public static TextView spinner_carpet_type;
    ArrayList<String> realizableList = new ArrayList<>();
    ArrayList<String> distressList = new ArrayList<>();
    ArrayList<String> carpetList = new ArrayList<>();
    ArrayList<String> carpet_typeList = new ArrayList<>();

    TextView select_error;
    TextView carpet_error;

    @SuppressLint("StaticFieldLeak")
    public static InternalFloorNewAdapter internalfloorlistAdapter;

    @SuppressLint("StaticFieldLeak")
    static EditText editText_realizable_custom;
    @SuppressLint("StaticFieldLeak")
    static EditText editText_distress_custom;


    @SuppressLint("StaticFieldLeak")
    static EditText etProposedValuation;


    //  @BindView(R.id.editText_floors)
    @SuppressLint("StaticFieldLeak")
    public static EditText editText_floors_measu;
    // @BindView(R.id.editText_approved_floors)
    @SuppressLint("StaticFieldLeak")
    public static EditText editText_approved_floors;
    @BindView(R.id.textview_actualusage)
    TextView textview_actualusage;
    // @BindView(R.id.editText_Floor_details)
    @SuppressLint("StaticFieldLeak")
    public static EditText editText_Floor_details;

    @SuppressLint("StaticFieldLeak")
    public static TextView textview_insurancevaluepe_result;


    @BindView(R.id.spinner_ft_meter_construction)
    Spinner spinner_ft_meter_construction;


    @SuppressLint("StaticFieldLeak")
    public static EditText editText_total_permissiblearea;


    @SuppressLint("StaticFieldLeak")
    public static EditText editText_total_actualarea;

    @SuppressLint("StaticFieldLeak")
    public static TextView textview_aspercompletion_result;

    @SuppressLint("StaticFieldLeak")
    public static EditText editText_rate_actualarea;


    @BindView(R.id.editText_actualarea)
    EditText editText_actualarea;


    @SuppressLint("StaticFieldLeak")
    public static EditText etProposedPercentage;


    public static CheckBox cbProposedPercentage;


    @BindView(R.id.textview_generl_info_measu)
    TextView textview_generl_info_measu;
    @SuppressLint("StaticFieldLeak")
    public static TextView textview_floor_name;
    @SuppressLint("StaticFieldLeak")
    public static TextView textview_stage;
    @SuppressLint("StaticFieldLeak")
    public static TextView textview_comp;
    @BindView(R.id.textview_progress)
    TextView textview_progress;
    @BindView(R.id.textview_doc_area)
    TextView textview_doc_area;
    @BindView(R.id.textview_actual_area)
    TextView textview_actual_area;
    @BindView(R.id.textview_age)
    TextView textview_age;
    @BindView(R.id.textview_life)
    TextView textview_life;
    @BindView(R.id.textview_floor_usage)
    TextView textview_floor_usage;
    @BindView(R.id.textview_average)
    TextView textview_average;
    //@BindView(R.id.textview_comp_total)
    // TextView textview_comp_total;
    @BindView(R.id.textview_total)
    TextView textview_total;
    //@BindView(R.id.textview_doc_total)
    @SuppressLint("StaticFieldLeak")
    public static TextView textview_doc_total;
    @SuppressLint("StaticFieldLeak")
    public static TextView textview_actual_total;
    @SuppressLint("StaticFieldLeak")
    public static TextView txt_total_sanctioned_area;
    @SuppressLint("StaticFieldLeak")
    public static TextView txt_total_actual_area;
    @SuppressLint("StaticFieldLeak")
    public static TextView txt_permissiable_area_value;
    @SuppressLint("StaticFieldLeak")
    public static TextView textview_comp_total;

    @SuppressLint("StaticFieldLeak")
    public static TextView textview_totalconstructionvalue_result;

    @BindView(R.id.tvRatePopup)
    TextView tvRatePopup;

    private RatePopupApi ratePopupApi;


    /*@BindView(R.id.edittext_general_floor_name)
    EditText edittext_general_floor_name;
    @BindView(R.id.edittext_general_comp)
    EditText edittext_general_comp;
    @BindView(R.id.edittext_general_progress)
    EditText edittext_general_progress;
    @BindView(R.id.edittext_general_doc_area)
    EditText edittext_general_doc_area;
    @BindView(R.id.edittext_general_actual_area)
    EditText edittext_general_actual_area;
    @BindView(R.id.edittext_general_age)
    EditText edittext_general_age;
    @BindView(R.id.edittext_general_life)
    EditText edittext_general_life;*/

    @BindView(R.id.textview_internal_composition)
    TextView textview_internal_composition;
    @BindView(R.id.textview_floor_name_composition)
    TextView textview_floor_name_composition;
    @BindView(R.id.textview_hall_dinning)
    TextView textview_hall_dinning;
    @BindView(R.id.textview_kitchen)
    TextView textview_kitchen;
    @BindView(R.id.textview_bedroom)
    TextView textview_bedroom;
    @BindView(R.id.textview_bath)
    TextView textview_bath;
    @BindView(R.id.textview_shop_office)
    TextView textview_shop_office;

    @BindView(R.id.ll_internal_composition)
    LinearLayout llInternalComposition;

    @BindView(R.id.textview_error_measure_land)
    TextView textview_error_measure_land;


    boolean is_offline = false;

    @BindView(R.id.textview_measurement_doc)
    TextView textview_measurement_doc;
    @BindView(R.id.textview_measurement_act)
    TextView textview_measurement_act;
    @BindView(R.id.image_addFloors)
    ImageView image_addFloors;

    // TODO - Spinner - Address
    //@BindView(R.id.spinner_measurement1)
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner_measurement1;

    //@BindView(R.id.spinner_measurement2)
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinner_measurement2;

    @BindView(R.id.recyclerview_generalinfo)
    RecyclerView recyclerview_generalinfo;
    @BindView(R.id.recyclerview_internal)
    RecyclerView recyclerview_internal;

    @BindView(R.id.div_general_info_measurment)
    LinearLayout generalInfoMeasurement;


    /* san Integration */
    @BindView(R.id.textview_actual_usage)
    TextView textview_actual_usage;

    // actual_usage
    ArrayList<PropertyActualUsage> Inter_floors_list = new ArrayList<>();

    private ArrayList<IndPropertyFloor> list;
    private ArrayList<IndPropertyFloor> floornolist;
    private ArrayList<String> floorusage = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static PropertyGeneralFloorAdapter listAdapter;

    public static ArrayList<IndPropertyFloorsValuation> getValFloorlistSave;

    public ArrayList<IndPropertyFloorsValuation> getValFloorlist;


    @SuppressLint("StaticFieldLeak")
    public static ValuationPermissibleAreaAdapter valuationPermissibleAreaAdapter;

    @SuppressLint("StaticFieldLeak")
    public static ValuationActualAreaAdapter listActualAdapter;

    public ArrayList<IndPropertyFloorsValuation> getValActualFloorlist;


    @SuppressLint("StaticFieldLeak")
    public static RecyclerView recyclerview_permissiblearea;
    @SuppressLint("StaticFieldLeak")
    public static RecyclerView recyclerview_actualarea;


    @SuppressLint("StaticFieldLeak")
    public static PropertyFloorInternalAdapter propertylistAdapter;
    private IndPropertyFloor stepsModel = new IndPropertyFloor();
    private IndPropertyFloor steps2Model = new IndPropertyFloor();
    public static LinearLayoutManager llm, lfm, llactualArea, llsanctionedArea;
    public OthersFormListener listener;
    public static String measurment_floor_id = "0";
    RotateLoading rotateloading;
    private String msg = "", info = "";

    AppDatabase appDatabase = null;
    int caseid_int = 0;
    public boolean enable_offline_button;

    static int mea_unit_construction = 0;

    static String total_permisssion_area_floors = "";
    static String total_actual_area_floors = "";
    static int measurment_unit_property_floors = 0;
    static int measurment_unit_property_for_dlc = 0;
    public static String permissiblearea_str_dlc = "";
    public static String actualarea_str_dlc = "";

    static int permission_check_construction = 0;

    AdapterView.OnItemSelectedListener listener1;


    Button button0, button1, button2, button3, button4, button5, button6,
            button7, button8, button9, buttonAdd, buttonSub, buttonDivision,
            buttonMul, button10, buttonC, buttonEqual, button_set, button_close;
    TextView edt1;
    float mValueOne, mValueCurrent;
    boolean mAddition, mSubtract, mMultiplication, mDivision, mClear;

    /***** Declaring Variable *****/
    TextView btnClear, btnSmallBracket, btnPercentage, btnSeven, btnNine, btnMultiply, btnEight, btnBack;
    TextView tvProcessor, tvResult, btnFour, btnFive, btnSix, btnMinus, txtOne, txtTwo;

    TextView txtThree, txtZero, txtPlus;
    String processor;
    Boolean isSmallBracketOpen;
    TextView btnDivide, btnDecimal, btnEqual;

    TextView btnclose, btnsetvalue;


    // calc
    @BindView(R.id.open_calc_compound)
    ImageView open_calc_compound;
    @BindView(R.id.open_calc_measurment)
    ImageView open_calc_measurment;


    @SuppressLint("StaticFieldLeak")
    public static EditText editText_compound_permissiblearea;

    @SuppressLint("StaticFieldLeak")
    public static EditText editText_as_per_measurement;


    // TODO RatioGroup ini
    @BindView(R.id.id_radiogroup_considerforvaluation_land)
    RadioGroup id_radiogroup_considerforvaluation_land;
    @BindView(R.id.id_radio_considerforvaluation_permissiblearea_land)
    RadioButton id_radio_considerforvaluation_permissiblearea_land;
    @BindView(R.id.id_radio_considerforvaluation_actualarea_land)
    RadioButton id_radio_considerforvaluation_actualarea_land;
    @BindView(R.id.id_radiogroup_considerforvaluation_construction)
    RadioGroup id_radiogroup_considerforvaluation_construction;
    @BindView(R.id.id_radio_considerforvaluation_permissiblearea_construction)
    RadioButton id_radio_considerforvaluation_permissiblearea_construction;
    @BindView(R.id.id_radio_considerforvaluation_actualarea_construction)
    RadioButton id_radio_considerforvaluation_actualarea_construction;


    @SuppressLint("StaticFieldLeak")
    public static EditText editText_guideline_rate;


    @SuppressLint("StaticFieldLeak")
    public static EditText editText_guideline_value;

    @SuppressLint("StaticFieldLeak")
    public static EditText editText_rate_permissiblearea;

    @BindView(R.id.editText_permissiblearea)
    EditText editText_permissiblearea;

    @BindView(R.id.editText_unit_permissiblearea)
    EditText editText_unit_permissiblearea;

    @BindView(R.id.editText_unit_actualarea)
    EditText editText_unit_actualarea;

    @BindView(R.id.tvCompareRate)
    TextView tvCompareRate;

    // TODO - Linear
    @BindView(R.id.linear_permissiblearea)
    LinearLayout linear_permissiblearea;
    @BindView(R.id.linear_actualarea)
    LinearLayout linear_actualarea;


    @BindView(R.id.spinner_ft_meter)
    Spinner spinner_ft_meter;


    int developedCounter;
    static String developedBy = "sabesh";
    static String deveopedNote = "developed in IU, Tue Feb 26, 2026";
    double result_value = 0.0;

    public static int mea_unit = 0;

    public static int permission_check = 0;

    static String caseid_str;


    public FSLandBuildingValuation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FSLandBuildingValuation.
     */
    // TODO: Rename and change types and number of parameters
    public static FSLandBuildingValuation newInstance(String param1, String param2) {
        FSLandBuildingValuation fragment = new FSLandBuildingValuation();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        caseid_str = String.valueOf(Singleton.getInstance().aCase.getCaseId());
        caseid_int = Singleton.getInstance().aCase.getCaseId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f_s_land_building_valuation, container, false);
        ButterKnife.bind(this, view);


        // Room - Declared
        if (MyApplication.getAppContext() != null) {
            appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        }
        // CaseID to interger


        // check the offline module is present or not
        enable_offline_button = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_ENABLE_OFFLINE, false);

        initViews(view);
        initLandMeasurementView(view);

        // TODO -  call the mandatory_validation
        if (Singleton.getInstance().enable_validation_error) {
            set_mandatory_building();
        }

        DisplayValuesLandBuilding();
        landMeasurement();
        constructionValuation();
        displayValuationDetails();
        getStatusfromResult();
        realizableMapping();
        distressView();
        carpet();
        carpetType();


        int total_floor_size = Singleton.getInstance().indPropertyFloors.size();

        if (total_floor_size > 0) {
            if (!general.isEmpty(Singleton.getInstance().indPropertyFloors.get(0).getDocumentFloorAreaUnit())) {
                measurment_unit_property_for_dlc = Integer.parseInt(Singleton.getInstance().indPropertyFloors.get(0).getDocumentFloorAreaUnit());
            }
        }

        // TODO - check with getDocumentFloorArea value list

        if (total_floor_size > 0) {
            float rate_float = 0;
            for (int x = 0; x < total_floor_size; x++) {
                if (!general.isEmpty(Singleton.getInstance().indPropertyFloors.get(x).getDocumentFloorArea())) {
                    float rate_check = general.convertTofloat(Singleton.getInstance().indPropertyFloors.get(x).getDocumentFloorArea());
                    rate_float = ((rate_float) + (rate_check));
                }
            }
            permissiblearea_str_dlc = String.valueOf(rate_float);
        }

        // TODO - check with getDocumentFloorArea value list
        if (total_floor_size > 0) {
            float rate_float = 0;
            for (int x = 0; x < total_floor_size; x++) {
                if (!general.isEmpty(Singleton.getInstance().indPropertyFloors.get(x).getMeasuredFloorArea())) {
                    float rate_check = general.convertTofloat(Singleton.getInstance().indPropertyFloors.get(x).getMeasuredFloorArea());
                    rate_float = ((rate_float) + (rate_check));
                }
            }
            actualarea_str_dlc = String.valueOf(rate_float);
        }


        tvCompareRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails();
            }
        });


        // id_radio_considerforvaluation_permissiblearea_land and id_radio_considerforvaluation_actualarea_land
        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyValuation.getDocumentedLandValueSel()))) {
            if (Singleton.getInstance().indPropertyValuation.getDocumentedLandValueSel()) {
                permission_check = 1;
                id_radio_considerforvaluation_permissiblearea_land.setChecked(true);
                id_radio_considerforvaluation_actualarea_land.setChecked(false);
                linear_permissiblearea.setVisibility(View.VISIBLE);
                linear_actualarea.setVisibility(View.GONE);
            } else {
                permission_check = 2;
                id_radio_considerforvaluation_permissiblearea_land.setChecked(false);
                id_radio_considerforvaluation_actualarea_land.setChecked(true);
                linear_permissiblearea.setVisibility(View.GONE);
                linear_actualarea.setVisibility(View.VISIBLE);
            }
        }

        // id_radio_considerforvaluation_permissiblearea_construction and id_radio_considerforvaluation_actualarea_construction
        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyValuation.getDocumentedConstrValueSel()))) {
            if (Singleton.getInstance().indPropertyValuation.getDocumentedConstrValueSel()) {
                permission_check_construction = 1;
                id_radio_considerforvaluation_permissiblearea_construction.setChecked(true);
                id_radio_considerforvaluation_actualarea_construction.setChecked(false);
                //textview_permissiblearea_head.setText(getResources().getString(R.string.permissiblearea));
                textview_permissiblearea_head.setText("Sanctioned area");

                recyclerview_permissiblearea.setVisibility(View.VISIBLE);
                recyclerview_actualarea.setVisibility(View.GONE);

            } else {
                permission_check_construction = 2;
                id_radio_considerforvaluation_permissiblearea_construction.setChecked(false);
                id_radio_considerforvaluation_actualarea_construction.setChecked(true);
                textview_permissiblearea_head.setText(getResources().getString(R.string.actualarea));

                recyclerview_actualarea.setVisibility(View.VISIBLE);
                recyclerview_permissiblearea.setVisibility(View.GONE);
            }
        }

        if (latvalue.getText().toString() != null) {
            ratePopupApi.getRatePopup(caseid_str, latvalue.getText().toString(), longvalue.getText().toString());
        } else {
            ratePopupApi.getRatePopup(caseid_str, String.valueOf(SettingsUtils.Latitudes), String.valueOf(SettingsUtils.Longitudes));
        }

        return view;
    }


    private void initLandMeasurementView(View view) {
        spinner_measurement1 = (Spinner) view.findViewById(R.id.spinner_measurement1);
        spinner_measurement2 = (Spinner) view.findViewById(R.id.spinner_measurement2);

        spinner_measurement1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(spinner_measurement1);
                return false;
            }
        });
        editText_approved_floors = (EditText) view.findViewById(R.id.editText_approved_floors);
        recyclerview_actualarea = (RecyclerView) view.findViewById(R.id.recyclerview_actualarea);
        recyclerview_permissiblearea = (RecyclerView) view.findViewById(R.id.recyclerview_permissiblearea);

        image_addFloors.setOnClickListener(this);
        editText_floors_measu = (EditText) view.findViewById(R.id.editText_floors);
        editText_Floor_details = (EditText) view.findViewById(R.id.editText_Floor_details);

        textview_comp_total = (TextView) view.findViewById(R.id.textview_comp_total);
        textview_doc_total = (TextView) view.findViewById(R.id.textview_doc_total);
        textview_actual_total = (TextView) view.findViewById(R.id.textview_actual_total);

        txt_total_sanctioned_area = (TextView) view.findViewById(R.id.txt_total_sanctioned_area_value);
        txt_total_actual_area = (TextView) view.findViewById(R.id.txt_total_actual_area_value);
        txt_permissiable_area_value = (TextView) view.findViewById(R.id.txt_permissiable_area_value);
        editText_construction_value = (EditText) view.findViewById(R.id.editText_construction_value);
        editText_total_permissiblearea = (EditText) view.findViewById(R.id.editText_total_permissiblearea);
        editText_total_actualarea = (EditText) view.findViewById(R.id.editText_total_actualarea);
        textview_aspercompletion_result = (TextView) view.findViewById(R.id.textview_aspercompletion_result);
        textview_totalconstructionvalue_result = (TextView) view.findViewById(R.id.textview_totalconstructionvalue_result);


    }


    /* add floor*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_addFloors:
                checkValidationforFloors();
                break;
        }
    }

    public void checkValidationforFloors() {

        image_addFloors.setVisibility(View.INVISIBLE);
        if (rotateloading != null) {
            rotateloading.start();
        }

        if (!general.isEmpty(editText_floors_measu.getText().toString())) {

            if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getNoOfFloors()))) {
                if (Singleton.getInstance().indProperty.getNoOfFloors() == 0) {
                    // TODO - If floor is created from first time (Start Inspection)
                    Singleton.getInstance().is_new_floor_created = true;
                } else {
                    // TODO - If floor is created form second time (Edit Inspection)
                    Singleton.getInstance().is_new_floor_created = false;
                }
            } else {
                Singleton.getInstance().is_new_floor_created = false;
            }


            // check offline or not
            is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);
            if (is_offline) {
                // offline
                Singleton.getInstance().is_new_floor_created = true;
            }


            /*******
             * Check calculation for the manual floorno textbox and generated floors dynamic list
             * ******/
            final String floorno_textbox = editText_floors_measu.getText().toString().trim();
            int floorsnoList = Singleton.getInstance().indProperty.getNoOfFloors();

            if (!general.isEmpty(floorno_textbox)) {
                int floorno = Integer.valueOf(floorno_textbox);

                if (floorsnoList != 0) {
                    if (floorno == floorsnoList) {
                        //Todo floors manual entry and its generate list equals same
                        Singleton.getInstance().floorsEntryList = true;
                    } else if (floorno < floorsnoList) {
                        //Todo floors manual entry and it has to delete from floorlist
                        Singleton.getInstance().floorsEntryList = false;
                        Singleton.getInstance().floorsDeleteList = true;
                    } else if (floorno > floorsnoList) {
                        //Todo floors manual entry and its generate list has to add from floorlist
                        Singleton.getInstance().floorsEntryList = false;
                        Singleton.getInstance().floorsDeleteList = false;
                    }
                } else {
                    if (floorno == floorsnoList) {
                        //Todo floors manual entry and its generate list equals same
                        Singleton.getInstance().floorsEntryList = true;
                    } else {
                        // Todo floors is empty then make initial as false for start inspection
                        Singleton.getInstance().floorsEntryList = false;
                        Singleton.getInstance().floorsDeleteList = false;
                    }
                }
            }


            /*******
             * User Floor Number equals to Floors List
             * ******/

            /*****
             * User Floor Number and Floors List calls for Delete Method
             * *****/
            if (Singleton.getInstance().floorsDeleteList) {
                Snackbar.make(editText_floors_measu, getResources().getString(R.string.deleting_floors), Snackbar.LENGTH_SHORT).show();
            } else {
                /******
                 * User Floor Number and Floors List calls for Add Method
                 * *****/
                Snackbar.make(editText_floors_measu, getResources().getString(R.string.genearating_floors), Snackbar.LENGTH_SHORT).show();
                // Set as true for Internal composition
                Singleton.getInstance().is_edit_floor_satge = true;
                Singleton.getInstance().is_edit_floor_docarea = true;
                Singleton.getInstance().is_edit_floor_age = true;
                // Set as true for Internal composition
                Singleton.getInstance().is_edit_floor_hall = true;
                Singleton.getInstance().is_edit_floor_kitchen = true;
                Singleton.getInstance().is_edit_floor_bedroom = true;
                Singleton.getInstance().is_edit_floor_bath = true;
                Singleton.getInstance().is_edit_floor_shop = true;
            }

            editText_floors_measu.setError(null);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (!Singleton.getInstance().is_new_floor_created) {
                        // Edit case
                        if (Singleton.getInstance().floorsDeleteList) {
                            // Delete case
                            if (Connectivity.isConnected(getActivity())) {
                                int floorno = Integer.valueOf(floorno_textbox);
                                Singleton.getInstance().indProperty.setNoOfFloors(floorno);
                                String floorsno = editText_floors_measu.getText().toString().trim();
                                initEnterFloorNumbersViews(floorsno);
                                initEnterFloorInternalViews(floorsno);
                                getValuationDynamicFloorsResult(floorsno);
                                // Todo Delete Floors as per the floor numbers in edit Inspection
                                InitiateDeleteFloorsWebservice();
                            } else {
                                internet_check_box("edit_inspec");
                            }
                        } else {
                            // Add case
                            int floorno = Integer.valueOf(floorno_textbox);
                            Singleton.getInstance().indProperty.setNoOfFloors(floorno);
                            String floorsno = editText_floors_measu.getText().toString().trim();
                            initEnterFloorNumbersViews(floorsno);
                            initEnterFloorInternalViews(floorsno);
                            getValuationDynamicFloorsResult(floorsno);
                            NotifyDataSetChangedfloors();
                        }

                    } else {
                        // new case - // Case refresh
                        int floorno = Integer.valueOf(floorno_textbox);
                        Singleton.getInstance().indProperty.setNoOfFloors(floorno);
                        String floorsno = editText_floors_measu.getText().toString().trim();
                        initEnterFloorNumbersViews(floorsno);
                        initEnterFloorInternalViews(floorsno);
                        getValuationDynamicFloorsResult(floorsno);
                        NotifyDataSetChangedfloors();
                    }


                }
            }, 200);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Singleton isnewfloorcreate true
                    //if (Singleton.getInstance().is_new_floor_created) {
                    hideSoftKeyboard(image_addFloors);
                    image_addFloors.setVisibility(View.VISIBLE);
                    if (rotateloading != null) {
                        rotateloading.stop();
                    }
                    //}
                }
            }, 3000);


        } else {
            editText_floors_measu.setError(getResources().getString(R.string.err_no_floor));
            image_addFloors.setVisibility(View.VISIBLE);
            if (rotateloading != null) {
                rotateloading.stop();
            }
        }
    }

    private void initEnterFloorNumbersViews(String floors_no) {

        if (Singleton.getInstance().indPropertyFloors != null)
            if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                list = new ArrayList<>();
                if (floors_no.equalsIgnoreCase("")) {
                    System.err.print("Floors No: Empty");
                } else {

                    /*******
                     * Show the Floors from API
                     * ******/
                    for (int i = 0; i < Singleton.getInstance().indPropertyFloors.size(); i++) {
                        IndPropertyFloor stepsModel = new IndPropertyFloor();
                        int id = Singleton.getInstance().indPropertyFloors.get(i).getConstructionStageId();
                        String name = Singleton.getInstance().indPropertyFloors.get(i).getFloorName();

                        stepsModel.setCaseId(Singleton.getInstance().aCase.getCaseId());
                        stepsModel.setConstructionStageId(Singleton.getInstance().indPropertyFloors.get(i).getConstructionStageId());
                        stepsModel.setFloorName(Singleton.getInstance().indPropertyFloors.get(i).getFloorName());
                        stepsModel.setPercentageCompletion(Singleton.getInstance().indPropertyFloors.get(i).getPercentageCompletion());
                        stepsModel.setSanctionedFloorArea(Singleton.getInstance().indPropertyFloors.get(i).getSanctionedFloorArea());
                        stepsModel.setDocumentFloorArea(Singleton.getInstance().indPropertyFloors.get(i).getDocumentFloorArea());
                        stepsModel.setMeasuredFloorArea(Singleton.getInstance().indPropertyFloors.get(i).getMeasuredFloorArea());
                        stepsModel.setPropertyAge(Singleton.getInstance().indPropertyFloors.get(i).getPropertyAge());
                        stepsModel.setResidualLife(Singleton.getInstance().indPropertyFloors.get(i).getResidualLife());
                        stepsModel.setPresentCondition(Singleton.getInstance().indPropertyFloors.get(i).getPresentCondition());
                        stepsModel.setFloorNo(Singleton.getInstance().indPropertyFloors.get(i).getFloorNo());

                        String dep = Singleton.getInstance().indPropertyFloors.get(i).getPercentageDepreciation();
                        stepsModel.setPercentageDepreciation(Singleton.getInstance().indPropertyFloors.get(i).getPercentageDepreciation());
                        stepsModel.setPropertyFloorUsageId(Singleton.getInstance().indPropertyFloors.get(i).getPropertyFloorUsageId());

                        stepsModel.setFlatHallNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatHallNo());
                        stepsModel.setFlatKitchenNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatKitchenNo());
                        stepsModel.setFlatBedroomNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatBedroomNo());
                        stepsModel.setFlatBathNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatBathNo());
                        stepsModel.setOfficeNo(Singleton.getInstance().indPropertyFloors.get(i).getOfficeNo());
                        stepsModel.setFlatPoojaNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatPoojaNo());

                        stepsModel.setFlatDinningNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatDinningNo());
                        stepsModel.setFloorName(Singleton.getInstance().indPropertyFloors.get(i).getFloorName());
                        stepsModel.setShopNo(Singleton.getInstance().indPropertyFloors.get(i).getShopNo());

                        floorusage = new ArrayList<>();
                        String value = Singleton.getInstance().indPropertyFloors.get(i).getPropertyFloorUsageId();
                        if (!General.isEmptyString(value)) {
                            if (value.contains(",")) {
                                floorusage.clear();
                                List<String> myList = new ArrayList<String>(Arrays.asList(value.split(",")));
                                floorusage.addAll(myList);
                            }
                        } else {
                            floorusage.clear();
                        }
                        stepsModel.setFloorUsage(floorusage);
                        list.add(stepsModel);

                    }

                    /*******
                     * For Extra Floors from the Current Floors
                     * ******/
                    int floors = Integer.valueOf(floors_no);
                    int previousfloors = Singleton.getInstance().indPropertyFloors.size();
                    if (floors > previousfloors) {
                        int future_floor = floors - previousfloors;
                        for (int i = previousfloors; i < floors; i++) {
                            IndPropertyFloor stepsModel = new IndPropertyFloor();
                            int finalI = i + 1;
                            stepsModel.setCaseId(Singleton.getInstance().aCase.getCaseId());
                            stepsModel.setFloorName(getResources().getString(R.string.floorname) + finalI);
                            stepsModel.setFloorUsage(floorusage);
                            stepsModel.setFloorNo(finalI);
                            list.add(stepsModel);


                            Singleton.getInstance().indPropertyFloors.add(stepsModel);
                        }
                    } else {
                        /*******
                         * For Floors Delete from the Current Floors as per the floor no
                         * ******/
                        int delete_floors = previousfloors - floors;
                        for (int i = floors; i < previousfloors; i++) {
                            if (i < list.size()) {
                                list.remove(i);
                            }
                            if (i < Singleton.getInstance().indPropertyFloors.size()) {
                                Singleton.getInstance().indPropertyFloors.remove(i);
                                i = i - 1;
                                previousfloors = previousfloors - 1;
                            }
                        }
                    }
                }

            } else {

                list = new ArrayList<>();
                if (!floors_no.equalsIgnoreCase("")) {
                    /* New Row adding */
                    int floors = Integer.valueOf(floors_no);
                    for (int i = 0; i < floors; i++) {
                        IndPropertyFloor stepsModel = new IndPropertyFloor();
                        int floorname_dynamic = i + 1;
                        stepsModel.setCaseId(Singleton.getInstance().aCase.getCaseId());
                        stepsModel.setFloorName(getResources().getString(R.string.floorname) + floorname_dynamic);
                        stepsModel.setConstructionStageId(0);
                        stepsModel.setFloorUsage(floorusage);
                        stepsModel.setFloorNo(floorname_dynamic);
                        list.add(stepsModel);

                        Singleton.getInstance().indPropertyFloors.add(stepsModel);
                    }
                }
            }

        listAdapter = new PropertyGeneralFloorAdapter(list, getActivity());
        llm = new LinearLayoutManager(getActivity());
        //Setting the adapter
        recyclerview_generalinfo.setAdapter(listAdapter);
        recyclerview_generalinfo.setLayoutManager(llm);

        if (list != null && list.isEmpty()) {
            textview_generl_info_measu.setVisibility(View.GONE);
            generalInfoMeasurement.setVisibility(View.GONE);
            llInternalComposition.setVisibility(View.GONE);
        } else {
            textview_generl_info_measu.setVisibility(View.VISIBLE);
            generalInfoMeasurement.setVisibility(View.VISIBLE);
            llInternalComposition.setVisibility(View.VISIBLE);
        }

    }

    private void initEnterFloorInternalViews(String floors_no) {


        if (Singleton.getInstance().indPropertyFloors != null)
            if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                floornolist = new ArrayList<>();
                if (floors_no.equalsIgnoreCase("")) {

                } else {

                    /*******
                     * Show the Floors from API
                     * ******/
                    for (int i = 0; i < Singleton.getInstance().indPropertyFloors.size(); i++) {
                        IndPropertyFloor steps2Model = new IndPropertyFloor();
                        int id = Singleton.getInstance().indPropertyFloors.get(i).getConstructionStageId();
                        String name = Singleton.getInstance().indPropertyFloors.get(i).getFloorName();
                        int caseid = Singleton.getInstance().indPropertyFloors.get(i).getCaseId();
                        Log.e("caseid internal floors:", caseid + "");

                        steps2Model.setCaseId(caseid_int);
                        steps2Model.setFloorName(Singleton.getInstance().indPropertyFloors.get(i).getFloorName());
                        steps2Model.setFlatHallNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatHallNo());
                        steps2Model.setFlatKitchenNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatKitchenNo());
                        steps2Model.setFlatBedroomNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatBedroomNo());
                        steps2Model.setFlatBathNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatBathNo());
                        steps2Model.setShopNo(Singleton.getInstance().indPropertyFloors.get(i).getShopNo());
                        steps2Model.setOfficeNo(Singleton.getInstance().indPropertyFloors.get(i).getOfficeNo());
                        steps2Model.setFlatPoojaNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatPoojaNo());
                        steps2Model.setFlatDinningNo(Singleton.getInstance().indPropertyFloors.get(i).getFlatDinningNo());
                        steps2Model.setFloorNo(Singleton.getInstance().indPropertyFloors.get(i).getFloorNo());

                        floornolist.add(steps2Model);

                        Singleton.getInstance().floorFromBackend = true;
                    }

                    /*******
                     * For Extra Floors from the Current Floors
                     * ******/
                    int floors = Integer.valueOf(floors_no);
                    int previousfloors = Singleton.getInstance().indPropertyFloors.size();
                    if (floors > previousfloors) {
                        int future_floor = floors - previousfloors;
                        for (int i = previousfloors; i < floors; i++) {
                            IndPropertyFloor steps2Model = new IndPropertyFloor();
                            steps2Model.setCaseId(Singleton.getInstance().aCase.getCaseId());
                            steps2Model.setFloorName(getResources().getString(R.string.floorname) + i + 1);
                            steps2Model.setFloorNo(i+1);
                            floornolist.add(steps2Model);
                        }
                    } else {
                        /*******
                         * For Floors Delete from the Current Floors as per the floor no
                         * ******/
                        int delete_floors = previousfloors - floors;
                        for (int i = floors; i < previousfloors; i++) {
                            if (i < floornolist.size()) {
                                floornolist.remove(i);
                                i = i - 1;
                                previousfloors = previousfloors - 1;
                            }
                        }
                      /*  int delete_floors = previousfloors - floors;
                        for (int i = floors; i < previousfloors && i >= 0; i--) {
                            floornolist.remove(i);
                        }*/
                    }
                }


            } else {
                floornolist = new ArrayList<>();
                if (!floors_no.equalsIgnoreCase("")) {
                    /* New Row adding */
                    int floors = Integer.valueOf(floors_no);
                    for (int i = 0; i < floors; i++) {
                        IndPropertyFloor steps2Model = new IndPropertyFloor();
                        int floorname_dynamic = i + 1;
                        steps2Model.setCaseId(caseid_int);
                        steps2Model.setFloorName(getResources().getString(R.string.floorname) + floorname_dynamic);
                        steps2Model.setFloorNo(floorname_dynamic);
                        floornolist.add(steps2Model);
                        Singleton.getInstance().floorFromBackend = false;
                    }
                }
            }

        internalfloorlistAdapter = new InternalFloorNewAdapter(floornolist, getActivity());
        lfm = new LinearLayoutManager(getActivity());
        //Setting the adapter
        recyclerview_internal.setAdapter(internalfloorlistAdapter);
        recyclerview_internal.setLayoutManager(lfm);
    }

    private void setFloorNoInternals(ArrayList<InternalFloorModel> internalFloorModels) {
        internalFloorModels.clear();
        InternalFloorModel floorModelw = new InternalFloorModel();
        floorModelw.setId(0); //-1
        floorModelw.setName("Select");
        internalFloorModels.add(floorModelw);
        for (int i = 1; i <= 11; i++) {
            InternalFloorModel floorModel = new InternalFloorModel();
            floorModel.setId(i);
            int setId = i - 1;
            floorModel.setName("" + setId);
            floorModel.setFloorid(setId);
            internalFloorModels.add(floorModel);
        }
    }


    public void getStatusfromResult() {
        String status = "";
        if (Singleton.getInstance().aCase != null) {
            status = String.valueOf(Singleton.getInstance().aCase.getStatus());
            if (!general.isEmpty(status)) {
                //  if (status.equalsIgnoreCase("2")) { //Edit inspection
                String floorsno = String.valueOf(Singleton.getInstance().indProperty.getNoOfFloors());
                if (!general.isEmpty(floorsno)) {
                    initValuationPermissibleFloors(floorsno);
                } else {
                    general.CustomToast("Floor No. empty");
                }
            }
        }
    }

    public void getValuationDynamicFloorsResult(String floorsno) {

        String status = "";
        if (!general.isEmpty(floorsno)) {
            Log.e("valuation_floor", floorsno);
            initValuationPermissibleFloors(floorsno);
        } else {
            general.CustomToast("Floor No. empty");
        }
    }

    /****** Get Valuation for permissible floor *******/
    public void initValuationPermissibleFloors(String floors_no) {
        floorsValuation = new ArrayList<>();
        if (general.isEmpty(floors_no)) {
            Log.e("ValuationPermissible", "Floors empty");
        } else {

            if (Singleton.getInstance().indPropertyFloorsValuations.size() > 0) {
                Log.e("ValuationPermissible", "Floors created");
                for (int i = 0; i < Singleton.getInstance().indPropertyFloorsValuations.size(); i++) {
                    IndPropertyFloorsValuation stepsModel = Singleton.getInstance().indPropertyFloorsValuations.get(i);
                    //IndPropertyFloorsValuation stepsModel = new IndPropertyFloorsValuation();
                    int id = Singleton.getInstance().indPropertyFloorsValuations.get(i).getCaseId();
                    int floorno = Singleton.getInstance().indPropertyFloorsValuations.get(i).getFloorNo();

                    stepsModel.setCaseId(caseid_int);
                    stepsModel.setDocumentConstrRate(Singleton.getInstance().indPropertyFloorsValuations.get(i).getDocumentConstrRate());
                    stepsModel.setDocumentConstrValue(Singleton.getInstance().indPropertyFloorsValuations.get(i).getDocumentConstrValue());
                    stepsModel.setMeasuredConstrRate(Singleton.getInstance().indPropertyFloorsValuations.get(i).getMeasuredConstrRate());
                    stepsModel.setMeasuredConstrValue(Singleton.getInstance().indPropertyFloorsValuations.get(i).getMeasuredConstrValue());
                    stepsModel.setFloorNo(Singleton.getInstance().indPropertyFloorsValuations.get(i).getFloorNo());
                    stepsModel.setFloorDepreciationValue(Singleton.getInstance().indPropertyFloorsValuations.get(i).getFloorDepreciationValue());
                    stepsModel.setFloorCarpetArea(Singleton.getInstance().indPropertyFloorsValuations.get(i).getFloorCarpetArea());
                    floorsValuation.add(stepsModel);
                }

                int floors = Integer.valueOf(floors_no);
                int previousfloors = Singleton.getInstance().indPropertyFloorsValuations.size();
                if (floors > previousfloors) {
                    int future_floor = floors - previousfloors;
                    for (int i = previousfloors; i < floors; i++) {
                        IndPropertyFloorsValuation stepsModel = new IndPropertyFloorsValuation();
                        stepsModel.setCaseId(caseid_int);
                        stepsModel.setFloorNo(i+1);
                        stepsModel.setDocumentConstrValue("");
                        stepsModel.setDocumentConstrRate("");
                        stepsModel.setMeasuredConstrValue("");
                        stepsModel.setMeasuredConstrRate("");
                        stepsModel.setFloorDepreciationValue("");
                        //stepsModel.setCaseId(caseid_int);
                        floorsValuation.add(stepsModel);

                        Singleton.getInstance().indPropertyFloorsValuations.add(stepsModel);
                    }
                } else {
                    /*******
                     * For Floors Delete from the Current Floors as per the floor no
                     * ******/
                    int delete_floors = previousfloors - floors;
                    for (int i = floors; i < previousfloors; i++) {
                        if (i < floorsValuation.size()) {
                            floorsValuation.remove(i);
                        }
                        if (i < Singleton.getInstance().indPropertyFloorsValuations.size()) {
                            Singleton.getInstance().indPropertyFloorsValuations.remove(i);
                            i = i - 1;
                            previousfloors = previousfloors - 1;
                        }
                    }
                }


            } else {

                if (!general.isEmpty(floors_no)) {

                    int floors_valsize = Integer.valueOf(floors_no);
                    floorsValuation = new ArrayList<>();
                    for (int i = 0; i < floors_valsize; i++) {
                        IndPropertyFloorsValuation stepsModel = new IndPropertyFloorsValuation();
                        stepsModel.setCaseId(caseid_int);
                        stepsModel.setFloorNo(i+1);
                        stepsModel.setDocumentConstrValue("");
                        stepsModel.setDocumentConstrRate("");
                        stepsModel.setMeasuredConstrValue("");
                        stepsModel.setMeasuredConstrRate("");
                        stepsModel.setFloorDepreciationValue("");
                        //stepsModel.setCaseId(caseid_int);
                        floorsValuation.add(stepsModel);
                        Singleton.getInstance().indPropertyFloorsValuations = floorsValuation;
                        Log.e("ValuationPermissible", "Floors1 created");

                    }
                }
            }
        }


       /* if (valuationPermissibleAreaAdapter != null) {
            valuationPermissibleAreaAdapter = new ValuationPermissibleAreaAdapter(floorsValuation, FragmentBuilding.listAdapter.getStepList(), fragmentActivity);
            recyclerview_permissiblearea.setAdapter(valuationPermissibleAreaAdapter);

            listActualAdapter = new ValuationActualAreaAdapter(floorsValuation, FragmentBuilding.listAdapter.getStepList(), fragmentActivity);
            recyclerview_actualarea.setAdapter(listActualAdapter);
        }*/

        llsanctionedArea = new LinearLayoutManager(getActivity());
        valuationPermissibleAreaAdapter = new ValuationPermissibleAreaAdapter(floorsValuation, listAdapter.getStepList(), fragmentActivity);
        recyclerview_permissiblearea.setAdapter(valuationPermissibleAreaAdapter);
        recyclerview_permissiblearea.setLayoutManager(llsanctionedArea);


        llactualArea = new LinearLayoutManager(getActivity());
        listActualAdapter = new ValuationActualAreaAdapter(floorsValuation, listAdapter.getStepList(), fragmentActivity);
        recyclerview_actualarea.setAdapter(listActualAdapter);
        recyclerview_actualarea.setLayoutManager(llactualArea);


    }

    /*******
     * Todo Delete Floors API Call in Edit Inspection
     * Suganya Integration
     * ******/
    private void InitiateDeleteFloorsWebservice() {
        if (Connectivity.isConnected(getActivity())) {
            general.showloading(getActivity());
            InitiateDeleteFloorsTask();
        } else {
            general.hideloading();
            Connectivity.showNoConnectionDialog(getActivity());
        }
    }

    private void InitiateDeleteFloorsTask() {

        LinkedTreeMap<String, Object> deleteMap = new LinkedTreeMap<>();
        deleteMap.put("DYNAMICFLOOR", Singleton.getInstance().indPropertyFloors);
        deleteMap.put("INDFLOORVALUATION", Singleton.getInstance().indPropertyFloorsValuations);
        deleteMap.put("NOOFFLOOR", editText_floors_measu.getText().toString().trim());


        String url = general.ApiBaseUrl() + SettingsUtils.DeleteFloors;

        JsonRequestData requestUrlData = new JsonRequestData();
        requestUrlData.setInitQueryUrl(url);
        requestUrlData.setCaseId(caseid_str);

        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(RequestParam.DeleteFloorRequestParams(requestUrlData));
        requestData.setMainJson(new Gson().toJson(deleteMap));
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        requestData.setRequestBody(RequestParam.SaveCaseInspectionRequestParams(requestData));

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(),
                requestData, SettingsUtils.PUT_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                if (requestData.isSuccessful()) {
                    String response = requestData.getResponse();
                    parseDeleteFloorsDataResponse(response);
                } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
                    general.hideloading();
                    General.sessionDialog(getActivity());
                } else {
                    general.hideloading();
                    General.customToast(getActivity().getString(R.string.something_wrong),
                            getActivity());
                }
            }
        });
        webserviceTask.execute();
    }

    private void parseDeleteFloorsDataResponse(String response) {

        DataResponse dataResponse = ResponseParser.parseSaveCaseInspectionResponse(response);
        String result = "";
        if (response != null) {
            result = dataResponse.status;
            msg = dataResponse.msg;
            info = dataResponse.info;
        } else {
            general.hideloading();
            general.CustomToast(getResources().getString(R.string.serverProblem));
        }

        if (result != null) {
            if (result.equals("1")) {
                // general.CustomToast(msg);

                general.hideloading();

                hideSoftKeyboard(image_addFloors);
                image_addFloors.setVisibility(View.VISIBLE);
                if (rotateloading != null) {
                    rotateloading.stop();
                }

                Singleton.getInstance().floorsEntryList = false;
                Singleton.getInstance().floorsDeleteList = false;

                NotifyDataSetChangedfloors();
            } else if (result.equals("2")) {
                general.CustomToast(msg);
            } else if (result.equals("0")) {
                general.CustomToast(msg);
            }
        } else {
            general.hideloading();
            general.CustomToast(getResources().getString(R.string.serverProblem));
        }
    }


    @SuppressLint("SetTextI18n")
    private void NotifyDataSetChangedfloors() {

        if (Singleton.getInstance().indPropertyFloors != null) {
            if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                int average_total = general.getCompletedSumValue(Singleton.getInstance().indPropertyFloors);
                if (textview_comp_total != null) {
                    textview_comp_total.setText("" + average_total);
                    // set Aspercom
                    aspercompletion_val("" + average_total);
                }

                // Total Doc Area
                int total_documentarea = general.getDocSumValue(Singleton.getInstance().indPropertyFloors);
                float total_documentarea_float = general.getDocSumValue_float(Singleton.getInstance().indPropertyFloors);
                if (textview_doc_total != null) {
                    //FragmentBuilding.textview_doc_total.setText("" + total_documentarea);
                    textview_doc_total.setText("" + total_documentarea_float);
                    if (txt_total_sanctioned_area != null)
                        txt_total_sanctioned_area.setText("" + total_documentarea_float);

                    constrction_measurment(String.valueOf(total_documentarea));
                } else {
                    constrction_measurment("");
                }

                // Total Doc Area
                int total_actualarea = general.getMeasureSumValue(Singleton.getInstance().indPropertyFloors);
                float total_actualarea_float = general.getMeasureSumValue_float(Singleton.getInstance().indPropertyFloors);

                if (textview_actual_total != null) {
                    //FragmentBuilding.textview_actual_total.setText("" + total_actualarea);
                    textview_actual_total.setText("" + total_actualarea_float);
                    if (txt_total_actual_area != null)
                        txt_total_actual_area.setText("" + total_actualarea_float);
                    constrction_actual(String.valueOf(total_actualarea));
                } else {
                    constrction_actual("");
                }


                float total_permissiable_area_value = general.getPermissibleAreaSumValue_float(Singleton.getInstance().indPropertyFloors);
                if (txt_permissiable_area_value != null) {
                    txt_permissiable_area_value.setText("" + total_permissiable_area_value);
                }

            }
        }
    }


    @SuppressLint("SetTextI18n")
    public static void aspercompletion_val(String as_per_com) {
        if (!general.isEmpty(as_per_com)) {
            if (as_per_com.contains(",")) {
                editText_aspercompletion.setText(general.ReplaceCommaSaveToString(as_per_com));
            } else {
                editText_aspercompletion.setText("" + as_per_com);
            }
        }
    }

    public void internet_check_box(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        builder.setTitle("Network Information");
        builder.setMessage("Please check your Internet connection");


        if (enable_offline_button) {
            // Show offline Button
            builder.setNeutralButton("Save offline", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (str.equalsIgnoreCase("start_inspec")) {
                        // TODO - Start_inspec - Add the offline case to the modal
                    } else {
                        // TODO - Edit_inspec - Add the offline case to the modal
                        // set as offline
                        SettingsUtils.getInstance().putValue(SettingsUtils.is_offline, true);
                        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);

                        int check_offline_data = appDatabase.interfaceOfflineDataModelQuery().get_case_list("" + Singleton.getInstance().aCase.getCaseId()).size();
                        if (check_offline_data == 0) {
                            ArrayList<DataModel> dataModel_list = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getCase_caseID("" + Singleton.getInstance().aCase.getCaseId());
                            // Get the online modal data
                            DataModel dataModel = dataModel_list.get(0);
                            // Init the offlinecase modal
                            OfflineDataModel offlineDataModel = new OfflineDataModel();
                            offlineDataModel.setCaseId(dataModel.getCaseId());
                            offlineDataModel.setApplicantName(dataModel.getApplicantName());
                            offlineDataModel.setApplicantContactNo(dataModel.getApplicantContactNo());
                            offlineDataModel.setPropertyAddress(dataModel.getPropertyAddress());
                            offlineDataModel.setContactPersonName(dataModel.getContactPersonName());
                            offlineDataModel.setContactPersonNumber(dataModel.getContactPersonNumber());
                            offlineDataModel.setBankName(dataModel.getBankName());
                            offlineDataModel.setBankBranchName(dataModel.getBankBranchName());
                            offlineDataModel.setBankId(dataModel.getBankId());
                            offlineDataModel.setAgencyBranchId(dataModel.getAgencyBranchId());
                            offlineDataModel.setPropertyType(dataModel.getPropertyType());
                            offlineDataModel.setTypeID(dataModel.getTypeID());
                            offlineDataModel.setAssignedAt(dataModel.getAssignedAt());
                            offlineDataModel.setReportChecker(dataModel.getReportChecker());
                            offlineDataModel.setStatus(dataModel.getStatus());
                            offlineDataModel.setReportDispatcher(dataModel.getReportDispatcher());
                            offlineDataModel.setFieldStaff(dataModel.getFieldStaff());
                            offlineDataModel.setAssignedTo(dataModel.getAssignedTo());
                            offlineDataModel.setReportMaker(dataModel.getReportMaker());
                            offlineDataModel.setReportFinalizer(dataModel.getReportFinalizer());
                            offlineDataModel.setPropertyId(dataModel.getPropertyId());
                            offlineDataModel.setReportFile(dataModel.getReportFile());
                            offlineDataModel.setReportId(dataModel.getReportId());
                            offlineDataModel.setReportTemplateId(dataModel.getReportTemplateId());
                            offlineDataModel.setSignature1(dataModel.getSignature1());
                            offlineDataModel.setPropertyCategoryId(dataModel.getPropertyCategoryId());
                            offlineDataModel.setSignature2(dataModel.getSignature2());
                            offlineDataModel.setEmployeeName(dataModel.getEmployeeName());
                            offlineDataModel.setRole(dataModel.getRole());
                            offlineDataModel.setStatusId(dataModel.getStatusId());
                            offlineDataModel.setShowoffline(true);
                            offlineDataModel.setOfflinecase(false);
                            offlineDataModel.setIs_property_changed(false);
                            offlineDataModel.setSync_edit(true);
                            // Insert into offline database
                            appDatabase.interfaceOfflineDataModelQuery().insertAll(offlineDataModel);
                        }

                        // updateOfflineDataModel > offlinecase as true
                        appDatabase.interfaceOfflineDataModelQuery().updateOfflineDataModel("" + Singleton.getInstance().aCase.getCaseId(), true);
                        // update_sync_edit - true
                        appDatabase.interfaceOfflineDataModelQuery().update_sync_edit_DataModel("" + Singleton.getInstance().aCase.getCaseId(), true);

                        // Add the offline case to the modal
                        if (!general.isEmpty(String.valueOf(Singleton.getInstance().aCase.getCaseId()))) {
                            // offline set the caseID and propertyID
                            OflineCase oflineCase = new OflineCase();
                            oflineCase.setCaseId(Singleton.getInstance().aCase.getCaseId());
                            oflineCase.setPropertyId(Singleton.getInstance().aCase.getPropertyId());
                            // Room Delete - OfflineCase
                            appDatabase.interfaceOfflineCaseQuery().deleteRow(oflineCase.getCaseId());
                            // Room Add - OfflineCase
                            appDatabase.interfaceOfflineCaseQuery().insertAll(oflineCase);

                            int PropertyId_is = Singleton.getInstance().aCase.getPropertyId();
                            // Room - delete lat long
                            appDatabase.interfaceLatLongQuery().deleteRow(caseid_int);
                            LatLongDetails latLongDetails = new LatLongDetails();
                            latLongDetails.setCaseId(caseid_int);
                            latLongDetails.setLatLongDetails(Singleton.getInstance().latlng_details);
                            // Room - add lat long
                            appDatabase.interfaceLatLongQuery().insertAll(latLongDetails);
                            // Photo delete and add
                            // Room Delete
                            appDatabase.interfaceGetPhotoQuery().deleteRow(PropertyId_is);
                            GetPhoto dummy_cameraImage = new GetPhoto();
                            dummy_cameraImage.setDefaultimage(true);
                            dummy_cameraImage.setPropertyId(PropertyId_is);
                            // Room Add
                            appDatabase.interfaceGetPhotoQuery().insertAll(dummy_cameraImage);

                            GetPhoto dummy_galleryImage = new GetPhoto();
                            dummy_galleryImage.setDefaultimage(true);
                            dummy_galleryImage.setPropertyId(PropertyId_is);
                            // Room Add
                            appDatabase.interfaceGetPhotoQuery().insertAll(dummy_galleryImage);

                            JSONArray jsonArray = new JSONArray();
                            for (int x = 0; x < PhotoLatLong.GetPhoto_list_response.size(); x++) {
                                if (!PhotoLatLong.GetPhoto_list_response.get(x).isDefaultimage()) {
                                    GetPhoto getPhoto = new GetPhoto();
                                    getPhoto.setId(0);
                                    // getPhoto.setId(PhotoLatLong.GetPhoto_list_response.get(x).getId());
                                    getPhoto.setLogo(PhotoLatLong.GetPhoto_list_response.get(x).getLogo());
                                    getPhoto.setTitle(PhotoLatLong.GetPhoto_list_response.get(x).getTitle());
                                    getPhoto.setPropertyId(PhotoLatLong.GetPhoto_list_response.get(x).getPropertyId());
                                    getPhoto.setDefaultimage(false);
                                    getPhoto.setNewimage(false);
                                    // Check the Base64Image should be less than 19,80,000 ...
                                    if (PhotoLatLong.GetPhoto_list_response.get(x).getLogo().length() < 1980000) {
                                        // Room Add
                                        appDatabase.interfaceGetPhotoQuery().insertAll(getPhoto);
                                    }
                                }
                            }
                        }

                        // Generate the Floor again
                        checkValidationforFloors();

                        // hit_photo_api > false
                        Singleton.getInstance().hit_photo_api = true;
                        general.CustomToast(getActivity().getResources().getString(R.string.case_moved_offline));
                    }
                }
            });
        }


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_SETTINGS);
                getActivity().startActivity(i);
                dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void initFloorViews() {
        list = new ArrayList<>();
        if (Singleton.getInstance().indPropertyFloors != null)
            if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                initEnterFloorNumbersViews(String.valueOf(Singleton.getInstance().indPropertyFloors.size()));
            } else {
                if (list == null || list.size() == 0) {
                    list = new ArrayList<>();

                    generalInfoMeasurement.setVisibility(View.GONE);
                    textview_generl_info_measu.setVisibility(View.GONE);
                }
                floorsGeneralRecyclerview();
            }
    }

    private void floorsGeneralRecyclerview() {
        listAdapter = new PropertyGeneralFloorAdapter(list, getActivity());
        llm = new LinearLayoutManager(getActivity());

        //Setting the adapter
        recyclerview_generalinfo.setAdapter(listAdapter);
        recyclerview_generalinfo.setLayoutManager(llm);
    }

    private void landMeasurement() {
        //  limit the 2 char after the decimal point
        editText_compound_permissiblearea.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        editText_as_per_measurement.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});


        if (Singleton.getInstance().indProperty != null) {

            //  Todo set the values for editext

            if (Singleton.getInstance().indProperty.getNoOfFloors() != 0) {
                editText_floors_measu.setText("" + Singleton.getInstance().indProperty.getNoOfFloors());
            } else {
                Log.e("Floor No.s text", "has null value");
            }

            if (Singleton.getInstance().indProperty.getApprovedNoOfFloors() != 0)
                editText_approved_floors.setText("" + Singleton.getInstance().indProperty.getApprovedNoOfFloors());
            if (!general.isEmpty(Singleton.getInstance().indProperty.getDocumentLandArea()))
                editText_compound_permissiblearea.setText(Singleton.getInstance().indProperty.getDocumentLandArea());
            if (!general.isEmpty(Singleton.getInstance().indProperty.getMeasuredLandArea()))
                editText_as_per_measurement.setText(Singleton.getInstance().indProperty.getMeasuredLandArea());
            if (!general.isEmpty(Singleton.getInstance().indProperty.getFloorDetails()))
                editText_Floor_details.setText(Singleton.getInstance().indProperty.getFloorDetails());

            final int completedSumValue = getCompletedSumValue(Singleton.getInstance().indPropertyFloors);
            textview_comp_total.setText("" + completedSumValue);

            final float docSumValue_float = general.getDocSumValue_float(Singleton.getInstance().indPropertyFloors);
            textview_doc_total.setText("" + docSumValue_float);
            txt_total_sanctioned_area.setText("" + docSumValue_float);

            final float measureSumValue_float = general.getMeasureSumValue_float(Singleton.getInstance().indPropertyFloors);
            txt_total_actual_area.setText("" + measureSumValue_float);
            final float permissibleSumValue_float = general.getPermissibleAreaSumValue_float(Singleton.getInstance().indPropertyFloors);
            txt_permissiable_area_value.setText("" + permissibleSumValue_float);


            int areaUnit = Singleton.getInstance().indProperty.getMeasuredLandAreaUnit();
            if (areaUnit != 0)
                spinner_measurement1.setSelection(areaUnit);

            spinner_measurement1.setSelection(areaUnit);
            if (Singleton.getInstance().measurements_list.size() > 0) {
                measurment_floor_id = String.valueOf(Singleton.getInstance().measurements_list.get(1).getMeasureUnitId());
                get_constrction_land(Singleton.getInstance().measurements_list.get(1).getMeasureUnitId());
            }
        }

        initFloorViews();
        initInternalCompositionFloorViews();








      /*  if (Singleton.getInstance().indProperty.getApprovedNoOfFloors() != 0)
            editText_approved_floors.setText("" + Singleton.getInstance().indProperty.getApprovedNoOfFloors());
        if (!general.isEmpty(Singleton.getInstance().indProperty.getDocumentLandArea()))
            editText_compound_permissiblearea.setText(Singleton.getInstance().indProperty.getDocumentLandArea());
        if (!general.isEmpty(Singleton.getInstance().indProperty.getMeasuredLandArea()))
            editText_as_per_measurement.setText(Singleton.getInstance().indProperty.getMeasuredLandArea());
        if (!general.isEmpty(Singleton.getInstance().indProperty.getFloorDetails()))
            editText_Floor_details.setText(Singleton.getInstance().indProperty.getFloorDetails());


        if (!general.isEmpty(Singleton.getInstance().indProperty.getFloorDetails()))
            editText_Floor_details.setText(Singleton.getInstance().indProperty.getFloorDetails());

*/

        editText_compound_permissiblearea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String charSequence = editText_compound_permissiblearea.getText().toString();
                    if (general.isEmpty(editText_as_per_measurement.getText().toString())) {
                        // If its empty
                        editText_as_per_measurement.setText(charSequence);
                        //TODO Interface
                        actual_measurment(charSequence);
                    }

                    editText_as_per_measurement.setError(null);
                    //TODO Interface
                    permission_measurment(charSequence);
                }
            }
        });


        editText_compound_permissiblearea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String charSequence = editText_compound_permissiblearea.getText().toString();
                    if (general.isEmpty(editText_as_per_measurement.getText().toString())) {
                        // If its empty
                        editText_as_per_measurement.setText(charSequence);
                        //TODO Interface
                        actual_measurment(charSequence);
                    }

                    editText_as_per_measurement.setError(null);
                    //TODO Interface
                    permission_measurment(charSequence);
                }
            }
        });


        /* LandMeasurement*/
        ArrayAdapter<Measurements> adapterMeasurements = new ArrayAdapter<>(getActivity(),
                R.layout.row_spinner_item, Singleton.getInstance().measurements_list);
        adapterMeasurements.setDropDownViewResource(R.layout.row_spinner_item_popup);

        spinner_measurement1.setAdapter(adapterMeasurements);

        spinner_measurement2.setAdapter(adapterMeasurements);

        listener1 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int measurementland = spinner_measurement1.getSelectedItemPosition();
                if (measurementland != 0) {
                    textview_error_measure_land.setVisibility(View.GONE);
                }


                Singleton.getInstance().indProperty.setDocumentLandAreaUnit(Singleton.getInstance().measurements_list.get(position).getMeasureUnitId());
                Singleton.getInstance().indProperty.setMeasuredLandAreaUnit(Singleton.getInstance().measurements_list.get(position).getMeasureUnitId());

                get_measurment_land(Singleton.getInstance().measurements_list.get(position).getMeasureUnitId(), Singleton.getInstance().measurements_list.get(position).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        spinner_measurement1.setOnItemSelectedListener(listener1);

        Singleton.getInstance().PropertyActualUsage_id.clear();
        Singleton.getInstance().PropertyActualUsage_name.clear();
        textview_actual_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog_dynamic("actual_usage");
                hideSoftKeyboard(textview_actual_usage);
            }
        });
        function_actual_usage();


        int areaUnit = Singleton.getInstance().indProperty.getMeasuredLandAreaUnit();
        if (areaUnit != 0)
            spinner_measurement1.setSelection(areaUnit);

        spinner_measurement1.setSelection(areaUnit);
       /* spinner_measurement1.post(new Runnable() {
            @Override
            public void run() {

            }
        });*/


        textview_actual_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog_dynamic("actual_usage");
                hideSoftKeyboard(textview_actual_usage);
            }
        });

    }

    private void function_actual_usage() {
        // clear the array and set the floor list in array
        Inter_floors_list = new ArrayList<>();
        Inter_floors_list = Singleton.getInstance().propertyActualUsages_list;
        // check Floor Dropdown is empty
        if (Inter_floors_list.size() > 0) {
            String getIntFloorId = Singleton.getInstance().indProperty.getPropertyActualUsageId();
            // check selected array is empty
            if (!general.isEmpty(getIntFloorId)) {
                // selected array convert to comma separate array
                List<String> Inter_floors_list_selected_id_commo = new ArrayList<>();
                Inter_floors_list_selected_id_commo = new ArrayList<String>(Arrays.asList(getIntFloorId.split(",")));
                if (Inter_floors_list_selected_id_commo.size() > 0) {
                    // Loop the Floor and get ID > check If selected array contains ID
                    for (int x = 0; x < Inter_floors_list.size(); x++) {
                        if (Inter_floors_list_selected_id_commo.toString().contains("" + Inter_floors_list.get(x).getPropertyActualUsageId())) {
                            for (int y = 0; y < Inter_floors_list_selected_id_commo.size(); y++) {
                                // Loop the selected array > If the selected array ID equal to Floor ID > save ID and name to Instance
                                int one_by_one_id = Integer.parseInt(Inter_floors_list_selected_id_commo.get(y));
                                if (Inter_floors_list.get(x).getPropertyActualUsageId() == one_by_one_id) {
                                    Singleton.getInstance().PropertyActualUsage_id.add(Inter_floors_list.get(x).getPropertyActualUsageId());
                                    Singleton.getInstance().PropertyActualUsage_name.add(Inter_floors_list.get(x).getName());
                                }
                            }
                            textview_actual_usage.setText(general.remove_array_brac_and_space(Singleton.getInstance().PropertyActualUsage_name.toString()));
                        }
                    }
                }
            } else {
                textview_actual_usage.setText(getResources().getString(R.string.select));
            }
        }
    }

    private void showdialog_dynamic(final String type_) {
        final String type_of_dialog = type_;

        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.multiselect_checkbox, null);
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(getContext());
        alertdialogBuilder.setView(view);
        final AlertDialog alertDialog = alertdialogBuilder.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView textview_heading = (TextView) alertDialog.findViewById(R.id.textview_heading);
        Button btn_cancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
        Button btn_save = (Button) alertDialog.findViewById(R.id.btn_save);
        RecyclerView recyclerview_dialog = (RecyclerView) alertDialog.findViewById(R.id.recyclerview_dialog);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_dialog.setLayoutManager(linearLayoutManager);

        if (type_of_dialog.equalsIgnoreCase("actual_usage")) {
            // Type -> actual_usage
            textview_heading.setText(getResources().getString(R.string.actualusage));
            String selectedId = Singleton.getInstance().indProperty.getPropertyActualUsageId();
            PropertyActualUsageAdapter propertyActualUsageAdapter = new PropertyActualUsageAdapter(getActivity(), Inter_floors_list, selectedId);
            recyclerview_dialog.setAdapter(propertyActualUsageAdapter);
        }


        textview_heading.setTypeface(general.mediumtypeface());

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type_of_dialog.equalsIgnoreCase("actual_usage")) {
                    // Type -> Floor
                    if (Singleton.getInstance().PropertyActualUsage_id.size() > 0) {
                        String PropertyActualUsage_id = general.remove_array_brac_and_space(Singleton.getInstance().PropertyActualUsage_id.toString());
                        // set the Inter floor ID
                        Singleton.getInstance().indProperty.setPropertyActualUsageId(PropertyActualUsage_id);
                        // setText to the floor text
                        String PropertyActualUsage_name = general.remove_array_brac_and_space(Singleton.getInstance().PropertyActualUsage_name.toString());
                        textview_actual_usage.setText(PropertyActualUsage_name);
                    } else {
                        // clear all the ID and dummy data and clear the settext
                        Singleton.getInstance().indProperty.setPropertyActualUsageId("");
                        textview_actual_usage.setText(getResources().getString(R.string.select));
                    }
                    Log.e("PropertyActualUsage_id", "::: " + Singleton.getInstance().PropertyActualUsage_id);
                    Log.e("PropertyActuaUs_name", ":: " + Singleton.getInstance().PropertyActualUsage_name);
                }

                alertDialog.dismiss();
            }
        });
    }


    public void displayValuationDetails() {

        if (Singleton.getInstance().indPropertyValuation != null) {
            String asperComPercentage = Singleton.getInstance().indPropertyValuation.getCompletionPercentage();
            String asperCompValue = Singleton.getInstance().indPropertyValuation.getTotalValueAtCompletion();

            if (!general.isEmpty(asperComPercentage)) {
                if (asperComPercentage.contains(","))
                    editText_aspercompletion.setText(general.ReplaceCommaSaveToString(asperComPercentage));
                else
                    editText_aspercompletion.setText(asperComPercentage);
            }

            if (!general.isEmpty(asperCompValue)) {
                if (asperCompValue.contains(","))
                    textview_aspercompletion_result.setText(general.DecimalFormattedCommaString(String.valueOf(asperCompValue)));

                else
                    textview_aspercompletion_result.setText(asperCompValue);
            }
        }


    }

    private void constructionValuation() {
        /* TODO - Display the final text after after some second  */
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getGuidelineValue())) {
            //  editText_guideline_value.setText(Singleton.getInstance().indPropertyValuation.getGuidelineValue());
            editText_guideline_value.setText(general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getGuidelineValue()));
        } else {
            editText_guideline_value.setText("");
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getConstructionDLCValue())) {
            //   editText_construction_value.setText(Singleton.getInstance().indPropertyValuation.getConstructionDLCValue());
            editText_construction_value.setText(general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getConstructionDLCValue()));
        } else {
            editText_construction_value.setText("");
        }


        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 6000);*/


    }

    private void realizableMapping() {
        spinner_realizable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(spinner_realizable);
                realizable_distress_popup(1, spinner_realizable);
            }
        });

        realizableList = new ArrayList<>();
        realizableList = general.Constructionval_common_array();
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getRealizationPercentage())) {
            /* values */
            boolean is_value_from_loop = false;
            for (int x = 0; x < realizableList.size(); x++) {
                if (realizableList.get(x).toString().replace("%", "").trim().equalsIgnoreCase(Singleton.getInstance().indPropertyValuation.getRealizationPercentage())) {
                    is_value_from_loop = true;
                }
            }
            if (is_value_from_loop) {
                /* From array */
                spinner_realizable.setText(Singleton.getInstance().indPropertyValuation.getRealizationPercentage());
                editText_realizable_custom.setText("");
                editText_realizable_custom.setVisibility(View.INVISIBLE);
            } else {
                /* Custom */
                spinner_realizable.setText(getResources().getString(R.string.custom));
                editText_realizable_custom.setVisibility(View.VISIBLE);
                String RealizationPercentage = Singleton.getInstance().indPropertyValuation.getRealizationPercentage();
                if (!general.isEmpty(RealizationPercentage)) {
                    editText_realizable_custom.setText(Singleton.getInstance().indPropertyValuation.getRealizationPercentage());
                }
            }
        } else {
            /* select */
            if (Singleton.getInstance().caseOtherDetailsModel != null && Singleton.getInstance().caseOtherDetailsModel.getData() != null && Singleton.getInstance().caseOtherDetailsModel.getData().get(0) != null) {
                if (Singleton.getInstance().caseOtherDetailsModel.getData().get(0).getRealizationPercentage() != null) {
                    spinner_realizable.setText(Singleton.getInstance().caseOtherDetailsModel.getData().get(0).getRealizationPercentage());
                } else {
                    spinner_realizable.setText(getResources().getString(R.string.select));
                }
            } else {
                spinner_realizable.setText(getResources().getString(R.string.select));
            }
            editText_realizable_custom.setText("");
            editText_realizable_custom.setVisibility(View.INVISIBLE);
        }

    }

    private void carpet() {
        spinner_carpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carpet_error.setVisibility(View.GONE);
                hideSoftKeyboard(spinner_carpet);
                realizable_distress_popup(3, spinner_carpet);
            }
        });

        carpetList = new ArrayList<>();
        carpetList = general.Constructionval_common_array_carpet();
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage())) {
            for (int x = 0; x < carpetList.size(); x++) {
                if (carpetList.get(x).toString().replace("%", "").trim().equalsIgnoreCase(Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage())) {
                    spinner_carpet.setText(Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage());
                }
            }
        } else {
            /* select */
            spinner_carpet.setText(getResources().getString(R.string.select));
        }
    }

    private void distressView() {
        spinner_distress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(spinner_distress);
                realizable_distress_popup(2, spinner_distress);
            }
        });

        distressList = new ArrayList<>();
        distressList = general.Constructionval_common_array();
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getDistressPercentage())) {
            /* values */
            boolean is_value_from_loop = false;
            for (int x = 0; x < distressList.size(); x++) {
                if (distressList.get(x).toString().replace("%", "").trim().equalsIgnoreCase(Singleton.getInstance().indPropertyValuation.getDistressPercentage())) {
                    is_value_from_loop = true;
                }
            }
            if (is_value_from_loop) {
                /* From array */
                spinner_distress.setText(Singleton.getInstance().indPropertyValuation.getDistressPercentage());
                editText_distress_custom.setText("");
                editText_distress_custom.setVisibility(View.INVISIBLE);
            } else {
                /* Custom */
                spinner_distress.setText(getResources().getString(R.string.custom));
                editText_distress_custom.setVisibility(View.VISIBLE);
                String DistressPercentage = Singleton.getInstance().indPropertyValuation.getDistressPercentage();
                if (!general.isEmpty(DistressPercentage)) {
                    editText_distress_custom.setText(Singleton.getInstance().indPropertyValuation.getDistressPercentage());
                }
            }
        } else {
            /* select */
            if (Singleton.getInstance().caseOtherDetailsModel != null && Singleton.getInstance().caseOtherDetailsModel.getData() != null && Singleton.getInstance().caseOtherDetailsModel.getData().get(0) != null) {
                if (Singleton.getInstance().caseOtherDetailsModel.getData().get(0).getDistressPercentage() != null) {
                    spinner_distress.setText(Singleton.getInstance().caseOtherDetailsModel.getData().get(0).getDistressPercentage());
                } else {
                    spinner_distress.setText(getResources().getString(R.string.select));
                }
            } else {
                spinner_distress.setText(getResources().getString(R.string.select));
            }
            editText_distress_custom.setText("");
            editText_distress_custom.setVisibility(View.INVISIBLE);
        }
    }

    public static boolean valid_distressValue() {
        boolean is_custom_valid = false;
        if (editText_distress_custom.getVisibility() == View.VISIBLE) {
            // distress_custom - > From Custom value
            String distress_custom = editText_distress_custom.getText().toString();
            if (!general.isEmpty(distress_custom)) {
                String total_value = textview_totalpropertyvalue_result.getText().toString();
                if ((!general.isEmpty(total_value)) && (!general.isEmpty(distress_custom))) {
                    int total_int = Integer.parseInt(general.ReplaceCommaSaveToString(total_value));
                    int distress_int = Integer.parseInt(distress_custom);
                    int final_realizationValue = general.getcutom_value(total_int, distress_int);
                    Singleton.getInstance().indPropertyValuation.setDistressValue("" + final_realizationValue);
                }
                Singleton.getInstance().indPropertyValuation.setDistressPercentage(distress_custom);
                editText_distress_custom.setError(null);
                is_custom_valid = true;
            } else {
                editText_distress_custom.requestFocus();
                editText_distress_custom.setError("Please enter distress custom value");
                is_custom_valid = false;
            }
        } else {
            // distress_custom - > From getDistressPercentage from the spinner
            String distress_custom = Singleton.getInstance().indPropertyValuation.getDistressPercentage();
            if (!general.isEmpty(distress_custom)) {
                String total_value = textview_totalpropertyvalue_result.getText().toString();
                if ((!general.isEmpty(total_value)) && (!general.isEmpty(distress_custom))) {
                    int total_int = Integer.parseInt(general.ReplaceCommaSaveToString(total_value));
                    int distress_int = Integer.parseInt(distress_custom);
                    int final_realizationValue = general.getcutom_value(total_int, distress_int);
                    Singleton.getInstance().indPropertyValuation.setDistressValue("" + final_realizationValue);
                }
            } else {
                Singleton.getInstance().indPropertyValuation.setDistressValue("");
            }
            is_custom_valid = true;
        }
        return is_custom_valid;
    }

    public static boolean valid_carpetValue() {
        boolean is_custom_valid = false;
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage())) {
            // not null
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getCarpetAreaTypeId())) {
                // not null
                is_custom_valid = true;
            } else {
                editText_remarks.requestFocus();
                // spinner_carpet_type.setError("Please select the carpet type");
                Toast.makeText(fragmentActivity, "Please select the carpet type", Toast.LENGTH_SHORT).show();
                is_custom_valid = false;
            }
        } else {
            is_custom_valid = true;
        }
        return is_custom_valid;
    }

    private void carpet_popup(final TextView textView) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.carpet_popup);

        TextView popuptitle = (TextView) dialog.findViewById(R.id.title);
        popuptitle.setTypeface(general.mediumtypeface());
        popuptitle.setText(getActivity().getResources().getString(R.string.carpet_per_type));

        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        RadioGroup id_radiogroup_stage = (RadioGroup) dialog.findViewById(R.id.id_radiogroup_stage);

        RadioButton id_radio_select = (RadioButton) dialog.findViewById(R.id.id_radio_select);
        RadioButton id_radio_per = (RadioButton) dialog.findViewById(R.id.id_radio_per);
        RadioButton id_radio_act = (RadioButton) dialog.findViewById(R.id.id_radio_act);
        RadioButton id_radio_sel = (RadioButton) dialog.findViewById(R.id.id_radio_sel);

        id_radio_select.setTypeface(general.mediumtypeface());
        id_radio_per.setTypeface(general.mediumtypeface());
        id_radio_act.setTypeface(general.mediumtypeface());
        id_radio_sel.setTypeface(general.mediumtypeface());


        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getCarpetAreaTypeId())) {
            int carpet_id = Integer.parseInt(Singleton.getInstance().indPropertyValuation.getCarpetAreaTypeId());
            if (carpet_id == 1) {
                id_radio_per.setChecked(true);
            } else if (carpet_id == 2) {
                id_radio_act.setChecked(true);
            } else if (carpet_id == 3) {
                id_radio_sel.setChecked(true);
            }
        } else {
            id_radio_select.setChecked(true);
        }

        id_radiogroup_stage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton id_radiogenearal = (RadioButton) dialog.findViewById(group.getCheckedRadioButtonId());
                //Log.e("id_radiogenearal_",": "+id_radiogenearal.getText().toString());
                String str_radiogenearal = id_radiogenearal.getText().toString();
                if (str_radiogenearal.equalsIgnoreCase("Select")) {
                    Singleton.getInstance().indPropertyValuation.setCarpetAreaTypeId("");
                } else if (str_radiogenearal.equalsIgnoreCase("Permissible Area")) {
                    Singleton.getInstance().indPropertyValuation.setCarpetAreaTypeId("1");
                } else if (str_radiogenearal.equalsIgnoreCase("Actual Area")) {
                    Singleton.getInstance().indPropertyValuation.setCarpetAreaTypeId("2");
                } else if (str_radiogenearal.equalsIgnoreCase("Selected Area")) {
                    Singleton.getInstance().indPropertyValuation.setCarpetAreaTypeId("3");
                }
                textView.setText(str_radiogenearal);

                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void carpetType() {
        spinner_carpet_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_error.setVisibility(View.GONE);
                spinner_carpet_type.setError(null);
                hideSoftKeyboard(spinner_carpet_type);
                carpet_popup(spinner_carpet_type);
            }
        });

        // spinner - carpet_typeList
        carpet_typeList = new ArrayList<>();
        carpet_typeList = general.carpettype_array();
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getCarpetAreaTypeId())) {
            int carpet_id = Integer.parseInt(Singleton.getInstance().indPropertyValuation.getCarpetAreaTypeId());
            spinner_carpet_type.setText(carpet_typeList.get(carpet_id));
        } else {
            /* select */
            spinner_carpet_type.setText(getResources().getString(R.string.select));
        }
    }

    private void realizable_distress_popup(final int type_is, final TextView textView) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.distress_popup);

        TextView popuptitle = (TextView) dialog.findViewById(R.id.title);
        popuptitle.setTypeface(general.mediumtypeface());

        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        RadioGroup id_radiogroup_stage = (RadioGroup) dialog.findViewById(R.id.id_radiogroup_stage);
        RadioButton id_radio_select = (RadioButton) dialog.findViewById(R.id.id_radio_select);
        RadioButton id_radio_50 = (RadioButton) dialog.findViewById(R.id.id_radio_50);
        RadioButton id_radio_55 = (RadioButton) dialog.findViewById(R.id.id_radio_55);
        RadioButton id_radio_60 = (RadioButton) dialog.findViewById(R.id.id_radio_60);
        RadioButton id_radio_65 = (RadioButton) dialog.findViewById(R.id.id_radio_65);
        RadioButton id_radio_70 = (RadioButton) dialog.findViewById(R.id.id_radio_70);
        RadioButton id_radio_75 = (RadioButton) dialog.findViewById(R.id.id_radio_75);
        RadioButton id_radio_80 = (RadioButton) dialog.findViewById(R.id.id_radio_80);
        RadioButton id_radio_85 = (RadioButton) dialog.findViewById(R.id.id_radio_85);
        RadioButton id_radio_90 = (RadioButton) dialog.findViewById(R.id.id_radio_90);
        RadioButton id_radio_95 = (RadioButton) dialog.findViewById(R.id.id_radio_95);
        RadioButton id_radio_100 = (RadioButton) dialog.findViewById(R.id.id_radio_100);
        RadioButton id_radio_Custom = (RadioButton) dialog.findViewById(R.id.id_radio_Custom);
        id_radio_select.setTypeface(general.mediumtypeface());
        id_radio_50.setTypeface(general.mediumtypeface());
        id_radio_55.setTypeface(general.mediumtypeface());
        id_radio_60.setTypeface(general.mediumtypeface());
        id_radio_65.setTypeface(general.mediumtypeface());
        id_radio_70.setTypeface(general.mediumtypeface());
        id_radio_75.setTypeface(general.mediumtypeface());
        id_radio_80.setTypeface(general.mediumtypeface());
        id_radio_85.setTypeface(general.mediumtypeface());
        id_radio_90.setTypeface(general.mediumtypeface());
        id_radio_95.setTypeface(general.mediumtypeface());
        id_radio_100.setTypeface(general.mediumtypeface());
        id_radio_Custom.setTypeface(general.mediumtypeface());

        boolean is_empty = true;
        if (type_is == 1) {
            // RealizationPercentage
            popuptitle.setText(getActivity().getResources().getString(R.string.realizable_per));
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getRealizationPercentage())) {
                is_empty = false;
                selected_value = Singleton.getInstance().indPropertyValuation.getRealizationPercentage();
            }
        } else if (type_is == 2) {
            // DistressPercentage
            popuptitle.setText(getActivity().getResources().getString(R.string.distress_per));
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getDistressPercentage())) {
                is_empty = false;
                selected_value = Singleton.getInstance().indPropertyValuation.getDistressPercentage();
            }
        } else if (type_is == 3) {
            id_radio_Custom.setVisibility(View.GONE);
            // DistressPercentage
            popuptitle.setText(getActivity().getResources().getString(R.string.carpet_per));
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage())) {
                is_empty = false;
                selected_value = Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage();
            }
        }

        if (!is_empty) {
            if (selected_value.equalsIgnoreCase("50")) {
                id_radio_50.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("55")) {
                id_radio_55.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("60")) {
                id_radio_60.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("65")) {
                id_radio_65.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("70")) {
                id_radio_70.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("75")) {
                id_radio_75.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("80")) {
                id_radio_80.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("85")) {
                id_radio_85.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("90")) {
                id_radio_90.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("95")) {
                id_radio_95.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("100")) {
                id_radio_100.setChecked(true);
            } else {
                id_radio_Custom.setChecked(true);
            }
        } else {
            id_radio_select.setChecked(true);
        }


        id_radiogroup_stage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton id_radiogenearal = (RadioButton) dialog.findViewById(group.getCheckedRadioButtonId());

                //Log.e("id_radiogenearal_",": "+id_radiogenearal.getText().toString());
                String str_radiogenearal = id_radiogenearal.getText().toString();
                if (type_is == 1) {
                    // RealizationPercentage
                    if (str_radiogenearal.equalsIgnoreCase("Select")) {
                        textView.setText(str_radiogenearal);
                        editText_realizable_custom.setText("");
                        editText_realizable_custom.setVisibility(View.INVISIBLE);
                        Singleton.getInstance().indPropertyValuation.setRealizationPercentage("");
                    } else if (str_radiogenearal.equalsIgnoreCase("Custom")) {
                        textView.setText(str_radiogenearal);
                        editText_realizable_custom.setText("");
                        editText_realizable_custom.setVisibility(View.VISIBLE);
                        // For reference set as 0 for custom
                        Singleton.getInstance().indPropertyValuation.setRealizationPercentage("0");
                    } else {
                        editText_realizable_custom.setText("");
                        editText_realizable_custom.setVisibility(View.INVISIBLE);
                        String realizableStr = str_radiogenearal.replace("%", "");
                        textView.setText(realizableStr);
                        Singleton.getInstance().indPropertyValuation.setRealizationPercentage(realizableStr);
                    }
                } else if (type_is == 2) {
                    // DistressPercentage
                    if (str_radiogenearal.equalsIgnoreCase("Select")) {
                        textView.setText(str_radiogenearal);
                        editText_distress_custom.setText("");
                        editText_distress_custom.setVisibility(View.INVISIBLE);
                        Singleton.getInstance().indPropertyValuation.setDistressPercentage("");
                    } else if (str_radiogenearal.equalsIgnoreCase("Custom")) {
                        textView.setText(str_radiogenearal);
                        editText_distress_custom.setText("");
                        editText_distress_custom.setVisibility(View.VISIBLE);
                        // For reference set as 0 for custom
                        Singleton.getInstance().indPropertyValuation.setDistressPercentage("0");
                    } else {
                        editText_distress_custom.setText("");
                        editText_distress_custom.setVisibility(View.INVISIBLE);
                        String distressStr = str_radiogenearal.replace("%", "");
                        textView.setText(distressStr);
                        Singleton.getInstance().indPropertyValuation.setDistressPercentage(distressStr);
                    }
                } else if (type_is == 3) {
                    if (str_radiogenearal.equalsIgnoreCase("Select")) {
                        textView.setText(str_radiogenearal);
                        Singleton.getInstance().indPropertyValuation.setCarpetAreaPercentage("");
                        // If select set carpet too select
                        spinner_carpet_type.setText(str_radiogenearal);
                        Singleton.getInstance().indPropertyValuation.setCarpetAreaTypeId("");
                    } else {
                        String carpetStr = str_radiogenearal.replace("%", "");
                        textView.setText(carpetStr);
                        Singleton.getInstance().indPropertyValuation.setCarpetAreaPercentage(carpetStr);
                    }
                }

                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private void DisplayValuesLandBuilding() {

        if (!general.isEmpty(caseid_str))
            Singleton.getInstance().indPropertyValuation.setCaseId(caseid_int);


        /* guideline rate and value and measurment */
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getGuidelineRate())) {
            editText_guideline_rate.setText(Singleton.getInstance().indPropertyValuation.getGuidelineRate());
        } else {
            editText_guideline_rate.setText("");
        }

        // spinner - Measuremnet - GuidelineRateUnit
        ArrayAdapter<Measurements> measurementsArrayAdapter_guild = new ArrayAdapter<Measurements>(getActivity(), R.layout.row_spinner_item, Singleton.getInstance().measurements_list_val_sqya) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(general.mediumtypeface());
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(general.mediumtypeface());
                return v;
            }
        };
        measurementsArrayAdapter_guild.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_ft_meter.setAdapter(measurementsArrayAdapter_guild);
        spinner_ft_meter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("spinner_getMeastId", "::: " + Singleton.getInstance().measurements_list_val_sqya.get(position).getMeasureUnitId());
                Singleton.getInstance().indPropertyValuation.setGuidelineRateUnit("" + Singleton.getInstance().measurements_list_val_sqya.get(position).getMeasureUnitId());
                mea_unit = position;
                permissionarea_dlc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyValuation.getGuidelineRateUnit()))) {
            Log.e("spinner_qualityofcon", "::: " + Singleton.getInstance().indPropertyValuation.getGuidelineRateUnit());
            ArrayList<Measurements> qualityConstructions_ = new ArrayList<>();
            qualityConstructions_ = Singleton.getInstance().measurements_list_val_sqya;
            for (int x = 0; x < qualityConstructions_.size(); x++) {
                int i = Integer.parseInt(Singleton.getInstance().indPropertyValuation.getGuidelineRateUnit());
                if (qualityConstructions_.get(x).getMeasureUnitId() == i) {
                    spinner_ft_meter.setSelection(x);
                    mea_unit = x;
                }
            }
        }

        /* Permission Area */
        if (!general.isEmpty(Singleton.getInstance().indProperty.getDocumentLandArea())) {
            editText_permissiblearea.setText(Singleton.getInstance().indProperty.getDocumentLandArea());
        } else {
            editText_permissiblearea.setText("");
        }

        int docareaUnit = Singleton.getInstance().indProperty.getDocumentLandAreaUnit();
        if (docareaUnit != 0) {
            editText_unit_permissiblearea.setText(Singleton.getInstance().measurements_list.get(docareaUnit).toString());
            editText_unit_actualarea.setText(Singleton.getInstance().measurements_list.get(docareaUnit).toString());
        } else {
            editText_unit_permissiblearea.setText("");
            editText_unit_actualarea.setText("");
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getDocumentLandRate())) {
            editText_rate_permissiblearea.setText(Singleton.getInstance().indPropertyValuation.getDocumentLandRate());
        } else {
            editText_rate_permissiblearea.setText("");
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getDocumentLandValue())) {
            // editText_total_permissiblearea.setText(Singleton.getInstance().indPropertyValuation.getDocumentLandValue());
            editText_total_permissiblearea.setText(general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getDocumentLandValue()));
        } else {
            editText_total_permissiblearea.setText("");
        }

        /* Autual Area */
        if (!general.isEmpty(Singleton.getInstance().indProperty.getMeasuredLandArea())) {
            editText_actualarea.setText(Singleton.getInstance().indProperty.getMeasuredLandArea());
        } else {
            editText_actualarea.setText("");
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getMeasuredLandRate())) {
            editText_rate_actualarea.setText(Singleton.getInstance().indPropertyValuation.getMeasuredLandRate());
        } else {
            editText_rate_actualarea.setText("");
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getMeasuredLandValue())) {
            //  editText_total_actualarea.setText(Singleton.getInstance().indPropertyValuation.getMeasuredLandValue());
            editText_total_actualarea.setText(general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getMeasuredLandValue()));
        } else {
            editText_total_actualarea.setText("");
        }

        editText_rate_permissiblearea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    Log.e("onFocus_coming", "onFocus_coming" + hasFocus);
                    // Copy the value to aspercompletion_val
                    String toString = editText_rate_permissiblearea.getText().toString();
                    CommonRate_permissiblearea_method(toString);
                }
            }
        });

        editText_rate_actualarea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    Log.e("onFocus_coming", "onFocus_coming" + hasFocus);
                    // Copy the value to aspercompletion_val
                    String toString = editText_rate_actualarea.getText().toString();
                    CommonRate_actualarea_method(toString);
                }
            }
        });


        editText_guideline_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                permissionarea_dlc();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editText_guideline_rate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    Log.i("Next key", "Enter pressed");
                    if (permission_check == 1) {
                        editText_guideline_rate.setNextFocusForwardId(R.id.editText_rate_permissiblearea);
                        editText_rate_permissiblearea.setSelection(editText_rate_permissiblearea.getText().toString().length());
                    } else if (permission_check == 2) {
                        editText_guideline_rate.setNextFocusForwardId(R.id.editText_rate_actualarea);
                        editText_rate_actualarea.setSelection(editText_rate_actualarea.getText().toString().length());
                    }
                }
                return false;
            }
        });

        editText_rate_permissiblearea.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    Log.i("Next key", "Enter pressed");
                    editText_rate_permissiblearea.setNextFocusForwardId(R.id.editText_construction_rate);
                    editText_construction_rate.setSelection(editText_construction_rate.getText().toString().length());
                }
                return false;
            }
        });

        editText_rate_actualarea.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    Log.i("Next key", "Enter pressed");
                    editText_rate_actualarea.setNextFocusForwardId(R.id.editText_construction_rate);
                    editText_construction_rate.setSelection(editText_construction_rate.getText().toString().length());
                }
                return false;
            }
        });





        /* Construction value */
        /* Construction rate and value and measurment */
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getConstructionDLCRate())) {
            editText_construction_rate.setText(Singleton.getInstance().indPropertyValuation.getConstructionDLCRate());
        } else {
            editText_construction_rate.setText("");
        }


        // spinner - Measuremnet - ConstructionDLCRateUnit
        ArrayAdapter<Measurements> measurementsArrayAdapter_guild_cons = new ArrayAdapter<Measurements>(getActivity(), R.layout.row_spinner_item, Singleton.getInstance().measurements_list_val_sqya) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(general.mediumtypeface());
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(general.mediumtypeface());
                return v;
            }
        };
        measurementsArrayAdapter_guild_cons.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_ft_meter_construction.setAdapter(measurementsArrayAdapter_guild_cons);
        spinner_ft_meter_construction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("spinner_getMeastId", "::: " + Singleton.getInstance().measurements_list_val_sqya.get(position).getMeasureUnitId());
                Singleton.getInstance().indPropertyValuation.setConstructionDLCRateUnit("" + Singleton.getInstance().measurements_list_val_sqya.get(position).getMeasureUnitId());
                mea_unit_construction = position;
                Construction_dlc();
                /*if (Construction_dlc_mea){
                    Log.e("Construction_dlc", "spinner");
                    Construction_dlc();
                }else {
                    Construction_dlc_mea = true;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyValuation.getConstructionDLCRateUnit()))) {
            Log.e("spinner_qualityofcon", "::: " + Singleton.getInstance().indPropertyValuation.getConstructionDLCRateUnit());
            ArrayList<Measurements> qualityConstructions_ = new ArrayList<>();
            qualityConstructions_ = Singleton.getInstance().measurements_list_val_sqya;
            for (int x = 0; x < qualityConstructions_.size(); x++) {
                int i = Integer.parseInt(Singleton.getInstance().indPropertyValuation.getConstructionDLCRateUnit());
                if (qualityConstructions_.get(x).getMeasureUnitId() == i) {
                    spinner_ft_meter_construction.setSelection(x);
                    mea_unit_construction = x;
                }
            }
        }

        editText_construction_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.e("Construction_dlc", "editext");
                Construction_dlc();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /* ProposedValuation */
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getProposedValuation())) {
            etProposedValuation.setText(Singleton.getInstance().indPropertyValuation.getProposedValuation());
        } else {
            etProposedValuation.setText("");
        }
        /* ProposedValuationComments */
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getProposedValuationComments())) {
            editText_remarks.setText(Singleton.getInstance().indPropertyValuation.getProposedValuationComments());
        } else {
            editText_remarks.setText("");
        }

//        Singleton.getInstance().indPropertyValuation.setIsEstimateJustified(String.valueOf(checkbox_estimate.isChecked()));
//        Singleton.getInstance().indPropertyValuation.setIsAddProposedValuationPercenatge(String.valueOf(checkbox_add_or_not.isChecked()));
//        Singleton.getInstance().indPropertyValuation.setPercentageofEstimate(et_percentage_of_estimation.getText().toString());

        if (Singleton.getInstance().indPropertyValuation.getIsAddProposedValuationPercenatge() != null) {
            cbProposedPercentage.setChecked(Singleton.getInstance().indPropertyValuation.getIsAddProposedValuationPercenatge());
        }


        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getPercentageofEstimate())) {
            etProposedPercentage.setText(Singleton.getInstance().indPropertyValuation.getPercentageofEstimate());
        } else {
            etProposedPercentage.setText("");
        }

    }

    public static void Construction_dlc() {

        int measurment_unit_property = measurment_unit_property_for_dlc;
        String permissiblearea_str = permissiblearea_str_dlc;
        String actualarea_str = actualarea_str_dlc;
        String guideline_rate_str = editText_construction_rate.getText().toString();

        /* PERMISSION AREA start */
        if (permission_check_construction == 1) {
            /* 1 -> sq.ft */
            if (mea_unit_construction == 1) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(permissiblearea_str)) && (mea_unit_construction != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("10.7639"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("9"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("43560"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("107639"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("17452.0069808"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_construction_value.setText("");
                }
                /* 2 -> sq.m */
            } else if (mea_unit_construction == 2) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(permissiblearea_str)) && (mea_unit_construction != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("0.09290304"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("0.836127"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("4046.85642"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("43560"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("1620.4319957"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_construction_value.setText("");
                }
                /* 3 -> sq.yard */
            } else if (mea_unit_construction == 3) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(permissiblearea_str)) && (mea_unit_construction != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("0.111111"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("1.19599"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("4840"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("11959.9"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("1620.4319957"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_construction_value.setText("");
                }
            }
            /* PERMISSION AREA over */
            /* AUTUAL AREA start */
        } else if (permission_check_construction == 2) {
            /* 1 -> sq.ft */
            if (mea_unit_construction == 1) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(actualarea_str)) && (mea_unit_construction != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //  editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("10.7639"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //   editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("9"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //   editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("43560"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //  editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("107639"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("17452.0069808"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_construction_value.setText("");
                }
                /* 2 -> sq.m */
            } else if (mea_unit_construction == 2) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(actualarea_str)) && (mea_unit_construction != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("0.09290304"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("1.1959900463011"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("4046.85642"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("43560"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("1620.4319957"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_construction_value.setText("");
                }
                /* 3 -> sq.yard */
            } else if (mea_unit_construction == 3) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(actualarea_str)) && (mea_unit_construction != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("0.111111"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("1.19599"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("4840"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("11959.9"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("1620.4319957"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_construction_value.setText("" + sum_total);
                        editText_construction_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_construction_value.setText("");
                }
            }
        }
        /* AUTUAL AREA end */
    }

    private void getDetails() {
        general.showloading(getActivity());
        String url = general.ApiBaseUrl() + SettingsUtils.GetPropertyDetails;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setCaseId(caseid_str);
        requestData.setUrl(RequestParam.getPropertyDetails(requestData));
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(), requestData,
                SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                try {
                    showPopup(requestData.isSuccessful(), requestData.getResponse(), requestData.getResponseCode());
                } catch (Exception e) {
                    general.hideloading();
                    e.printStackTrace();
                }
            }
        });
        webserviceTask.execute();


    }


    public void permissionarea_dlc() {
        String permissiblearea_str = editText_permissiblearea.getText().toString();
        String actualarea_str = editText_actualarea.getText().toString();
        int measurment_unit_property = Singleton.getInstance().indProperty.getDocumentLandAreaUnit();
        String guideline_rate_str = editText_guideline_rate.getText().toString();


        /* PERMISSION AREA start */
        if (permission_check == 1) {
            /* 1 -> sq.ft */
            if (mea_unit == 1) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(permissiblearea_str)) && (mea_unit != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));
                        //editText_guideline_value.setText("" + sum_total);
                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("10.7639"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("9"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("43560"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("107639"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("17452.0069808"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_guideline_value.setText("");
                }
                /* 2 -> sq.m */
            } else if (mea_unit == 2) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(permissiblearea_str)) && (mea_unit != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("0.09290304"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("0.836127"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("4046.85642"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("43560"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("1620.4319957"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_guideline_value.setText("");
                }
                /* 3 -> sq.yard */
            } else if (mea_unit == 3) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(permissiblearea_str)) && (mea_unit != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("0.111111"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("1.19599"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("4840"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("11959.9"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(permissiblearea_str)) * (general.convertTofloat("1620.4319957"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_guideline_value.setText("");
                }
            }
            /* PERMISSION AREA over */
            /* AUTUAL AREA start */
        } else if (permission_check == 2) {
            /* 1 -> sq.ft */
            if (mea_unit == 1) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(actualarea_str)) && (mea_unit != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("10.7639"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("9"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("43560"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("107639"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("17452.0069808"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_guideline_value.setText("");
                }
                /* 2 -> sq.m */
            } else if (mea_unit == 2) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(actualarea_str)) && (mea_unit != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("0.09290304"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("1.1959900463011"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("4046.85642"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("43560"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("1620.4319957"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_guideline_value.setText("");
                }

                /* 3 -> sq.yard */
            } else if (mea_unit == 3) {
                /* check the null based on permission str and unit */
                if ((!general.isEmpty(actualarea_str)) && (mea_unit != 0) && (!general.isEmpty(guideline_rate_str))) {
                    if (measurment_unit_property == 1) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("0.111111"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 2) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("1.19599"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 3) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 4) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("4840"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 5) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("11959.9"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    } else if (measurment_unit_property == 6) {
                        float sumtotal = 0;
                        int sum_total = 0;
                        sumtotal = (general.convertTofloat(actualarea_str)) * (general.convertTofloat("1620.4319957"));
                        sumtotal = (sumtotal) * (general.convertTofloat(guideline_rate_str));
                        sum_total = general.convertToRoundoff(sumtotal);
                        //editText_guideline_value.setText("" + sum_total);
                        editText_guideline_value.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    }
                } else {
                    /* all text should be null*/
                    editText_guideline_value.setText("");
                }
            }


        }
        /* AUTUAL AREA end */
    }


    private void showPopup(boolean successful, String response, int responseCode) {
        general.hideloading();
        if (successful && (responseCode == 200 || responseCode == 201)) {

            Log.e("showPopup", "showPopup: " + new Gson().toJson(response));
            comparePopup(new Gson().fromJson(response, GetPropertyDetailsModel.class));
        } else {
            General.customToast(getResources().getString(R.string.something_wrong), getActivity());
        }
    }

    private void comparePopup(GetPropertyDetailsModel fromJson) {
        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.popup_compare_property_rate);
        TextView tvApplicantName = dialog.findViewById(R.id.tvApplicantName);
        TextView tvBankRefNo = dialog.findViewById(R.id.tvBankRefNo);
        TextView tvLatLng = dialog.findViewById(R.id.tvLatLng);
        TextView tvAddress = dialog.findViewById(R.id.tvAddress);
        TextView tvPropertyType = dialog.findViewById(R.id.tvPropertyType);
        TextView tvNoResult = dialog.findViewById(R.id.tvNoResult);
        Spinner spPropertyType = dialog.findViewById(R.id.spPropertyType);
        Spinner spDistance = dialog.findViewById(R.id.spDistance);
        Button yesBtn = dialog.findViewById(R.id.yesBtn);
        Button noBtn = dialog.findViewById(R.id.noBtn);
        RecyclerView rvResult = dialog.findViewById(R.id.rvResult);

        if (fromJson.getData().getApplicantName() != null)
            tvApplicantName.setText(fromJson.getData().getApplicantName());
        if (fromJson.getData().getBankReferenceNO() != null)
            tvBankRefNo.setText(fromJson.getData().getBankReferenceNO());

        ArrayList<String> compareList = new ArrayList<>();
        compareList.add("Select");
        compareList.add("All");
        compareList.add("Same as current");

        ArrayList<String> kmList = new ArrayList<>();
        kmList.add("Select");
        kmList.add("0.5");
        kmList.add("1");
        kmList.add("2");
        kmList.add("3");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.row_spinner_item_, compareList);
        arrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spPropertyType.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter_ = new ArrayAdapter<>(getActivity(),
                R.layout.row_spinner_item_, kmList);
        arrayAdapter_.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spDistance.setAdapter(arrayAdapter_);


        tvLatLng.setText(latvalue.getText().toString() + ", " + longvalue.getText().toString());
        if (fromJson.getData().getPropertyAddress() != null)
            tvAddress.setText(fromJson.getData().getPropertyAddress().toString());

        if (fromJson.getData().getPropertyCategoryName() != null) {
            tvPropertyType.setText(fromJson.getData().getPropertyCategoryName());
        }


        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spPropertyType.getSelectedItemPosition() == 0) {
                    General.customToast("Choose property type!", getActivity());
                    return;
                } else if (spDistance.getSelectedItemPosition() == 0) {
                    General.customToast("Choose distance!", getActivity());
                    return;
                } else if (fromJson.getData().getPropertyCategoryId() == null) {
                    General.customToast("Property Type Id Required!", getActivity());
                    return;
                } else {

                    String url = general.ApiBaseUrl() + SettingsUtils.GetPropertyCompareDetails;
                    JsonRequestData requestData = new JsonRequestData();
                    requestData.setUrl(url);
                    requestData.setCaseId(caseid_str);
                    requestData.setDistance(spDistance.getSelectedItem().toString());
                    requestData.setCurrentPropertyType(String.valueOf(fromJson.getData().getPropertyCategoryId()));
                    requestData.setLatitude(latvalue.getText().toString());
                    requestData.setLongitude(longvalue.getText().toString());
                    requestData.setPropertyType(String.valueOf(spPropertyType.getSelectedItemPosition()));
                    requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
                    requestData.setRequestBody(RequestParam.GetPropertyCompareDetails(requestData));
                    WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(), requestData,
                            SettingsUtils.PUT_TOKEN);
                    webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                        @Override
                        public void onTaskComplete(JsonRequestData requestData) {
                            try {

                                if (requestData.isSuccessful() && (requestData.getResponseCode() == 200 || requestData.getResponseCode() == 201)) {
                                    GetPropertyCompareDetailsModel model = new Gson().fromJson(requestData.getResponse(), GetPropertyCompareDetailsModel.class);

                                    if (model.getData().size() > 0) {

                                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

                                        //Setting the adapter
                                        rvResult.setAdapter(new ComparePropertyRateAdapter(model.getData(), getActivity()));
                                        rvResult.setLayoutManager(llm);

                                        rvResult.setVisibility(View.VISIBLE);
                                        tvNoResult.setVisibility(View.GONE);
                                    } else {
                                        rvResult.setVisibility(View.GONE);
                                        tvNoResult.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    General.customToast(getResources().getString(R.string.something_wrong), getActivity());
                                }

                            } catch (Exception e) {
                                General.customToast(getResources().getString(R.string.something_wrong), getActivity());
                                e.printStackTrace();
                            }
                        }
                    });
                    webserviceTask.execute();
                }
            }
        });


        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void initViews(View view) {

        general = new General(getActivity());

        fragmentActivity = getActivity();

        editText_compound_permissiblearea = (EditText) view.findViewById(R.id.editText_compound_permissiblearea);
        editText_as_per_measurement = (EditText) view.findViewById(R.id.editText_as_per_measurement);

        caseid_str = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");

        editText_distress_custom = (EditText) view.findViewById(R.id.editText_distress_custom);

        editText_construction_value = (EditText) view.findViewById(R.id.editText_construction_value);
        editText_construction_rate = (EditText) view.findViewById(R.id.editText_construction_rate);
        editText_aspercompletion = (EditText) view.findViewById(R.id.editText_aspercompletion);


        textview_insurancevaluepe_result = (TextView) view.findViewById(R.id.textview_insurancevaluepe_result);


        editText_guideline_rate = (EditText) view.findViewById(R.id.editText_guideline_rate);
        editText_guideline_value = (EditText) view.findViewById(R.id.editText_guideline_value);
        editText_rate_permissiblearea = (EditText) view.findViewById(R.id.editText_rate_permissiblearea);
        editText_rate_actualarea = (EditText) view.findViewById(R.id.editText_rate_actualarea);
        etProposedValuation = (EditText) view.findViewById(R.id.etProposedValuation);
        cbProposedPercentage = view.findViewById(R.id.cbProposedPercentage);
        etProposedPercentage = view.findViewById(R.id.etProposedPercentage);

        open_calc_compound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*call_calc_function(1);*/
                Calculation_Popup_New(1);
            }
        });

        open_calc_measurment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call_calc_function(2);
                Calculation_Popup_New(2);
            }
        });

        editText_remarks = (EditText) view.findViewById(R.id.editText_remarks);


        spinner_realizable = (TextView) view.findViewById(R.id.spinner_realizable);
        spinner_distress = (TextView) view.findViewById(R.id.spinner_distress);
        spinner_carpet = (TextView) view.findViewById(R.id.spinner_carpet);
        spinner_carpet_type = (TextView) view.findViewById(R.id.spinner_carpet_type);

        carpet_error = (TextView) view.findViewById(R.id.carpet_error);
        select_error = (TextView) view.findViewById(R.id.select_error);

        editText_realizable_custom = (EditText) view.findViewById(R.id.editText_realizable_custom);

        textview_totalpropertyvalue_result = (TextView) view.findViewById(R.id.textview_totalpropertyvalue_result);


        //  radiogroup_considerforvaluation_land - Radio Button
        id_radiogroup_considerforvaluation_land.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                hideSoftKeyboard(editText_guideline_rate);

                RadioButton id_radiogenearal = (RadioButton) view.findViewById(group.getCheckedRadioButtonId());
                if (id_radiogenearal.getText().toString().equalsIgnoreCase(getResources().getString(R.string.documentarea))) {
                    linear_permissiblearea.setVisibility(View.VISIBLE);
                    linear_actualarea.setVisibility(View.GONE);
                    Singleton.getInstance().indPropertyValuation.setDocumentedLandValueSel(true);
                    Singleton.getInstance().indPropertyValuation.setMeasuredLandValueSel(false);
                    permission_check = 1;
                    permissionarea_dlc();
                    Total_Property_Value();
                } else {
                    linear_permissiblearea.setVisibility(View.GONE);
                    linear_actualarea.setVisibility(View.VISIBLE);
                    Singleton.getInstance().indPropertyValuation.setDocumentedLandValueSel(false);
                    Singleton.getInstance().indPropertyValuation.setMeasuredLandValueSel(true);
                    permission_check = 2;
                    permissionarea_dlc();
                    Total_Property_Value();
                    String toString = editText_rate_actualarea.getText().toString();
                    CommonRate_actualarea_method(toString);

                }
            }
        });

        //  id_radiogroup_considerforvaluation_construction - Radio Button
        id_radiogroup_considerforvaluation_construction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                hideSoftKeyboard(editText_guideline_rate);

                RadioButton id_radiogenearal = (RadioButton) view.findViewById(group.getCheckedRadioButtonId());
                if (id_radiogenearal.getText().toString().equalsIgnoreCase("Sanctioned area")) {
                    textview_permissiblearea_head.setText("Sanctioned area");
                    recyclerview_permissiblearea.setVisibility(View.VISIBLE);
                    recyclerview_actualarea.setVisibility(View.GONE);
                    TotalPermissibleValuationFloors();

                    Singleton.getInstance().indPropertyValuation.setDocumentedConstrValueSel(true);
                    Singleton.getInstance().indPropertyValuation.setMeasuredConstrValueSel(false);
                    permission_check_construction = 1;
                    Log.e("Construction_dlc", "radio1");
                    Construction_dlc();
                    Total_Property_Value();
                } else {
                    textview_permissiblearea_head.setText(getResources().getString(R.string.actualarea));
                    recyclerview_actualarea.setVisibility(View.VISIBLE);
                    recyclerview_permissiblearea.setVisibility(View.GONE);
                    TotalActualValuationFloors();
                    Singleton.getInstance().indPropertyValuation.setDocumentedConstrValueSel(false);
                    Singleton.getInstance().indPropertyValuation.setMeasuredConstrValueSel(true);
                    permission_check_construction = 2;
                    Log.e("Construction_dlc", "radio2");
                    Construction_dlc();
                    Total_Property_Value();
                }
            }
        });


        ratePopupApi = new RatePopupApi(getActivity(), this);
    }

    private void hideSoftKeyboard(View addkeys) {
        if ((addkeys != null) && (getActivity() != null)) {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(addkeys.getWindowToken(), 0);
        }
        show_emptyFocus();
    }

    public void show_emptyFocus() {
        // Show focus
        if (OtherDetails.my_focuslayout != null) {
            OtherDetails.my_focuslayout.requestFocus();
        }
    }


    public void Calculation_Popup_New(final int type_is) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calculator_popup_new);


        isSmallBracketOpen = false;
        developedCounter = 0;


        /***** Assigning Variable *****/
        btnClear = (TextView) dialog.findViewById(R.id.btn_delete);
        tvProcessor = (TextView) dialog.findViewById(R.id.tv_process);
        tvResult = (TextView) dialog.findViewById(R.id.tv_result);

        tvProcessor.setText("");
        tvResult.setText("");


        txtOne = (TextView) dialog.findViewById(R.id.btn_one);
        txtTwo = (TextView) dialog.findViewById(R.id.btn_two);
        txtThree = (TextView) dialog.findViewById(R.id.btn_three);
        btnFour = (TextView) dialog.findViewById(R.id.btn_four);
        btnFive = (TextView) dialog.findViewById(R.id.btn_five);
        btnSix = (TextView) dialog.findViewById(R.id.btn_six);
        btnSeven = (TextView) dialog.findViewById(R.id.btn_seven);
        btnEight = (TextView) dialog.findViewById(R.id.btn_eight);
        btnNine = (TextView) dialog.findViewById(R.id.btn_nine);
        txtZero = (TextView) dialog.findViewById(R.id.btn_zero);


        btnMultiply = (TextView) dialog.findViewById(R.id.btn_multiply);
        btnMinus = (TextView) dialog.findViewById(R.id.btn_minus);
        txtPlus = (TextView) dialog.findViewById(R.id.btn_plus);
        btnDivide = (TextView) dialog.findViewById(R.id.btn_divide);

        btnDecimal = (TextView) dialog.findViewById(R.id.btn_dot);
        btnBack = (TextView) dialog.findViewById(R.id.btn_back);

        btnSmallBracket = (TextView) dialog.findViewById(R.id.btn_small_bracket);
        btnEqual = (TextView) dialog.findViewById(R.id.btn_equal);
        btnPercentage = (TextView) dialog.findViewById(R.id.btn_percentage);
        btnclose = (TextView) dialog.findViewById(R.id.btnclose);
        btnsetvalue = (TextView) dialog.findViewById(R.id.btnsetvalue);
        TextView calculatortitle = (TextView) dialog.findViewById(R.id.calculatortitle);

        calculatortitle.setTypeface(general.regulartypeface());
        btnClear.setTypeface(general.regulartypeface());
        tvProcessor.setTypeface(general.regulartypeface());
        tvResult.setTypeface(general.regulartypeface());
        txtOne.setTypeface(general.RobotoLightTypeface());
        txtTwo.setTypeface(general.RobotoLightTypeface());
        txtThree.setTypeface(general.RobotoLightTypeface());
        btnFour.setTypeface(general.RobotoLightTypeface());
        btnFive.setTypeface(general.RobotoLightTypeface());
        btnSix.setTypeface(general.RobotoLightTypeface());
        btnSeven.setTypeface(general.RobotoLightTypeface());
        btnEight.setTypeface(general.RobotoLightTypeface());
        btnNine.setTypeface(general.RobotoLightTypeface());
        txtZero.setTypeface(general.RobotoLightTypeface());
        btnMultiply.setTypeface(general.regulartypeface());
        btnMinus.setTypeface(general.regulartypeface());
        txtPlus.setTypeface(general.regulartypeface());
        btnDivide.setTypeface(general.RobotoLightTypeface());
        btnDecimal.setTypeface(general.regulartypeface());
        btnBack.setTypeface(general.regulartypeface());
        btnSmallBracket.setTypeface(general.regulartypeface());
        btnEqual.setTypeface(general.regulartypeface());
        btnPercentage.setTypeface(general.regulartypeface());
        btnclose.setTypeface(general.regulartypeface());
        btnsetvalue.setTypeface(general.regulartypeface());

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                developedCounter++;
                if (developedCounter >= 5) {
                    developedCounter = 0;
                    tvProcessor.setText(developedBy);
                    tvResult.setText(deveopedNote);
                } else {
                    tvProcessor.setText("");
                    tvResult.setText("");
                }
            }
        });


        /******************************************************
         * Number Buttons on-Click
         ******************************************************/
        txtOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "1");
            }
        });
        txtTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "2");
            }
        });
        txtThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "3");
            }
        });
        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "4");
            }
        });
        btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "5");
            }
        });
        btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "6");
            }
        });
        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "7");
            }
        });
        btnEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "8");
            }
        });
        btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "9");
            }
        });
        txtZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "0");
            }
        });


        /******************************************************
         * Functional Buttons on-Click
         ******************************************************/
        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "*");
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "-");
            }
        });
        txtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "+");
            }
        });
        btnDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "/");
            }
        });
        btnDecimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + ".");
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                if (processor.length() > 0) {
                    processor = processor.substring(0, processor.length() - 1);
                    tvProcessor.setText(processor);
                } else {
                    tvResult.setText("");
                }
            }
        });
        btnSmallBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                if (isSmallBracketOpen) {
                    processor = tvProcessor.getText().toString();
                    tvProcessor.setText(processor + ")");
                    isSmallBracketOpen = false;
                } else {
                    processor = tvProcessor.getText().toString();
                    tvProcessor.setText(processor + "(");
                    isSmallBracketOpen = true;
                }
            }
        });

        btnPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "%");
            }
        });


        /******************************************************
         * Equal Buttons on-Click
         ******************************************************/
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();

                //processor = processor.replaceAll("X", "*");
                processor = processor.replaceAll("%", "/100");

                Context rhino = Context.enter();
                rhino.setOptimizationLevel(-1);
                String result = "";

                try {
                    Scriptable scope = rhino.initStandardObjects();
                    result = rhino.evaluateString(scope, processor, "JavaScript", 1, null).toString();
                } catch (Exception e) {
                    result = "Error";
                }

                /*************/
                if (result.contains("javascript") || result.contains("Error")) {
                    tvResult.setText("");
                } else if (result.contains(".")) {
                    Float resfloat = Float.valueOf(result);
                    result_value = Double.parseDouble(new DecimalFormat("##.##").format(resfloat));
                    Log.e("double", result_value + "");
                    /***********/

                    tvResult.setText("" + result_value);
                } else {
                    tvResult.setText("" + result);
                }
            }
        });

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnsetvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!general.isEmpty(tvResult.getText().toString())) {
                    // - values not allowed
                    if (!tvResult.getText().toString().contains("-")) {
                        /*int my_val = general.convertToRoundoff(Float.parseFloat(tvResult.getText().toString()));
                        String my_val_str = String.valueOf(my_val);*/
                        String my_val_str = tvResult.getText().toString();
                        if (type_is == 1) {
                            editText_compound_permissiblearea.setText(my_val_str);
                            String charSequence = editText_compound_permissiblearea.getText().toString();
                            if (general.isEmpty(editText_as_per_measurement.getText().toString())) {
                                // If its empty
                                editText_as_per_measurement.setText(charSequence);
                                actual_measurment(charSequence);
                                //TODO Interface
                               /* FragmentValuationBuilding fragmentValuationBuilding = new FragmentValuationBuilding();
                                fragmentValuationBuilding.actual_measurment(charSequence);*/
                            }
                            editText_compound_permissiblearea.setError(null);
                            editText_as_per_measurement.setError(null);
                            permission_measurment(charSequence);
                            //TODO Interface
                            /*FragmentValuationBuilding fragmentValuationBuilding = new FragmentValuationBuilding();
                            fragmentValuationBuilding.permission_measurment(charSequence);*/


                        } else {
                            editText_as_per_measurement.setText(my_val_str);
                            actual_measurment(my_val_str);

                            //TODO Interface
                          /*  FragmentValuationBuilding fragmentValuationBuilding = new FragmentValuationBuilding();
                            fragmentValuationBuilding.actual_measurment(my_val_str);*/


                        }
                    }
                    dialog.dismiss();
                } else if (general.isEmpty(tvResult.getText().toString())) {
                    general.CustomToast("Press equals first and then set the value");
                }

            }
        });

        dialog.show();
    }

    public void clearScreen() {
        processor = tvProcessor.getText().toString();
        if (processor.contains(developedBy)) {
            tvProcessor.setText("");
            tvResult.setText("");
        }
        developedCounter = 0;
    }

    public void CommonRate_permissiblearea_method(String str) {

        String rate_str = str;
        editText_rate_actualarea.setText(rate_str);
        String permissiblearea_str = editText_permissiblearea.getText().toString();

        if (!general.isEmpty(rate_str) && !general.isEmpty(permissiblearea_str)) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(rate_str)) * (general.convertTofloat(permissiblearea_str));
            sum_total = general.convertToRoundoff(sumtotal);
            editText_total_permissiblearea.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));
        } else {
            editText_total_permissiblearea.setText("");
        }

        String actualarea_str = editText_actualarea.getText().toString();
        if (!general.isEmpty(rate_str) && !general.isEmpty(actualarea_str)) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(rate_str)) * (general.convertTofloat(actualarea_str));
            sum_total = general.convertToRoundoff(sumtotal);
            editText_total_actualarea.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));
        } else {
            editText_total_actualarea.setText("");
        }

        Total_Property_Value();
    }

    public void Total_Property_Value() {
        /******total property result value********/

        String land_total_str = "0";
        String construction_total_str = "0";
        construction_total_str = textview_totalconstructionvalue_result.getText().toString();

        if (permission_check == 1) {
            land_total_str = editText_total_permissiblearea.getText().toString();
        } else if (permission_check == 2) {
            land_total_str = editText_total_actualarea.getText().toString();
        }

        if ((!general.isEmpty(land_total_str)) && (!general.isEmpty(construction_total_str))) {
            float property = (general.convertTofloat(land_total_str)) + (general.convertTofloat(construction_total_str));
            int total_property = general.convertToRoundoff(property);
            //textview_totalpropertyvalue_result.setText("" + total_property);
            textview_totalpropertyvalue_result.setText(general.DecimalFormattedCommaString(String.valueOf(total_property)));

        } else if (!general.isEmpty(land_total_str)) {
            float property = (general.convertTofloat(land_total_str));
            int total_property = general.convertToRoundoff(property);
            //textview_totalpropertyvalue_result.setText("" + total_property);
            textview_totalpropertyvalue_result.setText(general.DecimalFormattedCommaString(String.valueOf(total_property)));

        } else if (!general.isEmpty(construction_total_str)) {
            float property = (general.convertTofloat(construction_total_str));
            int total_property = general.convertToRoundoff(property);
            //textview_totalpropertyvalue_result.setText("" + total_property);
            textview_totalpropertyvalue_result.setText(general.DecimalFormattedCommaString(String.valueOf(total_property)));

        } else {
            textview_totalpropertyvalue_result.setText("");
        }


        /****As per Completion result value******/
        float percentageCompletetotal = 0;
        int percentageComplete_total = 0;
        String asperCompletepercentage = editText_aspercompletion.getText().toString();
        if (!General.isEmpty(asperCompletepercentage)) {
            String total_property = textview_totalpropertyvalue_result.getText().toString();
            if (!general.isEmpty(total_property)) {
                percentageCompletetotal = (general.convertTofloat(total_property) * ((general.convertTofloat(asperCompletepercentage)) / 100));
                percentageComplete_total = general.convertToRoundoff(percentageCompletetotal);
                // textview_aspercompletion_result.setText("" + percentageComplete_total);
                textview_aspercompletion_result.setText(general.DecimalFormattedCommaString(String.valueOf(percentageComplete_total)));

            } else {
                textview_aspercompletion_result.setText("");
            }
        }

    }


    public void CommonRate_actualarea_method(String str) {
Log.d("Hari","HAri");
        String rate_str = str;
        editText_rate_permissiblearea.setText(rate_str);
        String permissiblearea_str = editText_actualarea.getText().toString();

        if (!general.isEmpty(rate_str) && !general.isEmpty(permissiblearea_str)) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(rate_str)) * (general.convertTofloat(permissiblearea_str));
            sum_total = general.convertToRoundoff(sumtotal);
            editText_total_actualarea.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));
        } else {
            editText_total_actualarea.setText("");
        }

        String actualarea_str = editText_permissiblearea.getText().toString();
        if (!general.isEmpty(rate_str) && !general.isEmpty(actualarea_str)) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(rate_str)) * (general.convertTofloat(actualarea_str));
            sum_total = general.convertToRoundoff(sumtotal);
            editText_total_permissiblearea.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));
        } else {
            editText_total_permissiblearea.setText("");
        }

        Total_Property_Value();
    }


    /*Land Measurement*/
    public void permission_measurment(String str) {
        if (editText_permissiblearea != null) {
            if (!general.isEmpty(str)) {
                editText_permissiblearea.setText(str);
                permission_check = 1;
                permissionarea_dlc();
                /* multiple the above compond into  */
                String permissiblearea_str = editText_permissiblearea.getText().toString();
                if (!general.isEmpty(editText_rate_permissiblearea.getText().toString()) && !general.isEmpty(permissiblearea_str)) {
                    float sumtotal = 0;
                    int sum_total = 0;
                    sumtotal = (general.convertTofloat(editText_rate_permissiblearea.getText().toString())) * (general.convertTofloat(permissiblearea_str));
//                    sum_total = (int) sumtotal;
                    sum_total = general.convertToRoundoff(sumtotal);
                    //editText_total_permissiblearea.setText("" + sum_total);
                    editText_total_permissiblearea.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    Total_Property_Value();
                } else {
                    editText_total_permissiblearea.setText("");
                }
            } else {
                editText_permissiblearea.setText("");
                editText_total_permissiblearea.setText("");
            }
        }
    }

    public void actual_measurment(String str) {
        if (editText_actualarea != null) {
            if (!general.isEmpty(str)) {
                editText_actualarea.setText(str);
                permission_check = 2;
                permissionarea_dlc();
                /* multiple the above compond into  */
                String actualarea_str = editText_actualarea.getText().toString();
                if (!general.isEmpty(editText_rate_permissiblearea.getText().toString()) && !general.isEmpty(actualarea_str)) {
                    float sumtotal = 0;
                    int sum_total = 0;
                    sumtotal = (general.convertTofloat(editText_rate_permissiblearea.getText().toString())) * (general.convertTofloat(actualarea_str));
//                    sum_total = (int) sumtotal;
                    sum_total = general.convertToRoundoff(sumtotal);
                    //editText_total_actualarea.setText("" + sum_total);
                    editText_total_actualarea.setText(general.DecimalFormattedCommaString(String.valueOf(sum_total)));

                    Total_Property_Value();
                } else {
                    editText_total_actualarea.setText("");
                }
            } else {
                editText_actualarea.setText("");
                editText_total_actualarea.setText("");
            }
        }

    }


    private void initInternalCompositionFloorViews() {

        floornolist = new ArrayList<>();
        if (Singleton.getInstance().indPropertyFloors != null)
            if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                initEnterFloorInternalViews(String.valueOf(Singleton.getInstance().indPropertyFloors.size()));
            } else {
                if (floornolist == null || floornolist.size() == 0) {
                    floornolist = new ArrayList<>();
                }
                floorsInternalRecyclerview();
            }
    }

    private void floorsInternalRecyclerview() {
        internalfloorlistAdapter = new InternalFloorNewAdapter(floornolist, getActivity());
        lfm = new LinearLayoutManager(getActivity());
        //Setting the adapter
        recyclerview_internal.setAdapter(internalfloorlistAdapter);
        recyclerview_internal.setLayoutManager(lfm);
    }


    /* construction DLC from floors */
    public static void constrction_measurment(String str) {
        if (!general.isEmpty(str)) {
            permission_check_construction = 1;
            permissiblearea_str_dlc = str;
            Log.e("Construction_dlc", "measurment");
            Construction_dlc();
        }
    }

    public static void constrction_actual(String str) {
        if (!general.isEmpty(str)) {
            permission_check_construction = 2;
            actualarea_str_dlc = str;
            Log.e("Construction_dlc", "actual");
            Construction_dlc();
        }
    }


    private int getCompletedSumValue(ArrayList<IndPropertyFloor> floor_list) {
        int sumtotal = 0;

        if (floor_list.size() > 0) {
            for (int i = 0; i < floor_list.size(); i++) {
                String numOnly = general.NumOnly(String.valueOf(floor_list.get(i).getPercentageCompletion()));
                if (!numOnly.equalsIgnoreCase("")) {
                    int area = floor_list.get(i).getPercentageCompletion();
                    sumtotal = sumtotal + area;
                }
            }
            sumtotal = sumtotal / floor_list.size();
        }
        return sumtotal;
    }

    public void get_constrction_land(int id) {
        if (id != 0) {
            measurment_unit_property_for_dlc = id;
            Log.e("Construction_dlc", "land");
            // Method 2 - constant sq.ft fixed
            // Construction_dlc();
        }
    }

    public void save_landval() {

        if (!general.isEmpty(editText_compound_permissiblearea.getText().toString().trim())) {
            Singleton.getInstance().indProperty.setDocumentLandArea(editText_compound_permissiblearea.getText().toString().trim());
        }

        if (!general.isEmpty(editText_as_per_measurement.getText().toString().trim())) {
            Singleton.getInstance().indProperty.setMeasuredLandArea(editText_as_per_measurement.getText().toString().trim());
        }


        if (!general.isEmpty(editText_approved_floors.getText().toString().trim())) {
            Singleton.getInstance().indProperty.setApprovedNoOfFloors(Integer.valueOf(editText_approved_floors.getText().toString()));
        }


        if (!general.isEmpty(editText_Floor_details.getText().toString().trim())) {
            Singleton.getInstance().indProperty.setFloorDetails(editText_Floor_details.getText().toString());
        }


        Singleton.getInstance().indPropertyValuation.setGuidelineRate(editText_guideline_rate.getText().toString());
        Singleton.getInstance().indPropertyValuation.setGuidelineValue(general.ReplaceCommaSaveToString(editText_guideline_value.getText().toString()));
        Singleton.getInstance().indPropertyValuation.setDocumentLandRate(editText_rate_permissiblearea.getText().toString());
        Singleton.getInstance().indPropertyValuation.setDocumentLandValue(general.ReplaceCommaSaveToString(editText_total_permissiblearea.getText().toString()));
        Singleton.getInstance().indPropertyValuation.setMeasuredLandRate(editText_rate_actualarea.getText().toString());
        Singleton.getInstance().indPropertyValuation.setMeasuredLandValue(general.ReplaceCommaSaveToString(editText_total_actualarea.getText().toString()));
        /* construction rate */
        Singleton.getInstance().indPropertyValuation.setConstructionDLCRate(editText_construction_rate.getText().toString());
        Singleton.getInstance().indPropertyValuation.setConstructionDLCValue(general.ReplaceCommaSaveToString(editText_construction_value.getText().toString()));
        Singleton.getInstance().indPropertyValuation.setProposedValuation(etProposedValuation.getText().toString());
        Singleton.getInstance().indPropertyValuation.setProposedValuationComments(editText_remarks.getText().toString());
        Singleton.getInstance().indPropertyValuation.setIsEstimateJustified(false);
        Singleton.getInstance().indPropertyValuation.setIsAddProposedValuationPercenatge(cbProposedPercentage.isChecked());
        Singleton.getInstance().indPropertyValuation.setPercentageofEstimate(etProposedPercentage.getText().toString());
    }


    public void TotalPermissibleValuationFloors() {

        /****Total Construction result value******/
        getValFloorlist = new ArrayList<>();
        if (valuationPermissibleAreaAdapter != null) {
            getValFloorlist = valuationPermissibleAreaAdapter.getFloorvaluationStepList();
        }
        int total_construction = 0;
        if (getValFloorlist.size() > 0) {
            total_construction = general.getTotalConstructionValue(getValFloorlist);
            textview_totalconstructionvalue_result.setText(general.DecimalFormattedCommaString(String.valueOf(total_construction)));
            textview_insurancevaluepe_result.setText(general.DecimalFormattedCommaString(String.valueOf(total_construction)));

        }

        Total_Property_Value();
    }

    public void TotalActualValuationFloors() {


        /****Total Construction result value******/
        getValActualFloorlist = new ArrayList<>();
        if (listActualAdapter != null) {
            getValActualFloorlist = listActualAdapter.getFloorvaluationStepList();
        }
        int total_construction = 0;
        if (getValActualFloorlist.size() > 0) {
            total_construction = general.getTotalConstructionActualValue(getValActualFloorlist);
            /*textview_totalconstructionvalue_result.setText("" + total_construction);
            textview_insurancevaluepe_result.setText("" + total_construction);*/
            textview_totalconstructionvalue_result.setText(general.DecimalFormattedCommaString(String.valueOf(total_construction)));
            textview_insurancevaluepe_result.setText(general.DecimalFormattedCommaString(String.valueOf(total_construction)));

        }

        Total_Property_Value();
    }

    @Override
    public void onRatePopupSucess(RatePopup ratePopup) {

        try {
            if (ratePopup != null && ratePopup.getData() != null) {
                if (ratePopup.getData().getPropertyCategoryId() == 2 || ratePopup.getData().getPropertyCategoryId() == 3) {
                    if (ratePopup.getData().getMinAmount() != 0.0 && ratePopup.getData().getMaxAmount() != 0.0) {

                        tvRatePopup.setText("INR " + ratePopup.getData().getMinAmount() + " to " + ratePopup.getData().getMaxAmount() + " perSqYds");

                    } else {

                        tvRatePopup.setText("No land rate suggestion to display!");
                    }
                } else {
                    if (ratePopup.getData().getMinAmount() != 0.0 && ratePopup.getData().getMaxAmount() != 0.0) {
                        tvRatePopup.setText("INR " + ratePopup.getData().getMinAmount() + " to " + ratePopup.getData().getMaxAmount() + " perSq.ft");
                    } else {
                        tvRatePopup.setText("No flat rate suggestion to display!");
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }


    }

    @Override
    public void onRatePopupFailed(String msg) {
        Toast.makeText(getActivity(), "Unable to fetch market rate value!", Toast.LENGTH_SHORT).show();
    }

    public static void saveIndValuationFloorsCalculation() {

        /*****
         * set Floors valuation Calculation dynamic result
         * *****/
        setFloorsValuationCalcListData();

        /*****
         * set the total construction and property values
         * ******/
        String total_construct = textview_totalconstructionvalue_result.getText().toString();
        String total_property = textview_totalpropertyvalue_result.getText().toString();
        String aspercomp_resultval = textview_aspercompletion_result.getText().toString();
        String aspercomp_percentage = editText_aspercompletion.getText().toString();
        Singleton.getInstance().indPropertyValuation.setTotalConstructionValue(general.ReplaceCommaSaveToString(total_construct));
        Singleton.getInstance().indPropertyValuation.setInsuranceValue(general.ReplaceCommaSaveToString(total_construct));
        Singleton.getInstance().indPropertyValuation.setTotalPropertyValue(general.ReplaceCommaSaveToString(total_property));
        Singleton.getInstance().indPropertyValuation.setCompletionPercentage(general.ReplaceCommaSaveToString(aspercomp_percentage));
        Singleton.getInstance().indPropertyValuation.setTotalValueAtCompletion(general.ReplaceCommaSaveToString(aspercomp_resultval));


       /* getSubSequentRevaluation();
        storeProposedRenovateValues();*/
    }

    public static void setFloorsValuationCalcListData() {


        JSONArray jsonArray = new JSONArray();
        ArrayList<IndPropertyFloorsValuation> indPropertyFloorsValuationArrayList = new ArrayList<>();
        getValFloorlistSave = valuationPermissibleAreaAdapter.getFloorvaluationStepList();

        for (int j = 0; j < getValFloorlistSave.size(); j++) {
            if (Singleton.getInstance().indPropertyFloorsValuations.size() > 0) {

                IndPropertyFloorsValuation indPropertyFloorsValuation = new IndPropertyFloorsValuation();

                indPropertyFloorsValuation.setCaseId(Singleton.getInstance().aCase.getCaseId()); //Integer.value(caseid);
                indPropertyFloorsValuation.setDocumentConstrRate(getValFloorlistSave.get(j).getDocumentConstrRate());
                indPropertyFloorsValuation.setDocumentConstrValue(general.ReplaceCommaSaveToString(getValFloorlistSave.get(j).getDocumentConstrValue()));
                indPropertyFloorsValuation.setMeasuredConstrRate(getValFloorlistSave.get(j).getMeasuredConstrRate());
                indPropertyFloorsValuation.setMeasuredConstrValue(general.ReplaceCommaSaveToString(getValFloorlistSave.get(j).getMeasuredConstrValue()));
                indPropertyFloorsValuation.setFloorDepreciationValue(general.ReplaceCommaSaveToString(getValFloorlistSave.get(j).getFloorDepreciationValue()));
                indPropertyFloorsValuation.setFloorNo(j + 1);
                indPropertyFloorsValuationArrayList.add(indPropertyFloorsValuation);


            } else {
                // Add New Ind floors valuation
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("CaseId    ", caseid_str);
                    jsonObject.put("docrate", getValFloorlistSave.get(j).getDocumentConstrRate());
                    jsonObject.put("docval", getValFloorlistSave.get(j).getDocumentConstrValue());
                    jsonArray.put(jsonObject);

                    IndPropertyFloorsValuation indPropertyFloorsValuation = new IndPropertyFloorsValuation();
                    indPropertyFloorsValuation.setCaseId(Integer.valueOf(caseid_str)); //Integer.value(caseid);
                    indPropertyFloorsValuation.setDocumentConstrRate(getValFloorlistSave.get(j).getDocumentConstrRate());
                    indPropertyFloorsValuation.setDocumentConstrValue(general.ReplaceCommaSaveToString(getValFloorlistSave.get(j).getDocumentConstrValue()));
                    indPropertyFloorsValuation.setMeasuredConstrRate(getValFloorlistSave.get(j).getMeasuredConstrRate());
                    indPropertyFloorsValuation.setMeasuredConstrValue(general.ReplaceCommaSaveToString(getValFloorlistSave.get(j).getMeasuredConstrValue()));
                    indPropertyFloorsValuation.setFloorDepreciationValue(general.ReplaceCommaSaveToString(getValFloorlistSave.get(j).getFloorDepreciationValue()));
                    indPropertyFloorsValuation.setFloorNo(j + 1);
                    indPropertyFloorsValuationArrayList.add(indPropertyFloorsValuation);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Singleton.getInstance().indPropertyFloorsValuations = indPropertyFloorsValuationArrayList;
        }
    }

    public void get_measurment_land(int id, String name) {
        if (editText_unit_permissiblearea != null) {
            if (id != 0) {
                editText_unit_permissiblearea.setText(name);
                editText_unit_actualarea.setText(name);
                Singleton.getInstance().indProperty.setDocumentLandAreaUnit(id);
                Singleton.getInstance().indProperty.setMeasuredLandAreaUnit(id);
                permissionarea_dlc();
            } else {
                editText_unit_permissiblearea.setText("");
                editText_unit_actualarea.setText("");
            }
        }
    }

    private void set_mandatory_building() {



        // compound_permissiblearea
        String comp_permissiblearea = editText_compound_permissiblearea.getText().toString();
        if (!general.isEmpty(comp_permissiblearea)) {
            editText_compound_permissiblearea.setError(null);
        } else {
            editText_compound_permissiblearea.requestFocus();
            editText_compound_permissiblearea.setError(getResources().getString(R.string.err_comp_permis));
        }
        // as_per_measurement
        String aspermeasurement = editText_as_per_measurement.getText().toString();
        if (!general.isEmpty(aspermeasurement)) {
            editText_as_per_measurement.setError(null);
        } else {
            editText_as_per_measurement.requestFocus();
            editText_as_per_measurement.setError(getResources().getString(R.string.err_aspermeasu));
        }

        // Nooffloors
        String floor_no = editText_floors_measu.getText().toString();
        if (!general.isEmpty(floor_no)) {
            editText_floors_measu.setError(null);

            int measurementland = spinner_measurement1.getSelectedItemPosition();
            //int measurementconstruction = spinner_measurement2.getSelectedItemPosition();
            if (measurementland == 0) {
                // measurementland
                editText_floors_measu.requestFocus();

                textview_error_measure_land.setVisibility(View.VISIBLE);
                //((TextView) spinner_measurement1.getSelectedView()).setError(getResources().getString(R.string.err_measure_land));
                //general.CustomToast(getResources().getString(R.string.err_measure_land));
            }

            // Check all values
            ArrayList<IndPropertyFloor> floor_list = new ArrayList<>();
            floor_list = FragmentBuilding.listAdapter.getStepList();
            if (floor_list.size() == 0) {
                // Kindly Generate your Floors
                editText_floors_measu.requestFocus();
                //general.CustomToast(getResources().getString(R.string.err_msg_genera_floors));
            } else {
                // Check with Floor name
                for (int j = 0; j < floor_list.size(); j++) {
                    String floorName = floor_list.get(j).getFloorName();
                    if (general.isEmpty(floorName)) {
                        // If floor name is empty
                        editText_floors_measu.requestFocus();
                        textview_floor_name.setError(getResources().getString(R.string.err_msg_enter_floor_name));
                        //general.CustomToast(getResources().getString(R.string.err_msg_enter_floor_name));
                    }

                    int constructionStageId = floor_list.get(j).getConstructionStageId();
                    if (constructionStageId == 0) {
                        // If constructionStageId is empty
                        editText_floors_measu.requestFocus();
                        textview_stage.setError(getResources().getString(R.string.err_msg_stagetype));
                    }

                    String percentageCompletion = String.valueOf(floor_list.get(j).getPercentageCompletion());
                    if (general.isEmpty(percentageCompletion)) {
                        // If PercentageCompletion is empty
                        editText_floors_measu.requestFocus();
                        textview_comp.setError(getResources().getString(R.string.err_msg_percentage_completion));
                    }
                }

            }
        } else {
            editText_floors_measu.requestFocus();
            editText_floors_measu.setError(getResources().getString(R.string.err_no_floor));
        }
    }
}