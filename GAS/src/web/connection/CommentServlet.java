package web.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import main.java.org.example.cfc.QueryBCP;
import matching.Demand;
import matching.Organization;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet(description = "comment", urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String chainCode = "gopackage1";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		/* 星号表示所有的异域请求都可以接受， */
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		PrintWriter out=response.getWriter();
		// 读取请求内容
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		//这里读出来的字符串时JSON格式的
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		//将json字符串转换为json对象
		System.out.println(sb.toString());
		JSONObject json = JSONObject.parseObject(sb.toString());

//		System.out.println(json.toJSONString());
//		System.out.println(json.getString("newProvider"));
		
		int demandId = json.getIntValue("demandId");
		String key = demandId + "-MatchResult";
		QueryBCP query = new QueryBCP();
		String[] queryArgs = new String[] { key };
		JSONArray resultArray = new JSONArray();
		
		try {
			String jsonStr = query.query(chainCode, "queryByKey", queryArgs);
			JSONObject json1 = JSONObject.parseObject(jsonStr);
			
			
			JSONArray orgToSupply = json1.getJSONArray("orgToSupplyList");
					
			for (int j = 0;j < orgToSupply.size(); j ++){
				JSONObject orgInfo = new JSONObject();
				JSONObject orgObject = orgToSupply.getJSONObject(j);
				
				String orgIdStr = orgObject.getString("orgId");
				orgInfo.put("orgId", Integer.valueOf(orgIdStr));
				
				//query org info
				String[] queryArgs2 = new String[] { orgIdStr };
	            String jsonStr2 = query.query(chainCode, "queryByKey", queryArgs2);
				JSONObject json2 = JSONObject.parseObject(jsonStr2);
				String orgName = json2.getString("orgName");
				String orgType = json2.getString("orgType");
				orgInfo.put("orgName", orgName);
				orgInfo.put("orgType", orgType);
				
				//query supply info
				String supplyListString = orgObject.getString("supplyList");
				if (supplyListString.equals("")) {//Demander
					String demandName = Demand.getNameById(demandId);
					String amount = Demand.getAmountById(demandId);
					String unit = Demand.getUnitById(demandId);
					orgInfo.put("demandName", demandName);
					orgInfo.put("demandAmount", amount);
					resultArray.add(orgInfo);
					continue;
				}
				String[] supplyList = supplyListString.split(",");
				JSONArray supplyArray = new JSONArray();
				for (String supplyId : supplyList) {
					String[] queryArgs3 = new String[] { supplyId };
		            String jsonStr3 = query.query(chainCode, "queryByKey", queryArgs3);
					JSONObject json3 = JSONObject.parseObject(jsonStr3);
					
					JSONObject supply = new JSONObject();
					String supplyName = json3.getString("name");
					Double supplyAmount = json3.getDoubleValue("amount");
					String unit = json3.getString("unit");
					
					supply.put("supplyName", supplyName);
					supply.put("supplyAmount", supplyAmount);
					supply.put("unit", unit);
					supplyArray.add(supply);
				}
				orgInfo.put("supplies", supplyArray);
				
				resultArray.add(orgInfo);
			}	
			
			//out.append(resultArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		json.put("resultArray", resultArray);
		//将JSON返回前端
		out.append(json.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
