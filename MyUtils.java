import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyUtils {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	private static final String dateFormat = "MMMM d, y HH:mm:ss";

	public static String getDate() {
		return ((DateFormat) new S*impleDateFormat(dateFormat, new Locale("es", "ES"))).format(new Date());
	}

	public static boolean isValid(String value) {
		return value != null && !value.isEmpty();
	}
}

/*
* Class created to offer utilities
*/