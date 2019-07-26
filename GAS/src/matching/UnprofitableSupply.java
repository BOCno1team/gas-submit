package matching;

import main.java.org.example.cfc.InvokeBCP;
import main.java.org.example.cfc.QueryBCP;

public class UnprofitableSupply extends Supply {
	//block chain connection profile
	private final static String chainCode = "gopackage1";

	public static void main(String[] args) {
//		uplinkUnprofitableSupply(901, "water", 100, "L", 501);
		uplinkUnprofitableSupply(9002, "water", 200, "L", 503, 4, 0, 0, 10); 
		
//		updateUnprofitableSupplyAmount(711, 500);
//		updateUnprofitableSupplyAmount(712, 1000);
//		updateUnprofitableSupplyAmount(713, 2000);
		
		
		QueryBCP query = new QueryBCP();
		//String[] queryArgs = new String[]{"101-503"};
		String[] queryArgs2 = new String[]{Integer.toString(9002)};
//		String[] queryArgs3 = new String[]{Integer.toString(654321)};

		try {
//			String jsonStr = query.query(chainCode,"queryByKey",queryArgs);
			//System.out.println(jsonStr);
			String jsonStr2 = query.query(chainCode,"queryByKey", queryArgs2);
//			String jsonStr3 = query.query(chainCode,"queryByKey", queryArgs3);
//			System.out.println(jsonStr);
			System.out.println(jsonStr2);
//			System.out.println(jsonStr3);
		} catch (Exception e) {
	
			e.printStackTrace();
		}
	}
	
	/*
	 * Constructor with default location and cover radius attributes
	 */
	public UnprofitableSupply(int supplyId, String name, double amount, String unit, int providerID) {
		super(supplyId, name, amount, unit, providerID, -1, -1, -1, -1);
		int providerRank = Organization.getRankById(providerID);
		double lat = Organization.getLocationById(providerID)[0];
		double lon = Organization.getLocationById(providerID)[1];
		double coverRadius = Provider.getCoverRadiusById(providerID);
		this.setProviderRank(providerRank);
		this.setLat(lat);
		this.setLon(lon);
		this.setCoverRadius(coverRadius);
	}
	
	/*
	 * Constructor with user defined location and cover radius attributes.
	 */
	public UnprofitableSupply(int supplyId, String name, double amount, String unit, int providerID, int providerRank, double lat, double lon, double coverRadius) {
		super(supplyId, name, amount, unit, providerID, providerRank, lat, lon, coverRadius);
	}

	/**
	 * Initialize an unprofitable supply on the chain
	 */
    public void uplinkUnprofitableSupply() {
    	InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(this.getSupplyId()),String.valueOf(this.getName()),
						String.valueOf(this.getAmount()),String.valueOf(this.getUnit()), String.valueOf(this.getProviderId()), String.valueOf(this.getProviderRank()),
						String.valueOf(this.getLat()), String.valueOf(this.getLon()), String.valueOf(this.getCoverRadius())};
		try {
			invoke.invoke(chainCode,"initUnprofitablesupply",invokeArgs);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
    
    /**
     * Initialize an unprofitable supply on the chain
     * @param supplyId
     * @param name
     * @param amount
     * @param unit
     * @param providerID
     * @param providerRank
     * @param lat
     * @param lon
     * @param coverRadius
     */
    public static void uplinkUnprofitableSupply(int supplyId, String name, double amount, String unit, int providerID, int providerRank, double lat, double lon, double coverRadius) {
    	InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(supplyId),name,
						String.valueOf(amount),String.valueOf(unit),String.valueOf(providerID), String.valueOf(providerRank),
						String.valueOf(lat), String.valueOf(lon), String.valueOf(coverRadius)};
		try {
			invoke.invoke(chainCode,"initUnprofitablesupply",invokeArgs);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

    /**
     * Update the amount of unprofitable supply on the chain
     */
	public void updateUnprofitableSupplyAmount() {
		InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(this.getSupplyId()),String.valueOf(this.getAmount())};
		try {
			invoke.invoke(chainCode,"updateUnprofitablesupplyamount",invokeArgs);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Update the amount of unprofitable supply with supplyId on the chain to amount
	 * @param supplyId
	 * @param amount
	 */
	public static void updateUnprofitableSupplyAmount(int supplyId, double amount) {
		InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(supplyId),String.valueOf(amount)};
		try {
			invoke.invoke(chainCode,"updateUnprofitablesupplyamount",invokeArgs);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "UnprofitableSupply [supplyId=" + this.getSupplyId() + ", name=" + this.getName() + ", amount=" + this.getAmount() + 
				", providerID"+ this.getProviderId() + " with rank" + Organization.getRankById(this.getProviderId()) + "]";
	}
}

