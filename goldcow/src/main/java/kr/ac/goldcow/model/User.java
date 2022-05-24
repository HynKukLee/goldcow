package kr.ac.goldcow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="NO")
	private long no;
	private String email;
	private String password;
	private String name;
	private String address;
	private String phone;
	@Column(name="ISBUSINESS")
	private boolean isBusiness;
	@Column(name="BUSINESSNO")
	private String businessNo;
	private boolean isactivity;
	private long money;
	public void setMoney(long money) {
		this.money = money;
	}
	public long getMoney() {
		return money;
	}
	public long getNo() {
		return no;
	}
	public void setNo(long no) {
		this.no = no;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isBusiness() {
		return isBusiness;
	}
	public void setBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public boolean isIsactivity() {
		return isactivity;
	}
	public void setIsactivity(boolean isactivity) {
		this.isactivity = isactivity;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("no : %s, 이름 : %s, 주소 : %s, 이메일 : %s", no, name, address, email);
	}
}
