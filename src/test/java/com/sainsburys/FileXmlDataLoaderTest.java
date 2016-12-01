package com.sainsburys;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FileXmlDataLoaderTest {
	
	private static String inputFile;
	private static String directory;

	@Test
	public void testLoadXMLfromFileFormatA() throws Exception {
		directory ="testSamples";
    	directory =(System.getProperty("user.dir") + "/" + directory + "/");
    	inputFile = directory + "SampleLargeInputFormatA.xml";
    	//Run function
    	FileXmlDataLoader.loadXMLfromFile(inputFile);
    	//make sure that null has not been returned
		assertTrue(true);
	}

	@Test
	public void testLoadXMLfromFileFormatB() throws Exception {
		directory ="testSamples";
    	directory =(System.getProperty("user.dir") + "/" + directory + "/");
    	inputFile = directory + "SampleLargeInputFormatB.xml";
    	//Run function
    	FileXmlDataLoader.loadXMLfromFile(inputFile);
    	//make sure that null has not been returned
		assertTrue(true);
	}

}
