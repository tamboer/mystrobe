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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.mystrobe.client.connector.quarixbackend.ClassGenerator;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.connector.quarixbackend.generate.parser.JavaLexer;
import net.mystrobe.client.connector.quarixbackend.generate.parser.JavaParser;
import net.mystrobe.client.util.StringUtil;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.BaseTree;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * Class loads all class generation information from
 *  existing source files.<br/>
 *  
 * Sources folder is parsed and for each file information
 *  on user added code, imports, class type is stored into {@link SourceFileInfo} objects.<p/>
 * 
 * @author TVH Group NV
 */
public class GeneratedFilesDataLoader {

	private static final Logger logger = LoggerFactory.getLogger(GeneratedFilesDataLoader.class); 
	
	protected static final String GENERATED_ANNOTATION_NAME = Generated.class.getName().
					substring(Generated.class.getName().lastIndexOf(".") + 1, Generated.class.getName().length());
	
	protected static final String GENERATE_META_ANNOTATION_NAME = GeneratorMeta.class.getName().
					substring(GeneratorMeta.class.getName().lastIndexOf(".") + 1, GeneratorMeta.class.getName().length());
	
	/**
	 * Sources folder path
	 */
	private String generatedFilesFolderPath;

	public GeneratedFilesDataLoader(String generatedFilesFolderPath) {
		super();
		this.generatedFilesFolderPath = generatedFilesFolderPath;
	}
	
	/**
	 * Get all files in sources folder and subfolders.
	 * 
	 * @param sourceFolder Source file root folder.
	 * @return List of all source file in folder.
	 */
	public List<File> getAllSourcesFiles(File sourceFolder) {
		
		List<File> sourceFiles = new ArrayList<File>();
		
		for (File sourceFolderFile : sourceFolder.listFiles()) {
			if (sourceFolderFile.isFile()) {
				sourceFiles.add(sourceFolderFile);
			} else {
				sourceFiles.addAll(getAllSourcesFiles(sourceFolderFile));
			}
		}
		return sourceFiles;
	}
	
