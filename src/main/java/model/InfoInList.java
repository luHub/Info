package model;


public class InfoInList {

	private Integer id;
	private String title;
	
	public void setId(Integer integer) {
		this.id=integer;
	}
	
	public void setTitle(String title){
		this.title=title;
	}

	@Override
	public String toString() {
		return id+" "+title;
	}
}