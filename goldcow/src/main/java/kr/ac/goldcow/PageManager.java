package kr.ac.goldcow;

public class PageManager {
	private int entity;
	private int entityInOnePage;
	private int page;
	private int pageInOneTab;
	private int pageEntity;
	private int tab;
	private int startPage;
	private int endPage;
	private int startNum;
	private int endNum;
	public PageManager(int entity, int entityInOnePage, int page, int pageInOneTab) {
		this.entity = entity;
		this.entityInOnePage = entityInOnePage;
		this.page = page;
		this.pageInOneTab = pageInOneTab;
	}
	public void processPage(){
		pageEntity = entity/entityInOnePage;
		if(entity%entityInOnePage>0) {
			pageEntity++;
		}
		if(page%pageInOneTab == 0) {
			tab = page / pageInOneTab;
		}else {
			tab = (page / pageInOneTab)+1;
		}
		startPage = pageInOneTab*(tab-1)+1;
		endPage = startPage + pageInOneTab-1;
		if(endPage > pageEntity) {
			endPage = pageEntity;
		}
		startNum = entityInOnePage*(page - 1);
		endNum = startNum + entityInOnePage - 1;
		if(endNum > entity-1) {
			endNum = entity - 1;
		}
	}
	//테스트용 메서드
	public void print() {
		System.out.println("entity="+entity+"entityInOnePage"+entityInOnePage+"page="+page+"pageInOneTab"+pageInOneTab+"pageEntity"+pageEntity+"sp"+startPage+"ep"+endPage+"start"+startNum+"end"+endNum);
	}
	public int getEntity() {
		return entity;
	}
	public void setEntity(int entity) {
		this.entity = entity;
	}
	public int getEntityInOnePage() {
		return entityInOnePage;
	}
	public void setEntityInOnePage(int entityInOnePage) {
		this.entityInOnePage = entityInOnePage;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageInOneTab() {
		return pageInOneTab;
	}
	public void setPageInOneTab(int pageInOneTab) {
		this.pageInOneTab = pageInOneTab;
	}
	public int getPageEntity() {
		return pageEntity;
	}
	public void setPageEntity(int pageEntity) {
		this.pageEntity = pageEntity;
	}
	public int getTab() {
		return tab;
	}
	public void setTab(int tab) {
		this.tab = tab;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getStartNum() {
		return startNum;
	}
	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}
	public int getEndNum() {
		return endNum;
	}
	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}
}

