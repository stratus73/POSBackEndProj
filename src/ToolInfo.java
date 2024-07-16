public class ToolInfo
{
    ToolType TypeInfo;      // Full info (charge data, e.g.) for the specified tool type
    ToolCode CodeInfo;      // Tool name / brand for the specified tool

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
