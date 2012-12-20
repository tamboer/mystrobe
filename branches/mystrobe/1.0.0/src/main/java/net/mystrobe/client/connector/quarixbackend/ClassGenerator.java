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
 package net.mystrobe.client.connector.quarixbackend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.mystrobe.client.DataObjectAdaptor;
import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDSRelation;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataSource;
import net.mystrobe.client.SchemaColumnProperties;
import net.mystrobe.client.connector.Config;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.IConfig;
import net.mystrobe.client.connector.IServerConnector;
import net.mystrobe.client.connector.QuarixServerConnector;
import net.mystrobe.client.connector.quarixbackend.generate.GenerateFileType;
import net.mystrobe.client.connector.quarixbackend.generate.GeneratedFilesDataLoader;
import net.mystrobe.client.connector.quarixbackend.generate.SourceFileInfo;
import net.mystrobe.client.connector.quarixbackend.generate.GeneratedFilesDataLoader.GeneratedFilesInfo;
import net.mystrobe.client.impl.DAOSchema;
import net.mystrobe.client.impl.DSSchema;
import net.mystrobe.client.util.StringUtil;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author TVH Group NV
 */
public class ClassGenerator {

	private static final String TEMPLATES_DIR = System.getProperty("user.dir") + File.separator + "templates" + File.separator;
	
	private static final StringTemplateGroup TEMPLATES = new StringTemplateGroup("SourceGeneratorTemplates", TEMPLATES_DIR, DefaultTemplateLexer.class);
    
	private static String PKG_POSTFIX = "datatypes";
	
	private static final String TEST_PACKAGE = "databeans.test";

    //private static String PKG_NAME = "";//"com.tvh.prodsearch.databeans";// ClassGenerator.class.getPackage()
                                                                    // .getName()
                                                                    // + "." +
                                                                    // PKG_POSTFIX;

    public static final String DS_SCHEMA_CLASS_NAME_SUFFIX = "DSSchema";
    public static final String DATA_OBJECT_CLASS_NAME_SUFFIX = "DataObject";
    public static final String DAO_SCHEMA_CLASS_NAME_SUFFIX = "Schema";
    
    public static final String BEANS_PACKAGE_NAME = "beansPkg";
    public static final String DAOS_PACKAGE_NAME = "daoPkg";
    public static final String META_PACKAGE_NAME = "metaPkg";
    
    private static String destinationFolder = System.getProperty("user.dir")
            + File.separator + "src" + File.separator + PKG_POSTFIX;
    
    private static Logger logger = LoggerFactory.getLogger(ClassGenerator.class);

    private static String appName;

    private static String dsName;

    private static String doName;

   /**
     * Generate data object class name prefixes using user input values 
     *  for arguments prefix or daoId. 
     * 
     * @param dsSchema Data set schema.
     * @param daoIdsClassNamesPrefixMap 'Daoid' prefixes from command arguments. 
     * @param dsSchemaClassNameParameterPrefix 'Prefix' command argument value. 
     * @param dsSchemaClassNameGeneratedPrefix Ds schema generated prefix.
     * 
     * @return Map of generated dao object class name prefixes. Dao id used as keys in map. 
     */
    public static Map<String, String> genearteDAOClassesNamePrefix(IDSSchema dsSchema, Map<String, String> daoIdsClassNamesPrefixMap,
    		String dsSchemaClassNameParameterPrefix, String dsSchemaClassNameGeneratedPrefix) {
    
    	Enumeration<String> enu = dsSchema.getDataObjectIds();
        
        Map<String, String> dataObjectClassNamesGeneratedPrefixes = new HashMap<String, String>(dsSchema.getDataObjectsCount());  
        
        //build all data object classes prefixes 
        while (enu.hasMoreElements()) {

            ClassGenerator.doName = enu.nextElement();
            
            IDAOSchema daoSchema = dsSchema.getDataObjectSchema(ClassGenerator.doName);
            
            String dataObjectClassNamesPrefix =  daoIdsClassNamesPrefixMap != null ? daoIdsClassNamesPrefixMap.get(daoSchema.getDAOId()) : null;
            
            //apply data object class name prefix rules 
            if (dataObjectClassNamesPrefix == null) {
            	if (dsSchema.getDataObjectsCount() == 1 && dsSchemaClassNameParameterPrefix != null) {
            		dataObjectClassNamesPrefix = dsSchemaClassNameParameterPrefix;
            	} else {
            		dataObjectClassNamesPrefix = dsSchemaClassNameGeneratedPrefix + generateClassName(daoSchema);
            	}
            }
            
            logger.info("Data object class name prefix:" + dataObjectClassNamesPrefix);
            dataObjectClassNamesGeneratedPrefixes.put(daoSchema.getDAOId(), dataObjectClassNamesPrefix);
        }
        
        return dataObjectClassNamesGeneratedPrefixes;
    }
    
    /**
     * Method looks for already generated cod for daoIds of current dataset. 
     * 
     * @param dsSchema Current class generation ds schema.
     * @param generatedFilesInfo Already generated classes info.
     * @return Map of daoids and <tt>SourceFileInfo</tt> for daoIds which were already generated.   
     */
    public static Map<String, SourceFileInfo> findAlreadyGeneratedDAOs(IDSSchema dsSchema, GeneratedFilesInfo generatedFilesInfo) {
    
    	if (generatedFilesInfo == null || generatedFilesInfo.getDatasetDaoGeneratedFilesMap() == null ||
    			generatedFilesInfo.getDatasetDaoGeneratedFilesMap().isEmpty()) {
    		return null;
    	}
    	
    	Map<String, SourceFileInfo> alreadyGeneratedDAOsMap = new HashMap<String, SourceFileInfo>();
    	
    	Map<String, Map<String, Map<GenerateFileType, SourceFileInfo>>> daoGeneratedFilesMap = generatedFilesInfo.getDatasetDaoGeneratedFilesMap();

		Enumeration<String> enu = dsSchema.getDataObjectIds();
		
		while (enu.hasMoreElements()) {

			boolean found = false;
			
			String daoId = enu.nextElement();
			
			// Go over all generated dataset information
			for (Iterator<String> dsIdIterator = daoGeneratedFilesMap.keySet()
					.iterator(); dsIdIterator.hasNext();) {
				String dsId = dsIdIterator.next();
				if (!dsId.equals(dsSchema.getId())) {
					
					Map<String, Map<GenerateFileType, SourceFileInfo>> generatedDatasetDaosMap = daoGeneratedFilesMap.get(dsId);

					// go over generated daos of different datasets
					for (Iterator<String> daoIdIterator = generatedDatasetDaosMap.keySet().iterator(); daoIdIterator.hasNext();) {
						String generatedDaoId = daoIdIterator.next();
						if (daoId.equals(generatedDaoId)) {
							found = true;
							alreadyGeneratedDAOsMap.put(daoId, generatedDatasetDaosMap.get(daoId).get(GenerateFileType.DaoSchema));
							break;
						}
					}
				} 
				
				if (found) {
					break;
				}
			}
		}

		return alreadyGeneratedDAOsMap;
    }
   
