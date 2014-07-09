package net.mystrobe.client.ui.config;

import net.mystrobe.client.WicketDSRuntimeException;

import org.apache.wicket.Application;

public class MyStrobeWebSettingsProvider implements IMyStrobeSettings {
	
	private static volatile MyStrobeWebSettingsProvider instance;
	
	private IMyStrobeSettingsAware myStrobeSettingsAware;
	
	private final int DEFAULT_NAVIGATOR_VISIBLE_PAGES_COUNT = 4;

	private final int DEFAULT_PAGE_SIZE = 10;
	
	private MyStrobeWebSettingsProvider(final Application application) {
		
		if (application instanceof IMyStrobeSettingsAware) {
			myStrobeSettingsAware = (IMyStrobeSettingsAware) application;
		}
	}
	
	public static MyStrobeWebSettingsProvider getInstance(final Application application) {
		if (instance == null) {
			
			synchronized (MyStrobeWebSettingsProvider.class) {
				if (instance == null) {
					instance = new MyStrobeWebSettingsProvider(application);
				}
			}
		}
		return instance;
	}
	
	
	@Override
	public String getDynamicFormComponentsBuilderClassName() {
		if (myStrobeSettingsAware != null) {
			return myStrobeSettingsAware.getMyStrobeSettings().getDynamicFormComponentsBuilderClassName();
		}
		return null;
	}


	@Override
	public boolean isSelectedTableRowCssEnabled() {
		if (myStrobeSettingsAware != null) {
			return myStrobeSettingsAware.getMyStrobeSettings().isSelectedTableRowCssEnabled();
		}
		return false;
	}


	@Override
	public String getSelectedTableRowCssSelector() {
		if (myStrobeSettingsAware != null) {
			return myStrobeSettingsAware.getMyStrobeSettings().getSelectedTableRowCssSelector();
		}
		return null;
	}


	@Override
	public int getPageableNavigatorVisiblePagesCount() {
		if (myStrobeSettingsAware != null) {
			
			if (myStrobeSettingsAware.getMyStrobeSettings().getPageableNavigatorVisiblePagesCount() <= 0) {
				throw new WicketDSRuntimeException("Invalid web setting of navigator visible pages count.");
			}
			
			return myStrobeSettingsAware.getMyStrobeSettings().getPageableNavigatorVisiblePagesCount();
		}
		return DEFAULT_NAVIGATOR_VISIBLE_PAGES_COUNT;
	}


	@Override
	public boolean getUseUIComponentSizeForDataObjectBatchSize() {
		if (myStrobeSettingsAware != null) {
			return myStrobeSettingsAware.getMyStrobeSettings().getUseUIComponentSizeForDataObjectBatchSize();
		}
		return true;
	}

	@Override
	public int getPageSize() {
		if (myStrobeSettingsAware != null) {
			
			if (myStrobeSettingsAware.getMyStrobeSettings().getPageSize() <= 0) {
				throw new WicketDSRuntimeException("Invalid web setting of page size.");
			}
			
			
			return myStrobeSettingsAware.getMyStrobeSettings().getPageSize();
		}
		return DEFAULT_PAGE_SIZE;
	}
	
	public boolean isWebSettingsAware() {
		return (myStrobeSettingsAware != null);
	}
}
