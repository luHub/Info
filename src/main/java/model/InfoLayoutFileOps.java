package model;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import commons.info.InfoIO;
import meta.working.FileDTO;
import meta.working.InfoLayoutListDTO;


//This will be called on selection of each INFO DTO
public class InfoLayoutFileOps implements InfoQueuebale {
	
	private enum SAVE{ YES , NO,DELETE};
	private FileDTO<Integer,InfoLayoutListDTO> layoutFile; 
	private List<InfoInList> infoForList;
	private SAVE save=SAVE.NO;
	private InfoManager infoManager;
	
	@Override
	public void run() {
		//Updated the layout of Current INFO File
		//Manager->updateLayout->UpdateLayoutDTO (Controller only update from file
		//on start-up and writes to file to during programm execution)
		if(save.equals(SAVE.YES)){
			save=save.NO;
			//Save Layout
			save();
		}
		
			updateAllInfoFromFiles();
	
	}

	private void updateAllInfoFromFiles()  {
		//Get all from direcotry
		try {
			InfoLayoutListDTO updatedLayoutDTO;
			this.layoutFile.setId(1);
			this.layoutFile.setPath(this.infoManager.getLayoutConfigPath());
			if (Files.exists(this.infoManager.getLayoutConfigPath())){
			updatedLayoutDTO = InfoIO.readFile(this.infoManager.getLayoutConfigPath(),InfoLayoutListDTO.class);
			//layoutFile Reference is Updated
			this.layoutFile.setContend(updatedLayoutDTO);
			}
			else
			{
				this.layoutFile.setContend(new InfoLayoutListDTO());
				InfoIO.createFile(this.layoutFile);
			}
				
			//Fix: IOException, JsonParseException, JsonMappingException
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void setInfoManager(InfoManager infoManager) {
		this.infoManager=infoManager;
	}

	public void setLayoutFile(FileDTO<Integer, InfoLayoutListDTO> layoutFile) {
		this.layoutFile=layoutFile;
	}
	
	
	public void updateInfoToFile(FileDTO<Integer, InfoLayoutListDTO> layoutFile) {
		save=save.YES;
		this.layoutFile=layoutFile;
	}

	
	private void save(){
		try {
			InfoIO.deleteFile(this.layoutFile);
			InfoIO.createFile(this.layoutFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}