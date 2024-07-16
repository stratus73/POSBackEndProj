import java.util.ArrayList;

public class RentalHandler {

    //
    // Data collections for testing / prototyping purposes
    //
    ArrayList<ToolCode> ToolCodes = new ArrayList<ToolCode>();
    ArrayList<ToolType> ToolTypes = new ArrayList<ToolType>();

    //
    // For prototyping purposes the data is all self-contained, but normally this
    // data would be retrieved as needed from the database
    //
    private void InitData ()
    {
        ToolCodes.clear();
        ToolCodes.add (new ToolCode ("CHNS", "Chainsaw", "Stihl"));
        ToolCodes.add(new ToolCode ("LADW", "Ladder", "Werner"));
        ToolCodes.add(new ToolCode ("JAKD", "Jackhammer", "DeWalt"));
        ToolCodes.add(new ToolCode ("JAKR", "Jackhammer", "Rigid"));

        ToolTypes.clear();
        ToolTypes.add((new ToolType("Ladder", 1.99, true, true, false)));
        ToolTypes.add((new ToolType("Chainsaw", 1.49, true, false, true)));
        ToolTypes.add((new ToolType("Jackhammer", 2.99, true, false, false)));
    }

    public RentalHandler ()
    {
        InitData();
    }

    public ToolRet GetAvailableTools ()
    {
        ToolRet tr = new ToolRet();
        tr.Tools = new ArrayList<ToolInfo>();

        // Add a tool to the output for each item in our ToolCode data set
        for (ToolCode toolCode : ToolCodes)
        {
            ToolInfo tool = new ToolInfo();
            tool.setToolCode (toolCode);
            tr.Tools.add (tool);
        }

        tr.Success = true;
        tr.Message = "";

        return tr;
    }

    private BaseRet ValidateInput (RentalParams paramsIn)
    {
        BaseRet ret = new BaseRet();
        ret.Success = true;
        ret.Message = "";

        if (null == paramsIn)
        {
            ret.Success = false;
            ret.Message = "Invalid parameters entered, please try again.";
        }
        else if (0 >= paramsIn.RentalDayCount)
        {
            ret.Success = false;
            ret.Message = "Rental days must be 1 or greater.";
        }
        else if (0 > paramsIn.DiscountPerc || 100 < paramsIn.DiscountPerc)
        {
            ret.Success = false;
            ret.Message = "Discount percent must be between 0 and 100.";
        }

        return ret;
    }

    public RentalAgreement GenerateAgreement (RentalParams paramsIn)
    {
        RentalAgreement ret = new RentalAgreement(paramsIn);
        ret.Status = ValidateInput (paramsIn);

        // If the input validation failed then simply return
        if (!ret.Status.Success)
        {
            return ret;
        }

        // Get full code and charge information for the specified tool code or
        // return an error if the code doesn't match a tool in our dataset and store
        // it in the agreement
        try {
            ToolCode tc = ToolCodes.stream().filter((tool) -> tool.ToolCode.equals(paramsIn.ToolCode))
                    .findFirst().orElseThrow();

            ToolType tp = ToolTypes.stream().filter((ttype) -> ttype.ToolType.equals(tc.ToolType))
                    .findFirst().orElseThrow();

            ret.ToolIn = tp;
            ret.CodeInfo = tc;
        }
        catch (Exception e)
        {
            ret.Status.Success = false;
            ret.Status.Message = "No matching tool was found.";
            return ret;
        }

        // Calculate due date based on checkout date + number of days requested
        // and store it
        ret.DueDate = paramsIn.CheckoutDate.plusDays(paramsIn.RentalDayCount);

        // Start with charge days == requested # of rental days and then remove non-charged days as needed
        ret.ChargeDays = paramsIn.RentalDayCount;

        //
        // Remove non-charged days from the count based on any no-charge rules for the
        // specified tool
        //

        // Calculate the rental charge before any discounts and store it
        ret.PreDiscCharge = ret.ToolIn.DailyCharge * ret.ChargeDays;

        // Calculate the discount, if any, and store it
        ret.DiscAmt = (paramsIn.DiscountPerc / 100.0) * ret.PreDiscCharge;

        // Calculate the final charge and store it
        ret.FinalCharge = ret.PreDiscCharge - ret.DiscAmt;

        // Return the filled-in agreement
        return ret;
    }
}
