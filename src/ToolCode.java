public class ToolCode
{
    String ToolCode;        // Unique code identifying the specific tool
    String ToolType;        // Type of tool, used to look up charge specifics
    String Brand;           // Tool brand name

    public ToolCode (String code, String type, String brand)
    {
        ToolCode = code;
        ToolType = type;
        Brand = brand;
    }
}
