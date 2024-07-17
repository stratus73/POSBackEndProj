import java.time.LocalDate;

public class RentalParams
{
    String ToolCode;                // Tool code input from the POS system
    int RentalDayCount;             // Number of days requested for the rental
    int DiscountPerc;               // Discount % entered by the operator
    LocalDate CheckoutDate;         // Date the request was made at the POS system

    public RentalParams ()
    {

    }

    public BaseRet validateInput ()
    {
        BaseRet ret = new BaseRet();
        ret.Success = true;
        ret.Message = "";

        if (0 >= RentalDayCount)
        {
            ret.Success = false;
            ret.Message = "Rental days must be 1 or greater.";
        }
        else if (0 > DiscountPerc || 100 < DiscountPerc)
        {
            ret.Success = false;
            ret.Message = "Discount percent must be between 0 and 100.";
        }

        return ret;
    }
}
