package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import commons.flashcards.FlashCardIO;
import commons.info.InfoIO;
import meta.flashcardsdto.MultipleChoiceFlashCardDTO;
import meta.working.ConvertableToJSON;
import meta.working.FileDTO;
import meta.working.InfoDTO;
import meta.working.MapInfoDTO;

public class InfoFileOps implements Runnable {
	

	private enum SAVE{ YES , NO,DELETE};
	private ConcurrentHashMap<Integer, FileDTO<Integer,MapInfoDTO>> infoMap; 
	private List<InfoInList> infoForList;
	private FileDTO<Integer,ConvertableToJSON> infoToSave;
	private SAVE save=SAVE.NO;
	private InfoManager infoManager;
	private int deleteId;
 
	//TODO Check Why is not accepting Override Annotation???
	public void run() {
		if(save.equals(SAVE.YES)){
			save=SAVE.NO;
			saveInfo();
		}
		if(save.equals(SAVE.DELETE)){
			save=SAVE.NO;
			deleteInfo(deleteId);
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
			InfoIO.createFile(this.infoToSave);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteInfo(int deleteId2) {
		// TODO Auto-generated method stub
		
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
		this.infoToSave=fileDTO;
		
	}
	 
	
	//TODO: Add to Ops Interface
	public void setIdToDelete(Integer id) {
		save=SAVE.DELETE;
		this.deleteId=id;
	}	
}