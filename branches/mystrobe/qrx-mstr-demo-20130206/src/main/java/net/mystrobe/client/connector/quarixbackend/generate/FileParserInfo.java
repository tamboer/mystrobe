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
 * @author TVH Group NV
 */
public class FileParserInfo {
	
	private List<CodeBlockPosition> generatedCodePositions = new ArrayList<CodeBlockPosition>();
	
	private CodeBlockPosition mainClassCodePosition;
	
	private Map<String, String> generatorMetaAnnotationInfo = new HashMap<String, String>();
	
	/**
	 * List of java source file imports.
	 */
	private List<String> sourceFileImports = new ArrayList<String>();
	
	private String className;
	
	private String packageName;

	private String extendsClassName;
	
	private List<String> implementInterfaceNames;
	
	/**
	 * @return the generatedCodePositions
	 */
	public List<CodeBlockPosition> getGeneratedCodePositions() {
		return generatedCodePositions;
	}

	/**
	 * @param generatedCodePositions the generatedCodePositions to set
	 */
	public void setGeneratedCodePositions(
			List<CodeBlockPosition> generatedCodePositions) {
		this.generatedCodePositions = generatedCodePositions;
	}

	/**
	 * @return the mainClassCodePosition
	 */
	public CodeBlockPosition getMainClassCodePosition() {
		return mainClassCodePosition;
	}

	/**
	 * @param mainClassCodePosition the mainClassCodePosition to set
	 */
	public void setMainClassCodePosition(CodeBlockPosition mainClassCodePosition) {
		this.mainClassCodePosition = mainClassCodePosition;
	}

	/**
	 * @return the generatorMetaAnnotationInfo
	 */
	public Map<String, String> getGeneratorMetaAnnotationInfo() {
		return generatorMetaAnnotationInfo;
	}

	/**
	 * @param generatorMetaAnnotationInfo the generatorMetaAnnotationInfo to set
	 */
	public void setGeneratorMetaAnnotationInfo(
			Map<String, String> generatorMetaAnnotationInfo) {
		this.generatorMetaAnnotationInfo = generatorMetaAnnotationInfo;
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

	public String getExtendsClassName() {
		return extendsClassName;
	}

	public void setExtendsClassName(String extendsClassName) {
		this.extendsClassName = extendsClassName;
	}

	public List<String> getImplementInterfaceNames() {
		return implementInterfaceNames;
	}

	public void setImplementInterfaceNames(List<String> implementInterfaceNames) {
		this.implementInterfaceNames = implementInterfaceNames;
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




	public static class CodeBlockPosition {
		private int startLine = -1;
    	private int startLinePosition = -1;
    	private int startTokenLength = 1;
    	
    	
    	private int endLine = -1;
    	private int endLinePosition = -1;
    	private int endTokenLength = 1; 
    	
		/**
		 * @return the startLine
		 */
		public int getStartLine() {
			return startLine;
		}
		/**
		 * @param startLine the startLine to set
		 */
		public void setStartLine(int startLine) {
			this.startLine = startLine;
		}
		/**
		 * @return the startLinePosition
		 */
		public int getStartLinePosition() {
			return startLinePosition;
		}
		/**
		 * @param startLinePosition the startLinePosition to set
		 */
		public void setStartLinePosition(int startLinePosition) {
			this.startLinePosition = startLinePosition;
		}
		/**
		 * @return the endLine
		 */
		public int getEndLine() {
			return endLine;
		}
		/**
		 * @param endLine the endLine to set
		 */
		public void setEndLine(int endLine) {
			this.endLine = endLine;
		}
		/**
		 * @return the endLinePosition
		 */
		public int getEndLinePosition() {
			return endLinePosition;
		}
		/**
		 * @param endLinePosition the endLinePosition to set
		 */
		public void setEndLinePosition(int endLinePosition) {
			this.endLinePosition = endLinePosition;
		}
		
		/**
		 * @return the startTokenLength
		 */
		public int getStartTokenLength() {
			return startTokenLength;
		}
		/**
		 * @param startTokenLength the startTokenLength to set
		 */
		public void setStartTokenLength(int startTokenLength) {
			this.startTokenLength = startTokenLength;
		}
		/**
		 * @return the endTokenLength
		 */
		public int getEndTokenLength() {
			return endTokenLength;
		}
		/**
		 * @param endTokenLength the endTokenLength to set
		 */
		public void setEndTokenLength(int endTokenLength) {
			this.endTokenLength = endTokenLength;
		}
		
		
		@Override
		public String toString() {
			return "CodeBlockPosition [endLine=" + endLine
					+ ", endLinePosition=" + endLinePosition
					+ ", endTokenLength=" + endTokenLength + ", startLine="
					+ startLine + ", startLinePosition=" + startLinePosition
					+ ", startTokenLength=" + startTokenLength + "]";
		}
		
		
	}
}