    /**
     * Generate package folders for the new files.<br/>
     * 
     * <p>Package names can be fully specified for each class using -daoPkg/-beansPkg/metaPkg or
     *  the -pkg option can be used as prefix for all other package names.<p/>
     *  
     * When no package option is specified the default test package is used.       
     * 
     * 
     * @param pckName
     * @param packageNames
     * @param dstFolder
     * @return
     */
    public static Map<String, String> generatePackageFolders(String pckName, Map<String, String> packageNames, String dstFolder) {
    	
    	File dstFolderFile = new File(!StringUtil.isNullOrEmpty(dstFolder) ? dstFolder : destinationFolder );	
        
    	String destinationFolderPath = null;
    	if (!dstFolderFile.exists() && !dstFolderFile.mkdirs()) {
            logger.error("Unable to create destination folder: " + dstFolder
                    + " exiting");
            System.exit(1);
        } else {
        	destinationFolderPath = dstFolderFile.getAbsolutePath();
        }
    	
    	Map<String, String> filePaths = new HashMap<String, String>(3);
    	
    	String dsSchemaPath = null;
    	
        if (packageNames.containsKey(META_PACKAGE_NAME)) {
        	if (!StringUtil.isNullOrEmpty(pckName)) {
        		String newPackageName = pckName + "." + packageNames.get(META_PACKAGE_NAME); 
        		packageNames.put(META_PACKAGE_NAME, newPackageName);
        		dsSchemaPath = createPackage(newPackageName, destinationFolderPath);
        	} else {
        		dsSchemaPath = createPackage(packageNames.get(META_PACKAGE_NAME), destinationFolderPath);
        	}
        } else if (!StringUtil.isNullOrEmpty(pckName)) {
        	dsSchemaPath = createPackage(pckName, destinationFolderPath);
        	packageNames.put(META_PACKAGE_NAME, pckName);
        } else {
        	logger.debug("Please specify generic or specific (bean, dao, meta) package name(s).");
        	dsSchemaPath = createPackage(TEST_PACKAGE, destinationFolderPath);
        	packageNames.put(META_PACKAGE_NAME, TEST_PACKAGE);
		}
        filePaths.put(META_PACKAGE_NAME, dsSchemaPath);
        
        String beansPath = null;
        if (packageNames.containsKey(BEANS_PACKAGE_NAME)) {
        	if (!StringUtil.isNullOrEmpty(pckName)) {
        		String newPackageName = pckName + "." + packageNames.get(BEANS_PACKAGE_NAME); 
        		packageNames.put(BEANS_PACKAGE_NAME, newPackageName);
        		beansPath = createPackage(newPackageName, destinationFolderPath);
        	} else {
        		beansPath = createPackage(packageNames.get(BEANS_PACKAGE_NAME), destinationFolderPath);
        	}
        } else if (!StringUtil.isNullOrEmpty(pckName)) {
        	beansPath = createPackage(pckName, destinationFolderPath);
        	packageNames.put(BEANS_PACKAGE_NAME, pckName);
        } else {
        	logger.debug("Please specify generic or specific (bean, dao, meta) package name(s).");
        	beansPath = createPackage(TEST_PACKAGE, destinationFolderPath);
        	packageNames.put(BEANS_PACKAGE_NAME, TEST_PACKAGE);
        }
        filePaths.put(BEANS_PACKAGE_NAME, beansPath);
        
        String daoPath = null;
        if (packageNames.containsKey(DAOS_PACKAGE_NAME)) {
        	if (!StringUtil.isNullOrEmpty(pckName)) {
        		String newPackageName = pckName + "." + packageNames.get(DAOS_PACKAGE_NAME); 
        		packageNames.put(DAOS_PACKAGE_NAME, newPackageName);
        		daoPath = createPackage(newPackageName, destinationFolderPath);
        	} else {
        		daoPath = createPackage(packageNames.get(DAOS_PACKAGE_NAME), destinationFolderPath);
        	}
        } else if (!StringUtil.isNullOrEmpty(pckName)) {
        	daoPath = createPackage(pckName, destinationFolderPath);
        	packageNames.put(DAOS_PACKAGE_NAME, pckName);
        
        } else {
        	logger.debug("Please specify generic or specific (bean, dao, meta) package name(s).");
        	daoPath = createPackage(TEST_PACKAGE, destinationFolderPath);
        	packageNames.put(DAOS_PACKAGE_NAME, TEST_PACKAGE);
        }
        filePaths.put(DAOS_PACKAGE_NAME, daoPath);
        
        return filePaths;
        
    }

