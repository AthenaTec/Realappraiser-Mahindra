package com.realappraiser.gharvalue.Interface;

import com.realappraiser.gharvalue.model.IndPropertyFloorsValuation;

import java.util.ArrayList;

public interface AverageComPerInterface {


    void rateValueUpdate(ArrayList<IndPropertyFloorsValuation> stepsValuation, int adapterPosition,boolean isActual);
}
