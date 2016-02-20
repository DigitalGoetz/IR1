package snedeker.goetz.ir.assignmentOne.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaselineData {

	Map<Integer, List<Integer>> baseline;
	
	public BaselineData(){
		baseline = new HashMap<>();
	}
	
	public void insert(Integer key, Integer value){
		List<Integer> list = baseline.get(key);
		
		if(list == null){
			list = new ArrayList<>();
			list.add(value);
			baseline.put(key, list);
		}else{
			list.add(value);
		}
	}
	
	public List<Integer> getDocumentIds(Integer queryId){
		List<Integer> list = baseline.get(queryId);
		if(list == null){
			list = new ArrayList<>();
		}
		return list;
	}
	
}