    public static void generate(IConfig config, Map<String, String> packageNames, Map<String, String> filePaths,
    		String appName, String dsName, String dsSchemaClassNameParameterPrefix, 
    		Map<String, String> daoIdsClassNamesPrefixMap,
            GeneratedFilesInfo generatedFilesInfo) {

        ClassGenerator.appName = appName;
        ClassGenerator.dsName = dsName;
        
        IServerConnector srvConnector = new QuarixServerConnector(config);
        IAppConnector appConnector = srvConnector.getAppConnector(appName);

        IDSSchema dsSchema = appConnector.getSchema(dsName);
        if (dsSchema == null) {
            logger.error("No schema found for: " + dsName);
            System.exit(1);
            return;
        }

        String dsSchemaClassNameGeneratedPrefix = dsSchemaClassNameParameterPrefix == null ? generateDSSchemaClassNamePrefix(dsSchema)
        		: dsSchemaClassNameParameterPrefix;
        
        logger.debug("DsSchema class name prefix:" + dsSchemaClassNameGeneratedPrefix);
        
        Map<String, String> dataObjectClassNamesGeneratedPrefixes = genearteDAOClassesNamePrefix(dsSchema, daoIdsClassNamesPrefixMap,
        		dsSchemaClassNameParameterPrefix, dsSchemaClassNameGeneratedPrefix);
        
        Map<String, SourceFileInfo> alreadyGeneratedDAOIds =  findAlreadyGeneratedDAOs(dsSchema, generatedFilesInfo);
        
        SourceFileInfo dsSchemaSourceFileInfo = null;
        if (generatedFilesInfo != null) {
        	dsSchemaSourceFileInfo  = generatedFilesInfo.getDatasetSchemaGeneratedFilesMap().get(dsSchema.getId());
        }
        
        String dsClassName = generateDSSchema(filePaths.get(META_PACKAGE_NAME), packageNames, dsSchema, dsSchemaClassNameGeneratedPrefix, dataObjectClassNamesGeneratedPrefixes, dsSchemaSourceFileInfo, alreadyGeneratedDAOIds);

        Enumeration<String> enu = dsSchema.getDataObjectIds();
        
        while (enu.hasMoreElements()) {

            String daoId = enu.nextElement();
            
            if (alreadyGeneratedDAOIds != null && alreadyGeneratedDAOIds.containsKey(daoId)) {
            	logger.info("Classes already generated for daoId : " + daoId + "," + 
            			" existing class name:" + alreadyGeneratedDAOIds.get(daoId).getClassName() );
            	continue;
            }
            
            IDAOSchema daoSchema = dsSchema.getDataObjectSchema(daoId);
            
            String dataObjectClassNamesPrefix =  dataObjectClassNamesGeneratedPrefixes.get(daoSchema.getDAOId());
            
            SourceFileInfo daoSchemaSourceFileInfo = null;
            SourceFileInfo dataObjectSourceFileInfo = null;
            SourceFileInfo dataTypeSourceFileInfo = null;

            if (generatedFilesInfo != null && 
            		generatedFilesInfo.getDatasetDaoGeneratedFilesMap().containsKey((dsSchema.getId()))) {
            	
            	Map<String, Map<GenerateFileType, SourceFileInfo>> datasetDaoGeneratedFilesMap = generatedFilesInfo.getDatasetDaoGeneratedFilesMap().get((dsSchema.getId()));
            	
            	if (datasetDaoGeneratedFilesMap != null && datasetDaoGeneratedFilesMap.containsKey(daoSchema.getDAOId())) {
            		Map<GenerateFileType, SourceFileInfo> daosourceFilesInfoMap  = datasetDaoGeneratedFilesMap.get(daoSchema.getDAOId());
            		
            		daoSchemaSourceFileInfo = daosourceFilesInfoMap.get(GenerateFileType.DaoSchema);
            		dataObjectSourceFileInfo = daosourceFilesInfoMap.get(GenerateFileType.DataObject);
            		dataTypeSourceFileInfo = daosourceFilesInfoMap.get(GenerateFileType.DataType);
            	}
            }
            
            // generate DAO Schema
            generateDAOSchema(filePaths.get(META_PACKAGE_NAME), packageNames, dsSchema, daoSchema,
                    dsClassName, dataObjectClassNamesPrefix, daoSchemaSourceFileInfo);

            // generate data object class
            generateDataObject(filePaths.get(DAOS_PACKAGE_NAME), packageNames, dsSchema, daoSchema,
                    dsClassName, dataObjectClassNamesPrefix, dataObjectSourceFileInfo);

            // generate data type class
            generateDataType(filePaths.get(BEANS_PACKAGE_NAME), packageNames, dsSchema, daoSchema, dataObjectClassNamesPrefix, dataTypeSourceFileInfo);
        }
    }

    private static String generateDSSchema(String filePath, Map<String, String> packageNames, IDSSchema dsSchema, String dsSchemaClassNamePrefix,
    		Map<String, String> dataObjectClassNamesGeneratedPrefixes, SourceFileInfo dsSchemaSourceFileInfo, Map<String, SourceFileInfo> alreadyGeneratedDaos) {

        String dsSchemaclassName = dsSchemaClassNamePrefix + DS_SCHEMA_CLASS_NAME_SUFFIX;

        // checking the lock mechanism
//        if (isLocked(dsSchemaclassName)) {
//            return dsSchemaclassName;
//        }

        // generate the class body
        String content = generateDSSchemaClassFile(dsSchemaclassName, dsSchema,
        		dsSchemaClassNamePrefix, dataObjectClassNamesGeneratedPrefixes,
        		dsSchemaSourceFileInfo, alreadyGeneratedDaos, packageNames );

        writeClassFile(filePath, dsSchemaclassName + ".java", content);
        
        return dsSchemaclassName;
    }

    private static void generateDAOSchema(String filePath, Map<String, String> packageNames, IDSSchema dsSchema,
            IDAOSchema daoSchema, String dsClassName, String classNamePrefix,
            SourceFileInfo sourceFileInfo) {

        String className = generateDAOSchemaClassName(classNamePrefix);

        // checking the lock mechanism
//        if (isLocked(className)) {
//            return;
//        }

        // generate the class body
        String content = generateDAOSchemaClassFile(className, dsSchema,
                daoSchema, dsClassName, classNamePrefix, sourceFileInfo, packageNames);

        // writing out to file
        writeClassFile(filePath, className + ".java" , content);

    }

