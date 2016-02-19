package snedeker.goetz.ir.assignmentOne.utils;

public class BaselineComparison {

	SetType type;
	
	public BaselineComparison(SetType type){
		this.type = type;
		init();
	}
	
	private void init(){
		//Load in the baseline details for the particular dataset
		// -Query ID and corresponding list of document IDs
	}
	
	public enum SetType{
		MED, CRAN;
	}
}
