public class ToolInfo
{
    ToolType TypeInfo;
    ToolCode CodeInfo;

    public ToolInfo ()
    {

    }

    public String getCode ()
    {
        return CodeInfo.ToolCode;
    }

    public void setToolCode (ToolCode code)
    {
        CodeInfo = code;
    }

    public String getType ()
    {
        return CodeInfo.ToolType;
    }

    public String getBrand ()
    {
        return CodeInfo.Brand;
    }
}
