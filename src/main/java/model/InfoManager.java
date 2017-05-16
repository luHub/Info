package model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import meta.working.InfoCoverMapDTO;
import meta.working.InfoDTO;
import meta.working.InfoLayoutDTO;
import meta.working.InfoLayoutListDTO;
import meta.working.InfoIndexDTO;
import meta.working.MapInfoDTO;
import user.User;

//TODO Make all Managers Generics!
public class InfoManager {

	// TODO: Wire this to Workbook, IOC, or something like that.
	private final String layoutConfig = "layoutConfig.json";
	private final String infoCover = "infoCover.json";
	private User user;
	private boolean isModule = true;
	private MODE mode;
	private final EditMode editMode = new EditMode(this);
	private ReadMode readMode;
	private ListView<InfoInList> infoListView;

	private InfoPanelController infoPanelController;

	// Creates a concurrent Map and be careful
	private final InfoService infoService = new InfoService();
	private Integer lastInfoIdSelected;
	private InfoInList lastInfoInListSelected;

	public InfoManager(InfoPanelController infoPanelController) {
		this.infoPanelController = infoPanelController;
		initUser();
		initializeInfoService();
	}

	/*
	 * Info Manager entry point, when it is created it initializes an
	 * infoService
	 */
	public InfoManager() {
		initUser();
		initializeInfoService();
	}

	public void setInfoListView(ListView<InfoInList> infoView) {
		this.infoListView = infoView;
	}

	public ListView<InfoInList> getInfoListView() {
		return this.infoListView;
	}

	private ChangeListener<InfoInList> managerListener = new ChangeListener<InfoInList>() {
		public void changed(ObservableValue<? extends InfoInList> observable, InfoInList oldValue,
				InfoInList newValue) {
			// Switch List views
			// 1. Get Selected Info
			// TODO Check this ugly cast!
			FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap()
					.get(newValue.getId());
			// 2. Display it on the Info Pane
			// TODO Add EDIT and READ MODE
			InfoManager.this.editMode.setCurrentInfo(currentInfoFile);

		}
	};

