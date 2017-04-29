package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import commons.info.InfoIO;
import meta.working.ConvertableToJSON;
import meta.working.FileDTO;
import meta.working.InfoLayoutListDTO;
import meta.working.MapInfoDTO;

public class InfoFileOps implements InfoQueuebale {
	

	private enum SAVE{ YES , NO,DELETE};
	private ConcurrentHashMap<Integer, FileDTO<Integer,MapInfoDTO>> infoMap; 
	private List<InfoInList> infoForList;
	private FileDTO<Integer,ConvertableToJSON> info;
	private SAVE save=SAVE.NO;
	private InfoManager infoManager;

	//TODO Check Why is not accepting Override Annotation???
	public void run() {
		if(save.equals(SAVE.YES)){
			save=SAVE.NO;
			saveInfo();
		}
		if(save.equals(SAVE.DELETE)){
			save=SAVE.NO;
			deleteInfo();
		}
		updateAllInfoFromFiles();
		
	}

	//TODO Do this method more granular, it is inefficient but it works ok.
	private void updateAllInfoFromFiles() {
		//Get all question from direcotry
		Map<Integer,FileDTO<Integer,MapInfoDTO>> infoMap = new HashMap();
		try {
			infoMap = InfoIO.readAllInfoFiles(this.infoManager.getInfoPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		this.infoMap.putAll(infoMap);
		this.infoForList.clear();
		infoMap.forEach((k,v)->{ 
			InfoInList infoListItem = new InfoInList();
			infoListItem.setId(v.getId());
			infoListItem.setTitle(v.getContend().getTitle());
			this.infoForList.add(infoListItem);
		});
		  infoManager.updateUI();
	}

	private void saveInfo() {
		try {
			InfoIO.createFile(this.info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteInfo() {
		try {
			InfoIO.deleteFile(this.info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
	public void setInfoFromList(List<InfoInList> infoForList) {
		this.infoForList=infoForList;
		
	}

	public void setInfoManager(InfoManager infoManager) {
		this.infoManager=infoManager;
	}

	public void setInfoMap(ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> infoMap) {
		this.infoMap=infoMap;
	}

	public void updateInfoToFile(FileDTO<Integer, ConvertableToJSON> fileDTO) {
		save=save.YES;
		this.info=fileDTO;
		
	}
	 
	
	//TODO: Add to Ops Interface
	public void setFileToDelete(FileDTO<Integer, ConvertableToJSON> fileDTO) {
		save=SAVE.DELETE;
		this.info=fileDTO;
	}
}