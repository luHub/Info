package model;

import meta.working.INFO_TYPE;
import meta.working.InfoDTO;


public class Autosave<T> {

	private String stringCache = null;
	private INFO_TYPE type;
	
	public Autosave(INFO_TYPE type){
		this.type = type;
	}

	public void run(String textString, InfoManager infoManager, Integer id, InfoInList infoInList) {
		if(isDifferent(textString,stringCache) && stringCache !=null){
			//1.Updates Cache
			stringCache = textString;
			//2. Updates File
			InfoDTO infoDTO = new InfoDTO();
			infoDTO.setText(textString);
			infoDTO.setType(this.type);
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