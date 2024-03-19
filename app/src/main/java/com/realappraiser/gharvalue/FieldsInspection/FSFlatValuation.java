package com.realappraiser.gharvalue.FieldsInspection;

import static com.realappraiser.gharvalue.fragments.PhotoLatLong.latvalue;
import static com.realappraiser.gharvalue.fragments.PhotoLatLong.longvalue;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.adapter.ComparePropertyRateAdapter;
import com.realappraiser.gharvalue.adapter.PropertyActualUsageAdapter;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RatePopupApi;
import com.realappraiser.gharvalue.communicator.RatePopupupInterface;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.fragments.FragmentFlat;
import com.realappraiser.gharvalue.fragments.OtherDetails;
import com.realappraiser.gharvalue.fragments.PhotoLatLong;
import com.realappraiser.gharvalue.model.Construction;
import com.realappraiser.gharvalue.model.GetPropertyCompareDetailsModel;
import com.realappraiser.gharvalue.model.GetPropertyDetailsModel;
import com.realappraiser.gharvalue.model.IndPropertyFloor;
import com.realappraiser.gharvalue.model.InternalFloorModel;
import com.realappraiser.gharvalue.model.Measurements;
import com.realappraiser.gharvalue.model.PropertyActualUsage;
import com.realappraiser.gharvalue.model.RatePopup;
import com.realappraiser.gharvalue.utils.DecimalDigitsInputFilter;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.utils.Singleton;

