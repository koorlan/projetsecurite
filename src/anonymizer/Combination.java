package anonymizer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Combination {

		public static <T> List<List<T>> combination(List<T> values, int size) {

	    if (0 == size + 0 ) {
	        return Collections.singletonList(Collections.<T> emptyList());
	    }

	    if (values.isEmpty()) {
	        return Collections.emptyList();
	    }

	    List<List<T>> combination = new LinkedList<List<T>>();

	    T actual = values.iterator().next();

	    List<T> subSet = new LinkedList<T>(values);
	    subSet.remove(actual);

	    List<List<T>> subSetCombination = combination(subSet, size - 1);

	    for (List<T> set : subSetCombination) {
	        List<T> newSet = new LinkedList<T>(set);
	        newSet.add(0, actual);
	        combination.add(newSet);
	    }

	    combination.addAll(combination(subSet, size));

	    return combination;
	}
		public ArrayList<String> formatMyList(List<List<String>> powerSet)
		{
			ArrayList <String> formatedData = new ArrayList<String>();
			String tmp = "";
			int k=0, v=0;
			for(int i = 0 ; i < powerSet.size() ; ++i ,++v )
			{
				//formatedData+="[";
				k = 0;
				for(int j = 0 ; j < powerSet.get(i).size(); ++j)
				{
					tmp += powerSet.get(i).get(j); 
					k++;
					if(powerSet.get(i).size() != k)	
						tmp += ",";
				}
				formatedData.add(tmp);
				tmp = "";
				//if(powerSet.size() - 1 != v)	
					//tmp += ",";
			}
			return formatedData ;
		}
		
		
	
}
