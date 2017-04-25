package model;

import meta.working.INFO_TYPE;
import meta.working.InfoDTO;


//TODO CLEAN!!! Make this and InfoDTO more generic and delete methods
public class Autosave {

	private String stringCache = null;

	public void run(String textString, InfoManager infoManager, Integer id, InfoInList infoInList) {
		if(isDifferent(textString,stringCache) && stringCache !=null){
			//1.Updates Cache
			stringCache = textString;
			//2. Updates File
			InfoDTO infoDTO = new InfoDTO();
			infoDTO.setText(textString);
			infoDTO.setType(INFO_TYPE.TEXT);
			infoManager.updateInfo(id,infoInList,infoDTO);
			//3. Update GUI
		}else if( stringCache == null ){
			stringCache = textString;
		}
	}
	
	public void runWeb(String textString, InfoManager infoManager, Integer id, InfoInList infoInList) {
		if(isDifferent(textString,stringCache) && stringCache !=null){
			//1.Updates Cache
			stringCache = textString;
			//2. Updates File
			InfoDTO infoDTO = new InfoDTO();
			infoDTO.setText(textString);
			infoDTO.setType(INFO_TYPE.WEB);
			infoManager.updateInfo(id,infoInList,infoDTO);
			//3. Update GUI
		}else if( stringCache == null ){
			stringCache = textString;
		}
	}
	
	
	private boolean isDifferent(String textString, String stringCache) {
		return !textString.equals(stringCache);
	}
}