import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FSFlatValuation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FSFlatValuation extends Fragment implements View.OnTouchListener, RatePopupupInterface {


    public static General general;
    // @BindView(R.id.editText_totalapartmentfloors)
    public static EditText editText_totalapartmentfloors;
    public static EditText editText_totalflatsperfloor;
    public static EditText editText_flatsituatedinfloor;
    //public static EditText editText_floors;
    public static EditText editText_as_per_docs;

    public static EditText edittext_general_floor_name;
    public static EditText edittext_general_comp;
    public static EditText edittext_general_progress;
    public static EditText edittext_general_age;
    public static EditText edittext_general_life;

    public static EditText edittext_general_saleablearea;
    public static EditText et_permssible_area;
    public static EditText edittext_general_builduparea;
    public static EditText edittext_general_carpetarea;
    public static TextView textview_comp_total;
    public static TextView textview_internal_floor_name_composition;

    public static Spinner spinnerMeasurement_doc;
    public static Spinner spinner_measurement_act;
    public static Spinner stageSpinner;

    @BindView(R.id.textview_actualusage_flat)
    TextView textview_actualusage_flat;
    @BindView(R.id.textview_land_measurement)
    TextView textview_land_measurement;
    @BindView(R.id.textview_measurement_doc)
    TextView textview_measurement_doc;
    @BindView(R.id.textview_measurement_act)
    TextView textview_measurement_act;

    @BindView(R.id.textview_generl_info_measu)
    TextView textview_generl_info_measu;
    @BindView(R.id.textview_floor_name)
    TextView textview_floor_name;
    @BindView(R.id.textview_stage)
    TextView textview_stage;
    @BindView(R.id.textview_comp)
    TextView textview_comp;
    @BindView(R.id.textview_progress)
    TextView textview_progress;
    @BindView(R.id.textview_carpetarea)
    TextView textview_carpetarea;
    @BindView(R.id.textview_builduparea)
    TextView textview_builduparea;
    @BindView(R.id.textview_saleablearea)
    TextView textview_saleablearea;
    @BindView(R.id.textview_age)
    TextView textview_age;
    @BindView(R.id.textview_life)
    TextView textview_life;

    @BindView(R.id.textviewlabel_carpetloading_per)
    TextView textviewlabel_carpetloading_per;
    @BindView(R.id.textviewlabel_carpet_area_per)
    TextView textviewlabel_carpet_area_per;
    @BindView(R.id.textviewlabel_carpetloading_type)
    TextView textviewlabel_carpetloading_type;

    @BindView(R.id.textview_average)
    TextView textview_average;
    /*  @BindView(R.id.textview_comp_total)
      TextView textview_comp_total;*/
    @BindView(R.id.textview_total)
    TextView textview_total;
    @BindView(R.id.textview_doc_total)
    TextView textview_doc_total;
    @BindView(R.id.textview_actual_total)
    TextView textview_actual_total;

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

    // Todo spinner init
    @BindView(R.id.textview_actual_usage_flat)
    TextView textview_actual_usage_flat;


    /*@BindView(R.id.internal_hall_dinning)
    Spinner internal_hall_dinning;
    @BindView(R.id.internal_kitchen)
    Spinner internal_kitchen;
    @BindView(R.id.internal_bedroom)
    Spinner internal_bedroom;
    @BindView(R.id.internal_bath)
    Spinner internal_bath;
    @BindView(R.id.internal_shop_office)
    Spinner internal_shop_office;*/

    //@BindView(R.id.internal_hall_dinning)
    static EditText internal_hall_dinning;
    //@BindView(R.id.internal_kitchen)
    static EditText internal_kitchen;
    //@BindView(R.id.internal_bedroom)
    static EditText internal_bedroom;
    //@BindView(R.id.internal_bath)
    static EditText internal_bath;
    //@BindView(R.id.internal_shop_office)
    static EditText internal_shop_office;

    static EditText internal_pooja_room;


    // actual_usage
    ArrayList<PropertyActualUsage> Inter_floors_list = new ArrayList<>();
    public static String measurment_floor_id = "0";

    public static TextView textview_carpetloading_per;
    public static TextView textview_loading_factor;
    public static Spinner spinner_carpetloading_type;
    public Dialog dialog;
    String val_carpetloading_per = "";
    String val_textview_loading_factor_per = "";
    String val_carpetloading_type = "";

    // calc click function
    @BindView(R.id.open_calc_builduparea)
    ImageView open_calc_builduparea;
    @BindView(R.id.open_calc_saleablearea)
    ImageView open_calc_saleablearea;
    @BindView(R.id.open_calc_carpetarea)
    ImageView open_calc_carpetarea;

    @BindView(R.id.img_permssible_area)
    ImageView img_permssible_area;


    // calc
    Button button0, button1, button2, button3, button4, button5, button6,
            button7, button8, button9, buttonAdd, buttonSub, buttonDivision,
            buttonMul, button10, buttonC, buttonEqual, button_set, button_close;
    TextView edt1;
    float mValueOne, mValueCurrent;
    boolean mAddition, mSubtract, mMultiplication, mDivision, mClear;

    /***** Declaring Variable *****/
    TextView btnClear;
    TextView tvProcessor, tvResult;

    TextView btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero;
    String processor;
    Boolean isSmallBracketOpen;
    TextView btnMultiply, btnMinus, btnPlus, btnDivide, btnclose, btnsetvalue, btnDecimal, btnBack, btnSmallBracket, btnEqual, btnPercentage;
    int developedCounter;
    static String developedBy = "Sabesh";
    static String deveopedNote = "developed in IU, Tue Feb 28, 2024";
    double result_value = 0.0;


    /*Flat Valuation*/
    public static Runnable mJumpRunnable;
    public static long DELAY_TIME = 3000; //3 seconds
    public static Handler mHandler;




    // TODO - Textview
    @BindView(R.id.textview_area_measurment)
    TextView textview_area_measurment;
    @BindView(R.id.textview_current_fair)
    TextView textview_current_fair;
    @BindView(R.id.textview_areatype_heading)
    TextView textview_areatype_heading;
    @BindView(R.id.textview_currentarea_heading)
    TextView textview_currentarea_heading;
    @BindView(R.id.textview_marketrate_heading)
    TextView textview_marketrate_heading;
    @BindView(R.id.textview_marketvalue_heading)
    TextView textview_marketvalue_heading;


    @BindView(R.id.textview_insurance)
    TextView textview_insurance;
    @BindView(R.id.textview_area_sub_heading)
    TextView textview_area_sub_heading;
    @BindView(R.id.textview_dlcrate_heading)
    TextView textview_dlcrate_heading;
    @BindView(R.id.textview_governmentvalue_head)
    TextView textview_governmentvalue_head;
    @BindView(R.id.textview_governmentvalue_subhead)
    TextView textview_governmentvalue_subhead;
    @BindView(R.id.textview_realizable_head)
    TextView textview_realizable_head;
    @BindView(R.id.textview_realizable_distress_heading)
    TextView textview_realizable_distress_heading;
    @BindView(R.id.textview_value_subheading)
    TextView textview_value_subheading;
    @BindView(R.id.textview_realizable_value_head)
    TextView textview_realizable_value_head;
    @BindView(R.id.textview_distress_head)
    TextView textview_distress_head;

    @BindView(R.id.textview_aspercompletion)
    TextView textview_aspercompletion;
    @BindView(R.id.textview_proposedvaluation)
    TextView textview_proposedvaluation;
    @BindView(R.id.tvRatePopup)
    TextView tvRatePopup;
    @BindView(R.id.tvCompareRate)
    TextView tvCompareRate;

    public static Context mContext;
    private static final String TAG = "FragmentValuationPentho";

    // TODO - Edittext

    public static EditText edittext_type_area;
    public static EditText edittext_type_rate;
    public static EditText edittext_terrace_area;
    public static EditText edittext_terrace_rate;
    public static EditText edittext_insurance_area;
    public static EditText edittext_insurance_rate;
    public static EditText edittext_governmentvalue_area;
    public static EditText edittext_governmentvalue_rate;
    public static EditText editText_AsPerCompletion;
    public static EditText edittext_proposedvaluation_result;
    public static EditText editText_remarks;
    public static EditText edittext_carpet_area;
    public static EditText edittext_builtup_area;
    public static EditText edittext_Saleable_area;
    public static EditText edittext_permissible_area;
    public static TextView edittext_measurementunit;
    public static EditText edittext_realizable_value_total;
    public static EditText edittext_distress_total;

    // TODO - Textview
    public static TextView textview_type_of_area;
    public static TextView textview_aspercompletion_result;
    public static TextView textview_totalpropertyvalue_result;
    public static TextView textview_type_marketvalue;
    public static TextView textview_terrace_marketvalue;
    public static TextView textview_total_marketval;
    public static TextView textview_insurance_marketvalue;
    public static TextView textview_governmentvalue_marketvalue;
    public static TextView textview_totalpropertyvalue;
    public static TextView spinnerLoadingOverBuildup;



    // TODO - Spinner
    /*public static Spinner spinnerLoadingOverBuildup;*/
    public static Spinner spinnerAreaType;
    public static TextView spinner_realizable;
    public static TextView spinner_distress;


    @BindView(R.id.textview_areatype)
    TextView textview_areatypetvalue;
    @BindView(R.id.textview_loadingoverbuildup)
    TextView textview_loadingoverbuildup;



    public static EditText etEstimateCost;
    public static EditText etLoanAmount;
    public static EditText etOwnContribution;
    public static EditText etAverageConstruction;
    public static EditText etRecommendationPercentage;
    public static EditText etAmountSpend;
    public static EditText etAmountDisbursement;

    // TODO - String
    public static String property_type = "";
    public static String areaTypeID = "", Measurement_id = "1";

    public static ArrayAdapter<String> adapterAreaType;
    public static FragmentActivity fragmentActivity;
    public static ArrayList<String> loadingbuildupList = new ArrayList<>();
    public static ArrayList<String> realizableList = new ArrayList<>();
    public static ArrayList<String> distressList = new ArrayList<>();

    private RatePopupApi ratePopupApi;
    public static String caseid;

    public FSFlatValuation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FSFlatValuation.
     */
    // TODO: Rename and change types and number of parameters
    public static FSFlatValuation newInstance(String param1, String param2) {
        FSFlatValuation fragment = new FSFlatValuation();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_f_s_flat_valuation, container, false);

        ButterKnife.bind(this, view);
        general = new General(getActivity());
        fragmentActivity = getActivity();
        SettingsUtils.init(getActivity());

        initViewsStatic(view);
        initFlatValuation(view);
        initViews();
         InitiatePentHouseValues();
        /*AreaTextWatcher();*/


        open_calc_builduparea.setOnClickListener(v -> Calculation_Popup_New(1));

        open_calc_saleablearea.setOnClickListener(v -> Calculation_Popup_New(2));

        img_permssible_area.setOnClickListener(v -> Calculation_Popup_New(4));

        open_calc_carpetarea.setOnClickListener(v -> Calculation_Popup_New(3));



        // TODO -  call the mandatory_valiadation
        if (Singleton.getInstance().enable_validation_error) {
            set_pentflathouse_mandatory();
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void initFlatValuation(View view){
        initValues(view);
        setPropertyType();

        AreaTypeSpinner(Singleton.getInstance().areaType, "Select");

        PentHouseFlatCalculation();
        RealizableDistressSpinner();
        LoadingBuildupSpinner();
        RealizeDistressTotalVal();

        DisplayValuation();



        tvCompareRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails();
            }
        });


        spinner_realizable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(spinner_realizable);
                realizable_distress_popup(1, spinner_realizable);
            }
        });
        spinner_distress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(spinner_distress);
                realizable_distress_popup(2, spinner_distress);
            }
        });

        spinnerLoadingOverBuildup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(spinnerLoadingOverBuildup);
                loadingOverBuildup_popup(spinnerLoadingOverBuildup);
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
            } else {
                /* Custom */
                spinner_realizable.setText(getResources().getString(R.string.custom));
                edittext_realizable_value_total.setFocusable(true);
                edittext_realizable_value_total.setFocusableInTouchMode(true);
                edittext_realizable_value_total.setEnabled(true);
                edittext_realizable_value_total.setText("" + general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getRealizationValue()));

            }
        } else {
            /* select */
            spinner_realizable.setText(getResources().getString(R.string.select));
        }

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
            } else {
                /* Custom */
                spinner_distress.setText(getResources().getString(R.string.custom));
                edittext_distress_total.setFocusable(true);
                edittext_distress_total.setFocusableInTouchMode(true);
                edittext_distress_total.setEnabled(true);
                edittext_distress_total.setText("" + general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getDistressValue()));

            }
        } else {
            /* select */
            spinner_distress.setText(getResources().getString(R.string.select));
        }

        loadingbuildupList = new ArrayList<>();
        loadingbuildupList = general.LoadingBuilup_common_array();
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage())) {
            /* values */
            boolean is_value_from_loop = false;
            for (int x = 0; x < loadingbuildupList.size(); x++) {
                if (loadingbuildupList.get(x).toString().replace("%", "").trim().equalsIgnoreCase(Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage())) {
                    is_value_from_loop = true;
                }
            }
            if (is_value_from_loop) {
                /* From array */
                spinnerLoadingOverBuildup.setText(Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage());
            }
        } else {
            /* select */
            spinnerLoadingOverBuildup.setText(getResources().getString(R.string.select));
        }




        caseid = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");

        ratePopupApi = new RatePopupApi(getActivity(), this);

        Log.e(TAG, "onCreateView: " + PhotoLatLong.latvalue.getText().toString());

        if (latvalue.getText().toString()!=null) {
            ratePopupApi.getRatePopup(caseid, latvalue.getText().toString(), longvalue.getText().toString());
        }else {
            ratePopupApi.getRatePopup(caseid, String.valueOf(SettingsUtils.Latitudes), String.valueOf(SettingsUtils.Longitudes));
        }


        subsequentRevaluation();
    }




    private void setMeasurementSpinner(int id, ArrayList<Measurements> measurements, Spinner internalspinner) {
        if (measurements.size() > 0) {
            for (int i = 0; i < measurements.size(); i++) {
                if (measurements.get(i).getName().equalsIgnoreCase("Select")) {
                } else {
                    int floorno = measurements.get(i).getMeasureUnitId();
                    if (id == floorno) {
                        internalspinner.setSelection(i);
                    }
                }
            }
        }
    }


    private void initViewsStatic(View view) {
        internal_hall_dinning = (EditText) view.findViewById(R.id.internal_hall_dinning);
        internal_kitchen = (EditText) view.findViewById(R.id.internal_kitchen);
        internal_bedroom = (EditText) view.findViewById(R.id.internal_bedroom);
        internal_bath = (EditText) view.findViewById(R.id.internal_bath);
        internal_shop_office = (EditText) view.findViewById(R.id.internal_shop_office);
        internal_pooja_room = view.findViewById(R.id.internal_pooja_room);

        editText_totalapartmentfloors = (EditText) view.findViewById(R.id.editText_totalapartmentfloors);
        editText_totalflatsperfloor = (EditText) view.findViewById(R.id.editText_totalflatsperfloor);
        editText_flatsituatedinfloor = (EditText) view.findViewById(R.id.editText_flatsituatedinfloor);
        //editText_floors = (EditText) view.findViewById(R.id.editText_floors);
        editText_as_per_docs = (EditText) view.findViewById(R.id.editText_as_per_docs);
        edittext_general_floor_name = (EditText) view.findViewById(R.id.edittext_general_floor_name);
        edittext_general_comp = (EditText) view.findViewById(R.id.edittext_general_comp);
        edittext_general_progress = (EditText) view.findViewById(R.id.edittext_general_progress);
        edittext_general_age = (EditText) view.findViewById(R.id.edittext_general_age);
        edittext_general_life = (EditText) view.findViewById(R.id.edittext_general_life);
        edittext_general_saleablearea = (EditText) view.findViewById(R.id.edittext_general_saleablearea);
        et_permssible_area = (EditText) view.findViewById(R.id.et_permssible_area);
        edittext_general_builduparea = (EditText) view.findViewById(R.id.edittext_general_builduparea);
        edittext_general_carpetarea = (EditText) view.findViewById(R.id.edittext_general_carpetarea);
        textview_comp_total = (TextView) view.findViewById(R.id.textview_comp_total);
        textview_internal_floor_name_composition = (TextView) view.findViewById(R.id.textview_internal_floor_name_composition);
        spinnerMeasurement_doc = (Spinner) view.findViewById(R.id.spinnerMeasurement_doc);
        spinner_measurement_act = (Spinner) view.findViewById(R.id.spinner_measurement_act);
        stageSpinner = (Spinner) view.findViewById(R.id.stageSpinner);
        spinner_carpetloading_type = (Spinner) view.findViewById(R.id.spinner_carpetloading_type);
        textview_carpetloading_per = (TextView) view.findViewById(R.id.textview_carpetloading_per);
        textview_loading_factor = (TextView) view.findViewById(R.id.textview_loading_factor);

        //  limit the 2 char after the decimal point
        editText_as_per_docs.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
        edittext_general_builduparea.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        edittext_general_saleablearea.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        et_permssible_area.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        edittext_general_carpetarea.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});


    }

    private void initViews() {


        textview_land_measurement.setTypeface(general.mediumtypeface());
        editText_totalapartmentfloors.setTypeface(general.regulartypeface());
        editText_totalflatsperfloor.setTypeface(general.regulartypeface());
        editText_flatsituatedinfloor.setTypeface(general.regulartypeface());
        textview_actualusage_flat.setTypeface(general.regulartypeface());
//        editText_floors.setTypeface(general.regulartypeface());
        editText_as_per_docs.setTypeface(general.regulartypeface());
        textview_measurement_doc.setTypeface(general.regulartypeface());
        textview_measurement_act.setTypeface(general.regulartypeface());

        textview_generl_info_measu.setTypeface(general.mediumtypeface());
        textview_internal_composition.setTypeface(general.mediumtypeface());
        edittext_general_floor_name.setTypeface(general.regulartypeface());
        edittext_general_comp.setTypeface(general.regulartypeface());
        edittext_general_progress.setTypeface(general.regulartypeface());
        edittext_general_age.setTypeface(general.regulartypeface());
        edittext_general_life.setTypeface(general.regulartypeface());
        textview_floor_name.setTypeface(general.regulartypeface());
        textview_stage.setTypeface(general.regulartypeface());
        textview_comp.setTypeface(general.regulartypeface());
        textview_progress.setTypeface(general.regulartypeface());
        textview_carpetarea.setTypeface(general.regulartypeface());
        textview_builduparea.setTypeface(general.regulartypeface());
        textviewlabel_carpetloading_per.setTypeface(general.regulartypeface());
        textviewlabel_carpet_area_per.setTypeface(general.regulartypeface());
        textviewlabel_carpetloading_type.setTypeface(general.regulartypeface());
        textview_saleablearea.setTypeface(general.regulartypeface());
        textview_age.setTypeface(general.regulartypeface());
        textview_life.setTypeface(general.regulartypeface());
        textview_average.setTypeface(general.regulartypeface());
        textview_comp_total.setTypeface(general.regulartypeface());
        textview_total.setTypeface(general.regulartypeface());
        textview_doc_total.setTypeface(general.regulartypeface());
        textview_actual_total.setTypeface(general.regulartypeface());
        textview_floor_name_composition.setTypeface(general.regulartypeface());
        textview_hall_dinning.setTypeface(general.regulartypeface());
        textview_kitchen.setTypeface(general.regulartypeface());
        textview_bedroom.setTypeface(general.regulartypeface());
        textview_bath.setTypeface(general.regulartypeface());
        textview_shop_office.setTypeface(general.regulartypeface());
        textview_internal_floor_name_composition.setTypeface(general.regulartypeface());
        edittext_general_saleablearea.setTypeface(general.regulartypeface());
        et_permssible_area.setTypeface(general.regulartypeface());
        edittext_general_builduparea.setTypeface(general.regulartypeface());
        edittext_general_carpetarea.setTypeface(general.regulartypeface());
        textview_carpetloading_per.setTypeface(general.regulartypeface());
        textview_loading_factor.setTypeface(general.regulartypeface());

    }

    public void InitiatePentHouseValues() {

        /*editText_floors.setText("1");
        editText_floors.setFocusable(false);*/

        // TODO - changes
        edittext_general_comp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    String value = edittext_general_comp.getText().toString();
                    textview_comp_total.setText(value);
                    if (!general.isEmpty(value)) {
                        setEdittextAsPerCompletion(value);
                    }
                }
            }
        });

        /*edittext_general_comp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String value = charSequence.toString();
                textview_comp_total.setText(value);

                if (!general.isEmpty(value)) {
                    FragmentValuationPenthouse fragmentValuationPenthouse = new FragmentValuationPenthouse();
                    fragmentValuationPenthouse.setEdittextAsPerCompletion(value);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        edittext_general_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String age = charSequence.toString();
                if (!general.isEmpty(age)) {
                    if (Integer.valueOf(age) < 46) {
                        int life = general.getAgeLifeValue(Integer.valueOf(age));
                        edittext_general_life.setText("" + life);
                    } else {
                        edittext_general_life.setText("");
                    }
                } else {
                    edittext_general_life.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        edittext_general_floor_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String floorname = charSequence.toString();
                textview_internal_floor_name_composition.setText(floorname);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        SpinnerInitiateValuesSet();
        DisplayFlatorPentHouseValues();
    }

    private void AreaTextWatcher() {
        Singleton.getInstance().areaType.clear();
        Singleton.getInstance().areaType.add("Select");
        edittext_general_carpetarea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEdittextCarpetArea(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSpinnerAreaType(editable.toString(), getResources().getString(R.string.carpet));

            }
        });


        edittext_general_builduparea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEdittextBuildupArea(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSpinnerAreaType(editable.toString(), getResources().getString(R.string.builtup));
            }
        });

        edittext_general_builduparea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    fun_carpet_value();
                }
            }
        });

        edittext_general_saleablearea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEdittextSaleableArea(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSpinnerAreaType(editable.toString(), getResources().getString(R.string.Saleable));
            }
        });

        edittext_general_saleablearea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    fun_carpet_value();
                }
            }
        });

        et_permssible_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEdittextPermissibleArea(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSpinnerAreaType(editable.toString(), getResources().getString(R.string.permissible_area));
            }
        });
        et_permssible_area.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    fun_carpet_value();
                }
            }
        });


    }

    private void checkSpinnerAreaType(String editable, String area) {

        if (!general.isEmpty(editable.toString())) {
            if (!Singleton.getInstance().areaType.contains(area)) {
                Singleton.getInstance().areaType.add(area);
            }
        } else {
            if (Singleton.getInstance().areaType.contains(area)) {
                for (int i = 0; i < Singleton.getInstance().areaType.size(); i++) {
                    if (Singleton.getInstance().areaType.get(i).equalsIgnoreCase(area)) {
                        Singleton.getInstance().areaType.remove(i);
                    }
                }
            }
        }

        AreaTypeSpinner(Singleton.getInstance().areaType, area);
    }

    public void SpinnerInitiateValuesSet() {

        // Todo spinner set for penthouse land measurement
        getMeasurementonGeneralFloor();
        // Todo Multiselect
        getActualUsage();

        // Todo spinner set for penthouse general info measurement
        getConstructionStageSpinner();

        // Todo spinner set for internal composition floor values
        //setInternalCompositionFloorSpinner();
    }

    public void getMeasurementonGeneralFloor() {
        ArrayAdapter<Measurements> adapterMeasurements = new ArrayAdapter<>(getActivity(),
                R.layout.row_spinner_item, Singleton.getInstance().measurements_list);
        adapterMeasurements.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinnerMeasurement_doc.setAdapter(adapterMeasurements);
        spinnerMeasurement_doc.setOnTouchListener(this);

        spinnerMeasurement_doc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("spinner_measurement", "::: " + Singleton.getInstance().measurements_list.get(position).getMeasureUnitId());

                Singleton.getInstance().indProperty.setDocumentLandAreaUnit(Singleton.getInstance().measurements_list.get(position).getMeasureUnitId());
                Singleton.getInstance().indProperty.setMeasuredLandAreaUnit(Singleton.getInstance().measurements_list.get(position).getMeasureUnitId());

               }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<Measurements> adapterMeasurements2 = new ArrayAdapter<Measurements>(getActivity(),
                R.layout.row_spinner_item, Singleton.getInstance().measurements_list_flat);
        adapterMeasurements2.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_measurement_act.setAdapter(adapterMeasurements2);
        spinner_measurement_act.setSelection(1);
        spinner_measurement_act.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                measurment_floor_id = String.valueOf(Singleton.getInstance().measurements_list_flat.get(position).getMeasureUnitId());

                if (position == 0) {
                    edittext_measurementunit.setText("");
                } else {
                    edittext_measurementunit.setText("" + Singleton.getInstance().measurements_list_flat.get(position).getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        final ArrayList<String> carpetloading_type = new ArrayList<>();
        carpetloading_type.add("Select");
        carpetloading_type.add("Buildup Area");
        carpetloading_type.add("Saleable Area");
        carpetloading_type.add("Permissible Area");

        ArrayAdapter<String> adapterMeasurements3 = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner_item, carpetloading_type);
        adapterMeasurements3.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_carpetloading_type.setAdapter(adapterMeasurements3);

        String SelectedCarpetAreaTypeId = Singleton.getInstance().indPropertyValuation.getSelectedCarpetAreaTypeId();
        if (!general.isEmpty(SelectedCarpetAreaTypeId)) {
            int pos = Integer.parseInt(SelectedCarpetAreaTypeId);
            spinner_carpetloading_type.setSelection(pos, false);
            val_carpetloading_type = "" + SelectedCarpetAreaTypeId;
        } else {
            val_carpetloading_type = "" + 0;
            spinner_carpetloading_type.setSelection(0, false);
        }

        spinner_carpetloading_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                val_carpetloading_type = "" + position;
                Singleton.getInstance().indPropertyValuation.setSelectedCarpetAreaTypeId("" + position);
                fun_carpet_value();
                clickedSelectSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        String CarpetAreaPercentage_str = Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage();
        if (!general.isEmpty(CarpetAreaPercentage_str)) {
            val_carpetloading_per = CarpetAreaPercentage_str;
            textview_carpetloading_per.setText(CarpetAreaPercentage_str);
        } else {
            val_carpetloading_per = "";
            textview_carpetloading_per.setText("Select");
        }

        textview_carpetloading_per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(textview_carpetloading_per);
                carpetloading_per(textview_carpetloading_per, "carpet_per");

            }
        });


        String SaleableLoadingPercentage_str = Singleton.getInstance().indPropertyValuation.getSaleableLoadingPercentage();
        if (!general.isEmpty(SaleableLoadingPercentage_str)) {
            val_textview_loading_factor_per = SaleableLoadingPercentage_str;
            textview_loading_factor.setText(SaleableLoadingPercentage_str);
        } else {
            val_textview_loading_factor_per = "";
            textview_loading_factor.setText("Select");
        }

        textview_loading_factor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(textview_loading_factor);
                carpetloading_per(textview_loading_factor, "loading_factor");
            }
        });


    }

    /*public void getConstructionStageSpinner() {
        ArrayAdapter<Construction> adapterStageConstruct = new ArrayAdapter<>(getActivity(),
                R.layout.row_spinner_item, Singleton.getInstance().constructions_list);
        adapterStageConstruct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stageSpinner.setAdapter(adapterStageConstruct);

        stageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int stageSpinnerSelectedItemId = (int) stageSpinner.getSelectedItemId();
                int selectposition = stageSpinner.getSelectedItemPosition();

                if (position > 0) {
                    if (Singleton.getInstance().indPropertyFloors != null)
                        if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                            IndPropertyFloor indPropertyFloor = Singleton.getInstance().indPropertyFloors.get(0);

                            int constructid = 0;
                            if (Singleton.getInstance().constructions_list.size() > 0) {
                                for (int i = 0; i < Singleton.getInstance().constructions_list.size(); i++) {
                                    if (selectposition == i) {
                                        constructid = Singleton.getInstance().constructions_list.get(i).getConstructionId();
                                    }
                                }
                            }

                            indPropertyFloor.setConstructionStageId(constructid);
                            Singleton.getInstance().indPropertyFloors.set(0, indPropertyFloor);

                        } else {
                            IndPropertyFloor indPropertyFloor = new IndPropertyFloor();
                            int constructid = 0;
                            if (Singleton.getInstance().constructions_list.size() > 0) {
                                for (int i = 0; i < Singleton.getInstance().constructions_list.size(); i++) {
                                    if (selectposition == i) {
                                        constructid = Singleton.getInstance().constructions_list.get(i).getConstructionId();
                                    }
                                }
                            }

                            indPropertyFloor.setConstructionStageId(constructid);
                            Singleton.getInstance().indPropertyFloors.add(indPropertyFloor);
                        }
                }
                if (position == 1) {
                    edittext_general_comp.setText("" + 100);
                    textview_comp_total.setText("" + 100);
                    edittext_general_progress.setText("");

                    edittext_general_comp.setEnabled(false);
                    edittext_general_progress.setEnabled(false);
                } else {
                    if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                        int value = Singleton.getInstance().indPropertyFloors.get(0).getPercentageCompletion();
                        if (value == 0)
                            edittext_general_comp.setText("");
                    }
                    edittext_general_comp.setEnabled(true);
                    edittext_general_progress.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }*/

    public void getConstructionStageSpinner() {
        ArrayAdapter<Construction> adapterStageConstruct = new ArrayAdapter<>(getActivity(),
                R.layout.row_spinner_item, Singleton.getInstance().constructions_list);

        adapterStageConstruct.setDropDownViewResource(R.layout.row_spinner_item_popup);


        stageSpinner.setAdapter(adapterStageConstruct);
        stageSpinner.setOnTouchListener(this);

        if (Singleton.getInstance().indPropertyFloors.size() > 0) {
            if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getConstructionStageId()))) {
                int constructionid = Singleton.getInstance().indPropertyFloors.get(0).getConstructionStageId();
                stageSpinner.setSelection(constructionid, false);
            } else {
                stageSpinner.setSelection(0, false);
            }
        } else {
            stageSpinner.setSelection(0, false);
        }

        stageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int stageSpinnerSelectedItemId = (int) stageSpinner.getSelectedItemId();
                int selectposition = stageSpinner.getSelectedItemPosition();

                if (position > 0) {
                    if (Singleton.getInstance().indPropertyFloors != null) {
                        if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                            IndPropertyFloor indPropertyFloor = Singleton.getInstance().indPropertyFloors.get(0);
                            int constructid = 0;
                            if (Singleton.getInstance().constructions_list.size() > 0) {
                                for (int i = 0; i < Singleton.getInstance().constructions_list.size(); i++) {
                                    if (selectposition == i) {
                                        constructid = Singleton.getInstance().constructions_list.get(i).getConstructionId();
                                    }
                                }
                            }
                            indPropertyFloor.setConstructionStageId(constructid);
                            Singleton.getInstance().indPropertyFloors.set(0, indPropertyFloor);
                        } else {
                            IndPropertyFloor indPropertyFloor = new IndPropertyFloor();
                            int constructid = 0;
                            if (Singleton.getInstance().constructions_list.size() > 0) {
                                for (int i = 0; i < Singleton.getInstance().constructions_list.size(); i++) {
                                    if (selectposition == i) {
                                        constructid = Singleton.getInstance().constructions_list.get(i).getConstructionId();
                                    }
                                }
                            }
                            indPropertyFloor.setConstructionStageId(constructid);
                            Singleton.getInstance().indPropertyFloors.add(indPropertyFloor);
                        }
                    }
                }

                if (position == 1) {
                    // not editable - complete
                    edittext_general_comp.setText("" + 100);
                    textview_comp_total.setText("" + 100);
                    edittext_general_progress.setText("");
                    edittext_general_comp.setEnabled(false);
                    edittext_general_progress.setEnabled(false);
                    // as per com 100 in valuation
                    if (!general.isEmpty("" + 100)) {
                        setEdittextAsPerCompletion("" + 100);
                    }
                } else if (position == 2) {
                    // editable - underconstruction
                    edittext_general_comp.setText("");
                    textview_comp_total.setText("");
                    edittext_general_progress.setText("");
                    edittext_general_comp.setEnabled(true);
                    edittext_general_progress.setEnabled(true);
                    edittext_general_age.setText("");
                    edittext_general_life.setText("");
                } else {
                    // not editable - select
                    edittext_general_comp.setText("");
                    textview_comp_total.setText("");
                    edittext_general_progress.setText("");
                    edittext_general_comp.setEnabled(false);
                    edittext_general_progress.setEnabled(false);
                }

                if (FragmentFlat.edittext_general_comp != null) {
                    FragmentFlat.edittext_general_comp.setError(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }


    public void getActualUsage() {
        /* sugan Integration */
        Singleton.getInstance().PropertyActualUsage_id.clear();
        Singleton.getInstance().PropertyActualUsage_name.clear();
        textview_actual_usage_flat.setTypeface(general.regulartypeface());
        function_actual_usage();
        textview_actual_usage_flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(textview_actual_usage_flat);
                showdialog_dynamic("actual_usage");
            }
        });
        /* sugan Integration */

    }



    private void show_emptyFocus() {
        // Show focus
        if (OtherDetails.my_focuslayout != null) {
            OtherDetails.my_focuslayout.requestFocus();
        }
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
                            textview_actual_usage_flat.setText(general.remove_array_brac_and_space(Singleton.getInstance().PropertyActualUsage_name.toString()));
                        }
                    }
                }
            } else {
                textview_actual_usage_flat.setText(getResources().getString(R.string.select));
            }
        }
    }

    private void showdialog_dynamic(final String type_) {
        final String type_of_dialog = type_;

        View view = LayoutInflater.from(getContext()).inflate(R.layout.multiselect_checkbox, null);
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
                        textview_actual_usage_flat.setText(PropertyActualUsage_name);
                    } else {
                        // clear all the ID and dummy data and clear the settext
                        Singleton.getInstance().indProperty.setPropertyActualUsageId("");
                        textview_actual_usage_flat.setText(getResources().getString(R.string.select));
                    }
                    Log.e("PropertyActualUsage_id", "::: " + Singleton.getInstance().PropertyActualUsage_id);
                    Log.e("PropertyActuaUs_name", ":: " + Singleton.getInstance().PropertyActualUsage_name);
                }

                alertDialog.dismiss();
            }
        });
    }


    private void set_pentflathouse_mandatory() {

        String flatsituated = editText_flatsituatedinfloor.getText().toString();
        String stagespinner = stageSpinner.getSelectedItem().toString();
        String percentagecomp = edittext_general_comp.getText().toString();

        if (!general.isEmpty(flatsituated)) {
            editText_flatsituatedinfloor.setError(null);
        } else {
            editText_flatsituatedinfloor.requestFocus();
            editText_flatsituatedinfloor.setError(getActivity().getResources().getString(R.string.err_flatsituated));
        }

        if (stagespinner.equalsIgnoreCase("Select")) {
            ((TextView) stageSpinner.getSelectedView()).setError(getActivity().getResources().getString(R.string.err_construction_stage));
            edittext_general_comp.requestFocus();
        }

        if (!general.isEmpty(percentagecomp)) {
            edittext_general_comp.setError(null);
        } else {
            edittext_general_comp.requestFocus();
            edittext_general_comp.setError(getActivity().getResources().getString(R.string.err_percentage_completion));
        }


        if (general.isEmpty(edittext_general_builduparea.getText().toString())) {
            edittext_general_builduparea.setError("Build area required!");
            edittext_general_builduparea.requestFocus();
        }

        if (general.isEmpty(edittext_general_saleablearea.getText().toString())) {
            edittext_general_saleablearea.setError("Saleable area required!");
            edittext_general_saleablearea.requestFocus();
        }

        if (general.isEmpty(edittext_general_carpetarea.getText().toString())) {
            edittext_general_carpetarea.setError("Carpet area required!");
            edittext_general_carpetarea.requestFocus();
        }

    }


    private void setFloorNoInternals(ArrayList<InternalFloorModel> internalFloorModels) {
        internalFloorModels.clear();
        InternalFloorModel floorModelw = new InternalFloorModel();
        floorModelw.setId(0); //-1
        floorModelw.setName("Select");
        internalFloorModels.add(floorModelw);
        for (int i = 1; i <= 10; i++) {
            InternalFloorModel floorModel = new InternalFloorModel();
            floorModel.setId(i);
            int setId = i - 1;
            floorModel.setName("" + setId);
            floorModel.setFloorid(setId);
            internalFloorModels.add(floorModel);
        }
    }

    /*******
     * Display PentHouseValues from API
     * *********/
    private void DisplayFlatorPentHouseValues() {

        // Todo edittext set values from API
        setFlatPentHousePropertyDetails();
        setFlatPentHouseGeneralInternalFloors();

        // Todo Spinner internal floors selection from api
        setSpinnerInternalFloors();
    }


    public void setFlatPentHousePropertyDetails() {

        if (Singleton.getInstance().indProperty != null) {
            if (!general.isEmpty(Singleton.getInstance().indProperty.getTotalApartmentFloors()))
                editText_totalapartmentfloors.setText(Singleton.getInstance().indProperty.getTotalApartmentFloors());
            if (!general.isEmpty(Singleton.getInstance().indProperty.getTotalFlatsPerFloor()))
                editText_totalflatsperfloor.setText(Singleton.getInstance().indProperty.getTotalFlatsPerFloor());
            if (!general.isEmpty(Singleton.getInstance().indProperty.getFlatSituatedInFloor()))
                editText_flatsituatedinfloor.setText(Singleton.getInstance().indProperty.getFlatSituatedInFloor());
            if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getNoOfFloors()))) {
                int floorsno = Singleton.getInstance().indProperty.getNoOfFloors();
                /*if (floorsno == 0) {
                    floorsno = 1;
                    editText_floors.setText("" + floorsno);
                } else {
                    editText_floors.setText("" + floorsno);
                }*/
            }

            if (!general.isEmpty(Singleton.getInstance().indProperty.getDocumentLandArea()))
                editText_as_per_docs.setText(Singleton.getInstance().indProperty.getDocumentLandArea());
            if (!general.isEmpty(Singleton.getInstance().indProperty.getAvgPercentageCompletion()))
                textview_comp_total.setText(Singleton.getInstance().indProperty.getAvgPercentageCompletion());


            int docpos = Singleton.getInstance().indProperty.getDocumentLandAreaUnit();
            int actpos = Singleton.getInstance().indProperty.getMeasuredLandAreaUnit();
            if (docpos != 0) {
                Singleton.getInstance().indProperty.setDocumentLandAreaUnit(docpos);
                setMeasurementSpinner(docpos, Singleton.getInstance().measurements_list, spinnerMeasurement_doc);
            }

            if (actpos != 0) {
                Singleton.getInstance().indProperty.setMeasuredLandAreaUnit(actpos);
                setMeasurementSpinner(actpos, Singleton.getInstance().measurements_list_flat, spinner_measurement_act);
            }
        }
    }

    public void setFlatPentHouseGeneralInternalFloors() {
        if (Singleton.getInstance().indPropertyFloors.size() > 0) {
            if (!general.isEmpty(Singleton.getInstance().indPropertyFloors.get(0).getFloorName())) {
                String floorname = Singleton.getInstance().indPropertyFloors.get(0).getFloorName();
                edittext_general_floor_name.setText(floorname);
                textview_internal_floor_name_composition.setText(floorname);
            }
            if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getPercentageCompletion())))
                edittext_general_comp.setText("" + Singleton.getInstance().indPropertyFloors.get(0).getPercentageCompletion());
            if (!general.isEmpty(Singleton.getInstance().indPropertyFloors.get(0).getPresentCondition()))
                edittext_general_progress.setText(Singleton.getInstance().indPropertyFloors.get(0).getPresentCondition());
            if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getPropertyAge())))
                edittext_general_age.setText("" + Singleton.getInstance().indPropertyFloors.get(0).getPropertyAge());
            if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getResidualLife())))
                edittext_general_life.setText("" + Singleton.getInstance().indPropertyFloors.get(0).getResidualLife());

        }


        if (Singleton.getInstance().indPropertyValuation != null) {
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getSuperBuildUpArea())) {
                edittext_general_saleablearea.setText(Singleton.getInstance().indPropertyValuation.getSuperBuildUpArea());
            }
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getBuildUpArea())) {
                edittext_general_builduparea.setText(Singleton.getInstance().indPropertyValuation.getBuildUpArea());
            }
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getCarpetArea())) {
                edittext_general_carpetarea.setText(Singleton.getInstance().indPropertyValuation.getCarpetArea());
            }

            if (Singleton.getInstance().indPropertyFloors != null && Singleton.getInstance().indPropertyFloors.size() > 0 && Singleton.getInstance().indPropertyFloors.get(0) != null) {
                et_permssible_area.setText(Singleton.getInstance().indPropertyFloors.get(0).getSanctionedFloorArea());
            }


        }
    }

    private void setSpinnerInternalFloors() {
        if (Singleton.getInstance().indPropertyFloors != null) {
            if (Singleton.getInstance().indPropertyFloors.size() > 0) {

                if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getFlatHallNo()))) {
                    internal_hall_dinning.setText(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getFlatHallNo()));
                }

                if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getFlatKitchenNo()))) {
                    internal_kitchen.setText(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getFlatKitchenNo()));
                }

                if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getFlatBedroomNo()))) {
                    internal_bedroom.setText(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getFlatBedroomNo()));
                }

                if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getFlatBathNo()))) {
                    internal_bath.setText(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getFlatBathNo()));
                }

                if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getOfficeNo()))) {
                    internal_shop_office.setText(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getOfficeNo()));
                }
                if (!general.isEmpty(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getOfficeNo()))) {
                    internal_pooja_room.setText(String.valueOf(Singleton.getInstance().indPropertyFloors.get(0).getFlatPoojaNo()));
                }

                /*int halldinning = Singleton.getInstance().indPropertyFloors.get(0).getFlatHallNo();
                int kitchen = Singleton.getInstance().indPropertyFloors.get(0).getFlatKitchenNo();
                int bedroom = Singleton.getInstance().indPropertyFloors.get(0).getFlatBedroomNo();
                int bath = Singleton.getInstance().indPropertyFloors.get(0).getFlatBathNo();
                int shopno = Singleton.getInstance().indPropertyFloors.get(0).getShopNo();

                setFloorNumbersSpinner(halldinning, Singleton.getInstance().internalFloorHalldining, internal_hall_dinning);
                setFloorNumbersSpinner(kitchen, Singleton.getInstance().internalFloorKitchen, internal_kitchen);
                setFloorNumbersSpinner(bedroom, Singleton.getInstance().internalFloorBedroom, internal_bedroom);
                setFloorNumbersSpinner(bath, Singleton.getInstance().internalFloorBath, internal_bath);
                setFloorNumbersSpinner(shopno, Singleton.getInstance().internalFloorshopOffice, internal_shop_office);*/


               /* int constructionid = Singleton.getInstance().indPropertyFloors.get(0).getConstructionStageId();
                setConstructionstageSpinner(constructionid, Singleton.getInstance().constructions_list, stageSpinner);*/
            }

        }
    }


    public static void getFlatPentHousePropertyDetails() {

        String caseid = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");
        if (Singleton.getInstance().indProperty != null) {

            String total_appfloors = editText_totalapartmentfloors.getText().toString().trim();
            String total_flatsperfloor = editText_totalflatsperfloor.getText().toString().trim();
            String flatsituated = editText_flatsituatedinfloor.getText().toString().trim();
            String asperdocs = editText_as_per_docs.getText().toString().trim();

            if (!general.isEmpty(caseid))
                Singleton.getInstance().indProperty.setCaseId(Integer.valueOf(caseid));
            Singleton.getInstance().indProperty.setTotalApartmentFloors(total_appfloors);
            Singleton.getInstance().indProperty.setTotalFlatsPerFloor(total_flatsperfloor);
            Singleton.getInstance().indProperty.setFlatSituatedInFloor(flatsituated);
            /*if (!general.isEmpty(floors))*/
            Singleton.getInstance().indProperty.setNoOfFloors(1);
            Singleton.getInstance().indProperty.setDocumentLandArea(asperdocs);
            //Singleton.getInstance().indProperty.setAvgPercentageCompletion(textview_comp_total.getText().toString().trim());
            if (!general.isEmpty(edittext_general_comp.getText().toString()))
                Singleton.getInstance().indProperty.setAvgPercentageCompletion(edittext_general_comp.getText().toString().trim());


            /* *************
             * Get Spinner single select value
             * *********************/
            int pos = spinnerMeasurement_doc.getSelectedItemPosition();
            if (pos >= 0) {
                int measureUnitId = Singleton.getInstance().measurements_list.get(pos).getMeasureUnitId();
                if (measureUnitId != 0) {
                    Singleton.getInstance().indProperty.setDocumentLandAreaUnit(measureUnitId);
                } else {
                    Singleton.getInstance().indProperty.setDocumentLandAreaUnit(0);
                }
            }

            int posact = spinner_measurement_act.getSelectedItemPosition();
            int UnitId = Singleton.getInstance().measurements_list_flat.get(posact).getMeasureUnitId();
            if (UnitId != 0) {
                Singleton.getInstance().indProperty.setMeasuredLandAreaUnit(UnitId);
                Singleton.getInstance().indPropertyValuation.setCarpetAreaUnit("" + UnitId);
                Singleton.getInstance().indPropertyValuation.setConstructionDLCRateUnit("" + UnitId);
            } else {
                Singleton.getInstance().indProperty.setMeasuredLandAreaUnit(0);
                Singleton.getInstance().indPropertyValuation.setCarpetAreaUnit("");
                Singleton.getInstance().indPropertyValuation.setConstructionDLCRateUnit("");
            }

        }
    }

    public static void getFlatPentHouseGeneralInternalFloors() {

        String caseid = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");

        String generalfloorname = edittext_general_floor_name.getText().toString().trim();
        String flatcomp = edittext_general_comp.getText().toString().trim();
        String flatprogress = edittext_general_progress.getText().toString().trim();
        String flatage = edittext_general_age.getText().toString().trim();
        String flatlife = edittext_general_life.getText().toString().trim();

        if (Singleton.getInstance().indPropertyFloors != null) {

            if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                IndPropertyFloor indPropertyFloor = Singleton.getInstance().indPropertyFloors.get(0);
                if (!general.isEmpty(caseid))
                    indPropertyFloor.setCaseId(Integer.valueOf(caseid));
                indPropertyFloor.setFloorName(generalfloorname);

                if (!general.isEmpty(et_permssible_area.getText().toString())) {
                    indPropertyFloor.setSanctionedFloorArea("" + et_permssible_area.getText().toString());
                } else {
                    indPropertyFloor.setSanctionedFloorArea("");
                }

                if (!general.isEmpty(flatcomp)) {
                    indPropertyFloor.setPercentageCompletion(Integer.valueOf(flatcomp));
                } else {
                    indPropertyFloor.setPercentageCompletion(null);
                }

                indPropertyFloor.setPresentCondition(flatprogress);


                if (!general.isEmpty(flatage)) {
                    indPropertyFloor.setPropertyAge(Integer.valueOf(flatage));
                } else {
                    indPropertyFloor.setPropertyAge(null);
                }
                if (!general.isEmpty(flatlife)) {
                    indPropertyFloor.setResidualLife(Integer.valueOf(flatlife));
                } else {
                    indPropertyFloor.setResidualLife(null);
                }

                indPropertyFloor.setFloorNo(1);

                /* set */
                String charsequence_internal_hall_dinning = internal_hall_dinning.getText().toString();
                if (!general.isEmpty(charsequence_internal_hall_dinning)) {
                    indPropertyFloor.setFlatHallNo(Integer.valueOf(charsequence_internal_hall_dinning));
                } else {
                    indPropertyFloor.setFlatHallNo(0);
                }

                String charsequence_internal_kitchen = internal_kitchen.getText().toString();
                if (!general.isEmpty(charsequence_internal_kitchen)) {
                    indPropertyFloor.setFlatKitchenNo(Integer.valueOf(charsequence_internal_kitchen));
                } else {
                    indPropertyFloor.setFlatKitchenNo(0);
                }

                String charsequence_internal_bedroom = internal_bedroom.getText().toString();
                if (!general.isEmpty(charsequence_internal_bedroom)) {
                    indPropertyFloor.setFlatBedroomNo(Integer.valueOf(charsequence_internal_bedroom));
                } else {
                    indPropertyFloor.setFlatBedroomNo(0);
                }

                String charsequence_internal_bath = internal_bath.getText().toString();
                if (!general.isEmpty(charsequence_internal_bath)) {
                    indPropertyFloor.setFlatBathNo(Integer.valueOf(charsequence_internal_bath));
                } else {
                    indPropertyFloor.setFlatBathNo(0);
                }

                String charsequence_internal_shop_office = internal_shop_office.getText().toString();
                if (!general.isEmpty(charsequence_internal_shop_office)) {
                    indPropertyFloor.setOfficeNo(Integer.valueOf(charsequence_internal_shop_office));
                } else {
                    indPropertyFloor.setOfficeNo(0);
                }

                if (!general.isEmpty(internal_pooja_room.getText().toString())) {
                    indPropertyFloor.setFlatPoojaNo(Integer.valueOf(internal_pooja_room.getText().toString()));
                } else {
                    indPropertyFloor.setFlatPoojaNo(0);
                }

                Singleton.getInstance().indPropertyFloors.set(0, indPropertyFloor);
            } else {

                IndPropertyFloor indPropertyFloor = new IndPropertyFloor();
                if (!general.isEmpty(caseid))
                    indPropertyFloor.setCaseId(Integer.valueOf(caseid));
                indPropertyFloor.setFloorName(generalfloorname);
                if (!general.isEmpty(flatcomp)) {
                    indPropertyFloor.setPercentageCompletion(Integer.valueOf(flatcomp));
                } else {
                    indPropertyFloor.setPercentageCompletion(null);
                }
                indPropertyFloor.setPresentCondition(flatprogress);

                if (!general.isEmpty(flatage)) {
                    indPropertyFloor.setPropertyAge(Integer.valueOf(flatage));
                } else {
                    indPropertyFloor.setPropertyAge(null);
                }

                if (!general.isEmpty(flatlife)) {
                    indPropertyFloor.setResidualLife(Integer.valueOf(flatlife));
                } else {
                    indPropertyFloor.setResidualLife(null);
                }


                indPropertyFloor.setFloorNo(1);


                String charsequence_internal_hall_dinning = internal_hall_dinning.getText().toString();
                if (!general.isEmpty(charsequence_internal_hall_dinning)) {
                    indPropertyFloor.setFlatHallNo(Integer.valueOf(charsequence_internal_hall_dinning));
                } else {
                    indPropertyFloor.setFlatHallNo(0);
                }

                String charsequence_internal_kitchen = internal_kitchen.getText().toString();
                if (!general.isEmpty(charsequence_internal_kitchen)) {
                    indPropertyFloor.setFlatKitchenNo(Integer.valueOf(charsequence_internal_kitchen));
                } else {
                    indPropertyFloor.setFlatKitchenNo(0);
                }

                String charsequence_internal_bedroom = internal_bedroom.getText().toString();
                if (!general.isEmpty(charsequence_internal_bedroom)) {
                    indPropertyFloor.setFlatBedroomNo(Integer.valueOf(charsequence_internal_bedroom));
                } else {
                    indPropertyFloor.setFlatBedroomNo(0);
                }

                String charsequence_internal_bath = internal_bath.getText().toString();
                if (!general.isEmpty(charsequence_internal_bath)) {
                    indPropertyFloor.setFlatBathNo(Integer.valueOf(charsequence_internal_bath));
                } else {
                    indPropertyFloor.setFlatBathNo(0);
                }

                String charsequence_internal_shop_office = internal_shop_office.getText().toString();
                if (!general.isEmpty(charsequence_internal_shop_office)) {
                    indPropertyFloor.setOfficeNo(Integer.valueOf(charsequence_internal_shop_office));
                } else {
                    indPropertyFloor.setOfficeNo(0);
                }

                if (!general.isEmpty(internal_pooja_room.getText().toString())) {
                    indPropertyFloor.setFlatPoojaNo(Integer.valueOf(internal_pooja_room.getText().toString()));
                } else {
                    indPropertyFloor.setFlatPoojaNo(0);
                }

                if (!general.isEmpty(et_permssible_area.getText().toString())) {
                    indPropertyFloor.setSanctionedFloorArea("" + et_permssible_area.getText().toString());
                } else {
                    indPropertyFloor.setSanctionedFloorArea("");
                }


                Singleton.getInstance().indPropertyFloors.add(indPropertyFloor);
            }
        }


        if (Singleton.getInstance().indPropertyValuation != null) {

            String saleablearea = edittext_general_saleablearea.getText().toString().trim();
            String builduparea = edittext_general_builduparea.getText().toString().trim();
            String carpetarea = edittext_general_carpetarea.getText().toString().trim();
            String permissibleArea = et_permssible_area.getText().toString().trim();

            if (!general.isEmpty(caseid))
                Singleton.getInstance().indPropertyValuation.setCaseId(Integer.valueOf(caseid));
            Singleton.getInstance().indPropertyValuation.setSuperBuildUpArea(saleablearea);
            Singleton.getInstance().indPropertyValuation.setBuildUpArea(builduparea);
            Singleton.getInstance().indPropertyValuation.setCarpetArea(carpetarea);
            Singleton.getInstance().indPropertyValuation.setPermissibleArea(permissibleArea);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == spinnerMeasurement_doc || v == stageSpinner) {
            hideSoftKeyboard(spinnerMeasurement_doc);
        }
        return false;
    }

    public void PostPentHouseValues() {
        getFlatPentHousePropertyDetails();
        getFlatPentHouseGeneralInternalFloors();
    }


    private void carpetloading_per(final TextView textView, final String type_is) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.loading_over_builup_popup);

        TextView popuptitle = (TextView) dialog.findViewById(R.id.title);
        popuptitle.setTypeface(general.mediumtypeface());

        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        RadioGroup id_radiogroup_stage = (RadioGroup) dialog.findViewById(R.id.id_radiogroup_stage);
        RadioButton id_radio_select = (RadioButton) dialog.findViewById(R.id.id_radio_select);
        RadioButton id_radio_0 = (RadioButton) dialog.findViewById(R.id.id_radio_0);
        RadioButton id_radio_5 = (RadioButton) dialog.findViewById(R.id.id_radio_5);
        RadioButton id_radio_10 = (RadioButton) dialog.findViewById(R.id.id_radio_10);
        RadioButton id_radio_15 = (RadioButton) dialog.findViewById(R.id.id_radio_15);
        RadioButton id_radio_20 = (RadioButton) dialog.findViewById(R.id.id_radio_20);
        RadioButton id_radio_25 = (RadioButton) dialog.findViewById(R.id.id_radio_25);
        RadioButton id_radio_30 = (RadioButton) dialog.findViewById(R.id.id_radio_30);
        RadioButton id_radio_35 = (RadioButton) dialog.findViewById(R.id.id_radio_35);
        RadioButton id_radio_40 = (RadioButton) dialog.findViewById(R.id.id_radio_40);
        RadioButton id_radio_45 = (RadioButton) dialog.findViewById(R.id.id_radio_45);
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


        id_radio_select.setTypeface(general.mediumtypeface());
        id_radio_0.setTypeface(general.mediumtypeface());
        id_radio_5.setTypeface(general.mediumtypeface());
        id_radio_10.setTypeface(general.mediumtypeface());
        id_radio_15.setTypeface(general.mediumtypeface());
        id_radio_20.setTypeface(general.mediumtypeface());
        id_radio_25.setTypeface(general.mediumtypeface());
        id_radio_30.setTypeface(general.mediumtypeface());
        id_radio_35.setTypeface(general.mediumtypeface());
        id_radio_40.setTypeface(general.mediumtypeface());
        id_radio_45.setTypeface(general.mediumtypeface());
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

        id_radio_95.setVisibility(View.GONE);
        id_radio_100.setVisibility(View.GONE);

        boolean is_empty = true;
        String selected_value = "";


        if (type_is.equalsIgnoreCase("carpet_per")) {
            id_radio_80.setVisibility(View.VISIBLE);
            id_radio_85.setVisibility(View.VISIBLE);
            id_radio_90.setVisibility(View.VISIBLE);
            // Carpet Percentage
            popuptitle.setText(getActivity().getResources().getString(R.string.carpet_area_per));
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage())) {
                is_empty = false;
                selected_value = Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage();
            }
        } else {
            id_radio_80.setVisibility(View.GONE);
            id_radio_85.setVisibility(View.GONE);
            id_radio_90.setVisibility(View.GONE);
            // Loading Factor
            popuptitle.setText(getActivity().getResources().getString(R.string.loading_factor));
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getSaleableLoadingPercentage())) {
                is_empty = false;
                selected_value = Singleton.getInstance().indPropertyValuation.getSaleableLoadingPercentage();
            }
        }


        if (!is_empty) {
            if (selected_value.equalsIgnoreCase("0")) {
                id_radio_0.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("5")) {
                id_radio_5.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("10")) {
                id_radio_10.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("15")) {
                id_radio_15.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("20")) {
                id_radio_20.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("25")) {
                id_radio_25.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("30")) {
                id_radio_30.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("35")) {
                id_radio_35.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("40")) {
                id_radio_40.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("45")) {
                id_radio_45.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("50")) {
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
                if (type_is.equalsIgnoreCase("carpet_per")) {
                    // Carpet Percentage
                    if (str_radiogenearal.equalsIgnoreCase("Select")) {
                        textView.setText(str_radiogenearal);
                        Singleton.getInstance().indPropertyValuation.setCarpetAreaPercentage("");
                        // set and call the carpet value
                        val_carpetloading_per = "";
                        fun_carpet_value();
                        calPerthroughCarpetPer(false);
                    } else {
                        String loadingStr = str_radiogenearal.replace("%", "");
                        textView.setText(loadingStr);
                        Singleton.getInstance().indPropertyValuation.setCarpetAreaPercentage(loadingStr);
                        // set and call the carpet value
                        val_carpetloading_per = loadingStr;
                        fun_carpet_value();
                        calPerthroughCarpetPer(true);
                    }
                } else {
                    // Loading Factor
                    if (str_radiogenearal.equalsIgnoreCase("Select")) {
                        textView.setText(str_radiogenearal);
                        Singleton.getInstance().indPropertyValuation.setSaleableLoadingPercentage("");
                        // set and call the carpet value
                        val_textview_loading_factor_per = "";
                    } else {
                        String loadingStr = str_radiogenearal.replace("%", "");
                        textView.setText(loadingStr);
                        Singleton.getInstance().indPropertyValuation.setSaleableLoadingPercentage(loadingStr);
                        // set and call the carpet value
                        val_textview_loading_factor_per = loadingStr;
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

    private void fun_carpet_value() {

    }

    private void clickedSelectSpinner(int position) {
        Log.e("Fragment", String.valueOf(position));
        if (position == 1) {
            calPercentage(edittext_general_builduparea);
        } else if (position == 2) {
            calPercentage(edittext_general_saleablearea);
        } else if (position == 3) {
            calPercentage(et_permssible_area);
        }
    }

    private void calPercentage(EditText editText) {

        String carpetAreaPer = Singleton.getInstance().indPropertyValuation.getCarpetAreaPercentage();
        if (carpetAreaPer != null && !carpetAreaPer.isEmpty()
                && editText.getText().toString() != null &&
                !editText.getText().toString().isEmpty()
        ) {
            double amount = Double.parseDouble(editText.getText().toString());
            double res = (amount / 100.0f);
            double resInPer = res * Integer.parseInt(carpetAreaPer);
            // edittext_general_carpetarea.setText(String.valueOf(resInPer));
            edittext_general_carpetarea.setText(new DecimalFormat("##.##").format(resInPer));
        }
    }

    private void calPerthroughCarpetPer(boolean b) {
        if (b) {
            String selectedCarpetAreaTypeId = Singleton.getInstance().indPropertyValuation.getSelectedCarpetAreaTypeId();
            if (selectedCarpetAreaTypeId != null && !selectedCarpetAreaTypeId.isEmpty() && selectedCarpetAreaTypeId != "0"
                    && val_carpetloading_per != null &&
                    !val_carpetloading_per.isEmpty()
            ) {
                String areaValue = "";

                if (selectedCarpetAreaTypeId.equalsIgnoreCase("1")) {
                    areaValue = edittext_general_builduparea.getText().toString();
                } else if (selectedCarpetAreaTypeId.equalsIgnoreCase("2")) {
                    areaValue = edittext_general_saleablearea.getText().toString();
                } else if (selectedCarpetAreaTypeId.equalsIgnoreCase("3")) {
                    // calPercentage(et_permssible_area);
                    areaValue = et_permssible_area.getText().toString();
                }

                if (!areaValue.isEmpty()) {
                    if (val_carpetloading_per != null && !val_carpetloading_per.equalsIgnoreCase("0")){
                        double amount = Double.parseDouble(areaValue);
                        double res = (amount / 100.0f);
                        double resInPer = res * Integer.parseInt(val_carpetloading_per);
                        edittext_general_carpetarea.setText(new DecimalFormat("##.##").format(resInPer));
                    }else {
                        edittext_general_carpetarea.setText(areaValue);
                    }

                }
            }


        } else {
            edittext_general_carpetarea.setText("");
        }

    }


    // TODO - calc function
    public void Calculation_Popup_New(final int type_is) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calculator_popup_new);
        // dialog.setTitle("Calculator");

        isSmallBracketOpen = false;
        developedCounter = 0;


        /***** Assigning Variable *****/
        btnClear = (TextView) dialog.findViewById(R.id.btn_delete);
        tvProcessor = (TextView) dialog.findViewById(R.id.tv_process);
        tvResult = (TextView) dialog.findViewById(R.id.tv_result);

        tvProcessor.setText("");
        tvResult.setText("");


        btnOne = (TextView) dialog.findViewById(R.id.btn_one);
        btnTwo = (TextView) dialog.findViewById(R.id.btn_two);
        btnThree = (TextView) dialog.findViewById(R.id.btn_three);
        btnFour = (TextView) dialog.findViewById(R.id.btn_four);
        btnFive = (TextView) dialog.findViewById(R.id.btn_five);
        btnSix = (TextView) dialog.findViewById(R.id.btn_six);
        btnSeven = (TextView) dialog.findViewById(R.id.btn_seven);
        btnEight = (TextView) dialog.findViewById(R.id.btn_eight);
        btnNine = (TextView) dialog.findViewById(R.id.btn_nine);
        btnZero = (TextView) dialog.findViewById(R.id.btn_zero);


        btnMultiply = (TextView) dialog.findViewById(R.id.btn_multiply);
        btnMinus = (TextView) dialog.findViewById(R.id.btn_minus);
        btnPlus = (TextView) dialog.findViewById(R.id.btn_plus);
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
        btnOne.setTypeface(general.RobotoLightTypeface());
        btnTwo.setTypeface(general.RobotoLightTypeface());
        btnThree.setTypeface(general.RobotoLightTypeface());
        btnFour.setTypeface(general.RobotoLightTypeface());
        btnFive.setTypeface(general.RobotoLightTypeface());
        btnSix.setTypeface(general.RobotoLightTypeface());
        btnSeven.setTypeface(general.RobotoLightTypeface());
        btnEight.setTypeface(general.RobotoLightTypeface());
        btnNine.setTypeface(general.RobotoLightTypeface());
        btnZero.setTypeface(general.RobotoLightTypeface());
        btnMultiply.setTypeface(general.regulartypeface());
        btnMinus.setTypeface(general.regulartypeface());
        btnPlus.setTypeface(general.regulartypeface());
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
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "1");
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                processor = tvProcessor.getText().toString();
                tvProcessor.setText(processor + "2");
            }
        });
        btnThree.setOnClickListener(new View.OnClickListener() {
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
        btnZero.setOnClickListener(new View.OnClickListener() {
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
        btnPlus.setOnClickListener(new View.OnClickListener() {
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

                org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
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
                            // BuildupArea
                            edittext_general_builduparea.setText(my_val_str);
                            setEdittextBuildupArea(my_val_str);
                            checkSpinnerAreaType(my_val_str, getResources().getString(R.string.builtup));
                            fun_carpet_value();
                        } else if (type_is == 2) {
                            // SaleableArea
                            edittext_general_saleablearea.setText(my_val_str);
                            setEdittextSaleableArea(my_val_str);
                            checkSpinnerAreaType(my_val_str, getResources().getString(R.string.Saleable));
                            fun_carpet_value();
                        } else if (type_is == 3) {
                            // CarpetArea
                            edittext_general_carpetarea.setText(my_val_str);
                            setEdittextCarpetArea(my_val_str);
                            checkSpinnerAreaType(my_val_str, getResources().getString(R.string.carpet));
                        } else if (type_is == 4) {
                            setEdittextPermissibleArea(my_val_str);
                            checkSpinnerAreaType(my_val_str, getResources().getString(R.string.permissible_area));
                            et_permssible_area.setText(my_val_str);
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





    private void getDetails() {
        general.showloading(getActivity());
        String url = general.ApiBaseUrl() + SettingsUtils.GetPropertyDetails;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setCaseId(SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, ""));
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

    private void showPopup(boolean successful, String response, int responseCode) {
        general.hideloading();
        if (successful && (responseCode == 200 || responseCode == 201)) {
            comparePopup(new Gson().fromJson(response, GetPropertyDetailsModel.class));
        } else {
            General.customToast(getResources().getString(R.string.something_wrong), getActivity());
        }
    }

    private void comparePopup(GetPropertyDetailsModel fromJson) {
        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
                    requestData.setCaseId(SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, ""));
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

                                    Log.e("comparePopup", "onTaskComplete: " + new Gson().toJson(requestData.getResponse()));

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

    private void getSubSequentRevaluation(){
        if (!general.isEmpty(etEstimateCost.getText().toString())){
            Singleton.getInstance().indPropertyValuation.setEstimatedCostofConstructiononCompletion(etEstimateCost.getText().toString());
        }
        if (!general.isEmpty(etLoanAmount.getText().toString())){
            Singleton.getInstance().indPropertyValuation.setLoanAmountInclusiveInsuranceOtherAmount(etLoanAmount.getText().toString());
        }
        if (!general.isEmpty(etOwnContribution.getText().toString())){
            Singleton.getInstance().indPropertyValuation.setOwnContributionAmount(etOwnContribution.getText().toString());
        }
        if (!general.isEmpty(etRecommendationPercentage.getText().toString())){
            Singleton.getInstance().indPropertyValuation.setRecommendationPercentage(etRecommendationPercentage.getText().toString());
        }
        if (!general.isEmpty(etAmountDisbursement.getText().toString())){
            Singleton.getInstance().indPropertyValuation.setAmounttobeDisbursement(etAmountDisbursement.getText().toString());
        }
        if (!general.isEmpty(etAmountSpend.getText().toString())){
            Singleton.getInstance().indPropertyValuation.setAmountSpent(etAmountSpend.getText().toString());
        }
        if (!general.isEmpty(etAverageConstruction.getText().toString())){
            Singleton.getInstance().indPropertyValuation.setAverageConstructionPercentage(etAverageConstruction.getText().toString());
        }
    }

    private void subsequentRevaluation() {

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getEstimatedCostofConstructiononCompletion())){
            etEstimateCost.setText(Singleton.getInstance().indPropertyValuation.getEstimatedCostofConstructiononCompletion());
        }
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getLoanAmountInclusiveInsuranceOtherAmount())){
            etLoanAmount.setText(Singleton.getInstance().indPropertyValuation.getLoanAmountInclusiveInsuranceOtherAmount());
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getAverageConstructionPercentage())){
         //   etAverageConstruction.setText(Singleton.getInstance().indPropertyValuation.getAverageConstructionPercentage());
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getRecommendationPercentage())){
        //    etRecommendationPercentage.setText(Singleton.getInstance().indPropertyValuation.getRecommendationPercentage());
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getAmounttobeDisbursement())){
            etAmountDisbursement.setText(Singleton.getInstance().indPropertyValuation.getAmounttobeDisbursement());
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getAmountSpent())){
            etAmountSpend.setText(Singleton.getInstance().indPropertyValuation.getAmountSpent());
        }

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getOwnContributionAmount())){
            etOwnContribution.setText(Singleton.getInstance().indPropertyValuation.getOwnContributionAmount());
        }

        /*etEstimateCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                ownContributionCal();
                amountSpendCalculation();
            }
        });
*/
        /*etLoanAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                ownContributionCal();
            }
        });*/

        /*etAverageConstruction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                amountSpendCalculation();
            }
        });*/

    }

    private void ownContributionCal() {
        if (etEstimateCost.getText().toString().length() > 0
                && etLoanAmount.getText().toString().length() > 0) {
            int est = Integer.parseInt(etEstimateCost.getText().toString());
            int loanAmt = Integer.parseInt(etLoanAmount.getText().toString());
            etOwnContribution.setText((est - loanAmt) + "");
        }
    }

    private void amountSpendCalculation() {
      /*  if (etEstimateCost.getText().toString().length() > 0 &&
                etAverageConstruction.getText().toString().length() > 0) {

            int est = Integer.parseInt(etEstimateCost.getText().toString());
            int avgCons = Integer.parseInt(etAverageConstruction.getText().toString());
            if (avgCons>100){
                General.customToast("Average construction percentage must be less than or equal to 100",getActivity());
            }else {
                etAmountSpend.setText(((est / 100) * avgCons) + "");
            }
        }*/
    }


    private void setPropertyType() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            property_type = bundle.getString("property_type");
        }

        if (property_type.equalsIgnoreCase("flat")) {
          //  linear_penthouse_terrace_row.setVisibility(View.GONE);
        } else {
          //  linear_penthouse_terrace_row.setVisibility(View.VISIBLE);
        }
    }

    private void DisplayValuation() {
        DisplayAreaTypeValues();
        DisplayMarketRateValues();
        DisplaySpinnerValues();
    }

    private void DisplayAreaTypeValues() {
        if (Singleton.getInstance().indPropertyValuation != null) {
            /******Set Edittext area type values in Valuation******/
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getCarpetArea())) {
                setEdittextCarpetArea(Singleton.getInstance().indPropertyValuation.getCarpetArea());
                Singleton.getInstance().areaType.add(getResources().getString(R.string.carpet));

            }
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getBuildUpArea())) {
                setEdittextBuildupArea(Singleton.getInstance().indPropertyValuation.getBuildUpArea());
                Singleton.getInstance().areaType.add(getResources().getString(R.string.builtup));

            }
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getSuperBuildUpArea())) {
                setEdittextSaleableArea(Singleton.getInstance().indPropertyValuation.getSuperBuildUpArea());
                Singleton.getInstance().areaType.add(getResources().getString(R.string.Saleable));
            }

            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getPermissibleArea())) {
                setEdittextPermissibleArea(Singleton.getInstance().indPropertyValuation.getPermissibleArea());
                Singleton.getInstance().areaType.add(getResources().getString(R.string.permissible_area));
            }

            /*****Set spinner Area type as per the id*****/
            String carpetAreaid = Singleton.getInstance().indPropertyValuation.getCarpetAreaTypeId();
            if (!general.isEmpty(carpetAreaid)) {
                String area_type = "";
                int carpetID = Integer.valueOf(carpetAreaid);
                if (carpetID < Singleton.getInstance().areaType.size()) {
                    if (carpetID == 1) {
                        area_type = "Carpet";
                    } else if (carpetID == 2) {
                        area_type = "Built up";
                    } else if (carpetID == 3) {
                        area_type = "Saleable";
                    } else if (carpetID == 4) {
                        area_type = "Permissible";
                    } else {
                        area_type = "Select";
                    }
                    AreaTypeSpinner(Singleton.getInstance().areaType, area_type);
                }
            }

            /***Set Measurement unit value****/
            Measurement_id = Singleton.getInstance().indPropertyValuation.getCarpetAreaUnit();
            if (!general.isEmpty(Measurement_id)) {
                int measure = Integer.valueOf(Measurement_id);
                if (measure == 1 && measure < Singleton.getInstance().measurements_list_flat.size()) {
                    edittext_measurementunit.setText("" + Singleton.getInstance().measurements_list_flat.get(1).getName());

                } else {
                    edittext_measurementunit.setText("");
                }

            }
        }
    }

    private void DisplayMarketRateValues() {
        if (Singleton.getInstance().indPropertyValuation != null) {

            String areatype_rate = Singleton.getInstance().indPropertyValuation.getConstructionRate();
            String insurance_rate = Singleton.getInstance().indPropertyValuation.getInsuredConstructionRate();
            String govern_dlc_rate = Singleton.getInstance().indPropertyValuation.getConstructionDLCRate();
            String terrace_area = Singleton.getInstance().indPropertyValuation.getTerraceArea();
            String terrace_rate = Singleton.getInstance().indPropertyValuation.getTerraceRate();
            //   String areatype_marketval = Singleton.getInstance().indPropertyValuation.getTotalConstructionValue();
            String insurance_value = Singleton.getInstance().indPropertyValuation.getInsuranceValue();
            String dlc_marketval = Singleton.getInstance().indPropertyValuation.getConstructionDLCValue();
            String terrace_val = Singleton.getInstance().indPropertyValuation.getTerraceValue();
            String total_propertyval = Singleton.getInstance().indPropertyValuation.getTotalPropertyValue();

            String realized_total = Singleton.getInstance().indPropertyValuation.getRealizationValue();
            String distress_total = Singleton.getInstance().indPropertyValuation.getDistressValue();
            String aspercomp_percent = Singleton.getInstance().indPropertyValuation.getCompletionPercentage();
            String aspercomp_total = Singleton.getInstance().indPropertyValuation.getTotalValueAtCompletion();
            String proposed_val = Singleton.getInstance().indPropertyValuation.getProposedValuation();
            String proposed_remarks = Singleton.getInstance().indPropertyValuation.getProposedValuationComments();

            if (!general.isEmpty(areatype_rate)) {
                edittext_type_rate.setText(areatype_rate);
            }
            if (!general.isEmpty(insurance_rate)) {
                edittext_insurance_rate.setText(insurance_rate);
            }
            if (!general.isEmpty(govern_dlc_rate)) {
                edittext_governmentvalue_rate.setText(govern_dlc_rate);
            }
            if (!general.isEmpty(insurance_value)) {
                textview_insurance_marketvalue.setText("" + general.DecimalFormattedCommaString(insurance_value));
                //textview_insurance_marketvalue.setText(insurance_value);
            }
            if (!general.isEmpty(dlc_marketval)) {
                textview_governmentvalue_marketvalue.setText("" + general.DecimalFormattedCommaString(dlc_marketval));
                //  textview_governmentvalue_marketvalue.setText(dlc_marketval);
            }

            if (property_type.equalsIgnoreCase("penthouse")) {
                if (!general.isEmpty(terrace_area)) {
                  //  edittext_terrace_area.setText(terrace_area);
                }
                if (!general.isEmpty(terrace_rate)) {
                    edittext_terrace_rate.setText(terrace_rate);
                }
                if (!general.isEmpty(terrace_val)) {
                    textview_terrace_marketvalue.setText("" + general.DecimalFormattedCommaString(terrace_val));
                    // textview_terrace_marketvalue.setText(terrace_val);
                }
            }

            if (!general.isEmpty(total_propertyval)) {
                textview_totalpropertyvalue_result.setText("" + general.DecimalFormattedCommaString(String.valueOf(total_propertyval)));
/*
                textview_totalpropertyvalue_result.setText(total_propertyval);*/
            }
            if (!general.isEmpty(realized_total)) {
                edittext_realizable_value_total.setText("" + general.DecimalFormattedCommaString(realized_total));
                // edittext_realizable_value_total.setText(realized_total);
            }
            if (!general.isEmpty(distress_total)) {
                edittext_distress_total.setText("" + general.DecimalFormattedCommaString(distress_total));
                // edittext_distress_total.setText(distress_total);
            }
            if (!general.isEmpty(aspercomp_percent)) {
                editText_AsPerCompletion.setText(aspercomp_percent);
            }
            if (!general.isEmpty(aspercomp_total)) {
                textview_aspercompletion_result.setText("" + general.DecimalFormattedCommaString(aspercomp_total));
                // textview_aspercompletion_result.setText(aspercomp_total);
            }
            if (!general.isEmpty(proposed_val)) {
                edittext_proposedvaluation_result.setText(proposed_val);
            }
            if (!general.isEmpty(proposed_remarks)) {
                editText_remarks.setText(proposed_remarks);
            }
        }
    }

    private void DisplaySpinnerValues() {
        if (Singleton.getInstance().indPropertyValuation != null) {
            /******Set Spinner type values in Valuation******/

            String loadingBuilup = Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage();
            String realizable_val = Singleton.getInstance().indPropertyValuation.getRealizationPercentage();
            String distress_val = Singleton.getInstance().indPropertyValuation.getDistressPercentage();

            /*  *//*****Set Loading Over Build up spinner******//*
            if (!general.isEmpty(loadingBuilup)) {
                loadingBuilup = general.NumOnly(loadingBuilup);
                for (int i = 0; i < loadingbuildupList.size(); i++) {
                    String loadingBuildupStr = loadingbuildupList.get(i).toString().replace("%", "");
                    if (loadingBuildupStr.equalsIgnoreCase(loadingBuilup)) {
                        spinnerLoadingOverBuildup.setSelection(i);
                    }
                }
            }*/

            /*****Set Realizable spinner******/
            /*if (!general.isEmpty(realizable_val)) {
                    realizable_val = general.NumOnly(realizable_val);
                    for (int i = 0; i < realizableList.size(); i++) {
                        String realizableStr="";
                        if(realizableList.get(i).toString().contains("%")) {
                            realizableStr = realizableList.get(i).toString().replace("%", "");
                        }
                        if (realizableStr.equalsIgnoreCase(realizable_val)) {
                            spinner_realizable.setSelection(i);
                        }
                        else if(realizable_val.equalsIgnoreCase("101")) {
                            spinner_realizable.setSelection(12);
                        }

                }
            }*/

            /*****Set Distress spinner******/
            /*if (!general.isEmpty(distress_val)) {
                distress_val = general.NumOnly(distress_val);
                for (int i = 0; i < distressList.size(); i++) {
                    String distressStr = "";
                    if (distressList.get(i).toString().contains("%")) {
                        distressStr = distressList.get(i).toString().replace("%", "");
                    }
                    if (distressStr.equalsIgnoreCase(distress_val)) {
                        spinner_distress.setSelection(i);
                    } else if (distress_val.equalsIgnoreCase("101")) {
                        spinner_distress.setSelection(12);
                    }

                }
            }*/
        }
    }

    private void initValues(View view) {
        mContext = this.getContext();

        edittext_type_area = (EditText) view.findViewById(R.id.edittext_type_area);
        edittext_type_rate = (EditText) view.findViewById(R.id.edittext_type_rate);
       // edittext_terrace_area = (EditText) view.findViewById(R.id.edittext_terrace_area);
        edittext_terrace_rate = (EditText) view.findViewById(R.id.edittext_terrace_rate);
        edittext_insurance_area = (EditText) view.findViewById(R.id.edittext_insurance_area);
        edittext_insurance_rate = (EditText) view.findViewById(R.id.edittext_insurance_rate);
        edittext_governmentvalue_area = (EditText) view.findViewById(R.id.edittext_governmentvalue_area);
        edittext_governmentvalue_rate = (EditText) view.findViewById(R.id.edittext_governmentvalue_rate);
        editText_AsPerCompletion = (EditText) view.findViewById(R.id.editText_AsPerCompletion);
        edittext_proposedvaluation_result = (EditText) view.findViewById(R.id.etProposedValuation);
        editText_remarks = (EditText) view.findViewById(R.id.editText_remarks);
        edittext_carpet_area = (EditText) view.findViewById(R.id.edittext_carpet_area);
        edittext_builtup_area = (EditText) view.findViewById(R.id.edittext_builtup_area);
        edittext_Saleable_area = (EditText) view.findViewById(R.id.edittext_Saleable_area);
       // edittext_permissible_area = (EditText) view.findViewById(R.id.edittext_permissible_area);
        edittext_measurementunit = (EditText) view.findViewById(R.id.edittext_measurementunit);
        edittext_realizable_value_total = (EditText) view.findViewById(R.id.edittext_realizable_value_total);
        edittext_distress_total = (EditText) view.findViewById(R.id.edittext_distress_total);

        textview_type_of_area = (TextView) view.findViewById(R.id.textview_type_of_area);
        textview_aspercompletion_result = (TextView) view.findViewById(R.id.textview_aspercompletion_result);
        textview_totalpropertyvalue_result = (TextView) view.findViewById(R.id.textview_totalpropertyvalue_result);
        textview_type_marketvalue = (TextView) view.findViewById(R.id.textview_type_marketvalue);
        textview_terrace_marketvalue = (TextView) view.findViewById(R.id.textview_terrace_marketvalue);
        textview_total_marketval = (TextView) view.findViewById(R.id.textview_total_marketval);
        textview_insurance_marketvalue = (TextView) view.findViewById(R.id.textview_insurance_marketvalue);
        textview_governmentvalue_marketvalue = (TextView) view.findViewById(R.id.textview_governmentvalue_marketvalue);
        textview_totalpropertyvalue = (TextView) view.findViewById(R.id.textview_totalpropertyvalue);

        // spinnerLoadingOverBuildup = (Spinner) view.findViewById(R.id.spinnerLoadingOverBuildup);
        spinnerAreaType = (Spinner) view.findViewById(R.id.spinnerAreaType);
        spinner_realizable = (TextView) view.findViewById(R.id.spinner_realizable);
        spinner_distress = (TextView) view.findViewById(R.id.spinner_distress);
        spinnerLoadingOverBuildup = (TextView) view.findViewById(R.id.spinnerLoadingOverBuildup);

        etEstimateCost = view.findViewById(R.id.etEstimateCost);
        etLoanAmount = view.findViewById(R.id.etLoanAmount);
        etOwnContribution = view.findViewById(R.id.etOwnContribution);
       // etAverageConstruction = view.findViewById(R.id.etAverageConstruction);
       // etRecommendationPercentage = view.findViewById(R.id.etRecommendationPercentage);
        etAmountSpend = view.findViewById(R.id.etAmountSpend);
        etAmountDisbursement = view.findViewById(R.id.etAmountDisbursement);

        edittext_carpet_area.setFocusable(false);
        edittext_builtup_area.setFocusable(false);
        edittext_Saleable_area.setFocusable(false);
       // edittext_permissible_area.setFocusable(false);
        edittext_type_area.setFocusable(false);
        edittext_insurance_area.setFocusable(false);
        edittext_governmentvalue_area.setFocusable(false);
        edittext_realizable_value_total.setFocusable(false);
        edittext_distress_total.setFocusable(false);
        edittext_measurementunit.setFocusable(false);


        /* *//***Clear Area type spinner***//*
        Singleton.getInstance().areaType.clear();
        Singleton.getInstance().areaType.add("Select");*/


        //  limit the 2 char after the decimal point
        edittext_proposedvaluation_result.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(15,2)});


       /* etEstimateCost = view.findViewById(R.id.etEstimateCost);
        etLoanAmount = view.findViewById(R.id.etLoanAmount);
        etOwnContribution = view.findViewById(R.id.etOwnContribution);
        etAverageConstruction = view.findViewById(R.id.etAverageConstruction);
        etRecommendationPercentage = view.findViewById(R.id.etRecommendationPercentage);
        etAmountSpend = view.findViewById(R.id.etAmountSpend);
        etAmountDisbursement = view.findViewById(R.id.etAmountDisbursement);*/

    }


    /*****
     * Initiate Edittext values
     * ******/
    public void setEdittextCarpetArea(String setArea) {
        if (edittext_carpet_area != null) {
            edittext_carpet_area.setText(setArea);
            edittext_type_area.setText(setArea);
            edittext_insurance_area.setText(setArea);
            edittext_governmentvalue_area.setText(setArea);
        }
    }

    public void setEdittextBuildupArea(String setArea) {
        if (edittext_builtup_area != null) {
            edittext_builtup_area.setText(setArea);
            edittext_type_area.setText(setArea);
            edittext_insurance_area.setText(setArea);
            edittext_governmentvalue_area.setText(setArea);
        }
    }

    public void setEdittextSaleableArea(String setArea) {
        if (edittext_Saleable_area != null) {
            edittext_Saleable_area.setText(setArea);
            edittext_type_area.setText(setArea);
            edittext_insurance_area.setText(setArea);
            edittext_governmentvalue_area.setText(setArea);
        }
    }

    public void setEdittextPermissibleArea(String setArea) {
      /*  if (edittext_permissible_area != null) {
            edittext_permissible_area.setText(setArea);
            edittext_type_area.setText(setArea);
            edittext_insurance_area.setText(setArea);
            edittext_governmentvalue_area.setText(setArea);
        }*/

        edittext_type_area.setText(setArea);
        edittext_insurance_area.setText(setArea);
        edittext_governmentvalue_area.setText(setArea);
    }

    public void setEdittextAsPerCompletion(final String setValue) {

        mJumpRunnable = new Runnable() {
            public void run() {
                editText_AsPerCompletion.setText(setValue);
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mJumpRunnable, DELAY_TIME);
    }

    /*******
     * Initiate Spinner values
     * *******/

    public void AreaTypeSpinner(final ArrayList<String> areaType, final String area_type) {
        if (mContext != null) {
            adapterAreaType = new ArrayAdapter<>(mContext,
                    R.layout.row_spinner_item, areaType);
            adapterAreaType.setDropDownViewResource(R.layout.row_spinner_item_popup);
            spinnerAreaType.setAdapter(adapterAreaType);
            spinnerAreaType.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(spinnerAreaType);
                    return false;
                }
            });

            if (area_type.equalsIgnoreCase("Carpet")) {
                for (int i = 0; i < areaType.size(); i++) {
                    if (area_type.equalsIgnoreCase(areaType.get(i))) {
                        spinnerAreaType.setSelection(i);
                        areaTypeID = "1";
                    }
                }

            } else if (area_type.equalsIgnoreCase("Built up")) {
                for (int i = 0; i < areaType.size(); i++) {
                    String area = areaType.get(i);
                    if (area_type.equalsIgnoreCase(areaType.get(i))) {
                        spinnerAreaType.setSelection(i);
                        areaTypeID = "2";
                    }
                }
            /*if (areaType.size() >= 3)
                spinnerAreaType.setSelection(2);*/
            } else if (area_type.equalsIgnoreCase("Saleable")) {
                for (int i = 0; i < areaType.size(); i++) {
                    if (area_type.equalsIgnoreCase(areaType.get(i))) {
                        spinnerAreaType.setSelection(i);
                        areaTypeID = "3";
                    }
                }
            } else if (area_type.equalsIgnoreCase("Permissible")) {
                for (int i = 0; i < areaType.size(); i++) {
                    if (area_type.equalsIgnoreCase(areaType.get(i))) {
                        spinnerAreaType.setSelection(i);
                        areaTypeID = "4";
                    }
                }
            }


            else {
                spinnerAreaType.setSelection(0);
                areaTypeID = "0";
            }

            spinnerAreaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("spinner_areatype", "::: " + areaType.get(position));

                    if (position == 0) {
                        System.err.println("Position zero on Spinner area type");
                        areaTypeID = "0";
                    } else {
                        if (areaType.get(position).equalsIgnoreCase("Carpet")) {
                            // For DB
                            Singleton.getInstance().indPropertyValuation.setSelectedConstructionArea(edittext_carpet_area.getText().toString());
                            setEdittextCarpetArea(edittext_carpet_area.getText().toString().trim());
                            textview_type_of_area.setText(areaType.get(position));

                            areaTypeID = "1";
                            if (property_type.equalsIgnoreCase("penthouse")) {
                                PentHouseSpinnerCalculation();
                            } else {
                                FlatSpinnerCalculation();
                            }
                        } else if (areaType.get(position).equalsIgnoreCase("Built up")) {
                            // For DB
                            Singleton.getInstance().indPropertyValuation.setSelectedConstructionArea(edittext_builtup_area.getText().toString());
                            setEdittextBuildupArea(edittext_builtup_area.getText().toString().trim());
                            textview_type_of_area.setText(areaType.get(position));

                            areaTypeID = "2";
                            if (property_type.equalsIgnoreCase("penthouse")) {
                                PentHouseSpinnerCalculation();
                            } else {
                                FlatSpinnerCalculation();
                            }
                        } else if (areaType.get(position).equalsIgnoreCase("Saleable")) {
                            // For DB
                            Singleton.getInstance().indPropertyValuation.setSelectedConstructionArea(edittext_Saleable_area.getText().toString());
                            setEdittextSaleableArea(edittext_Saleable_area.getText().toString().trim());
                            textview_type_of_area.setText(areaType.get(position));

                            areaTypeID = "3";
                            if (property_type.equalsIgnoreCase("penthouse")) {
                                PentHouseSpinnerCalculation();
                            } else {
                                FlatSpinnerCalculation();
                            }
                        } else if(areaType.get(position).equalsIgnoreCase("Permissible")){
                           // setEdittextPermissibleArea(edittext_permissible_area.getText().toString().trim());
                            areaTypeID = "4";
                            textview_type_of_area.setText(areaType.get(position));
                            if (property_type.equalsIgnoreCase("penthouse")) {
                                PentHouseSpinnerCalculation();
                            } else {
                                FlatSpinnerCalculation();
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }


    public void PentHouseSpinnerCalculation() {
        AreaTypeMarketCalculation(edittext_type_area.getText().toString().trim(), edittext_type_rate.getText().toString().trim());
        //TerraceMarketCalculation(edittext_terrace_area.getText().toString().trim(), edittext_terrace_rate.getText().toString().trim());
        InsuranceMarketCalculation(edittext_insurance_area.getText().toString().trim(), edittext_insurance_rate.getText().toString().trim());
        GovernmentMarketCalculation(edittext_governmentvalue_area.getText().toString().trim(), edittext_governmentvalue_rate.getText().toString().trim());
        AsperCompletionCalculation(editText_AsPerCompletion.getText().toString().trim(), textview_totalpropertyvalue_result.getText().toString().trim());
        setRealizableDistressCalculationVal();
    }

    public void FlatSpinnerCalculation() {
        FlatAreaTypeMarketCalculation(edittext_type_area.getText().toString().trim(), edittext_type_rate.getText().toString().trim());
        InsuranceMarketCalculation(edittext_insurance_area.getText().toString().trim(), edittext_insurance_rate.getText().toString().trim());
        GovernmentMarketCalculation(edittext_governmentvalue_area.getText().toString().trim(), edittext_governmentvalue_rate.getText().toString().trim());
        AsperCompletionCalculation(editText_AsPerCompletion.getText().toString().trim(), textview_totalpropertyvalue_result.getText().toString().trim());
        setRealizableDistressCalculationVal();
    }

    public void PentHouseFlatCalculation() {

        //Type area rate
        edittext_type_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String area_rate = charSequence.toString();
                if (property_type.equalsIgnoreCase("penthouse")) {
                    AreaTypeMarketCalculation(edittext_type_area.getText().toString(), area_rate);
                    AsperCompletionCalculation(editText_AsPerCompletion.getText().toString().trim(), textview_totalpropertyvalue_result.getText().toString().trim());
                    setRealizableDistressCalculationVal();
                } else {
                    FlatAreaTypeMarketCalculation(edittext_type_area.getText().toString(), area_rate);
                    AsperCompletionCalculation(editText_AsPerCompletion.getText().toString().trim(), textview_totalpropertyvalue_result.getText().toString().trim());
                    setRealizableDistressCalculationVal();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //Terrace
        /*edittext_terrace_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String terrace_area = charSequence.toString();
                if (property_type.equalsIgnoreCase("penthouse")) {
                    TerraceMarketCalculation(terrace_area, edittext_terrace_rate.getText().toString());
                    AsperCompletionCalculation(editText_AsPerCompletion.getText().toString().trim(), textview_totalpropertyvalue_result.getText().toString().trim());
                    setRealizableDistressCalculationVal();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/


        //Terrace
      /*  edittext_terrace_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String terrace_rate = charSequence.toString();
                if (property_type.equalsIgnoreCase("penthouse")) {
                   // TerraceMarketCalculation(edittext_terrace_area.getText().toString(), terrace_rate);
                    AsperCompletionCalculation(editText_AsPerCompletion.getText().toString().trim(), textview_totalpropertyvalue_result.getText().toString().trim());
                    setRealizableDistressCalculationVal();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/
        //Insurance rate
        edittext_insurance_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String insurance_rate = charSequence.toString();
                InsuranceMarketCalculation(edittext_insurance_area.getText().toString(), insurance_rate);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Government rate
        edittext_governmentvalue_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String government_rate = charSequence.toString();
                GovernmentMarketCalculation(edittext_governmentvalue_area.getText().toString(), government_rate);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // as per completion
        editText_AsPerCompletion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String aspercomple_rate = charSequence.toString();
                AsperCompletionCalculation(aspercomple_rate, textview_totalpropertyvalue_result.getText().toString().trim());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void AreaTypeMarketCalculation(String area_val, String area_rate) {
        if ((!general.isEmpty(area_val)) && (!general.isEmpty(area_rate))) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(area_val)) * (general.convertTofloat(area_rate));
            sum_total = general.convertToRoundoff(sumtotal);
            // textview_type_marketvalue.setText("" + sum_total);
            textview_type_marketvalue.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));

        } else {
            textview_type_marketvalue.setText("" + 0);
        }

        /*****Total calculation*****/
        String area_total = textview_type_marketvalue.getText().toString().trim();
        String terrace_total = textview_terrace_marketvalue.getText().toString().trim();

        if (!general.isEmpty(area_total) && !(general.isEmpty(terrace_total))) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(area_total)) + (general.convertTofloat(terrace_total));
            sum_total = general.convertToRoundoff(sumtotal);

            textview_total_marketval.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));
            textview_totalpropertyvalue_result.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));

           /* textview_total_marketval.setText("" + sum_total);
            textview_totalpropertyvalue_result.setText("" + sum_total);*/
        } else if (!general.isEmpty(area_total)) {

            textview_total_marketval.setText("" + general.DecimalFormattedCommaString(String.valueOf(area_total)));
            textview_totalpropertyvalue_result.setText("" + general.DecimalFormattedCommaString(String.valueOf(area_total)));

       /*     textview_total_marketval.setText("" + area_total);
            textview_totalpropertyvalue_result.setText("" + area_total);*/
        } else if (!general.isEmpty(terrace_total)) {

            textview_total_marketval.setText("" + general.DecimalFormattedCommaString(String.valueOf(terrace_total)));
            textview_totalpropertyvalue_result.setText("" + general.DecimalFormattedCommaString(String.valueOf(terrace_total)));


           /* textview_total_marketval.setText("" + terrace_total);
            textview_totalpropertyvalue_result.setText("" + terrace_total);*/
        }
    }

    public void FlatAreaTypeMarketCalculation(String area_val, String area_rate) {
        if (!general.isEmpty(area_val) && !general.isEmpty(area_rate)) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(area_val)) * (general.convertTofloat(area_rate));
            sum_total = general.convertToRoundoff(sumtotal);
            // textview_type_marketvalue.setText("" + sum_total);
            // textview_total_marketval.setText("" + sum_total);
            //  textview_totalpropertyvalue_result.setText("" + sum_total);
            textview_type_marketvalue.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));
           // textview_total_marketval.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));
            textview_totalpropertyvalue_result.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));

        } else {
            textview_type_marketvalue.setText("" + 0);
           // textview_total_marketval.setText("" + 0);
            textview_totalpropertyvalue_result.setText("" + 0);
        }
    }

    public void TerraceMarketCalculation(String area_val, String area_rate) {
        if (!general.isEmpty(area_val) && !general.isEmpty(area_rate)) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(area_val)) * (general.convertTofloat(area_rate));
            sum_total = general.convertToRoundoff(sumtotal);
            //  textview_terrace_marketvalue.setText("" + sum_total);
            textview_terrace_marketvalue.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));

        } else {
            textview_terrace_marketvalue.setText("" + 0);
        }


        /*****Total calculation*****/

        String area_total = textview_type_marketvalue.getText().toString().trim();
        String terrace_total = textview_terrace_marketvalue.getText().toString().trim();

        if (!general.isEmpty(area_total) && !(general.isEmpty(terrace_total))) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(area_total)) + (general.convertTofloat(terrace_total));
            sum_total = general.convertToRoundoff(sumtotal);
          /*  textview_total_marketval.setText("" + sum_total);
            textview_totalpropertyvalue_result.setText("" + sum_total);*/

           // textview_total_marketval.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));
            textview_totalpropertyvalue_result.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));

        } else if (!general.isEmpty(area_total)) {

            //textview_total_marketval.setText("" + general.DecimalFormattedCommaString(String.valueOf(area_total)));
            textview_totalpropertyvalue_result.setText("" + general.DecimalFormattedCommaString(String.valueOf(area_total)));

          /*  textview_total_marketval.setText("" + area_total);
            textview_totalpropertyvalue_result.setText("" + area_total);*/
        } else if (!general.isEmpty(terrace_total)) {

            //textview_total_marketval.setText("" + general.DecimalFormattedCommaString(String.valueOf(terrace_total)));
            textview_totalpropertyvalue_result.setText("" + general.DecimalFormattedCommaString(String.valueOf(terrace_total)));
           /* textview_total_marketval.setText("" + terrace_total);
            textview_totalpropertyvalue_result.setText("" + terrace_total);*/
        }
    }

    public void InsuranceMarketCalculation(String area_val, String area_rate) {
        if (!general.isEmpty(area_val) && !general.isEmpty(area_rate)) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(area_val)) * (general.convertTofloat(area_rate));
            sum_total = general.convertToRoundoff(sumtotal);
            //    textview_insurance_marketvalue.setText("" + sum_total);
            textview_insurance_marketvalue.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));
        } else {
            textview_insurance_marketvalue.setText("" + 0);
        }
    }


    public void GovernmentMarketCalculation(String area_val, String area_rate) {
        if (!general.isEmpty(area_val) && !general.isEmpty(area_rate)) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(area_val)) * (general.convertTofloat(area_rate));
            sum_total = general.convertToRoundoff(sumtotal);
            //textview_governmentvalue_marketvalue.setText("" + sum_total);
            textview_governmentvalue_marketvalue.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));
        } else {
            textview_governmentvalue_marketvalue.setText("" + 0);
        }
    }

    public void AsperCompletionCalculation(String completed_percentage, String total_propertyval) {
        if (!general.isEmpty(completed_percentage) && !general.isEmpty(total_propertyval)) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(completed_percentage) / 100) * (general.convertTofloat(total_propertyval));
            sum_total = general.convertToRoundoff(sumtotal);
            //  textview_aspercompletion_result.setText("" + sum_total);
            textview_aspercompletion_result.setText("" + general.DecimalFormattedCommaString(String.valueOf(sum_total)));
        } else {
            textview_aspercompletion_result.setText("" + 0);
        }
    }

    public void setRealizableDistressCalculationVal() {

        String select_realizable = spinner_realizable.getText().toString();
        if ((!select_realizable.equalsIgnoreCase("Select")) && (!select_realizable.equalsIgnoreCase("Custom"))) {
            String realize = select_realizable.replace("%", "").trim();
            String realizable = RealizableDistressCalculation(realize, textview_totalpropertyvalue_result.getText().toString().trim());
            edittext_realizable_value_total.setText("" + general.DecimalFormattedCommaString(realizable));
        }

        String select_distress = spinner_distress.getText().toString();
        if ((!select_distress.equalsIgnoreCase("Select")) && (!select_distress.equalsIgnoreCase("Custom"))) {
            String distressval = select_distress.replace("%", "").trim();
            String distress = RealizableDistressCalculation(distressval, textview_totalpropertyvalue_result.getText().toString().trim());
            edittext_distress_total.setText("" + general.DecimalFormattedCommaString(distress));
        }
    }


    public String RealizableDistressCalculation(String realizable_distress_val, String total_property_val) {
        String total_value = "";
        if (!general.isEmpty(realizable_distress_val) && !general.isEmpty(total_property_val) && !realizable_distress_val.equalsIgnoreCase("Custom")) {
            float sumtotal = 0;
            int sum_total = 0;
            sumtotal = (general.convertTofloat(realizable_distress_val) / 100) * (general.convertTofloat(total_property_val));
            sum_total = general.convertToRoundoff(sumtotal);
            total_value = "" + sum_total;
        } else {
            total_value = "" + 0;
        }
        return total_value;
    }


    public void RealizableDistressSpinner() {
/*
        // spinner - realizableList
        realizableList = new ArrayList<>();
        realizableList = general.Constructionval_common_array();
        ArrayAdapter<String> realizableListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner_item, realizableList) {
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
        realizableListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_realizable.setAdapter(realizableListAdapter);
        spinner_realizable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Singleton.getInstance().indPropertyValuation.setRealizationPercentage(null);
                } else {
                    if (realizableList.get(position).toString().equalsIgnoreCase("Custom")) {
                        Singleton.getInstance().indPropertyValuation.setRealizationPercentage("101");
                        edittext_realizable_value_total.setFocusable(true);
                        edittext_realizable_value_total.setFocusableInTouchMode(true);
                        edittext_realizable_value_total.setEnabled(true);
                        if(general.isEmpty(Singleton.getInstance().indPropertyValuation.getRealizationValue()))
                        edittext_realizable_value_total.setText("");
                    } else {
                        String realizableStr = realizableList.get(position).toString().replace("%", "");
                        Singleton.getInstance().indPropertyValuation.setRealizationPercentage(realizableStr);

                        String realizablevalue = RealizableDistressCalculation(realizableStr, textview_totalpropertyvalue_result.getText().toString().trim());
                        edittext_realizable_value_total.setText("" + general.DecimalFormattedCommaString(realizablevalue));
                        Singleton.getInstance().indPropertyValuation.setRealizationValue(realizablevalue);
                        edittext_realizable_value_total.setFocusable(false);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getRealizationPercentage()))

        {
             *//* values *//*
            for (int x = 0; x < realizableList.size(); x++) {
                if (realizableList.get(x).toString().replace("%", "").trim().equalsIgnoreCase(Singleton.getInstance().indPropertyValuation.getRealizationPercentage())) {
                    spinner_realizable.setSelection(x);
                }
            }
          //  edittext_realizable_value_total.setText(Singleton.getInstance().indPropertyValuation.getRealizationValue());
            edittext_realizable_value_total.setText("" + general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getRealizationValue()));
        } else if (Singleton.getInstance().indPropertyValuation.getRealizationPercentage() == null || Singleton.getInstance().indPropertyValuation.getRealizationPercentage() == "null")

        {
            *//* select - 0 - position *//*
            spinner_realizable.setSelection(0);
        } else

        {
            *//* Custom *//*
            spinner_realizable.setSelection(12);
            edittext_realizable_value_total.setFocusable(true);
            //edittext_realizable_value_total.setText(Singleton.getInstance().indPropertyValuation.getRealizationValue());
            edittext_realizable_value_total.setText("" + general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getRealizationValue()));
        }


        */


        /*// spinner - distressList
        distressList = new ArrayList<>();
        distressList = general.Constructionval_common_array();
        ArrayAdapter<String> distressListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner_item, distressList) {
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
        distressListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_distress.setAdapter(distressListAdapter);
        spinner_distress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Singleton.getInstance().indPropertyValuation.setDistressPercentage(null);
                } else {
                    if (distressList.get(position).toString().equalsIgnoreCase("Custom")) {
                        Singleton.getInstance().indPropertyValuation.setDistressPercentage("101");
                        edittext_distress_total.setFocusable(true);
                        edittext_distress_total.setFocusableInTouchMode(true);
                        edittext_distress_total.setEnabled(true);
                        if (general.isEmpty(Singleton.getInstance().indPropertyValuation.getDistressValue()))
                            edittext_distress_total.setText("");
                    } else {
                        String distressStr = distressList.get(position).toString().replace("%", "");
                        Singleton.getInstance().indPropertyValuation.setDistressPercentage(distressStr);

                        String distressvalue = RealizableDistressCalculation(distressStr, textview_totalpropertyvalue_result.getText().toString().trim());
                        //edittext_distress_total.setText(distressvalue);
                        edittext_distress_total.setText("" + general.DecimalFormattedCommaString(distressvalue));
                        Singleton.getInstance().indPropertyValuation.setDistressValue(distressvalue);

                        edittext_distress_total.setFocusable(false);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getDistressPercentage()))

        {
            *//* values *//*
            for (int x = 0; x < distressList.size(); x++) {
                if (distressList.get(x).toString().replace("%", "").trim().equalsIgnoreCase(Singleton.getInstance().indPropertyValuation.getDistressPercentage())) {
                    spinner_distress.setSelection(x);
                }
            }
            //edittext_distress_total.setText(Singleton.getInstance().indPropertyValuation.getDistressValue());
            edittext_distress_total.setText("" + general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getDistressValue()));
        } else if (Singleton.getInstance().indPropertyValuation.getDistressPercentage() == null || Singleton.getInstance().indPropertyValuation.getDistressPercentage() == "null")

        {
            *//* select - 0 - position *//*
            spinner_distress.setSelection(0);
        } else

        {
            *//* Custom *//*
            spinner_distress.setSelection(12);
            edittext_distress_total.setFocusable(true);
            edittext_distress_total.setText("" + general.DecimalFormattedCommaString(Singleton.getInstance().indPropertyValuation.getDistressValue()));
            //  edittext_distress_total.setText(Singleton.getInstance().indPropertyValuation.getDistressValue());
        }*/
    }

    public void LoadingBuildupSpinner() {

     /*   // spinner - Loading over Build up
        loadingbuildupList = new ArrayList<>();
        loadingbuildupList = general.LoadingBuilup_common_array();
        ArrayAdapter<String> ListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner_item, loadingbuildupList) {
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
        ListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoadingOverBuildup.setAdapter(ListAdapter);
        spinnerLoadingOverBuildup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Singleton.getInstance().indPropertyValuation.setBAULoadingPercentage(null);
                } else {

                    String loadingBuildupStr = loadingbuildupList.get(position).toString().replace("%", "");
                    Singleton.getInstance().indPropertyValuation.setBAULoadingPercentage(loadingBuildupStr);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage()))

        {
             *//* values *//*
            for (int x = 0; x < loadingbuildupList.size(); x++) {
                if (loadingbuildupList.get(x).toString().replace("%", "").trim().equalsIgnoreCase(Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage())) {
                    spinnerLoadingOverBuildup.setSelection(x);
                }
            }
        } else if (Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage() == null || Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage() == "null")

        {
            *//* select - 0 - position *//*
            spinnerLoadingOverBuildup.setSelection(0);
        } else

        {
            *//* Custom *//*
            spinnerLoadingOverBuildup.setSelection(12);
        }*/
    }

    public void RealizeDistressTotalVal() {
        edittext_realizable_value_total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String value = charSequence.toString();
                if (!general.isEmpty(value)) {
                    Singleton.getInstance().indPropertyValuation.setRealizationValue(value);
                } else {
                    Singleton.getInstance().indPropertyValuation.setRealizationValue("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edittext_distress_total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = charSequence.toString();
                if (!general.isEmpty(value)) {
                    Singleton.getInstance().indPropertyValuation.setDistressValue(value);
                } else {
                    Singleton.getInstance().indPropertyValuation.setDistressValue("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void setIndPropertyValuationforPentHouseFlat() {

        if (Singleton.getInstance().indPropertyValuation != null) {

            String caseid = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");
            if (!general.isEmpty(caseid))
                Singleton.getInstance().indPropertyValuation.setCaseId(Integer.valueOf(caseid));
            Singleton.getInstance().indPropertyValuation.setCarpetArea(edittext_carpet_area.getText().toString().trim());
            Singleton.getInstance().indPropertyValuation.setBuildUpArea(edittext_builtup_area.getText().toString().trim());
            Singleton.getInstance().indPropertyValuation.setSuperBuildUpArea(edittext_Saleable_area.getText().toString().trim());
            // Singleton.getInstance().indPropertyValuation.setCarpetAreaUnit(Measurement_id);
            Singleton.getInstance().indPropertyValuation.setCarpetAreaTypeId(areaTypeID);

            Singleton.getInstance().indPropertyValuation.setTotalValueAtCompletion(general.ReplaceCommaSaveToString(textview_aspercompletion_result.getText().toString().trim()));
            Singleton.getInstance().indPropertyValuation.setCompletionPercentage(editText_AsPerCompletion.getText().toString().trim());
            Singleton.getInstance().indPropertyValuation.setTotalPropertyValue(general.ReplaceCommaSaveToString(textview_totalpropertyvalue_result.getText().toString().trim()));

            if (property_type.equalsIgnoreCase("penthouse")) {
               // Singleton.getInstance().indPropertyValuation.setTerraceArea(edittext_terrace_area.getText().toString().trim());
                Singleton.getInstance().indPropertyValuation.setTerraceRate(edittext_terrace_rate.getText().toString().trim());
                Singleton.getInstance().indPropertyValuation.setTerraceValue(general.ReplaceCommaSaveToString(textview_terrace_marketvalue.getText().toString().trim()));
            }

            Singleton.getInstance().indPropertyValuation.setConstructionRate(edittext_type_rate.getText().toString().trim());
            Singleton.getInstance().indPropertyValuation.setInsuredConstructionRate(edittext_insurance_rate.getText().toString().trim());
            Singleton.getInstance().indPropertyValuation.setInsuranceValue(general.ReplaceCommaSaveToString(textview_insurance_marketvalue.getText().toString().trim()));
            Singleton.getInstance().indPropertyValuation.setConstructionDLCRate(edittext_governmentvalue_rate.getText().toString().trim());
            Singleton.getInstance().indPropertyValuation.setConstructionDLCValue(general.ReplaceCommaSaveToString(textview_governmentvalue_marketvalue.getText().toString().trim()));
            String val = edittext_realizable_value_total.getText().toString().trim();
            Singleton.getInstance().indPropertyValuation.setRealizationValue(general.ReplaceCommaSaveToString(edittext_realizable_value_total.getText().toString().trim()));
            Singleton.getInstance().indPropertyValuation.setDistressValue(general.ReplaceCommaSaveToString(edittext_distress_total.getText().toString().trim()));
            Singleton.getInstance().indPropertyValuation.setProposedValuation(edittext_proposedvaluation_result.getText().toString().trim());
            Singleton.getInstance().indPropertyValuation.setProposedValuationComments(editText_remarks.getText().toString().trim());

            //getSubSequentRevaluation();
        }

        if (Singleton.getInstance().indProperty != null) {
            String caseid = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");
            if (!general.isEmpty(caseid))
                Singleton.getInstance().indProperty.setCaseId(Integer.valueOf(caseid));
            Singleton.getInstance().indProperty.setAvgPercentageCompletion(editText_AsPerCompletion.getText().toString().trim());
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
        String selected_value = "";
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
                        Singleton.getInstance().indPropertyValuation.setRealizationPercentage("");
                        // setRealizationValue
                        edittext_realizable_value_total.setText("");
                        Singleton.getInstance().indPropertyValuation.setRealizationValue("");
                    } else if (str_radiogenearal.equalsIgnoreCase("Custom")) {
                        textView.setText(str_radiogenearal);
                        // For reference set as 0 for custom
                        Singleton.getInstance().indPropertyValuation.setRealizationPercentage("101");
                        edittext_realizable_value_total.setText("");
                        Singleton.getInstance().indPropertyValuation.setRealizationValue("");
                        // setRealizationValue
                        edittext_realizable_value_total.setFocusable(true);
                        edittext_realizable_value_total.setFocusableInTouchMode(true);
                        edittext_realizable_value_total.setEnabled(true);
                        if (general.isEmpty(Singleton.getInstance().indPropertyValuation.getRealizationValue()))
                            edittext_realizable_value_total.setText("");
                    } else {
                        String realizableStr = str_radiogenearal.replace("%", "");
                        textView.setText(realizableStr);
                        Singleton.getInstance().indPropertyValuation.setRealizationPercentage(realizableStr);
                        // setRealizationValue
                        String realizablevalue = RealizableDistressCalculation(realizableStr, textview_totalpropertyvalue_result.getText().toString().trim());
                        edittext_realizable_value_total.setText("" + general.DecimalFormattedCommaString(realizablevalue));
                        Singleton.getInstance().indPropertyValuation.setRealizationValue(realizablevalue);
                        edittext_realizable_value_total.setFocusable(false);
                    }
                } else if (type_is == 2) {
                    // DistressPercentage
                    if (str_radiogenearal.equalsIgnoreCase("Select")) {
                        textView.setText(str_radiogenearal);
                        Singleton.getInstance().indPropertyValuation.setDistressPercentage("");
                        // setDistressValue
                        edittext_distress_total.setText("");
                        Singleton.getInstance().indPropertyValuation.setDistressValue("");
                    } else if (str_radiogenearal.equalsIgnoreCase("Custom")) {
                        textView.setText(str_radiogenearal);
                        // For reference set as 0 for custom
                        Singleton.getInstance().indPropertyValuation.setDistressPercentage("101");
                        edittext_distress_total.setText("");
                        Singleton.getInstance().indPropertyValuation.setDistressValue("");
                        // setDistressValue
                        edittext_distress_total.setFocusable(true);
                        edittext_distress_total.setFocusableInTouchMode(true);
                        edittext_distress_total.setEnabled(true);
                        if (general.isEmpty(Singleton.getInstance().indPropertyValuation.getDistressValue()))
                            edittext_distress_total.setText("");
                    } else {
                        String distressStr = str_radiogenearal.replace("%", "");
                        textView.setText(distressStr);
                        Singleton.getInstance().indPropertyValuation.setDistressPercentage(distressStr);
                        // setDistressValue
                        String distressvalue = RealizableDistressCalculation(distressStr, textview_totalpropertyvalue_result.getText().toString().trim());
                        edittext_distress_total.setText("" + general.DecimalFormattedCommaString(distressvalue));
                        Singleton.getInstance().indPropertyValuation.setDistressValue(distressvalue);
                        edittext_distress_total.setFocusable(false);
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


    private void loadingOverBuildup_popup(final TextView textView) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.loading_over_builup_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView popuptitle = (TextView) dialog.findViewById(R.id.title);
        popuptitle.setTypeface(general.mediumtypeface());

        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        RadioGroup id_radiogroup_stage = (RadioGroup) dialog.findViewById(R.id.id_radiogroup_stage);
        RadioButton id_radio_select = (RadioButton) dialog.findViewById(R.id.id_radio_select);
        RadioButton id_radio_0 = (RadioButton) dialog.findViewById(R.id.id_radio_0);
        RadioButton id_radio_5 = (RadioButton) dialog.findViewById(R.id.id_radio_5);
        RadioButton id_radio_10 = (RadioButton) dialog.findViewById(R.id.id_radio_10);
        RadioButton id_radio_15 = (RadioButton) dialog.findViewById(R.id.id_radio_15);
        RadioButton id_radio_20 = (RadioButton) dialog.findViewById(R.id.id_radio_20);
        RadioButton id_radio_25 = (RadioButton) dialog.findViewById(R.id.id_radio_25);
        RadioButton id_radio_30 = (RadioButton) dialog.findViewById(R.id.id_radio_30);
        RadioButton id_radio_35 = (RadioButton) dialog.findViewById(R.id.id_radio_35);
        RadioButton id_radio_40 = (RadioButton) dialog.findViewById(R.id.id_radio_40);
        RadioButton id_radio_45 = (RadioButton) dialog.findViewById(R.id.id_radio_45);
        RadioButton id_radio_50 = (RadioButton) dialog.findViewById(R.id.id_radio_50);
        RadioButton id_radio_55 = (RadioButton) dialog.findViewById(R.id.id_radio_55);
        RadioButton id_radio_60 = (RadioButton) dialog.findViewById(R.id.id_radio_60);
        RadioButton id_radio_65 = (RadioButton) dialog.findViewById(R.id.id_radio_65);
        RadioButton id_radio_70 = (RadioButton) dialog.findViewById(R.id.id_radio_70);
        RadioButton id_radio_75 = (RadioButton) dialog.findViewById(R.id.id_radio_75);
        id_radio_select.setTypeface(general.mediumtypeface());
        id_radio_0.setTypeface(general.mediumtypeface());
        id_radio_5.setTypeface(general.mediumtypeface());
        id_radio_10.setTypeface(general.mediumtypeface());
        id_radio_15.setTypeface(general.mediumtypeface());
        id_radio_20.setTypeface(general.mediumtypeface());
        id_radio_25.setTypeface(general.mediumtypeface());
        id_radio_30.setTypeface(general.mediumtypeface());
        id_radio_35.setTypeface(general.mediumtypeface());
        id_radio_40.setTypeface(general.mediumtypeface());
        id_radio_45.setTypeface(general.mediumtypeface());
        id_radio_50.setTypeface(general.mediumtypeface());
        id_radio_55.setTypeface(general.mediumtypeface());
        id_radio_60.setTypeface(general.mediumtypeface());
        id_radio_65.setTypeface(general.mediumtypeface());
        id_radio_70.setTypeface(general.mediumtypeface());
        id_radio_75.setTypeface(general.mediumtypeface());

        boolean is_empty = true;
        String selected_value = "";

        // loading buildup Percentage
        popuptitle.setText(getActivity().getResources().getString(R.string.loading_per));
        if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage())) {
            is_empty = false;
            selected_value = Singleton.getInstance().indPropertyValuation.getBAULoadingPercentage();
        }


        if (!is_empty) {
            if (selected_value.equalsIgnoreCase("0")) {
                id_radio_0.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("5")) {
                id_radio_5.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("10")) {
                id_radio_10.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("15")) {
                id_radio_15.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("20")) {
                id_radio_20.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("25")) {
                id_radio_25.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("30")) {
                id_radio_30.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("35")) {
                id_radio_35.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("40")) {
                id_radio_40.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("45")) {
                id_radio_45.setChecked(true);
            } else if (selected_value.equalsIgnoreCase("50")) {
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

                // loading over build upPercentage
                if (str_radiogenearal.equalsIgnoreCase("Select")) {
                    textView.setText(str_radiogenearal);
                    Singleton.getInstance().indPropertyValuation.setBAULoadingPercentage("");
                } else {
                    String loadingStr = str_radiogenearal.replace("%", "");
                    textView.setText(loadingStr);
                    Singleton.getInstance().indPropertyValuation.setBAULoadingPercentage(loadingStr);

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


    private void hideSoftKeyboard(View addkeys) {
        if ((addkeys != null) && (getActivity() != null)) {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(addkeys.getWindowToken(), 0);
        }
        show_emptyFocus();
    }



    @Override
    public void onRatePopupSucess(RatePopup ratePopup) {

        if (ratePopup.getData() != null) {
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


    }

    @Override
    public void onRatePopupFailed(String msg) {
        Toast.makeText(getActivity(), "Unable to fetch market rate value!", Toast.LENGTH_SHORT).show();
    }




}