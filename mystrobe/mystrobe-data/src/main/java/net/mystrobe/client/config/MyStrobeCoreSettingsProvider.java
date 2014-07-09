/**
 * Copyright (C) 2010-2011 TVH Group NV. <kalman.tiboldi@tvh.com>
 *
 * This file is part of the MyStroBe project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.mystrobe.client.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyStrobeCoreSettingsProvider implements IBLSessionIDProvider, IMyStrobeCoreSettingsProvider {

	protected Logger logger = LoggerFactory.getLogger(MyStrobeCoreSettingsProvider.class);
	
	private static MyStrobeCoreSettingsProvider instance = new MyStrobeCoreSettingsProvider();
	
	private Map<String, IMyStrobeCoreSettings> applicationSettingsMap = new HashMap<>();

	private IBLSessionIDProvider blSessionIDProvider;
	
	private MyStrobeCoreSettings defaultSettings;
	
	private MyStrobeCoreSettingsProvider() {			
		defaultSettings = new  MyStrobeCoreSettings();
		defaultSettings.setBatchSize(10);
		defaultSettings.setCacheData(false);
	}
	
	public static MyStrobeCoreSettingsProvider getInstance() {
		return instance;
	}
        
    public void addApplicationCoreSettings(final String appName, final IMyStrobeCoreSettings settings) {
		
		if (settings == null) {
			throw new IllegalArgumentException("Setting can't be null");
		}
		
		applicationSettingsMap.put(appName, settings);
	}
        
    public void setBlSessionIDProvider(IBLSessionIDProvider blSessionIDProvider){
        this.blSessionIDProvider = blSessionIDProvider;
    }
                
        
	public boolean canProvideBLSessionIDs() {
		return blSessionIDProvider != null;
	}
	
        @Override
	public String getBLSessionID() {
		return blSessionIDProvider.getBLSessionID();
	}

	@Override
	public Integer getBatchSize(String appName) {
        if (applicationSettingsMap.containsKey(appName)) {
            return applicationSettingsMap.get(appName).getBatchSize();
        }
        return defaultSettings.getBatchSize();
	}

	@Override
	public Boolean getCacheData(String appName) {
        if (applicationSettingsMap.containsKey(appName)) {
            return applicationSettingsMap.get(appName).getCacheData();
        }
        return  defaultSettings.getCacheData();
	}
}
