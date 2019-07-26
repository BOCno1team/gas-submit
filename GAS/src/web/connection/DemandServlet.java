package web.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import matching.Demand;
import matching.MatchResult;
import matching.Organization;
import matching.ProfitableSupply;
import matching.Supply;
import matching.UnprofitableSupply;

/**
 * Servlet implementation class DemandServlet
 */
@WebServlet(description = "DemandServlet", urlPatterns = { "/demand" })
public class DemandServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DemandServlet() {
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
		int demanderId = json.getIntValue("DemanderId");//需求方ID
		System.out.println(demanderId);
//		System.out.println(json.getString("newProvider"));
//		//获取JSON中的内容，此处取id对应的内容
		JSONArray array = JSONObject.parseArray(json.getString("demandFormList"));
		System.out.println(array.size());
		JSONObject obj = null;
		List<Demand> demandList = new ArrayList<>();
		for(int i = 0 ; i < array.size() ; i++){
			obj = array.getJSONObject(i);
			String name = obj.getString("name");
			String category = obj.getString("categoty");
			int id = obj.getIntValue("demandId");
			int amount = obj.getIntValue("amount");
			String unit = obj.getString("unit");
			
			Demand demand = new Demand(id, name, category, amount, unit, 1, demanderId, 0, 0);
			demandList.add(demand);
			//TODO: uplink demand.
			demand.uplinkDemand();
		}
		
		JSONArray unprofitableArray = new JSONArray();
		JSONArray profitableArray = new JSONArray();
		JSONArray fundArray = new JSONArray();
		JSONArray demandArray = new JSONArray();
		
		Collections.sort(demandList);
		for (Demand d : demandList) {
			MatchResult result = d.matchToSupply();
			result.uplinkMatchResult();
			
			Demand demand = result.getDemand();
			
			JSONObject jsonDemand = new JSONObject();
			jsonDemand.put("demandId", demand.getDemandId());
			jsonDemand.put("name", demand.getName());
			jsonDemand.put("amount", demand.getAmountNeeded());
			jsonDemand.put("unit", demand.getUnit());
			jsonDemand.put("demanderId", demand.getDemanderId());
			demandArray.add(jsonDemand);
			
			for (Supply s : result.getUnprofitableList()) {
				JSONObject item = new JSONObject();
				item.put("supplyId",s.getSupplyId());
				item.put("name", s.getName());
				item.put("amount", s.getAmount());
				item.put("unit", s.getUnit());
				item.put("providerId", s.getProviderId());
				item.put("providerName", Organization.getNameById(s.getProviderId()));
				unprofitableArray.add(item);
			}
						
			for (Supply s : result.getProfitableList()) {
				JSONObject item = new JSONObject();
				item.put("supplyId",s.getSupplyId());
				item.put("name", s.getName());
				item.put("amount", s.getAmount());
				item.put("unit", s.getUnit());
				item.put("unitPrice", ((ProfitableSupply) s).getUnitPrice());
				item.put("providerId", s.getProviderId());
				item.put("providerName", Organization.getNameById(s.getProviderId()));
				profitableArray.add(item);
			}
			
			for (Supply s : result.getFundList()) {
				JSONObject item = new JSONObject();
				item.put("supplyId",s.getSupplyId());
				item.put("name", "Fund");
				item.put("amount", s.getAmount());
				item.put("unit", "USD");
				item.put("providerId", s.getProviderId());
				item.put("providerName", Organization.getNameById(s.getProviderId()));
				fundArray.add(item);
			}				
		}
		
		json.put("DemandList", demandArray);
		json.put("UnprofitableSupplyList", unprofitableArray);
		json.put("ProfitableSupplyList", profitableArray);
		json.put("fundList", fundArray);
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
