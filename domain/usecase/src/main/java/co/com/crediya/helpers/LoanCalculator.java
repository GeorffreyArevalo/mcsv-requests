package co.com.crediya.helpers;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class LoanCalculator {

    private LoanCalculator() {}

    public static BigDecimal calculateMonthlyFee(BigDecimal amount, BigDecimal monthlyRate, Integer monthTerm) {

        MathContext context = MathContext.DECIMAL128;
        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal decimalMonthlyRate = monthlyRate.setScale(2, RoundingMode.HALF_UP).divide( BigDecimal.valueOf(100), context );

        BigDecimal onePlusRate = decimalMonthlyRate.add(BigDecimal.ONE, context);
        BigDecimal onePlusRateToTerm = onePlusRate.pow(monthTerm, context);
        BigDecimal onePlusRateToNegativeTerm = BigDecimal.ONE.divide(onePlusRateToTerm, context);

        BigDecimal amountMultiplyRate = roundedAmount.multiply(decimalMonthlyRate, context);
        BigDecimal oneSubtractRateToNegativeTerm = BigDecimal.ONE.subtract(onePlusRateToNegativeTerm, context);
        BigDecimal monthlyFee = amountMultiplyRate.divide(oneSubtractRateToNegativeTerm, context);

        return monthlyFee.setScale(2, RoundingMode.HALF_UP);

    }

}
