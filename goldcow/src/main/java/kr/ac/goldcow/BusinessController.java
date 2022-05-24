package kr.ac.goldcow;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class BusinessController {//공공APi에 접근해서 사업자등록번호의 타당성을 검증하는 클래스
	public String checkbusiness(String businessNo, String businessName){//공공API공공APi에 접근해서 사업자등록번호의 타당성을 검증하는 메서드
		// TODO Auto-generated method stub
		String num1 = null;//진짜사업자등록번호라면 리턴해줄 문자열 변수
		try {
			String url = "http://apis.data.go.kr/B552015/NpsBplcInfoInqireService/getBassInfoSearch?serviceKey=6f2KhHBNmy2ArHk62abmcUFSbHQOT%2F6ccfrPA0D1hnm65wmDwr2QiuVTz31FfsXr%2FNFTSdz3%2F%2BCt%2B7OryQjeVQ%3D%3D&numOfRows=1";
			//공공API의 주소와 인증키
			url += "&wkpl_nm=";//주소에 사업자이름을 추가
			url += URLEncoder.encode(businessName,"utf-8");//주소에 사업자이름을 추가
			url += "&bzowr_rgst_no=";//사업자등록번호 추가
			url += businessNo.substring(0, 6);//사업자등록번호의 앞 6자리 추출
			URL obj = new URL(url);//url 생성
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();//커넥션생성
		    con.setRequestMethod("GET");//요청을get으로 설정
		    con.setRequestProperty("User-Agent", "Chrome/version");//요청설정
		    con.setRequestProperty("Accept-Charset", "UTF-8");//요청설정
		    con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");//요청설정
		    int responseCode = con.getResponseCode();//결과저장
		    BufferedReader in = new BufferedReader(
		    		new InputStreamReader(con.getInputStream(),"utf-8"));//요청에서 버퍼스트림가져옴
		    String inputLine;
		    StringBuffer response = new StringBuffer();
		    while ((inputLine = in.readLine()) != null) {
		        response.append(inputLine);//한줄씩 결과값을 저장
		    }
		    in.close();//버퍼리더 닫음
		    String source = response.toString();//받아온 결과를 문자열로 저장
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//문서빌더팩토리생성
		    DocumentBuilder builder = factory.newDocumentBuilder();//빌더생성
		    Document xml =  builder.parse(new InputSource(new StringReader(source)));//xml문서로 만듦
		    xml.getDocumentElement().normalize();
		    XPath xpath = XPathFactory.newInstance().newXPath();
		    Node num = (Node)xpath.evaluate("//body/totalCount", xml, XPathConstants.NODE);//결과값의 수를 얻음
		    num1 = num.getTextContent();//결과값의 수를 저장
		    if(num1.equals("0")) {//결과값이 없을경우
		    	return null;//null을 리턴함(실패!)
		    }
		    num = (Node)xpath.evaluate("//item/wkplNm", xml, XPathConstants.NODE);//결과값이 있을경우 사업자등록번호를 추출
		    num1 = num.getTextContent();//추출한 사업자등록번호를 저장
			}catch(Exception e) {
				e.printStackTrace();
			}
		return num1;//사업자등록번호를 리턴(성공!)
		}
	}
