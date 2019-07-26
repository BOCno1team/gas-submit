package web.connection;

import java.io.BufferedReader;

import matching.Organization;
import matching.ProfitableSupply;
import matching.UnprofitableSupply;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class hello
 */
@WebServlet(description = "test", urlPatterns = { "/hello" })
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public HelloServlet() {
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
		String supplyName = json.getString("supplyName");
		int provider = json.getIntValue("provider");//供给方ID
		JSONArray array = JSONObject.parseArray(json.getString("supplyFormList"));
		JSONObject obj = null;
		for(int i = 0 ; i < array.size() ; i++){			
			obj = array.getJSONObject(i);
			String name = obj.getString("name");
			int id = obj.getIntValue("supplyId");
			double amount = obj.getDoubleValue("amount");
			String unit = obj.getString("unit");
			double price = obj.getDoubleValue("unitPrice");
			if (price == 0) {
				UnprofitableSupply s = new UnprofitableSupply(id, name, amount, unit, provider, Organization.getRankById(provider), 0, 0, 10);
				s.uplinkUnprofitableSupply();
			} else {
				ProfitableSupply s = new ProfitableSupply(id, name, amount, unit, provider, price, 0, 0, 10);
				s.uplinkProfitableSupply();
			}
		}
			
		
		
		//System.out.println(array.toString());
		
		//TODO
		/**
		 * 需要在这里加入各种处理逻辑，以前端JSON中获取的内容为输入，调用撮合算法或是信息发布等功能，
		 * 并获取返回值（全部基于java对象进行操作）
		 * */
		
		
		//组织返回的内容
		json = new JSONObject();
		json.put("res", "aaa");
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