	/**
	 * Method loads all class generation relevant information
	 *  for each of the source files in the sources folder.  
	 * 
	 * @return Source file class generation information(source type, user code, imports).
	 * @throws Exception Can not read/parse code information from the source files 
	 */
	public GeneratedFilesInfo loadGeneratedFilesData() throws Exception {
		
		logger.debug("Generated sources folder:" + generatedFilesFolderPath);
		
		File filesFolder = new File(generatedFilesFolderPath);
		
		Map<String, Map<String, Map<GenerateFileType,SourceFileInfo>>> datasetDaoGeneratedFilesMap = new HashMap<String, Map<String,Map<GenerateFileType,SourceFileInfo>>>();  
		Map<String, SourceFileInfo> datasetSchemaGeneratedFilesMap = new HashMap<String, SourceFileInfo>();
		
		if (!filesFolder.isDirectory()) {
			throw new Exception("Sources folder is not valid. Please provide correct folder for sources.");
		}
		
		List<File> sourcesFiles = getAllSourcesFiles(filesFolder);
		
		for (File sourceFile : sourcesFiles) {
			
			SourceFileInfo fileInfo = loadFileData(sourceFile);
			
			Map<String, String> generateMetaAnnotationMap = fileInfo.getGeneratorMetaAnnotationMap();
			
			String dsId = generateMetaAnnotationMap.get("dsId");
			String daoId = generateMetaAnnotationMap.get("daoId");
			
			System.out.println( "generated sources DSID:" + dsId );
			System.out.println( "generated sources DaoID:" + daoId );
			
			if (daoId == null) {
				//DSSchema file
				
				datasetSchemaGeneratedFilesMap.put(dsId, fileInfo);
				
				continue;
			}
			
			Map<String, Map<GenerateFileType,SourceFileInfo>> datasetFilesMap;
			
			if (datasetDaoGeneratedFilesMap.containsKey(dsId)) {
				datasetFilesMap = datasetDaoGeneratedFilesMap.get(dsId);
			} else {
				datasetFilesMap = new HashMap<String, Map<GenerateFileType,SourceFileInfo>>();
				datasetDaoGeneratedFilesMap.put(dsId, datasetFilesMap);
			}
			
			Map<GenerateFileType,SourceFileInfo> daoFilesMap; 
			if (datasetFilesMap.containsKey(daoId)) {
				daoFilesMap = datasetFilesMap.get(daoId);
			} else {
				daoFilesMap = new HashMap<GenerateFileType, SourceFileInfo>(3);
				datasetFilesMap.put(daoId, daoFilesMap);
			}
			
			daoFilesMap.put(fileInfo.getFileType(), fileInfo);
		}
		
		return new GeneratedFilesInfo(datasetDaoGeneratedFilesMap, datasetSchemaGeneratedFilesMap);
	}
	
	
	
	
	/**
	 * Validate file using the antlr grammar.
	 * 
	 * @param fileContent File content.
	 * @return True when file is valid according to anltr grammar parser.
	 */
	public static boolean validateFile(String fileName, String fileContent) {
		
		try {
			
			CharStream charStream = new ANTLRStringStream(fileContent);
			
			JavaLexer javaLexer = new JavaLexer(charStream);
			
			CommonTokenStream tokenStream = new CommonTokenStream(javaLexer); 
			
			JavaParser javaParser = new JavaParser(tokenStream);
			
			javaParser.enableErrorMessageCollection(true);
			javaParser.javaSource();
			
			if (javaParser.hasErrors()) {
				
				for (String message : javaParser.getMessages()) {
					logger.error(" File validation failed for file: " + fileName + ", message:" + message);
				}
			}
			return true;
			
		} catch (Exception e) {
			
			logger.error(" File validation failed for file: " + fileName, e);
			
			return false;
		} 
		
	}
	
	/**
	 * Load source file information for file <tt>sourceFile</tt>.
	 * 
	 * @param sourceFile File to load information for.
	 * @return Source file class generation information.
	 * @throws Exception Can not access/read/parse file.
	 */
	protected SourceFileInfo loadFileData(File sourceFile) throws Exception {
	
		logger.debug("Load generated file data from file:" + sourceFile.getName());
		
		CharStream charStream = new ANTLRFileStream(sourceFile.getPath());
		
		JavaLexer javaLexer = new JavaLexer(charStream);
		
		CommonTokenStream tokenStream = new CommonTokenStream(javaLexer); 
		
		JavaParser javaParser = new JavaParser(tokenStream);
		
		javaParser.enableErrorMessageCollection(true);
		
		try {
			JavaParser.javaSource_return javaCode  = javaParser.javaSource();
			
			if (javaParser.hasErrors()) {
				
				for (String message : javaParser.getMessages()) {
					logger.error(" Failed to parse file " + sourceFile.getName() + ", message:" + message);
				}
				
				throw new Exception(" Can not parse source file " + sourceFile.getName() + ", look up for validation error messages.");
			
			} else {
			
				CommonTree astRoot = (CommonTree) javaCode.getTree();
				
				FileParserInfo fileParserInfo = new FileParserInfo();
				
				parseTree(1, astRoot, tokenStream, fileParserInfo);
				
				SourceFileInfo sourceFileInfo = getSourceFileNotGeneratedCodeInfo(fileParserInfo, sourceFile);
				
				sourceFileInfo.setGeneratorMetaAnnotationMap(fileParserInfo.getGeneratorMetaAnnotationInfo());
				
				sourceFileInfo.setFileType(getGeneratedFileType(sourceFile.getName()));
				
				sourceFileInfo.setPackageName(fileParserInfo.getPackageName()); 

				sourceFileInfo.setClassName(fileParserInfo.getClassName()); 
				
				sourceFileInfo.setSourceFileImports(fileParserInfo.getSourceFileImports());
				
				return sourceFileInfo;
			}
			
		} catch (RecognitionException e) {
			
			throw new Exception("Unable to parse source code for file: " + sourceFile.getName(), e);
		}
	}
	
