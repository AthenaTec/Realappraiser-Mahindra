package com.realappraiser.gharvalue.communicator

data class ConvenyanceActivityResposne(
    val DateofTravel: String,
    val TotalDistance: Float,
    val NoOfActivities: Int,
    val Activities: List<ConvenyanceActivityResponseData>,
    )
