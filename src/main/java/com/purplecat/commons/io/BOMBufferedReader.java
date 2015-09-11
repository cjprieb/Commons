package com.purplecat.commons.io;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class BOMBufferedReader extends BufferedReader {
	public static final String BOM_UTF8 = "\uFEFF";
	
	private int mLinesRead = 0;
	
	public BOMBufferedReader(Reader in) {
		super(in);
	}
	
	public BOMBufferedReader(Reader in, int sz) {
		super(in, sz);
	}
	
	@Override
	public String readLine() throws IOException {
		String line = super.readLine();
		mLinesRead++;
		if ( mLinesRead == 1 && line != null ) {
			if ( line.startsWith(BOM_UTF8) ) {
				line = line.substring(1);
			}
		}
		return(line);
	}
	
}
