package com.purplecat.commons;

import java.util.Locale;

public interface IResourceService {

	public String getString(int id);
	
	public String getImageFile(int id);

	public Locale getLocaleFrom(int locale);

	public String getCommonString(int id);
	
}
