package net.mystrobe.client.config;

public class MyStrobeCoreSettings implements IMyStrobeCoreSettings {

	private int batchSize = 10;
	
	private boolean cacheData;
	
	/**
	 * Adjust data object batch size according to UI component size. 
	 * 
	 * @return true/false.
	 */
	public boolean useUIComponentSizeForDataObjectBatchSize = true;
	
	@Override
	public Integer getBatchSize() {
		return batchSize;
	}

	@Override
	public Boolean getCacheData() {
		return cacheData;
	}

	@Override
	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	@Override
	public void setCacheData(Boolean cacheData) {
		this.cacheData = cacheData;
	}
}
