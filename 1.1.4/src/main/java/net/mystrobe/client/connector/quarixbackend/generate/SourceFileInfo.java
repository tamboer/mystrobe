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
 package net.mystrobe.client.connector.quarixbackend.generate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class contains java source file information.<br/>
 * 
 * Used by the class generation process to store information
 *  of not generated(user added code) code in existing generated classes.<p/>
 *  
 * @author TVH Group NV
 */
public class SourceFileInfo {
	
	/**
	 * Source file type
	 */
	private GenerateFileType fileType;
	
	/**
	 * Map will store {@link net.mystrobe.client.connector.quarixbackend.GeneratorMeta} 
	 *  properties values.
	 */
	private Map<String, String> generatorMetaAnnotationMap = new HashMap<String, String>(6); 
	
	/**
	 * List of java source file imports.
	 */
	private List<String> sourceFileImports = new ArrayList<String>();
	
	/**
	 * Source file package
	 */
	private String packageName;
	
	/**
	 * Source file class name
	 */
	private String className;
	
	/**
	 * Blocks of code added by the user to the main generated class  
	 */
	private String mainClassNotGeneratedCode;
	
	/**
	 * Blocks of code added by the user after the main generated class 
	 */
	private String afterMainClassNotGeneratedCode;

	/**
	 * @return the generatorMetaAnnotationMap
	 */
	public Map<String, String> getGeneratorMetaAnnotationMap() {
		return generatorMetaAnnotationMap;
	}

	/**
	 * @param generatorMetaAnnotationMap the generatorMetaAnnotationMap to set
	 */
	public void setGeneratorMetaAnnotationMap(
			Map<String, String> generatorMetaAnnotationMap) {
		this.generatorMetaAnnotationMap = generatorMetaAnnotationMap;
	}

	/**
	 * @return the sourceFileImports
	 */
	public List<String> getSourceFileImports() {
		return sourceFileImports;
	}

	/**
	 * @param sourceFileImports the sourceFileImports to set
	 */
	public void setSourceFileImports(List<String> sourceFileImports) {
		this.sourceFileImports = sourceFileImports;
	}

	/**
	 * @return the mainClassNotGeneratedCode
	 */
	public String getMainClassNotGeneratedCode() {
		return mainClassNotGeneratedCode;
	}

	/**
	 * @param mainClassNotGeneratedCode the mainClassNotGeneratedCode to set
	 */
	public void setMainClassNotGeneratedCode(String mainClassNotGeneratedCode) {
		this.mainClassNotGeneratedCode = mainClassNotGeneratedCode;
	}

	/**
	 * @return the afterMainClassNotGeneratedCode
	 */
	public String getAfterMainClassNotGeneratedCode() {
		return afterMainClassNotGeneratedCode;
	}

	/**
	 * @param afterMainClassNotGeneratedCode the afterMainClassNotGeneratedCode to set
	 */
	public void setAfterMainClassNotGeneratedCode(
			String afterMainClassNotGeneratedCode) {
		this.afterMainClassNotGeneratedCode = afterMainClassNotGeneratedCode;
	}

	/**
	 * @return the fileType
	 */
	public GenerateFileType getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(GenerateFileType fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
}
