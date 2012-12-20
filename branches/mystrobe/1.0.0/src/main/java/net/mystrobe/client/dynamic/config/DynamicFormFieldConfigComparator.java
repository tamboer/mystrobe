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

import java.util.Comparator;
import java.util.Map;

import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig.Property;

/**
 * @author TVH Group NV
 */
public class DynamicFormFieldConfigComparator implements Comparator<Map<Property,Object>> {
	
		public int compare(Map<Property, Object> o1,
				Map<Property, Object> o2) {
			
			if (o1 == null && o2  == null) {
				return 0;
			} else if (o1 == null) {
				return 1;
			} else if (o2 == null) {
				return -1;
			}
			
			Comparable sortValue1 = (Comparable) o1.get(Property.SortValue);  
			Comparable sortValue2 = (Comparable) o2.get(Property.SortValue);  
			
			if (sortValue1 == null && sortValue2 == null ) {
				return 0;
			} else if (sortValue1 != null && sortValue2 == null) {
				return -1;
			} else if (sortValue1 == null) {
				return 1;
			}
			
			return sortValue1.compareTo(sortValue2);
		}
}
