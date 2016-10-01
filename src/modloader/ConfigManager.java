package modloader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

	public ConfigManager() { }
	
	private static Properties properties = new Properties();
	private static FileInputStream inputStream = null;
	
	/**
	 * Loads a config for reading. 
	 * @param filePath - The path of the file
	 * @return Properties of that config.
	 */
	public static Properties loadConfig(String filePath) {
		try {
			properties = new Properties();
			inputStream = new FileInputStream(filePath);
			properties.load(inputStream);
		} catch (FileNotFoundException e) {
			Log.warn("Could not find config file: " + filePath);
			return null;
		} catch (IOException e) {
			Log.error("Could not read config file: " + filePath, e);
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					Log.error("Could not close config file: " + filePath, e);
					return null;
				}
			}
		}
		return properties;
	}
	
	/**
	 * Saves Properties information to a file.
	 * @param properties - The Properties
	 * @param filePath - The path to save the file to
	 */
	public static void writeConfig(Properties properties, String filePath) {
		try {
			Log.info("Writing config to: " + filePath);
			properties.store(new FileOutputStream(filePath), null);
		} catch (IOException e) {
			Log.error("Could not write config file: " + filePath, e);
		}
	}
	
	/**
	 * Collects the information from log and prints it to a file.
	 * @param log
	 */
	public static void writeLog(StringBuilder log, String logPath) {
		BufferedWriter b = null;
		String s = log.toString();
		
		try {
			b = new BufferedWriter(new FileWriter(logPath));
			b.write(s);
		} catch (IOException e) {
			Log.error("", e);
		} finally {
			try {
				b.close();
			} catch (Exception e) {}
		}
	}
	
	public static boolean clearPreferences() {
		Log.info("Clearing old preferences...");
		
		try {
			File pref = new File(Engine.prefPath);
			File port = new File(Engine.portPath);
			File log = new File(CurrentOS.getSaveDir() + File.separator + CurrentOS.getLogName());
			File cacheFolder = new File(CurrentOS.getSaveDir() + File.separator + "cache");
			
			if(pref.exists()) pref.delete();
			if(port.exists()) port.delete();
			if(log.exists()) log.delete();
			if(cacheFolder.isDirectory()) {
				new ModCache().clearCache();
				cacheFolder.delete();
			}
			
			return true;
		} catch (Exception e) {
			Log.error("", e);
			return false;
		}
	}
}
