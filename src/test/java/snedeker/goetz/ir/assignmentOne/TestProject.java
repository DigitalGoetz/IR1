package snedeker.goetz.ir.assignmentOne;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;

import snedeker.goetz.ir.assignmentOne.utils.Metrics;

public class TestProject {

	Logger log = Logger.getLogger(getClass());
	
	String[] queries = {"test","properties","slipstream","sample","experiment"};
	
	
	
	@Test
	public void testQueryApplication() throws IOException {
	
		// Create Index
		log.debug("Creating Index...");
		Application testApplication = new Application();
		testApplication.createIndex();
		log.debug("Index Created.");
		//Obtain Index Metrics
		int size = Metrics.getFolderSizeOnDisk(new File(testApplication.getIndexPath()));
		log.debug("Index currently residing within " + size + " bytes on disk.");
		
		// FIXME continue coding here
		
		
		
	}

}
