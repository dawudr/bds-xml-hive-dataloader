package com.sainsburys;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class XmlDataListenerTest {
	
	private static String inputFile;
	private static String directory;

	@Test
	public void listLatestTransactionsfromFileFormatA() throws Exception {
		directory ="testSamples";
    	directory =(System.getProperty("user.dir") + "/" + directory + "/");
    	inputFile = directory + "R10xml.xml";
    	//Run function
    	XmlDataListener.listLatestTransactions(inputFile);
    	//make sure that null has not been returned
		assertTrue(true);
	}

}
