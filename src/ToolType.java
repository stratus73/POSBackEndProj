public class ToolType
{
    String ToolType;
    double DailyCharge;
    boolean WeekdayCharge;
    boolean WeekendCharge;
    boolean HolidayCharge;

    public ToolType ()
    {
        DailyCharge = 0.0;
        WeekdayCharge = true;
        WeekendCharge = false;
        HolidayCharge = false;
    }

    public ToolType (String toolType, double dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge)
    {
        ToolType = toolType;
        DailyCharge = dailyCharge;
        WeekdayCharge = weekdayCharge;
        WeekendCharge = weekendCharge;
        HolidayCharge = holidayCharge;
    }
}
