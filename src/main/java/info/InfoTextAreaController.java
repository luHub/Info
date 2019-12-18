package info;

import info.util.Filters;
import info.util.InfoLayout;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import meta.working.INFO_TYPE;
import meta.working.InfoDTO;
import meta.working.InfoLayoutDTO;
import model.Autosave;
import model.InfoInList;
import model.InfoManager;

public class InfoTextAreaController implements Editable {

	private InfoManager infoManager;
	private ChangeListener<Boolean> onFocusListener;

	// InfoId and TextAreaId
	private InfoInList infoInList;
	private Integer id;

	private Autosave autosave = new Autosave(INFO_TYPE.TEXT);

	@FXML
	private TextArea infoTextArea;

	@Deprecated
	public TextArea getInfoTextArea() {
		return infoTextArea;
	}

	public void setInfoManager(InfoManager infoManager) {
		this.infoManager = infoManager;

		this.infoTextArea.editableProperty().bind(this.infoManager.getInfoPanelController().getIsEditable());

		this.onFocusListener = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue.equals(true)) {
					InfoTextAreaController.this.infoManager.getInfoPanelController().getDelteButton().setDisable(false);
				} else {
					InfoTextAreaController.this.infoManager.getInfoPanelController().getDelteButton().setDisable(true);
				}
			}
		};
		this.infoTextArea.focusedProperty().addListener(this.onFocusListener);

		this.infoTextArea.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (Filters.isStringValid(newValue)) {
					autosave.run(newValue, infoManager, id, infoInList);
					infoTextArea.setStyle("-fx-text-fill:white;");
				} else {
					infoTextArea.setStyle("-fx-text-fill:red;");
				}
			}
		});
	}

	public void setInfoDTO(Integer textId, InfoInList infoInList, InfoDTO infoDTO) {
		this.infoTextArea.setText(infoDTO.getText());
		this.infoInList = infoInList;
		this.id = textId;

		final InfoLayoutDTO infoLayotDTO = this.infoManager.readInfoLayoutDTO(id, infoInList);
		infoTextArea.setMinHeight(infoLayotDTO.getHeight());
		//Takes cares of resizing and pesisting changes
		InfoLayout.saveResize(infoTextArea, infoManager, infoLayotDTO);
	}

	@Override
	public void onChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDelete() {
		this.infoTextArea.focusedProperty().removeListener(this.onFocusListener);
	}

	@FXML
	void onMouseClickedTextArea(MouseEvent event) {
		this.infoManager.getInfoPanelController().setLastSelectedINFO();
		this.infoManager.setLastInfoSelected(this.id, this.infoInList);
	}

}