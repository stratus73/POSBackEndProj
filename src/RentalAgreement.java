import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Locale;

public class RentalAgreement
{
    BaseRet Status;         // Success/ Failure of any operation that returns this class

    ToolCode CodeInfo;      // Full tool code info for the specified tool to rent
    ToolType ToolIn;        // Full tool type info (e.g. charge rules) for the type of tool to rent
    RentalParams ParamsIn;  // Parameters entered at the POS system for this rental request

    LocalDate DueDate;      // Date the tool will be due based on start date and # rental days
    int ChargeDays;         // Total # of days we'll charge for after considering all factors
    double PreDiscCharge;   // Rental amount prior to applying discount
    double DiscAmt;         // Discount $$
    double FinalCharge;     // Final charge after discount

    public RentalAgreement (RentalParams params)
    {
        ParamsIn = params;
    }

    public void performCalcs ()
    {
        // Calculate due date based on checkout date + number of days requested
        // and store it
        DueDate = ParamsIn.CheckoutDate.plusDays(ParamsIn.RentalDayCount);

        //
        // Calculate actual number of days to charge based on any no-charge rules for the
        // specified tool type
        //
        ChargeDays = remNonChargeDays (ParamsIn.CheckoutDate, ParamsIn.RentalDayCount, ToolIn.WeekdayCharge,
                ToolIn.WeekendCharge, ToolIn.HolidayCharge);

        // Calculate the rental charge before any discounts (rounded half-up to cents)
        PreDiscCharge = new BigDecimal(ToolIn.DailyCharge * ChargeDays).
                setScale(2, RoundingMode.HALF_UP).doubleValue();

        // Calculate the discount, if any (rounded half up to cents)
        DiscAmt = new BigDecimal((ParamsIn.DiscountPerc / 100.0) * PreDiscCharge).
            setScale(2, RoundingMode.HALF_UP).doubleValue();

        // Calculate the final charge
        FinalCharge = PreDiscCharge - DiscAmt;
    }

    public void printToConsole ()
    {
        System.out.printf ("Tool code: %s\r\n", ParamsIn.ToolCode);
        System.out.printf ("Tool type: %s\r\n", CodeInfo.ToolType);
        System.out.printf ("Tool brand: %s\r\n", CodeInfo.Brand);
        System.out.printf ("Rental days: %d\r\n", ParamsIn.RentalDayCount);
        System.out.printf ("Check out date: %s\r\n", dateFormat(ParamsIn.CheckoutDate));
        System.out.printf ("Due date: %s\r\n", dateFormat(DueDate));
        System.out.printf ("Daily rental charge: %s\r\n", currencyFormat(ToolIn.DailyCharge));
        System.out.printf ("Charge days: %d\r\n", ChargeDays);
        System.out.printf ("Pre-discount charge: %s\r\n", currencyFormat(PreDiscCharge));
        System.out.printf ("Discount percent: %s\r\n", percentFormat(ParamsIn.DiscountPerc));
        System.out.printf ("Discount amount: %s\r\n", currencyFormat(DiscAmt));
        System.out.printf ("Final charge: %s\r\n", currencyFormat(FinalCharge));
    }

    private String dateFormat (LocalDate dt)
    {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yy");
        return dt.format(fmt);
    }

    private String currencyFormat (double curr)
    {
        Locale usLocale = Locale.US;
        NumberFormat fmt = NumberFormat.getCurrencyInstance (usLocale);
        return fmt.format(curr);
    }

    private String percentFormat (int perc)
    {
        Formatter formatter = new Formatter();
        formatter.format ("%d%%", perc);
        return formatter.toString();
    }

    private int remNonChargeDays (LocalDate startDate, int dwRawCount, boolean bChargeWD,
                                  boolean bChargeWE, boolean bChargeHD)
    {
        int dwRetCount = 0;

        //
        // Walk from the start date to the end date and only count days that should be charged
        //
        for (int i=0; i<dwRawCount; i++)
        {
            LocalDate dtCheck = startDate.plusDays(i);

            // The holiday check overrides the other checks if we have a match
            if (isHoliday (dtCheck)) {
                if (bChargeHD)
                    dwRetCount++;
            }
            else {
                if (bChargeWD && isWeekday(dtCheck))
                    dwRetCount++;
                else if (bChargeWE && isWeekend(dtCheck))
                    dwRetCount++;
            }
        }

        return dwRetCount;
    }

    private boolean isWeekday (LocalDate dtCheck)
    {
        return !isWeekend(dtCheck);
    }

    private boolean isWeekend (LocalDate dtCheck)
    {
        DayOfWeek dw = dtCheck.getDayOfWeek();
        if (dw == DayOfWeek.SATURDAY || dw == DayOfWeek.SUNDAY)
            return true;
        else
            return false;
    }

    private boolean isHoliday (LocalDate dtCheck)
    {
        if (dtCheck.equals(getIndepDayDate(dtCheck.getYear())) ||
                dtCheck.equals(getLaborDayDate(dtCheck.getYear())))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //
    // Get the observed date for Independence Day for the given year
    //
    private LocalDate getIndepDayDate (int year)
    {
        LocalDate dtJul4 = LocalDate.of(year, 7, 4);

        // If the given date falls on a Saturday then subtract a day
        if (dtJul4.getDayOfWeek() == DayOfWeek.SATURDAY)
            return dtJul4.minusDays(1);

        // Or on a Sunday then add a day
        if (dtJul4.getDayOfWeek() == DayOfWeek.SUNDAY)
            return dtJul4.plusDays(1);

        return dtJul4;
    }

    //
    // Get the observed date for Labor Day for the given year
    //
    private LocalDate getLaborDayDate (int year)
    {
        LocalDate dtLD = LocalDate.of(year, 9, 1);

        // Count forward until we find the first Monday
        while (DayOfWeek.MONDAY != dtLD.getDayOfWeek())
        {
            dtLD = dtLD.plusDays(1);
        }

        return dtLD;
    }
}