	/**
	 * Parse the AST antlr tree and look for blocks of generated code.
	 * 
	 * @param level
	 * @param ast 
	 * @param tokenStream Source file tokens.
	 * @param fileParserInfo Method is called recursevely and info
	 * 		is saved to this object which is passed on to each tree parsing method call.   
	 */
	private void parseTree(int level, BaseTree ast, CommonTokenStream tokenStream, FileParserInfo fileParserInfo )  {
		if (ast == null || ast.getChildren() == null) {
			return;
		}
		
		int nextLevel = level + 1;
//		String levelText = " ";
//		for (int i = 1; i<level ; i++) {
//			levelText = levelText + "-";
//		}
		
		for (Object child : ast.getChildren()) {
			
//			if (child != null) {
//				Tree tree = (Tree) child;
//				System.out.println("Token:" + levelText + tree.getText()   );
//			}
			
			if (child != null && child instanceof BaseTree) {
				BaseTree childBaseTree = (BaseTree)child;
				
				if (childBaseTree.getType() == JavaParser.PACKAGE) {
					fileParserInfo.setPackageName(getPackageName(childBaseTree));
				}
				
				if (childBaseTree.getType() == JavaParser.IMPORT) {
					String packageName = getPackageName(childBaseTree);
					fileParserInfo.getSourceFileImports().add(packageName);
				}
				
				
				if (childBaseTree.getType() == JavaParser.VAR_DECLARATION  ||
						childBaseTree.getType() == JavaParser.VOID_METHOD_DECL || 
						childBaseTree.getType() == JavaParser.FUNCTION_METHOD_DECL ||
						childBaseTree.getType() == JavaParser.CONSTRUCTOR_DECL ||
						childBaseTree.getType() == JavaParser.CLASS ||
						childBaseTree.getType() == JavaParser.ENUM
						) {
					
					if (checkForGeneratedAnnotations(childBaseTree, fileParserInfo)) {
						
						List<Token> tokens = tokenStream.getTokens(childBaseTree.getTokenStartIndex(), childBaseTree.getTokenStopIndex());
						
						FileParserInfo.CodeBlockPosition codePosition = buildCodeBlockPosition(tokens.get(0), tokens.get(tokens.size() - 1));
						
						fileParserInfo.getGeneratedCodePositions().add(codePosition);
						
					} else if (childBaseTree.getType() == JavaParser.CLASS) {
						
						BaseTree modifierList =  (BaseTree) childBaseTree.getFirstChildWithType(JavaParser.MODIFIER_LIST);
						Tree publicModifier = modifierList.getFirstChildWithType(JavaParser.PUBLIC);
						Tree classAncestor = childBaseTree.getAncestor(JavaParser.CLASS);
						
						if (publicModifier != null && classAncestor == null) {
							
							getClassName(childBaseTree, fileParserInfo);
							
							Tree classBody = childBaseTree.getFirstChildWithType(JavaParser.CLASS_TOP_LEVEL_SCOPE);
							List<Token> tokens = tokenStream.getTokens(classBody.getTokenStartIndex(), classBody.getTokenStopIndex());
							
							FileParserInfo.CodeBlockPosition codePosition = buildCodeBlockPosition(tokens.get(0), tokens.get(tokens.size() - 1));
							
							fileParserInfo.setMainClassCodePosition(codePosition);
						}
						
						//if not entire class is generated then parse it for generated code 
						parseTree(nextLevel, childBaseTree, tokenStream, fileParserInfo );
					}
					
				}  else {
					parseTree(nextLevel, (BaseTree)child, tokenStream, fileParserInfo );
				}
			}
		}
	}
	
