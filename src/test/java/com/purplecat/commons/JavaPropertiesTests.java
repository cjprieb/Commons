package com.purplecat.commons;

import com.purplecat.commons.logs.ConsoleLog;
import com.purplecat.commons.logs.ILoggingService;
import com.purplecat.commons.utils.JavaPropertiesRepository;
import com.purplecat.commons.utils.JavaPropertyException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Crystal on 9/15/15.
 */
public class JavaPropertiesTests {

	private JavaPropertiesRepository _properties;
	private ILoggingService _logging;

	@Before
	public void setup() {
		_logging = new ConsoleLog();
		_properties = new JavaPropertiesRepository(_logging, "property_tests.config");
	}

	@Test
	public void stringProperties() throws JavaPropertyException {
		String key = "string";
		String value = "test", def = "sample";

		_properties.setValue(key, value);
		String result = _properties.getValue(key, def);
		assertEquals("string value doesn't match", value, result);

		result = _properties.getValue(key + "diff", def);
		assertEquals("string value doesn't match", def, result);
	}

	@Test
	public void integerProperties() throws JavaPropertyException {
		String key = "integer";
		int value = 10, def = 5;

		_properties.setValue(key, value);
		int result = _properties.getValue(key, def);
		assertEquals("int value doesn't match", value, result);

		result = _properties.getValue(key + "diff", def);
		assertEquals("int value doesn't match", def, result);
	}

	@Test
	public void doubleProperties() throws JavaPropertyException {
		String key = "double";
		double value = 1.12, def = 5, tolerence = .001;

		_properties.setValue(key, value);
		double result = _properties.getValue(key, def);
		assertEquals("double value doesn't match", value, result, tolerence);

		result = _properties.getValue(key + "diff", def);
		assertEquals("double value doesn't match", def, result, tolerence);
	}

	@Test
	public void longProperties() throws JavaPropertyException {
		String key = "long";
		long value = 110, def = 50;

		_properties.setValue(key, value);
		long result = _properties.getValue(key, def);
		assertEquals("long value doesn't match", value, result);

		result = _properties.getValue(key + "diff", def);
		assertEquals("long value doesn't match", def, result);
	}

	@Test
	public void booleanProperties() throws JavaPropertyException {
		String key = "boolean";
		boolean value = true, def = false;

		_properties.setValue(key, value);
		boolean result = _properties.getValue(key, def);
		assertEquals("boolean value doesn't match", value, result);

		result = _properties.getValue(key + "diff", def);
		assertEquals("boolean value doesn't match", def, result);
	}

	@Test
	public void propertyMismatch() throws JavaPropertyException {
		String key = "key1";
		String value = "sample";
		int def = 10;

		_properties.setValue(key, value);
		int result = _properties.getValue(key, def);
		assertEquals("int value doesn't match", def, result);
	}

	@Test
	public void arrayProperties() throws JavaPropertyException {
		String key = "key1";
		String[] value = { "sample", "test" };
		String[] valueComplex = { "sample with a \"quote\" inside.", "a another \\\"thing quoted\" with extra \\slashes\\." };
		String[] def = {};

		_properties.setValue(key, value);
		String[] result = _properties.getValue(key, def);
		assertArrayEquals(value, result);

		_properties.setValue(key, valueComplex);
		result = _properties.getValue(key, def);
		assertArrayEquals(valueComplex, result);

	}

	private void assertArrayEquals(String[] expected, String[] result) {
		assertEquals("size doesn't match", expected.length, result.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals("values don't match at " + i, expected[i], result[i]);
		}
	}
}