    private static void generateDataObject(String filePath, Map<String, String> packageNames, IDSSchema dsSchema,
            IDAOSchema daoSchema, String dsClassName, String classNamePrefix, SourceFileInfo sourceFileInfo) {

        String className = generateDataObjectClassName( classNamePrefix );

        // checking the lock mechanism
//        if (isLocked(className)) {
//            return;
//        }

        // generate the class body
        String content = generateDataObjectClassFile(className, dsSchema,
                daoSchema, dsClassName, classNamePrefix, sourceFileInfo, packageNames);

        // writing out to file
        writeClassFile(filePath, className + ".java", content);
    }

    private static void generateDataType(String filePath, Map<String, String> packageNames, IDSSchema dsSchema,
            IDAOSchema daoSchema, String classNamePrefix, SourceFileInfo sourceFileInfo) {

        String className = classNamePrefix;

        // checking the lock mechanism
//        if (isLocked(className)) {
//            return;
//        }

        // generate the class body
        String content = generateClassFile(className, dsSchema, daoSchema, sourceFileInfo, packageNames);

        // writing out to file
        writeClassFile(filePath, className + ".java", content);
    }

    private static String generateClassName(IDAOSchema daoSchema) {

    	String doId = daoSchema.getDAOId();
        if (doId.startsWith("tt-")) {
            doId = doId.substring(3);
        }
        doId = doId.substring(0, 1).toUpperCase() + doId.substring(1);
        
        logger.debug("Generated class name as no prefix was set:" + doId);
        
        return getLegalName(doId, "DataBean");
    }

    private static String generateDSSchemaClassNamePrefix(IDSSchema dsSchema) {

    	String dsId = dsSchema.getId();
        if (dsId.startsWith("ds")) {
            dsId = dsId.substring(2);
        }
        dsId = dsId.substring(0, 1).toUpperCase() + dsId.substring(1);
        
        logger.debug("Generated ds schema class name as no prefix was set:" + dsId);
        
        return getLegalName(dsId, "Schema");
    	
    }
    
    private static String generateDataObjectClassName(String classNamePrefix) {
        return classNamePrefix + DATA_OBJECT_CLASS_NAME_SUFFIX;
    }

    private static String generateDAOSchemaClassName(String classNamePrefix) {
        return classNamePrefix + DAO_SCHEMA_CLASS_NAME_SUFFIX;
    }

    /**
     * Filtering out illegal characters from field names
     * 
     * @param fieldName
     * @return java-like field name
     */
    private static String getLegalName(String fieldName, String defaultName) {
        if (fieldName == null)
            return defaultName;

        String field = "";

        for (int i = 0; i < fieldName.length(); i++) {
            if (Character.isJavaIdentifierPart(fieldName.charAt(i)))
                field += fieldName.charAt(i);
        }

        if (field.length() == 0)
            field = defaultName;
        return field;
    }
    
    private static void insertUserNotGeneratedCode(StringTemplate classST, SourceFileInfo sourceFileInfo) {
    	
    	if (sourceFileInfo != null) {
    		classST.setAttribute("userMainClassCode", sourceFileInfo.getMainClassNotGeneratedCode());
        	classST.setAttribute("userAfterMainClassCode", sourceFileInfo.getAfterMainClassNotGeneratedCode());
    	} else {
    		classST.setAttribute("userMainClassCode", StringUtil.EMPTY_STRING);
        	classST.setAttribute("userAfterMainClassCode", StringUtil.EMPTY_STRING);
    	}
    }
    
    /**
     * Merge generated imports with old source file imports.
     * 
     * @param imports Generated source file imports.
     * @param oldSourceImports Old source file imports list.
     * 
     * @return Merged generated and user imports list.
     */
    private static String[] buildClassImportsArray(String[] imports, List<String> oldSourceImports) {
    	
    	Set<String> importsSet = new HashSet<String>(Arrays.asList(imports));
    	
    	importsSet.addAll(oldSourceImports);
    	
    	return importsSet.toArray(new String [importsSet.size()]);
    	
    }

    private static String generateDataObjectClassFile(String className,
            IDSSchema dsSchema, IDAOSchema daoSchema, String dsClassName, String classNamePrexif,
            SourceFileInfo dataObjectSourceFileInfo, Map<String, String> filesPackages) {

        List<StringTemplate> propList = new ArrayList<StringTemplate>();
        Collection<String> columnNames = daoSchema.getColumnNames();
        for (String column : columnNames) {

            StringTemplate properties = TEMPLATES.getInstanceOf("properties");
            properties.setAttribute("name", getLegalName(column, "prop"));
            properties.setAttribute("column", column);

            Map<SchemaColumnProperties, String> props = daoSchema
                    .getColumnProperties(column);
            for (SchemaColumnProperties prop : props.keySet()) {
                properties.setAttribute(prop.name(), props.get(prop));
            }

            propList.add(properties);
        }

        StringTemplate body = TEMPLATES.getInstanceOf("dataObjectBody");
        body.setAttribute("type", "IDAOSchema");
        body.setAttribute("daoSchemaTypeGenerics", classNamePrexif);
        body.setAttribute("name", "getSchema");
        body.setAttribute("serialVersionUID", "" + System.currentTimeMillis());
        body.setAttribute("dsSchema", dsClassName);
        body.setAttribute("daoId", daoSchema.getDAOId());

        StringTemplate classST = TEMPLATES.getInstanceOf("class");
        classST.setAttribute("packageName", filesPackages.get(DAOS_PACKAGE_NAME));
        
        String [] classImports = new String[] { DataObjectAdaptor.class.getName(),
                IDAOSchema.class.getName(),
                Serializable.class.getName(),
                GeneratorMeta.class.getName(),
                Generated.class.getName()};
        
        String daoSChemaClassName = generateDAOSchemaClassName(classNamePrexif);
        Set<String> additionalImports = new HashSet<String>();
        if (!filesPackages.get(DAOS_PACKAGE_NAME).equals(filesPackages.get(META_PACKAGE_NAME))) {
        	additionalImports.add(filesPackages.get(META_PACKAGE_NAME) + "." + dsClassName);
        	additionalImports.add(filesPackages.get(META_PACKAGE_NAME) + "." + daoSChemaClassName);
        }
        
        if (!filesPackages.get(DAOS_PACKAGE_NAME).equals(filesPackages.get(BEANS_PACKAGE_NAME))) {
        	additionalImports.add(filesPackages.get(BEANS_PACKAGE_NAME) + "." + classNamePrexif);
        }
        
        if (dataObjectSourceFileInfo != null ||  !additionalImports.isEmpty() ) {
        	List<String> additionalImportsList = new ArrayList<String>(additionalImports);
        	
        	if (dataObjectSourceFileInfo != null) {
        		additionalImportsList.addAll(dataObjectSourceFileInfo.getSourceFileImports());
        	}
        	classImports = buildClassImportsArray(classImports, additionalImportsList);
        }
        classST.setAttribute("import", classImports);
                	
        classST.setAttribute("comment", getBodyComment());
        classST.setAttribute("annotation", getAnnotation(dsSchema, daoSchema));
        classST.setAttribute("className", className);
        classST.setAttribute("daoClassName", daoSChemaClassName);
        classST.setAttribute("extends", "DataObjectAdaptor");
        classST.setAttribute("extendsGenerics", classNamePrexif);
        classST.setAttribute("implements", "Serializable");
        classST.setAttribute("body", body);	
        
        insertUserNotGeneratedCode(classST, dataObjectSourceFileInfo);

        return classST.toString();
    }

