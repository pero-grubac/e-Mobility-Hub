package org.unibl.etf.emobility_hub.services;

import org.unibl.etf.emobility_hub.models.projection.DailyRevenueProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface IStatisticService {
    List<DailyRevenueProjection> getDailyRevenue(LocalDateTime start, LocalDateTime end);
}
