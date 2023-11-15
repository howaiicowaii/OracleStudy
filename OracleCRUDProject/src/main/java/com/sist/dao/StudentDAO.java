package com.sist.dao;
import java.util.*;
import java.sql.*;
public class StudentDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	public StudentDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {}
	}
	public void getConnection()
	{
		try
		{
			conn=DriverManager.getConnection(URL,"hr","happy");
		}catch(Exception ex) {}
	}
	public void disConnection()
	{
		try
		{
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		}catch(Exception ex) {}
	}
	// 기능 
	// 1. 목록 => SELECT => 페이징 (자바 , 오라클) 어디서 나눌지 
//                               -> 오라클이 빠를 것 => 인라인뷰
//	   => 오라클만 연동 => 출력은 여기서 X => main,브라우저로 전송
//	   => 데이터를 모아서 전송 (목록) ArrayList
	public List<StudentVO> stdListData()
	{
		List<StudentVO> list=new ArrayList<StudentVO>();
		try
		{
			// 1. 오라클 연결 
			getConnection();
			// 2. SQL 문장 만든다 (오라클로 전송할 목적)
			String sql="SELECT hakbun,name,kor,eng,math,"
					+ "kor+eng+math,ROUND((kor+eng+math)/3.0,2),"
					+ "RANK() OVER(ORDER BY(kor+eng+math) DESC),"
					+ "TO_CHAR(regdate,'YYYY-MM-DD HH24:MI:SS') "
					+ "FROM student";
			// RANK()는 자동 정렬 
			// => 웹은 모든 데이터가 공유 => 데이터는 반드시 오라클에 저장된 상태
			
			// => VueJS (VueX) / ReactJS (Redux)
			//      JSP           JSP    ------- Spring
//			뒤에 x 붙은 것들이 MVC 구조 여서 요즘 많이 쓰는 추세 
			// =====> 데이터가 없는 상태에선 X (데이터 관리 프로그램)
			// Back-End (SQL) , Front-End (JSON/JSONP)
			// VO => {} , List => [] => [{},{},{}...]
			// XML => JSON ==> Ajax
			
			// 3. 오라클로 SQL 전송
			ps=conn.prepareStatement(sql);
			// 4. SQL 문장을 실행. 실행된 결과를 rs 에 채워준다 
			ResultSet rs=ps.executeQuery();
			while(rs.next()) // 맨위부터 한줄씩 데이터 읽기 
			{
				// 데이터를 모아서 전송 => MainClass
				// 여러개 이기 때문에 ArrayList 사용 
				StudentVO vo=new StudentVO();
				vo.setHakbun(rs.getInt(1));
				// => 인덱스 번호 1 부터 시작 
				vo.setName(rs.getString(2));
				vo.setKor(rs.getInt(3));
				vo.setEng(rs.getInt(4));
				vo.setMath(rs.getInt(5));
				vo.setTotal(rs.getInt(6));
				vo.setAvg(rs.getDouble(7));
				vo.setRank(rs.getInt(8));
				vo.setDbday(rs.getString(9)); // regdate 를 TO_CHAR 로 변환한 dbday 받아온다
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
	// 2. 추가 => INSERT => COMMIT (AutoCommit -> 자바)
	// INSERT UPDATE DELETE => void => 오라클에서만 실행해라 
	public void stdInsert(StudentVO vo) // StudentVO 에서 학생들의 데이터 변수 매개변수로 받기
	{
		try
		{
			getConnection();
//			String sql="INSERT INTO student(hakbun,name,kor,eng,math) "
//					+ "VALUES((SELECT MAX(hakbun)+1 FROM student),'"
//					+vo.getName()+"',"+vo.getKor()+","+vo.getEng()
//					+","+vo.getMath()+")";
//			System.out.println(sql); 
			String sql="INSERT INTO student(hakbun,name,kor,eng,math) "
					+ "VALUES((SELECT MAX(hakbun)+1 FROM student),?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setInt(2, vo.getKor());
			ps.setInt(3, vo.getEng());
			ps.setInt(4, vo.getMath());
			// 데이터형 처리 
			// setString ==> ' '
			// 실행 
			ps.executeUpdate(); // 데이터베이스가 변동 => 
			// executeQuery 에는 COMMIT 이 없지만 
			// executeUpdate 에는 COMMIT 이 포함되어 있다 (AutoCommit)
			
		}catch(Exception ex)
		{
			ex.printStackTrace(); 
		}
		finally
		{
			disConnection();
		}
	}
	// 3. 상세보기 => SELECT WHERE -> pk (hakbun 매개변수로 받아온다)
//	      ------ 반드시 PRIMARY KEY 값을 전송해줘야한다
	public StudentVO stdDetailData(int hakbun)
	{
		StudentVO vo=new StudentVO();
		try
		{
			getConnection();
			String sql="SELECT hakbun,name,kor,eng,math,"
					+ "kor+eng+math,ROUND((kor+eng+math)/3.0,2),"
					+ "RANK() OVER(ORDER BY(kor+eng+math) DESC),"
					+ "TO_CHAR(regdate,'YYYY-MM-DD HH24:MI:SS') "
					+ "FROM student"
					+ "WHERE hakbun="+hakbun;
			ps=conn.prepareStatement(sql);
			// 4. SQL 문장을 실행. 실행된 결과를 rs 에 채워준다 
			ResultSet rs=ps.executeQuery();
			
			rs.next();
			
			vo.setHakbun(rs.getInt(1));
			// => 인덱스 번호 1 부터 시작 
			vo.setName(rs.getString(2));
			vo.setKor(rs.getInt(3));
			vo.setEng(rs.getInt(4));
			vo.setMath(rs.getInt(5));
			vo.setTotal(rs.getInt(6));
			vo.setAvg(rs.getDouble(7));
			vo.setRank(rs.getInt(8));
			vo.setDbday(rs.getString(9));
			
			rs.close();
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		return vo;
	}
	// 4. 수정 => UPDATE => COMMIT
	public StudentVO stdUpdateData(int hakbun) // 수정 전 데이터 받기
	{
		StudentVO vo=new StudentVO();
		try
		{
			getConnection();
			String sql="SELECT hakbun,name,kor,eng,math "
					+ "FROM student "
					+ "WHERE hakbun="+hakbun;
			// 대댓글 / 답변형 
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery(); // SQL 문장 실행결과를 rs 에 넣고 SQL 문장 실행 
			rs.next(); // 실행 후 커서를 rs의 다음 줄의 맨앞줄에 갖다 놓는다
			vo.setHakbun(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setKor(rs.getInt(3));
			vo.setEng(rs.getInt(4));
			vo.setMath(rs.getInt(5));
			
			rs.close();
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		return vo;
	}
	public void stdUpdate(StudentVO vo) // 여기서 실제 수정 수행 
	{
		try
		{
			getConnection();
			String sql="UPDATE student SET "
					+ "name=?,kor=?,eng=?,math=? "
					+ "WHERE hakbun=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setInt(2, vo.getKor());
			ps.setInt(3, vo.getEng());
			ps.setInt(4, vo.getMath());
			ps.setInt(5, vo.getHakbun());
			
			ps.executeUpdate();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
	}
	// 5. 삭제 => DELETE => COMMIT 
	public void stdDelete(int hakbun)
	{
		try
		{
			getConnection();
			String sql="DELETE FROM student "
					+ "WHERE hakbun="+hakbun;
			ps=conn.prepareStatement(sql);
			ps.executeUpdate();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
	}
//	public static void main(String[] args) {
//		StudentDAO dao=new StudentDAO();
//		StudentVO vo=new StudentVO();
//		vo.setHakbun(8);
//		vo.setName("이산");
//		vo.setKor(90);
//		vo.setEng(60);
//		vo.setMath(80);
//		dao.stdInsert(vo);
//	}
	
}
