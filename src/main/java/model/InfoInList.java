package model;


public class InfoInList {

	private Integer id;
	private String title;
	
	public void setId(Integer integer) {
		this.id=integer;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setTitle(String title){
		this.title=title;
	}

	@Override
	public String toString() {
		return id+" "+title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InfoInList))
			return false;
		InfoInList other = (InfoInList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	
	
}