	/**
	 * Check if current code block annotation is of type {@link Generated}.<br/>
	 * 
	 * When {@link net.mystrobe.client.connector.quarixbackend.GeneratorMeta} annotation is found 
	 * 	read all information to easily identify current file type, dsId and daoId. 
	 * 
	 * @param ast Code block tree.
	 * @param fileParserInfo File parser info
	 * @return True if this code block was generated.
	 */
	private boolean checkForGeneratedAnnotations (BaseTree ast, FileParserInfo fileParserInfo) {
		BaseTree modifierList =  (BaseTree) ast.getFirstChildWithType(JavaParser.MODIFIER_LIST);
		
		if (modifierList == null ||  modifierList.getChildren() == null) {
			return false;
		}
		
		for (Object child : modifierList.getChildren()) {
			Tree tree = (Tree) child;
			if (tree.getType() == JavaParser.AT) {
				Tree annotationIdentifier = ((BaseTree)tree).getFirstChildWithType(JavaParser.IDENT); 
				
				if (GENERATE_META_ANNOTATION_NAME.equals(annotationIdentifier.getText())) {
					fileParserInfo.setGeneratorMetaAnnotationInfo(getGenerateMetaAnotationInfo(tree));
				}
				
				if ( GENERATED_ANNOTATION_NAME.equals(annotationIdentifier.getText())) {
					return true;
				}	
			}
		}
		return false;
	}
	
	/**
	 * Read all {@link @GeneratorMeta} from the AST tree and
	 *  return it as a {@link Map}.
	 * 
	 * @param generateMetaAnnotationTree
	 * @return Map of generator meta annotation info.
	 */
	private Map<String, String> getGenerateMetaAnotationInfo(Tree generateMetaAnnotationTree) {
		Map<String, String> generateMetaAnnotationInfoMap = new HashMap<String, String>(); 
		
		Tree annotationInitBlock = ((BaseTree) generateMetaAnnotationTree).getFirstChildWithType(JavaParser.ANNOTATION_INIT_BLOCK);
		Tree annotationInitKeyList = ((BaseTree) annotationInitBlock).getFirstChildWithType(JavaParser.ANNOTATION_INIT_KEY_LIST);
		
		for (Object annotationProperty : ((BaseTree)annotationInitKeyList).getChildren()) {
			
			BaseTree annotationPropertyTree = (BaseTree) annotationProperty;
			
			if (annotationPropertyTree.getType() == JavaParser.IDENT) {
				String propertyName = annotationPropertyTree.getText();
				String propertyValue = annotationPropertyTree.getChild(0).getChild(0).getText();
				
				if (propertyValue.indexOf("\"") == 0) {
					propertyValue = propertyValue.substring(1, propertyValue.lastIndexOf("\""));
				}
				
				generateMetaAnnotationInfoMap.put(propertyName, propertyValue);
				//System.out.println( " Property name: " + propertyName + " ,property value:" + propertyValue);
			}
		}
		
		return generateMetaAnnotationInfoMap;
	}
	
	private void displayAllChildren (BaseTree classBaseTree, int level) {
		
		StringBuilder prefix = new StringBuilder("");
		for (int i = 0; i < level; i++) {
			prefix.append("-");
		}
		
		logger.debug(prefix.toString() + " " + classBaseTree.getText());
		
		if (classBaseTree.getChildCount() > 0) {
			for (Object child : classBaseTree.getChildren() ) {
				if (child instanceof BaseTree) {
					BaseTree childBaseTree = (BaseTree) child;
					displayAllChildren(childBaseTree, level + 1);
				}
			}
		} 
	}
	
