package com.purplecat.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
	public static final int 		BUFFER_SIZE = 0x2000;
	public static final Charset 	UTF8 		= Charset.forName("UTF-8");
	
	public static void copy(File source, File dest) throws IOException {
		IOException error = null;
	    if ( !dest.exists() ) { dest.createNewFile(); }

        FileChannel in = null;
        FileChannel out = null; 

	    try {
	        in = new FileInputStream( source ).getChannel();
	        out = new FileOutputStream( dest ).getChannel();

	        out.transferFrom( in, 0, in.size() );
	    }
	    catch (IOException e) {
	    	error = e;
	    }
	    finally {
	    	if ( in != null ) in.close();
	    	if ( out != null ) out.close();
	    }
		if ( error != null ) {
			throw error;
		}
	}
	
	public static String readAllText(File file) throws IOException {
		IOException error = null;

		FileInputStream stream = null;
		BufferedReader br = null;
		StringBuilder text = new StringBuilder();

	    try {
	        stream = new FileInputStream( file );
			br = new BOMBufferedReader(new InputStreamReader(stream, UTF8), BUFFER_SIZE);
			String line = br.readLine();
			while ( line != null ) {
				if ( text.length() > 0 ) {
					text.append("\n");
				}
				text.append(line);
				line = br.readLine();
			}
	    }
	    catch (IOException e) {
	    	error = e;
	    }
	    finally {
	    	if ( stream != null ) stream.close();
	    	if ( br != null ) br.close();
	    }
		if ( error != null ) {
			throw error;
		}
		else {
			return text.toString();
		}		
	}
	
	public static List<String> readAllLines(File file) throws IOException {
		IOException error = null;

		FileInputStream stream = null;
		BufferedReader br = null;
		List<String> lines = new LinkedList<String>();

	    try {
	        stream = new FileInputStream( file );
			br = new BOMBufferedReader(new InputStreamReader(stream, UTF8), BUFFER_SIZE);
			String line = br.readLine();
			while ( line != null ) {
				lines.add(line);
				line = br.readLine();
			}
	    }
	    catch (IOException e) {
	    	error = e;
	    }
	    finally {
	    	if ( stream != null ) stream.close();
	    	if ( br != null ) br.close();
	    }
		if ( error != null ) {
			throw error;
		}
		else {
			return lines;
		}		
	}

}
