package matching;

public class ProfitableSupplyDistancePair implements Comparable<ProfitableSupplyDistancePair>{
	private Demand demand;
	private ProfitableSupply supply;
	private double distance;
	
	public ProfitableSupplyDistancePair(Demand demand, ProfitableSupply supply) {
		this.demand = demand;
		this.supply = supply;
		this.distance = calculateDistance(demand, supply);
	}
	
	/**
	 * Calculate the distance between two points in kilometers.
	 * @param target The demand that the supply would be delivered to.
	 * @param supply2 The start point for the delivery
	 * @return
	 */
	static double calculateDistance(Demand target, ProfitableSupply supply2) {
		
		double lat1 = target.getLat();
		double lon1 = target.getLon();
		double lat2 = supply2.getLat();
		double lon2 = supply2.getLon();
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
	public int compareTo(ProfitableSupplyDistancePair other) {
		if (this.supply.getProviderRank() > other.supply.getProviderRank()) { // compare rank
			return -1; 
		} else if (this.supply.getProviderRank() < other.supply.getProviderRank()) {
			return 1;
		} else { //compare distance
			if (this.distance < other.distance) {
				return -1;
			} else if (this.distance > other.distance) {
				return 1;
			} else { //compare unit price
				if (this.supply.getUnitPrice() < ((ProfitableSupply) other.supply).getUnitPrice()) {
					return -1;
				} else if (this.supply.getUnitPrice() > ((ProfitableSupply) other.supply).getUnitPrice()) {
					return 1;
				} else { //compare amount
					if (this.supply.getAmount() > other.supply.getAmount()) {
						return -1;
					} else if (this.supply.getAmount() < other.supply.getAmount()) {
						return 1;
					}
				}	
				return 0;
			}
		}
	}

	public Demand getDemand() {
		return demand;
	}

	public void setDemand(Demand demand) {
		this.demand = demand;
	}

	public ProfitableSupply getSupply() {
		return supply;
	}

	public void setSupply(ProfitableSupply supply) {
		this.supply = supply;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
