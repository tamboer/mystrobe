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
 package net.mystrobe.client.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mystrobe.client.dynamic.config.IDynamicFormConfig;


/**
 * String utility class.
 * 
 * @author TVH Group NV
 */
public class StringUtil {
	
	
	/**
	 * Strings to remove
	 */
	private static final String NORMALIZE_REG_EXP = "\\+|-";
	
	private static Pattern PATTERN = Pattern.compile(NORMALIZE_REG_EXP); 
	
	/**
	 * Config 'true' string
	 */
	public static final String TRUE = "true";
	
	/**
	 * Config 'false' string
	 */
	public static final String FALSE = "false";
	
	/**
	 * Empty string constant value.
	 */
	public static final String EMPTY_STRING = ""; 
	
	/**
	 * Checks if <tt>value</tt> is null or empty.
	 * 
	 * @param value String value to check.
	 * @return true if value is null or empty
	 */
	public static final boolean isNullOrEmpty(String value) {
		return (value == null || EMPTY_STRING.equals(value.trim()));
	}
	
	/**
	 * Checks if <tt>value</tt> is null or empty.
	 * 
	 * @param value String value to check.
	 * @return true if value is null or empty
	 */
	public static final String normalizeColumnName(String value) {
		Matcher matcher  = PATTERN.matcher(value);
		return matcher.replaceAll(EMPTY_STRING);
	}
	
	public static final String buildDefaultResourceValue(String key) {
		StringBuilder stringBuilder = new StringBuilder(IDynamicFormConfig.RESOURCE_KEY_DEFAULT_PREFIX);
		stringBuilder.append(key).append(IDynamicFormConfig.RESOURCE_KEY_DEFAULT_SUFFIX);
		
		return stringBuilder.toString();
	}
}
