package com.sist.main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sist.dao.loseVO;

public class LoseMain {

	public static void main(String[] args) {
		 // TODO Auto-generated method stub
	      try {
	         Document doc=Jsoup.connect("https://www.zooseyo.or.kr/zooseyo_or_kr.html?").get();
	         Elements link=doc.select("td td table b a");
	         
	         for(int i=0;i<link.size();i++) {
//	            System.out.println("https://www.zooseyo.or.kr/"+link.get(i).attr("href"));
	            loseVO vo=new loseVO();
//	            vo.setPoster(link.get(i).attr("src"));
	            String subLink="https://www.zooseyo.or.kr/"+link.get(i).attr("href"); // 영화 클릭하면 나오는 URL주소
	            Document doc2=Jsoup.connect(subLink).get(); // 상세보기 
	            
	           
   
	            
	            // 사진 (poster) Done
	            Element poster=doc2.selectFirst("table table table table table img");
	            String posterData="https://www.zooseyo.or.kr/"+poster.attr("src");
//	            System.out.println(posterData);
//	            System.out.println(poster);
	            
	            // 잃어버린 장소(loc) Done
	            Element loc=doc2.selectFirst("table table table table table table table b");
	            String locData=loc.text();
//	            System.out.println(locData);
	            
	            // 잃어버린 날짜(date)
//	            Element date=doc2.selectFirst("table table table table table table");
//	            System.out.println(date);
	            Element date=doc2.selectFirst("table table");
	            String strdate =date.text();
//	            System.out.println(strdate);
	            String strdate2=strdate.substring(strdate.indexOf("202"));
//	            System.out.println(strdate2);

	            String strdate3 = strdate2.substring(0,10);
	            System.out.println(strdate3); // 날짜 짤랐따 
	            
	            // 제목 (title) Done
	            Element title=doc2.selectFirst("head title");
	            String titleText=title.text();
//	            System.out.println(titleText);
	            String titleData=titleText.substring(10,titleText.length()-19);
//	            System.out.println(titleData);
	            
	            // 내용 (content) Done
//	            Element content=doc2.selectFirst("table table table table table table tbody");
//	            System.out.println(content);
	            // retry
	            Element content=doc2.selectFirst("table table");
	            String str =content.text();
	            String str2 = str.substring(str.indexOf(")")+1);
//	            System.out.println(str2);
//	            System.out.println(content.text());	            
//	            String test=content.text();
//	            System.out.println(test);
	            
	         }
	      
	      }catch(Exception ex) {}
	      
	   }


	}
