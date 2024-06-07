package com.realappraiser.gharvalue.ticketRaiseSystem.adapter;

import com.realappraiser.gharvalue.viewtickets.model.ViewTicketModel;
import java.util.List;

public interface ItemClickListener {
    void onClick(int position, List<ViewTicketModel.Data> viewTicketData);
}
