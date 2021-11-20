package Helper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import controllers.RepositoryIssuesController.*;

public class WordStats {
	
	public Map<String, Integer> countWords(List<String> titles) {
//		Map<String, Integer> countMap =  new HashMap<String,Integer>();
//		for(String title : titles) {
//			if(!(title == null || title.isEmpty())){
//				String[] parts = title.split("[\\s+\",-.\\|!]");
//				for(String part : parts) {
//					if(!countMap.containsKey(part.toLowerCase())) {
//						countMap.put(part.toLowerCase(), 1);
//				}
//					else {
//						countMap.put(part.toLowerCase(), countMap.get(part.toLowerCase()) + 1);
//					}
//				
//				}
//		}
//	  }
		List<String> parts = new ArrayList();
		for(String title : titles) {
			if(!(title == null || title.isEmpty())){
				String[] part = (title.split("[\\s+\",-.\\|!\\[\\]]"));
				for(String p : part)
					parts.add(p.toLowerCase());
			}
		}
		Map<String, Integer> counts =
				parts.stream()
				.filter(i -> !i.isEmpty())
				.collect(
                    Collectors.toMap(
                        w -> w, w -> 1, Integer::sum));
		return counts.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));
		}
}
