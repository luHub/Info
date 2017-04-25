package model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.OptionalInt;

import commons.WorkbookIO;
import info.InfoPanelController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import meta.working.ConvertableToJSON;
import meta.working.FileDTO;
import meta.working.INFO_TYPE;
import meta.working.InfoDTO;
import meta.working.MapInfoDTO;
import user.User;

public class InfoManager {
	
	//TODO: Wire this to Workbook, IOC, or something like that.
	private User user;
	private boolean isModule=true;
	private MODE mode;	
	private final  EditMode editMode = new EditMode(this);
	private ReadMode readMode;
	private ListView<InfoInList> infoListView;
	private final String INFO = "info";
	
	
	private InfoPanelController infoPanelController;
	
	
	public InfoManager(InfoPanelController infoPanelController){
		this.infoPanelController = infoPanelController;
		initUser();
		initializeInfoService();
	}
	
	public InfoManager(){ 
		initUser();
		initializeInfoService();
	}
	
	public void setInfoListView(ListView<InfoInList> infoListView) {
		this.infoListView=infoListView;
	}
	
	private ChangeListener<InfoInList> managerListener = new ChangeListener<InfoInList>() {
		public void changed(ObservableValue<? extends InfoInList> observable, InfoInList oldValue,
				InfoInList newValue) {
			//Switch List views
			//1. Get Selected Info 
			//TODO Check this ugly cast!
			FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap().get(newValue.getId());
			//2. Display it on the Info Pane
			//TODO Add EDIT and READ MODE
			InfoManager.this.editMode.setCurrentInfo(currentInfoFile);
			
			 
			// Set StudyModeManagerView
//			if(newValue!=null){
//			Integer questionId = newValue.getId();
//			MultipleChoiceFlashCardDTO currentQuestion = (MultipleChoiceFlashCardDTO) questionService.getQuestionMap()
//					.get(questionId);
//			QuestionManager.this.currentQuestion = currentQuestion;
//			if (mode.equals(MODE.STUDY)) {
//				QuestionManager.this.studyModeManager.setCurrentQuestion(currentQuestion);
//			} else if (mode.equals(MODE.CREATE)) {
//				QuestionManager.this.creatorModeManager.setCurrentQuesion();
//			}
//		}
			}
	};
	
