package com.greedy.section01.problem;

import static com.greedy.common.JDBCTemplate.close;
import static com.greedy.common.JDBCTemplate.getConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Application {
	
	public static void main(String[] args) {
		
		/* 메뉴 등록하기 */
		
		Connection con = getConnection();
		
		Properties prop = new Properties();
		
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		
		ResultSet rset1 = null;
		ResultSet rset2 = null;
		
		int result = 0;
		int maxMenuCode = 0;
		List<Map<Integer,String>> categoryList = null; // integer를 int로 형변환 할 수 있음
		
		try {
			prop.loadFromXML(new FileInputStream("mapper/menu-query.xml"));
			String query1 = prop.getProperty("selectLastMenuCode");
			String query2 = prop.getProperty("selectAllCategoryList");
			String query3 = prop.getProperty("insertMenu");

			pstmt1 = con.prepareStatement(query1);
			pstmt2 = con.prepareStatement(query2);
			pstmt3 = con.prepareStatement(query3);
			
			rset1 = pstmt1.executeQuery();
			
			if(rset1.next()) {
				maxMenuCode = rset1.getInt("MAX(A.MENU_CODE)");
			}
			
			System.out.println("maxMenuCode : " + maxMenuCode);
			
			// 카테고리 메뉴 가져오기
			rset2 = pstmt2.executeQuery();
			
			categoryList = new ArrayList<>();
			
			while(rset2.next()) {
				
				// String 형태를 Map으로 만들어서 사용한다.
				Map<Integer, String> category = new HashMap<>();
				
				category.put(rset2.getInt("CATEGORY_CODE"), rset2.getString("CATEGORY_NAME"));
				
				categoryList.add(category);
			}
			
			System.out.println("categoryList : " + categoryList);
			
			Scanner sc = new Scanner(System.in);
			System.out.print("등록할 메뉴의 이름을 입력하세요 : ");
			String menuName = sc.nextLine();
			System.out.print("신규 메뉴의 가격을 입력하세요 : ");
			int menuPrice = sc.nextInt();
			System.out.print("카테고리 번호를 선택해주세요(1.식사, 2.음료, 3.디저트., 4.한식, 5.퓨전) : ");
			sc.nextLine();
			String categoryName = sc.nextLine();
			System.out.print("바로 판매 메뉴에 적용하시겠습니다까?(예/아니오)");
			String answer = sc.nextLine();
			
			int categoryCode = 0;
			switch(categoryName) {
			   case "식사" : categoryCode = 1; break;
			   case "음료" : categoryCode = 2; break;
			   case "디저트" : categoryCode = 3; break;
			   case "한식" : categoryCode = 4; break;
			   case "퓨전" : categoryCode = 5; break;			
			}
			
			String orderableStatus = "";
			switch(answer) {
			    case "예" : orderableStatus = "Y"; break; 
			    case "아니요" : orderableStatus = "N"; break; 
			}
			
			pstmt3.setInt(1, maxMenuCode + 1);
			pstmt3.setString(2, menuName);
			pstmt3.setInt(3, menuPrice);
			pstmt3.setInt(4, categoryCode);
			pstmt3.setString(5, orderableStatus);
			
			result = pstmt3.executeUpdate();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset1);
			close(rset2);
			close(pstmt1);
			close(pstmt2);
			close(pstmt3);
			close(con);
		}
		
		if(result > 0) {
			System.out.println("메뉴 등록 성공");
		} else {
			System.out.println("메뉴 등록 실패");
		}

	}

}