package kr.ac.goldcow.model;




import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name = "PROJECT")
public class Project {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="NO")
private long no;
@Column(name="USER_NO")
private long userNo;
private String name;
private String contents;
@Column(name="IMAGENAME")
private String imageName;
private long amount;
private long state;
private long category;
private long nowamount;
private Timestamp expiredate;
@OneToMany(cascade=CascadeType.ALL, mappedBy="project",fetch = FetchType.EAGER)
private List<Comment> comment;

public long getNowamount() {
	return nowamount;
}



public void setNowamount(long nowamount) {
	this.nowamount = nowamount;
}



public Timestamp getExpiredate() {
	return expiredate;
}



public void setExpiredate(Timestamp expiredate) {
	this.expiredate = expiredate;
}



public long getCategory() {
	return category;
}



public void setCategory(long category) {
	this.category = category;
}





public List<Comment> getComment() {
	return comment;
}



public void setComment(List<Comment> comment) {
	this.comment = comment;
}



public long getNo() {
	return no;
}




public String getName() {
	return name;
}



public String getContents() {
	return contents;
}



public String getImageName() {
	return imageName;
}



public long getAmount() {
	return amount;
}



public long getState() {
	return state;
}



public void setNo(long no) {
	this.no = no;
}




public void setName(String name) {
	this.name = name;
}



public void setContents(String contents) {
	this.contents = contents;
}



public void setImageName(String imageName) {
	this.imageName = imageName;
}



public void setAmount(long amount) {
	this.amount = amount;
}



public void setState(long state) {
	this.state = state;
}



public void buildEntity(Project project) {
	this.setNo(project.getNo());
	this.setName(project.getName());
	this.setContents(project.getContents());
	this.setImageName(project.getImageName());
	this.setAmount(project.getAmount());
	this.setState(project.getState());
}



public long getUserNo() {
	return userNo;
}



public void setUserNo(long userNo) {
	this.userNo = userNo;
}
}
