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
import matching.MatchResult;
import matching.Organization;

/**
 * Servlet implementation class WorkingMessageServlet
 */
@WebServlet("/WorkingMessageServlet")
public class WorkingMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WorkingMessageServlet() {
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
		int demandId = Integer.valueOf(json.getString("goodsId"));
		String message = json.getString("content");
//		System.out.println(json.toJSONString());
//		System.out.println(json.getString("newProvider"));		
		int userId = Integer.valueOf(Demand.getDemanderIdById(demandId));
		System.out.println(demandId+" "+ message +" "+userId);
		MatchResult.giveMessage(demandId, userId, message);
		//组织返回的内容
		json = new JSONObject();
		String[] orgStrList= MatchResult.queryOrgList(demandId);
		JSONArray orgList = new JSONArray();
		for (String orgStrId : orgStrList) {
			int orgId = Integer.valueOf(orgStrId);
			JSONObject orgIdAndName = new JSONObject();
					
			String orgType = Organization.getTypeById(orgId);
			orgIdAndName.put("orgType", orgType);
			
			String orgName = Organization.getNameById(orgId);
//			if (orgType.equals("demander")) {
//				orgIdAndName.put("demanderName", orgName);
//				orgIdAndName.put("demanderId", orgId);
//			} else {
			orgIdAndName.put("orgName", orgName);
			orgIdAndName.put("orgId", orgId);
			//}
				
			orgList.add(orgIdAndName);
		}
		JSONObject mockExecutor = new JSONObject();
		mockExecutor.put("orgType", "executor");
		mockExecutor.put("orgId", "301");
		mockExecutor.put("orgName", "dumpExcutor");
		orgList.add(mockExecutor);
		
		json.put("orgList", orgList);
		//将JSON返回前端
		System.out.println(json.toString());
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
