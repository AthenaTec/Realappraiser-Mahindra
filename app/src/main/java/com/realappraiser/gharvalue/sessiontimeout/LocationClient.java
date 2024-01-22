package com.realappraiser.gharvalue.sessiontimeout;

import android.location.Location;

public interface LocationClient {
    Location getLocationUpdates(Long intervel);
    Exception getException(String message);
}
