package com.purplecat.commons.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FileUtils {
	public static final int 		BUFFER_SIZE = 0x2000;
	public static final Charset 	UTF8 		= Charset.forName("UTF-8");
	public static final String 		URL_START 	= "URL=";
	
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
	
	public static void writeAllText(File file, String text) throws IOException {
		IOException error = null;

		FileOutputStream stream = null;
		BufferedWriter bw = null;

	    try {
	        stream = new FileOutputStream( file );
			bw = new BufferedWriter(new OutputStreamWriter(stream, UTF8), BUFFER_SIZE);
			bw.write(text);
	    }
	    catch (IOException e) {
	    	error = e;
	    }
	    finally {
	    	if ( bw != null ) bw.close();
	    	if ( stream != null ) stream.close();
	    }
		if ( error != null ) {
			throw error;
		}
	}
	
	/**
	 * Parses the file looking for the first line starting with "URL="
	 * 	If found, returns the value of "URL="
	 *  Otherwise, returns an empty string ""; 
	 * @param file - assumes a text-based .URL file (Internet Shortcut)
	 * @return
	 */
	public static String parseInternetShortcut(File file) throws IOException {
		String url = "";
		List<String> allLines = FileUtils.readAllLines(file);
		if ( allLines != null && allLines.size() > 0 ) {
			Optional<String> match = allLines.stream().filter(line -> line.startsWith(URL_START)).findFirst();
			if ( match.isPresent() ) {
				url = match.get().substring(URL_START.length());
			}
		}
		return(url);
	}

}
