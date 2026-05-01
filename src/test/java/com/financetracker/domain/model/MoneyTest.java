package com.financetracker.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Money Value Object")
class MoneyTest {

    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("should create Money from BigDecimal")
        void shouldCreateFromBigDecimal() {
            Money money = Money.of(new BigDecimal("100.50"));
            assertEquals(new BigDecimal("100.50"), money.amount());
        }

        @Test
        @DisplayName("should create Money from double")
        void shouldCreateFromDouble() {
            Money money = Money.of(100.50);
            assertEquals(0, new BigDecimal("100.5").compareTo(money.amount()));
        }

        @Test
        @DisplayName("should create zero Money")
        void shouldCreateZero() {
            Money money = Money.zero();
            assertTrue(money.isZero());
        }

        @Test
        @DisplayName("should round to 2 decimal places")
        void shouldRoundToTwoDecimals() {
            Money money = Money.of(new BigDecimal("100.556"));
            assertEquals(new BigDecimal("100.56"), money.amount());
        }

        @Test
        @DisplayName("should throw on null amount")
        void shouldThrowOnNull() {
            assertThrows(NullPointerException.class, () -> Money.of((BigDecimal) null));
        }
    }

    @Nested
    @DisplayName("Arithmetic")
    class Arithmetic {

        @Test
        @DisplayName("should add two Money values")
        void shouldAdd() {
            Money a = Money.of(100.00);
            Money b = Money.of(50.25);
            Money result = a.add(b);
            assertEquals(0, new BigDecimal("150.25").compareTo(result.amount()));
        }

        @Test
        @DisplayName("should subtract two Money values")
        void shouldSubtract() {
            Money a = Money.of(100.00);
            Money b = Money.of(30.50);
            Money result = a.subtract(b);
            assertEquals(0, new BigDecimal("69.50").compareTo(result.amount()));
        }
    }

    @Nested
    @DisplayName("Comparisons")
    class Comparisons {

        @Test
        @DisplayName("should detect positive amount")
        void shouldDetectPositive() {
            assertTrue(Money.of(1.00).isPositive());
            assertFalse(Money.of(-1.00).isPositive());
            assertFalse(Money.zero().isPositive());
        }

        @Test
        @DisplayName("should detect negative amount")
        void shouldDetectNegative() {
            assertTrue(Money.of(-1.00).isNegative());
            assertFalse(Money.of(1.00).isNegative());
        }

        @Test
        @DisplayName("should detect zero amount")
        void shouldDetectZero() {
            assertTrue(Money.zero().isZero());
            assertFalse(Money.of(1.00).isZero());
        }
    }
}
