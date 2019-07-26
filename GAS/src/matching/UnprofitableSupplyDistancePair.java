package matching;

public class UnprofitableSupplyDistancePair implements Comparable<UnprofitableSupplyDistancePair> {
	private Demand demand;
	private UnprofitableSupply supply;
	private double distance;
	
	public UnprofitableSupplyDistancePair(Demand demand, UnprofitableSupply supply) {
		this.demand = demand;
		this.supply = supply;
		this.distance = calculateDistance(demand, supply);		
	}
	
	public Demand getDemand() {
		return demand;
	}

	public void setDemand(Demand demand) {
		this.demand = demand;
	}

	public UnprofitableSupply getSupply() {
		return supply;
	}

	public void setSupply(UnprofitableSupply supply) {
		this.supply = supply;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Calculate the distance between two points in kilometers.
	 * @param target The demand that the supply would be delivered to.
	 * @param source The start point for the delivery
	 * @return
	 */
	static double calculateDistance(Demand target, UnprofitableSupply source) {
		
		double lat1 = target.getLat();
		double lon1 = target.getLon();
		double lat2 = source.getLat();
		double lon2 = source.getLon();
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		}
		else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515 * 1.609344;
			
			return dist;
		}
	}

	@Override
	public int compareTo(UnprofitableSupplyDistancePair other) {
		if (this.supply.getProviderRank() > other.supply.getProviderRank()) { // compare rank
			return -1; 
		} else if (this.supply.getProviderRank() < other.supply.getProviderRank()) {
			return 1;
		} else { //compare distance
			if (this.distance < other.distance) {
				return -1;
			} else if (this.distance > other.distance) {
				return 1;
			} else { //compare amount
				if (this.supply.getAmount() > other.supply.getAmount()) {
					return -1;
				} else if (this.supply.getAmount() < other.supply.getAmount()) {
					return 1;
				}
				return 0;
			}
		}
	}

}
