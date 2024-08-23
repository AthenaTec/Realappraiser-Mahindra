package com.realappraiser.gharvalue.FieldsInspection;

import static com.realappraiser.gharvalue.fragments.PhotoLatLong.latsValue;
import static com.realappraiser.gharvalue.fragments.PhotoLatLong.latvalue;
import static com.realappraiser.gharvalue.fragments.PhotoLatLong.longsValue;
import static com.realappraiser.gharvalue.fragments.PhotoLatLong.longvalue;
import static com.realappraiser.gharvalue.utils.General.siteVisitDateToConversion;
import static com.realappraiser.gharvalue.utils.General.uiVisiblityCount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.realappraiser.gharvalue.AppDatabase;
import com.realappraiser.gharvalue.Interface.AverageComPerInterface;
import com.realappraiser.gharvalue.MyApplication;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.activities.HomeActivity;
import com.realappraiser.gharvalue.adapter.ExterStructureAdapter;
import com.realappraiser.gharvalue.adapter.FlooringAdapter;
import com.realappraiser.gharvalue.adapter.PresentlyOccupiedAdapter;
import com.realappraiser.gharvalue.adapter.ProximityAdapter;
import com.realappraiser.gharvalue.adapter.Recycler_remarks_adapter;
import com.realappraiser.gharvalue.adapter.RoofingAdapter;
import com.realappraiser.gharvalue.communicator.DataModel;
import com.realappraiser.gharvalue.communicator.DataResponse;
import com.realappraiser.gharvalue.communicator.DropDownResponse;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.ResponseParser;
import com.realappraiser.gharvalue.communicator.ShowFSUIResponse;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.fragments.FragmentBuilding;
import com.realappraiser.gharvalue.fragments.FragmentLand;
import com.realappraiser.gharvalue.fragments.FragmentValuationLand;
import com.realappraiser.gharvalue.fragments.OtherDetailsFragment;
import com.realappraiser.gharvalue.fragments.PhotoLatLong;
import com.realappraiser.gharvalue.model.ConcreteGrade;
import com.realappraiser.gharvalue.model.EnvExposureCondition;
import com.realappraiser.gharvalue.model.Floor;
import com.realappraiser.gharvalue.model.GetPhoto;
import com.realappraiser.gharvalue.model.IndPropertyFloorsValuation;
import com.realappraiser.gharvalue.model.Land;
import com.realappraiser.gharvalue.model.LatLongDetails;
import com.realappraiser.gharvalue.model.Locality;
import com.realappraiser.gharvalue.model.Maintenance;
import com.realappraiser.gharvalue.model.Marketablity;
import com.realappraiser.gharvalue.model.OfflineDataModel;
import com.realappraiser.gharvalue.model.OflineCase;
import com.realappraiser.gharvalue.model.OwnershipType;
import com.realappraiser.gharvalue.model.PresentlyOccupied;
import com.realappraiser.gharvalue.model.Proximity;
import com.realappraiser.gharvalue.model.Remarks;
import com.realappraiser.gharvalue.model.RequestApiStatus;
import com.realappraiser.gharvalue.model.Roof;
import com.realappraiser.gharvalue.model.SoilType;
import com.realappraiser.gharvalue.model.Structure;
import com.realappraiser.gharvalue.model.Tenure;
import com.realappraiser.gharvalue.model.TypeOfFooting;
import com.realappraiser.gharvalue.model.TypeOfMasonry;
import com.realappraiser.gharvalue.model.TypeOfMortar;
import com.realappraiser.gharvalue.model.TypeOfSteel;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.GPSService;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.ResponseStorage;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.utils.Singleton;
import com.realappraiser.gharvalue.worker.LocationTrackerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.realappraiser.gharvalue.R.id;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FieldsInspectionDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FieldsInspectionDetails extends Fragment implements View.OnClickListener, View.OnTouchListener, CompoundButton.OnCheckedChangeListener, AverageComPerInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = FieldsInspectionDetails.class.getName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Boolean isNPA = false;
    String property_type = "building";

    String editText_db_east_str, editText_db_west_str, editText_db_north_str, editText_db_south_str;

    private String db_east = "", db_west = "", db_north = "", db_south = "";


    private static FieldsInspectionDetails instance = null;

    private String siteVisiteInCalender = "", visitDate = "";


    private static boolean isImageClicked = false;
    private static boolean isLocalityImgClicked = false;
    private static boolean isPropertyImgClicked = false;
    private static boolean isPropertyDetailsImgClicked = false;
    private static boolean isNdmaParamterImgClicked = false;
    private static boolean isFair_market_valuationImgClicked = false;

    public boolean enable_offline_button;

    //Todo Room persistance Library for offfline storage
    private AppDatabase appDatabase;

    int PropertyId_is = 0;

    private String msg = "", info = "";

    int caseid_int = 0;

    private General general;

    ArrayList<String> initials_typeList = new ArrayList<>();

    boolean isvalid = true;
    String str_latvalue, str_longvalue;

    Context mContext;
    private String caseid;

    boolean is_offline = false;
    boolean is_local = false;

    public static String save_type = "";

    public static final Handler _handler = new Handler();
    private static int DATA_INTERVAL = 30 * 1000;

    @BindView(R.id.txt_proressBar)
    TextView txtProgressBar;

    @BindView(R.id.image_general_layout)
    ImageView imageView;

    @BindView(R.id.image_locality_layout)
    ImageView imgLocality;

    @BindView(R.id.image_property_layout)
    ImageView imgProperty;

    @BindView(R.id.image_property_details_layout)
    ImageView imgPropertyDetails;

    @BindView(R.id.image_ndma_parameter_layout)
    ImageView imgNdmaParamter;

    @BindView(R.id.image_fair_market_valuation_layout)
    ImageView imgFairMarketValuation;

    @BindView(R.id.general_text)
    TextView textView;

    @BindView(R.id.locality_text)
    TextView txtLocality;

    @BindView(R.id.property_text)
    TextView txtProperty;

    @BindView(R.id.property_details_text)
    TextView txtPropertyDetails;

    @BindView(R.id.ndma_parameter_text)
    TextView txtNdmaParamter;

    @BindView(R.id.fair_market_valuation_text)
    TextView txtFairMarketValuationLayout;

    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.localityCardView)
    CardView cardViewLocality;

    @BindView(R.id.propertyCardView)
    CardView cardViewProperty;

    @BindView(R.id.cardViewPropertyDetails)
    CardView cardViewPropertyDetails;

    @BindView(R.id.cardViewndma_parameter)
    CardView cardViewNdmaParamter;

    @BindView(R.id.cardView_fair_market_valuation)
    CardView cardViewFairMarketValuationLayout;

    @BindView(R.id.ll_general_sec_layout)
    LinearLayout linearLayout;

    @BindView(R.id.ll_locality_subsection)
    LinearLayout llLocalitySubSection;

    @BindView(R.id.ll_property_subsection)
    LinearLayout llPropertySubSection;

    @BindView(R.id.ll_property_details_sec_layout)
    LinearLayout llPropertyDetailsSubSection;

    @BindView(R.id.ll_ndma_parameter_sec_layout)
    LinearLayout llNdmaParamter;

    @BindView(R.id.ll_fair_market_valuation_sec_layout)
    LinearLayout llFairMarketValuationLayoutSubSection;

    @BindView(R.id.ll_as_per_flat)
    LinearLayout llAsPerFlat;


    @BindView(R.id.etName)
    EditText applicantName;

    /*@BindView(R.id.spSalutionBorrower)
    Spinner spSalutionBorrower;*/

    @BindView(R.id.et_SalutionBorrower)
    TextInputEditText et_SalutionBorrower;

    @BindView(R.id.llNameOfBorrower)
    LinearLayout llNameOfBorrower;

    @BindView(R.id.et_seller_name)
    EditText et_seller_name;

    @BindView(R.id.spSalutionSeller)
    Spinner spSalutionSeller;

    /*@BindView(R.id.spinner_type_of_loan)
    Spinner spinner_type_of_loan;*/

    @BindView(R.id.et_type_of_loan)
    TextInputEditText etTypeOfLoan;

    @BindView(R.id.ll_type_of_loan)
    LinearLayout llTypeOfLoan;

    @BindView(R.id.llNameOfSeller)
    LinearLayout llNameOfSeller;


    @BindView(R.id.et_seller_type)
    EditText et_seller_type;

    @BindView(R.id.layout_seller_type)
    TextInputLayout til_seller_type;

    @BindView(R.id.ll_contact_info)
    LinearLayout ll_contact_info;
    @BindView(R.id.etPersonName)
    EditText etPersonName;

    @BindView(R.id.etPersonNo)
    EditText etPersonNo;

    @BindView(R.id.ll_contact_info_property)
    LinearLayout ll_contact_info_property;

    @BindView(R.id.etPersonName_property)
    EditText etPersonName_property;

    @BindView(R.id.etPersonNo_property)
    EditText etPersonNo_property;

    @BindView(R.id.tl_etpropertyTypeProperty)
    TextInputLayout tl_etpropertyTypeProperty;

    @BindView(R.id.tl_etpropertyTypeNdma)
    TextInputLayout tl_etpropertyTypeNdma;

    @BindView(R.id.txt_type_of_structure_property_dtls)
    TextView type_of_construction_property_dtls;


    @BindView(R.id.textview_exter_struc_text_property_dtls)
    TextView textview_exter_struc_text_property_dtls;

    @BindView(R.id.llFlooring_property_dtls)
    LinearLayout llFlooring_property_dtls;

    @BindView(R.id.txt_flooring_property_dtls)
    TextView txt_flooring_property_dtls;

    @BindView(R.id.textview_flooring_text_property_dtls)
    TextView textview_flooring_text_property_dtls;
    @BindView(R.id.iv_calender)
    ImageView iv_calender;

    @BindView(R.id.date_value)
    TextView date_value;

    @BindView(R.id.ll_calender)
    LinearLayout ll_calender;

    @BindView(R.id.date_error_msg)
    TextView date_error_msg;

    @BindView(R.id.et_name_of_owner)
    EditText et_name_of_owner;

    @BindView(R.id.spSalutionOwner)
    Spinner spSalutionOwner;

    @BindView(R.id.llNameOfOwner)
    LinearLayout llNameOfOwner;

    @BindView(R.id.et_finnonId)
    EditText et_finnonId;

    @BindView(R.id.tl_finnonId)
    TextInputLayout tl_finnonId;


    /* Locality */
    @BindView(R.id.tl_complete_address)
    TextInputLayout tl_complete_address;

    @BindView(R.id.editText_addr_perdoc)
    EditText editText_addr_perdoc;

    @BindView(R.id.scroll_view)
    ScrollView scroll_view;

    @BindView(R.id.etLat)
    EditText etLat;

    @BindView(R.id.etLong)
    EditText etLong;

    @BindView(R.id.tl_plot_no)
    TextInputLayout tl_plot_no;

    @BindView(R.id.editText_plotno)
    EditText editText_plotno;

    @BindView(R.id.tl_unit_no)
    TextInputLayout tl_unit_no;

    @BindView(R.id.editText_unit_no)
    EditText editText_unit_no;

    @BindView(R.id.tl_taluka)
    TextInputLayout tl_taluka;

    @BindView(R.id.et_taluka)
    EditText et_taluka;

    @BindView(R.id.tl_village_post)
    TextInputLayout tl_village_post;

    @BindView(R.id.et_village_post)
    EditText et_village_post;

    @BindView(R.id.tl_survey_no)
    TextInputLayout tl_survey_no;

    @BindView(R.id.editText_surveyno)
    EditText editText_surveyno;

    @BindView(R.id.tl_district)
    TextInputLayout til_district;

    @BindView(R.id.et_district)
    EditText et_district;

    @BindView(R.id.editText_landmark)
    EditText editText_landmark;

    @BindView(R.id.tl_landmark)
    TextInputLayout tl_landmark;

    @BindView(R.id.et_pincode)
    EditText etPinCode;

    @BindView(R.id.tl_pincode)
    TextInputLayout tl_pincode;

    @BindView(R.id.ll_geo_co_ordinates)
    LinearLayout ll_geo_co_ordinates;

    /* as per document*/
    @BindView(R.id.ll_as_per_doc)
    LinearLayout ll_as_per_doc;

    boolean asPerDoc = false;

    @BindView(R.id.editText_db_east)
    EditText editText_db_east;
    @BindView(R.id.editText_db_west)
    EditText editText_db_west;
    @BindView(R.id.editText_db_north)
    EditText editText_db_north;
    @BindView(R.id.editText_db_south)
    EditText editText_db_south;

    /* As per site-building/house */
    @BindView(R.id.ll_as_per_site)
    LinearLayout ll_as_per_site;

    boolean asPerSite = false;


    @BindView(R.id.editText_ab_east)
    EditText editText_ab_east;
    @BindView(R.id.editText_ab_west)
    EditText editText_ab_west;
    @BindView(R.id.editText_ab_north)
    EditText editText_ab_north;
    @BindView(R.id.editText_ab_south)
    EditText editText_ab_south;

    @BindView(R.id.spinner_typeoflocality)
    Spinner spinner_typeoflocality;

    @BindView(R.id.ll_locality)
    LinearLayout ll_locality;


    @BindView(R.id.ll_proximity)
    LinearLayout ll_proximity;

    @BindView(R.id.recyclerview_proxmity)
    RecyclerView recyclerview_proxmity;


    @BindView(R.id.ll_masonry)
    LinearLayout ll_masonry;
    @BindView(R.id.spinner_masonry)
    Spinner spinner_masonry;

    @BindView(R.id.spinner_landapprovedfor)
    Spinner spinner_landapprovedfor;


    private ArrayList<Proximity> list;

    private ProximityAdapter listAdapter;

    private LinearLayoutManager llm;


    @BindView(R.id.et_habitation)
    EditText et_habitation;

    @BindView(R.id.tl_habitation)
    TextInputLayout tl_habitation;

    @BindView(R.id.et_adverse_nearby)
    EditText et_adverse_nearby;

    @BindView(R.id.tl_adverse_nearby)
    TextInputLayout til_adverse_nearby;


    @BindView(R.id.etUnitActualEast)
    EditText etUnitActualEast;
    @BindView(R.id.etUnitActualWest)
    EditText etUnitActualWest;
    @BindView(R.id.etUnitActualNorth)
    EditText etUnitActualNorth;
    @BindView(R.id.etUnitActualSouth)
    EditText etUnitActualSouth;


    /* Property */

    @BindView(R.id.spinner_select_tenure_ownership)
    Spinner spinner_select_tenure_ownership;

    @BindView(R.id.ll_ownership)
    LinearLayout ll_ownership;


    @BindView(R.id.etpropertyTypeProperty)
    EditText etpropertyTypeProperty;


