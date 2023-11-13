package com.sist.dao;
import java.util.*;
import java.sql.*;
public class MovieDAO {
	// 오라클 연결 
	private Connection conn;
	// SQL 송수신
	private PreparedStatement ps;
	// 오라클 주소 
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	// 드라이버 등록 => 생성자에서 생성하는 게 기본 
	public MovieDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 클래스 메모리 할당 (new,Class.forName)
			// 클래스명을 등록 => 패키지부터 등록 => Spring
		}catch(Exception ex) {}
	}
	// 오라클 연결
	public void getConnection()
	{
		try
		{
			conn=DriverManager.getConnection(URL,"hr","happy");
			// conn hr/happy 수행 
		}catch(Exception ex) {}
	}
	// 오라클 닫기 
	public void disConnection()
	{
		try
		{
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		}catch(Exception ex) {}
	}
	////////////////////////// DAO 는 다 동일 
	// 기능 설정 
	// 검색 => 여러개면 ArrayList / 한개면 => VO
	// VO => 영화 1개에 대한 모든 정보를 가지고 있다 
	public ArrayList<MovieVO> movieFindData(String column,String fd)
	{
		ArrayList<MovieVO> list=
				new ArrayList<MovieVO>();
		try
		{
			getConnection();
			String sql="SELECT title,genre,actor "
					+ "FROM movie "
					+ "WHERE "+column+" LIKE '%'||?||'%'"; // ? = 사용자 입력값
			// 테이블명이나 컬럼명은 문자열 결합 => 검색어는 ''
			ps=conn.prepareStatement(sql);
			ps.setString(1, fd);
			// 결과값 받기 => ?가 있는데 값을 설정하지 않으면 오류 발생 
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				// rs.next()를 하면 => 한줄씩 읽어 온다 
				MovieVO vo=new MovieVO();
				vo.setTitle(rs.getString(1));
				vo.setGenre(rs.getString(2));
				vo.setActor(rs.getString(3));
				
				list.add(vo);
			}
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		return list;
	}
	public int movieFindCount(String column,String fd)
	{
		int count=0;
		try
		{
			getConnection();
			String sql="SELECT COUNT(*) "
					+"FROM movie "
					+"WHERE "+column+" LIKE '%'||?||'%'";
			ps=conn.prepareStatement(sql);
			ps.setString(1, fd);
			ResultSet rs=ps.executeQuery();
			rs.next(); // 1개만 가져올 거라 while 쓰면 안된다 
			count=rs.getInt(1);
			rs.close();
			// 0 => 검색 결과가 없는 상태 , 0이상이면 검색 결과 O
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		return count;
	}
}
