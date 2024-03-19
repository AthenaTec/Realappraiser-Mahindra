package com.realappraiser.gharvalue.bankTemplate.ui;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.fragments.FragmentBuilding;
import com.realappraiser.gharvalue.fragments.FragmentFlat;
import com.realappraiser.gharvalue.fragments.FragmentLand;
import com.realappraiser.gharvalue.fragments.FragmentValuationBuilding;
import com.realappraiser.gharvalue.fragments.FragmentValuationLand;
import com.realappraiser.gharvalue.fragments.FragmentValuationPenthouse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FieldStaffInspectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FieldStaffInspectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static boolean isImageClicked = false;
    private static boolean isLocalityImgClicked = false;

    private static boolean isPropertyImgClicked = false;
    private static boolean isPropertyDetailsImgClicked = false;

    private static boolean isNdmaParamterImgClicked = false;
    private static boolean isFair_market_valuationImgClicked = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FieldStaffInspectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LandAndBuildingFSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FieldStaffInspectionFragment newInstance(String param1, String param2) {
        FieldStaffInspectionFragment fragment = new FieldStaffInspectionFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_land_and_building_f_s, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_general_layout);
        ImageView imgLocality = (ImageView) view.findViewById(R.id.image_locality_layout);
        ImageView imgProperty = (ImageView) view.findViewById(R.id.image_property_layout);
        ImageView imgPropertyDetails = (ImageView) view.findViewById(R.id.image_property_details_layout);
        ImageView imgNdmaParamter = (ImageView) view.findViewById(R.id.image_ndma_parameter_layout);
        ImageView imgFairMarketValuation = (ImageView) view.findViewById(R.id.image_fair_market_valuation_layout);


        TextView textView = (TextView) view.findViewById(R.id.general_text);
        TextView txtLocality = (TextView) view.findViewById(R.id.locality_text);
        TextView txtProperty = (TextView) view.findViewById(R.id.property_text);
        TextView txtPropertyDetails = (TextView) view.findViewById(R.id.property_details_text);
        TextView txtNdmaParamter = (TextView) view.findViewById(R.id.ndma_parameter_text);
        TextView txtFairMarketValuationLayout = (TextView) view.findViewById(R.id.fair_market_valuation_text);



        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        CardView cardViewLocality = (CardView) view.findViewById(R.id.localityCardView);
        CardView cardViewProperty = (CardView) view.findViewById(R.id.propertyCardView);
        CardView cardViewPropertyDetails = (CardView) view.findViewById(R.id.cardViewPropertyDetails);
        CardView cardViewNdmaParamter = (CardView) view.findViewById(R.id.cardViewndma_parameter);
        CardView cardViewFairMarketValuationLayout = (CardView) view.findViewById(R.id.cardView_fair_market_valuation);


        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_general_sec_layout);
        LinearLayout llLocalitySubSection = (LinearLayout) view.findViewById(R.id.ll_locality_subsection);
        LinearLayout llPropertySubSection = (LinearLayout) view.findViewById(R.id.ll_property_subsection);
        LinearLayout llPropertyDetailsSubSection = (LinearLayout) view.findViewById(R.id.ll_property_details_sec_layout);
        LinearLayout llNdmaParamter = (LinearLayout) view.findViewById(R.id.ll_ndma_parameter_sec_layout);
        LinearLayout llFairMarketValuationLayoutSubSection = (LinearLayout) view.findViewById(R.id.ll_fair_market_valuation_sec_layout);

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
        return view;
    }

}