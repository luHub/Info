package model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import commons.WorkbookIO;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import meta.working.ConvertableToJSON;
import meta.working.FileDTO;
import meta.working.InfoDTO;
import meta.working.MapInfoDTO;
import user.User;

public class InfoManager {
	
	//TODO: Wire this to Workbook, IOC, or something like that.
	private User user;
	private boolean isModule=true;
	private MODE mode;	
	private EditMode editMode;
	private ReadMode readMode;
	private ListView<InfoInList> infoListView;
	private final String INFO = "info";
	
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
	
	
	//TODO Write Test for this method!
	public void createNewInfoFile(){ 
		//1 Create a new Info File:
		String ext ="json";
		//2 Current info size to put proper primary Key:
		int fileId=infoService.getInfoForList().size(); 
		//3 Config File
		final Path INFO_PATH = Paths.get(getUser().path().toString(),"info"); 
		MapInfoDTO mapInfoDTO = new MapInfoDTO();
		InfoDTO infoDTO = new InfoDTO();
		
		infoDTO.setText("Insert Text Here");
		infoDTO.setContainsText(true);
		mapInfoDTO.getMap().put(fileId, infoDTO);
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
}