    private static String generateDAOSchemaClassFile(String className,
            IDSSchema dsSchema, IDAOSchema daoSchema, String dsClassName, String classNamePrefix,
            SourceFileInfo daoSchemaSourceFileInfo, Map<String, String> filesPackages) {
    	
    	List<StringTemplate> columnsEnum = new ArrayList<StringTemplate>();
    	Collection<String> columnNames = daoSchema.getColumnNames();
    	
    	for (String column : columnNames) {
    		
    		if (IDataBean.ROW_ID_FIELD_NAME.equals(column) ||
    				IDataBean.ROW_STATE_FIELD_NAME.equals(column)) {
    			continue;
    		}
    		
    		StringTemplate columnNameEnumProperties = TEMPLATES.getInstanceOf("columnNamesEnum");
    		columnNameEnumProperties.setAttribute("columnEnumId", getLegalName(column, "prop").toUpperCase());
    		columnNameEnumProperties.setAttribute("column", column);
    		columnsEnum.add(columnNameEnumProperties);
    	}
        
    	
    	List<StringTemplate> propList = new ArrayList<StringTemplate>();
        for (String column :  columnNames) {

            StringTemplate properties = TEMPLATES.getInstanceOf("properties");
            properties.setAttribute("name", getLegalName(column, "prop"));
            properties.setAttribute("column", column);

            Map<SchemaColumnProperties, String> props = daoSchema
                    .getColumnProperties(column);
            for (SchemaColumnProperties prop : props.keySet()) {
                properties.setAttribute(prop.name(), props.get(prop));
            }

            propList.add(properties);
        }

        StringTemplate body = TEMPLATES.getInstanceOf("daoSchemaBody");
        body.setAttribute("name", className);
        body.setAttribute("dsSchema", dsClassName);
        body.setAttribute("daoId", daoSchema.getDAOId());
        body.setAttribute("iDataTypeClass",	classNamePrefix + ".class");
        body.setAttribute("batchSize", daoSchema.getBatchSize() > 0 ? daoSchema.getBatchSize() : 50);
        body.setAttribute("margin", daoSchema.getMargin());
        body.setAttribute("isAutosync", daoSchema.isAutosync());
        body.setAttribute("isOpenOnInit", daoSchema.isOpenOnInit());
        body.setAttribute("isReadOnly", daoSchema.isReadOnly());
        body.setAttribute("isDynamic", daoSchema.isDynamic());
        body.setAttribute("isSendChangesOnly", daoSchema.isSendChangesOnly());
        body.setAttribute("isSetFilterEveryTime",
                daoSchema.isSetFilterEveryTime());
        body.setAttribute("properties", propList.toArray());
        body.setAttribute("columnsEnum", columnsEnum.toArray());

        StringTemplate classST = TEMPLATES.getInstanceOf("class");
        classST.setAttribute("packageName", filesPackages.get(META_PACKAGE_NAME));
        
        String [] classImports = new String[] { Map.class.getName(), HashMap.class.getName(),
                LinkedHashMap.class.getName(),
                DAOSchema.class.getName(),
                SchemaColumnProperties.class.getName(),
                GeneratorMeta.class.getName(),
                Generated.class.getName()};
        
        Set<String> additionalImports = new HashSet<String>();
        if (!filesPackages.get(META_PACKAGE_NAME).equals(filesPackages.get(BEANS_PACKAGE_NAME))) {
        	additionalImports.add(filesPackages.get(BEANS_PACKAGE_NAME) + "." + classNamePrefix);
        }
        
        if (daoSchemaSourceFileInfo != null || !additionalImports.isEmpty()) {
        	List<String> additionalImportsList = new ArrayList<String>(additionalImports);
        	if (daoSchemaSourceFileInfo != null) {
        		additionalImportsList.addAll(daoSchemaSourceFileInfo.getSourceFileImports());
        	}
        	classImports = buildClassImportsArray(classImports, additionalImportsList);
        }
        classST.setAttribute("import", classImports);
        
        classST.setAttribute("comment", getBodyComment());
        classST.setAttribute("annotation", getAnnotation(dsSchema, daoSchema));
        classST.setAttribute("className", className);
        classST.setAttribute("extends", "DAOSchema");
        classST.setAttribute("extendsGenerics", classNamePrefix);
        classST.setAttribute("body", body);
        
        insertUserNotGeneratedCode(classST, daoSchemaSourceFileInfo);
        
        return classST.toString();
    }