	/**
	 * User config depends if this Component is working isolated or inside a workbook
	 */
	private void initUser(){
		if(isModule){
			setUser(new User());
			getUser().setUserName("user");
			getUser().setCurrentWorkbook("workbook");
			try {
				WorkbookIO.createWorkbookIfNotExists("user", "workbook");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//TODO: Reference User Object from Workbook. This is a dangerous part so be careful with references and stuff
			//@UserFromWorkBook (We could create this nice annotation and a custom object container)
			//The idea is be able to use "new" and not put all our app inside a not Java Core APIs.
		}
	}
	
	//Creates a concurrent Map and be careful
	private InfoService infoService = new InfoService();
	private Integer lastInfoIdSelected;
	private InfoInList lastInfoInListSelected;
	
	
	//TODO Write Test for this method!
	public void createNewInfoFile(){ 
		//1 Create a new Info File
		String ext ="json";
		//2 Get File Last Max ID and add 1
		 List<InfoInList> iif = infoService.getInfoForList();
		 OptionalInt maxId = iif.stream().mapToInt(w->w.getId()).max();
		 int fileId = 0;
		 if(maxId.isPresent()){
			 fileId = maxId.getAsInt()+1;
		 }
		//3 Config File
		final Path INFO_PATH = Paths.get(getUser().path().toString(),"info"); 
		MapInfoDTO mapInfoDTO = new MapInfoDTO();
		InfoDTO infoDTO = new InfoDTO();
		
		infoDTO.setText("Insert Text Here");
		infoDTO.setType(INFO_TYPE.TEXT);
		mapInfoDTO.getMap().put(0, infoDTO);
		mapInfoDTO.setTitle("INFO");  
		FileDTO<Integer,ConvertableToJSON> fileDTO = new FileDTO(fileId, INFO_PATH, ext);
		fileDTO.setContend(mapInfoDTO);
		infoService.addInfoFileToSave(fileDTO); 
	}
	 
	private void initializeInfoService() {
		this.infoService.setInfoManager(this);
		this.infoService.start();
		this.infoService.getInfoFromFiles();
	}
	

	public void updateUI() {
		Platform.runLater(() -> {			
			infoListView.getSelectionModel().selectedItemProperty().removeListener(managerListener);
			List<InfoInList> info = infoService.getInfoForList();
			ObservableList<InfoInList> ol = FXCollections.observableList(info);
			infoListView.setItems(ol); 
			infoListView.getSelectionModel().selectedItemProperty().addListener(managerListener);
//			// TODO not use this null comparison
//			if (currentQuestion == null) {
//				questionListView.getSelectionModel().selectFirst();
//			} else if (questionStatus.equals(QUESTION_STATUS.OLD)) {
//				QuestionInList qil = new QuestionInList();
//				qil.setId(String.valueOf(currentQuestion.getId()));
//				qil.setType(currentQuestion.getQuestion().getType());
//				questionListView.getSelectionModel().select(qil);
//			} else if (questionStatus.equals(QUESTION_STATUS.NEW)) {
//				questionStatus = QUESTION_STATUS.OLD;
//				questionListView.getSelectionModel().selectLast();
//			}else if(questionStatus.equals(QUESTION_STATUS.DELETED)){
//				questionListView.getSelectionModel().selectLast();
//				questionStatus=QUESTION_STATUS.OLD;
//			}
		});
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Path getInfoPath() {
		return Paths.get(user.path().toString(),"info");
	}

	public InfoPanelController getInfoPanelController() {
		return infoPanelController;
	}

	public void setReadMode() {
		
	}

	public void setEditMode() {
		// TODO Auto-generated method stub
		
	}
	

	public void createNewText() { 
		//1. Get CurrentInfoDTO
		 InfoInList infoInList=this.infoListView.getSelectionModel().getSelectedItem();
		 //2. Append a new Key Pair to its end
		 FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap().get(infoInList.getId());
		 //TODO a List would be better than a map, do the refactor!
		 int mapLastId =currentInfoFile.getContend().getMap().keySet().size();
		 InfoDTO infoDTO = new InfoDTO(); 
		 infoDTO.setType(INFO_TYPE.TEXT);
		 //TODO to Regionalization no crazy Strings!
		 infoDTO.setText("Add text here");
		 currentInfoFile.getContend().getMap().put(mapLastId+1, infoDTO);
		 //TODO Check this part used UPDATE_IO instead of DELETE/CREATE
		//3. Save File
		 this.infoService.deleteFile(currentInfoFile);
		 this.infoService.addInfoFileToSave(currentInfoFile);
		 //4. UpdateDisplay
		 Platform.runLater(() -> {
			 this.editMode.setCurrentInfo(currentInfoFile);
		 });
	} 
	
	public void deleteInfo() {
		if(lastInfoIdSelected !=null && lastInfoInListSelected != null){
		// 1. Get FileDTO
		FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap()
				.get(lastInfoInListSelected.getId());
		// 2. Remove Info
		currentInfoFile.getContend().getMap().remove(lastInfoIdSelected);
		// 3. Update FileDTO
		//TODO use a beter way to do this if possible
		 this.infoService.deleteFile(currentInfoFile);
		 this.infoService.addInfoFileToSave(currentInfoFile);
		//4. UpdateDisplay
		 Platform.runLater(() -> {
			 this.editMode.setCurrentInfo(currentInfoFile);
		 });
		this.lastInfoIdSelected = null;
		this.lastInfoInListSelected = null;
		}
	}

	public void deleteFile() {
		//1. Current File Path
		final Path INFO_PATH = Paths.get(getUser().path().toString(),"info"); 
		//2. Id Path
		InfoInList infoInList=this.infoListView.getSelectionModel().getSelectedItem();
		//3. Create FileDTO
		FileDTO<Integer, MapInfoDTO> fileDTO = new FileDTO<>();
		fileDTO.setId(infoInList.getId());
		fileDTO.setPath(INFO_PATH);
		this.infoService.deleteFile(fileDTO);
	}

	public void setLastInfoSelected(Integer id,InfoInList infoInList) {
		this.lastInfoIdSelected= id;
		this.lastInfoInListSelected = infoInList;
	}

	public void updateInfo(Integer id, InfoInList infoInList, InfoDTO infoDTO) {
		// 1. Get FileDTO
				FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap()
						.get(infoInList.getId());
				// 2. Remove Info
				currentInfoFile.getContend().getMap().get(id).setText(infoDTO.getText());
				// 3. Update FileDTO
				//TODO use a beter way to do this if possible
				 this.infoService.deleteFile(currentInfoFile);
				 this.infoService.addInfoFileToSave(currentInfoFile);
	}
}