	/**
	 * Get class name.
	 * 
	 * @param classBaseTree Start class block.
	 * @param fileParserInfo Current file parsing info.
	 */
	private void getClassName(BaseTree classBaseTree, FileParserInfo fileParserInfo) {
		
		Tree classNameChild = classBaseTree.getChild(classBaseTree.getFirstChildWithType(JavaParser.MODIFIER_LIST).getChildIndex() + 1);
		fileParserInfo.setClassName(classNameChild.getText());
		
		//displayAllChildren(classBaseTree, 0);
		
		if (classBaseTree.getFirstChildWithType(JavaParser.EXTENDS_CLAUSE) != null) {
			Tree extendsNameChild = classBaseTree.getFirstChildWithType(JavaParser.EXTENDS_CLAUSE);
			String extendsClassName = extendsNameChild.getChild(0).getChild(0).getChild(0).getText();
			logger.debug("Extends class name : " + extendsClassName);
			
			fileParserInfo.setExtendsClassName(extendsClassName);
		}
		
		if (classBaseTree.getFirstChildWithType(JavaParser.IMPLEMENTS_CLAUSE) != null) {
			List<Tree> implementsInterfaceTreeList = ((BaseTree)classBaseTree.getFirstChildWithType(JavaParser.IMPLEMENTS_CLAUSE)).getChildren();
			List<String> implementsInterfaceNames = new ArrayList<String>(implementsInterfaceTreeList.size());
			
			for ( Tree implementsClassNode : implementsInterfaceTreeList ) {
				implementsInterfaceNames.add(implementsClassNode.getChild(0).getChild(0).getText());
				logger.debug("Implements class name : " + implementsClassNode.getChild(0).getChild(0).getText());
			}
			fileParserInfo.setImplementInterfaceNames(implementsInterfaceNames);
		}
	}
	
	/**
	 * Build package name.
	 * 
	 * @param Package AST tree
	 * @return String representing the class name.
	 */
	private String getPackageName(BaseTree classBaseTree) {
	
		String packageName = "";
		for (Object objectTree : classBaseTree.getChildren()) {
			
			BaseTree childTree = (BaseTree) objectTree;
			
			if (childTree.getChildCount() > 0) {
				packageName += getPackageName(childTree); 
			} else  {
				
				if (packageName.length() > 0) {
					packageName += ".";
				}
				
				packageName += childTree.getText();
			}
		}
		
		return packageName;
	}
	
	
	/**
	 * Read code block positioning information from 
	 *  starrt and end tokens and return it as a {@link FileParserInfo.CodeBlockPosition} 
	 * 
	 * @param startToken Code block start token.
	 * @param endToken Code block end token.
	 * @return Code positioning information.
	 */
	private FileParserInfo.CodeBlockPosition buildCodeBlockPosition(Token startToken, Token endToken) {
		
		FileParserInfo.CodeBlockPosition codePosition = new FileParserInfo.CodeBlockPosition();
		
		codePosition.setStartLine(startToken.getLine());
		codePosition.setStartLinePosition(startToken.getCharPositionInLine());
		codePosition.setStartTokenLength(startToken.getText().length());
	
		codePosition.setEndLine(endToken.getLine());
		codePosition.setEndLinePosition(endToken.getCharPositionInLine());
		codePosition.setEndTokenLength(endToken.getText().length());
		
		return codePosition;
	}
	
