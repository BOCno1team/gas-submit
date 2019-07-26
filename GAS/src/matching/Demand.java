package matching;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import main.java.org.example.cfc.InvokeBCP;
import main.java.org.example.cfc.QueryBCP;

public class Demand implements Comparable<Demand>, Serializable {
	private int demandId;
	private String name;
	private String category;
	private int amountNeeded;
	private String unit;
	private int priority; // range from 1 to 3 to represent the urgency.
	private int demanderId;
	private double lat; // latitude of the destination in decimal degrees
	private double lon; //longitude of the destination in decimal degrees

	private final static String chainCode = "gopackage1";
	private static final long serialVersionUID = 20190625050327L;


	/*
	 * The map that matches a category to its corresponding priority.
	 */
	private static Map<String, Integer> priorityForCategoryMap = new HashMap<String, Integer>() {
		{
			put("Food", 1);
			put("Medical", 2);
			put("Rescue", 3);
			put("Epidemic Prevention", 4);
			put("Construction", 6);
		}
	};
	
	
	/*
	 * Constructor without priority and location
	 */
	public Demand(int demandId, String name, String category, int amount, String unit, int demanderId) {
		super();
		this.demandId = demandId;
		this.name = name;
		this.category = category;
		this.amountNeeded = amount;
		this.unit = unit;
		this.priority = priorityForCategoryMap.get(category);
		this.demanderId = demanderId;
		this.lat = Organization.getLocationById(demanderId)[0];
		this.lon = Organization.getLocationById(demanderId)[1];
	}

	/*
	 * Constructor with location but without priority
	 */
	public Demand(int demandId, String name, String category, int amount, String unit, int demanderId, double lat, double lon) {
		super();
		this.demandId = demandId;
		this.name = name;
		this.category = category;
		this.amountNeeded = amount;
		this.unit = unit;
		this.priority = priorityForCategoryMap.get(category);
		this.demanderId = demanderId;
		this.lat = lat;
		this.lon = lon;
	}
	
	/*
	 * Constructor with priority but without location
	 */
	public Demand(int demandId, String name, String category, int amount, String unit, int priority, int demanderId) {
		super();
		this.demandId = demandId;
		this.name = name;
		this.category = category;
		this.amountNeeded = amount;
		this.unit = unit;
		this.priority = priority;
		this.demanderId = demanderId;
		this.lat = Organization.getLocationById(demanderId)[0];
		this.lon = Organization.getLocationById(demanderId)[1];
	}

	/*
	 * Constructor with both location and priority
	 */
	public Demand(int demandId, String name, String category, int amount, String unit, int priority, int demanderId, double lat, double lon) {
		super();
		this.demandId = demandId;
		this.name = name;
		this.category = category;
		this.amountNeeded = amount;
		this.unit = unit;
		this.demanderId = demanderId;
		this.priority = priority;
		this.lat = lat;
		this.lon = lon;
	}

	/*
	 * Match this demand in the supply pools.
	 */
	public MatchResult matchToSupply() {
		SupplyManager supplyManager = new SupplyManager();

		// First match with the unprofitable supply pool
		System.out.println("****** matching in unprofitable supply pool ******");
		List<Supply> unprofitableSupplyList = supplyManager.mapInUnprofitableSupplyPool(this);
		double sum = supplyManager.getTotalAmount(unprofitableSupplyList);

		double amountStillNeeded = this.amountNeeded - sum;
		if (amountStillNeeded <= 0) {
			System.out.println("Unprofitable supply List is:\n");
			System.out.println(unprofitableSupplyList);
			return new MatchResult(this, unprofitableSupplyList, new ArrayList<Supply>(), new ArrayList<Supply>(), sum);
		}

		// Calculate the price needed to pay for the available resources
		// in the profitable supply pool.
		System.out.println("\n****** calculating price in profitable supply pool ******");
		double price = supplyManager.calculatePriceInProfitableSupplyPool(this);
		System.out.println("Total price needed is: " + price + "USD\n");
		
		// Map in the profitable supply pool with the fund.
		System.out.println("****** calculating fund available ******");
		double fund = price < supplyManager.getTotalFund() ? price : supplyManager.getTotalFund();
		System.out.println("Fund actually provided is " + fund + "USD\n");
		
		System.out.println("****** matching in profitable supply pool ******");
		List<Supply> profitableSupplyList = supplyManager.mapInProfitableSupplyPool(this, fund);
		sum += supplyManager.getTotalAmount(profitableSupplyList);
		double fundUsed = supplyManager.getTotalPrice(profitableSupplyList);

		// Deduct the fund actually used in the unprofitable supply pool.
		System.out.println("\n****** matching for fund to cover the profitable supplies ******");
		List<Supply> fundList = supplyManager.mapInUnprofitableSupplyPool("Fund", fundUsed);
		if (fundUsed != supplyManager.getTotalAmount(fundList)) {
			throw new RuntimeException();
		}
		
		System.out.println("\n\n##### Matching finished - Final result as follows:");
		System.out.println("Unprofitable supply List:");
		System.out.println(unprofitableSupplyList);
		
		System.out.println("\n Fund List is:");
		System.out.println(fundList);

		System.out.println("\n Profitable supply List is:");
		System.out.println(profitableSupplyList);

		System.out.println("\n##### Actually gathered amount of resources is:" + sum + this.getUnit());

		if (sum < this.amountNeeded) {
			System.out.println("Unfulfilled demand is added to the demand pool.\n\n");
		}

		MatchResult result = new MatchResult(this, unprofitableSupplyList, profitableSupplyList, fundList, sum);
		result.prepareForFeedback();
		result.updateOrgParticipationList();
		return result;
	}

