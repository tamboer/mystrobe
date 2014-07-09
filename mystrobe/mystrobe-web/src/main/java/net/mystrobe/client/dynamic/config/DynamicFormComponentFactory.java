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
package net.mystrobe.client.dynamic.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.mystrobe.client.WicketDSRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  
 *
 */
public class DynamicFormComponentFactory {

	protected static final Logger logger = LoggerFactory.getLogger(DynamicFormComponentFactory.class);
	
	/**
	 * Form components builders map. 
	 */
	private static ConcurrentMap<String, IDynamicFormComponentBuilder> componentBuidersMap = new ConcurrentHashMap<>();
	
	/**
	 * 
	 * @param a_builderClassName
	 * @return
	 */
	public static IDynamicFormComponentBuilder getBuilder(final String a_builderClassName) {
		
		String builderClassName = a_builderClassName != null ? a_builderClassName : 
			DynamicFormComponentBuilder.class.getName();
			
		if (!componentBuidersMap.containsKey(builderClassName)) {
			
			synchronized (componentBuidersMap) {
				try {
					
					if (!componentBuidersMap.containsKey(builderClassName))  {
						IDynamicFormComponentBuilder formComponentBuilderInstance = (IDynamicFormComponentBuilder) Class.forName(builderClassName).newInstance();
						componentBuidersMap.putIfAbsent(builderClassName, formComponentBuilderInstance);
					}
					
				} catch (InstantiationException e) {
					logger.error("Can not instantiate form component builder class.", e);
					throw new WicketDSRuntimeException(e);
				} catch (IllegalAccessException e) {
					logger.error("Can not access component builder class.", e);
					throw new WicketDSRuntimeException(e);
				} catch (ClassNotFoundException e) {
					logger.error("Can not find component builder class.", e);
					throw new WicketDSRuntimeException(e);
				}
			}
		}
		
		return componentBuidersMap.get(builderClassName);
	}
	
	public static IDynamicFormComponentBuilder getBuilder() {
		return getBuilder(DynamicFormComponentBuilder.class.getName());
	}
}
