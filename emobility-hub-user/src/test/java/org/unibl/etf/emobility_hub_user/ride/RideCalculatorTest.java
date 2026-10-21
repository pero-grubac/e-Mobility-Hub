package org.unibl.etf.emobility_hub_user.ride;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

class RideCalculatorTest {
    static final LocalDateTime START = LocalDateTime.of(2026, 10, 21, 10, 0);

    @Test
    void distanceIsTheGreatCircleDistance() {
        // Banja Luka to Sarajevo is about 140 km as the crow flies.
        assertThat(RideCalculator.distanceKm(44.7722, 17.1910, 43.8563, 18.4131)).isEqualTo(140.5, offset(2.0));
        assertThat(RideCalculator.distanceKm(44.77, 17.19, 44.77, 17.19)).isZero();
    }

    @Test
    void priceIsRentPricePerHourTimesDuration() {
        assertThat(RideCalculator.price(45, START, START.plusMinutes(90))).isEqualTo(67.5);
        assertThat(RideCalculator.price(4, START, START.plusMinutes(20))).isEqualTo(1.33);
    }

    @Test
    void everyRideCostsAtLeastOneMinute() {
        assertThat(RideCalculator.billableHours(START, START.plusSeconds(5))).isEqualTo(60 / 3600.0);
        assertThat(RideCalculator.price(60, START, START)).isEqualTo(1.0);
    }

    @Test
    void roundsHalfUpToCents() {
        assertThat(RideCalculator.round2(2.345)).isEqualTo(2.35);
        assertThat(RideCalculator.round2(2.344)).isEqualTo(2.34);
    }
}