	/**
	 * Parses source file and identifies all generated user code, 
	 *  main class start end positions, import statements<br/> 
	 * 
	 * @param fileParserInfo Parsing information
	 * @param sourceFile Source file.
	 * @return Source file class generation information.
	 * 
	 * @throws IOException Can not read/access file.
	 */
    private SourceFileInfo getSourceFileNotGeneratedCodeInfo(FileParserInfo fileParserInfo, File sourceFile ) throws IOException{
    
    	SourceFileInfo sourceFileInfo = new SourceFileInfo();
    	
    	String NL = System.getProperty("line.separator");
	    Scanner scanner = new Scanner(new FileInputStream(sourceFile));
	    
	    int lineCount = 0;
	    int generatedBlockIndex = 0;
	    
	    //StringBuilder beforeMainClassCode = new StringBuilder();
	    List<String> sourceFileImports = new ArrayList<String>();
	    
	    //main class user added code
	    StringBuilder notGeneratedMainClassCode = new StringBuilder(StringUtil.EMPTY_STRING);
	    
	    //flag used to avoid adding multiple indentation block one after each other
	    boolean addNewEmptyLine = true;
	    
	    //after main class user added code
	    StringBuilder afterMainClassCode = new StringBuilder(StringUtil.EMPTY_STRING);
	    
	    List<FileParserInfo.CodeBlockPosition> generatedCodePositions = fileParserInfo.getGeneratedCodePositions();
	    
	    FileParserInfo.CodeBlockPosition codePosition = null;
	    if (generatedCodePositions != null && !generatedCodePositions.isEmpty() ) { 
	    	codePosition = generatedCodePositions.get(0);
	    }
	    
	    FileParserInfo.CodeBlockPosition mainClassCodePosition = fileParserInfo.getMainClassCodePosition();
	    
	    try {
	    
	    	while (scanner.hasNextLine()) {
	    		
				String sourceCodeLine = scanner.nextLine();
				lineCount++;
				boolean generatedBlockLine = false;
				int generatedBlockEndLinePosition = 0;
				
				if (codePosition != null ) {
					if (lineCount < codePosition.getEndLine() && lineCount > codePosition.getStartLine()) {
						  continue;
					} else if (lineCount == codePosition.getEndLine()) {
						generatedBlockEndLinePosition = codePosition.getEndLinePosition() + codePosition.getEndTokenLength();
						sourceCodeLine = sourceCodeLine.substring(generatedBlockEndLinePosition, sourceCodeLine.length());
						generatedBlockLine = true;
					} else if (lineCount == codePosition.getStartLine()) {
						generatedBlockLine = true;
						sourceCodeLine = sourceCodeLine.substring(0, codePosition.getStartLinePosition());
					}
				}
					    	  
				if (codePosition != null && codePosition.getEndLine() <= lineCount) {
					generatedBlockIndex++;
					if (generatedBlockIndex < generatedCodePositions.size()) {
						codePosition = generatedCodePositions.get(generatedBlockIndex);
						
						if (codePosition.getStartLine() == lineCount ) {
							
							if (!StringUtil.EMPTY_STRING.equals(sourceCodeLine.trim())) {
								sourceCodeLine = sourceCodeLine.substring(0, codePosition.getStartLinePosition() -  generatedBlockEndLinePosition);
							} else {
								continue;
							}
						}
					
					} else {
						codePosition = null;
					}
				}
				
				if (generatedBlockLine && StringUtil.EMPTY_STRING.equals(sourceCodeLine.trim()) ) {
					continue; 
				} 
				
				if (lineCount >= mainClassCodePosition.getStartLine() && 
						lineCount <= mainClassCodePosition.getEndLine()) {
					
					if (lineCount == mainClassCodePosition.getStartLine()) {
						sourceCodeLine = sourceCodeLine.substring(mainClassCodePosition.getStartLinePosition() + mainClassCodePosition.getStartTokenLength(), sourceCodeLine.length());
						
						if (StringUtil.EMPTY_STRING.equals(sourceCodeLine.trim())) {
							continue;
						}
						
					} else if (lineCount == mainClassCodePosition.getEndLine()) {
						
						String codeAfterMainClass = sourceCodeLine.substring(mainClassCodePosition.getEndLinePosition() + mainClassCodePosition.getEndTokenLength(),
								sourceCodeLine.length());
						
						if (!StringUtil.EMPTY_STRING.equals(codeAfterMainClass)) {
							afterMainClassCode.append(codeAfterMainClass).append(NL);
						}
						
						sourceCodeLine = sourceCodeLine.substring(0, mainClassCodePosition.getEndLinePosition());
						if (StringUtil.EMPTY_STRING.equals(sourceCodeLine.trim())) {
							continue;
						}
					}
					
					if (StringUtil.EMPTY_STRING.equals(sourceCodeLine.trim()) && addNewEmptyLine) {
						//avoid adding multiple empty lines 
						addNewEmptyLine = false;
						notGeneratedMainClassCode.append(sourceCodeLine).append(NL);
					} else if (!StringUtil.EMPTY_STRING.equals(sourceCodeLine.trim())) {
						addNewEmptyLine = true;
						notGeneratedMainClassCode.append(sourceCodeLine).append(NL);
					}
				
				} else if (lineCount > mainClassCodePosition.getEndLine())  {
					afterMainClassCode.append(sourceCodeLine).append(NL);
				} 
			}
	    }
	    
	    finally{
	      scanner.close();
	    }
		    
		sourceFileInfo.setAfterMainClassNotGeneratedCode(afterMainClassCode.toString());
		sourceFileInfo.setMainClassNotGeneratedCode(notGeneratedMainClassCode.toString());
		sourceFileInfo.setSourceFileImports(sourceFileImports);
		
		return sourceFileInfo;
    }
    
