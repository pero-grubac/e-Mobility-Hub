package org.unibl.etf.emobility_hub_user.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorsTest {

    @ParameterizedTest
    @ValueSource(strings = {"4242424242424242", "4242 4242 4242 4242", "5555-5555-5555-4444", "378282246310005"})
    void acceptsCardNumbersThatPassLuhn(String number) {
        assertThat(Validators.isValidCardNumber(number)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"4242424242424241", "1234", "abcdabcdabcdabcd", "", "42424242424242424242424"})
    void rejectsBadCardNumbers(String number) {
        assertThat(Validators.isValidCardNumber(number)).isFalse();
    }

    @Test
    void keepsOnlyTheLastFourDigits() {
        assertThat(Validators.lastFour("4242 4242 4242 1881")).isEqualTo("1881");
    }

    @Test
    void expiryIsValidUntilTheEndOfItsMonth() {
        YearMonth now = YearMonth.of(2026, 10);

        assertThat(Validators.isValidExpiry("10/26", now)).isTrue();
        assertThat(Validators.isValidExpiry("01/30", now)).isTrue();
        assertThat(Validators.isValidExpiry("09/26", now)).isFalse();
        assertThat(Validators.isValidExpiry("13/27", now)).isFalse();
        assertThat(Validators.isValidExpiry("1/27", now)).isFalse();
        assertThat(Validators.isValidExpiry(null, now)).isFalse();
    }

    @Test
    void cvvHasThreeOrFourDigits() {
        assertThat(Validators.isValidCvv("123")).isTrue();
        assertThat(Validators.isValidCvv("1234")).isTrue();
        assertThat(Validators.isValidCvv("12")).isFalse();
        assertThat(Validators.isValidCvv("12a")).isFalse();
    }

    @Test
    void coordinatesMustBeInRange() {
        assertThat(Validators.isValidLatitude(44.77)).isTrue();
        assertThat(Validators.isValidLatitude(90.1)).isFalse();
        assertThat(Validators.isValidLatitude(null)).isFalse();
        assertThat(Validators.isValidLongitude(-180.0)).isTrue();
        assertThat(Validators.isValidLongitude(180.5)).isFalse();
        assertThat(Validators.isValidLongitude(Double.NaN)).isFalse();
        assertThat(Validators.parseDouble("17.19")).isEqualTo(17.19);
        assertThat(Validators.parseDouble("east")).isNull();
    }

    @Test
    void registrationFieldPatterns() {
        assertThat(Validators.isValidUsername("john.doe_1")).isTrue();
        assertThat(Validators.isValidUsername("jo")).isFalse();
        assertThat(Validators.isValidUsername("john doe")).isFalse();
        assertThat(Validators.isValidEmail("john@example.com")).isTrue();
        assertThat(Validators.isValidEmail("john@")).isFalse();
        assertThat(Validators.isValidPhone("+387-65-111-111")).isTrue();
        assertThat(Validators.isValidPhone("call me")).isFalse();
        assertThat(Validators.isValidIdCard("ID-001-JD")).isTrue();
        assertThat(Validators.isValidIdCard("1")).isFalse();
        assertThat(Validators.isValidPassword("eightchr")).isTrue();
        assertThat(Validators.isValidPassword("short")).isFalse();
    }
}
