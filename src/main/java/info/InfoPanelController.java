package info;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import meta.working.InfoMainLayoutDTO;
import model.InfoInList;
import model.InfoManager;
import model.MODE;
import util.RunAfter;
 
public class InfoPanelController { 
	 
	@FXML
    private Button ModeButton;

    @FXML
    private MenuButton addInfoButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<InfoInList> infoView;
    
    @FXML
    private VBox displayVBox;

    @FXML
    private AnchorPane infoAnchorPane;
    
    @FXML
    private AnchorPane infoDisplay;
    
    @FXML
    private SplitPane splitPane;
	
    private MODE mode = MODE.READ; 
    
    private LastSelected lastSelected = LastSelected.NONE;
    
    private InfoManager infoManager = new InfoManager(this);
    
    private BooleanProperty isEditable = new SimpleBooleanProperty();

    
    final int newFile = 0;
	final int newText = 1;
	final int newWeb = 2;
	final int newImage = 3;
	
	private RunAfter runAfter = new RunAfter<Double>(10);

	final PseudoClass FAVORITE_PSEUDO_CLASS = PseudoClass.getPseudoClass("favorite");
    @FXML
    private void initialize(){
    	
    	splitPane.setDividerPosition(0, 0.3f);
    	
    	setToReadMode();
    	setNewInfoButton();
    	initializeInfoManager();
    	
		addInfoButton.getItems().get(InfoPanelController.this.newText).setDisable(true);
		addInfoButton.getItems().get(InfoPanelController.this.newWeb).setDisable(true);
    	
    	this.infoView.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if(newValue.equals(true)){
				InfoPanelController.this.getDelteButton().setDisable(false);
			}else{
				InfoPanelController.this.getDelteButton().setDisable(true);
			}
		}});
    	
    	//Customization of ListView to work with objects in callback it will use toString From object:
    	infoView.setCellFactory(new Callback<ListView<InfoInList>, ListCell<InfoInList>>() {
			@Override
			public ListCell<InfoInList> call(ListView<InfoInList> param) {
				
				ListCell<InfoInList> myQuestion = new ListCell<InfoInList>(){
					@Override
                    protected void updateItem(InfoInList t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                        	setGraphic(t.getInfoNode());
                        }else{
                        	setGraphic(null);
                        }
                    }
				};
				return myQuestion;
			}
		});
    	
    	
    	
    	//TODO WOrking on CHanges
    	final InfoMainLayoutDTO infoLayotDTO = this.infoManager.readMainLayoutDTO();
    	splitPane.setDividerPosition(0, infoLayotDTO.getSplitPanePos());
    	//Split Pane Persistance
    	
    	splitPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println("Observable Number Value:  "+observable.getValue());
				InfoPanelController.this.runAfter.setValue(newValue);
			}
		});
    	
    	
    }

	private void initializeInfoManager() {
		this.infoManager.setInfoListView(infoView);
		
	}

	@FXML
	private void deleteInfoButton(ActionEvent event) {
		//Continue here to delete items
		if(lastSelected.equals(LastSelected.LIST)){ 
			this.infoManager.deleteFile();
		}else if(lastSelected.equals(LastSelected.INFO)){
			this.infoManager.deleteInfo();
		}
    }

    @FXML
    private void editInfoButton(ActionEvent event) {

    }

    @FXML
    private void ModeButtonOnAction(ActionEvent event) {
    	if(mode == MODE.READ){
    		setToEditMode();
    	}else{
    		setToReadMode();
    	}
    }
    
    private void setToEditMode(){
    	mode = MODE.EDIT;
    	this.ModeButton.setText("Read");
		deleteButton.setVisible(true);
		deleteButton.setDisable(true);
		addInfoButton.setVisible(true);
		this.infoManager.setEditMode();
		this.isEditable.set(true);
	}
    
    private void setToReadMode() {
    	mode = MODE.READ;
		this.ModeButton.setText("Edit");
		deleteButton.setVisible(false);
		addInfoButton.setVisible(false);
		this.infoManager.setReadMode();
		this.isEditable.set(false);
    }
    
    
    private void setNewInfoButton() {
    	
    	//TODO FIX this for general purpouse (MOuse, keyboard,touch,etc)
    	addInfoButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
    		@Override
			public void handle(MouseEvent event) {
			if(infoView.getSelectionModel().getSelectedItem()==null){
				addInfoButton.getItems().get(InfoPanelController.this.newText).setDisable(true);
				addInfoButton.getItems().get(InfoPanelController.this.newWeb).setDisable(true);
			}
			else
			{
				addInfoButton.getItems().get(InfoPanelController.this.newText).setDisable(false);
				addInfoButton.getItems().get(InfoPanelController.this.newWeb).setDisable(false);
			}
    		}
    	});

    	
		addInfoButton.getItems().get(newFile).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
			infoManager.createNewInfoFile();
			}
		});
		addInfoButton.getItems().get(newText).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
			infoManager.createNewText();
			}
		});
		addInfoButton.getItems().get(newWeb).setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
			infoManager.createNewWebView();
			}
		});
	}

	public VBox getDisplayVBox() {
		return displayVBox;
	}

	public BooleanProperty getIsEditable() {
		return isEditable;
	}

	

	public Button getDelteButton() {
		return deleteButton;
	}
	
	
	
	public void setLastSelectedINFO() {
		lastSelected = LastSelected.INFO;
	}
	
	@FXML
    void onMoudeClickedList(MouseEvent event) {
		lastSelected = LastSelected.LIST;
		
    }
	
}