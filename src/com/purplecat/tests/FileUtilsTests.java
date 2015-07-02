package com.purplecat.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.purplecat.commons.io.FileUtils;

public class FileUtilsTests {
	static String _filePath1 = "src/com/purplecat/tests/sample.txt";
	static String _filePath2 = "src/com/purplecat/tests/copy of sample.txt";

	@Test
	public void copyFiles() {
		File source = new File(_filePath1);
		File dest = new File(_filePath2);
		try {
			System.out.println("copying from " + source.getAbsolutePath());
			System.out.println("  to " + dest.getAbsolutePath());
			FileUtils.copy(source, dest);
			
			String sourceText = FileUtils.readAllText(source);
			String destText = FileUtils.readAllText(dest);
			
			assertEquals(sourceText, destText);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException occurred");
		}
	}

	@Test
	public void readAllText() {
		File source = new File(_filePath1);
		try {
			System.out.println("Reading all text from " + source.getAbsolutePath());
			String sourceText = FileUtils.readAllText(source);
			
			System.out.println("    Text: \"" + sourceText + " \"");
			
			assertTrue(sourceText.startsWith("This is a sample file."));
			assertTrue(sourceText.endsWith("It's practically a poem."));
			
			int newLineCnt = 0;
			int newLineIndex = sourceText.indexOf('\n'); 
			while ( newLineIndex >= 0 ) {
				newLineCnt++;
				newLineIndex = sourceText.indexOf('\n', newLineIndex+1);
			}
			assertEquals(5, newLineCnt);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException occurred");
		}
	}

	@Test
	public void readAllLines() {
		File source = new File(_filePath1);
		try {
			System.out.println("Reading all lines from " + source.getAbsolutePath());
			List<String> sourceText = FileUtils.readAllLines(source);
			
			for ( String line : sourceText ) {
				System.out.println("    Line: \"" + line + "\"");
			}
			
			assertEquals(6, sourceText.size());			
			assertTrue(sourceText.get(0).startsWith("This is a sample file."));
			assertTrue(sourceText.get(5).endsWith("It's practically a poem."));
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException occurred");
		}
	}

}
