package com.purplecat.commons.utils;

import com.google.inject.Inject;
import com.purplecat.commons.logs.ILoggingService;

import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>To use dependency injection with this repository, make sure to
 * include a named string of "ConfigFilePath" that points to the
 * location and file where the properties should be stored.</p>
 *
 * Created by Crystal on 9/13/15.
 */
public class JavaPropertiesRepository {
	private final static String TAG = "JavaPropetiesRepository";
	private static final int START = 0;
	private static final int STRING_START_TOKEN = 1;
	private static final int STRING_TOKEN = 2;
	private static final int ESCAPE_TOKEN = 3;
	private static final int STRING_END_TOKEN = 4;

	private final ILoggingService _logging;
	private final File _settingsFile;

	private Properties _appProps;

	@Inject
	public JavaPropertiesRepository(ILoggingService logging, @Named("ConfigFilePath") String filePath) {
		_logging = logging;
		_settingsFile = new File(filePath);
	}

	public void setValue(String key, String value) {
		try {
			if (_appProps == null) {
				load();
			}
			if (value != null) {
				_appProps.setProperty(key, value);
			} else {
				_appProps.remove(key);
			}
			save();
		} catch (JavaPropertyException e) {
			_logging.error(TAG, String.format("Error setting property '%s' with value '%s'", key, value), e);
		}
	}

	public void setValue(String key, double value) {
		setValue(key, String.valueOf(value));
	}

	public void setValue(String key, long value){
		setValue(key, String.valueOf(value));
	}

	public void setValue(String key, boolean value) {
		setValue(key, String.valueOf(value));
	}

	public void setValue(String key, int value) {
		setValue(key, String.valueOf(value));
	}

	public void setValue(String key, String[] list) {
		setValue(key, Arrays.asList(list));
	}

	public void setValue(String key, Iterable<String> list) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (String s : list) {
			if ( i > 0 ) { builder.append(","); }
			builder.append("\"");
			for (int j = 0; j < s.length(); j++) {
				char c = s.charAt(j);
				if ( c == '\"' || c == '\\' ) builder.append('\\');
				builder.append(c);
			}
			builder.append("\"");
			i++;
		}
		builder.append("]");
		setValue(key, builder.toString());
	}

	public String getValue(String key, String def) {
		try {
			if ( _appProps == null ) {
				load();
			}
			return _appProps.getProperty(key, def);
		} catch (JavaPropertyException e) {
			_logging.error(TAG, String.format("Error getting property '%s'", key), e);
		}
		return def;
	}

	public int getValue(String key, int def) {
		String sValue = getValue(key, (String)null);
		try {
			if ( sValue != null ) {
				return Integer.parseInt(sValue);
			}
		} catch (NumberFormatException e) {}

		return def;
	}

	public double getValue(String key, double def) {
		String sValue = getValue(key, (String)null);
		try {
			if ( sValue != null ) {
				return Double.parseDouble(sValue);
			}
		} catch (NumberFormatException e) {}

		return def;
	}

	public long getValue(String key, long def) {
		String sValue = getValue(key, (String)null);
		try {
			if ( sValue != null ) {
				return Long.parseLong(sValue);
			}
		} catch (NumberFormatException e) {}

		return def;
	}

	public boolean getValue(String key, boolean def) {
		String sValue = getValue(key, (String)null);
		try {
			if ( sValue != null ) {
				return Boolean.parseBoolean(sValue);
			}
		} catch (NumberFormatException e) {}

		return def;
	}

	public List<String> getValue(String key, List<String> def) {
		String value = getValue(key, (String)null);
		if ( value == null ) return def;

		List<String> list = new ArrayList<String>();
		StringBuilder str = new StringBuilder();
		int state = START;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch ( state ) {
				case START:
					if ( c == '[' ) {
						state = STRING_START_TOKEN;
					}
					break;

				case STRING_START_TOKEN:
					if ( c == '"' ) {
						state = STRING_TOKEN;
					}
					break;

				case STRING_TOKEN:
					if ( c == '\\' ) {
						state = ESCAPE_TOKEN;
					}
					else if ( c == '"' ) {
						list.add(str.toString());
						str.setLength(0);
						state = STRING_END_TOKEN;
					}
					else {
						str.append(c);
					}
					break;

				case ESCAPE_TOKEN:
					str.append(c);
					state = STRING_TOKEN;
					break;

				case STRING_END_TOKEN:
					if ( c == ',' ) {
						state = STRING_START_TOKEN;
					}
					break;

			}
		}
		return list;
	}

	public String[] getValue(String key, String[] def) {
		List<String> value = getValue(key, def != null ? Arrays.asList(def) : null);
		return value.toArray(def);
	}

	protected void load() throws JavaPropertyException {
		if ( !_settingsFile.exists() ) {
			_appProps = new Properties();
			return;
		}

		FileInputStream in = null;
		try {
			_appProps = new Properties();
			in = new FileInputStream(_settingsFile);
			_appProps.load(in);
			in.close();
		} catch (IOException e) {
			throw new JavaPropertyException("Java Properties could not be loaded", e);
		} finally {
			if ( in != null ) try {
				in.close();
			} catch (IOException e) { /*ignored*/ }
		}
	}

	protected void save() throws JavaPropertyException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(_settingsFile);
			_appProps.store(out, "---Java Properties Repostiory---");
			out.close();
		} catch (IOException e) {
			_logging.error(TAG, "Error saving properties", e);
			throw new JavaPropertyException("Java Properties could not be saved", e);
		} finally {
			if ( out != null ) try {
				out.close();
			} catch (IOException e) { /*ignored*/ }
		}
	}

}
