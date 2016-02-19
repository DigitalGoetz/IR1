package snedeker.goetz.ir.assignmentOne.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaselineData {

	Map<Integer, List<Integer>> baseline;
	
	public BaselineData(){
		baseline = new HashMap<>();
	}
	
	public void insert(Integer key, Integer value){
		List list = baseline.get(key);
	}
	
}
