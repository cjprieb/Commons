package com.purplecat.commons.utils;

/**
 * Created by Crystal on 9/15/15.
 */
public class JavaPropertyException extends Exception {
	public JavaPropertyException(String message, Exception inner) {
		super(message, inner);
	}
}
