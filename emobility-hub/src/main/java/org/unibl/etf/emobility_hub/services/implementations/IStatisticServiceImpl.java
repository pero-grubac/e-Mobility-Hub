package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.models.projection.DailyRevenueProjection;
import org.unibl.etf.emobility_hub.repositories.RentalEntityRepository;
import org.unibl.etf.emobility_hub.services.IStatisticService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class IStatisticServiceImpl implements IStatisticService {

    @Autowired
    private RentalEntityRepository rentalRepository;

    @Override
    public List<DailyRevenueProjection> getDailyRevenue(LocalDateTime start, LocalDateTime end) {
        return rentalRepository.findDailyRevenue(start, end);
    }
}
