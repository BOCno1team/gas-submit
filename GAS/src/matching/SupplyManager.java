package matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import main.java.org.example.cfc.QueryBCP;

/*
 * Work for the subclasses.
 */
public class SupplyManager {
	// block chain connection profile
	private final static String chainCode = "gopackage1";

	public SupplyManager() {
		super();
	}

	/**
	 * Get all the unprofitable supplies with resource name.
	 * 
	 * @param ResourceName
	 * @return
	 */
	public static List<UnprofitableSupply> getUnprofitableSupplyList(String ResourceName) {
		QueryBCP queryHelper = new QueryBCP();
		String[] args = new String[] { ResourceName };
		String jsonStr;
		List<UnprofitableSupply> resultList = new ArrayList<UnprofitableSupply>();
		JSONObject jsonObj = null;
		int supplyId = 0;
		String name = null;
		double amount = 0;
		String unit = null;
		int providerId = 0;
		int providerRank = 0;
		double lat = 0;
		double lon = 0;
		double coverRadius = 0;
		try {
			jsonStr = queryHelper.query(chainCode, "queryUnproByName", args);
			JSONArray jsonArr = JSONObject.parseArray(jsonStr);
			for (int i = 0; i < jsonArr.size(); i++) {
				jsonObj = jsonArr.getJSONObject(i);
				jsonObj = JSONObject.parseObject(jsonObj.getString("Record"));
				supplyId = jsonObj.getIntValue("supplyID");
				name = jsonObj.getString("name");
				amount = jsonObj.getDoubleValue("amount");
				unit = jsonObj.getString("unit");
				providerId = jsonObj.getIntValue("providerID");
				providerRank = Organization.getRankById(providerId);
				lat = jsonObj.getDoubleValue("lat");
				lon = jsonObj.getDoubleValue("lon");
				coverRadius = jsonObj.getDoubleValue("coverRadius");

				if (amount != 0) {
					resultList.add(new UnprofitableSupply(supplyId, name, amount, unit, providerId, providerRank, lat,
							lon, coverRadius));
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return resultList;
	}

	/**
	 * Get all the unprofitable supplies with resource name.
	 * 
	 * @param demand
	 * @return
	 */
	public static List<UnprofitableSupplyDistancePair> getUnprofitableSupplyList(Demand demand) {
		QueryBCP queryHelper = new QueryBCP();
		String[] args = new String[] { demand.getName() };
		String jsonStr;
		List<UnprofitableSupplyDistancePair> resultList = new ArrayList<UnprofitableSupplyDistancePair>();
		JSONObject jsonObj = null;
		int supplyId = 0;
		String name = null;
		double amount = 0;
		String unit = null;
		int providerId = 0;
		int providerRank = 2;
		double lat = 0;
		double lon = 0;
		double coverRadius = 0;
		try {
			jsonStr = queryHelper.query(chainCode, "queryUnproByName", args);
			JSONArray jsonArr = JSONObject.parseArray(jsonStr);
			for (int i = 0; i < jsonArr.size(); i++) {
				jsonObj = jsonArr.getJSONObject(i);
				jsonObj = JSONObject.parseObject(jsonObj.getString("Record"));
				supplyId = jsonObj.getIntValue("supplyID");
				name = jsonObj.getString("name");
				amount = jsonObj.getDoubleValue("amount");
				unit = jsonObj.getString("unit");
				providerId = jsonObj.getIntValue("providerID");
				providerRank = Organization.getRankById(providerId);
				lat = jsonObj.getDoubleValue("lat");
				lon = jsonObj.getDoubleValue("lon");
				coverRadius = jsonObj.getDoubleValue("coverRadius");

				UnprofitableSupply supply = new UnprofitableSupply(supplyId, name, amount, unit, providerId,
						providerRank, lat, lon, coverRadius);
				UnprofitableSupplyDistancePair pair = new UnprofitableSupplyDistancePair(demand, supply);
				double distance = pair.getDistance();
				if (amount != 0 && distance < supply.getCoverRadius()) {
					resultList.add(pair);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return resultList;
	}

//	public List<ProfitableSupply> getProfitableSupplyList(String ResourceName) {
//		QueryBCP queryHelper =new QueryBCP();
//		String[] args =new String[]{ResourceName};
//		String jsonStr;
//		List<ProfitableSupply> resultList = new ArrayList<ProfitableSupply>();
//		JSONObject jsonObj = null;
//		int supplyId = 0;
//		String name = null;
//		int amount = 0;
//		String unit = null;
//		int providerId = 0;
//		int unitPrice = 0;
//		int providerRank = 0;
//		try {
//			jsonStr = queryHelper.query(chainCode, "queryProByName", args);
//			JSONArray jsonArr = JSONObject.parseArray(jsonStr);
//			for (int i = 0 ; i < jsonArr.size() ; i++){
//				jsonObj = jsonArr.getJSONObject(i);
//				jsonObj = JSONObject.parseObject(jsonObj.getString("Record"));
//				supplyId = jsonObj.getIntValue("supplyID");
//				name = jsonObj.getString("name");
//				amount = jsonObj.getIntValue("amount");
//				unit = jsonObj.getString("unit");
//				providerId = jsonObj.getIntValue("organization");
//				unitPrice = jsonObj.getIntValue("unitprice");
//				providerRank = Organization.getRankById(providerId);
//			
//				if (amount != 0 && unitPrice != 0) { //TODO: is there a better solution?
//					resultList.add(new ProfitableSupply(supplyId,name,amount,unit,providerId,unitPrice,providerRank));
//				}
//			}
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		
//		return resultList;
//	}

	/**
	 * Get all the profitable supplies with resource name.
	 * 
	 * @param demand
	 * @return
	 */
	public List<ProfitableSupplyDistancePair> getProfitableSupplyList(Demand demand) {
		QueryBCP queryHelper = new QueryBCP();
		String[] args = new String[] { demand.getName() };
		String jsonStr;
		List<ProfitableSupplyDistancePair> resultList = new ArrayList<ProfitableSupplyDistancePair>();
		JSONObject jsonObj = null;
		int supplyId = 0;
		String name = null;
		double amount = 0;
		String unit = null;
		int providerId = 0;
		int unitPrice = 0;
		int providerRank = 2;
		double lat = 0;
		double lon = 0;
		double coverRadius = 0;
		try {
			jsonStr = queryHelper.query(chainCode, "queryProByName", args);
			JSONArray jsonArr = JSONObject.parseArray(jsonStr);
			for (int i = 0; i < jsonArr.size(); i++) {
				jsonObj = jsonArr.getJSONObject(i);
				jsonObj = JSONObject.parseObject(jsonObj.getString("Record"));
				supplyId = jsonObj.getIntValue("supplyID");
				name = jsonObj.getString("name");
				amount = jsonObj.getDoubleValue("amount");
				unit = jsonObj.getString("unit");
				providerId = jsonObj.getIntValue("providerID");
				unitPrice = jsonObj.getIntValue("unitprice");
				providerRank = Organization.getRankById(providerId);
				// providerRank = jsonObj.getIntValue("providerRank");
				lat = jsonObj.getDoubleValue("lat");
				lon = jsonObj.getDoubleValue("lon");
				coverRadius = jsonObj.getDoubleValue("coverRadius");

				ProfitableSupply supply = new ProfitableSupply(supplyId, name, amount, unit, providerId, unitPrice, lat,
						lon, coverRadius);
				ProfitableSupplyDistancePair pair = new ProfitableSupplyDistancePair(demand, supply);
				double distance = pair.getDistance();

				if (amount != 0 && unitPrice != 0 && distance < supply.getCoverRadius()) {
					resultList.add(pair);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return resultList;
	}

	/**
	 * Get total amount provided in the supply list
	 * 
	 * @param list
	 * @return
	 */
	public double getTotalAmount(List<? extends Supply> list) {
		double total = list.stream().mapToDouble(s -> s.getAmount()).sum();
		return total;
	}

	/**
	 * Calculate the total price needed to pay for the profitable supplies in the
	 * list.
	 * 
	 * @param profitableSupplyList
	 * @return
	 */
	public double getTotalPrice(List<Supply> profitableSupplyList) {
		double totalPrice = 0;

		for (Supply s : profitableSupplyList) {
			totalPrice += ((ProfitableSupply) s).getUnitPrice() * s.getAmount();
		}
		return totalPrice;
	}

	/**
	 * Get total amount of fund available
	 * @return
	 */
	public double getTotalFund() {
		List<UnprofitableSupply> fundList = getUnprofitableSupplyList("Fund");

		double totalFund = fundList.stream().mapToDouble(s -> s.getAmount()).sum();
		return totalFund;
	}

	/**
	 * Map the demand in the unprofitable supply pool.
	 * 
	 * @param amountNeeded
	 * @param supplyList
	 * @return the list of supplies that's mapped to the demand.
	 */
	List<Supply> mapInUnprofitableSupplyPool(String resourceName, double amountNeeded) {
		double sum = 0;
		List<Supply> supplyList = new ArrayList<Supply>();

		List<UnprofitableSupply> unprofitableSupplyPool = getUnprofitableSupplyList(resourceName);
		Collections.sort(unprofitableSupplyPool);

		for (UnprofitableSupply s : unprofitableSupplyPool) {
			if (sum == amountNeeded) {
				break;
			}
			double amountStillNeeded = amountNeeded - sum;
			double amountUsed = s.getAmount() > amountStillNeeded ? amountStillNeeded : s.getAmount();

			// Add supply to be used to the supply list
			UnprofitableSupply sCopy = (UnprofitableSupply) s.clone();
			sCopy.setAmount(amountUsed);
			supplyList.add(sCopy);

			// Update info
			sum += amountUsed;
//			s.deductAmount(amountUsed);  ****************************************
//			s.updateUnprofitableSupplyAmount(); ***************LOOK HERE***************

			System.out.println("UNPROFITABLE: (supplyID " + s.getSupplyId() + ") Org" + s.getProviderId() + " provided "
					+ amountUsed + s.getUnit());
		}

		return supplyList;
	}

	/**
	 * Map the demand in the unprofitable supply pool.
	 * @param demand
	 * @return
	 */
	List<Supply> mapInUnprofitableSupplyPool(Demand demand) {
		double amountNeeded = demand.getAmountNeeded();
		double sum = 0;
		List<Supply> supplyList = new ArrayList<Supply>();

		List<UnprofitableSupplyDistancePair> unprofitableSupplyPool = getUnprofitableSupplyList(demand);
		Collections.sort(unprofitableSupplyPool);

		for (UnprofitableSupplyDistancePair pair : unprofitableSupplyPool) {
			UnprofitableSupply s = pair.getSupply();
			if (sum == amountNeeded) {
				break;
			}
			double amountStillNeeded = amountNeeded - sum;
			double amountUsed = s.getAmount() > amountStillNeeded ? amountStillNeeded : s.getAmount();

			// Add supply to be used to the supply list
			UnprofitableSupply sCopy = (UnprofitableSupply) s.clone();
			sCopy.setAmount(amountUsed);
			supplyList.add(sCopy);

			// Update info
			sum += amountUsed;
//			s.deductAmount(amountUsed);  ****************************************
//			s.updateUnprofitableSupplyAmount(); ***************LOOK HERE***************

			System.out.println("UNPROFITABLE: (supplyID " + s.getSupplyId() + ") Org" + s.getProviderId() + " provided "
					+ amountUsed + s.getUnit());
		}

		return supplyList;
	}

	/**
	 * Calculate the price needed to pay for the most optimal amount of resources in
	 * the profitable supply pool.
	 * 
	 * @param resourceName
	 * @param amountNeeded
	 * @return the price for supplies in the profitable supply pool.
	 */
	double calculatePriceInProfitableSupplyPool(Demand demand) {
		int amountNeeded = demand.getAmountNeeded();
		double price = 0;
		int sum = 0;

		List<ProfitableSupplyDistancePair> profitableSupplyPool = getProfitableSupplyList(demand);
		Collections.sort(profitableSupplyPool);

		for (ProfitableSupplyDistancePair pair : profitableSupplyPool) {
			ProfitableSupply s = pair.getSupply();
			if (sum == amountNeeded) {
				break;
			}

			int amountStillNeeded = amountNeeded - sum;
			int amountUsed = (int) (s.getAmount() > amountStillNeeded ? amountStillNeeded : s.getAmount());
			sum += amountUsed;
			price += amountUsed * s.getUnitPrice();
		}

		return price;
	}

	/**
	 * Map the demand in the profitable supply pool.
	 * 
	 * @param demand
	 * @param fund   The fund available to pay for the resources in the profitable
	 *               supply pool.
	 * @return
	 */
	List<Supply> mapInProfitableSupplyPool(Demand demand, double fund) {
		int amountNeeded = demand.getAmountNeeded();
		double fundLeft = fund;
		int sum = 0;
		List<Supply> supplyList = new ArrayList<Supply>();

		List<ProfitableSupplyDistancePair> profitableSupplyPool = getProfitableSupplyList(demand);
		Collections.sort(profitableSupplyPool);

		for (ProfitableSupplyDistancePair pair : profitableSupplyPool) {
			ProfitableSupply s = pair.getSupply();
			int amountStillNeeded = amountNeeded - sum;

			// The amount of supply affordable with the fund.
			int amountAffordable = (int) (fundLeft / s.getUnitPrice());
			if (amountAffordable == 0) { // Since supplies with low unit prices rank ahead.
				break;
			}
			// The actual amount of supply provided considering both fund and amount.
			int amountProvided = (int) (amountAffordable > s.getAmount() ? s.getAmount() : amountAffordable);

			int amountUsed = amountProvided > amountStillNeeded ? amountStillNeeded : amountProvided;

			// Add supply to be used to the supply list
			ProfitableSupply sCopy = (ProfitableSupply) s.clone();
			sCopy.setAmount(amountUsed);
			supplyList.add(sCopy);

			// Update info
			sum += amountUsed;
			fundLeft -= amountUsed * s.getUnitPrice();
//			s.deductAmount(amountUsed); ***************LOOK HERE***************
//			s.updateProfitableSupplyAmount();   ***************LOOK HERE***************
			System.out.println("PROFITABLE: (supplyID " + s.getSupplyId() + ") Org" + s.getProviderId() + " provided "
					+ amountUsed + s.getUnit());
		}

		return supplyList;
	}
}
