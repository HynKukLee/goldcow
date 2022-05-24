package kr.ac.goldcow.model;

import java.sql.Timestamp ;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SUPPORT")
public class Support {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="NO")
	private long no;
	@Column(name="USER_NO")
	private long userNo;
	@Column(name="PROJECT_NO")
	private long projectno;
	private long amount;
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name="SUPPORTDATE")
	private Timestamp supportDate;
	@Column(name="ISCANCELED")
	private boolean isCanceled;
	@Column(name="REWARDLIST")
	private String rewardList;
	@ManyToOne(cascade= {CascadeType.ALL})
	@JoinColumn(name="USER_NO", insertable=false, updatable=false)
	private User user;
	@ManyToOne(cascade= {CascadeType.ALL})
	@JoinColumn(name="PROJECT_NO", insertable=false, updatable=false)
	private Project project;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public String getRewardList() {
		return rewardList;
	}
	public void setRewardList(String rewardList) {
		this.rewardList = rewardList;
	}
	public void setNo(long no) {
		this.no = no;
	}


	public void setAmount(long amount) {
		this.amount = amount;
	}

	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}
	public long getNo() {
		return no;
	}


	public long getUserNo() {
		return userNo;
	}
	public void setUserNo(long userNo) {
		this.userNo = userNo;
	}
	public long getProjectno() {
		return projectno;
	}
	public void setProjectno(long projectno) {
		this.projectno = projectno;
	}
	public long getAmount() {
		return amount;
	}

	public Timestamp getSupportDate() {
		return supportDate;
	}
	public void setSupportDate(Timestamp supportDate) {
		this.supportDate = supportDate;
	}
	public boolean isCanceled() {
		return isCanceled;
	}
}
