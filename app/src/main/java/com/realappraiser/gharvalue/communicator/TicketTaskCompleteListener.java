package com.realappraiser.gharvalue.communicator;

public interface TicketTaskCompleteListener<TicketJsonRequestData> {

    void onTaskComplete(TicketJsonRequestData jsonRequestData);

}