    private static String generateDSSchemaClassFile(String className, IDSSchema dsSchema,
    		String beanPrefixName, Map<String, String> dataObjectClassNamesGeneratedPrefixes,
    		SourceFileInfo dsSchemaSourceFileInfo, Map<String, SourceFileInfo> alreadyGeneratedDaos, Map<String, String> filePackages) {
    	logger.debug( "Start generate ds schema" );
        String relations = "";
        Enumeration<IDSRelation> rels = dsSchema.getRelations();
        while (rels.hasMoreElements()) {
            IDSRelation dsRel = rels.nextElement();
            
            String childColumnName = dsRel.getChildColumnName().replaceAll("&quot;", ""); 
            childColumnName = childColumnName.replaceAll("\"", "");
            
            String childDaoId = dsRel.getChildDAOId().replaceAll("&quot;", ""); 
            childDaoId = childDaoId.replaceAll("\"", "");
            
            String parentColumnName = dsRel.getParentColumnName().replaceAll("&quot;", ""); 
            parentColumnName = parentColumnName.replaceAll("\"", ""); 
            
            String parentDaoId = dsRel.getParentDAOId().replaceAll("&quot;", ""); 
            parentDaoId = parentDaoId.replaceAll("\"", "");
            
            relations += "\"" + childColumnName + ","
                    + childDaoId + "," + parentColumnName
                    + "," + parentDaoId + "\"" + ", ";
        }
        relations = relations.trim();
        if (relations.endsWith(",")) {
            relations = relations.substring(0, relations.length() - 1);
        }

        List<String> pairList = new ArrayList<String>();
        Enumeration<String> idsList = dsSchema.getDataObjectIds();
        List<String> alreadyGeneratedDaosImorts = new ArrayList<String>();
        
        while (idsList.hasMoreElements()) {
            
        	String id = idsList.nextElement();
            String daoSchemaClassName;
            
            if (alreadyGeneratedDaos != null && alreadyGeneratedDaos.containsKey(id)) {
            	
            	SourceFileInfo alredayGeneratedDAOInfo = alreadyGeneratedDaos.get(id);
            	daoSchemaClassName = alredayGeneratedDAOInfo.getClassName();
            	
            	if (!alredayGeneratedDAOInfo.getPackageName().equals(filePackages.get(META_PACKAGE_NAME))) {
            		alreadyGeneratedDaosImorts.add(alredayGeneratedDAOInfo.getPackageName()+ "." + alredayGeneratedDAOInfo.getClassName());
            	}
            } else {
            	String classNamePrefix = dataObjectClassNamesGeneratedPrefixes.get(id);
            	daoSchemaClassName = generateDAOSchemaClassName(classNamePrefix);
            }
            
            pairList.add("\""
                    + id
                    + "\", new "
                    + daoSchemaClassName + "()");
        }

        StringTemplate body = TEMPLATES.getInstanceOf("dsSchemaBody");
        body.setAttribute("name", className);
        body.setAttribute("id", dsSchema.getId());
        body.setAttribute("batchSize", dsSchema.getBatchSize());
        body.setAttribute("margin", dsSchema.getMargin());
        body.setAttribute("urn", dsSchema.getURN());
        body.setAttribute("hasAtomicChanges", dsSchema.hasAtomicChanges());
        body.setAttribute("hasFilteredChildren", dsSchema.hasFilteredChildren());
        body.setAttribute("isDynamic", dsSchema.isDynamic());
        body.setAttribute("isReadOnly", dsSchema.isReadOnly());
        body.setAttribute("relations", relations);
        body.setAttribute("daoMap", pairList.toArray());

        StringTemplate classST = TEMPLATES.getInstanceOf("class");
        classST.setAttribute("packageName", filePackages.get(META_PACKAGE_NAME));
        
        String [] classImports = new String[] { Collections.class.getName(),
                DSSchema.class.getName(), GeneratorMeta.class.getName(),
                Generated.class.getName()};
        
        if (dsSchemaSourceFileInfo != null || !alreadyGeneratedDaosImorts.isEmpty() ) {
        	if (dsSchemaSourceFileInfo != null) {
        		alreadyGeneratedDaosImorts.addAll(dsSchemaSourceFileInfo.getSourceFileImports());
        	}
        	classImports = buildClassImportsArray(classImports, alreadyGeneratedDaosImorts);
        }
        
        classST.setAttribute("import", classImports);
       
        classST.setAttribute("comment", getBodyComment());
        classST.setAttribute("annotation", getAnnotation(dsSchema, null));
        classST.setAttribute("className", className);
        classST.setAttribute("extends", "DSSchema");
        classST.setAttribute("body", body);
        
        insertUserNotGeneratedCode(classST, dsSchemaSourceFileInfo);
        
        logger.debug( "End generate ds schema" );
        
        return classST.toString();
    }

