package matching;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import main.java.org.example.cfc.InvokeBCP;
import main.java.org.example.cfc.QueryBCP;

public class Organization {
	private int orgId;
	private String name;
	private int score; // range from 0 to 100
	private int rank;
	private String orgType;
	
	//IBM location API could be applied here to get latitude & longitude info.
	private double defaultLat;
	private double defaultLon;
	//block chain connection profile
	private final static String chainCode = "gopackage1";
	private final static String fcnName = "queryByKey";
	
	
	public static void main(String args[]){

		
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{"9001-MatchResult"};

		try {
			String jsonStr = query.query(chainCode,"queryByKey",queryArgs);
			System.out.println(jsonStr);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public Organization(int orgId, String name, int score, int rank, String orgType, double defaultLat, double defaultLon) {
		super();
		this.orgId = orgId;
		this.name = name;
		this.score = score;
		this.rank = rank;
		this.orgType = orgType;
		this.defaultLat = defaultLat;
		this.defaultLon = defaultLon;
	}
	
	/**
	 * Initialize an organization on the chain.
	 * @param orgId
	 * @param name
	 * @param orgType
	 * @param lat
	 * @param lon
	 */
	public static void initOrganization(int orgId, String name, String orgType, double lat, double lon) {
		InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(orgId), name, 
				String.valueOf(50), String.valueOf(2), orgType, String.valueOf(lat), String.valueOf(lon)};
		try {
			invoke.invoke(chainCode,"initOrganization",invokeArgs);
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the type of the organization with id
	 * @param id
	 * @return
	 */
	public static String getTypeById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		String orgType=null;
		try {
			String jsonStr = query.query(chainCode,fcnName,queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			orgType = json.getString("orgType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgType;
	}
	
	/**
	 * Get name of the organization with id
	 * @param id
	 * @return
	 */
	public static String getNameById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		String orgType=null;
		try {
			String jsonStr = query.query(chainCode,fcnName,queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			orgType = json.getString("name");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgType;
	}
	
	/**
	 * Get rank of the organization with id
	 * @param id
	 * @return
	 */
	public static int getRankById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		int rank = -1;
		try {
			String jsonStr = query.query(chainCode,fcnName,queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			rank = Integer.parseInt(json.getString("rank"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rank;
	}
	
	public static double[] getLocationById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		double lat = -1;
		double lon = -1;
		try {
			String jsonStr = query.query(chainCode,fcnName,queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			lat = json.getDoubleValue("defaultLat");
			lon = json.getDoubleValue("defaultLon");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new double[] {lat, lon};
	}
	
	/**
	 * Get score of the organization with id
	 * @param id
	 * @return
	 */
	public static int getScoreById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		int score = -1;
		try {
			String jsonStr = query.query(chainCode,fcnName,queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			score = Integer.parseInt(json.getString("score"));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return score;
	}
	
	/**
	 * Reconstruct a organization object by querying from the chain.
	 * @param id
	 * @return
	 */
	public static Organization queryOrgById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		
		int orgId = 0;
		String name = null;
		int score = 0;
		int rank = 0;
		String orgType = null;
		double lat = -1;
		double lon = -1;
	
		String jsonStr;
		try {
			jsonStr = query.query(chainCode,fcnName,queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			orgId = id;
			name = json.getString("name");
			score = json.getIntValue("score");
			rank = json.getIntValue("rank");
			orgType = json.getString("orgType");
			lat = json.getDoubleValue("defaultLat");
			lon = json.getDoubleValue("defaultLon");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Organization org = new Organization(orgId, name, score, rank, orgType, lat, lon);
		return org;
	}
	
	/**
	 * Update organization rank after receiving feedback from other organizations.
	 * @param avg1
	 * @param avg2
	 * @param avg3
	 * @param avg4
	 * @param avg5
	 */
	public void updateOrganization(int avg1, int avg2, int avg3, int avg4, int avg5) {
		InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(this.getOrgId()),
				String.valueOf(avg1),String.valueOf(avg2),String.valueOf(avg3),String.valueOf(avg4),String.valueOf(avg5)};
		try {
			invoke.invoke(chainCode,"updateOrganization",invokeArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/*
	 * Getters and setters
	 */
	public double getDefaultLat() {
		return defaultLat;
	}

	public void setDefaultLat(double defaultLat) {
		this.defaultLat = defaultLat;
	}

	public double getDefaultLon() {
		return defaultLon;
	}

	public void setDefaultLon(double defaultLon) {
		this.defaultLon = defaultLon;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	
}