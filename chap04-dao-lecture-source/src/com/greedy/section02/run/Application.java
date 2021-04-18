package com.greedy.section02.run;

import static com.greedy.common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.greedy.section02.model.dao.MenuDAO;
import com.greedy.section02.model.dto.MenuDTO;

public class Application {
	
	// section01.problem의 Application 내용을 복사하여 DTO, DAO와 Application 구성
	
	public static void main(String[] args) {
		
		Connection con = getConnection();
		MenuDAO registDAO = new MenuDAO();
		
		/* 메뉴의 마지막 번호 조회 */
		int maxMenuCode = registDAO.selectLastMenuCode(con);
		
		System.out.println("maxMenuCode : " + maxMenuCode);
		
		/* 카테고리 조회 */
		List<Map<Integer,String>> categoryList = registDAO.selectAllCategory(con);
		
		for(Map<Integer, String> category : categoryList) {
			
			System.out.println(category);
		}
		
		/* 3. 신규 메뉴 등록 */
		/* 3-1. 신규 메뉴 등록을 위한 정보 입력 */
		System.out.println("categoryList : " + categoryList);
		
		Scanner sc = new Scanner(System.in);
		System.out.print("등록할 메뉴의 이름을 입력하세요 : ");
		String menuName = sc.nextLine();
		System.out.print("신규 메뉴의 가격을 입력하세요 : ");
		int menuPrice = sc.nextInt();
		System.out.print("카테고리를 선택해주세요(식사, 음료, 디저트., 한식, 퓨전) : ");
		sc.nextLine();
		String categoryName = sc.nextLine();
		System.out.print("바로 판매 메뉴에 적용하시겠습니다까?(예/아니오)");
		String answer = sc.nextLine();
		
		/* 3-2. 신규 메뉴 등록을 위한 값 제공 */
		int menuCode = maxMenuCode + 1;
		
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
		
		MenuDTO newMenu = new MenuDTO(menuCode, menuName, menuPrice, categoryCode, orderableStatus);
		
		/* 3-3. 신규 메뉴 등록을 위한 메소드 호출하여 등록 */
		int result = registDAO.insertNewMenu(con,newMenu);
		
		if(result > 0) {
			System.out.println("메뉴 등록 성공");
		} else {
			System.out.println("메뉴 등록 실패");
		}
	
	}

}