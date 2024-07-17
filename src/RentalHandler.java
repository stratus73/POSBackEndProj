import java.time.DayOfWeek;
import java.util.ArrayList;
import java.time.LocalDate;

public class RentalHandler {

    //
    // Data collections for testing / prototyping purposes
    //
    ArrayList<ToolCode> ToolCodes = new ArrayList<>();
    ArrayList<ToolType> ToolTypes = new ArrayList<>();

    //
    // For prototyping purposes the data is all self-contained, but normally this
    // data would be retrieved as needed from the database
    //
    private void initData ()
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
        initData();
    }

    public ToolRet getAvailableTools ()
    {
        ToolRet tr = new ToolRet();
        tr.Tools = new ArrayList<>();

        // Add a tool to the output for each item in our ToolCode data set
        // Note that this would usually involve database operations, but is simplified here
        // for prototype / test purposes
        tr.Tools.addAll(ToolCodes);

        tr.Success = true;
        tr.Message = "";

        return tr;
    }

    public RentalAgreement generateAgreement (RentalParams paramsIn) {
        RentalAgreement ret = new RentalAgreement(paramsIn);
        ret.Status = paramsIn.validateInput();

        // If the input validation failed then simply return
        if (!ret.Status.Success) {
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
        } catch (Exception e) {
            ret.Status.Success = false;
            ret.Status.Message = "No matching tool was found.";
            return ret;
        }

        // Perform rental agreement calculations
        ret.performCalcs();

        // Return the filled-in agreement
        return ret;
    }
}