	@Override
	public int compareTo(Demand other) {
		if (this.priority < other.priority) {
			return 1;
		} else if (this.priority > other.priority) {
			return -1;
		}
		return 0;
	}
	
	/**
	 * Initialize demand on the chain
	 * @param demandId
	 * @param name
	 * @param category
	 * @param amount
	 * @param unit
	 * @param demanderId
	 * @param priority
	 * @param lat
	 * @param lon
	 */
	public static void uplinkDemand(int demandId, String name, String category, int amount, String unit, int demanderId, int priority, double lat, double lon) {
    	InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(demandId),name, category, 
						String.valueOf(amount),String.valueOf(unit), String.valueOf(priority), 
						String.valueOf(demanderId), String.valueOf(lat), String.valueOf(lon)};
		try {
			invoke.invoke(chainCode,"initDemand",invokeArgs);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize demand on the chain
	 */
	public void uplinkDemand() {
    	InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(demandId),name, category, 
						String.valueOf(amountNeeded),String.valueOf(unit), String.valueOf(priority),
						String.valueOf(demanderId), String.valueOf(lat), String.valueOf(lon)};
		try {
			invoke.invoke(chainCode,"initDemand",invokeArgs);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Update the demand amount to amount.
	 * @param demandId
	 * @param amount
	 */
	public static void updateDemandAmount(int demandId, double amount) {
		InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(demandId),String.valueOf(amount)};
		try {
			invoke.invoke(chainCode,"updateDemandamount",invokeArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Query demander ID given demand ID.
	 * @param id
	 * @return
	 */
	public static String getDemanderIdById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		String orgType=null;
		try {
			String jsonStr = query.query(chainCode,"queryByKey",queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			orgType = json.getString("demanderID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgType;
	}
	
	/**
	 * Query demand object name given demand ID.
	 * @param id
	 * @return
	 */
	public static String getNameById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		String name=null;
		try {
			String jsonStr = query.query(chainCode,"queryByKey",queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			name = json.getString("name");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}
	
	/**
	 * Query amount needed given demand ID.
	 * @param id
	 * @return
	 */
	public static String getAmountById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		String amount=null;
		try {
			String jsonStr = query.query(chainCode,"queryByKey",queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			amount = json.getString("amount");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amount;
	}
	
	/**
	 * Query unit given demand ID.
	 * @param id
	 * @return
	 */
	public static String getUnitById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		String unit=null;
		try {
			String jsonStr = query.query(chainCode,"queryByKey",queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			unit = json.getString("unit");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unit;
	}
	
	/*
	 *  The getters and setters
	 */
	public int getDemanderId() {
		return demanderId;
	}

	public void setDemanderId(int demanderId) {
		this.demanderId = demanderId;
	}
	
	public int getDemandId() {
		return demandId;
	}

	public void setDemandId(int demandId) {
		this.demandId = demandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public static void sleep(int second) {
		try {
			Thread.sleep(second * 1000);
	   } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getAmountNeeded() {
		return amountNeeded;
	}

	public void setAmountNeeded(int amountNeeded) {
		this.amountNeeded = amountNeeded;
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
	
	public static void main(String[] args) {
		int demandId = 9001;
		String name = "bread";
		String category = "Food";
		int amountNeeded = 400;
		String unit = "kg";
		int demanderId = 601;
		int priority = 1;
		double lat = 0;
		double lon = 0;
		Demand d1 = new Demand(demandId, name, category, amountNeeded, unit, priority, demanderId, lat, lon);

		d1.uplinkDemand();
//		MatchResult result = d1.matchToSupply();
//
//		int orgID = 503;
//		System.out.println("****** Feedback process for org" + orgID + " starts ******");	
//		List<Integer> feedbackList = result.getFeedbackOrgs(orgID);
//		System.out.println("-----" + orgID + " should get feedback from organizations: " + feedbackList + "\n");
//		
//		//print previous result
//		System.out.println("Previous score of org " + orgID + " is" + Organization.getScoreById(503));
//		
//		System.out.println("\nGrading in process...");
//		result.giveFeedback(503, 5, 5, 5, 5, 5);
//		result.giveFeedback(503, 3, 3, 3, 3, 3);
//		result.giveFeedback(503, 4, 4, 4, 4, 4);
//	
////		verify result
////		String key = Integer.toString(demandId)+"-"+Integer.toString(503);
////		QueryBCP query = new QueryBCP();
////		String[] queryArgs = new String[]{key}; 
//
//		try {
//			Thread.sleep(100000);
////			String jsonStr = query.query("go_package2","query", queryArgs);
////			System.out.println(jsonStr);			
//	   } catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("new score of org" + orgID + " is " + Organization.getScoreById(503));
	}
}
