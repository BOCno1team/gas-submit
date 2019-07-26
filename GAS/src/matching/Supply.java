package matching;

public class Supply implements Comparable<Supply>, Cloneable {
	private int supplyId;
	private String name;
	private double amount;
	private String unit;
	private int providerId;
	private int providerRank;
	/*
	 * IBM location API could be applied here to get latitude & longitude info.
	 */
	private double lat; //latitude of the providing position in decimal degrees
	private double lon; //Longitude of the providing position in decimal degrees
	private double coverRadius; //in kilometer
	
	public Supply() {
	};
	
	public Supply(int supplyId, String name, double amount, String unit, int providerId, int providerRank, double lat, double lon, double coverRadius) {
		super();
		this.supplyId = supplyId;
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.providerId = providerId;
		this.providerRank = providerRank;
		this.lat = lat;
		this.lon = lon;
		this.coverRadius = coverRadius;
	}

	public int compareTo(Supply other) {
		if (this.getProviderRank() > other.getProviderRank()) {
			return -1; 
		} else if (this.getProviderRank() < other.getProviderRank()) {
			return 1;
		} else {
			if (this.getAmount() > other.getAmount()) {
				return -1;
			} else if (this.getAmount() < other.getAmount()) {
				return 1;
			}
			return 0;
		}
	}

	@Override
	public Object clone() {
		Supply s = null;
		try {
			s = (Supply) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/*
	 * Getters and setters
	 */
	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public void setSupplyId(int supplyId) {
		this.supplyId = supplyId;
	}
	
	public int getSupplyId() {
		return supplyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void deductAmount(double amountToDeduct) {
		this.amount -= amountToDeduct;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getProviderRank() {
		return providerRank;
	}

	public void setProviderRank(int providerRank) {
		this.providerRank = providerRank;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getCoverRadius() {
		return coverRadius;
	}

	public void setCoverRadius(double coverRadius) {
		this.coverRadius = coverRadius;
	}
}
