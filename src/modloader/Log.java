package modloader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	private static final String INFO = "[INFO]\t\t" ;
	private static final String ERROR = "[ERROR]\t\t";
	private static final String WARNING = "[WARNING]\t";
	
	private static StringBuilder log = new StringBuilder();
	
	public static void info(String msg)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String s = "("+timeStamp+")\t" + INFO + msg;
		log.append(s + System.lineSeparator());
		System.out.println(s);
	}
	
	public static void error(String msg)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String s = "("+timeStamp+")\t" + ERROR + msg;
		log.append(s + System.lineSeparator());
		System.err.println(s);
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
