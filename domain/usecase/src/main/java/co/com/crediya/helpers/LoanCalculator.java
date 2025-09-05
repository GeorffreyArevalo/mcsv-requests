package co.com.crediya.helpers;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LoanCalculator {

    private LoanCalculator() {}

    public static BigDecimal calculateMonthlyFee(BigDecimal amount, BigDecimal monthlyRate, LocalDate deadline) {

        MathContext context = MathContext.DECIMAL128;
        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal decimalMonthlyRate = monthlyRate.setScale(2, RoundingMode.HALF_UP).divide( BigDecimal.valueOf(100), context );

        long term = ChronoUnit.MONTHS.between(LocalDate.now(), deadline);


        BigDecimal onePlusRate = decimalMonthlyRate.add(BigDecimal.ONE, context);
        BigDecimal onePlusRateToTerm = onePlusRate.pow((int) term, context);
        BigDecimal onePlusRateToNegativeTerm = BigDecimal.ONE.divide(onePlusRateToTerm, context);

        BigDecimal amountMultiplyRate = roundedAmount.multiply(decimalMonthlyRate, context);
        BigDecimal oneSubtractRateToNegativeTerm = BigDecimal.ONE.subtract(onePlusRateToNegativeTerm, context);
        BigDecimal monthlyFee = amountMultiplyRate.divide(oneSubtractRateToNegativeTerm, context);

        return monthlyFee.setScale(2, RoundingMode.HALF_UP);

    }

}
