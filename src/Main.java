import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    // Rental Agreement object, encapsulates the business logic for us
    static Checkout rentalObj;

    private static String getConsoleInput ()
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

    private static String getUserToolSelection (ArrayList<ToolCode> Tools)
    {
        System.out.println ("Enter a tool code for rental or \"X\" to exit the POS tool:");

        for (ToolCode tool : Tools)
        {
            System.out.printf ("\t%s\t%s\t%s\r\n", tool.ToolCode, tool.ToolType, tool.Brand);
        }

        return getConsoleInput();
    }

    private static int promptAndRetrieveInt (String sPrompt)
    {
        System.out.println (sPrompt);
        return Integer.parseInt (getConsoleInput());
    }

    private static int getUserDayCount ()
    {
        return promptAndRetrieveInt("Enter the number of days the customer wants to rent the tool for: ");
    }

    private static int getUserDiscount ()
    {
        return promptAndRetrieveInt("Enter the % discount for the customer (between 0 and 100): ");
    }

    private static boolean isValidTC (String sTC)
    {
        // An empty or NULL string is invalid and "X" means Exit, so not a valid tool code...
        if (null == sTC || sTC.isEmpty() || sTC.equals("X"))
        {
            return false;
        }
        else
        {
            // Accept all other entered codes and let the downstream code sort out if it's really valid or not
            return true;
        }
    }

    private static boolean isExitCode (String sCode)
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
        rentalObj = new Checkout();

        // Get the tool options to display to the user
        ToolRet tr = rentalObj.getAvailableTools ();

        if (!tr.Success)
        {
            System.out.println ("There was an error initializing the tool options: " + tr.Message +
                    ". Please try again later.");
        }
        else
        {
            // Show the UI, ask the user for rental parameters, generate and display
            // the rental agreement, and then repeat until the user enters "X" to exit
            boolean bContinue = true;
            while (bContinue) {

                // Get the rental parameters from the user
                String tc = getUserToolSelection(tr.Tools);
                if (isValidTC(tc)) {
                    int days = getUserDayCount();
                    int discPerc = getUserDiscount();

                    // Generate an agreement based on the entered parameters
                    RentalParams params = new RentalParams();
                    params.CheckoutDate = LocalDate.now();
                    params.ToolCode = tc;
                    params.RentalDayCount = days;
                    params.DiscountPerc = discPerc;
                    try
                    {
                    RentalAgreement agmnt = rentalObj.generateAgreement(params);
                    if (!agmnt.Status.Success)
                    {
                        System.out.println ("Error generating rental agreement: " + agmnt.Status.Message);
                    }
                    else
                    {
                        // Show the agreement on the console
                        agmnt.printToConsole ();
                    }
                    }
                    catch (Exception exc)
                    {
                        System.out.println ("There was an error generating the agreement: " + exc.getMessage());
                    }
                }
                else if (isExitCode(tc))
                {
                    bContinue = false;
                }
            }
        }
    }
}

