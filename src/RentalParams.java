import java.time.LocalDate;

public class RentalParams
{
    String ToolCode;
    int RentalDayCount;
    int DiscountPerc;
    LocalDate CheckoutDate;

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
