import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    // Rental Agreement object, encapsulates the business logic for us
    static RentalHandler rentalObj;

    private static String GetConsoleInput ()
    {
        // Use a buffered reader to get the input string from the user
        BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
        String selection = "";
        try
        {
            selection = rdr.readLine();
        }
        catch (IOException e)
        {
            System.err.println ("Error reading tool code input, please try again. Error message: " + e.getMessage());
        }

        return selection;
    }

    private static String GetUserToolSelection (ArrayList<ToolInfo> Tools)
    {
        System.out.println ("Enter a tool for rental or \"X\" to exit the POS tool:");

        for (ToolInfo tool : Tools)
        {
            System.out.printf ("\t%s\t%s\t%s\r\n", tool.getCode(), tool.getType(), tool.getBrand());
        }

        return GetConsoleInput();
    }

    private static int PromptAndRetrieveInt (String sPrompt)
    {
        System.out.println (sPrompt);
        return Integer.parseInt (GetConsoleInput());
    }

    private static int GetUserDayCount ()
    {
        return PromptAndRetrieveInt("Enter the number of days the customer wants to rent the tool for: ");
    }

    private static int GetUserDiscount ()
    {
        return PromptAndRetrieveInt("Enter the % discount for the customer (between 0 and 100): ");
    }

    private static boolean IsValidTC (String sTC)
    {
        // An empty or NULL string is invalid
        if (null == sTC || sTC.isEmpty())
        {
            return false;
        }
        // "X" means Exit, so not a valid tool code...
        else if (sTC.equals("X"))
        {
            return false;
        }
        else
        {
            // Accept all other entered codes and let the downstream code sort out if it's really valid or not
            return true;
        }
    }

    private static boolean IsExitCode (String sCode)
    {
        if (null != sCode && sCode.equals("X"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void main(String[] args) {

        // Initialize our rental object
        rentalObj = new RentalHandler();

        // Get the tool options to display to the user
        ToolRet tr = rentalObj.GetAvailableTools ();

        if (!tr.Success)
        {
            System.out.println ("There was an error initializing the tool options: " + tr.Message +
                    ". Please try again later.");
        }
        else
        {
            boolean bContinue = true;
            while (bContinue) {
                String tc = GetUserToolSelection(tr.Tools);
                if (IsValidTC(tc)) {
                    int days = GetUserDayCount();
                    int discPerc = GetUserDiscount();

                    RentalParams params = new RentalParams();
                    params.CheckoutDate = LocalDate.now();
                    params.ToolCode = tc;
                    params.RentalDayCount = days;
                    params.DiscountPerc = discPerc;
                    RentalAgreement agmnt = rentalObj.GenerateAgreement(params);
                    if (!agmnt.Status.Success)
                    {
                        System.out.println ("Error generating rental agreement: " + agmnt.Status.Message);
                    }
                    else
                    {
                        agmnt.PrintToConsole ();
                    }
                }
                else if (IsExitCode(tc))
                {
                    bContinue = false;
                }
            }
        }
    }
}

