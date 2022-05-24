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

public class BusinessController {//����APi�� �����ؼ� ����ڵ�Ϲ�ȣ�� Ÿ�缺�� �����ϴ� Ŭ����
	public String checkbusiness(String businessNo, String businessName){//����API����APi�� �����ؼ� ����ڵ�Ϲ�ȣ�� Ÿ�缺�� �����ϴ� �޼���
		// TODO Auto-generated method stub
		String num1 = null;//��¥����ڵ�Ϲ�ȣ��� �������� ���ڿ� ����
		try {
			String url = "http://apis.data.go.kr/B552015/NpsBplcInfoInqireService/getBassInfoSearch?serviceKey=6f2KhHBNmy2ArHk62abmcUFSbHQOT%2F6ccfrPA0D1hnm65wmDwr2QiuVTz31FfsXr%2FNFTSdz3%2F%2BCt%2B7OryQjeVQ%3D%3D&numOfRows=1";
			//����API�� �ּҿ� ����Ű
			url += "&wkpl_nm=";//�ּҿ� ������̸��� �߰�
			url += URLEncoder.encode(businessName,"utf-8");//�ּҿ� ������̸��� �߰�
			url += "&bzowr_rgst_no=";//����ڵ�Ϲ�ȣ �߰�
			url += businessNo.substring(0, 6);//����ڵ�Ϲ�ȣ�� �� 6�ڸ� ����
			URL obj = new URL(url);//url ����
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();//Ŀ�ؼǻ���
		    con.setRequestMethod("GET");//��û��get���� ����
		    con.setRequestProperty("User-Agent", "Chrome/version");//��û����
		    con.setRequestProperty("Accept-Charset", "UTF-8");//��û����
		    con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");//��û����
		    int responseCode = con.getResponseCode();//�������
		    BufferedReader in = new BufferedReader(
		    		new InputStreamReader(con.getInputStream(),"utf-8"));//��û���� ���۽�Ʈ��������
		    String inputLine;
		    StringBuffer response = new StringBuffer();
		    while ((inputLine = in.readLine()) != null) {
		        response.append(inputLine);//���پ� ������� ����
		    }
		    in.close();//���۸��� ����
		    String source = response.toString();//�޾ƿ� ����� ���ڿ��� ����
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//�����������丮����
		    DocumentBuilder builder = factory.newDocumentBuilder();//��������
		    Document xml =  builder.parse(new InputSource(new StringReader(source)));//xml������ ����
		    xml.getDocumentElement().normalize();
		    XPath xpath = XPathFactory.newInstance().newXPath();
		    Node num = (Node)xpath.evaluate("//body/totalCount", xml, XPathConstants.NODE);//������� ���� ����
		    num1 = num.getTextContent();//������� ���� ����
		    if(num1.equals("0")) {//������� �������
		    	return null;//null�� ������(����!)
		    }
		    num = (Node)xpath.evaluate("//item/wkplNm", xml, XPathConstants.NODE);//������� ������� ����ڵ�Ϲ�ȣ�� ����
		    num1 = num.getTextContent();//������ ����ڵ�Ϲ�ȣ�� ����
			}catch(Exception e) {
				e.printStackTrace();
			}
		return num1;//����ڵ�Ϲ�ȣ�� ����(����!)
		}
	}
