import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

public class RentalAgreement
{
    BaseRet Status;

    ToolCode CodeInfo;
    ToolType ToolIn;
    RentalParams ParamsIn;

    LocalDate DueDate;      // Date the tool will be due based on start date and # rental days
    int ChargeDays;         // Total # of days we'll charge for after considering all factors
    double PreDiscCharge;   // Rental amount prior to applying discount
    double DiscAmt;         // Discount $$
    double FinalCharge;     // Final charge after discount

    public RentalAgreement (RentalParams params)
    {
        ParamsIn = params;
    }

    public void PrintToConsole ()
    {
        System.out.printf ("Tool code: %s\r\n", ParamsIn.ToolCode);
        System.out.printf ("Tool type: %s\r\n", CodeInfo.ToolType);
        System.out.printf ("Tool brand: %s\r\n", CodeInfo.Brand);
        System.out.printf ("Rental days: %d\r\n", ParamsIn.RentalDayCount);
        System.out.printf ("Check out date: %s\r\n", DateFormat(ParamsIn.CheckoutDate));
        System.out.printf ("Due date: %s\r\n", DateFormat(DueDate));
        System.out.printf ("Daily rental charge: %s\r\n", CurrencyFormat(ToolIn.DailyCharge));
        System.out.printf ("Charge days: %d\r\n", ChargeDays);
        System.out.printf ("Pre-discount charge: %s\r\n", CurrencyFormat(PreDiscCharge));
        System.out.printf ("Discount percent: %s\r\n", PercentFormat(ParamsIn.DiscountPerc));
        System.out.printf ("Discount amount: %s\r\n", CurrencyFormat(DiscAmt));
        System.out.printf ("Final charge: %s\r\n", CurrencyFormat(FinalCharge));
    }

    private String DateFormat (LocalDate dt)
    {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yy");
        return dt.format(fmt);
    }

    private String CurrencyFormat (double curr)
    {
        Locale usLocale = Locale.US;
        NumberFormat fmt = NumberFormat.getCurrencyInstance (usLocale);
        return fmt.format(curr);
    }

    private String PercentFormat (int perc)
    {
        Formatter formatter = new Formatter();
        formatter.format ("%d%%", perc);
        return formatter.toString();
    }
}
