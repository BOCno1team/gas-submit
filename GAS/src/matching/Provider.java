package matching;
import com.alibaba.fastjson.JSONObject;

import main.java.org.example.cfc.InvokeBCP;
import main.java.org.example.cfc.QueryBCP;

public class Provider extends Organization {
	private double defaultCoverRadius;

	private final static String chainCode = "gopackage1";
	
	public static void main(String args[]){
		//initOrganization(503, "sos", "demander", 31.2034, 121.4948, 5000);
		//initOrganization(501, "RedCross", "provider", 34.37971, 108.80859, 1500);

		
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{"501"};

		try {
			String jsonStr = query.query(chainCode,"queryByKey",queryArgs);
			System.out.println(jsonStr);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public Provider(int orgId, String name, int score, int rank, String orgType, double defaultLat, double defaultLon,
			double coverRadius) {
		super(orgId, name, score, rank, orgType, defaultLat, defaultLon);
		this.defaultCoverRadius = coverRadius;
	}

	/**
	 * Get cover radius from the chain using id.
	 * @param id
	 * @return
	 */
	public static double getCoverRadiusById(int id) {
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[]{Integer.toString(id)};
		double defaultCoverRadius = -1;
		try {
			String jsonStr = query.query(chainCode, "queryByKey",queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			defaultCoverRadius = Integer.parseInt(json.getString("defaultCoverRadius"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultCoverRadius;
	}
	
	/**
	 * Initialize an organization on the chain
	 * @param orgId
	 * @param name
	 * @param orgType
	 * @param lat
	 * @param lon
	 * @param coverRadius
	 */
	public static void initOrganization(int orgId, String name, String orgType, double lat, double lon, double coverRadius) {
		InvokeBCP invoke = new InvokeBCP();
		String[] invokeArgs = new String[]{String.valueOf(orgId), name, 
				String.valueOf(50), String.valueOf(2), orgType, String.valueOf(lat), 
				String.valueOf(lon), String.valueOf(coverRadius)};
		try {
			invoke.invoke(chainCode,"initProvider",invokeArgs);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Reconstruct an organization from the chain by its id.
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
		double coverRadius = -1;
	
		String jsonStr;
		try {
			jsonStr = query.query(chainCode,"queryByKey", queryArgs);
			JSONObject json = JSONObject.parseObject(jsonStr);
			orgId = id;
			name = json.getString("name");
			score = json.getIntValue("score");
			rank = json.getIntValue("rank");
			orgType = json.getString("orgType");
			lat = json.getDoubleValue("defaultLat");
			lon = json.getDoubleValue("defaultLon");
			coverRadius = json.getDoubleValue("defaultCoverRadius");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Provider org = new Provider(orgId, name, score, rank, orgType, lat, lon, coverRadius);
		return org;
	}
	
	
	/*
	 * Getters and setters
	 */
	public double getDefaultCoverRadius() {
		return defaultCoverRadius;
	}

	public void setDefaultCoverRadius(double defaultCoverRadius) {
		this.defaultCoverRadius = defaultCoverRadius;
	}

}
