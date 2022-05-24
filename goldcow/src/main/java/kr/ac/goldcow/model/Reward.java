package kr.ac.goldcow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REWARD")
public class Reward {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="NO")
	private long no;
	@Column(name="PROJECT_NO")
	private long projectNo;
	private String name;
	private String content;
	private long price;
	public void setProjectNo(long projectNo) {
		this.projectNo = projectNo;
	}

	public long getProjectNo() {
		return projectNo;
	}
	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}




	public void setName(String name) {
		this.name = name;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public String getContent() {
		return content;
	}
	public long getPrice() {
		return price;
	}
}
