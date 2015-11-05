package com.purplecat.commons.utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

/**
 * Created by Crystal on 9/15/15.
 */
public class SimpleCache<T> {
	private Period _keepForPeriod = null;
	private DateTime _lastUpdated = null;
	private T _storedItem;

	public SimpleCache(Period keepForPeriod) {
		_keepForPeriod = keepForPeriod;
	}

	public T get() {
		if ( _lastUpdated == null ||
				_lastUpdated.plus(_keepForPeriod).isAfter(DateTime.now()) ) {
			_storedItem = null;
			return null;
		}
		else {
			return _storedItem;
		}
	}

	public void set(T item) {
		_storedItem = item;
		_lastUpdated = DateTime.now();
	}
}
