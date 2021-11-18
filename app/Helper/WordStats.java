package Helper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import controllers.RepositoryIssuesController.*;

public class WordStats {
	
	public Map<String, Integer> countWords(List<String> titles) {
		Map<String, Integer> countMap =  new HashMap<String,Integer>();
		for(String title : titles) {
			if(!(title == null || title.isEmpty())){
				String[] parts = title.split("[\\s+\",-.\\|!]");
				for(String part : parts) {
					if(!countMap.containsKey(part.toLowerCase())) {
						countMap.put(part.toLowerCase(), 1);
				}
					else {
						countMap.put(part.toLowerCase(), countMap.get(part.toLowerCase()) + 1);
					}
				
				}
		}
	  }
		return countMap;
}
}
