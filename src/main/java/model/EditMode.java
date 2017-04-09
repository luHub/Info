package model;

import java.util.HashMap;

import info.InfoPanelController;
import info.InfoTextAreaController;
import info.Loader;
import meta.working.FileDTO;
import meta.working.InfoDTO;
import meta.working.MapInfoDTO;

public class EditMode { 
	
	private InfoManager infoManager;
	
	public EditMode(InfoManager infoManager) {
		this.infoManager = infoManager;
	}

	public void setCurrentInfo(FileDTO<Integer, MapInfoDTO> currentInfoFile) {
		//1. Clear Info Pane
		clearInfoPane();
		//2. Display Information File
		addInfoToPane(currentInfoFile.getContend());
	}
	
	private void clearInfoPane(){
		this.infoManager.getInfoPanelController().getDisplayVBox().getChildren().clear();
	}
	
	private void addInfoToPane(MapInfoDTO mapInfoDTO){
		mapInfoDTO.getMap().entrySet().forEach(es->{
			addSection(es.getValue());
		});
	}
	
	private void addSection(InfoDTO infoDTO){
		switch(infoDTO.getType()){
		case TEXT:
			loadTextPane(infoDTO);
			break;
		case IMG:
			//Add Img:
			break;
		case WEB:
			//Add Web:			
			break;
		}
	}

	private void loadTextPane(final InfoDTO infoDTO) {
		Loader loader = new Loader();
		InfoTextAreaController itac = loader.addTextArea(this.infoManager.getInfoPanelController().getDisplayVBox(),infoManager);
		itac.setText(infoDTO.getText());
	}		
}