    private static String generateClassFile(String className,
            IDSSchema dsSchema, IDAOSchema daoSchema, SourceFileInfo sourceFileInfo, Map<String, String> filePackages) {

        String idField = null;
        String javaField = null;

        List<StringTemplate> fields = new ArrayList<StringTemplate>();
        List<StringTemplate> methods = new ArrayList<StringTemplate>();
        Collection<String> columns = daoSchema.getColumnNames();

        boolean dateUsed = false;
        boolean bigDecimalUsed = false;
        boolean bigIntegerUsed = false; 
        
        for (String column : columns) {

            StringTemplate field = TEMPLATES.getInstanceOf("field");
            StringTemplate setMethod = TEMPLATES.getInstanceOf("setterMethod");
            StringTemplate getMethod = TEMPLATES.getInstanceOf("getterMethod");

            javaField = getLegalName(column, "column");
            String type = (String) daoSchema.getColumnProperties(column).get(
                    SchemaColumnProperties.Type);

            // string|character|integer|decimal|logical
            if (type == null || type.equalsIgnoreCase("string")
                    || type.equalsIgnoreCase("character")) {
                type = "String";
            } else if (type.equalsIgnoreCase("integer")) {
            	 type = "Integer";
            } else if (type.equalsIgnoreCase("int64")) {
            	 type = "BigInteger"; 
            	 bigIntegerUsed = true;
            } else if (type.equalsIgnoreCase("decimal")) {
                bigDecimalUsed = true;
            	type = "BigDecimal";
            } else if (type.equalsIgnoreCase("logical")) {
                type = "Boolean";
            } else if (type.equalsIgnoreCase("date")) {
                dateUsed = true;
                type = "Date";
            } else if (type.equalsIgnoreCase("clob")) {
                type = "byte []";
            } else if (type.equalsIgnoreCase("datetime") || type.startsWith("datetime")) {
                // TODO resolve this is maybe Gregorian calendar
            	dateUsed = true;
            	type = "Date";
            }

            if (column.contains("rowid")) {
                idField = column;
                if (!type.equals("String")) {
                    idField = "\"\"+" + idField;
                }
            }

            field.setAttribute("type", type);
            field.setAttribute("name", javaField);
            fields.add(field);

            String camelField = javaField.substring(0, 1).toUpperCase()
                    + javaField.substring(1);

            // adding setter method
            setMethod.setAttribute("modifier", "public");
            setMethod.setAttribute("type", "void");
            setMethod.setAttribute("name", "set" + camelField);
            StringTemplate param = TEMPLATES.getInstanceOf("parameter");
            param.setAttribute("type", type);
            param.setAttribute("name", javaField);
            setMethod
                    .setAttribute("parameters", new StringTemplate[] { param });
            setMethod.setAttribute("body", "this." + javaField + " = "
                    + javaField + ";");

            methods.add(setMethod);

            // adding getter method
            String getterName = "get" + camelField;
            if (getterName.equalsIgnoreCase("getRowId")
                    || getterName.equalsIgnoreCase("getDataSource")) {
                // this is an inherited method, it added later
            } else {
                getMethod.setAttribute("modifier", "public");
                getMethod.setAttribute("type", type);
                getMethod.setAttribute("name", getterName);
                getMethod.setAttribute("return", javaField);

                methods.add(getMethod);
            }

        }

        List<String> imports = new ArrayList<String>();
        if (dateUsed) {
            imports.add(Date.class.getName());
        }
        
        if (bigDecimalUsed) {
        	imports.add(BigDecimal.class.getName());
        }
        
        if (bigIntegerUsed) {
        	imports.add(BigInteger.class.getName());
        }
        
        imports.add(IDataSource.class.getName());
        imports.add(IDataBean.class.getName());
        imports.add(GeneratorMeta.class.getName());
        imports.add(Generated.class.getName());

        StringTemplate body = TEMPLATES.getInstanceOf("dataTypeBody");
        body.setAttribute("serialVersionUID", System.currentTimeMillis());
        body.setAttribute("rowid", idField == null ? "null" : idField);
        body.setAttribute("fields", fields.toArray());
        body.setAttribute("methods", methods.toArray());

        StringTemplate classST = TEMPLATES.getInstanceOf("class");
        classST.setAttribute("packageName", filePackages.get(BEANS_PACKAGE_NAME));
        
        String [] classImports = imports.toArray(new String[imports.size()]);
        if (sourceFileInfo != null  ) {
        	classImports = buildClassImportsArray(classImports, sourceFileInfo.getSourceFileImports());
        }
        classST.setAttribute("import", classImports);

        classST.setAttribute("comment", getBodyComment());
        classST.setAttribute("annotation", getAnnotation(dsSchema, daoSchema));
        classST.setAttribute("className", className);
        classST.setAttribute("implements", "IDataBean");
        classST.setAttribute("body", body);
        
        insertUserNotGeneratedCode(classST, sourceFileInfo);

        return classST.toString();
    }

    private static StringTemplate getAnnotation(IDSSchema dsSchema,
            IDAOSchema daoSchema) {

        StringTemplate annotation = TEMPLATES.getInstanceOf("annotation");
        annotation.setAttribute("appName", ClassGenerator.appName);
        annotation.setAttribute("urn", dsSchema.getURN());
        annotation.setAttribute("dsName", ClassGenerator.dsName);
        annotation.setAttribute("dsId", dsSchema.getId());

        if (daoSchema != null) {
            annotation.setAttribute("daoId", daoSchema.getDAOId());
        }

        return annotation;
    }

    private static StringTemplate getBodyComment() {

        StringTemplate comment = TEMPLATES.getInstanceOf("comment");
        comment.setAttribute("className", ClassGenerator.class.getName());
        comment.setAttribute("appName", ClassGenerator.appName);
        comment.setAttribute("dsName", ClassGenerator.dsName);
        comment.setAttribute("doName", ClassGenerator.doName);

        return comment;
    }

    private static String createPackage(String packageName, String destinationFolder) {
        String ret = destinationFolder;
        StringTokenizer st = new StringTokenizer(packageName, ".");
        while (st.hasMoreElements()) {
            String folderName = (String) st.nextElement();
            ret += File.separator + folderName;
        }
        File folder = new File(ret);
        if (!folder.exists()) {
            folder.mkdirs();	
        }
        
        logger.debug("Created package path: " + ret);
        
        return ret;
    }

    private static void writeClassFile(String path, String fileName, String content) {
        
    	logger.debug("Generated class file name: " + fileName);
    	
    	if (!GeneratedFilesDataLoader.validateFile(fileName, content)) {
    		//if validation fails stop class generation process
    		logger.debug("Class validation failed for generated class: " + fileName);
          	System.exit(1);
        }
    	
    	Writer writer = null;
        String filePath = path + File.separator + fileName;
    	try {
            File file = new File(filePath);
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
        } catch (FileNotFoundException e) {
            logger.error( "", e);
        } catch (IOException e) {
            logger.error( "", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.error( "", e);
            }
        }
    }