    private GenerateFileType getGeneratedFileType(String fileName) {
    	
    	String fileNameWithoutExtension = fileName.substring(0, fileName.indexOf(".")); 
    	
    	if (fileNameWithoutExtension.endsWith(ClassGenerator.DS_SCHEMA_CLASS_NAME_SUFFIX)) {
    		return GenerateFileType.DSSchema;
    	} else if ( fileNameWithoutExtension.endsWith(ClassGenerator.DATA_OBJECT_CLASS_NAME_SUFFIX)) {
    		return GenerateFileType.DataObject;
    	} else if (fileNameWithoutExtension.endsWith(ClassGenerator.DAO_SCHEMA_CLASS_NAME_SUFFIX)) {
    		return GenerateFileType.DaoSchema;
    	} 
    	
    	return GenerateFileType.DataType;
    }
    
    /**
     * Class stores all(in the sources folder) generated files data.
     * 
     * @author TVH Group NV
     */
    public static class GeneratedFilesInfo {
    	
    	/**
		 * Map having dataset id's as keys and
		 *  DSSchema sourfe file information as values.
		 */
    	private Map<String, SourceFileInfo> datasetSchemaGeneratedFilesMap;
    	
    	/**
    	 * Map using dataset id's as keys and another map as value having
    	 *  daoId's as keys. The second map has a 3rd Map as value.
    	 *  Last map uses source file type as key and source file info as value.</p>
    	 *  
    	 * Map structure represents the relationship of  datasetId has multiple daoIds, and for
    	 *  each daoId we have 3 separate generated source files (Schema, DataObject and DataType).</p>    	 
    	 */
    	private Map<String, Map<String, Map<GenerateFileType,SourceFileInfo>>> datasetDaoGeneratedFilesMap;  
    	
		public GeneratedFilesInfo(
				Map<String, Map<String, Map<GenerateFileType, SourceFileInfo>>> datasetDaoGeneratedFilesMap,
				Map<String, SourceFileInfo> datasetSchemaGeneratedFilesMap) {
			
			super();
			this.datasetDaoGeneratedFilesMap = datasetDaoGeneratedFilesMap;
			this.datasetSchemaGeneratedFilesMap = datasetSchemaGeneratedFilesMap;
		}

		/**
		 * @return the datasetDaoGeneratedFilesMap
		 */
		public Map<String, Map<String, Map<GenerateFileType, SourceFileInfo>>> getDatasetDaoGeneratedFilesMap() {
			return datasetDaoGeneratedFilesMap;
		}

		/**
		 * @return the datasetSchemaGeneratedFilesMap
		 */
		public Map<String, SourceFileInfo> getDatasetSchemaGeneratedFilesMap() {
			return datasetSchemaGeneratedFilesMap;
		}
	}
}
