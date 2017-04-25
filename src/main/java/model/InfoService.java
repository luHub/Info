package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import meta.working.ConvertableToJSON;
import meta.working.FileDTO;
import meta.working.InfoDTO;
import meta.working.MapInfoDTO;

public class InfoService<V> extends Service<V> {

	private final ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> infoMap = new ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>>();
	protected final Queue<InfoFileOps> queue = new ConcurrentLinkedQueue();
	private final List<InfoInList> infoForList = new ArrayList();
	private InfoManager infoManager;

	@Override
	protected Task<V> createTask() {
		// TODO Auto-generated method stub
		return new Task<V>() {

			@Override
			protected V call() throws Exception {
				while (true) {
					try {
						InfoFileOps questionFileOps = null;
						synchronized (queue) {
							while (queue.isEmpty()) {
								queue.wait();
							}
							// Get the next work item off of the queue
							questionFileOps = queue.remove();
							// Process the work item
							// Prepare with common data to work
							questionFileOps.run();
						}
					} catch (InterruptedException ie) {
						break; // Terminate
					}
				}
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
			}

			@Override
			protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelled!");
			}

			@Override
			protected void failed() {
				super.failed();
				updateMessage("Failed!");
			}
		};
	}

	public void setInfoManager(InfoManager infoManager) {
		this.infoManager = infoManager;
	}

	public List<InfoInList> getInfoForList() {
		return this.infoForList;
	}

	public void addInfoFileToSave(FileDTO<Integer, ConvertableToJSON> fileDTO) {
		synchronized (queue) {
			// Add work to the queue
			InfoFileOps infoFileOps = new InfoFileOps();
			infoFileOps.setInfoFromList(this.getInfoForList());
			infoFileOps.setInfoMap(this.infoMap);
			infoFileOps.setInfoManager(this.infoManager);
			infoFileOps.setInfoFromList(infoForList);
			infoFileOps.updateInfoToFile(fileDTO);
			queue.add(infoFileOps);
			// Notify the monitor object that all the threads
			// are waiting on. This will awaken just one to
			// begin processing work from the queue
			queue.notify();
		}
	}

	// TODO Get Info From Files
	public void getInfoFromFiles() {
		synchronized (queue) {
			InfoFileOps infoFileOps = prepateFileOps();
			queue.add(infoFileOps);
			// Notify the monitor object that all the threads
			// are waiting on. This will awaken just one to
			// begin processing work from the queue
			queue.notify();
		}
	}

	public boolean isQueueEmpty() {
		// TODO Auto-generated method stub
		return queue.isEmpty();
	}

	// TODO: Delete infoFile
	public void deleteFile(FileDTO<Integer, ConvertableToJSON> fileDTO) {
		synchronized (queue) {
			//1. Prepare delete Operation
			InfoFileOps infoFileOps = prepateFileOps();
			infoFileOps.setFileToDelete(fileDTO);
			//2. Add to service queue 
			queue.add(infoFileOps);
			//3. Notify  
			queue.notify();
		}
	}

	private InfoFileOps prepateFileOps() {
		InfoFileOps infoFileOps = new InfoFileOps();
		infoFileOps.setInfoFromList(this.infoForList);
		infoFileOps.setInfoMap(this.infoMap);
		infoFileOps.setInfoManager(this.infoManager);
		return infoFileOps;
	}

	public ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> getInfoMap() {
		return infoMap;
	}

	public void deleteInfo(Integer lastInfoIdSelected) {
		// TODO Auto-generated method stub
		
	}

}