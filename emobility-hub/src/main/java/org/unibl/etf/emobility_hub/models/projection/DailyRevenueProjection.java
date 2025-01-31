package org.unibl.etf.emobility_hub.models.projection;

import java.time.LocalDate;

public interface DailyRevenueProjection {
    LocalDate getRentalDay();
    Double getTotalRevenue();
}