	/**
	 * User config depends if this Component is working isolated or inside a
	 * workbook
	 */
	private void initUser() {
		if (isModule) {
			setUser(new User());
			getUser().setUserName("user");
			getUser().setCurrentWorkbook("workbook");
			try {
				WorkbookIO.createWorkbookIfNotExists("user", "workbook");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// TODO: Reference User Object from Workbook. This is a dangerous
			// part so be careful with references and stuff
			// @UserFromWorkBook (We could create this nice annotation and a
			// custom object container)
			// The idea is be able to use "new" and not put all our app inside
			// not Java Core APIs.
		}
	}

	// TODO Write Test for this method!
	public void createNewInfoFile() {
		// 1 Create a new Info File
		String ext = "json";
		// 2 Get File Last Max ID and add 1
		List<InfoInList> iif = infoService.getInfoForList();
		OptionalInt maxId = iif.stream().mapToInt(w -> w.getId()).max();
		int fileId = 0;
		if (maxId.isPresent()) {
			fileId = maxId.getAsInt() + 1;
		}
		// 3 Config File
		final Path INFO_PATH = Paths.get(getUser().path().toString(), "info");
		MapInfoDTO mapInfoDTO = new MapInfoDTO();
		InfoDTO infoDTO = new InfoDTO();

		infoDTO.setText("Insert Text Here");
		infoDTO.setType(INFO_TYPE.TEXT);
		mapInfoDTO.getMap().put(0, infoDTO);
		mapInfoDTO.setTitle("INFO");
		FileDTO<Integer, ConvertableToJSON> fileDTO = new FileDTO(fileId, INFO_PATH, ext);
		fileDTO.setContend(mapInfoDTO);
		infoService.addInfoFileToSave(fileDTO,false);
	}

	private void initializeInfoService() {
		this.infoService.setInfoManager(this);
		this.infoService.start();
		this.infoService.getInfoFromFiles();
		this.infoService.getInfoCovers();
		this.infoService.getLayoutFromFile();
	}



	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Path getInfoPath() {
		return Paths.get(user.path().toString(), "info");
	}

	public Path getLayoutConfigPath() {
		return Paths.get(user.path().toString(), layoutConfig);
	}

	public Path getInfoFrontCoverPath() {
		return Paths.get(user.path().toString(), infoCover);
	}

	public InfoPanelController getInfoPanelController() {
		return infoPanelController;
	}

	// TODO Check this!!!
	public void setReadMode() {

	}

	// TODO Check this!!!
	public void setEditMode() {
		// TODO Auto-generated method stub

	}

	public void createNewText() {
		// 1. Get CurrentInfoDTO
		InfoInList infoInList = this.infoListView.getSelectionModel().getSelectedItem();
		// 2. Append a new Key Pair to its end
		FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap()
				.get(infoInList.getId());
		// TODO a List would be better than a map, do the refactor!
		int mapLastId = currentInfoFile.getContend().getMap().keySet().size();
		InfoDTO infoDTO = new InfoDTO();
		infoDTO.setType(INFO_TYPE.TEXT);
		// TODO to Regionalization no crazy Strings!
		infoDTO.setText("Add text here");
		currentInfoFile.getContend().getMap().put(mapLastId + 1, infoDTO);
		// TODO Check this part used UPDATE_IO instead of DELETE/CREATE
		// 3. Save File
		this.infoService.deleteFile(currentInfoFile,true);
		this.infoService.addInfoFileToSave(currentInfoFile,true);
		// 4. UpdateDisplay
		Platform.runLater(() -> {
			this.editMode.setCurrentInfo(currentInfoFile);
		});
	}

	public void deleteInfo() {
		if (lastInfoIdSelected != null && lastInfoInListSelected != null) {
			// 1. Get FileDTO
			FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap()
					.get(lastInfoInListSelected.getId());
			// 2. Remove Info
			currentInfoFile.getContend().getMap().remove(lastInfoIdSelected);
			// 3. Update FileDTO
			// TODO use a beter way to do this if possible
			this.infoService.deleteFile(currentInfoFile,true);
			this.infoService.addInfoFileToSave(currentInfoFile,true);
			// 4. UpdateDisplay
			Platform.runLater(() -> {
				this.editMode.setCurrentInfo(currentInfoFile);
			});
			this.lastInfoIdSelected = null;
			this.lastInfoInListSelected = null;
		}
	}

	public void deleteFile() {
		// 1. Current File Path
		final Path INFO_PATH = Paths.get(getUser().path().toString(), "info");
		// 2. Id Path
		InfoInList infoInList = this.infoListView.getSelectionModel().getSelectedItem();
		// 3. Create FileDTO
		FileDTO<Integer, MapInfoDTO> fileDTO = new FileDTO<>();
		fileDTO.setId(infoInList.getId());
		fileDTO.setPath(INFO_PATH);
		this.infoService.deleteFile(fileDTO,false);
	}

	public void setLastInfoSelected(Integer id, InfoInList infoInList) {
		this.lastInfoIdSelected = id;
		this.lastInfoInListSelected = infoInList;
	}

	public void updateInfo(Integer id, InfoInList infoInList, InfoDTO infoDTO) {
		// 1. Get FileDTO
		FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap()
				.get(infoInList.getId());
		// 2. Remove Info
		currentInfoFile.getContend().getMap().get(id).setText(infoDTO.getText());
		// 3. Update FileDTO
		// TODO use a beter way to do this if possible
		this.infoService.deleteFile(currentInfoFile,true);
		this.infoService.addInfoFileToSave(currentInfoFile,true);
	}

	public void createNewWebView() {
		// TODO Auto-generated method stub
		// 1. Get CurrentInfoDTO
		InfoInList infoInList = this.infoListView.getSelectionModel().getSelectedItem();
		// 2. Append a new Key Pair to its end
		FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap()
				.get(infoInList.getId());
		// TODO a List would be better than a map, do the refactor!
		int mapLastId = currentInfoFile.getContend().getMap().keySet().size();
		InfoDTO infoDTO = new InfoDTO();
		infoDTO.setType(INFO_TYPE.WEB);
		// TODO add a default no connection web page
		infoDTO.setText("https://www.google.com");
		currentInfoFile.getContend().getMap().put(mapLastId + 1, infoDTO);
		// TODO Check this part used UPDATE_IO instead of DELETE/CREATE
		// 3. Save File
		this.infoService.deleteFile(currentInfoFile,true);
		this.infoService.addInfoFileToSave(currentInfoFile,true);
		// 4. UpdateDisplay
		Platform.runLater(() -> {
			this.editMode.setCurrentInfo(currentInfoFile);
		});
	}

	// Layout Config if this scales to much use another class:
	public InfoLayoutDTO readInfoLayoutDTO(Integer id, InfoInList infoInList) {

		final double heightDefault = 30.0;

		InfoLayoutDTO infoLayoutDTO = new InfoLayoutDTO();
		infoLayoutDTO.setInfoId(id);
		infoLayoutDTO.setInfoFileId(infoInList.getId());
		infoLayoutDTO.setHeight(heightDefault);
		FileDTO<Integer, InfoLayoutListDTO> layoutFile = infoService.getLayoutInfo();

		Optional<InfoLayoutDTO> optInfoLayoutDTO = layoutFile.getContend().getInfoLayoutList().stream()
				.filter(x -> x.getInfoFileId().equals(infoInList.getId()) && x.getInfoId().equals(id)).findFirst();

		return optInfoLayoutDTO.isPresent() ? optInfoLayoutDTO.get() : infoLayoutDTO;

	}

	public void updateInfoLayoutDTO(InfoLayoutDTO infoLayotDTO) {

		this.infoService.addInfoLayoutToSave(infoLayotDTO);
	}

	public void updateInfoDetails(FileDTO<Integer, InfoIndexDTO> infoDTO) {

	}

	public void updateCover() {
		this.infoService.updateCover();
	}

	public void updateUI(InfoCoverMapDTO infoCoverMapDTO) {
		Platform.runLater(() -> {
			infoListView.getSelectionModel().selectedItemProperty().removeListener(managerListener);
			infoListView.getItems().clear();
			List<InfoInList> info = new ArrayList<>();
			infoCoverMapDTO.getInfoCovers().forEach((k,v)->{
				InfoInList infoInList = new InfoInList(v);
				infoInList.setInfoManager(this);
				info.add(infoInList);
			});
			ObservableList<InfoInList> ol = FXCollections.observableList(info);

			if (info.isEmpty()) {
				infoPanelController.getDelteButton().setDisable(true);
				infoPanelController.getDisplayVBox().getChildren().clear();
			}
			this.infoService.getInfoForList().clear();
			this.infoService.getInfoForList().addAll(info);
			infoListView.setItems(ol);
			infoListView.getSelectionModel().selectedItemProperty().addListener(managerListener);
		});
	}
}