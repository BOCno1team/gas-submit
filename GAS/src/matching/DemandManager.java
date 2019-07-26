package matching;
import java.util.Collections;
import java.util.List;

import io.opencensus.internal.StringUtils;


public class DemandManager {

	public DemandManager() {
		super();
	}
	
	/**
	 * Match the demands to the supplies.
	 * @param demandList
	 */
	void matchDemandsToSupplies(List<Demand> demandList) {
		Collections.sort(demandList);
		for (Demand d : demandList) {
			MatchResult result = d.matchToSupply();
			result.prepareForFollowUp();
		}
	}
}
