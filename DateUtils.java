import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
	
	private static final ThreadLocal<Map<String,DateFormat>> threadFormat = new ThreadLocal<Map<String,DateFormat>>();
	
	public static String format(Date date,String pattern){
		Map<String,DateFormat> formatMap = threadFormat.get();
		if(formatMap == null){
			formatMap = new HashMap<String,DateFormat>();
			threadFormat.set(formatMap);
		}
		DateFormat dateFormat = formatMap.get(pattern);
		if(dateFormat == null){
			dateFormat = new SimpleDateFormat(pattern);
			formatMap.put(pattern, dateFormat);
		}
		return dateFormat.format(date);
	}
	
	public static Date parse(String source,String pattern) throws ParseException{
		Map<String,DateFormat> formatMap = threadFormat.get();
		if(formatMap == null){
			formatMap = new HashMap<String,DateFormat>();
			threadFormat.set(formatMap);
		}
		DateFormat dateFormat = formatMap.get(pattern);
		if(dateFormat == null){
			dateFormat = new SimpleDateFormat(pattern);
			formatMap.put(pattern, dateFormat);
		}
		return dateFormat.parse(source);
	}
}
