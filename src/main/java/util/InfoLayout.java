package util;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import meta.working.InfoLayoutDTO;
import model.InfoInList;
import model.InfoManager;

public class InfoLayout {
	// Add more stuff to this utility.
	public static void saveResize(final Region region,final InfoManager infoManager,final InfoLayoutDTO infoLayotDTO) {

		region.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (isInSouthMargin(region, event)) {
					region.setCursor(Cursor.S_RESIZE);
				} else {
					region.setCursor(Cursor.DEFAULT);
				}
			}

			private boolean isInSouthMargin(Region region, MouseEvent event) {
				return event.getY() > event.getY() - 5;
			}
		});

		region.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				double delta = event.getY() - region.getHeight();
				region.setMinHeight(region.getMinHeight() + delta);
				infoLayotDTO.setHeight(region.getMinHeight() + delta);
				infoManager.updateInfoLayoutDTO(infoLayotDTO);
			}
		});
	}

	public void loadHeight(Integer id, InfoInList idBook) {
		
	}

	public void saveHeight(Integer id, InfoInList idBook) {

	}
	
	
	
}