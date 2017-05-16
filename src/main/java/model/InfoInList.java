package model;

import info.InfoCellController;
import info.Loader;
import javafx.scene.Node;
import meta.working.InfoIndexDTO;

public class InfoInList {
	
	private InfoCellController infoCell;
	private InfoIndexDTO infoIndexDTO;
	private Loader loader = new Loader();
	
	private InfoManager infoManager;
	
	public InfoInList(final InfoIndexDTO infoIndexDTO){
		this.infoIndexDTO=infoIndexDTO;
		this.infoCell = loader.addInfoCell();
	}
	
	public int getId(){
		return this.infoIndexDTO.getInfoFileId();
	}
	
	public InfoIndexDTO getInfoIndexDTO(){
		return this.infoIndexDTO;
	}
	
	public void setTitle(String title){
		this.infoIndexDTO.setTitle(title);
	}

	@Override
	public String toString() {
		return this.infoIndexDTO.getInfoFileId()+" "+this.infoIndexDTO.getTitle();
	}

	public InfoCellController getInfoCellController(){
		return this.infoCell;
	}
	

	public Node getInfoNode() {
		
		this.infoCell.initialize(infoManager,infoIndexDTO,this);
		return this.infoCell.getInfoCellView();
	}

	public void setInfoManager(InfoManager infoManager) {
		this.infoManager=infoManager;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((infoCell == null) ? 0 : infoCell.hashCode());
		result = prime * result + ((infoIndexDTO == null) ? 0 : infoIndexDTO.hashCode());
		result = prime * result + ((infoManager == null) ? 0 : infoManager.hashCode());
		result = prime * result + ((loader == null) ? 0 : loader.hashCode());
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
		if (infoCell == null) {
			if (other.infoCell != null)
				return false;
		} else if (!infoCell.equals(other.infoCell))
			return false;
		if (infoIndexDTO == null) {
			if (other.infoIndexDTO != null)
				return false;
		} else if (!infoIndexDTO.equals(other.infoIndexDTO))
			return false;
		if (infoManager == null) {
			if (other.infoManager != null)
				return false;
		} else if (!infoManager.equals(other.infoManager))
			return false;
		if (loader == null) {
			if (other.loader != null)
				return false;
		} else if (!loader.equals(other.loader))
			return false;
		return true;
	}
}