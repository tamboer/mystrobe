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
package net.mystrobe.client.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation for search bean fields.
 *    
 * Used by data objects when repositioning to clear/reset data objects filters.
 * 
 * For <tt>operator</tt> supported values are: '=', '>', '<', '<=', '>=', '!=' and '*'. If incorrect 
 *  values is set '=' will be used. <br/> 
 *  
 * For @link java.lang.Date and @link java.sql.Timestamp field types format has to be set. 
 * 
 * @author TVH Group NV
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SearchFilter {

	public String name();
	
	public String operator() default "=";
	
	public String defaultValue() default "";
	
	public String format() default "";
	
	public boolean remove() default true ;
}
