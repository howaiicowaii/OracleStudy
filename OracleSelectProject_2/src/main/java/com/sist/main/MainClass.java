package com.sist.main;
// JSP 로 변경 
import java.util.*;
import com.sist.dao.*;
public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EmpDAO dao=new EmpDAO();
//		ArrayList<EmpVO> list=dao.empAllData();
//		for(EmpVO vo:list)
//		{
//			System.out.println(vo.getEmpno()+" "
//					+vo.getEname()+" "
//					+vo.getJob()+" "
//					+vo.getHiredate()+" "
//					+vo.getSal()+" "
//					+vo.getDvo().getDname()+" "
//					+vo.getDvo().getLoc()+" "
//					+vo.getSvo().getGrade());
//		}
//		
//		ArrayList<EmpVO> list=dao.subqueryEmpData();
//		for(EmpVO vo:list)
//		{
//			System.out.println(vo.getEmpno()+" "
//					+vo.getEname()+" "
//					+vo.getDvo().getDname()+" "
//					+vo.getDvo().getLoc()+" "
//					+vo.getHiredate()+" "
//					+vo.getSal());
//		}
		
		ArrayList<EmpVO> list=dao.subqueryInEmpListData();
		for(EmpVO vo:list)
		{
			System.out.println(vo.getEmpno()+" "
					+vo.getEname()+" "
					+vo.getDvo().getDname()+" "
					+vo.getDvo().getLoc()+" "
					+vo.getHiredate()+" "
					+vo.getSal());
		}
	}

}
