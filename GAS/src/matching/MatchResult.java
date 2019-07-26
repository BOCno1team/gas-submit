package matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import main.java.org.example.cfc.InvokeBCP;
import main.java.org.example.cfc.QueryBCP;

public class MatchResult {
	private Demand demand = null;
	private List<Supply> unprofitableList = null;
	private List<Supply> profitableList = null;
	private List<Supply> fundList = null;
	private double sumGethered;

	private final static String chainCode = "gopackage1";

	public MatchResult(Demand demand, List<Supply> unprofitableList, List<Supply> profitableList, List<Supply> fundList,
			double sumGethered) {
		super();
		this.demand = demand;
		this.unprofitableList = unprofitableList;
		this.profitableList = profitableList;
		this.fundList = fundList;
		this.sumGethered = sumGethered;
	}

	public void uplinkMatchResult() {
		String key = this.demand.getDemandId() + "-" + "MatchResult";
		JSONObject result = new JSONObject();

		JSONArray unprofitableArray = new JSONArray();
		JSONArray profitableArray = new JSONArray();
		JSONArray fundArray = new JSONArray();
		JSONArray demandArray = new JSONArray();

		Demand demand = this.getDemand();
		JSONObject jsonDemand = new JSONObject();
		jsonDemand.put("demandId", demand.getDemandId());
		jsonDemand.put("name", demand.getName());
		jsonDemand.put("amount", demand.getAmountNeeded());
		jsonDemand.put("unit", demand.getUnit());
		jsonDemand.put("demanderId", demand.getDemanderId());

		for (Supply s : this.getUnprofitableList()) {
			JSONObject item = new JSONObject();
			item.put("supplyId", s.getSupplyId());
			item.put("name", s.getName());
			item.put("amount", s.getAmount());
			item.put("unit", s.getUnit());
			item.put("providerId", s.getProviderId());
			item.put("providerName", Organization.getNameById(s.getProviderId()));
			unprofitableArray.add(item);
		}

		for (Supply s : this.getProfitableList()) {
			JSONObject item = new JSONObject();
			item.put("supplyId", s.getSupplyId());
			item.put("name", s.getName());
			item.put("amount", s.getAmount());
			item.put("unit", s.getUnit());
			item.put("unitPrice", ((ProfitableSupply) s).getUnitPrice());
			item.put("providerId", s.getProviderId());
			item.put("providerName", Organization.getNameById(s.getProviderId()));
			profitableArray.add(item);
		}

		for (Supply s : this.getFundList()) {
			JSONObject item = new JSONObject();
			item.put("supplyId", s.getSupplyId());
			item.put("name", "Fund");
			item.put("amount", s.getAmount());
			item.put("unit", "USD");
			item.put("providerId", s.getProviderId());
			item.put("providerName", Organization.getNameById(s.getProviderId()));
			fundArray.add(item);
		}
		
		JSONArray orgList = new JSONArray();
		List<String> involvedOrgs = new ArrayList<>();
		for (int orgId : this.getAllInvolvedOrg()) {
			JSONObject orgToSupply = new JSONObject();
			orgToSupply.put("orgId", orgId);
			List<String> supplyList = this.getSupplyIdsForOrg(orgId);
			
			String supplyString = String.join(",", supplyList);
			orgToSupply.put("supplyList", supplyString);
			
			orgList.add(orgToSupply);
			involvedOrgs.add(String.valueOf(orgId));
		}
		String orgListString = String.join(",", involvedOrgs);

		result.put("Demand", jsonDemand);
		result.put("UnprofitableSupplyList", unprofitableArray);
		result.put("ProfitableSupplyList", profitableArray);
		result.put("fundList", fundArray);
		result.put("involvedOrgList", orgListString);
		result.put("orgToSupplyList", orgList);

		String resultString = JSONObject.toJSONString(result);
		String[] invokeArgs = new String[] { key, resultString };

		try {
			InvokeBCP invoke = new InvokeBCP();
			invoke.invoke(chainCode, "set", invokeArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the list containing the demandIds that each organization participates
	 * in.
	 */
	void updateOrgParticipationList() {
		List<Integer> orgList = getAllInvolvedOrg();
		for (int orgId : orgList) {
			String key = orgId + "-ParticipationList";
			QueryBCP query = new QueryBCP();
			String[] queryArgs = new String[] { key };

			try {
				String jsonStr = query.query(chainCode, "queryByKey", queryArgs);
				JSONObject json = JSONObject.parseObject(jsonStr);
				JSONArray demandToSupply = json.getJSONArray("demandList");
				
				List<String> supplyList = getSupplyIdsForOrg(orgId);			
				String newListString = String.join(",", supplyList);

				JSONObject item = new JSONObject();
				item.put("demandId", this.getDemand().getDemandId());
				item.put("supplyList", newListString);

				demandToSupply.add(item);
				json.put("demandList", demandToSupply);

				String newJSONString = JSONObject.toJSONString(json);

				String[] invokeArgs = new String[] { key, newJSONString };
				InvokeBCP invoke = new InvokeBCP();
				invoke.invoke(chainCode, "set", invokeArgs);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Confirm the work is finished by organization with orgID.
	 */
	public static void confirmOrgStatus(int demandId, int orgId) {
		String key = demandId + orgId + "-status";
		String[] invokeArgs = new String[] { key, "1" };
		InvokeBCP invoke = new InvokeBCP();
		try {
			invoke.invoke(chainCode, "set", invokeArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Get contract status for the organization.
	 */
	public static int getOrgStatus(int demandId, int orgId) {
		int status = 0;
		
		String key = demandId + orgId + "-status";
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[] { key };

		try {
			String statusStr = query.query(chainCode, "queryByKey", queryArgs);
			status = Integer.valueOf(statusStr);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return status;
	}

	/**
	 * Organization with orgId gives a message for demand with demandId
	 * @param demandId
	 * @param orgId
	 * @param msg
	 */
	public static void giveMessage(int demandId, int orgId, String msg) {
		String key = demandId + "-message";
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[] { key };
		String separate = "\n";
		try {
			String jsonStr = query.query(chainCode, "queryByKey", queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			JSONArray msgList = json.getJSONArray("msgList");
//			for (int j = 0;j< msgList.size();j++){
//                JSONObject cur = msgList.getJSONObject(j);
//                if (cur.getIntValue("orgId") == orgId) {
//                	String prevMsg = cur.getString("messages");
//                	String newMsg = prevMsg + separate + msg;
//                	cur.put("messages", newMsg);
//                	//TODO delete & add again?
//                	msgList.put(j, cur);
//                }
//           }
			JSONObject newJSON = new JSONObject();
			newJSON.put("orgId", orgId);
			newJSON.put("message", msg);
			msgList.add(newJSON);

			json.put("msgList", msgList);

			String newJSONString = JSONObject.toJSONString(json);

			String[] invokeArgs = new String[] { key, newJSONString };
			InvokeBCP invoke = new InvokeBCP();
			invoke.invoke(chainCode, "set", invokeArgs);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Give feedback to the organization with receiverId.
	 * 
	 * @param demandId
	 * @param receiverId
	 * @param grade1
	 * @param grade2
	 * @param grade3
	 * @param grade4
	 * @param grade5
	 */
	void giveFeedback(int receiverId, int grade1, int grade2, int grade3, int grade4, int grade5) {
		String key = this.getDemand().getDemandId() + "-" + Integer.toString(receiverId);
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[] { key };

		try {
			String jsonStr = query.query(chainCode, "queryByKey", queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			int sum1 = json.getIntValue("grade1");
			int sum2 = json.getIntValue("grade2");
			int sum3 = json.getIntValue("grade3");
			int sum4 = json.getIntValue("grade4");
			int sum5 = json.getIntValue("grade5");
			int count = json.getIntValue("count");
			int totalCount = json.getIntValue("total");
			count++;

			JSONObject newGrade = new JSONObject();
			int new1 = grade1 + sum1;
			int new2 = grade2 + sum2;
			int new3 = grade3 + sum3;
			int new4 = grade4 + sum4;
			int new5 = grade5 + sum5;

			newGrade.put("grade1", new1);
			newGrade.put("grade2", new2);
			newGrade.put("grade3", new3);
			newGrade.put("grade4", new4);
			newGrade.put("grade5", new5);
			newGrade.put("count", count);
			newGrade.put("total", totalCount);

			String newGradeString = JSONObject.toJSONString(newGrade);
			String[] invokeArgs = new String[] { key, newGradeString };
			InvokeBCP invoke = new InvokeBCP();
			invoke.invoke(chainCode, "set", invokeArgs);

			if (count == totalCount) {
				Organization receiver = Organization.queryOrgById(receiverId);
				receiver.updateOrganization(new1 / totalCount, new2 / totalCount, new3 / totalCount, new4 / totalCount,
						new5 / totalCount);
				System.out.println("Updated the organization's new grade");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get the IDs of organizations that orgId needs to give & get feedback.
	 * 
	 * @param orgId
	 * @return
	 */
	public List<Integer> getFeedbackOrgs(int orgId) {
		String orgType = Organization.getTypeById(orgId);

		List<Integer> orgList = getAllInvolvedOrg();
		List<Integer> result = new ArrayList<Integer>();

		for (int ID : orgList) {
			if (orgId != ID && Organization.getTypeById(ID) != orgType) {
				result.add(ID);
			}
		}

		return result;
	}
	
	

	/**
	 * Calculate the number of organizations that it needs to give & get feedback.
	 * 
	 * @param orgId
	 * @return the number of organizations that it needs to give & get feedback.
	 */
	public int calculateNumOfMembers(int orgId) {
		return getFeedbackOrgs(orgId).size();
	}

	public void prepareForFeedback() {
		List<Integer> orgList = getAllInvolvedOrg();
		for (int orgId : orgList) {
			initOneFeedback(orgId);
		}
	}
	
	/**
	 * Initialize the message for this demand to an empty string on the chain.
	 */
	public void prepareForMessage() {
		String key = this.getDemand().getDemandId() + "-message";
		String[] invokeArgs = new String[] { key, "" };
		InvokeBCP invoke = new InvokeBCP();
		try {
			invoke.invoke(chainCode, "set", invokeArgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * Initialize status for organizations to confirm completion of the contract later
	 */
	public void prepareForStatus() {
		for (int orgId : getAllInvolvedOrg()) {
			String key = this.getDemand().getDemandId() + orgId + "-status";
			String[] invokeArgs = new String[] { key, "0" };
			InvokeBCP invoke = new InvokeBCP();
			try {
				invoke.invoke(chainCode, "set", invokeArgs);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	
	public void prepareForFollowUp() {
		prepareForFeedback();
		prepareForMessage();
		prepareForStatus();
	}

	/**
	 * Initialize the grading data for the org with receiverID.
	 * 
	 * @param receiverId
	 */
	public void initOneFeedback(int receiverId) {
		String key = Integer.toString(this.demand.getDemandId()) + "-" + Integer.toString(receiverId); ////////// 1
		// String key = "1501";
		JSONObject initialGrade = new JSONObject();
		int total = calculateNumOfMembers(receiverId);

		initialGrade.put("grade1", 0);
		initialGrade.put("grade2", 0);
		initialGrade.put("grade3", 0);
		initialGrade.put("grade4", 0);
		initialGrade.put("grade5", 0);
		initialGrade.put("count", 0);
		initialGrade.put("total", total);

		String initialGradeString = JSONObject.toJSONString(initialGrade);
		String[] invokeArgs = new String[] { key, initialGradeString };

		try {
			InvokeBCP invoke = new InvokeBCP();
			invoke.invoke(chainCode, "set", invokeArgs);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * Get the IDs of all the organizations involved in this transaction.
	 * 
	 * @return IDs of all the organizations involved in this transaction.
	 */
	private List<Integer> getAllInvolvedOrg() {
		List<Integer> orgList = new ArrayList<>();
		orgList.add(this.getDemand().getDemanderId());

		for (Supply s : unprofitableList) {
			if (!orgList.contains(s.getProviderId())) {
				orgList.add(s.getProviderId());
			}
		}

		for (Supply s : profitableList) {
			if (!orgList.contains(s.getProviderId())) {
				orgList.add(s.getProviderId());
			}
		}

		for (Supply s : fundList) {
			if (!orgList.contains(s.getProviderId())) {
				orgList.add(s.getProviderId());
			}
		}
		return orgList;
	}

	/**
	 * Assume the organization type for orgId is provider or executor.
	 * 
	 * @param orgId
	 * @return
	 */
	private List<String> getSupplyIdsForOrg(int orgId) {
		List<Supply> total = new ArrayList<>();
		total.addAll(unprofitableList);
		total.addAll(profitableList);
		total.addAll(fundList);

		List<String> result = new ArrayList<>();
		String orgType = Organization.getTypeById(orgId);
		if (orgType.equals("demander")) {
			return result;
		}	
		for (Supply s : total) {
			if (((Integer) s.getProviderId()).equals(orgId)) {
				result.add(""+s.getSupplyId());
			}
		}

		return result;
	}
	
	public static String[] queryOrgList(int demandId) {
		String key = demandId + "-" + "MatchResult";
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[] { key };
		String jsonStr = "";
		
		try {
			jsonStr = query.query(chainCode, "queryByKey", queryArgs);
			System.out.println(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject json = JSONObject.parseObject(jsonStr);
		String orgListString = json.getString("involvedOrgList");
		String[] orgList = orgListString.split(",");	
		return orgList;
	}

	/*
	 * Getters and setters
	 */
	public Demand getDemand() {
		return demand;
	}

	public void setDemand(Demand demand) {
		this.demand = demand;
	}

	public List<Supply> getUnprofitableList() {
		return unprofitableList;
	}

	public void setUnprofitableList(List<Supply> unprofitableList) {
		this.unprofitableList = unprofitableList;
	}

	public List<Supply> getProfitableList() {
		return profitableList;
	}

	public void setProfitableList(List<Supply> profitableList) {
		this.profitableList = profitableList;
	}

	public List<Supply> getFundList() {
		return fundList;
	}

	public void setFundList(List<Supply> fundList) {
		this.fundList = fundList;
	}

	public double getSumGethered() {
		return sumGethered;
	}

	public void setSumGethered(double sumGethered) {
		this.sumGethered = sumGethered;
	}
	
	public static void main(String[] args) {
		// initOneFeedback(501);

//		String key = Integer.toString(901) + "-" + Integer.toString(503);
//		QueryBCP query = new QueryBCP();
//		String[] queryArgs = new String[] { key };
//
//		try {
//			String jsonStr = query.query(chainCode, "queryByKey", queryArgs);
//			System.out.println(jsonStr);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		System.out.println(MatchResult.queryOrgList(9001));
	}
}
