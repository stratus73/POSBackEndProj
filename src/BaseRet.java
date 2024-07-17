public class BaseRet
{
    boolean Success;        // True if the given method succeeded with no issues
    String Message;         // Message to display to the user to help resolve any issues that occurred

    public BaseRet ()
    {
        Success = false;
        Message = "";
    }
}