//    @BindView(R.id.etpropertyTypePropertyDetails)
//    EditText etpropertyTypePropertyDetails;

    @BindView(R.id.etpropertyTypeNdma)
    EditText etpropertyTypeNdma;

    @BindView(R.id.checkbox_sanctioned_plan)
    CheckBox checkbox_sanctioned_plan;

    @BindView(R.id.et_ground)
    EditText et_ground;

    @BindView(R.id.tl_ground_coverage)
    TextInputLayout tl_ground_coverage;

    @BindView(R.id.et_engineer_license)
    EditText et_engineer_license;

    @BindView(R.id.layout_engineer_license)
    TextInputLayout layout_engineer_license;

    @BindView(R.id.et_authority)
    EditText et_authority;

    @BindView(R.id.tl_authority)
    TextInputLayout tl_authority;

    @BindView(R.id.et_plan_prepared_by)
    EditText et_plan_prepared_by;

    @BindView(R.id.tl_plan_prepared_by)
    TextInputLayout tl_plan_prepared_by;

    @BindView(R.id.ll_planno_date)
    LinearLayout ll_planno_date;

    @BindView(R.id.et_approved_plan_no)
    EditText et_approved_plan_no;

    @BindView(R.id.et_approved_plan_date)
    EditText et_approved_plan_date;

    @BindView(R.id.spinner_typeoflocality_property)
    Spinner spinner_typeoflocality_property;

    @BindView(R.id.spinner_purpose)
    Spinner spinner_purpose;

    @BindView(R.id.ll_purpose)
    LinearLayout llPurpose;

    @BindView(R.id.checkbox_isproperty_demolish)
    CheckBox checkbox_isproperty_demolish;

    @BindView(R.id.ll_property_rbtn)
    LinearLayout llPropertyRbtn;

    @BindView(R.id.rg_demolishProperty)
    RadioGroup getRgDemoLish;

    /* property details*/
    @BindView(R.id.et_multiple_kitchen)
    EditText et_multiple_kitchen;

    @BindView(R.id.tl_no_of_multiple_kitchens)
    TextInputLayout tl_no_of_multiple_kitchens;

    @BindView(R.id.checkbox_lift_in_building)
    CheckBox checkbox_lift_in_building;

    @BindView(R.id.etAverageConstruction)
    EditText etAverageConstruction;

    @BindView(R.id.tl_avg_construction_per)
    TextInputLayout tl_avg_construction_per;

    @BindView(R.id.etRecommendationPercentage)
    EditText etRecommendationPercentage;

    @BindView(R.id.tl_recommendation_per)
    TextInputLayout tl_recommendation_per;

    @BindView(R.id.et_construction_stage)
    EditText et_construction_stage;

    @BindView(R.id.tl_construction_stage)
    TextInputLayout tl_construction_stage;

    //remark section
    @BindView(R.id.etRecommendationPercentage_remark)
    EditText etRecommendationPercentage_remark;

    @BindView(R.id.tl_recommendation_per_remark)
    TextInputLayout tl_recommendation_per_remark;

    @BindView(R.id.et_construction_stage_remark)
    EditText et_construction_stage_remark;

    @BindView(R.id.tl_construction_stage_remark)
    TextInputLayout tl_construction_stage_remark;


    @BindView(R.id.spinner_maintenanceofbuilding)
    Spinner spinner_maintenanceofbuilding;

    @BindView(R.id.ll_maintenace_of_building)
    LinearLayout ll_maintenace_of_building;

    @BindView(R.id.ll_presently_occupied)
    LinearLayout ll_presently_occupied;


    @BindView(R.id.textview_persently_occupied_text)
    TextView textview_persently_occupied_text;

    @BindView(R.id.et_occupant_name)
    EditText et_occupant_name;

    @BindView(R.id.tl_name_of_occupant)
    TextInputLayout tl_name_of_occupant;

    // Address - PresentlyOccupied
    ArrayList<PresentlyOccupied> PresentlyOccupied_list = new ArrayList<>();

    /* NDMA Parameter */

    // Exterior - Type Of Structure

    @BindView(R.id.txt_type_of_structure)
    TextView type_of_construction;
    ArrayList<Structure> Exter_struc_list = new ArrayList<>();
    ArrayList<Structure> Exter_struc_list_property_dtls = new ArrayList<>();
    @BindView(R.id.textview_exter_struc_text)
    TextView textview_exter_struc_text;

    @BindView(R.id.spinner_mortar)
    Spinner spinner_mortar;

    @BindView(R.id.ll_typeOfMotor)
    LinearLayout ll_typeOfMotor;

    @BindView(R.id.spinner_concrete_grade)
    Spinner spinner_concrete_grade;

    @BindView(R.id.ll_concrete)
    LinearLayout ll_concrete;

    @BindView(R.id.checkbox_expansion_joint)
    CheckBox checkbox_expansion_joint;

    @BindView(R.id.spinner_environment_exposure_condition)
    Spinner spinner_environment_exposure_condition;

    @BindView(R.id.ll_env)
    LinearLayout ll_env;

    @BindView(R.id.et_cyclone_zone)
    EditText et_cyclone_zone;

    @BindView(R.id.tl_cyclone_zone)
    TextInputLayout tl_cyclone_zone;

    @BindView(R.id.et_seismic)
    EditText et_seismic;

    @BindView(R.id.tl_seismic_zone)
    TextInputLayout tl_seismic_zone;


    // Floor
    ArrayList<Floor> Inter_floors_list = new ArrayList<>();
    ArrayList<Floor> Inter_floors_list_property_dtls = new ArrayList<>();
    @BindView(R.id.textview_flooring_text)
    TextView textview_flooring_text;

    @BindView(R.id.llFlooring)
    LinearLayout llFlooring;

    // Roof
    ArrayList<Roof> Inter_roofing_list = new ArrayList<>();
    @BindView(R.id.textview_roofing_text)
    TextView textview_roofing_text;

    @BindView(R.id.ll_roof_type)
    LinearLayout ll_roof_type;

    @BindView(R.id.et_flood_zone)
    EditText et_flood_zone;

    @BindView(R.id.tl_flood_prone_area)
    TextInputLayout tl_flood_prone_area;

    @BindView(R.id.checkbox_projected_part)
    CheckBox checkbox_projected_part;

    @BindView(R.id.spinner_soil_type)
    Spinner spinner_soil_type;

    @BindView(R.id.ll_type_soil)
    LinearLayout ll_type_soil;

    @BindView(R.id.et_aspect_ratio)
    EditText et_aspect_ratio;

    @BindView(R.id.tl_plan_aspect_ratio)
    TextInputLayout tl_plan_aspect_ratio;

    @BindView(R.id.et_regulatory_zone)
    EditText et_regulatory_zone;

    @BindView(R.id.tl_costal_regulatory_zone)
    TextInputLayout tl_costal_regulatory_zone;

    @BindView(R.id.spinner_foundation)
    Spinner spinner_foundation;

    @BindView(R.id.ll_footing_foundation)
    LinearLayout ll_footing_foundation;

    @BindView(R.id.et_above_ground)
    EditText et_above_ground;

    @BindView(R.id.tl_no_of_floors_above_ground)
    TextInputLayout tl_no_of_floors_above_ground;

    @BindView(R.id.et_basement)
    EditText et_basement;

    @BindView(R.id.tl_basement)
    TextInputLayout tl_basement;

    @BindView(R.id.id_radio_fire_exit)
    CheckBox cb_fire_exit;

    @BindView(R.id.spinner_steel)
    Spinner spinner_steel;

    @BindView(R.id.ll_steel_grade)
    LinearLayout ll_steel_grade;

    @BindView(R.id.id_ground_slope_more_than)
    CheckBox cb_ground_slope_more_than;

    @BindView(R.id.checkbox_hill_slope)
    CheckBox checkbox_hill_slope;

    @BindView(R.id.checkbox_liquefiable)
    CheckBox checkbox_liquefiable;

    @BindView(R.id.recyclerview_remarks)
    RecyclerView recyclerview_remarks;

    @BindView(R.id.editText_additional_remarks)
    EditText editText_additional_remarks;

    @BindView(R.id.editText_special_remarks)
    EditText editText_special_remarks;

    @BindView(R.id.spinner_marketability)
    Spinner spinner_marketability;

    @BindView(R.id.ll_marketability)
    LinearLayout ll_marketability;


    @BindView(R.id.tl_additional_document)
    LinearLayout tl_additional_document;

    @BindView(R.id.et_additional_document)
    EditText et_additional_document;

    Recycler_remarks_adapter madapter_remarks;


    @BindView(R.id.textview_save_top_dashboard)
    TextView textview_save_top_dashboard;
    @BindView(R.id.textview_save_bottom_dashboard)
    TextView textview_save_bottom_dashboard;

    @BindView(R.id.textview_save_top)
    TextView textview_save_top;
    @BindView(R.id.textview_save_bottom)
    TextView textview_save_bottom;


    @BindView(R.id.fsProgressStatus)
    ProgressBar fsProgressBarStatus;

    @BindView(R.id.same_as_doc_boundary_checkbox)
    CheckBox same_as_doc_boundary_checkbox;

    public FieldsInspectionDetails() {
        // Required empty public constructor
    }

    @SuppressLint("StaticFieldLeak")
    public static LinearLayout my_focuslayout;


    int fsProgressCount = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FieldsInspectionDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static FieldsInspectionDetails newInstance(String param1, String param2) {
        FieldsInspectionDetails fragment = new FieldsInspectionDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        general = new General(getActivity());
        uiVisiblityCount = 0;
        fsProgressCount = 0;
        mContext = this.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fields_inspection_details, container, false);
        ButterKnife.bind(this, view);

        instance = this;
        initValues(view);
        checkbox_boundary();
        editText_db_north.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_db_north_str = charSequence.toString();
                checkbox_boundary();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_db_south.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_db_south_str = charSequence.toString();
                checkbox_boundary();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText_db_east.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_db_east_str = charSequence.toString();
                checkbox_boundary();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText_db_west.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_db_west_str = charSequence.toString();
                checkbox_boundary();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cardView.setOnClickListener(view1 -> {

            if (!isImageClicked) {
                isImageClicked = true;
                textView.setTextColor(getResources().getColor(R.color.black));
                cardView.setCardBackgroundColor(getResources().getColor(R.color.slightgrey));
                imageView.setImageResource(R.drawable.icon_less_subsection);
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                isImageClicked = false;
                textView.setTextColor(getResources().getColor(R.color.white));
                cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                imageView.setImageResource(R.drawable.icon_more_subsection);
                linearLayout.setVisibility(View.GONE);
            }

        });
        cardViewLocality.setOnClickListener(view1 -> {

            if (!isLocalityImgClicked) {
                isLocalityImgClicked = true;
                txtLocality.setTextColor(getResources().getColor(R.color.black));
                cardViewLocality.setCardBackgroundColor(getResources().getColor(R.color.slightgrey));
                imgLocality.setImageResource(R.drawable.icon_less_subsection);
                llLocalitySubSection.setVisibility(View.VISIBLE);
            } else {
                isLocalityImgClicked = false;
                txtLocality.setTextColor(getResources().getColor(R.color.white));
                cardViewLocality.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                imgLocality.setImageResource(R.drawable.icon_more_subsection);
                llLocalitySubSection.setVisibility(View.GONE);
            }
        });
        cardViewProperty.setOnClickListener(view1 -> {

            if (!isPropertyImgClicked) {
                isPropertyImgClicked = true;
                txtProperty.setTextColor(getResources().getColor(R.color.black));
                cardViewProperty.setCardBackgroundColor(getResources().getColor(R.color.slightgrey));
                imgProperty.setImageResource(R.drawable.icon_less_subsection);
                llPropertySubSection.setVisibility(View.VISIBLE);
            } else {
                isPropertyImgClicked = false;
                txtProperty.setTextColor(getResources().getColor(R.color.white));
                cardViewProperty.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                imgProperty.setImageResource(R.drawable.icon_more_subsection);
                llPropertySubSection.setVisibility(View.GONE);
            }
        });
        cardViewPropertyDetails.setOnClickListener(view1 -> {

            if (!isPropertyDetailsImgClicked) {
                isPropertyDetailsImgClicked = true;
                txtPropertyDetails.setTextColor(getResources().getColor(R.color.black));
                cardViewPropertyDetails.setCardBackgroundColor(getResources().getColor(R.color.slightgrey));
                imgPropertyDetails.setImageResource(R.drawable.icon_less_subsection);
                llPropertyDetailsSubSection.setVisibility(View.VISIBLE);
            } else {
                isPropertyDetailsImgClicked = false;
                txtPropertyDetails.setTextColor(getResources().getColor(R.color.white));
                cardViewPropertyDetails.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                imgPropertyDetails.setImageResource(R.drawable.icon_more_subsection);
                llPropertyDetailsSubSection.setVisibility(View.GONE);
            }
        });
        cardViewNdmaParamter.setOnClickListener(view1 -> {

            if (!isNdmaParamterImgClicked) {
                isNdmaParamterImgClicked = true;
                txtNdmaParamter.setTextColor(getResources().getColor(R.color.black));
                cardViewNdmaParamter.setCardBackgroundColor(getResources().getColor(R.color.slightgrey));
                imgNdmaParamter.setImageResource(R.drawable.icon_less_subsection);
                llNdmaParamter.setVisibility(View.VISIBLE);
            } else {
                isNdmaParamterImgClicked = false;
                txtNdmaParamter.setTextColor(getResources().getColor(R.color.white));
                cardViewNdmaParamter.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                imgNdmaParamter.setImageResource(R.drawable.icon_more_subsection);
                llNdmaParamter.setVisibility(View.GONE);
            }
        });
        cardViewFairMarketValuationLayout.setOnClickListener(view1 -> {

            if (!isFair_market_valuationImgClicked) {
                isFair_market_valuationImgClicked = true;
                txtFairMarketValuationLayout.setTextColor(getResources().getColor(R.color.black));
                cardViewFairMarketValuationLayout.setCardBackgroundColor(getResources().getColor(R.color.slightgrey));
                imgFairMarketValuation.setImageResource(R.drawable.icon_less_subsection);
                llFairMarketValuationLayoutSubSection.setVisibility(View.VISIBLE);
            } else {
                isFair_market_valuationImgClicked = false;
                txtFairMarketValuationLayout.setTextColor(getResources().getColor(R.color.white));
                cardViewFairMarketValuationLayout.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                imgFairMarketValuation.setImageResource(R.drawable.icon_more_subsection);
                llFairMarketValuationLayoutSubSection.setVisibility(View.GONE);
            }
        });

        iv_calender.setOnClickListener(view12 -> showCalender());

        initShowUIApi();


        // TODO -  call the mandatory_valiadation
        if (Singleton.getInstance().enable_validation_error) {
            fsMandatoryInputData();
        }

        return view;
    }

    private void initValues(View view) {

        SettingsUtils.init(mContext);
        if (MyApplication.getAppContext() != null) {
            appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        }
        caseid = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");

        if (!General.isEmpty(caseid)) {
            caseid_int = Integer.parseInt(caseid);
        }
        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);


        /******
         * Set the property category type
         * ******/
        String property_cate_id = SettingsUtils.getInstance().getValue(SettingsUtils.PropertyCategoryId, "");
        String property_bank = SettingsUtils.getInstance().getValue(SettingsUtils.Case_Bank, "");

        Log.e(TAG, "initValues: " + SettingsUtils.getInstance().getValue(SettingsUtils.PropertyCategoryId, ""));
        Log.e(TAG, "case_bank: " + SettingsUtils.getInstance().getValue(SettingsUtils.Case_Bank, ""));
        Log.e(TAG, "case_bank_branch: " + SettingsUtils.getInstance().getValue(SettingsUtils.Case_BankBranch, ""));

        setPropertyType(property_cate_id);
        setPropertyBank(property_bank);


        textview_save_top.setOnClickListener(this);
        textview_save_bottom.setOnClickListener(this);
        textview_save_top_dashboard.setOnClickListener(this);
        textview_save_bottom_dashboard.setOnClickListener(this);


        same_as_doc_boundary_checkbox.setOnCheckedChangeListener(this);

        my_focuslayout = (LinearLayout) view.findViewById(R.id.my_focuslayout);
        my_focuslayout.requestFocus();
    }

    private void initGeneral() {
        Log.d("Bank_template", isNPA.toString());

        if (general.getUIVisibility("Purpose")) {
            llPurpose.setVisibility(View.VISIBLE);
            initPurpose();
        } else {
            llPurpose.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Loan Type")) {
            typeOfLoan();
            llTypeOfLoan.setVisibility(View.VISIBLE);
        } else {
            llTypeOfLoan.setVisibility(View.GONE);
        }



        /* Applicant/Borrower */
        if (general.getUIVisibility("Applicant Name")) {
            initNameOfBorrower();
            llNameOfBorrower.setVisibility(View.VISIBLE);
        } else llNameOfBorrower.setVisibility(View.GONE);

        if (general.getUIVisibility("Name Of Seller")) {
            initNameOfSeller();
            llNameOfSeller.setVisibility(View.VISIBLE);
        } else llNameOfSeller.setVisibility(View.GONE);

        if (general.getUIVisibility("Name Of Owner")) {
            initNameOfOwner();
            llNameOfOwner.setVisibility(View.VISIBLE);
        } else llNameOfOwner.setVisibility(View.GONE);


        if (general.getUIVisibility("Type Of Seller")) {
            til_seller_type.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().indProperty.getTypeofSeller() != null && !general.isEmpty(Singleton.getInstance().indProperty.getTypeofSeller())) {
                fsProgressCount = fsProgressCount + 1;
                et_seller_type.setText(Singleton.getInstance().indProperty.getTypeofSeller());
            } else et_seller_type.setText("");
        } else til_seller_type.setVisibility(View.GONE);

        if (general.getUIVisibility("Property Contact Person Name, Property Contact Person Mobile Number") && general.getSectionID("Property Contact Person Name, Property Contact Person Mobile Number") == 1) {
            ll_contact_info.setVisibility(View.VISIBLE);

            if (!general.isEmpty(Singleton.getInstance().aCase.getContactPersonName())) {
                etPersonName.setText(Singleton.getInstance().aCase.getContactPersonName());
            }
            if (!general.isEmpty(Singleton.getInstance().aCase.getContactPersonNumber()))
                etPersonNo.setText(Singleton.getInstance().aCase.getContactPersonNumber());

            if (!general.isEmpty(Singleton.getInstance().aCase.getContactPersonName()) && !general.isEmpty(Singleton.getInstance().aCase.getContactPersonNumber())) {
                fsProgressCount = fsProgressCount + 1;
            }

        } else ll_contact_info.setVisibility(View.GONE);


        if (general.getUIVisibility("Site Visit Date")) {
            ll_calender.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().aCase.getSiteVisitDate() != null) {
                visitDate = general.siteVisitDate(Singleton.getInstance().aCase.getSiteVisitDate());
                if (visitDate != null && !visitDate.isEmpty()) {
                    date_value.setText(visitDate);
                    fsProgressCount = fsProgressCount + 1;
                }

            }
        } else ll_calender.setVisibility(View.GONE);


        if (general.getUIVisibility("Finnone ID")) {
            if (!general.isEmpty(Singleton.getInstance().aCase.getBankReferenceNO())) {
                et_finnonId.setText(Singleton.getInstance().aCase.getBankReferenceNO());
                tl_finnonId.setVisibility(View.VISIBLE);
                fsProgressCount = fsProgressCount + 4;
            }

        } else tl_finnonId.setVisibility(View.GONE);


    }

    private void checkbox_boundary() {
        if ((General.isEmpty(editText_db_east_str)) && (General.isEmpty(editText_db_west_str)) && (General.isEmpty(editText_db_north_str)) && (General.isEmpty(editText_db_south_str))) {
            same_as_doc_boundary_checkbox.setVisibility(View.INVISIBLE);
        } else {
            same_as_doc_boundary_checkbox.setVisibility(View.VISIBLE);
        }
    }

    private void initLocality() {

        if (general.getUIVisibility("Complete Property Address")) {
            tl_complete_address.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().aCase.getPropertyAddress() != null) {
                editText_addr_perdoc.setText(Singleton.getInstance().aCase.getPropertyAddress());
            }
        } else tl_complete_address.setVisibility(View.GONE);

        if (property_type.equalsIgnoreCase("flat")) {
            tl_plot_no.setVisibility(View.GONE);
            tl_unit_no.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().property.getUnitNo() != null) {
                editText_unit_no.setText(Singleton.getInstance().property.getUnitNo());
                fsProgressCount = fsProgressCount + 1;
            }
        } else {
            if (general.getUIVisibility("Plot No.")) {
                tl_unit_no.setVisibility(View.GONE);
                tl_plot_no.setVisibility(View.VISIBLE);
                if (Singleton.getInstance().property.getPlotNo() != null) {
                    editText_plotno.setText(Singleton.getInstance().property.getPlotNo());
                    fsProgressCount = fsProgressCount + 1;
                }

            } else tl_plot_no.setVisibility(View.GONE);

        }


        if (general.getUIVisibility("Survey No.")) {
            tl_survey_no.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getSurveyNo())) {
                fsProgressCount = fsProgressCount + 4;
                editText_surveyno.setText(Singleton.getInstance().property.getSurveyNo().trim());
            }
        } else tl_survey_no.setVisibility(View.GONE);


        if (general.getUIVisibility("Village Name ")) {
            tl_village_post.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().aCase.getVillageName() != null) {
                et_village_post.setText(Singleton.getInstance().aCase.getVillageName());
                fsProgressCount = fsProgressCount + 1;
            }
        } else {
            tl_village_post.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Taluka/Mandal/ Tehsil ")) {
            tl_taluka.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().aCase.getTaluka() != null) {
                et_taluka.setText(Singleton.getInstance().aCase.getTaluka());
                fsProgressCount = fsProgressCount + 1;
            }
        } else {
            tl_taluka.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("District")) {
            til_district.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().aCase.getDistrict() != null) {
                et_district.setText("" + Singleton.getInstance().aCase.getDistrict());
                fsProgressCount = fsProgressCount + 1;
            }
        } else til_district.setVisibility(View.GONE);

        if (general.getUIVisibility("Landmark")) {
            tl_landmark.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().property.getLandmark() != null)
                fsProgressCount = fsProgressCount + 1;
            editText_landmark.setText(Singleton.getInstance().property.getLandmark());
        } else {
            tl_landmark.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Pincode")) {
            tl_pincode.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().aCase.getPincode() != 0) {
                etPinCode.setText("" + Singleton.getInstance().aCase.getPincode());
                fsProgressCount = fsProgressCount + 1;
            }
        } else tl_pincode.setVisibility(View.GONE);





        /* As per document */

        if (Singleton.getInstance().property.getSameAsDocumentBoundary() != null)
            same_as_doc_boundary_checkbox.setChecked(Singleton.getInstance().property.getSameAsDocumentBoundary());

        if (general.getUIVisibility("EastDoc")) {
            asPerDoc = true;
            editText_db_east.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getDocBoundryEast())) {
                editText_db_east.setText(Singleton.getInstance().property.getDocBoundryEast());
                editText_db_east_str = Singleton.getInstance().property.getDocBoundryEast();
            }
        } else {
            editText_db_east.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("WestDoc")) {
            asPerDoc = true;
            editText_db_west.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getDocBoundryWest())) {
                editText_db_west.setText(Singleton.getInstance().property.getDocBoundryWest());
                editText_db_west_str = Singleton.getInstance().property.getDocBoundryWest();
            }
        } else {
            editText_db_west.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("NorthDoc")) {
            asPerDoc = true;
            editText_db_north.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getDocBoundryNorth())) {
                editText_db_north.setText(Singleton.getInstance().property.getDocBoundryNorth());
                editText_db_north_str = Singleton.getInstance().property.getDocBoundryNorth();

            }
        } else {
            editText_db_north.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("SouthDoc")) {
            asPerDoc = true;
            editText_db_south.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getDocBoundrySouth())) {
                editText_db_south.setText(Singleton.getInstance().property.getDocBoundrySouth());
                editText_db_south_str = Singleton.getInstance().property.getDocBoundrySouth();

            }
        } else {
            editText_db_south.setVisibility(View.GONE);
        }

        if (asPerDoc) {
            // fsProgressCount = fsProgressCount + 1;
            ll_as_per_doc.setVisibility(View.VISIBLE);
        } else ll_as_per_doc.setVisibility(View.GONE);



        /* As per site - building / house */
        if (general.getUIVisibility("EastSite")) {
            asPerSite = true;
            editText_ab_east.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getBoundryEast()))
                editText_ab_east.setText(Singleton.getInstance().property.getBoundryEast());
        } else {
            editText_ab_east.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("WestSite")) {
            asPerSite = true;
            editText_ab_west.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getBoundryWest()))
                editText_ab_west.setText(Singleton.getInstance().property.getBoundryWest());
        } else {
            editText_ab_west.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("NorthSite")) {
            asPerSite = true;
            editText_ab_north.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getBoundryNorth()))
                editText_ab_north.setText(Singleton.getInstance().property.getBoundryNorth());
        } else {
            editText_ab_north.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("SouthSite")) {
            asPerSite = true;
            editText_ab_south.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getBoundrySouth()))
                editText_ab_south.setText(Singleton.getInstance().property.getBoundrySouth());
        } else {
            editText_ab_south.setVisibility(View.GONE);
        }

        if (asPerSite) {
            //fsProgressCount = fsProgressCount + 1;
            ll_as_per_site.setVisibility(View.VISIBLE);
        } else {
            ll_as_per_site.setVisibility(View.GONE);
        }


        if (property_type.equalsIgnoreCase("flat") || property_type.equalsIgnoreCase("penthouse")) {
            initFlatType();
        }

        if (general.getUIVisibility("Type of locality")) {
            ll_locality.setVisibility(View.VISIBLE);
            localityDataMapping();
        } else {
            ll_locality.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Proximity - civic, social commercial facilities:")) {
            ll_proximity.setVisibility(View.VISIBLE);
            initProximityRecyclerView();
        } else {
            ll_proximity.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Habitation % in locality")) {
            tl_habitation.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getHabitationPercentageinLocality())) {
                fsProgressCount = fsProgressCount + 1;
                et_habitation.setText(Singleton.getInstance().property.getHabitationPercentageinLocality());
            }
        } else {
            tl_habitation.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Adverse feature nearby")) {
            til_adverse_nearby.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getAdverseFeatureNearby())) {
                fsProgressCount = fsProgressCount + 1;
                et_adverse_nearby.setText(Singleton.getInstance().property.getAdverseFeatureNearby());
            }
        } else {
            til_adverse_nearby.setVisibility(View.GONE);
        }
    }

    private void initProximityRecyclerView() {
        // list = getIntent().getStringArrayListExtra("list");
        //To show at least one row
        if (Singleton.getInstance().proximities != null)
            if (Singleton.getInstance().proximities.size() > 0) {
                list = new ArrayList<>();
                for (int i = 0; i < Singleton.getInstance().proximities.size(); i++) {
                    Proximity stepsModel = new Proximity();
                    int id = Singleton.getInstance().proximities.get(i).getProximityId();
                    String distid = Singleton.getInstance().proximities.get(i).getProximityDistance();

                    stepsModel.setId(Singleton.getInstance().proximities.get(i).getId());
                    stepsModel.setCaseId(Singleton.getInstance().proximities.get(i).getCaseId());
                    stepsModel.setProximityId(Singleton.getInstance().proximities.get(i).getProximityId());
                    stepsModel.setProximityName(Singleton.getInstance().proximities.get(i).getProximityName());
                    stepsModel.setProximityDistance(Singleton.getInstance().proximities.get(i).getProximityDistance());
                    list.add(stepsModel);
                    fsProgressCount = fsProgressCount + 1;
                }

            } else {
                Singleton.getInstance().proximities.clear();
                if (list == null || list.size() == 0) {
                    list = new ArrayList<>();
                    //  list.add("");
                    Proximity stepsModel = new Proximity();
                    stepsModel.setProximityId(5);
                    list.add(stepsModel);
                    Singleton.getInstance().proximities.add(stepsModel);
                    Proximity stepsModel2 = new Proximity();
                    stepsModel2.setProximityId(1);
                    list.add(stepsModel2);
                    Singleton.getInstance().proximities.add(stepsModel);
                }
            }

        listAdapter = new ProximityAdapter(list, getActivity());
        llm = new LinearLayoutManager(getActivity());

        //Setting the adapter
        recyclerview_proxmity.setAdapter(listAdapter);
        recyclerview_proxmity.setLayoutManager(llm);
    }

    private void initPropertyValues() {


        if (general.getUIVisibility("Type of property") && general.getSectionID("Type of property") == 3) {
            tl_etpropertyTypeProperty.setVisibility(View.VISIBLE);
            typeOfProperty(3);
        } else {
            tl_etpropertyTypeProperty.setVisibility(View.GONE);
        }
//        typeOfProprty();

        if (general.getUIVisibility("Ownership Type")) {
            ll_ownership.setVisibility(View.VISIBLE);
            initOwnerShipType();
        } else {
            ll_ownership.setVisibility(View.GONE);
        }

        if(general.getUIVisibility("Land Approved for")){
            initLandApprovedFor();
        }


        if (general.getUIVisibility("Is construction done as per sanctioned plan")) {
            checkbox_sanctioned_plan.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().property.getIsConstructionDoneAsPerSanctionedPlan() != null) {
                fsProgressCount = fsProgressCount + 1;
                checkbox_sanctioned_plan.setChecked(Singleton.getInstance().property.getIsConstructionDoneAsPerSanctionedPlan());
            }
        } else {
            checkbox_sanctioned_plan.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Ground Coverage")) {
            tl_ground_coverage.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().indProperty.getGroundCoverage())) {
                fsProgressCount = fsProgressCount + 1;
                et_ground.setText(Singleton.getInstance().indProperty.getGroundCoverage());
            }
        } else tl_ground_coverage.setVisibility(View.GONE);


        if (general.getUIVisibility("Architect / Engineer License No.")) {
            layout_engineer_license.setVisibility(View.VISIBLE);
            if (!General.isEmpty(Singleton.getInstance().aCase.getArchitectEngineerLicenseNo())) {
                et_engineer_license.setText("" + Singleton.getInstance().aCase.getArchitectEngineerLicenseNo());
                fsProgressCount = fsProgressCount + 1;
            } else if (!General.isEmpty(Singleton.getInstance().indProperty.getArchitectEngineerLicenseNo()) && General.isEmpty(Singleton.getInstance().aCase.getArchitectEngineerLicenseNo())) {
                et_engineer_license.setText("" + Singleton.getInstance().indProperty.getArchitectEngineerLicenseNo());
                fsProgressCount = fsProgressCount + 1;
            }
        } else layout_engineer_license.setVisibility(View.GONE);

        /* is there any demolition risk on property*/
        if (general.getUIVisibility(" Is Property in demolition risk?")) {
            llPropertyRbtn.setVisibility(View.VISIBLE);
            demolishUIMapping();
        } else {
            llPropertyRbtn.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Authority")) {
            tl_authority.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().aCase.getApprovedPlanApprovingAuthority()!= null) {
                if(!General.isEmpty(Singleton.getInstance().aCase.getApprovedPlanApprovingAuthority())){
                    fsProgressCount = fsProgressCount + 2;
                    et_authority.setText(Singleton.getInstance().aCase.getApprovedPlanApprovingAuthority());
                }

            }} else {
            tl_authority.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Prepared By")) {
            tl_plan_prepared_by.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().aCase.getApprovedPlanPreparedBy() != null  && !General.isEmpty(Singleton.getInstance().aCase.getApprovedPlanPreparedBy())){
                fsProgressCount = fsProgressCount + 1;
                et_plan_prepared_by.setText(Singleton.getInstance().aCase.getApprovedPlanPreparedBy());
            }
        } else {
            tl_plan_prepared_by.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Plan Number, Date")) {
            ll_planno_date.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().aCase.getApprovedPlanDate() != null) {
                if (!General.isEmpty(Singleton.getInstance().aCase.getApprovedPlanNumber())) {
                    et_approved_plan_no.setText(Singleton.getInstance().aCase.getApprovedPlanNumber());
                }

                if (!General.isEmpty(Singleton.getInstance().aCase.getApprovedPlanDate())) {
                    et_approved_plan_date.setText(Singleton.getInstance().aCase.getApprovedPlanDate());
                }
            }
        } else ll_planno_date.setVisibility(View.GONE);

        if (general.getUIVisibility("Property Contact Person Name, Property Contact Person Mobile Number") && general.getSectionID("Property Contact Person Name, Property Contact Person Mobile Number") == 3) {
            ll_contact_info_property.setVisibility(View.VISIBLE);

            if (!general.isEmpty(Singleton.getInstance().aCase.getContactPersonName())) {
                etPersonName_property.setText(Singleton.getInstance().aCase.getContactPersonName());
            }
            if (!general.isEmpty(Singleton.getInstance().aCase.getContactPersonNumber()))
                etPersonNo_property.setText(Singleton.getInstance().aCase.getContactPersonNumber());

            if (!general.isEmpty(Singleton.getInstance().aCase.getContactPersonName()) && !general.isEmpty(Singleton.getInstance().aCase.getContactPersonNumber())) {
                fsProgressCount = fsProgressCount + 1;
            }

        } else ll_contact_info_property.setVisibility(View.GONE);

        if (property_type.equalsIgnoreCase("land")) {
            checkbox_isproperty_demolish.setVisibility(View.GONE);
        }


    }

    private void initLandApprovedFor() {

        ArrayAdapter<Land> landArrayAdapter = new ArrayAdapter<Land>(mContext, R.layout.row_spinner_item, Singleton.getInstance().land_list) {
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
        landArrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_landapprovedfor.setAdapter(landArrayAdapter);
        spinner_landapprovedfor.setOnTouchListener(this);

        Integer id = Singleton.getInstance().property.getTypeOfLandId();
        if (!general.isEmpty(String.valueOf(id))) {
            fsProgressCount = fsProgressCount + 1;
            spinner_landapprovedfor.setSelection(id);
        }
    }

    private void initOwnerShipType() {

        ArrayAdapter<OwnershipType> adapterTenure = new ArrayAdapter<OwnershipType>(mContext, R.layout.row_spinner_item, Singleton.getInstance().ownershipTypes_list) {
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
        adapterTenure.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_select_tenure_ownership.setAdapter(adapterTenure);
        spinner_select_tenure_ownership.setOnTouchListener(this);

        Integer id = Singleton.getInstance().aCase.getTypeofOwnershipDd();
        if (!general.isEmpty(String.valueOf(id))) {
            fsProgressCount = fsProgressCount + 1;
            spinner_select_tenure_ownership.setSelection(id);
        }
    }


    private void regionalDevelopment() {
        // spinner - Locality
        ArrayAdapter<Locality> localityArrayAdapter = new ArrayAdapter<Locality>(getActivity(), R.layout.row_spinner_item, Singleton.getInstance().localities_list) {
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
        localityArrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_typeoflocality.setAdapter(localityArrayAdapter);
        spinner_typeoflocality.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().property.getTypeLocalityId()))) {
            Log.e("spinner_Locality", "::: " + Singleton.getInstance().property.getTypeLocalityId());
            if (Singleton.getInstance().property.getTypeLocalityId() != 0) {
                ArrayList<Locality> qualityConstructions_ = new ArrayList<>();
                qualityConstructions_ = Singleton.getInstance().localities_list;
                for (int x = 0; x < qualityConstructions_.size(); x++) {
                    if (qualityConstructions_.get(x).getTypeLocalityId() == Singleton.getInstance().property.getTypeLocalityId()) {
                        spinner_typeoflocality.setSelection(x);
                    }
                }
            }
        }
    }

    private void initPropertyDetailsValue() {
        if (general.getUIVisibility("Average Construction Percentage") && general.getSectionID("Average Construction Percentage") == 4) {
            tl_avg_construction_per.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getAverageConstructionPercentage())) {
                fsProgressCount = fsProgressCount + 1;
                etAverageConstruction.setText(Singleton.getInstance().indPropertyValuation.getAverageConstructionPercentage());
            }
        } else tl_avg_construction_per.setVisibility(View.GONE);


        if (general.getUIVisibility("Is Lift Available In Building ")) {
            checkbox_lift_in_building.setVisibility(View.VISIBLE);
            fsProgressCount = fsProgressCount + 1;
            if (Singleton.getInstance().indProperty.getLiftInBuilding() != null)
                checkbox_lift_in_building.setChecked(Singleton.getInstance().indProperty.getLiftInBuilding());
        } else checkbox_lift_in_building.setVisibility(View.GONE);


        if (general.getUIVisibility("Recommendation Percentage") && general.getSectionID("Recommendation Percentage") == 4) {
            tl_recommendation_per.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getRecommendationPercentage())) {
                fsProgressCount = fsProgressCount + 1;
                etRecommendationPercentage.setText(Singleton.getInstance().indPropertyValuation.getRecommendationPercentage());
            }
        } else tl_recommendation_per.setVisibility(View.GONE);


        /*if(general.getUIVisibility("Average Construction Percentage")){
            tl_construction_stage.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().indProperty.getDescriptionofConstructionStage())) {
                et_construction_stage.setText(Singleton.getInstance().indProperty.getDescriptionofConstructionStage());
            }
        }else{
            tl_construction_stage.setVisibility(View.GONE);
        }*/
        if (general.getUIVisibility("Average construction percentage,Description of construction stage") && general.getSectionID("Average construction percentage,Description of construction stage") == 4) {
            if (property_type.equalsIgnoreCase("land")) {
                tl_construction_stage.setVisibility(View.GONE);
            } else {
                if (!general.isEmpty(Singleton.getInstance().indProperty.getDescriptionofConstructionStage())) {
                    fsProgressCount = fsProgressCount + 1;
                    et_construction_stage.setText(Singleton.getInstance().indProperty.getDescriptionofConstructionStage());
                }
            }
        } else {
            tl_construction_stage.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Type Of Structure") && general.getSectionID("Type Of Structure") == 4) {
            type_of_construction_property_dtls.setVisibility(View.VISIBLE);
            textview_exter_struc_text_property_dtls.setVisibility(View.VISIBLE);
            function_exterior_structure_property_dtls();
        } else {
            type_of_construction_property_dtls.setVisibility(View.GONE);
            textview_exter_struc_text_property_dtls.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Flooring") && general.getSectionID("Flooring") == 4) {
            llFlooring_property_dtls.setVisibility(View.VISIBLE);
            function_interior_floor_property_dtls();
        } else {
            llFlooring_property_dtls.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Maintenance Of Building")) {
            ll_maintenace_of_building.setVisibility(View.VISIBLE);
            initMaintanceBuilding();
        } else ll_maintenace_of_building.setVisibility(View.GONE);


        if (general.getUIVisibility("Presently Occupied")) {
            ll_presently_occupied.setVisibility(View.VISIBLE);
            function_PresentlyOccupied();
        } else ll_presently_occupied.setVisibility(View.GONE);

        if (general.getUIVisibility("Number of Multiple kitchens")) {
            tl_no_of_multiple_kitchens.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().indProperty.getNumberofMultiplekitchens())) {
                fsProgressCount = fsProgressCount + 1;
                et_multiple_kitchen.setText(Singleton.getInstance().indProperty.getNumberofMultiplekitchens());
            }
        } else tl_no_of_multiple_kitchens.setVisibility(View.GONE);


        if (general.getUIVisibility("Name of Occupant")) {
            tl_name_of_occupant.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getNameofOccupant())) {
                fsProgressCount = fsProgressCount + 1;
                et_occupant_name.setText(Singleton.getInstance().property.getNameofOccupant());
            }
        } else tl_name_of_occupant.setVisibility(View.GONE);
    }

    private void function_PresentlyOccupied() {


        textview_persently_occupied_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(textview_persently_occupied_text);
                showdialog_dynamic("presently_occupied");
            }
        });

        // clear the array and set the floor list in array
        // clear the array and set the floor list in array
        PresentlyOccupied_list = new ArrayList<>();
        PresentlyOccupied_list = Singleton.getInstance().presentlyOccupied_list;

        Singleton.getInstance().PresentlyOccupied_name.clear();
        Singleton.getInstance().PresentlyOccupied_id.clear();

        // check Floor Dropdown is empty
        if (PresentlyOccupied_list.size() > 0) {
            String getIntFloorId = Singleton.getInstance().property.getPresentlyOccupied();
            // check selected array is empty
            if (!general.isEmpty(getIntFloorId)) {
                // selected array convert to comma separate array
                List<String> PresentlyOccupied_list_selected_id_commo = new ArrayList<>();
                PresentlyOccupied_list_selected_id_commo = new ArrayList<String>(Arrays.asList(getIntFloorId.split(",")));
                if (PresentlyOccupied_list_selected_id_commo.size() > 0) {
                    // Loop the Floor and get ID > check If selected array contains ID
                    for (int x = 0; x < PresentlyOccupied_list.size(); x++) {
                        if (PresentlyOccupied_list_selected_id_commo.toString().contains("" + PresentlyOccupied_list.get(x).getPresentlyOccupiedId())) {
                            for (int y = 0; y < PresentlyOccupied_list_selected_id_commo.size(); y++) {
                                // Loop the selected array > If the selected array ID equal to Floor ID > save ID and name to Instance
                                int one_by_one_id = Integer.parseInt(PresentlyOccupied_list_selected_id_commo.get(y));
                                if (PresentlyOccupied_list.get(x).getPresentlyOccupiedId() == one_by_one_id) {
                                    Singleton.getInstance().PresentlyOccupied_id.add(PresentlyOccupied_list.get(x).getPresentlyOccupiedId());
                                    Singleton.getInstance().PresentlyOccupied_name.add(PresentlyOccupied_list.get(x).getName());
                                    fsProgressCount = fsProgressCount + 1;
                                }
                            }
                            textview_persently_occupied_text.setText(general.remove_array_brac_and_space(Singleton.getInstance().PresentlyOccupied_name.toString()));
                        }
                    }
                }
            } else {
                textview_persently_occupied_text.setText(getResources().getString(R.string.select));
            }
        }
    }


    private void initNDMAParameters() {

        if (general.getUIVisibility("Type of property") && general.getSectionID("Type of property") == 5) {
            tl_etpropertyTypeNdma.setVisibility(View.VISIBLE);
            typeOfProperty(5);
        } else if (general.getUIVisibility("Property Type") && general.getSectionID("Property Type") == 5) {
            tl_etpropertyTypeNdma.setVisibility(View.VISIBLE);
            typeOfProperty(5);

        } else {
            tl_etpropertyTypeNdma.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Type of Masonry")) {
            ll_masonry.setVisibility(View.VISIBLE);
            typeOfMasonry();
        } else {
            ll_masonry.setVisibility(View.GONE);
        }

        if (property_type.equalsIgnoreCase("land")) {
            type_of_construction.setVisibility(View.GONE);
            textview_exter_struc_text.setVisibility(View.GONE);
        }
        if (general.getUIVisibility("Type Of Structure") && general.getSectionID("Type Of Structure") == 5) {
            type_of_construction.setVisibility(View.VISIBLE);
            textview_exter_struc_text.setVisibility(View.VISIBLE);
            function_exterior_structure();
        } else {
            type_of_construction.setVisibility(View.GONE);
            textview_exter_struc_text.setVisibility(View.GONE);
        }


        textview_exter_struc_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(textview_exter_struc_text);
                showdialog_dynamic("exter_stru");
            }
        });

        if (general.getUIVisibility("Flooring") && general.getSectionID("Flooring") == 5) {
            llFlooring.setVisibility(View.VISIBLE);
            function_interior_floor();
        } else {
            llFlooring.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Type of Mortar")) {
            ll_typeOfMotor.setVisibility(View.VISIBLE);
            initSpinnerMortar();
        } else ll_typeOfMotor.setVisibility(View.GONE);


        if (general.getUIVisibility("Concrete Grade")) {
            ll_concrete.setVisibility(View.VISIBLE);
            initiateConcreteGradeDropDownFields();
        } else {
            ll_concrete.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Whether expansion joint available")) {
            checkbox_expansion_joint.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().indProperty.getWhetherExpansionJointAvailable() != null)
                checkbox_expansion_joint.setChecked(Singleton.getInstance().indProperty.getWhetherExpansionJointAvailable());
        } else checkbox_expansion_joint.setVisibility(View.GONE);


        if (general.getUIVisibility("Environment Exposure Condition")) {
            ll_env.setVisibility(View.VISIBLE);
            initiateEnvExposureConditionDropDownFields();
        } else ll_env.setVisibility(View.GONE);


        if (general.getUIVisibility("Cyclone zone with Speed")) {
            tl_cyclone_zone.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getCycloneZone())) {
                fsProgressCount = fsProgressCount + 1;
                et_cyclone_zone.setText(Singleton.getInstance().property.getCycloneZone());
            }
        } else {
            tl_cyclone_zone.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Seismic Zone")) {
            tl_seismic_zone.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getSeismicZone())) {
                fsProgressCount = fsProgressCount + 1;
                et_seismic.setText(Singleton.getInstance().property.getSeismicZone());
            }
        } else {
            tl_seismic_zone.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Roofing")) {
            ll_roof_type.setVisibility(View.VISIBLE);
            function_interior_roof();
        } else ll_roof_type.setVisibility(View.GONE);


        if (general.getUIVisibility("Flood Prone Zone")) {
            tl_flood_prone_area.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getFloodProneZone())) {
                fsProgressCount = fsProgressCount + 1;
                et_flood_zone.setText(Singleton.getInstance().property.getFloodProneZone());
            }
        } else tl_flood_prone_area.setVisibility(View.GONE);

        if (general.getUIVisibility("Is projected part available")) {
            checkbox_projected_part.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().indProperty.getProjectedPartAvailable() != null) {
                fsProgressCount = fsProgressCount + 1;
                checkbox_projected_part.setChecked(Singleton.getInstance().indProperty.getProjectedPartAvailable());
            }
        } else {
            checkbox_projected_part.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Soil Type")) {
            ll_type_soil.setVisibility(View.VISIBLE);
            initiateSoilType();
        } else {
            ll_type_soil.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Plan Aspect Ratio/ Length & Width")) {
            tl_plan_aspect_ratio.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().indProperty.getPlanAspectRatio())) {
                fsProgressCount = fsProgressCount + 1;
                et_aspect_ratio.setText(Singleton.getInstance().indProperty.getPlanAspectRatio());
            }
        } else {
            tl_plan_aspect_ratio.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Coastal Regulatory Zone")) {
            tl_costal_regulatory_zone.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().property.getCoastalRegulatoryZone())) {
                fsProgressCount = fsProgressCount + 1;
                et_regulatory_zone.setText(Singleton.getInstance().property.getCoastalRegulatoryZone());
            }
        } else {
            tl_costal_regulatory_zone.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Footing foundation type")) {
            ll_footing_foundation.setVisibility(View.VISIBLE);
            initFootingFoundation();
        } else {
            ll_footing_foundation.setVisibility(View.GONE);
        }

        if (property_type.equalsIgnoreCase("land")) {
            tl_no_of_floors_above_ground.setVisibility(View.GONE);

        } else {
            if (general.getUIVisibility("No. of floors above ground") && general.getSectionID("No. of floors above ground") == 5) {
                tl_no_of_floors_above_ground.setVisibility(View.VISIBLE);
                if (!general.isEmpty(Singleton.getInstance().indProperty.getNoofFloorsAboveGround())) {
                    fsProgressCount = fsProgressCount + 1;
                    et_above_ground.setText(Singleton.getInstance().indProperty.getNoofFloorsAboveGround());
                }
            } else
                tl_no_of_floors_above_ground.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("Basement")) {
            tl_basement.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().indProperty.getBasementDetails() != null) {
                fsProgressCount = fsProgressCount + 1;
                et_basement.setText(Singleton.getInstance().indProperty.getBasementDetails() + "");
            }
        } else tl_basement.setVisibility(View.GONE);


        if (general.getUIVisibility("Fire Exit")) {
            cb_fire_exit.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().indProperty.getFireExitData() != null) {
                fsProgressCount = fsProgressCount + 1;
                cb_fire_exit.setChecked(Singleton.getInstance().indProperty.getFireExitData());
            }
        } else {
            cb_fire_exit.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Type of Steel Grade")) {
            ll_steel_grade.setVisibility(View.VISIBLE);
            initSteelGrade();
        } else ll_steel_grade.setVisibility(View.GONE);

        if (general.getUIVisibility("Ground slope more than 20%")) {
            cb_ground_slope_more_than.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().indProperty.getGroundSlopeData() != null) {
                fsProgressCount = fsProgressCount + 1;
                cb_ground_slope_more_than.setChecked(Singleton.getInstance().indProperty.getGroundSlopeData());
            }
        } else cb_ground_slope_more_than.setVisibility(View.GONE);


        if (general.getUIVisibility("Is in Hill Slope")) {
            checkbox_hill_slope.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().property.getIsInHillSlope() != null) {
                fsProgressCount = fsProgressCount + 1;
                checkbox_hill_slope.setChecked(Singleton.getInstance().property.getIsInHillSlope());
            }
        } else checkbox_hill_slope.setVisibility(View.GONE);


        if (general.getUIVisibility("Is soil liquefiable")) {
            checkbox_liquefiable.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().indProperty.getIsSoilLiquefiable() != null) {
                fsProgressCount = fsProgressCount + 1;
                checkbox_liquefiable.setChecked(Singleton.getInstance().indProperty.getIsSoilLiquefiable());
            }
        } else checkbox_liquefiable.setVisibility(View.GONE);


    }

    private void initSteelGrade() {
        ArrayAdapter<TypeOfSteel.Datum> adapterSteel = new ArrayAdapter<TypeOfSteel.Datum>(mContext, R.layout.row_spinner_item, Singleton.getInstance().typeOfSteelList) {

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
        adapterSteel.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_steel.setAdapter(adapterSteel);
        spinner_steel.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getTypeofSteelGradeId()))) {
            Log.e("spinner_qualityofcon", "::: " + Singleton.getInstance().indProperty.getTypeofSteelGradeId());
            for (int x = 0; x < Singleton.getInstance().typeOfSteelList.size(); x++) {
                if (Singleton.getInstance().typeOfSteelList.get(x).getTypeofsteelgradeId() != null)
                    if (Singleton.getInstance().indProperty.getTypeofSteelGradeId().equals(Singleton.getInstance().typeOfSteelList.get(x).getTypeofsteelgradeId())) {
                        fsProgressCount = fsProgressCount + 1;
                        spinner_steel.setSelection(x);
                        break;
                    }
            }
        }
    }

    private void initPurpose() {
        // spinner - Property
        ArrayAdapter<DropDownResponse.Datum> localityArrayAdapter = new ArrayAdapter<DropDownResponse.Datum>(getActivity(), R.layout.row_spinner_item, Singleton.getInstance().purposeOfList) {
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
        localityArrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_purpose.setAdapter(localityArrayAdapter);
        spinner_purpose.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().property.getPurpose()))) {
            for (int x = 0; x < Singleton.getInstance().purposeOfList.size(); x++) {
                if (Singleton.getInstance().property.getPurposeofloanId() != null && Singleton.getInstance().purposeOfList.get(x).getPurposeofloanId() != null && Singleton.getInstance().purposeOfList.get(x) != null && Singleton.getInstance().purposeOfList.get(x).getPurposeofloanId() != null)
                    if (Singleton.getInstance().property.getPurposeofloanId().equals(Singleton.getInstance().purposeOfList.get(x).getPurposeofloanId())) {
                        spinner_purpose.setSelection(x);
                        fsProgressCount = fsProgressCount + 1;
                        break;
                    }
            }
        }
    }

    private void initNameOfBorrower() {
        // spinner - nameof seller initials
        initials_typeList = new ArrayList<>();
        initials_typeList = general.NameofSellerInitials_array();
        /*ArrayAdapter<String> nameofsellerListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner_item, initials_typeList) {
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

        nameofsellerListAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spSalutionBorrower.setPrompt(getResources().getString(R.string.nameof_Borrower));
        spSalutionBorrower.setAdapter(nameofsellerListAdapter);
        spSalutionBorrower.setOnTouchListener(this);*/

        if (!general.isEmpty(Singleton.getInstance().aCase.getApplicantName())) {
            String applicant_id = Singleton.getInstance().aCase.getApplicantName();
            String[] splitInitial = {};
            String nameofApplicant = "", initialconcat = "";
            int itemCount = 0;


            //splitInitial = nameseller_id.split("\\s+");
            for (int i = 0; i < initials_typeList.size(); i++) {
                String initials = initials_typeList.get(i);

                if (applicant_id.trim().equalsIgnoreCase(initials.trim())) {
                    //spSalutionBorrower.setSelection(i, false);
                    et_SalutionBorrower.setText("" + initials.trim());
                    nameofApplicant = "";
                    splitInitial = null;
                    break;
                } else {
                    if (applicant_id.trim().contains(".")) {
                        itemCount = applicant_id.split("\\.").length;
                        splitInitial = applicant_id.split("\\.");

                        if (itemCount > 2) {
                            String first = splitInitial[0].trim();
                            String second = splitInitial[1].trim();
                            initialconcat = first + "." + second;
                        }
                    }

                    if (itemCount > 2) {
                        if (initials.trim().equalsIgnoreCase(initialconcat.trim())) {
                            et_SalutionBorrower.setText("" + initialconcat.trim());
                            //spSalutionBorrower.setSelection(i, false);
                            break;
                        }
                    } else {

                        if (applicant_id.trim().equalsIgnoreCase(initials.trim())) {
                            //spSalutionBorrower.setSelection(i, false);
                            et_SalutionBorrower.setText("" + initials.trim());
                            break;
                        } else if (splitInitial.length > 0) {
                            if (splitInitial[0].equalsIgnoreCase(initials.trim())) {
                                et_SalutionBorrower.setText("" + initials.trim());
                                // spSalutionBorrower.setSelection(i, false);
                                break;
                            }
                        }

                    }
                }
            }


            if (splitInitial != null && splitInitial.length > 0) {
                for (int i = 0; i < splitInitial.length; i++) {

                    if (itemCount > 2) {
                        if (i != 0 && i != 1) nameofApplicant += splitInitial[i];
                    } else {
                        if (i != 0) {
                            nameofApplicant += splitInitial[i] + " "; // pipe symbol "\\|"
                            //nameofseller += splitInitial[i] + " "; // pipe symbol "\\|"
                        }
                    }
                }
            } else {

                for (int i = 0; i < initials_typeList.size(); i++) {
                    String initials = initials_typeList.get(i);
                    if (initials.trim().equalsIgnoreCase(applicant_id.trim())) {
                        //spSalutionBorrower.setSelection(i, false);
                        nameofApplicant = "";
                        break;

                    } else {
                        nameofApplicant = applicant_id;
                    }
                }

            }


            if (!general.isEmpty(nameofApplicant)) {
                fsProgressCount = fsProgressCount + 1;
                applicantName.setText(nameofApplicant.trim());
            }

        }
    }


    private void initNameOfSeller() {
        // spinner - nameof seller initials
        initials_typeList = new ArrayList<>();
        initials_typeList = general.NameofSellerInitials_array();
        ArrayAdapter<String> nameofsellerListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner_item, initials_typeList) {
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

        nameofsellerListAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spSalutionSeller.setPrompt(getResources().getString(R.string.nameofseller));
        spSalutionSeller.setAdapter(nameofsellerListAdapter);
        spSalutionSeller.setOnTouchListener(this);

        if (!general.isEmpty(Singleton.getInstance().property.getNameOfSeller())) {
            String applicant_id = Singleton.getInstance().property.getNameOfSeller();
            String[] splitInitial = {};
            String nameofSeller = "", initialconcat = "";
            int itemCount = 0;


            //splitInitial = nameseller_id.split("\\s+");
            for (int i = 0; i < initials_typeList.size(); i++) {
                String initials = initials_typeList.get(i);

                if (applicant_id.trim().equalsIgnoreCase(initials.trim())) {
                    spSalutionSeller.setSelection(i, false);
                    nameofSeller = "";
                    splitInitial = null;
                    break;
                } else {
                    if (applicant_id.trim().contains(".")) {
                        itemCount = applicant_id.split("\\.").length;
                        splitInitial = applicant_id.split("\\.");

                        if (itemCount > 2) {
                            String first = splitInitial[0].trim();
                            String second = splitInitial[1].trim();
                            initialconcat = first + "." + second;
                        }
                    }

                    if (itemCount > 2) {
                        if (initials.trim().equalsIgnoreCase(initialconcat.trim())) {
                            spSalutionSeller.setSelection(i, false);
                            break;
                        }
                    } else {

                        if (applicant_id.trim().equalsIgnoreCase(initials.trim())) {
                            spSalutionSeller.setSelection(i, false);
                            break;
                        } else if (splitInitial.length > 0) {
                            if (splitInitial[0].equalsIgnoreCase(initials.trim())) {
                                spSalutionSeller.setSelection(i, false);
                                break;
                            }
                        }

                    }
                }
            }


            if (splitInitial != null && splitInitial.length > 0) {
                for (int i = 0; i < splitInitial.length; i++) {

                    if (itemCount > 2) {
                        if (i != 0 && i != 1) nameofSeller += splitInitial[i];
                    } else {
                        if (i != 0) {
                            nameofSeller += splitInitial[i] + " "; // pipe symbol "\\|"
                            //nameofseller += splitInitial[i] + " "; // pipe symbol "\\|"
                        }
                    }
                }
            } else {

                for (int i = 0; i < initials_typeList.size(); i++) {
                    String initials = initials_typeList.get(i);
                    if (initials.trim().equalsIgnoreCase(applicant_id.trim())) {
                        spSalutionSeller.setSelection(i, false);
                        nameofSeller = "";
                        break;

                    } else {
                        nameofSeller = applicant_id;
                    }
                }

            }


            if (!general.isEmpty(nameofSeller)) {
                fsProgressCount = fsProgressCount + 1;
                et_seller_name.setText(nameofSeller.trim());
            }

        }
    }


    private void initNameOfOwner() {
        // spinner - nameof seller initials
        initials_typeList = new ArrayList<>();
        initials_typeList = general.NameofSellerInitials_array();
        ArrayAdapter<String> nameofsellerListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner_item, initials_typeList) {
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

        nameofsellerListAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spSalutionOwner.setPrompt("Name of Owner");
        spSalutionOwner.setAdapter(nameofsellerListAdapter);
        spSalutionOwner.setOnTouchListener(this);

        if (!general.isEmpty(Singleton.getInstance().property.getNameOfPurchaser())) {
            String applicant_id = Singleton.getInstance().property.getNameOfPurchaser();
            String[] splitInitial = {};
            String nameofOwner = "", initialconcat = "";
            int itemCount = 0;


            //splitInitial = nameseller_id.split("\\s+");
            for (int i = 0; i < initials_typeList.size(); i++) {
                String initials = initials_typeList.get(i);

                if (applicant_id.trim().equalsIgnoreCase(initials.trim())) {
                    spSalutionOwner.setSelection(i, false);
                    nameofOwner = "";
                    splitInitial = null;
                    break;
                } else {
                    if (applicant_id.trim().contains(".")) {
                        itemCount = applicant_id.split("\\.").length;
                        splitInitial = applicant_id.split("\\.");

                        if (itemCount > 2) {
                            String first = splitInitial[0].trim();
                            String second = splitInitial[1].trim();
                            initialconcat = first + "." + second;
                        }
                    }

                    if (itemCount > 2) {
                        if (initials.trim().equalsIgnoreCase(initialconcat.trim())) {
                            spSalutionOwner.setSelection(i, false);
                            break;
                        }
                    } else {

                        if (applicant_id.trim().equalsIgnoreCase(initials.trim())) {
                            spSalutionOwner.setSelection(i, false);
                            break;
                        } else if (splitInitial.length > 0) {
                            if (splitInitial[0].equalsIgnoreCase(initials.trim())) {
                                spSalutionOwner.setSelection(i, false);
                                break;
                            }
                        }

                    }
                }
            }


            if (splitInitial != null && splitInitial.length > 0) {
                for (int i = 0; i < splitInitial.length; i++) {

                    if (itemCount > 2) {
                        if (i != 0 && i != 1) nameofOwner += splitInitial[i];
                    } else {
                        if (i != 0) {
                            nameofOwner += splitInitial[i] + " "; // pipe symbol "\\|"
                            //nameofseller += splitInitial[i] + " "; // pipe symbol "\\|"
                        }
                    }
                }
            } else {

                for (int i = 0; i < initials_typeList.size(); i++) {
                    String initials = initials_typeList.get(i);
                    if (initials.trim().equalsIgnoreCase(applicant_id.trim())) {
                        spSalutionOwner.setSelection(i, false);
                        nameofOwner = "";
                        break;

                    } else {
                        nameofOwner = applicant_id;
                    }
                }

            }


            if (!general.isEmpty(nameofOwner)) {
                et_name_of_owner.setText(nameofOwner.trim());
                fsProgressCount = fsProgressCount + 1;
            }
        }
    }

    private void initFootingFoundation() {
        ArrayAdapter<TypeOfFooting.Datum> adapterFooting = new ArrayAdapter<TypeOfFooting.Datum>(mContext, R.layout.row_spinner_item, Singleton.getInstance().typeOfFootingList) {

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
        adapterFooting.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_foundation.setAdapter(adapterFooting);
        spinner_foundation.setOnTouchListener(this);
        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getTypeofFootingFoundationId()))) {
            Log.e("spinner_qualityofcon", "::: " + Singleton.getInstance().indProperty.getTypeofSteelGradeId());
            for (int x = 0; x < Singleton.getInstance().typeOfFootingList.size(); x++) {
                if (Singleton.getInstance().typeOfFootingList.get(x).getTypeoffootingfoundationId() != null)
                    if (Singleton.getInstance().indProperty.getTypeofFootingFoundationId().equals(Singleton.getInstance().typeOfFootingList.get(x).getTypeoffootingfoundationId())) {
                        spinner_foundation.setSelection(x);
                        fsProgressCount = fsProgressCount + 1;
                        break;
                    }
            }
        }
    }


    private void initiateSoilType() {
        ArrayAdapter<SoilType.SoilTypeData> soilTypeAdapter = new ArrayAdapter<SoilType.SoilTypeData>(mContext, R.layout.row_spinner_item, Singleton.getInstance().soilTypeData) {

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
        soilTypeAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_soil_type.setAdapter(soilTypeAdapter);
        spinner_soil_type.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getSoilTypeDd()))) {
            Log.e("spinner_soilType", "::: " + Singleton.getInstance().indProperty.getSoilTypeDd());
            for (int x = 0; x < Singleton.getInstance().soilTypeData.size(); x++) {

                if (Singleton.getInstance().indProperty.getSoilTypeDd().equals(Singleton.getInstance().soilTypeData.get(x).getId())) {
                    spinner_soil_type.setSelection(x);
                    fsProgressCount = fsProgressCount + 1;
                    break;
                }
            }
        }

    }

    private void function_interior_roof() {


        textview_roofing_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(textview_roofing_text);
                showdialog_dynamic("roofing");
            }
        });

        // clear the array and set the floor list in array
        Inter_roofing_list = new ArrayList<>();
        Inter_roofing_list = Singleton.getInstance().roof_list;
        // check Floor Dropdown is empty
        if (Inter_roofing_list.size() > 0) {
            String getIntFloorId = Singleton.getInstance().indProperty.getIntRoofId();
            // check selected array is empty
            if (!general.isEmpty(getIntFloorId)) {
                // selected array convert to comma separate array
                List<String> Inter_roofing_list_selected_id_commo = new ArrayList<>();
                Inter_roofing_list_selected_id_commo = new ArrayList<String>(Arrays.asList(getIntFloorId.split(",")));
                if (Inter_roofing_list_selected_id_commo.size() > 0) {
                    // Loop the Floor and get ID > check If selected array contains ID
                    for (int x = 0; x < Inter_roofing_list.size(); x++) {
                        if (Inter_roofing_list_selected_id_commo.toString().contains("" + Inter_roofing_list.get(x).getIntRoofId())) {
                            for (int y = 0; y < Inter_roofing_list_selected_id_commo.size(); y++) {
                                // Loop the selected array > If the selected array ID equal to Floor ID > save ID and name to Instance
                                int one_by_one_id = Integer.parseInt(Inter_roofing_list_selected_id_commo.get(y));
                                if (Inter_roofing_list.get(x).getIntRoofId() == one_by_one_id) {
                                    Singleton.getInstance().interior_roofing_id.add(Inter_roofing_list.get(x).getIntRoofId());
                                    Singleton.getInstance().interior_roofing_name.add(Inter_roofing_list.get(x).getName());
                                    fsProgressCount = fsProgressCount + 1;
                                }
                            }
                            textview_roofing_text.setText(general.remove_array_brac_and_space(Singleton.getInstance().interior_roofing_name.toString()));
                        }
                    }
                }
            } else {
                textview_roofing_text.setText(getResources().getString(R.string.select));
            }
        }
    }

    private void initiateEnvExposureConditionDropDownFields() {
        ArrayAdapter<EnvExposureCondition.EnvExposureConditionData> envExposureConditionAdapter = new ArrayAdapter<EnvExposureCondition.EnvExposureConditionData>(mContext, R.layout.row_spinner_item, Singleton.getInstance().envExposureConditionData) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(general.mediumtypeface());
                // ((TextView) v).setText(Singleton.getInstance().concreteGrade.get(position).getName());
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(general.mediumtypeface());
                return v;
            }

        };
        envExposureConditionAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_environment_exposure_condition.setAdapter(envExposureConditionAdapter);
        spinner_environment_exposure_condition.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getEnvironmentExposureConditionDd()))) {
            Log.e("spinner_concreteGrade", "::: " + Singleton.getInstance().indProperty.getEnvironmentExposureConditionDd());
            for (int x = 0; x < Singleton.getInstance().envExposureConditionData.size(); x++) {

                if (Singleton.getInstance().indProperty.getEnvironmentExposureConditionDd().equals(Singleton.getInstance().envExposureConditionData.get(x).getId())) {
                    spinner_environment_exposure_condition.setSelection(x);
                    break;
                }
            }
        }

    }

    private void initiateConcreteGradeDropDownFields() {
        ArrayAdapter<ConcreteGrade.ConcreteGradeData> concreteGradeAdapter = new ArrayAdapter<ConcreteGrade.ConcreteGradeData>(mContext, R.layout.row_spinner_item, Singleton.getInstance().concreteGrade) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(general.mediumtypeface());
                // ((TextView) v).setText(Singleton.getInstance().concreteGrade.get(position).getName());
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(general.mediumtypeface());
                return v;
            }

        };
        concreteGradeAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_concrete_grade.setAdapter(concreteGradeAdapter);
        spinner_concrete_grade.setOnTouchListener(this);


        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getConcreteGradeDd()))) {
            Log.e("spinner_concreteGrade", "::: " + Singleton.getInstance().indProperty.getConcreteGrade());
            for (int x = 0; x < Singleton.getInstance().concreteGrade.size(); x++) {

                if (Singleton.getInstance().indProperty.getConcreteGradeDd().equals(Singleton.getInstance().concreteGrade.get(x).getId())) {
                    spinner_concrete_grade.setSelection(x);
                    break;
                }
            }
        }

    }


    private void initSpinnerMortar() {
        ArrayAdapter<TypeOfMortar.Datum> adapterMortor = new ArrayAdapter<TypeOfMortar.Datum>(mContext, R.layout.row_spinner_item, Singleton.getInstance().typeOfMortarsList) {

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
        adapterMortor.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_mortar.setAdapter(adapterMortor);
        spinner_mortar.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getTypeofMortarId()))) {
            Log.e("spinner_qualityofcon", "::: " + Singleton.getInstance().indProperty.getTypeofMortarId());
            for (int x = 0; x < Singleton.getInstance().typeOfMortarsList.size(); x++) {
                if (Singleton.getInstance().typeOfMortarsList.get(x).getTypeofmortarId() != null)
                    if (Singleton.getInstance().indProperty.getTypeofMortarId().equals(Singleton.getInstance().typeOfMortarsList.get(x).getTypeofmortarId())) {
                        spinner_mortar.setSelection(x);
                        break;
                    }
            }
        }
    }

    private void function_interior_floor() {

        textview_flooring_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(textview_flooring_text);
                showdialog_dynamic("floor");
            }
        });


        // clear the array and set the floor list in array
        Inter_floors_list = new ArrayList<>();
        Inter_floors_list = Singleton.getInstance().floors_list;

        Singleton.getInstance().interior_flooring_id.clear();
        Singleton.getInstance().interior_flooring_name.clear();

        // check Floor Dropdown is empty
        if (Inter_floors_list.size() > 0) {
            String getIntFloorId = Singleton.getInstance().indProperty.getIntFloorId();
//            Inter_floors_list_selected_id_dummy = getIntFloorId;
            // check selected array is empty
            if (!general.isEmpty(getIntFloorId)) {
                // selected array convert to comma separate array
                List<String> Inter_floors_list_selected_id_commo = new ArrayList<>();
                Inter_floors_list_selected_id_commo = new ArrayList<String>(Arrays.asList(getIntFloorId.split(",")));
                if (Inter_floors_list_selected_id_commo.size() > 0) {
                    // Loop the Floor and get ID > check If selected array contains ID
                    for (int x = 0; x < Inter_floors_list.size(); x++) {
                        if (Inter_floors_list_selected_id_commo.toString().contains("" + Inter_floors_list.get(x).getIntFloorId())) {
                            for (int y = 0; y < Inter_floors_list_selected_id_commo.size(); y++) {
                                // Loop the selected array > If the selected array ID equal to Floor ID > save ID and name to Instance
                                int one_by_one_id = Integer.parseInt(Inter_floors_list_selected_id_commo.get(y));
                                if (Inter_floors_list.get(x).getIntFloorId() == one_by_one_id) {
                                    Singleton.getInstance().interior_flooring_id.add(Inter_floors_list.get(x).getIntFloorId());
                                    Singleton.getInstance().interior_flooring_name.add(Inter_floors_list.get(x).getName());
                                }
                            }
                            textview_flooring_text.setText(general.remove_array_brac_and_space(Singleton.getInstance().interior_flooring_name.toString()));
                        }
                    }
                }
            } else {
                textview_flooring_text.setText(getResources().getString(R.string.select));
            }
        }
    }


    private void function_exterior_structure() {
        // clear the array and set the floor list in array
        Exter_struc_list = new ArrayList<>();
        Exter_struc_list = Singleton.getInstance().structures_list;

        Singleton.getInstance().exter_stru_id.clear();
        Singleton.getInstance().exter_stru_name.clear();

        // check Floor Dropdown is empty
        if (Exter_struc_list.size() > 0) {
            String getIntFloorId = Singleton.getInstance().indProperty.getTypeOfStructureId();
            // check selected array is empty
            if (!general.isEmpty(getIntFloorId)) {
                // selected array convert to comma separate array
                List<String> Exter_struc_list_selected_id_commo = new ArrayList<>();
                Exter_struc_list_selected_id_commo = new ArrayList<String>(Arrays.asList(getIntFloorId.split(",")));
                if (Exter_struc_list_selected_id_commo.size() > 0) {
                    // Loop the Floor and get ID > check If selected array contains ID
                    for (int x = 0; x < Exter_struc_list.size(); x++) {
                        if (Exter_struc_list_selected_id_commo.toString().contains("" + Exter_struc_list.get(x).getTypeOfStructureId())) {
                            for (int y = 0; y < Exter_struc_list_selected_id_commo.size(); y++) {
                                // Loop the selected array > If the selected array ID equal to Floor ID > save ID and name to Instance
                                int one_by_one_id = Integer.parseInt(Exter_struc_list_selected_id_commo.get(y));
                                if (Exter_struc_list.get(x).getTypeOfStructureId() == one_by_one_id) {
                                    Singleton.getInstance().exter_stru_id.add(Exter_struc_list.get(x).getTypeOfStructureId());
                                    Singleton.getInstance().exter_stru_name.add(Exter_struc_list.get(x).getName());
                                }
                            }
                        }
                        textview_exter_struc_text.setText(general.remove_array_brac_and_space(Singleton.getInstance().exter_stru_name.toString()));
                    }
                }
            }
        } else {
            textview_exter_struc_text.setText(getResources().getString(R.string.select));
        }
    }

    private void function_interior_floor_property_dtls() {

        textview_flooring_text_property_dtls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(textview_flooring_text_property_dtls);
                showdialog_dynamic("floor_prpty_dtls");
            }
        });


        // clear the array and set the floor list in array
        Inter_floors_list_property_dtls = new ArrayList<>();
        Inter_floors_list_property_dtls = Singleton.getInstance().floors_list;

        Singleton.getInstance().interior_flooring_id.clear();
        Singleton.getInstance().interior_flooring_name.clear();

        // check Floor Dropdown is empty
        if (Inter_floors_list_property_dtls.size() > 0) {
            String getIntFloorId = Singleton.getInstance().indProperty.getIntFloorId();
//            Inter_floors_list_selected_id_dummy = getIntFloorId;
            // check selected array is empty
            if (!general.isEmpty(getIntFloorId)) {
                // selected array convert to comma separate array
                List<String> Inter_floors_list_selected_id_commo = new ArrayList<>();
                Inter_floors_list_selected_id_commo = new ArrayList<String>(Arrays.asList(getIntFloorId.split(",")));
                if (Inter_floors_list_selected_id_commo.size() > 0) {
                    // Loop the Floor and get ID > check If selected array contains ID
                    for (int x = 0; x < Inter_floors_list_property_dtls.size(); x++) {
                        if (Inter_floors_list_selected_id_commo.toString().contains("" + Inter_floors_list_property_dtls.get(x).getIntFloorId())) {
                            for (int y = 0; y < Inter_floors_list_selected_id_commo.size(); y++) {
                                // Loop the selected array > If the selected array ID equal to Floor ID > save ID and name to Instance
                                int one_by_one_id = Integer.parseInt(Inter_floors_list_selected_id_commo.get(y));
                                if (Inter_floors_list_property_dtls.get(x).getIntFloorId() == one_by_one_id) {
                                    Singleton.getInstance().interior_flooring_id.add(Inter_floors_list_property_dtls.get(x).getIntFloorId());
                                    Singleton.getInstance().interior_flooring_name.add(Inter_floors_list_property_dtls.get(x).getName());
                                }
                            }
                            textview_flooring_text_property_dtls.setText(general.remove_array_brac_and_space(Singleton.getInstance().interior_flooring_name.toString()));
                        }
                    }
                }
            } else {
                textview_flooring_text_property_dtls.setText(getResources().getString(R.string.select));
            }
        }
    }

    private void function_exterior_structure_property_dtls() {

        textview_exter_struc_text_property_dtls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(textview_exter_struc_text_property_dtls);
                showdialog_dynamic("exter_stru_prpty_dtls");
            }
        });

        // clear the array and set the floor list in array
        Exter_struc_list_property_dtls = new ArrayList<>();
        Exter_struc_list_property_dtls = Singleton.getInstance().structures_list;

        Singleton.getInstance().exter_stru_id.clear();
        Singleton.getInstance().exter_stru_name.clear();

        // check Floor Dropdown is empty
        if (Exter_struc_list_property_dtls.size() > 0) {
            String getIntFloorId = Singleton.getInstance().indProperty.getTypeOfStructureId();
            // check selected array is empty
            if (!general.isEmpty(getIntFloorId)) {
                // selected array convert to comma separate array
                List<String> Exter_struc_list_selected_id_commo = new ArrayList<>();
                Exter_struc_list_selected_id_commo = new ArrayList<String>(Arrays.asList(getIntFloorId.split(",")));
                if (Exter_struc_list_selected_id_commo.size() > 0) {
                    // Loop the Floor and get ID > check If selected array contains ID
                    for (int x = 0; x < Exter_struc_list_property_dtls.size(); x++) {
                        if (Exter_struc_list_selected_id_commo.toString().contains("" + Exter_struc_list_property_dtls.get(x).getTypeOfStructureId())) {
                            for (int y = 0; y < Exter_struc_list_selected_id_commo.size(); y++) {
                                // Loop the selected array > If the selected array ID equal to Floor ID > save ID and name to Instance
                                int one_by_one_id = Integer.parseInt(Exter_struc_list_selected_id_commo.get(y));
                                if (Exter_struc_list_property_dtls.get(x).getTypeOfStructureId() == one_by_one_id) {
                                    Singleton.getInstance().exter_stru_id.add(Exter_struc_list_property_dtls.get(x).getTypeOfStructureId());
                                    Singleton.getInstance().exter_stru_name.add(Exter_struc_list_property_dtls.get(x).getName());
                                }
                            }
                        }
                        textview_exter_struc_text_property_dtls.setText(general.remove_array_brac_and_space(Singleton.getInstance().exter_stru_name.toString()));
                    }
                }
            }
        } else {
            textview_exter_struc_text_property_dtls.setText(getResources().getString(R.string.select));
        }
    }


    private void hideSoftKeyboard(View addkeys) {
        if ((addkeys != null) && (getActivity() != null)) {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(addkeys.getWindowToken(), 0);
        }
    }

    private void initMaintanceBuilding() {
        // spinner - Maintenance Of Building
        ArrayAdapter<Maintenance> maintenanceArrayAdapter = new ArrayAdapter<Maintenance>(getActivity(), R.layout.row_spinner_item, Singleton.getInstance().maintenances_list) {
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
        maintenanceArrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_maintenanceofbuilding.setAdapter(maintenanceArrayAdapter);
        spinner_maintenanceofbuilding.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getMaintenanceOfBuildingId()))) {
            Log.e("spinner_maintenance", "::: " + Singleton.getInstance().indProperty.getMaintenanceOfBuildingId());
            if (Singleton.getInstance().indProperty.getMaintenanceOfBuildingId() != 0) {
                ArrayList<Maintenance> qualityConstructions_ = new ArrayList<>();
                qualityConstructions_ = Singleton.getInstance().maintenances_list;
                for (int x = 0; x < qualityConstructions_.size(); x++) {
                    if (qualityConstructions_.get(x).getMaintenanceOfBuildingId() == Singleton.getInstance().indProperty.getMaintenanceOfBuildingId()) {
                        spinner_maintenanceofbuilding.setSelection(x);
                        fsProgressCount = fsProgressCount + 1;
                    }
                }
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

        if (type_of_dialog.equalsIgnoreCase("presently_occupied")) {
            // Type -> presently_occupied
            textview_heading.setText(getResources().getString(R.string.persently_occupied));
            String selectedId = Singleton.getInstance().property.getPresentlyOccupied();
            PresentlyOccupiedAdapter presentlyOccupiedAdapter = new PresentlyOccupiedAdapter(getActivity(), PresentlyOccupied_list, selectedId);
            recyclerview_dialog.setAdapter(presentlyOccupiedAdapter);

        } else if (type_of_dialog.equalsIgnoreCase("exter_stru")) {
            // Type -> Roofing
            textview_heading.setText(getResources().getString(R.string.typeofstructure));
            String selectedId = Singleton.getInstance().indProperty.getTypeOfStructureId();
            ExterStructureAdapter exterStructureAdapter = new ExterStructureAdapter(getActivity(), Exter_struc_list, selectedId);
            recyclerview_dialog.setAdapter(exterStructureAdapter);
        } else if (type_of_dialog.equalsIgnoreCase("floor")) {
            // Type -> Floor
            textview_heading.setText(getResources().getString(R.string.flooring));
            String selectedId = Singleton.getInstance().indProperty.getIntFloorId();
            FlooringAdapter flooringAdapter = new FlooringAdapter(getActivity(), Inter_floors_list, selectedId);
            recyclerview_dialog.setAdapter(flooringAdapter);
        } else if (type_of_dialog.equalsIgnoreCase("floor_prpty_dtls")) {
            // Type -> Floor
            textview_heading.setText(getResources().getString(R.string.flooring));
            String selectedId = Singleton.getInstance().indProperty.getIntFloorId();
            FlooringAdapter flooringAdapter = new FlooringAdapter(getActivity(), Inter_floors_list_property_dtls, selectedId);
            recyclerview_dialog.setAdapter(flooringAdapter);
        } else if (type_of_dialog.equalsIgnoreCase("exter_stru_prpty_dtls")) {
            // Type -> Roofing
            textview_heading.setText(getResources().getString(R.string.typeofstructure));
            String selectedId = Singleton.getInstance().indProperty.getTypeOfStructureId();
            ExterStructureAdapter exterStructureAdapter = new ExterStructureAdapter(getActivity(), Exter_struc_list_property_dtls, selectedId);
            recyclerview_dialog.setAdapter(exterStructureAdapter);
        } else if (type_of_dialog.equalsIgnoreCase("roofing")) {
            // Type -> Roofing
            textview_heading.setText(getResources().getString(R.string.roofing));
            String selectedId = Singleton.getInstance().indProperty.getIntRoofId();
            RoofingAdapter roofingAdapter = new RoofingAdapter(getActivity(), Inter_roofing_list, selectedId);
            recyclerview_dialog.setAdapter(roofingAdapter);
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
                if (type_of_dialog.equalsIgnoreCase("presently_occupied")) {
                    // Type -> presently_occupied
                    if (Singleton.getInstance().PresentlyOccupied_id.size() > 0) {
                        String PresentlyOccupied_id = general.remove_array_brac_and_space(Singleton.getInstance().PresentlyOccupied_id.toString());
                        // set the Inter floor ID
                        Singleton.getInstance().property.setPresentlyOccupied(PresentlyOccupied_id);
                        // setText to the floor text
                        String PresentlyOccupied_name = general.remove_array_brac_and_space(Singleton.getInstance().PresentlyOccupied_name.toString());
                        textview_persently_occupied_text.setText(PresentlyOccupied_name);
                    } else {
                        // clear all the ID and dummy data and clear the settext
                        Singleton.getInstance().property.setPresentlyOccupied("");
                        textview_persently_occupied_text.setText(getResources().getString(R.string.select));
                    }
                    Log.e("PresentlyOccupied_id", "::: " + Singleton.getInstance().PresentlyOccupied_id);
                    Log.e("PresentlyOccupied_name", ":: " + Singleton.getInstance().PresentlyOccupied_name);

                } else if (type_of_dialog.equalsIgnoreCase("exter_stru")) {
                    // Type -> door
                    if (Singleton.getInstance().exter_stru_id.size() > 0) {
                        String exter_stru_id = general.remove_array_brac_and_space(Singleton.getInstance().exter_stru_id.toString());
                        // set the Inter floor ID
                        Singleton.getInstance().indProperty.setTypeOfStructureId(exter_stru_id);
                        // setText to the floor text
                        String exter_stru_name = general.remove_array_brac_and_space(Singleton.getInstance().exter_stru_name.toString());
                        textview_exter_struc_text.setText(exter_stru_name);
                    } else {
                        // clear all the ID and dummy data and clear the settext
                        Singleton.getInstance().indProperty.setTypeOfStructureId("");
                        textview_exter_struc_text.setText(getResources().getString(R.string.select));
                    }
                    Log.e("exter_stru_id", "::: " + Singleton.getInstance().exter_stru_id);
                    Log.e("exter_stru_name", ":: " + Singleton.getInstance().exter_stru_name);
                } else if (type_of_dialog.equalsIgnoreCase("floor")) {
                    // Type -> Floor
                    if (Singleton.getInstance().interior_flooring_id.size() > 0) {
                        String interior_flooring_id = general.remove_array_brac_and_space(Singleton.getInstance().interior_flooring_id.toString());
                        // set the Inter floor ID
                        Singleton.getInstance().indProperty.setIntFloorId(interior_flooring_id);
                        // setText to the floor text
                        String interior_flooring_name = general.remove_array_brac_and_space(Singleton.getInstance().interior_flooring_name.toString());
                        textview_flooring_text.setText(interior_flooring_name);
                    } else {
                        // clear all the ID and dummy data and clear the settext
                        Singleton.getInstance().indProperty.setIntFloorId("");
                        textview_flooring_text.setText(getResources().getString(R.string.select));
                    }
                    Log.e("interior_flooring_id", "::: " + Singleton.getInstance().interior_flooring_id);
                    Log.e("interior_flooring_name", ":: " + Singleton.getInstance().interior_flooring_name);
                } else if (type_of_dialog.equalsIgnoreCase("roofing")) {
                    // Type -> Roofing
                    if (Singleton.getInstance().interior_roofing_id.size() > 0) {
                        String interior_roofing_id = general.remove_array_brac_and_space(Singleton.getInstance().interior_roofing_id.toString());
                        // set the Inter floor ID
                        Singleton.getInstance().indProperty.setIntRoofId(interior_roofing_id);
                        // setText to the floor text
                        String interior_roofing_name = general.remove_array_brac_and_space(Singleton.getInstance().interior_roofing_name.toString());
                        textview_roofing_text.setText(interior_roofing_name);
                    } else {
                        // clear all the ID and dummy data and clear the settext
                        Singleton.getInstance().indProperty.setIntRoofId("");
                        textview_roofing_text.setText(getResources().getString(R.string.select));
                    }
                    Log.e("interior_roofing_id", "::: " + Singleton.getInstance().interior_roofing_id);
                    Log.e("interior_roofing_name", ":: " + Singleton.getInstance().interior_roofing_name);
                } else if (type_of_dialog.equalsIgnoreCase("floor_prpty_dtls")) {
                    // Type -> Floor
                    if (Singleton.getInstance().interior_flooring_id.size() > 0) {
                        String interior_flooring_id = general.remove_array_brac_and_space(Singleton.getInstance().interior_flooring_id.toString());
                        // set the Inter floor ID
                        Singleton.getInstance().indProperty.setIntFloorId(interior_flooring_id);
                        // setText to the floor text
                        String interior_flooring_name = general.remove_array_brac_and_space(Singleton.getInstance().interior_flooring_name.toString());
                        textview_flooring_text_property_dtls.setText(interior_flooring_name);
                    } else {
                        // clear all the ID and dummy data and clear the settext
                        Singleton.getInstance().indProperty.setIntFloorId("");
                        textview_flooring_text.setText(getResources().getString(R.string.select));
                    }
                    Log.e("interior_flooring_id", "::: " + Singleton.getInstance().interior_flooring_id);
                    Log.e("interior_flooring_name", ":: " + Singleton.getInstance().interior_flooring_name);
                } else if (type_of_dialog.equalsIgnoreCase("exter_stru_prpty_dtls")) {
                    // Type -> door
                    if (Singleton.getInstance().exter_stru_id.size() > 0) {
                        String exter_stru_id = general.remove_array_brac_and_space(Singleton.getInstance().exter_stru_id.toString());
                        // set the Inter floor ID
                        Singleton.getInstance().indProperty.setTypeOfStructureId(exter_stru_id);
                        // setText to the floor text
                        String exter_stru_name = general.remove_array_brac_and_space(Singleton.getInstance().exter_stru_name.toString());
                        textview_exter_struc_text_property_dtls.setText(exter_stru_name);
                    } else {
                        // clear all the ID and dummy data and clear the settext
                        Singleton.getInstance().indProperty.setTypeOfStructureId("");
                        textview_exter_struc_text_property_dtls.setText(getResources().getString(R.string.select));
                    }
                    Log.e("exter_stru_id", "::: " + Singleton.getInstance().exter_stru_id);
                    Log.e("exter_stru_name", ":: " + Singleton.getInstance().exter_stru_name);
                }

                alertDialog.dismiss();
            }
        });
    }

    private void initiatePropertyFragmentViews() {

        // TODO - Load the fragment according to property
        if (property_type.equalsIgnoreCase("building")) {

            /* *//* Load Building in fragment tab *//*
            FieldsInspectionLandBuilding fieldsInspectionLandBuilding = new FieldsInspectionLandBuilding();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_property_type_insert, fieldsInspectionLandBuilding).commitAllowingStateLoss();
*/

            llAsPerFlat.setVisibility(View.GONE);


            /* Valuation Building in fragment tab */
            FSLandBuildingValuation fsLandBuildingValuation = new FSLandBuildingValuation();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_fair_market_valuation, fsLandBuildingValuation).commitAllowingStateLoss();


        } else if (property_type.equalsIgnoreCase("flat") || property_type.equalsIgnoreCase("penthouse")) {

            llAsPerFlat.setVisibility(View.VISIBLE);
            FSFlatValuation fsFlatValuation = new FSFlatValuation();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_fair_market_valuation, fsFlatValuation).commitAllowingStateLoss();

        } else if (property_type.equalsIgnoreCase("land")) {
            llAsPerFlat.setVisibility(View.GONE);
            FSLandValuation fsLandValuation = new FSLandValuation();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_fair_market_valuation, fsLandValuation).commitAllowingStateLoss();
        }

        /* Load Flat in fragment tab */

            /*
            FragmentFlat fragment_flat = new FragmentFlat();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_property_type_insert, fragment_flat).commitAllowingStateLoss();

            *//* set bundel type and send to common fragment *//*
            Bundle bundle_pass = new Bundle();
            bundle_pass.putString("property_type", property_type);
            *//* Valuation Penthouse in fragment tab *//*
            FragmentValuationPenthouse fragment_valuation_penthouse = new FragmentValuationPenthouse();
            fragment_valuation_penthouse.setArguments(bundle_pass);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_valuation_type_insert, fragment_valuation_penthouse).commitAllowingStateLoss();


        } else if (property_type.equalsIgnoreCase("land")) {

            *//* Load Land in fragment tab *//*
            FragmentLand fragment_land = new FragmentLand();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_property_type_insert, fragment_land).commitAllowingStateLoss();
            *//* Valuation Land in fragment tab *//*
            FragmentValuationLand fragment_valuation_land = new FragmentValuationLand();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_valuation_type_insert, fragment_valuation_land).commitAllowingStateLoss();

        }
    */


    }

    private void initMarketability() {
        // spinner - Marketability
        ArrayAdapter<Marketablity> marketablityArrayAdapter = new ArrayAdapter<Marketablity>(getActivity(), R.layout.row_spinner_item, Singleton.getInstance().marketablities_list) {
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
        marketablityArrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_marketability.setAdapter(marketablityArrayAdapter);
        spinner_marketability.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getMarketabilityId()))) {
            Log.e("spinner_marketability", "::: " + Singleton.getInstance().indProperty.getMarketabilityId());
            if (Singleton.getInstance().indProperty.getMarketabilityId() != 0) {
                ArrayList<Marketablity> qualityConstructions_ = new ArrayList<>();
                qualityConstructions_ = Singleton.getInstance().marketablities_list;
                for (int x = 0; x < qualityConstructions_.size(); x++) {
                    if (qualityConstructions_.get(x).getMarketabilityId() == Singleton.getInstance().indProperty.getMarketabilityId()) {
                        spinner_marketability.setSelection(x);
                    }
                }
            }
        }

    }

    private void initRemark() {
        // Remarks Recyclerview
        LinearLayoutManager linearLayoutManager_remarks = new LinearLayoutManager(getActivity());
        recyclerview_remarks.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ArrayList<Remarks> remarks_list = new ArrayList<>();
        remarks_list = Singleton.getInstance().remarks_list;
        String selected_remarks_id = Singleton.getInstance().property.getRemarks();
        madapter_remarks = new Recycler_remarks_adapter(getActivity(), remarks_list, selected_remarks_id);
        recyclerview_remarks.setAdapter(madapter_remarks);
    }

    private void setPropertyType(String property_cate_id) {
        if (property_cate_id.equalsIgnoreCase("2")) {
            property_type = "building";
        } else if (property_cate_id.equalsIgnoreCase("1")) {
            property_type = "flat";
        } else if (property_cate_id.equalsIgnoreCase("4")) {
            property_type = "penthouse";
        } else if (property_cate_id.equalsIgnoreCase("3")) {
            property_type = "land";
        }
    }

    private void setPropertyBank(String property_bank) {
        isNPA = property_bank.contains("NPA");
    }

    public static FieldsInspectionDetails getInstance() {
        return instance;
    }

    private void showCalender() {
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog;
        int mYear = newCalendar.get(Calendar.YEAR);
        int mMonth = newCalendar.get(Calendar.MONTH);
        int mDay = newCalendar.get(Calendar.DAY_OF_MONTH);

        newCalendar.add(Calendar.YEAR, 0);
        long upperLimit = newCalendar.getTimeInMillis();

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                siteVisiteInCalender = "";
                newCalendar.set(Calendar.YEAR, year);
                newCalendar.set(Calendar.MONTH, month);
                newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                date_value.setText(sdf.format(newCalendar.getTime()));

                visitDate = sdf.format(newCalendar.getTime());

                siteVisiteInCalender = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(newCalendar.getTime());
                Log.e("Selected Date", "Calender.." + siteVisiteInCalender);
                date_error_msg.setVisibility(View.GONE);

            }
        }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMinDate(newCalendar.get(Calendar.YEAR));

        datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        datePickerDialog.getDatePicker().setMaxDate(upperLimit);
        datePickerDialog.show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }


    private void saveFSDetails() {
        if (!general.isEmpty(et_cyclone_zone.getText().toString())) {
            Singleton.getInstance().property.setCycloneZone(et_cyclone_zone.getText().toString().trim());
        } else {
            Singleton.getInstance().property.setCycloneZone("");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_save_top:
                if (!is_offline)
                    SettingsUtils.getInstance().putValue(SettingsUtils.is_local, false);
                save_type = "save";
                SaveFormDetails();
                break;
            case R.id.textview_save_bottom:
                if (!is_offline)
                    SettingsUtils.getInstance().putValue(SettingsUtils.is_local, false);
                save_type = "save";
                SaveFormDetails();
                break;
            case R.id.textview_save_top_dashboard:
                if (!is_offline)
                    SettingsUtils.getInstance().putValue(SettingsUtils.is_local, false);
                save_type = "savego";
                SaveFormDetails();
                break;
            case R.id.textview_save_bottom_dashboard:
                if (!is_offline)
                    SettingsUtils.getInstance().putValue(SettingsUtils.is_local, false);
                save_type = "savego";
                SaveFormDetails();
                break;

        }
    }

    public final Runnable getData = new Runnable() {
        @Override
        public void run() {
            getDataFrame();
        }
    };

    private void getDataFrame() {
        if (OtherDetailsFragment.isVisible) {
            SettingsUtils.getInstance().putValue(SettingsUtils.is_local, true);
            save_type = "save";
            SaveFormDetails();
        }
        /**/
        _handler.postDelayed(getData, DATA_INTERVAL);
    }


    public void geoCorodinate() {
        String latlong = Singleton.getInstance().property.getLatLongDetails();
        if (latlong == null || latlong.contains(":")) {
            etLat.setText(latvalue.getText().toString());
            etLong.setText(longvalue.getText().toString());
            str_latvalue = PhotoLatLong.latvalue.getText().toString();
            str_longvalue = PhotoLatLong.longvalue.getText().toString();
            String strLatLong = str_latvalue + ":" + str_longvalue;
            Singleton.getInstance().property.setLatLongDetails(strLatLong);
        } else if (latlong.isEmpty()) {
            etLat.setText(latvalue.getText().toString());
            etLong.setText(longvalue.getText().toString());
            str_latvalue = PhotoLatLong.latvalue.getText().toString();
            str_longvalue = PhotoLatLong.longvalue.getText().toString();
            String strLatLong = str_latvalue + ":" + str_longvalue;
            Singleton.getInstance().property.setLatLongDetails(strLatLong);
        } else {
            String[] temp = latlong.split(":");
            str_latvalue = temp[0].trim();
            str_longvalue = temp[1].trim();
            etLat.setText(str_latvalue);
            etLong.setText(str_longvalue);
            String strLatLong = str_latvalue + ":" + str_longvalue;
            Singleton.getInstance().property.setLatLongDetails(strLatLong);
        }

    }


    public void SaveFormDetails() {

        // The case is filled from Mobile Tab

        // check offline or not
        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);

        Singleton.getInstance().property.setPodiums("1");

        if (!is_offline) {
            // online
            if (Connectivity.isConnected(getActivity())) {
                save_function();
            } else {
                String curent_statusID = Singleton.getInstance().curent_statusID;
                Log.e("curent_statusID", "curent_statusID: " + curent_statusID);
                if (!general.isEmpty(curent_statusID)) {
                    if (curent_statusID.equalsIgnoreCase("13")) {
                        // Start Inspection - Data will save in LOCALDB
                        // TODO - Incase, If thr is no internet on start Inspection - move the case to offline mode
                        internet_check_box("start_inspec");
                    } else {
                        internet_check_box("edit_inspec");
                    }
                }
            }
        } else {
            // offline
            save_function();
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
                        // set as offline
                        SettingsUtils.getInstance().putValue(SettingsUtils.is_offline, true);
                        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);

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
                            // Check the getPhoto_propertyid is zero > we want to add the dummy images
                            if (appDatabase.interfaceGetPhotoQuery().getPhoto_propertyid(PropertyId_is).size() == 0) {
                                // Room Delete - PhotoCase
                                appDatabase.interfaceGetPhotoQuery().deleteRow(PropertyId_is);
                                // Room Add - PhotoCase
                                GetPhoto dummy_cameraImage = new GetPhoto();
                                dummy_cameraImage.setDefaultimage(true);
                                dummy_cameraImage.setPropertyId(PropertyId_is);
                                appDatabase.interfaceGetPhotoQuery().insertAll(dummy_cameraImage);
                                // Room Add - PhotoCase
                                GetPhoto dummy_galleryImage = new GetPhoto();
                                dummy_galleryImage.setDefaultimage(true);
                                dummy_galleryImage.setPropertyId(PropertyId_is);
                                appDatabase.interfaceGetPhotoQuery().insertAll(dummy_galleryImage);
                            }
                            save_function();
                        }
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
                            // Check the getPhoto_propertyid is zero > we want to add the dummy images
                            if (appDatabase.interfaceGetPhotoQuery().getPhoto_propertyid(PropertyId_is).size() == 0) {
                                // Room Delete - PhotoCase
                                appDatabase.interfaceGetPhotoQuery().deleteRow(PropertyId_is);
                                // Room Add - PhotoCase
                                GetPhoto dummy_cameraImage = new GetPhoto();
                                dummy_cameraImage.setDefaultimage(true);
                                dummy_cameraImage.setPropertyId(PropertyId_is);
                                appDatabase.interfaceGetPhotoQuery().insertAll(dummy_cameraImage);
                                // Room Add - PhotoCase
                                GetPhoto dummy_galleryImage = new GetPhoto();
                                dummy_galleryImage.setDefaultimage(true);
                                dummy_galleryImage.setPropertyId(PropertyId_is);
                                appDatabase.interfaceGetPhotoQuery().insertAll(dummy_galleryImage);
                            }

                            // hit_photo_api > false
                            Singleton.getInstance().hit_photo_api = true;

                            save_function();
                        }
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
                mContext.startActivity(i);
                dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void save_function() {
        if (property_type.equalsIgnoreCase("building")) {
            getProximityListData();
            function_save();
        } else if (property_type.equalsIgnoreCase("flat") || property_type.equalsIgnoreCase("penthouse")) {
            getProximityListData();
            function_save_penthouse();
        } else if (property_type.equalsIgnoreCase("land")) {
            getProximityListData();
            function_save_land();
        }
    }

    // save Function //
    public void function_save() {
        if (FieldsInspectionDetails.my_focuslayout != null) {
            FieldsInspectionDetails.my_focuslayout.requestFocus();
        }

        //invisible_save_button();
        general.showloading(getActivity());
        // Setting values and sending to data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                str_latvalue = PhotoLatLong.latvalue.getText().toString();
                str_longvalue = PhotoLatLong.longvalue.getText().toString();
                Singleton.getInstance().latlng_details = str_latvalue + ":" + str_longvalue;
                Singleton.getInstance().aCase.setStatus(Integer.valueOf(getResources().getString(R.string.edit_inspection)));

                getCommonFsInputData();

                /* valuation - Building */
                FSLandBuildingValuation fsLandBuildingValuation = new FSLandBuildingValuation();

                fsLandBuildingValuation.save_landval();
                FSLandBuildingValuation.saveIndValuationFloorsCalculation();

                // Visible the save button
                visible_save_button();

                if (Singleton.getInstance().hit_photo_api) {
                    // True - call photo function first
                    call_photo_function();
                } else {
                    // False
                    // TODO - Final Api
                    InitiateSaveCaseInspectionTask();
                }


            }
        }, 1000);
    }

    // save Function //
    public void function_save_penthouse() {
        if (FieldsInspectionDetails.my_focuslayout != null) {
            FieldsInspectionDetails.my_focuslayout.requestFocus();
        }
      //  invisible_save_button();
        general.showloading(getActivity());
        // Setting values and sending to data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                str_latvalue = latvalue.getText().toString();
                str_longvalue = PhotoLatLong.longvalue.getText().toString();
                Singleton.getInstance().latlng_details = str_latvalue + ":" + str_longvalue;
                Singleton.getInstance().aCase.setStatus(Integer.valueOf(getResources().getString(R.string.edit_inspection)));



                /*getGeneralInputData();
                getLocalityInputData();
                getPropertyInputData();
                getPropertyDetailsInputData();
                getNDMAParameters();
                getMarketInputData();
                getRemarkInputData();

                Singleton.getInstance().indProperty.setCaseId(Singleton.getInstance().aCase.getCaseId());
                Singleton.getInstance().property.setCaseId(Singleton.getInstance().aCase.getCaseId());*/

                getCommonFsInputData();

                FSFlatValuation fsFlatValuation = new FSFlatValuation();
                fsFlatValuation.PostPentHouseValues();
                fsFlatValuation.setIndPropertyValuationforPentHouseFlat();


                /******Penthouse or flat values*******/
               /* FragmentFlat fragmentFlat = new FragmentFlat();
                fragmentFlat.PostPentHouseValues();

                FragmentValuationPenthouse fragmentValuationPenthouse = new FragmentValuationPenthouse();
                fragmentValuationPenthouse.setIndPropertyValuationforPentHouseFlat();*/

                // Visible the save button
                visible_save_button();

                if (Singleton.getInstance().hit_photo_api) {
                    // True - call photo function first
                    call_photo_function();
                } else {
                    // False
                    // TODO - Final Api
                    InitiateSaveCaseInspectionTask();
                }

            }
        }, 1000);
    }

    // save Function //
    public void function_save_land() {
        if (FieldsInspectionDetails.my_focuslayout != null) {
            FieldsInspectionDetails.my_focuslayout.requestFocus();
        }
       // invisible_save_button();
        general.showloading(getActivity());
        // Setting values and sending to data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                str_latvalue = latvalue.getText().toString();
                str_longvalue = PhotoLatLong.longvalue.getText().toString();
                Singleton.getInstance().latlng_details = str_latvalue + ":" + str_longvalue;
                Singleton.getInstance().aCase.setStatus(Integer.valueOf(getResources().getString(R.string.edit_inspection)));

               /* getProximityListData();
                getAddressDetailsInputData();
                getMoreRemarks();
                getBrokerdetails();
                getMoreDetails();*/

                /*FragmentLand fragmentLand = new FragmentLand();
                fragmentLand.PostLandValues();

                *//* valuation - Building *//*
                FSLandValuation fsLandValuation = new FragmentValuationLand();
                fragmentValuationLand.save_landval();*/


                getCommonFsInputData();

                FSLandValuation fsLandValuation = new FSLandValuation();
                fsLandValuation.PostLandValues();
                fsLandValuation.save_landval();

//
//                FragmentValuationLand fragmentValuationLand = new FragmentValuationLand();
//                fragmentValuationLand.save_landval();

                // Visible the save button
                visible_save_button();

                if (Singleton.getInstance().hit_photo_api) {
                    // True - call photo function first
                    call_photo_function();
                } else {
                    // False
                    // TODO - Final Api
                    InitiateSaveCaseInspectionTask();
                }

            }
        }, 1000);
    }


    private void invisible_save_button() {
        textview_save_top.setVisibility(View.INVISIBLE);
        textview_save_top_dashboard.setVisibility(View.INVISIBLE);
        /*textview_save_bottom.setVisibility(View.INVISIBLE);
        textview_save_bottom_dashboard.setVisibility(View.INVISIBLE);*/
    }

    private void visible_save_button() {
//        textview_save_top.setVisibility(View.VISIBLE);
      textview_save_top_dashboard.setVisibility(View.VISIBLE);
        /*textview_save_bottom.setVisibility(View.VISIBLE);
        textview_save_bottom_dashboard.setVisibility(View.VISIBLE);*/
    }

    private void call_photo_function() {
        // check offline or not
        if (!is_offline) {
            // online
            if (Connectivity.isConnected(getActivity())) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray jsonArray = new JSONArray();
                        for (int x = 0; x < PhotoLatLong.GetPhoto_list_response.size(); x++) {
                            if (!PhotoLatLong.GetPhoto_list_response.get(x).isDefaultimage()) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("ID", PhotoLatLong.GetPhoto_list_response.get(x).getId());
                                    jsonObject.put("PropertyId", PhotoLatLong.GetPhoto_list_response.get(x).getPropertyId());
                                    if (!general.isEmpty(PhotoLatLong.GetPhoto_list_response.get(x).getTitle())) {
                                        jsonObject.put("Title", PhotoLatLong.GetPhoto_list_response.get(x).getTitle());
                                    } else {
                                        jsonObject.put("Title", "");
                                    }
                                    jsonObject.put("Logo", PhotoLatLong.GetPhoto_list_response.get(x).getLogo());
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                jsonArray.put(jsonObject);
                            }
                        }
                        if (jsonArray.length() > 0) {
                            UploadImage(jsonArray.toString());
                        }
                    }
                }, 500);
            } else {
                // TODO - Final Api
                InitiateSaveCaseInspectionTask();
            }
        } else {
            // offline
            // Room - delete lat long
            appDatabase.interfaceLatLongQuery().deleteRow(caseid_int);
            LatLongDetails latLongDetails = new LatLongDetails();
            latLongDetails.setCaseId(caseid_int);
            latLongDetails.setLatLongDetails(Singleton.getInstance().latlng_details);
            // Room - add lat long
            appDatabase.interfaceLatLongQuery().insertAll(latLongDetails);
            // Photo delete and add
            save_image_offline("next_btn");
        }

    }

    private void save_image_offline(final String btn_is) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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

                // hit_photo_api > false
                Singleton.getInstance().hit_photo_api = false;
                // TODO - Final Api
                InitiateSaveCaseInspectionTask();

            }
        }, 100);
    }

    private void UploadImage(String s) {

        JSONObject jsonObject = new JSONObject();
        String PropertyId = SettingsUtils.getInstance().getValue(SettingsUtils.PropertyId, "");
        if (general.isEmpty(PropertyId)) {
            PropertyId = "0";
        }
        try {
            jsonObject.put("PropertyId", PropertyId);
            jsonObject.put("LatLongDetails", Singleton.getInstance().latlng_details);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JSONObject edit_synk_obj = new JSONObject();
        try {
            edit_synk_obj.put("PropertyId", PropertyId);
            edit_synk_obj.put("is_sync", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonRequestData data = new JsonRequestData();
        // set Photo
        data.setCompanyName(s);
        // set the lat and long
        data.setContactPersonName(jsonObject.toString());
        // set edit_synk
        data.setApplicantName(edit_synk_obj.toString());

        data.setUrl(general.ApiBaseUrl() + SettingsUtils.IMAGE);
        data.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        data.setRequestBody(RequestParam.uploadimageRequestParams(data));

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(), data, SettingsUtils.POST_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                if (requestData.isSuccessful()) {
                    parseuploadimageResponse(requestData.getResponse());
                } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
                    general.hideloading();
                    General.sessionDialog(getActivity());
                } else {
                    general.hideloading();
                    General.customToast(getActivity().getString(R.string.something_wrong), getActivity());
                }
            }
        });
        webserviceTask.execute();
    }

    private void parseuploadimageResponse(String response) {

        // hit_photo_api > false
        Singleton.getInstance().hit_photo_api = false;
        // TODO - Final Api
        InitiateSaveCaseInspectionTask();

    }

    private void InitiateSaveCaseInspectionTask() {

        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);
        is_local = SettingsUtils.getInstance().getValue(SettingsUtils.is_local, false);
        if (!is_offline && !is_local) {
            //online
            if (Connectivity.isConnected(getActivity())) {

                LinkedTreeMap<String, Object> mainOb = new LinkedTreeMap<>();
                mainOb.put("Case", Singleton.getInstance().aCase);
                mainOb.put("Property", Singleton.getInstance().property);
                mainOb.put("IndProperty", Singleton.getInstance().indProperty);
                mainOb.put("IndPropertyValuation", Singleton.getInstance().indPropertyValuation);
                mainOb.put("IndPropertyFloors", Singleton.getInstance().indPropertyFloors);
                mainOb.put("IndPropertyFloorsValuation", Singleton.getInstance().indPropertyFloorsValuations);
                mainOb.put("Proximity", Singleton.getInstance().proximities);
                LinkedTreeMap<String, Object> map = new LinkedTreeMap<>();
                map.put("is_sync", false);
                mainOb.put("edit_synk", map);

                String url = general.ApiBaseUrl() + SettingsUtils.SaveCaseInspection;
                // String url = SettingsUtils.BASE_URL + SettingsUtils.SaveCaseInspection;
                JsonRequestData requestData = new JsonRequestData();
                requestData.setUrl(url);
                requestData.setMainJson(new Gson().toJson(mainOb));
                requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
                requestData.setRequestBody(RequestParam.SaveCaseInspectionRequestParams(requestData));

                Log.e("EditRequest", new Gson().toJson(requestData));

                Log.e("EditRequest MainJson FS", new Gson().toJson(mainOb));

                /*try{
                    JSONObject jsonObj = new JSONObject(new Gson().toJson(requestData.getMainJson()));
                    Log.e("EditRequest", String.valueOf(jsonObj));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }*/


                WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(), requestData, SettingsUtils.POST_TOKEN);
                webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                    @Override
                    public void onTaskComplete(JsonRequestData requestData) {
                        general.hideloading();


                       /* if (requestData.isSuccessful()) {
                            parseSaveCaseInspectionResponse(requestData.getResponse());
                        } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
                            General.sessionDialog(getActivity());
                        } else {
                            General.customToast(getActivity().getString(R.string.something_wrong),
                                    getActivity());
                        }*/

                        RequestApiStatus requestApiStatus = new Gson().fromJson(requestData.getResponse(), RequestApiStatus.class);
                        if (requestApiStatus.getStatus() == 1) {
                            parseSaveCaseInspectionResponse(requestData.getResponse());
                        } else if (requestApiStatus.getStatus() == 2) {
                            General.customToast("Please Enter Valid Input", getActivity());
                        }

                    }
                });
                webserviceTask.execute();
            } else {
                Connectivity.showNoConnectionDialog(getActivity());
                general.hideloading();
            }
        } else {
            //offline
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Room - delete lat long
                    appDatabase.interfaceLatLongQuery().deleteRow(caseid_int);
                    LatLongDetails latLongDetails = new LatLongDetails();
                    latLongDetails.setCaseId(caseid_int);
                    latLongDetails.setLatLongDetails(Singleton.getInstance().latlng_details);
                    // Room - add lat long
                    appDatabase.interfaceLatLongQuery().insertAll(latLongDetails);

                    //  Case - Room Delete
                    appDatabase.interfaceCaseQuery().deleteRow(caseid_int);
                    // Case - Room Add
                    Singleton.getInstance().aCase.setCaseId(caseid_int);
                    appDatabase.interfaceCaseQuery().insertAll(Singleton.getInstance().aCase);

                    // Property - Room Delete
                    appDatabase.interfacePropertyQuery().deleteRow(caseid_int);
                    // Property - Room Add
                    Singleton.getInstance().property.setCaseId(caseid_int);
                    appDatabase.interfacePropertyQuery().insertAll(Singleton.getInstance().property);

                    // IndProperty - Room Delete
                    appDatabase.interfaceIndpropertyQuery().deleteRow(caseid_int);
                    // IndProperty - Room Add
                    Singleton.getInstance().indProperty.setCaseId(caseid_int);
                    appDatabase.interfaceIndpropertyQuery().insertAll(Singleton.getInstance().indProperty);

                    // IndPropertyValuation - Room Delete
                    appDatabase.interfaceIndPropertyValuationQuery().deleteRow(caseid_int);
                    // IndPropertyValuation - Room Add
                    Singleton.getInstance().indPropertyValuation.setCaseId(caseid_int);
                    appDatabase.interfaceIndPropertyValuationQuery().insertAll(Singleton.getInstance().indPropertyValuation);

                    // IndPropertyFloor - Room Delete
                    appDatabase.interfaceIndPropertyFloorsQuery().deleteRow(caseid_int);
                    // IndPropertyFloor - Room Add
                    if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                        for (int x = 0; x < Singleton.getInstance().indPropertyFloors.size(); x++) {
                            appDatabase.interfaceIndPropertyFloorsQuery().insertAll(Singleton.getInstance().indPropertyFloors.get(x));
                        }
                    }

                    // IndPropertyFloorsValuation - Room Delete
                    appDatabase.interfaceIndPropertyFloorsValuationQuery().deleteRow(caseid_int);
                    // IndPropertyFloorsValuation - Room Add
                    if (Singleton.getInstance().indPropertyFloorsValuations.size() > 0) {
                        for (int x = 0; x < Singleton.getInstance().indPropertyFloorsValuations.size(); x++) {
                            appDatabase.interfaceIndPropertyFloorsValuationQuery().insertAll(Singleton.getInstance().indPropertyFloorsValuations.get(x));
                        }
                    }

                    // IndPropertyFloorsValuation - Room Delete
                    appDatabase.interfaceProximityQuery().deleteRow(caseid_int);
                    // IndPropertyFloorsValuation - Room Add
                    if (Singleton.getInstance().proximities.size() > 0) {
                        for (int x = 0; x < Singleton.getInstance().proximities.size(); x++) {
                            /*if (x > 1) {
                                // TODO - set ID as 0 from Thrid row
                            }*/
                            Singleton.getInstance().proximities.get(x).setId(0);
                            appDatabase.interfaceProximityQuery().insertAll(Singleton.getInstance().proximities.get(x));
                        }
                    }

                    // update the offline caseID
                    UpdateOfflineStatusEditcase("" + caseid_int, "2");


                }
            }, 0);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    is_local = SettingsUtils.getInstance().getValue(SettingsUtils.is_local, false);
                    if (!is_local) {
                        general.CustomToast(mContext.getResources().getString(R.string.case_save_offline));
                    }
                    general.hideloading();
                    if (save_type.equalsIgnoreCase("savego")) {
                        Singleton.getInstance().openCaseList.clear();
                        Singleton.getInstance().closeCaseList.clear();
//                        Singleton.getInstance().call_offline_tab = "call_offline_tab";
                        if (is_offline) {
                            Singleton.getInstance().call_offline_tab = "call_offline_tab";
                        }
                        startActivity(new Intent(getContext(), HomeActivity.class));
                        getActivity().finish();
                    }
                }
            }, 500);


        }
    }

    private void parseSaveCaseInspectionResponse(String response) {

        // Re - Initial the curent_statusID as 0 for move to case to online
        Singleton.getInstance().curent_statusID = "0";

        DataResponse dataResponse = ResponseParser.parseSaveCaseInspectionResponse(response);
        String result = "";
        if (response != null) {
            result = dataResponse.status;
            msg = dataResponse.msg;
            info = dataResponse.info;
        }

        if (result.equals("2")) {
            general.CustomToast(msg);
            general.hideloading();
        }

        // Room - Delete - OfflineCase
        appDatabase.interfaceOfflineCaseQuery().deleteRow(caseid_int);
        // Room - Delete - Document_list
        appDatabase.interfaceDocumentListQuery().deleteRow(caseid_int);
        // Room - Delete - GetPhoto
        appDatabase.interfaceGetPhotoQuery().deleteRow(PropertyId_is);
        // Room - Delete - LatLong
        appDatabase.interfaceLatLongQuery().deleteRow(caseid_int);
        // Room Delete - Case
        appDatabase.interfaceCaseQuery().deleteRow(caseid_int);
        // Room Delete - Property
        appDatabase.interfacePropertyQuery().deleteRow(caseid_int);
        // Room Delete - Indproperty
        appDatabase.interfaceIndpropertyQuery().deleteRow(caseid_int);
        // Room Delete - IndPropertyValuation
        appDatabase.interfaceIndPropertyValuationQuery().deleteRow(caseid_int);
        // Room Delete - IndPropertyFloors
        appDatabase.interfaceIndPropertyFloorsQuery().deleteRow(caseid_int);
        // Room Delete - IndPropertyFloorsValuation
        appDatabase.interfaceIndPropertyFloorsValuationQuery().deleteRow(caseid_int);
        // Room Delete - Proximity
        appDatabase.interfaceProximityQuery().deleteRow(caseid_int);

        // Room Delete - Offline case
        DeleteOfflineDatabyCaseID("" + caseid_int);
        if (result != null) {
            if (result.equals("1")) {
                general.CustomToast(info);

                sendLatLongValueToServer();


                //floorgeneralfields = true;
                if (Singleton.getInstance().is_new_floor_created) {
                    /* Start Inspection - Update the case ID */
                    String case_id = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");
                    UpdateStatusCaseIdWebservice(case_id, getResources().getString(R.string.edit_inspection));
                } else {
                    general.hideloading();
                    if (save_type.equalsIgnoreCase("savego")) {
                        Singleton.getInstance().openCaseList.clear();
                        Singleton.getInstance().closeCaseList.clear();
                        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);
                        if (is_offline) {
                            Singleton.getInstance().call_offline_tab = "call_offline_tab";
                        }
                        startActivity(new Intent(getContext(), HomeActivity.class));
                        getActivity().finish();
                    }
                }
            } else if (result.equals("2")) {
                general.CustomToast(msg);
                general.hideloading();
            } else if (result.equals("0")) {

                try { //if case alredy move into report maker but still it editing in mobile to avoid this negative case.Handle below code
                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.has("status")) {
                        /* if case already send into report marker means below functionality get execute*/
                        if (jsonObject.getString("status").equals("3")) {
                            msg = "Case Already Moved to Next Stage";
                            Singleton.getInstance().openCaseList.clear();
                            Singleton.getInstance().closeCaseList.clear();
                            is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);
                            if (is_offline) {
                                Singleton.getInstance().call_offline_tab = "call_offline_tab";
                            }
                            startActivity(new Intent(getContext(), HomeActivity.class));
                            getActivity().finish();
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }


                general.CustomToast(msg);
                general.hideloading();
            }
        } else {
            general.CustomToast(mContext.getResources().getString(R.string.serverProblem));
            general.hideloading();
        }
    }

    private void DeleteOfflineDatabyCaseID(String case_id) {
        //make the offline case datamodel to delete the case
        if (MyApplication.getAppContext() != null) {
            if (appDatabase.interfaceOfflineDataModelQuery().getDataModels().size() > 0) {
                int value = appDatabase.interfaceOfflineDataModelQuery().DeleteOfflineDatabyCaseid(case_id);
                Log.e("delete offline case row", value + "");
            }
        }
    }

    private void sendLatLongValueToServer() {
        if (SettingsUtils.Latitudes < 0.0) {
            getCurrentLocation(getActivity());
        } else {
            new LocationTrackerApi(getActivity()).shareLocation(SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "")
                    , SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""), "Field Inspection Submit", SettingsUtils.Latitudes, SettingsUtils.Longitudes, "", 1);

        }
    }

    private void getCurrentLocation(Activity activity) {

        if (general.GPS_status()) {
            try {
                GPSService gpsService = new GPSService(activity);
                gpsService.getLocation();
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (general.getcurrent_latitude(activity) != 0) {
                            /*Here store current location of user latLong*/
                            SettingsUtils.Longitudes = general.getcurrent_longitude(activity);
                            SettingsUtils.Latitudes = general.getcurrent_latitude(activity);
                            SettingsUtils.getInstance().putValue("lat", String.valueOf(general.getcurrent_latitude(activity)));
                            SettingsUtils.getInstance().putValue("long", String.valueOf(general.getcurrent_longitude(activity)));

                            new LocationTrackerApi(getActivity()).shareLocation(SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, ""), SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""), "Field Inspection Submit", SettingsUtils.Latitudes, SettingsUtils.Longitudes, "", 1);

                        }
                    }
                }, 1500);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    // Status Edit inspection Update  API call //
    private void UpdateStatusCaseIdWebservice(String case_id, String statusId) {

        if (Connectivity.isConnected(mContext)) {
            InitiateUpdateStatusCaseidTask(case_id, statusId);
        } else {
            Connectivity.showNoConnectionDialog(mContext);
            general.hideloading();
        }
    }

    private void InitiateUpdateStatusCaseidTask(String case_id, String statusId) {

        String url = general.ApiBaseUrl() + SettingsUtils.UpdateCaseStatusById;
        // String url = SettingsUtils.BASE_URL + SettingsUtils.UpdateCaseStatusById;
        String modifiedby = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, "");
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setCaseId(case_id);//case_id
        requestData.setModifiedBy(modifiedby);
        requestData.setStatus(statusId);//statusId
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        requestData.setRequestBody(RequestParam.UpdateCaseStatusRequestParams(requestData));

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(), requestData, SettingsUtils.PUT_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                if (requestData.isSuccessful()) {
                    parseUpdateStatusCaseidResponse(requestData.getResponse());
                } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
                    general.hideloading();
                    General.sessionDialog(getActivity());
                } else {
                    general.hideloading();
                    General.customToast(getActivity().getString(R.string.something_wrong), getActivity());
                }
            }
        });
        webserviceTask.execute();
    }

    private void parseUpdateStatusCaseidResponse(String response) {

        DataResponse dataResponse = ResponseParser.parseUpdateStatusCaseResponse(response);
        String result = "";
        if (response != null) {
            result = dataResponse.status;
            msg = dataResponse.msg;
            info = dataResponse.info;
            Singleton.getInstance().updateCaseStatusModel = dataResponse.updateCaseStatusModel;
        }

        if (result != null) {
            if (result.equals("1")) {
                if (save_type.equalsIgnoreCase("savego")) {
                    Singleton.getInstance().openCaseList.clear();
                    Singleton.getInstance().closeCaseList.clear();
                    startActivity(new Intent(getContext(), HomeActivity.class));
                    getActivity().finish();
                }
            } else if (result.equals("2")) {
                general.CustomToast(msg);
            } else if (result.equals("0")) {
                general.CustomToast(msg);
            }
        } else {
            general.CustomToast(mContext.getResources().getString(R.string.serverProblem));
        }
        general.hideloading();
    }

    // Todo update Start to Edit Inspecction
    private void UpdateOfflineStatusEditcase(String case_id, String updateCaseStatus) {
        if (appDatabase.interfaceOfflineDataModelQuery().getDataModels().size() > 0) {
            appDatabase.interfaceOfflineDataModelQuery().updateOfflineCaseStatus(updateCaseStatus, case_id);
            // update the case for casemodal
            appDatabase.interfaceCaseQuery().updateCaseStatus(updateCaseStatus, case_id);
        }
    }

    private void getProximityListData() {

        caseid = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");

        JSONArray jsonArray = new JSONArray();
        ArrayList<Proximity> proximityArrayList = new ArrayList<>();
        if(listAdapter!=null){
            list = listAdapter.getStepList();
            for (int j = 0; j < list.size(); j++) {
                if (Singleton.getInstance().proximities.size() > 0) {
                    // Update from the proximity values
                    Proximity proximity = new Proximity();
                    proximity.setId(list.get(j).getId());
                    proximity.setCaseId(Integer.valueOf(caseid)); //Integer.value(caseid);
                    proximity.setProximityId(list.get(j).getProximityId());
                    proximity.setProximityName(list.get(j).getProximityName());
                    proximity.setProximityDistance(list.get(j).getProximityDistance());
                    proximityArrayList.add(proximity);
                } else {
                    // Add New proximity
                    try {
                        JSONObject proximityObj = new JSONObject();
                        proximityObj.put("CaseId    ", caseid);
                        proximityObj.put("ProximityId", list.get(j).getProximityId());
                        proximityObj.put("ProximityName", list.get(j).getProximityName());
                        proximityObj.put("ProximityDistance", list.get(j).getProximityDistance());
                        jsonArray.put(proximityObj);

                        Proximity proximity = new Proximity();
                        proximity.setCaseId(Integer.valueOf(caseid)); //list.get(j).getProximityId()
                        proximity.setProximityId(list.get(j).getProximityId());
                        proximity.setProximityName(list.get(j).getProximityName());
                        proximity.setProximityDistance(list.get(j).getProximityDistance());
                        proximityArrayList.add(proximity);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                Singleton.getInstance().proximities = proximityArrayList;

            }

        }
 }

    private void getGeneralInputData() {

        caseid = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");
        Singleton.getInstance().property.setCaseId(Integer.parseInt(caseid));
        Singleton.getInstance().indProperty.setCaseId(Integer.parseInt(caseid));

        if (llPurpose.getVisibility() == View.VISIBLE) getPurpose();

        if (llNameOfBorrower.getVisibility() == View.VISIBLE) getNameOfBorrower();

        if (llNameOfSeller.getVisibility() == View.VISIBLE) getNameOfSeller();

        if (llNameOfOwner.getVisibility() == View.VISIBLE) getNameOfOwner();


        if (ll_calender.getVisibility() == View.VISIBLE) {
            if (Singleton.getInstance().property != null) {

                if (!general.isEmpty(visitDate)) {
                    siteVisiteInCalender = siteVisitDateToConversion(visitDate);
                    Singleton.getInstance().aCase.setSiteVisitDate(siteVisiteInCalender);
                } else {
                    Singleton.getInstance().aCase.setSiteVisitDate("");
                }


                if (!general.isEmpty(etPersonName.getText().toString())) {
                    Singleton.getInstance().aCase.setContactPersonName(etPersonName.getText().toString());
                } else {
                    Singleton.getInstance().aCase.setContactPersonName("");
                }

                if (!general.isEmpty(etPersonNo.getText().toString())) {
                    Singleton.getInstance().aCase.setContactPersonNumber(etPersonNo.getText().toString());
                } else {
                    Singleton.getInstance().aCase.setContactPersonNumber("");
                }
            }
        }


        if (til_seller_type.getVisibility() == View.VISIBLE) {
            if (Singleton.getInstance().indProperty != null) {
                if (!general.isEmpty(et_seller_type.getText().toString()))
                    Singleton.getInstance().indProperty.setTypeofSeller(et_seller_type.getText().toString());
                else Singleton.getInstance().indProperty.setTypeofSeller("");
            }
        }


        if (tl_finnonId.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_finnonId.getText().toString().trim())) {
                Singleton.getInstance().aCase.setBankReferenceNO(et_finnonId.getText().toString());
            }
        }


    }

    private void getLocalityInputData() {



        /* Address*/
        if (tl_complete_address.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(editText_addr_perdoc.getText().toString())) {
                Singleton.getInstance().aCase.setPropertyAddress(editText_addr_perdoc.getText().toString().trim());
            } else {
                Singleton.getInstance().aCase.setPropertyAddress("");
            }
        }


        if (tl_unit_no.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(editText_unit_no.getText().toString().trim())) {
                Singleton.getInstance().property.setUnitNo(editText_unit_no.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setUnitNo("");
            }
        }

        if (tl_plot_no.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(editText_plotno.getText().toString().trim())) {
                Singleton.getInstance().property.setPlotNo(editText_plotno.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setPlotNo("");
            }
        }


        if (tl_survey_no.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(editText_surveyno.getText().toString().trim())) {
                Singleton.getInstance().property.setSurveyNo(editText_surveyno.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setSurveyNo("");
            }
        }


        if (tl_landmark.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(editText_landmark.getText().toString().trim())) {
                Singleton.getInstance().property.setLandmark(editText_landmark.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setLandmark("");
            }
        }


        if (tl_village_post.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_village_post.getText().toString().trim())) {
                Singleton.getInstance().aCase.setVillageName(et_village_post.getText().toString().trim());
            } else {
                Singleton.getInstance().aCase.setVillageName("");
            }
        }


        if (tl_taluka.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_taluka.getText().toString().trim())) {
                Singleton.getInstance().aCase.setTaluka(et_taluka.getText().toString().trim());
            } else {
                Singleton.getInstance().aCase.setTaluka("");
            }
        }


        if (til_district.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_district.getText().toString().trim())) {
                Singleton.getInstance().aCase.setDistrict(et_district.getText().toString().trim());
            } else {
                Singleton.getInstance().aCase.setDistrict("");
            }
        }


        if (tl_pincode.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(etPinCode.getText().toString().trim())) {
                Singleton.getInstance().aCase.setPincode(Integer.parseInt(etPinCode.getText().toString().trim()));
            }
        }


        /* As per document */
        if (ll_as_per_doc.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(editText_db_east.getText().toString().trim())) {
                Singleton.getInstance().property.setDocBoundryEast(editText_db_east.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setDocBoundryEast("");
            }
            if (!general.isEmpty(editText_db_west.getText().toString().trim())) {
                Singleton.getInstance().property.setDocBoundryWest(editText_db_west.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setDocBoundryWest("");
            }
            if (!general.isEmpty(editText_db_north.getText().toString().trim())) {
                Singleton.getInstance().property.setDocBoundryNorth(editText_db_north.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setDocBoundryNorth("");
            }
            if (!general.isEmpty(editText_db_south.getText().toString().trim())) {
                Singleton.getInstance().property.setDocBoundrySouth(editText_db_south.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setDocBoundrySouth("");
            }
        }


        /* As per site - building / house */
        if (ll_as_per_site.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(editText_ab_east.getText().toString().trim())) {
                Singleton.getInstance().property.setBoundryEast(editText_ab_east.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setBoundryEast("");
            }
            if (!general.isEmpty(editText_ab_west.getText().toString().trim())) {
                Singleton.getInstance().property.setBoundryWest(editText_ab_west.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setBoundryWest("");
            }
            if (!general.isEmpty(editText_ab_north.getText().toString().trim())) {
                Singleton.getInstance().property.setBoundryNorth(editText_ab_north.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setBoundryNorth("");
            }
            if (!general.isEmpty(editText_ab_south.getText().toString().trim())) {
                Singleton.getInstance().property.setBoundrySouth(editText_ab_south.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setBoundrySouth("");
            }

        }

        getFlatTypeLocality();

        /* Type of Locality */
        if (ll_locality.getVisibility() == View.VISIBLE) {
            int spinner_typeoflocality_posClass = spinner_typeoflocality.getSelectedItemPosition();
            if (spinner_typeoflocality_posClass > 0) {
                int classId = Singleton.getInstance().localities_list.get(spinner_typeoflocality_posClass).getTypeLocalityId();
                if (classId != 0) Singleton.getInstance().property.setTypeLocalityId(classId);
            } else {
                Singleton.getInstance().property.setTypeLocalityId(null);
            }
        }

        if (ll_proximity.getVisibility() == View.VISIBLE) {
            getProximityListData();
        }


        if (til_adverse_nearby.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_adverse_nearby.getText().toString())) {
                Singleton.getInstance().property.setAdverseFeatureNearby(et_adverse_nearby.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setAdverseFeatureNearby("");
            }
        }


        if (tl_habitation.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_habitation.getText().toString())) {
                Singleton.getInstance().property.setHabitationPercentageinLocality(et_habitation.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setHabitationPercentageinLocality("");
            }
        }


    }

    private void getPropertyInputData() {

        if (ll_ownership.getVisibility() == View.VISIBLE) {
            int posOwnership = spinner_select_tenure_ownership.getSelectedItemPosition();
            if (posOwnership > 0) {
                int id = Singleton.getInstance().ownershipTypes_list.get(posOwnership).getId();
                if (id != 0) Singleton.getInstance().aCase.setTypeofOwnershipDd(id);
            } else {
                Singleton.getInstance().aCase.setTypeofOwnershipDd(0);
            }
        }

            if (spinner_landapprovedfor.getVisibility() == View.VISIBLE) {
                int landapprovedfor = spinner_landapprovedfor.getSelectedItemPosition();
                if (landapprovedfor > 0) {
                    int id = Singleton.getInstance().land_list.get(landapprovedfor).getTypeOfLandId();
                    if (id != 0) Singleton.getInstance().property.setTypeOfLandId(id);
                } else {
                    Singleton.getInstance().property.setTypeOfLandId(null);
                }
            }


        if (ll_contact_info_property.getVisibility() == View.VISIBLE) {
            if (!General.isEmpty(etPersonName_property.getText().toString())) {
                Singleton.getInstance().aCase.setContactPersonName(etPersonName_property.getText().toString());
            } else {
                Singleton.getInstance().aCase.setContactPersonName("");
            }

            if (!General.isEmpty(etPersonNo_property.getText().toString())) {
                Singleton.getInstance().aCase.setContactPersonNumber(etPersonNo_property.getText().toString());
            } else {
                Singleton.getInstance().aCase.setContactPersonNumber("");
            }
        }


        if (checkbox_sanctioned_plan.getVisibility() == View.VISIBLE) {
            Singleton.getInstance().property.setIsConstructionDoneAsPerSanctionedPlan(checkbox_sanctioned_plan.isChecked());
        }


        if (tl_ground_coverage.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_ground.getText().toString()))
                Singleton.getInstance().indProperty.setGroundCoverage(et_ground.getText().toString());
            else Singleton.getInstance().indProperty.setGroundCoverage("");
        }


        if (layout_engineer_license.getVisibility() == View.VISIBLE) {
            if (!General.isEmpty(et_engineer_license.getText().toString())) {
                Singleton.getInstance().aCase.setArchitectEngineerLicenseNo(et_engineer_license.getText().toString());
                Singleton.getInstance().indProperty.setArchitectEngineerLicenseNo(et_engineer_license.getText().toString());
            } else {
                Singleton.getInstance().aCase.setArchitectEngineerLicenseNo("");
                Singleton.getInstance().indProperty.setArchitectEngineerLicenseNo("");
            }
        }


        if (llPropertyRbtn.getVisibility() == View.VISIBLE) {
            boolean check_demolish = checkbox_isproperty_demolish.isChecked();
            Singleton.getInstance().property.setIsPropertyInDemolitionList(check_demolish);
            if (checkbox_isproperty_demolish.isChecked()) {
                if (getRgDemoLish.getCheckedRadioButtonId() != -1) {
                    int btn = getRgDemoLish.getCheckedRadioButtonId();
                    if (btn == R.id.high) {
                        Singleton.getInstance().property.setDemolitionListValue("High");
                    } else if (btn == R.id.medium) {
                        Singleton.getInstance().property.setDemolitionListValue("Medium");
                    } else if (btn == R.id.low) {
                        Singleton.getInstance().property.setDemolitionListValue("Low");
                    } else {
                        Singleton.getInstance().property.setDemolitionListValue("");
                    }
                } else {
                    Singleton.getInstance().property.setDemolitionListValue("");
                }


            }
        }


        if (tl_authority.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_authority.getText().toString().trim())) {
                Singleton.getInstance().aCase.setApprovedPlanApprovingAuthority(et_authority.getText().toString().trim());
            }
        }


        if (tl_plan_prepared_by.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_plan_prepared_by.getText().toString().trim())) {
                Singleton.getInstance().aCase.setApprovedPlanPreparedBy(et_plan_prepared_by.getText().toString().trim());
            } else {
                Singleton.getInstance().aCase.setApprovedPlanPreparedBy("");
            }
        }

        if (ll_planno_date.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_approved_plan_no.getText().toString().trim())) {
                Singleton.getInstance().aCase.setApprovedPlanNumber(et_approved_plan_no.getText().toString().trim());
            } else {
                Singleton.getInstance().aCase.setApprovedPlanNumber("");
            }/* plan date */
            if (!general.isEmpty(et_approved_plan_date.getText().toString().trim())) {
                Singleton.getInstance().aCase.setApprovedPlanDate(et_approved_plan_date.getText().toString().trim());
            } else {
                Singleton.getInstance().aCase.setApprovedPlanDate("");
            }
        }



        /* type of the property */
       /* int typeOfProperty = spinner_property.getSelectedItemPosition();
        if (typeOfProperty > 0) {
            int landId = Singleton.getInstance().typeOfProperty.get(typeOfProperty).getTypeOfPropertyId();
            if (landId != 0)
                Singleton.getInstance().property.setTypeOfPropertyId(landId);
        } else {
            //Singleton.getInstance().property.setTypeOfPropertyId(null);
        }*/


    }

    private void getPropertyDetailsInputData() {

        if (checkbox_lift_in_building.getVisibility() == View.VISIBLE)
            Singleton.getInstance().indProperty.setLiftInBuilding(checkbox_lift_in_building.isChecked());


        if (tl_avg_construction_per.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(etAverageConstruction.getText().toString()))
                Singleton.getInstance().indPropertyValuation.setAverageConstructionPercentage(etAverageConstruction.getText().toString());
            else Singleton.getInstance().indPropertyValuation.setAverageConstructionPercentage("");
        }


        if (tl_recommendation_per.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(etRecommendationPercentage.getText().toString())) {
                Singleton.getInstance().indPropertyValuation.setRecommendationPercentage(etRecommendationPercentage.getText().toString());
            }
        }

        if (!general.isEmpty(et_construction_stage.getText().toString())) {
            Singleton.getInstance().indProperty.setDescriptionofConstructionStage(et_construction_stage.getText().toString());
        }




        /* Maintaince of the property */
        if (ll_maintenace_of_building.getVisibility() == View.VISIBLE) {
            int spinner_maintenanceofbuildingSelectedItemPosition = spinner_maintenanceofbuilding.getSelectedItemPosition();
            if (spinner_maintenanceofbuildingSelectedItemPosition > 0) {
                int landId = Singleton.getInstance().maintenances_list.get(spinner_maintenanceofbuildingSelectedItemPosition).getMaintenanceOfBuildingId();
                if (landId != 0)
                    Singleton.getInstance().indProperty.setMaintenanceOfBuildingId(landId);
            } else {
                Singleton.getInstance().indProperty.setMaintenanceOfBuildingId(null);
            }
        }

        /*Name of the occupant*/
        if (tl_name_of_occupant.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_occupant_name.getText().toString())) {
                Singleton.getInstance().property.setNameofOccupant(et_occupant_name.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setNameofOccupant("");
            }
        }


        /* name of multiple kitchens */
        if (tl_no_of_multiple_kitchens.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_multiple_kitchen.getText().toString())) {
                Singleton.getInstance().indProperty.setNumberofMultiplekitchens(et_multiple_kitchen.getText().toString());
            }
        }


    }

    private void getNDMAParameters() {


        if (ll_masonry.getVisibility() == View.VISIBLE) {
            if (spinner_masonry.getSelectedItemPosition() > 0) {
                Singleton.getInstance().indProperty.setTypeofMasonryId(Singleton.getInstance().typeOfMasonryList.get(spinner_masonry.getSelectedItemPosition()).getTypeofmasonryId());
            } else {
                Singleton.getInstance().indProperty.setTypeofMasonryId(0);
            }
        }


        if (ll_typeOfMotor.getVisibility() == View.VISIBLE) {
            if (spinner_mortar.getSelectedItemPosition() > 0) {
                Singleton.getInstance().indProperty.setTypeofMortarId(Singleton.getInstance().typeOfMortarsList.get(spinner_mortar.getSelectedItemPosition()).getTypeofmortarId());
            } else Singleton.getInstance().indProperty.setTypeofMortarId(0);
        }


        if (ll_concrete.getVisibility() == View.VISIBLE) {
            if (spinner_concrete_grade.getSelectedItemPosition() > 0) {
                Singleton.getInstance().indProperty.setConcreteGradeDd(Singleton.getInstance().concreteGrade.get(spinner_concrete_grade.getSelectedItemPosition()).getId());
            } else {
                Singleton.getInstance().indProperty.setConcreteGradeDd(0);
            }
        }


        /* expansion joint available */
        if (checkbox_expansion_joint.getVisibility() == View.VISIBLE)
            Singleton.getInstance().indProperty.setWhetherExpansionJointAvailable(checkbox_expansion_joint.isChecked());


        /* Environment exposure condition */
        if (ll_env.getVisibility() == View.VISIBLE) {
            if (spinner_environment_exposure_condition.getSelectedItemPosition() > 0) {
                Singleton.getInstance().indProperty.setEnvironmentExposureConditionDd(Singleton.getInstance().envExposureConditionData.get(spinner_environment_exposure_condition.getSelectedItemPosition()).getId());
            } else {
                Singleton.getInstance().indProperty.setEnvironmentExposureConditionDd(0);
            }
        }


        /* Cyclone zone wind speed */
        if (tl_cyclone_zone.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_cyclone_zone.getText().toString())) {
                Singleton.getInstance().property.setCycloneZone(et_cyclone_zone.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setCycloneZone("");
            }
        }


        /* seismic zone */
        if (tl_seismic_zone.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_seismic.getText().toString())) {
                Singleton.getInstance().property.setSeismicZone(et_seismic.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setSeismicZone("");
            }
        }



        /* Flood prone area */
        if (tl_flood_prone_area.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_flood_zone.getText().toString())) {
                Singleton.getInstance().property.setFloodProneZone(et_flood_zone.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setFloodProneZone("");
            }
        }


        /* is project part available */
        if (checkbox_projected_part.getVisibility() == View.VISIBLE) {
            Singleton.getInstance().indProperty.setProjectedPartAvailable(checkbox_projected_part.isChecked());
        }


        /*Type of soil*/
        if (ll_type_soil.getVisibility() == View.VISIBLE) {
            if (spinner_soil_type.getSelectedItemPosition() > 0) {
                Singleton.getInstance().indProperty.setSoilTypeDd(Singleton.getInstance().soilTypeData.get(spinner_soil_type.getSelectedItemPosition()).getId());
            } else {
                Singleton.getInstance().indProperty.setSoilTypeDd(0);
            }
        }


        /* plan aspect ratio */
        if (tl_plan_aspect_ratio.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_aspect_ratio.getText().toString())) {
                Singleton.getInstance().indProperty.setPlanAspectRatio(et_aspect_ratio.getText().toString());
            } else {
                Singleton.getInstance().indProperty.setPlanAspectRatio("");
            }
        }


        /*Coastal regulatory zone*/
        if (tl_costal_regulatory_zone.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_regulatory_zone.getText().toString())) {
                Singleton.getInstance().property.setCoastalRegulatoryZone(et_regulatory_zone.getText().toString().trim());
            } else {
                Singleton.getInstance().property.setCoastalRegulatoryZone("");
            }
        }


        /* Footing/foundation type */
        if (ll_footing_foundation.getVisibility() == View.VISIBLE) {
            if (spinner_foundation.getSelectedItemPosition() > 0) {
                Singleton.getInstance().indProperty.setTypeofFootingFoundationId(Singleton.getInstance().typeOfFootingList.get(spinner_foundation.getSelectedItemPosition()).getTypeoffootingfoundationId());
            } else {
                Singleton.getInstance().indProperty.setTypeofFootingFoundationId(0);
            }
        }


        /* number of floors of building */
        if (tl_no_of_floors_above_ground.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_above_ground.getText().toString())) {
                Singleton.getInstance().indProperty.setNoofFloorsAboveGround(et_above_ground.getText().toString());
            } else {
                Singleton.getInstance().indProperty.setNoofFloorsAboveGround("");
            }
        }


        /* base of building*/
        if (tl_basement.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_basement.getText().toString())) {
                Singleton.getInstance().indProperty.setBasementDetails(et_basement.getText().toString());
            } else {
                Singleton.getInstance().indProperty.setBasementDetails("");
            }
        }


        /* is fire exit */
        if (cb_fire_exit.getVisibility() == View.VISIBLE)
            Singleton.getInstance().indProperty.setFireExitData(cb_fire_exit.isChecked());

        /* Type of Steel Grade */
        if (ll_steel_grade.getVisibility() == View.VISIBLE) {
            if (spinner_steel.getSelectedItemPosition() > 0) {
                Singleton.getInstance().indProperty.setTypeofSteelGradeId(Singleton.getInstance().typeOfSteelList.get(spinner_steel.getSelectedItemPosition()).getTypeofsteelgradeId());
            } else {
                Singleton.getInstance().indProperty.setTypeofSteelGradeId(0);
            }
        }


        if (cb_ground_slope_more_than.getVisibility() == View.VISIBLE)
            Singleton.getInstance().indProperty.setGroundSlopeData(cb_ground_slope_more_than.isChecked());

        if (checkbox_liquefiable.getVisibility() == View.VISIBLE)
            Singleton.getInstance().indProperty.setIsSoilLiquefiable(checkbox_liquefiable.isChecked());

        if (checkbox_hill_slope.getVisibility() == View.VISIBLE)
            Singleton.getInstance().property.setIsInHillSlope(checkbox_hill_slope.isChecked());

    }


    private void getMarketInputData() {
        int spinner_marketabilitySelectedItemPosition = spinner_marketability.getSelectedItemPosition();
        if (spinner_marketabilitySelectedItemPosition > 0) {
            int landId = Singleton.getInstance().marketablities_list.get(spinner_marketabilitySelectedItemPosition).getMarketabilityId();
            if (landId != 0) Singleton.getInstance().indProperty.setMarketabilityId(landId);
        } else {
            Singleton.getInstance().indProperty.setMarketabilityId(null);
        }
    }

    private void getRemarkInputData() {
        if (tl_recommendation_per_remark.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(etRecommendationPercentage_remark.getText().toString())) {
                Singleton.getInstance().indPropertyValuation.setRecommendationPercentage(etRecommendationPercentage_remark.getText().toString());
            } else {
                Singleton.getInstance().indPropertyValuation.setRecommendationPercentage("");
            }
        }

        if (tl_construction_stage_remark.getVisibility() == View.VISIBLE) {
            if (!general.isEmpty(et_construction_stage_remark.getText().toString())) {
                Singleton.getInstance().indProperty.setDescriptionofConstructionStage(et_construction_stage_remark.getText().toString());
            } else {
                Singleton.getInstance().indProperty.setDescriptionofConstructionStage("");
            }
        }

        String remarks_id_ = general.remove_array_brac_and_space(Singleton.getInstance().Remarks_Id.toString());
        Log.e("other_save", "remarks_id_is: " + remarks_id_);
        Singleton.getInstance().property.setRemarks(remarks_id_);
        // OtherRemarks
        String OtherRemarks = editText_additional_remarks.getText().toString();
        if (!general.isEmpty(OtherRemarks)) {
            Singleton.getInstance().property.setOtherRemarks(OtherRemarks.trim());
        } else {
            Singleton.getInstance().property.setOtherRemarks("");
        }
        Log.e("other_save", "OtherRemarks: " + Singleton.getInstance().property.getOtherRemarks());
        // SpecialRemarks
        String SpecialRemarks = editText_special_remarks.getText().toString();
        if (!general.isEmpty(SpecialRemarks)) {
            Singleton.getInstance().property.setSpecialRemarks(SpecialRemarks.trim());
        } else {
            Singleton.getInstance().property.setSpecialRemarks("");
        }

        String additionalDoc = et_additional_document.getText().toString();
        if (!general.isEmpty(additionalDoc)) {
            Singleton.getInstance().property.setOtherDocuments(additionalDoc.trim());
        } else {
            Singleton.getInstance().property.setOtherDocuments("");
        }
    }

    private void initShowUIApi() {

        if (general.isNetworkAvailable()) {
            String url = general.ApiBaseUrl() + SettingsUtils.ShowFSUIRequest;
            String caseID = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);
            requestData.setCaseID(caseID); // id
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            requestData.setUrl(RequestParam.FetchFSVisibleUI(requestData));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(mContext, requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData((TaskCompleteListener<JsonRequestData>) requestData1 -> {
                if (requestData1.isSuccessful()) {
                    RequestApiStatus requestApiStatus = new Gson().fromJson(requestData1.getResponse(), RequestApiStatus.class);
                    if (requestApiStatus.getStatus() == 1) {
                        ShowFSUIResponse showFSUIResponse = new Gson().fromJson(requestData1.getResponse(), ShowFSUIResponse.class);

                        Log.e("ShowUIBasedOnProperty= ", new Gson().toJson(requestData1.getResponse()));

                        validateAndShowUI(showFSUIResponse.getData());
                    } else if (requestApiStatus.getStatus() == 2) {
                        General.customToast("Unable to fetch details from server", getActivity());
                    }
                } else {
                    General.customToast("Unable to fetch details from server", getActivity());
                }
            });
            webserviceTask.execute();
        } else {
            if(ResponseStorage.getInstance().getSavedResponse()!=null){
                ShowFSUIResponse savedResponse = ResponseStorage.getInstance().getSavedResponse();
                if(savedResponse.getData()!=null){
                validateAndShowUIOffline(savedResponse.getData());
            }}
            General.customToast("Please check your Internet Connection!", getActivity());
        }
    }

    private void initFlatType() {
        if (!general.isEmpty(Singleton.getInstance().indProperty.getUnitBoundryPerActEast())) {
            etUnitActualEast.setText(Singleton.getInstance().indProperty.getUnitBoundryPerActEast());
        }
        if (!general.isEmpty(Singleton.getInstance().indProperty.getUnitBoundryPerActWest())) {
            etUnitActualWest.setText(Singleton.getInstance().indProperty.getUnitBoundryPerActWest());
        }
        if (!general.isEmpty(Singleton.getInstance().indProperty.getUnitBoundryPerActNorth())) {
            etUnitActualNorth.setText(Singleton.getInstance().indProperty.getUnitBoundryPerActNorth());
        }
        if (!general.isEmpty(Singleton.getInstance().indProperty.getUnitBoundryPerActSouth())) {
            etUnitActualSouth.setText(Singleton.getInstance().indProperty.getUnitBoundryPerActSouth());
        }
    }

    private void getFlatTypeLocality() {
        if (!general.isEmpty(etUnitActualEast.getText().toString())) {
            Singleton.getInstance().indProperty.setUnitBoundryPerActEast(etUnitActualEast.getText().toString());
        } else {
            Singleton.getInstance().indProperty.setUnitBoundryPerActEast("");
        }

        if (!general.isEmpty(etUnitActualWest.getText().toString())) {
            Singleton.getInstance().indProperty.setUnitBoundryPerActWest(etUnitActualWest.getText().toString());
        } else {
            Singleton.getInstance().indProperty.setUnitBoundryPerActWest("");
        }

        if (!general.isEmpty(etUnitActualNorth.getText().toString())) {
            Singleton.getInstance().indProperty.setUnitBoundryPerActNorth(etUnitActualNorth.getText().toString());
        } else {
            Singleton.getInstance().indProperty.setUnitBoundryPerActNorth("");
        }

        if (!general.isEmpty(etUnitActualSouth.getText().toString())) {
            Singleton.getInstance().indProperty.setUnitBoundryPerActSouth(etUnitActualSouth.getText().toString());
        } else {
            Singleton.getInstance().indProperty.setUnitBoundryPerActSouth("");
        }
    }

    private void getCommonFsInputData() {
        getProximityListData();
        getGeneralInputData();
        getLocalityInputData();
        getPropertyInputData();
        getPropertyDetailsInputData();
        if (!isNPA) {
            getNDMAParameters();
        }


        getMarketInputData();
        getRemarkInputData();

        Singleton.getInstance().indProperty.setCaseId(Singleton.getInstance().aCase.getCaseId());
        Singleton.getInstance().indPropertyValuation.setCaseId(Singleton.getInstance().aCase.getCaseId());
        Singleton.getInstance().property.setCaseId(Singleton.getInstance().aCase.getCaseId());
    }

    private void validateAndShowUI(ArrayList<ShowFSUIResponse.Datum> data) {
        Singleton.getInstance().uiData.clear();
        Singleton.getInstance().uiData = data;
        setFsSurveyData();
    }

    private void validateAndShowUIOffline(ArrayList<ShowFSUIResponse.Datum> data) {
        Singleton.getInstance().uiData = data;
        setFsSurveyData();
    }


    private void localityDataMapping() {
        // spinner - Locality
        ArrayAdapter<Locality> localityArrayAdapter = new ArrayAdapter<Locality>(getActivity(), R.layout.row_spinner_item, Singleton.getInstance().localities_list) {
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
        localityArrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_typeoflocality.setAdapter(localityArrayAdapter);
        //     spinner_typeoflocality_property.setAdapter(localityArrayAdapter); //regional development in property section
        spinner_typeoflocality.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().property.getTypeLocalityId()))) {
            Log.e("spinner_Locality", "::: " + Singleton.getInstance().property.getTypeLocalityId());
            if (Singleton.getInstance().property.getTypeLocalityId() != 0) {
                ArrayList<Locality> qualityConstructions_ = new ArrayList<>();
                qualityConstructions_ = Singleton.getInstance().localities_list;
                for (int x = 0; x < qualityConstructions_.size(); x++) {
                    if (qualityConstructions_.get(x).getTypeLocalityId() == Singleton.getInstance().property.getTypeLocalityId()) {
                        spinner_typeoflocality.setSelection(x);
                        //      spinner_typeoflocality_property.setSelection(x);
                        fsProgressCount = fsProgressCount + 1;
                        //regional development in property section
                    }
                }
            }
        }
    }

    private void demolishUIMapping() {

        checkbox_isproperty_demolish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llPropertyRbtn.setVisibility(View.VISIBLE);
                } else {
                    llPropertyRbtn.setVisibility(View.GONE);
                    getRgDemoLish.clearCheck();
                }
            }
        });

        if (Singleton.getInstance().property.getIsPropertyInDemolitionList() != null) {
            checkbox_isproperty_demolish.setChecked(Singleton.getInstance().property.getIsPropertyInDemolitionList());
            if (Singleton.getInstance().property.getIsPropertyInDemolitionList()) {
                llPropertyRbtn.setVisibility(View.VISIBLE);

                if (Singleton.getInstance().property.getDemolitionListValue() != null && !Singleton.getInstance().property.getDemolitionListValue().isEmpty()) {

                    String value = Singleton.getInstance().property.getDemolitionListValue();
                    RadioButton rbn = null;
                    if (value.equalsIgnoreCase("high")) {
                        rbn = getRgDemoLish.findViewById(R.id.high);
                    } else if (value.equalsIgnoreCase("medium")) {
                        rbn = getRgDemoLish.findViewById(R.id.medium);
                    } else if (value.equalsIgnoreCase("low")) {
                        rbn = getRgDemoLish.findViewById(R.id.low);
                    }
                    if (rbn != null) {
                        rbn.setChecked(true);
                        fsProgressCount = fsProgressCount + 1;
                    }
                }
            } else {
                llPropertyRbtn.setVisibility(View.GONE);
            }
        }
    }

    private void typeOfMasonry() {
        ArrayAdapter<TypeOfMasonry.Datum> adapterMasonry = new ArrayAdapter<TypeOfMasonry.Datum>(mContext, R.layout.row_spinner_item, Singleton.getInstance().typeOfMasonryList) {

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

        adapterMasonry.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spinner_masonry.setAdapter(adapterMasonry);
        spinner_masonry.setOnTouchListener(this);

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().indProperty.getTypeofMasonryId()))) {
            Log.e("spinner_qualityofcon", "::: " + Singleton.getInstance().indProperty.getTypeofMasonryId());
            for (int x = 0; x < Singleton.getInstance().typeOfMasonryList.size(); x++) {

                if (Singleton.getInstance().typeOfMasonryList.get(x).getTypeofmasonryId() != null)
                    if (Singleton.getInstance().indProperty.getTypeofMasonryId().equals(Singleton.getInstance().typeOfMasonryList.get(x).getTypeofmasonryId())) {
                        spinner_masonry.setSelection(x);
                        break;
                    }
            }
        }
    }

    private void setFsSurveyData() {
        initGeneral();
        initLocality();
        initPropertyValues();
        if (property_type.equalsIgnoreCase("land") && isNPA) {
            cardViewPropertyDetails.setVisibility(View.GONE);

        } else {
            cardViewPropertyDetails.setVisibility(View.VISIBLE);
            initPropertyDetailsValue();

        }

        if (!isNPA) {
            cardViewNdmaParamter.setVisibility(View.VISIBLE);
            initNDMAParameters();
        } else {
            cardViewNdmaParamter.setVisibility(View.GONE);
        }


        initiatePropertyFragmentViews();

        initRemarkData();

        updateFsProgress();
    }

    void initRemarkData() {

        if (general.getUIVisibility("Recommendation Percentage") && general.getSectionID("Recommendation Percentage") != 4) {
            tl_recommendation_per_remark.setVisibility(View.VISIBLE);
            if (!general.isEmpty(Singleton.getInstance().indPropertyValuation.getRecommendationPercentage())) {
                fsProgressCount = fsProgressCount + 1;
                etRecommendationPercentage_remark.setText(Singleton.getInstance().indPropertyValuation.getRecommendationPercentage());
            }
        } else tl_recommendation_per_remark.setVisibility(View.GONE);


        if (general.getUIVisibility("Average construction percentage,Description of construction stage") && general.getSectionID("Average construction percentage,Description of construction stage") != 4) {
            if (property_type.equalsIgnoreCase("land")) {
                tl_construction_stage_remark.setVisibility(View.GONE);
            } else {
                if (!general.isEmpty(Singleton.getInstance().indProperty.getDescriptionofConstructionStage())) {
                    fsProgressCount = fsProgressCount + 1;
                    et_construction_stage_remark.setText(Singleton.getInstance().indProperty.getDescriptionofConstructionStage());
                }
            }
        } else {
            tl_construction_stage_remark.setVisibility(View.GONE);
        }
        if (general.getUIVisibility("Marketability")) {
            ll_marketability.setVisibility(View.VISIBLE);
            initMarketability();
        } else {
            ll_marketability.setVisibility(View.GONE);
        }

        if (general.getUIVisibility("Document Received / Others") && !isNPA) {
            if (!general.isEmpty(Singleton.getInstance().property.getOtherDocuments()))
                et_additional_document.setText(Singleton.getInstance().property.getOtherDocuments());
            tl_additional_document.setVisibility(View.VISIBLE);

        } else {
            tl_additional_document.setVisibility(View.GONE);
        }


        if (general.getUIVisibility("REMARKS DETAILS")) {
            recyclerview_remarks.setVisibility(View.VISIBLE);
            initRemark();
        } else {
            recyclerview_remarks.setVisibility(View.GONE);
        }

        // additional_remarks
        if (!general.isEmpty(Singleton.getInstance().property.getOtherRemarks())) {
            editText_additional_remarks.setText(Singleton.getInstance().property.getOtherRemarks().trim());
        }
        // special_remarks
        if (!general.isEmpty(Singleton.getInstance().property.getSpecialRemarks())) {
            editText_special_remarks.setText(Singleton.getInstance().property.getSpecialRemarks().trim());
        }

    }


    private void getPurpose() {
        if (spinner_purpose.getSelectedItemPosition() > 0) {
            Singleton.getInstance().property.setPurposeofloanId(Singleton.getInstance().purposeOfList.get(spinner_purpose.getSelectedItemPosition()).getPurposeofloanId());
            Singleton.getInstance().property.setPurpose(Singleton.getInstance().purposeOfList.get(spinner_purpose.getSelectedItemPosition()).getName());
        }
    }

    private void getNameOfBorrower() {
        String nameofseller = applicantName.getText().toString();
       /* if (!general.isEmpty(nameofseller)) {
            String spinnerinitial = spSalutionBorrower.getSelectedItem().toString();
            if (spinnerinitial.contains(".")) {
                spinnerinitial = spinnerinitial.replace(".", ". ");
                //  spinnerinitial = spinnerinitial + ". ";
            }
            if (spinnerinitial.equalsIgnoreCase("Select"))
                Singleton.getInstance().aCase.setApplicantName(nameofseller.trim());
            else
                Singleton.getInstance().aCase.setApplicantName(spinnerinitial + ". " + nameofseller.trim());
        } else {
            String spinnerinitial = spSalutionBorrower.getSelectedItem().toString();
            if (spinnerinitial.contains(".")) {
                spinnerinitial = spinnerinitial.replace(".", ". ");
                //spinnerinitial = spinnerinitial + ". ";
            }
            if (spinnerinitial.equalsIgnoreCase("Select"))
                Singleton.getInstance().aCase.setApplicantName("");
            else
                Singleton.getInstance().aCase.setApplicantName(spinnerinitial);
        }*/
    }

    private void getNameOfSeller() {
        String nameofseller = et_seller_name.getText().toString();
        if (!general.isEmpty(nameofseller)) {
            String spinnerinitial = spSalutionSeller.getSelectedItem().toString();
            if (spinnerinitial.contains(".")) {
                spinnerinitial = spinnerinitial.replace(".", ". ");
                //  spinnerinitial = spinnerinitial + ". ";
            }
            if (spinnerinitial.equalsIgnoreCase("Select"))
                Singleton.getInstance().property.setNameOfSeller(nameofseller.trim());
            else
                Singleton.getInstance().property.setNameOfSeller(spinnerinitial + ". " + nameofseller.trim());
        } else {
            String spinnerinitial = spSalutionSeller.getSelectedItem().toString();
            if (spinnerinitial.contains(".")) {
                spinnerinitial = spinnerinitial.replace(".", ". ");
                //spinnerinitial = spinnerinitial + ". ";
            }
            if (spinnerinitial.equalsIgnoreCase("Select"))
                Singleton.getInstance().property.setNameOfSeller("");
            else Singleton.getInstance().property.setNameOfSeller(spinnerinitial);
        }
    }


    private void getNameOfOwner() {
        String nameofseller = et_name_of_owner.getText().toString();
        if (!general.isEmpty(nameofseller)) {
            String spinnerinitial = spSalutionOwner.getSelectedItem().toString();
            if (spinnerinitial.contains(".")) {
                spinnerinitial = spinnerinitial.replace(".", ". ");
            }
            if (spinnerinitial.equalsIgnoreCase("Select"))
                Singleton.getInstance().property.setNameOfPurchaser(nameofseller.trim());
            else
                Singleton.getInstance().property.setNameOfPurchaser(spinnerinitial + ". " + nameofseller.trim());
        } else {
            String spinnerinitial = spSalutionOwner.getSelectedItem().toString();
            if (spinnerinitial.contains(".")) {
                spinnerinitial = spinnerinitial.replace(".", ". ");
                //spinnerinitial = spinnerinitial + ". ";
            }
            if (spinnerinitial.equalsIgnoreCase("Select"))
                Singleton.getInstance().property.setNameOfPurchaser("");
            else Singleton.getInstance().property.setNameOfPurchaser(spinnerinitial);
        }
    }

    private void typeOfProperty(int caseid_int) {
        if (!general.isEmpty(String.valueOf(Singleton.getInstance().property.getTypeOfPropertyId()))) {
            for (int x = 0; x < Singleton.getInstance().typeOfProperty.size(); x++) {
                if (Singleton.getInstance().typeOfProperty.get(x).getTypeOfPropertyId() != null)
                    if (Singleton.getInstance().property.getTypeOfPropertyId() == Singleton.getInstance().typeOfProperty.get(x).getTypeOfPropertyId()) {
                        if (caseid_int == 3) {
                            etpropertyTypeProperty.setText(Singleton.getInstance().typeOfProperty.get(x).getName());
                        } else if (caseid_int == 5) {
                            etpropertyTypeNdma.setText(Singleton.getInstance().typeOfProperty.get(x).getName());

                        }
//                    etpropertyTypePropertyDetails.setText(Singleton.getInstance().typeOfProperty.get(x).getName());
//                        etpropertyTypeNdma.setText(Singleton.getInstance().typeOfProperty.get(x).getName());
                        break;
                    }
            }
        }
    }


    private void typeOfLoan() {
        // spinner - Property
        ArrayAdapter<DropDownResponse.Datum> localityArrayAdapter = new ArrayAdapter<DropDownResponse.Datum>(getActivity(), R.layout.row_spinner_item, Singleton.getInstance().loanType) {
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
        localityArrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
       /* spinner_type_of_loan.setAdapter(localityArrayAdapter);
        spinner_type_of_loan.setOnTouchListener(this);*/

        if (!general.isEmpty(String.valueOf(Singleton.getInstance().aCase.getLoanType()))) {
            for (int x = 0; x < Singleton.getInstance().loanType.size(); x++) {

                if (Singleton.getInstance().aCase.getLoanType().equals(Singleton.getInstance().loanType.get(x).getName())) {
                    // spinner_type_of_loan.setSelection(x);
                    etTypeOfLoan.setText("" + Singleton.getInstance().loanType.get(x).getName());
                    fsProgressCount = fsProgressCount + 1;
                    break;
                }
            }
        }
    }


    private void fsMandatoryInputData() {
        generalMandatory();
        localityMandatory();
     //   propertyMandatory();
        cardViewFairMarketValuationLayout.callOnClick();
    }

    private void DispDocumentBoundary() {
        /**************
         * Document and Actual Boundary
         * *********************/
        db_east = editText_db_east.getText().toString().trim();
        if (!general.isEmpty(db_east)) {
            Singleton.getInstance().property.setDocBoundryEast(db_east);
        } else {
            Singleton.getInstance().property.setDocBoundryEast("");
        }

        db_west = editText_db_west.getText().toString().trim();
        if (!general.isEmpty(db_west)) {
            Singleton.getInstance().property.setDocBoundryWest(db_west);
        } else {
            Singleton.getInstance().property.setDocBoundryWest("");
        }

        db_north = editText_db_north.getText().toString().trim();
        if (!general.isEmpty(db_north)) {
            Singleton.getInstance().property.setDocBoundryNorth(db_north);
        } else {
            Singleton.getInstance().property.setDocBoundryNorth("");
        }

        db_south = editText_db_south.getText().toString().trim();
        if (!general.isEmpty(db_south)) {
            Singleton.getInstance().property.setDocBoundrySouth(db_south);
        } else {
            Singleton.getInstance().property.setDocBoundrySouth("");
        }
    }


    private void computeFSProgressBar(int a) {

    }


    private void generalMandatory() {


        boolean isGeneralMandatory = false;

       /* if (spinner_purpose.getSelectedItemId() == 0) {
             isGeneralMandatory = true;
            General.customToast("Please select Purpose", getActivity());
        } else if (spinner_type_of_loan.getSelectedItemId() == 0) {
            General.customToast("Please select Type of Loan", getActivity());
            isGeneralMandatory = true;
        } else*/
        if (general.isEmpty(Singleton.getInstance().aCase.getApplicantName())) {
            applicantName.setError("Please enter Borrower Name");
            isGeneralMandatory = true;
        }

        if (general.isEmpty(Singleton.getInstance().property.getNameOfPurchaser())) {
            et_name_of_owner.setError("Please enter Owner name ");
            isGeneralMandatory = true;
        }

        if (general.isEmpty(Singleton.getInstance().aCase.getContactPersonName())) {
            etPersonName.setError("Please enter Contact Person name ");
            isGeneralMandatory = true;
        }
        if (general.isEmpty(Singleton.getInstance().aCase.getContactPersonNumber())) {
            etPersonNo.setError("Please enter Mobile No ");
            isGeneralMandatory = true;
        }
        if (general.isEmpty(Singleton.getInstance().aCase.getBankReferenceNO())) {
            et_finnonId.setError("Please enter Valuation reference no ");
            isGeneralMandatory = true;
        }
        if (general.isEmpty(Singleton.getInstance().aCase.getSiteVisitDate())) {
            date_error_msg.setVisibility(View.VISIBLE);
            isGeneralMandatory = true;
        }

        if (isGeneralMandatory) {
            cardView.callOnClick();
        }

    }

    private void localityMandatory() {

        boolean isLocalityMandatory = false;

        if (general.isEmpty(Singleton.getInstance().aCase.getPropertyAddress())) {
            editText_addr_perdoc.setError("Please Property Address");
            isLocalityMandatory = true;
        }
        if (general.isEmpty(Singleton.getInstance().property.getSurveyNo())) {
            editText_surveyno.setError("Please enter survey");
            isLocalityMandatory = true;
        }
        /*if (general.isEmpty(editText_landmark.getText().toString())) {
            editText_landmark.setError("Please enter landmark");
            isLocalityMandatory = true;
        }
        if (general.isEmpty(etPinCode.getText().toString())) {
            etPinCode.setError("Please enter pincode");
            isLocalityMandatory = true;
        }*/


        if (isLocalityMandatory) {
            cardViewLocality.callOnClick();
        }
    }


//    private void propertyMandatory() {
//        if (Singleton.getInstance().aCase.getApprovedPlanApprovingAuthority()==null && General.isEmpty(Singleton.getInstance().aCase.getApprovedPlanApprovingAuthority())) {
//            et_authority.setError("Please enter Authority");
//            cardViewProperty.callOnClick();
//        }
//    }


    private void updateFsProgress() {
        if (uiVisiblityCount != 0) {
            int c = (fsProgressCount * 100) / uiVisiblityCount;
            Log.e("fsProgressStatus", String.valueOf(c));
            fsProgressBarStatus.setProgress(c);
            txtProgressBar.setText("" + c + "%");
            Log.e("fsTotalProgress", String.valueOf(c));
        } else {
            txtProgressBar.setText("" + "0%");
        }


        if (general.getUIVisibility("LAT/LONG DETAILS")) {
            ll_geo_co_ordinates.setVisibility(View.VISIBLE);
            geoCorodinate();
        } else ll_geo_co_ordinates.setVisibility(View.GONE);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.same_as_doc_boundary_checkbox:
                if (same_as_doc_boundary_checkbox.isPressed()) {
                    if (isChecked) {
                        boolean check_boundary = same_as_doc_boundary_checkbox.isChecked();
                        Singleton.getInstance().property.setSameAsDocumentBoundary(check_boundary);
                        DispDocumentBoundary();
                        editText_ab_east.setText(db_east);
                        editText_ab_west.setText(db_west);
                        editText_ab_north.setText(db_north);
                        editText_ab_south.setText(db_south);
                    } else {
                        boolean check_boundary = same_as_doc_boundary_checkbox.isChecked();
                        Log.e("Same as Doc", String.valueOf(check_boundary));
                        Singleton.getInstance().property.setSameAsDocumentBoundary(check_boundary);
                    }
                }
                break;
        }
    }

    @Override
    public void rateValueUpdate(ArrayList<IndPropertyFloorsValuation> stepsValuation, int adapterPosition, boolean isActual) {
        if(Singleton.getInstance().indPropertyFloors.size() > 1){
            float totalValuePer = 0;
            float totalValue = 0;
            for(int i = 0;i < Singleton.getInstance().indPropertyFloors.size();i++){
                if(!stepsValuation.get(i).getMeasuredConstrValue().isEmpty() &&
                        Singleton.getInstance().indPropertyFloors.get(i).getPercentageCompletion() != null &&
                        Singleton.getInstance().indPropertyFloors.get(i).getPercentageCompletion() != 0
                        && Singleton.getInstance().indPropertyFloors.get(i).getPercentageCompletion() != -1

                ){
                    float c =  Singleton.getInstance().indPropertyFloors.get(i).getPercentageCompletion();
                    float a  = c / 100;
                    if(isActual){
                        totalValuePer = totalValuePer + (Integer.parseInt(stepsValuation.get(i).getMeasuredConstrValue()) * a);
                        Log.e("totalValuePer", String.valueOf(totalValuePer));
                        totalValue = totalValue + Integer.parseInt(stepsValuation.get(i).getMeasuredConstrValue());
                        Log.e("totalValue", String.valueOf(totalValue));
                    }else{
                        totalValuePer = totalValuePer + (Integer.parseInt(stepsValuation.get(i).getDocumentConstrValue()) * a);
                        Log.e("totalValuePer", String.valueOf(totalValuePer));
                        totalValue = totalValue + Integer.parseInt(stepsValuation.get(i).getDocumentConstrValue());
                        Log.e("totalValue", String.valueOf(totalValue));
                    }
                }
            }
            float finalValue = totalValuePer / totalValue;
            Log.e("totalValueFraction", String.valueOf(finalValue));
            float totalInPer = finalValue * 100;
            if(totalValue > 0.0)
            {
                  /* etAverageConstruction.setText(String.valueOf(totalInPer));
                   FragmentBuilding.textview_comp_total.setText(String.valueOf(totalInPer));*/

                String totalAvg = ""+ new DecimalFormat(".##").format(totalInPer);

                etAverageConstruction.setText(totalAvg);
           //     FragmentBuilding.textview_comp_total.setText(totalAvg);
                Singleton.getInstance().indPropertyValuation.setAverageConstructionPercentage(totalAvg);

            }else{
                etAverageConstruction.setText("");
             //   FragmentBuilding.textview_comp_total.setText("");
                Singleton.getInstance().indPropertyValuation.setAverageConstructionPercentage("");
            }
        }else{
            Integer integer = Singleton.getInstance().indPropertyFloors.get(0).getPercentageCompletion();
            if(integer !=null ){
                String totalAvg = ""+ new DecimalFormat(".##").format(integer);
                etAverageConstruction.setText(totalAvg);
                //etAverageConstruction.setText(String.valueOf(integer));
                //FragmentBuilding.textview_comp_total.setText(String.valueOf(integer));
            //    FragmentBuilding.textview_comp_total.setText(totalAvg);
                Singleton.getInstance().indPropertyValuation.setAverageConstructionPercentage(totalAvg);
            }
        }
    }
}
