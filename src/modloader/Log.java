package modloader;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	private static final String INFO = "[INFO]\t\t" ;
	private static final String ERROR = "[ERROR]\t\t";
	private static final String WARNING = "[WARNING]\t";
	private static final String TAB = "\t";
	
	private static StringBuilder log = new StringBuilder();
	
	public static void info(String msg)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String s = "("+timeStamp+")\t" + INFO + msg;
		log.append(s + System.lineSeparator());
		System.out.println(s);
	}
	
	public static String tab()
	{
		return TAB;
	}
	
	public static void error(String msg, Exception e)
	{
		new Error();
		
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String s = "("+timeStamp+")\t" + ERROR + msg;
		log.append(s + System.lineSeparator());
		System.err.println(s);
		
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String s2 = sw.toString();
		log.append(s2 + System.lineSeparator());
		System.err.println(s2);
	}
	
	public static void warn(String msg)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String s = "("+timeStamp+")\t" + WARNING + msg;
		log.append(s + System.lineSeparator());
		System.err.println(s);
	}
	
	public static void printLog() {
		String logPath = CurrentOS.getSaveDir() + File.separator + CurrentOS.getLogName();
		Log.info("Printing log to file: " + logPath);
		ConfigManager.writeLog(log, logPath);
	}
}