    private static Class<?>[] getClasses(String pkgname)
            throws ClassNotFoundException {

        List<Class<?>> classes = new ArrayList<Class<?>>();

        File directory = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = pkgname.replace('.', '/');
            URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pkgname + " (" + directory
                    + ") does not appear to be a valid package");
        }
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".class")) {
                    classes.add(Class.forName(pkgname + '.'
                            + files[i].substring(0, files[i].length() - 6)));
                }
            }
        } else {
            throw new ClassNotFoundException(pkgname
                    + " does not appear to be a valid package");
        }

        return classes.toArray(new Class[classes.size()]);
    }
    
    protected static GeneratedFilesInfo loadGeneratedFilesInfo (String sourcesPath) throws Exception {
    	
    	File file = new File (sourcesPath);
    	
    	if (!file.isDirectory()) {
    		file = new File(System.getProperty("user.dir") + File.separator + sourcesPath);
    		
    		if (!file.isDirectory()) {
    			logger.error("Can not find already generated sources path. '" + sourcesPath+ "' is not a valid folder path. " );
    			return null;
    		}
    	}
    	
    	GeneratedFilesDataLoader generatedFilesDataLoader = new GeneratedFilesDataLoader(file.getPath());
    	
    	return generatedFilesDataLoader.loadGeneratedFilesData(); 
    	
    }

    protected static void printUsageInformation() {
        System.err.println("Usage: [-server=<server url>] "
                + " [-user=<username>]  [-password=<password>] \n"
                + " [-dst=<destination folder>] [-pkg=<package name>]  \n"
                + " [-daoprefix=<daoid1:Prefix1,daoid2:Prefix2>] \n"
                + " [-prefix=<bean name prefix>] \n"
                + " [-sourcesFolder=<absolute/relative path to already generated sources folder>] \n"
                + " appName datasetname" + "\n\n\n");
        System.err.println (" -prefix=<bean name prefix> -   Specify generated classes name prefix when dataset contains a single data object.\n  ");
        System.err.println ("                            -   Can specify DSSchema class name prefix when dataset contains multiple data objects.\n\n ");
        System.err.println (" -daoprefix=<daoid1:Prefix1,daoid2:Prefix2> - Specify generated data object classes name prefix when dataset contains multiple data objects.\n ");
    }

    public static void main(String[] args) {
        
    	logger.info(" Generate sources command parameters: " + Arrays.asList(args));
    	
    	String dstFolder = "./generated/";
        String dsName = "";
        String beanNamePrefix = null;
        String pkgName = null;
        String appName = "ProdSearch";
        String server = "AppServer://niko:5162/TVH_DEV_PRODSEARCH";
        String user = "";
        String password = "";
        String daoNamesPrefix = null;
        String sourcesFolder = null;
        
        Map<String, String> packageNames = new HashMap<String, String>(3); 
        
        
        if (args.length < 2) {
            printUsageInformation();
            System.exit(1);
        }

        String option, optionName, optionValue;
        int optionSepIdx;

        for (int idx = 0; idx < args.length - 2; idx++) {

            option = args[idx];
            optionSepIdx = option.indexOf('=');
            if (optionSepIdx == -1) {
                System.err
                        .println("Invalid opton syntax, please use: -optionName=optionValue");
                System.exit(1);
            }
            optionName = option.substring(0, optionSepIdx);
            optionValue = optionSepIdx + 1 < option.length() ? option
                    .substring(optionSepIdx + 1) : "";

            if ("-server".equalsIgnoreCase(optionName))
                server = optionValue;
            else if ("-user".equalsIgnoreCase(optionName))
                user = optionValue;
            else if ("-password".equalsIgnoreCase(optionName))
                password = optionValue;
            else if ("-dst".equalsIgnoreCase(optionName))
                dstFolder = optionValue;
            else if ("-pkg".equalsIgnoreCase(optionName))
                pkgName = optionValue;
            else if ("-prefix".equalsIgnoreCase(optionName))
            	beanNamePrefix = optionValue;
            else if ("-daoprefix".equalsIgnoreCase(optionName))
            	daoNamesPrefix = optionValue;
            else if ("-sourcesFolder".equalsIgnoreCase(optionName))
            	sourcesFolder = optionValue;
            else if (("-" + BEANS_PACKAGE_NAME).equalsIgnoreCase(optionName))
            	packageNames.put(BEANS_PACKAGE_NAME, optionValue);
            else if (("-" + DAOS_PACKAGE_NAME).equalsIgnoreCase(optionName))
            	packageNames.put(DAOS_PACKAGE_NAME, optionValue);
            else if (("-" + META_PACKAGE_NAME).equalsIgnoreCase(optionName))
            	packageNames.put(META_PACKAGE_NAME, optionValue);
        }

        appName = args[args.length - 2];
        dsName = args[args.length - 1];

        Config config = new Config();
        config.setValue(IConfig.APP_SERVER_URL, server);
        config.setValue(IConfig.USER, user);
        config.setValue(IConfig.PASSWORD, password);
        
        Map<String, String> daoIdsClassNamesPrefixMap = null;
        
        if ( daoNamesPrefix != null ) {
        	
        	String [] daoIdsPrefix = daoNamesPrefix.split(",");
        	daoIdsClassNamesPrefixMap = new HashMap<String, String>(daoIdsPrefix.length);
        	
        	for (String daoIdPrefix : daoIdsPrefix) {
        		String [] daoIdPrefixValues = daoIdPrefix.split(":");
        		if (daoIdPrefixValues.length != 2 ) {
        			System.err.println("Invalid parameter for daoprefix option: " + daoNamesPrefix);
        			System.exit(1);
        		}
        		daoIdsClassNamesPrefixMap.put(daoIdPrefixValues[0], daoIdPrefixValues[1]);
        	}
        }
        
        GeneratedFilesInfo generatedFilesInfo = null;
        
        if (sourcesFolder != null) {
        	 try {
        		 generatedFilesInfo = loadGeneratedFilesInfo(sourcesFolder);
			} catch (Exception e) {
				logger.error("Can not load generated files data.", e);
				System.err.println("Input sources file validation failure");
				System.exit(1);
			}
        }
        
        ClassGenerator.generate(config, packageNames, generatePackageFolders(pkgName, packageNames, dstFolder) , appName, dsName, beanNamePrefix, daoIdsClassNamesPrefixMap, generatedFilesInfo);
    }
}
