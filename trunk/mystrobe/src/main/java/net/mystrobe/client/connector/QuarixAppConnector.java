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
  package net.mystrobe.client.connector;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDSRelation;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataSet;
import net.mystrobe.client.IFilterParameter;
import net.mystrobe.client.SortState;
import net.mystrobe.client.SortState.Sort;
import net.mystrobe.client.connector.quarixbackend.AppServerAction;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.connector.quarixbackend.Globals;
import net.mystrobe.client.connector.quarixbackend.NamingHelper;
import net.mystrobe.client.connector.quarixbackend.XMLUtil;
import net.mystrobe.client.connector.quarixbackend.api.IAppServer;
import net.mystrobe.client.connector.quarixbackend.json.DSResponse;
import net.mystrobe.client.impl.DAOSchema;
import net.mystrobe.client.impl.DSSchema;

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



/**
 * An application connector implementation that knows how to exchange data with
 * applications hosted on a Quarix for Progress server.
 * 
 * @author TVH Group NV
 */
public class QuarixAppConnector implements IAppConnector, Serializable {

	private static final long serialVersionUID = 8583769148567987260L;
	
	private static Logger logger = LoggerFactory.getLogger(QuarixAppConnector.class);
	
	/**
	 * Use different app server action for each thread.
	 */
	private transient ThreadLocal<AppServerAction> appServerAction = new ThreadLocal<AppServerAction>();
	
	/**
	 * Xml builder used to create new XML documents for server requests
	 */
	private transient DocumentBuilder xmlDocBuilder;
	
	private ConcurrentMap<String, IDSSchema> dsSchemaCache = new ConcurrentHashMap<String, IDSSchema>();	
	
	private IConfig config;
	protected AnnotationDB annotationDB = new AnnotationDB();
	protected String appName = null;
	protected IAppServer appServer = null;
	

	public QuarixAppConnector(IAppServer appServer, String appName, IConfig config) {	    
		this.config = config;
		this.appName = appName;
		this.appServer = appServer;
		
		initConnector();
		
		scanForAnnotations();
	}
	
	public QuarixAppConnector(IAppServer appServer, String appName) {	    
		this.appName = appName;
		this.appServer = appServer;
		
		initConnector();
	}
	
	protected void initConnector() {
		try {
			DocumentBuilderFactory xmlDocFactory = DocumentBuilderFactory.newInstance();
			xmlDocBuilder = xmlDocFactory.newDocumentBuilder();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * Create (or retrieve) the app server action for current thread.
	 *  
	 * @return App server action for current thread.
	 */
	protected AppServerAction getAppServerAction() {
    	if( appServerAction.get() == null ) {
    		AppServerAction serverAction =  new AppServerAction(appServer, appName);
    		appServerAction.set(serverAction);
    		
    		if (logger.isDebugEnabled()) {
        		logger.debug("App sever action for app server : " + this.config.getValue(IConfig.APP_SERVER_URL) 
        				+ " and app name:" + this.appName );
        	}
        }
    	
    	return appServerAction.get();
	}
	
	@Deprecated
	protected void scanForAnnotations() {
	    URL[] urls = null;
	    
	    List<String> packages = null;	    
	    if( config.getValue(IConfig.APP_DATABEAN_PACKAGES) != null) {
	        packages = getPackages( config.getValue(IConfig.APP_DATABEAN_PACKAGES) );
	    } else if(  config.getValue(IConfig.DATABEAN_PACKAGES) != null ) {
	        packages = getPackages( config.getValue(IConfig.DATABEAN_PACKAGES) );
	    }
	    
	    if( packages != null ) {
	        List<URL> urlList = new ArrayList<URL>(packages.size());
	        URL[] pkgUrl = null;
	        for(String pkg : packages) {	            
	        	logger.debug("From resource base " + pkg);
	            pkgUrl = ClasspathUrlFinder.findClassPaths( pkg);
	            
	            if( pkgUrl != null && pkgUrl.length > 0) {
	                urlList.add(pkgUrl[0]);
	            } else {
	                pkgUrl = ClasspathUrlFinder.findResourceBases( pkg.replace('.', '/'));	                
	                if( pkgUrl != null && pkgUrl.length > 0) {
	                    urlList.add(pkgUrl[0]);
	                }
	            }
	        }	
	        urls = urlList.toArray(new URL[urlList.size()]);
	    } else {
	        //urls = ClasspathUrlFinder.findClassPaths();
	    }
	    
	    if( urls == null || urls.length == 0 ) return;
	    
	    try {
	    	if( logger.isDebugEnabled() ) {
		        for( URL u : urls) {
		        	logger.debug("Scanning URL for annotation " + u);
		        }
		        long st = System.currentTimeMillis();
	            annotationDB.scanArchives(urls);
	            long tm = System.currentTimeMillis() - st;
	            logger.info("Scanning for annotation total durration in milliseconds " + tm);	    		
	    	} else {
	    		annotationDB.scanArchives(urls);
	    	}
	    	
        } catch (IOException e) {
            logger.warn("Unable to scann class path for annotated archives");
        }	    
	}

	
	@Override
	public void finalize() throws Throwable {

	}

	
	/**
	 * 
	 * @param dataSetURN
	 */
	public IDSSchema getSchema(String dataSetURN) {

		// hit from cache
		if (dsSchemaCache.containsKey(dataSetURN))
			return dsSchemaCache.get(dataSetURN);

		
		IDSSchema schema = null;
		
		// the annotations way
                try { //RP: aicea vine inlocuit
                    //Class<?> clazz = ClassGenerator.findDSSchemaClass(dataSetURN);
                    Class<?> clazz = locateLocalSchema(dataSetURN);
                    if (clazz != null) {
                        schema = (IDSSchema) clazz.newInstance();
                    }
                } catch (Throwable ignored) {}


		// the 'request from server' way
		if(schema == null) {
			schema =  requestSchema(dataSetURN);
		}
		
		// put in the cache if needed
		if (schema != null && !schema.isDynamic()) {
			dsSchemaCache.put(dataSetURN, schema);
		}

		return schema;
	}

	
	private IDSSchema requestSchema(String dataSetURN) {
		getAppServerAction().process(dataSetURN, Globals.METHOD_PAINT, null);

		Document doc = getAppServerAction().getResponseXML();

		String id = doc.getDocumentElement().getAttribute(Globals.ATTRIBUTE_ID);

		String expression = "child::"
				+ XMLUtil.getLowerCaseExpr(Globals.ELEMENT_COMM);
		List<Element> coms = XMLUtil.xpathQuery(expression, doc
				.getDocumentElement());
		if (coms.isEmpty()) {
			logger.error("Incorect schema response received " + getAppServerAction().getResponseXML().toString());
			throw new RuntimeException("No " + Globals.ELEMENT_COMM
					+ " element found!");
		}

		expression = "child::"
				+ XMLUtil.getLowerCaseExpr(Globals.ELEMENT_PROPERTIES);
		List<Element> props = XMLUtil.xpathQuery(expression, doc
				.getDocumentElement());
		if (props.isEmpty()) {
			throw new RuntimeException("No " + Globals.ELEMENT_PROPERTIES
					+ " element found!");
		}

		expression = "//" + XMLUtil.getLowerCaseExpr(Globals.ELEMENT_TABLE);
		List<Element> tables = XMLUtil.xpathQuery(expression, doc
				.getDocumentElement());
		if (tables.isEmpty()) {
			throw new RuntimeException("No specified table found!");
		}

		expression = "//" + XMLUtil.getLowerCaseExpr(Globals.ELEMENT_RELATION);
		List<Element> relations = XMLUtil.xpathQuery(expression, doc
				.getDocumentElement());

		IDSSchema schema = getSchema(dataSetURN, id, coms.get(0), props.get(0),
				tables, relations);

		return schema;
	}
	
	/**
	 * 
	 * @param dataSetURN
	 */
	public IDataSet instantiateDataSet(String dataSetURN) {
		getAppServerAction().process(dataSetURN, Globals.METHOD_PAINT, null);
		return null;
	}

	
	@Override
	public IDSResponse dataRequest(String dataSetURN, IDSRequest dsRequest) {
		if (dataSetURN == null) {
			throw new RuntimeException("No data set URN specified");
		}
		
		return dataRequest(getSchema(dataSetURN), dsRequest);
	}
	
	public LocalizationProperties localizationRequest() {
		getAppServerAction().process(Globals.LOCALIZATION_URN, Globals.METHOD_DATA, null);
		LocalizationProperties locProps = getAppServerAction().getLocalizationResponseJSON();
		
		return locProps;
	}
	
	/**
	 * Makes a data request with the request details encoded in the request
	 * parameter to the backend application server and returns the response
	 * containing data and / or messages resulted.
	 * 
	 * @param dsRequest
	 *            The request details for the backend application server.
	 */
	@Override
	public IDSResponse dataRequest(IDSSchema dsSchema, IDSRequest dsRequest) {
		if (dsSchema.getURN() == null) {
			throw new RuntimeException("No data set URN specified");
		}	

		Document doc;
		try {
			if (xmlDocBuilder == null) {
				initConnector();
			}
			doc = xmlDocBuilder.newDocument();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}


        // <datasetMsg>
        //    <daoMsg prefetch="false" command="submitCommit" startRowId="011" batchSize="100" skipRow="false" id="doTimeReg">
        //       <filter fld="task_id" op="between" val="???" />
        //       <filter fld="tr_dat" op="between" val="05-01-2009?11-01-2009" />
        //       <filter fld="tr_typ" op="BEGINS" val="?" />
        //           <row id="011" state="2">
        //                <rowImage type="bi">
        //                     <fld name="rowstate">2</fld> /* 0-UNMODIFIED, 1-DELETED, 2-UPDATED, 3-NEW */
        //                     <fld name="rowid">011</fld>
        //                     <fld name="cre_dat">1st row</fld>
        //                     <fld name="tr_dat">3</fld>
        //                </rowImage>
        //                <rowImage type="ai">
        //                     <fld name="rowstate">2</fld> UPDATE
        //                     <fld name="rowid">011</fld>
        //                     <fld name="cre_dat">1st row</fld>
        //                     <fld name="tr_dat">12</fld>
        //                </rowImage>
        //           </row>
        //           <!-- ...  rows -->
        //    </daoMsg>
        // </datasetMsg>
        //

        
		Element msgEl = doc.createElement(Globals.ELEMENT_DATASETMSG);

		// creating action element
		Element actionEl = doc.createElement(Globals.ELEMENT_ACTION);
		actionEl.setAttribute(Globals.ATTRIBUTE_COMMAND, dsRequest.getActionCommand());
        actionEl.setAttribute(Globals.ATTRIBUTE_RESPONSEFORMAT, Globals.RSP_FORMAT_JSON);
		msgEl.appendChild(actionEl);

        Enumeration<String> ids = dsRequest.getDAORequestIds();
		if (ids == null) {
			throw new RuntimeException("No DO request found in the DS request!");
		}

        // creating daoMsg element
		while (ids.hasMoreElements()) {
			IDAORequest<IDataBean> daoRequest = dsRequest.getDAORequest(ids.nextElement());
            IDAOSchema<IDataBean> daoSchema = (IDAOSchema) dsSchema.getDataObjectSchema( daoRequest.getDAOId() );
            
			Element daoMsgEl = doc.createElement(Globals.ELEMENT_DAOMSG);
			daoMsgEl.setAttribute(Globals.ATTRIBUTE_PREFETCH, String.valueOf(daoRequest.isPrefetch()));
			daoMsgEl.setAttribute(Globals.ATTRIBUTE_COMMAND, daoRequest.getCommand());
			daoMsgEl.setAttribute(Globals.ATTRIBUTE_BATCHSIZE, "" + daoRequest.getBatchSize());
			daoMsgEl.setAttribute(Globals.ATTRIBUTE_SKIPROW, "" + daoRequest.isSkipRow());
			daoMsgEl.setAttribute(Globals.ATTRIBUTE_STARTROWID, daoRequest.getStartRowId());
			daoMsgEl.setAttribute(Globals.ATTRIBUTE_ID, daoRequest.getDAOId());

			// adding the filter elements
			Set<IFilterParameter> filters = daoRequest.getFilters();
			if (filters != null) {
				for (IFilterParameter filterParameter : filters) {
					Element filterEl = doc.createElement(Globals.ELEMENT_FILTER);
					filterEl.setAttribute(Globals.ATTRIBUTE_FLD, filterParameter.getColumn());
					filterEl.setAttribute(Globals.ATTRIBUTE_OP, filterParameter.getOperator().name().toLowerCase());
					
					LocalizationProperties localizationProperties = getAppServerAction().getLocalizationProperties();
					String parameterStringValue =  NamingHelper.getFormattedOutputValue(filterParameter.getValue(), filterParameter.getFormat(), localizationProperties);
					filterEl.setAttribute(Globals.ATTRIBUTE_VAL, parameterStringValue);
					
					daoMsgEl.appendChild(filterEl);
				}
			}

			// adding the sort state
			SortState ss = daoRequest.getSortState();
			if (ss != null) {
				Element sortEl = doc.createElement(Globals.ELEMENT_SORT);
				sortEl.setAttribute(Globals.ATTRIBUTE_FLD, ss.getSortColumn());
				sortEl.setAttribute(Globals.ATTRIBUTE_REV, "" + ( Sort.Descending.equals(ss.getSortOrder())));
				daoMsgEl.appendChild(sortEl);
			}

			msgEl.appendChild(daoMsgEl);

			Iterator<IDAORow<IDataBean>> daoRowsIterator =  daoRequest.getRows().iterator();
            while( daoRowsIterator.hasNext() ) daoMsgEl.appendChild(getRowXML( daoSchema, doc, daoRowsIterator.next()));
		}

		doc.appendChild(msgEl);

		String xml = XMLUtil.elementToString(msgEl);

		getAppServerAction().process(dsSchema.getURN(), Globals.METHOD_DATA, xml);
		DSResponse ret = getAppServerAction().getResponseJSON();
		if (ret != null) {
			ret.setDSSchema( dsSchema );
		}

		return ret;
	}




    private  Element getRowXML(IDAOSchema<IDataBean> schema, Document document, IDAORow<IDataBean> daoRow) {

        if( daoRow == null ) throw new IllegalArgumentException("argument [daoRow] can not be null");
        if( daoRow.getRowData() == null && daoRow.getBeforeImage() == null )  throw new IllegalStateException("IDAORow, can not be getRowData and getBeforeImage both null");

        Element row = document.createElement(Globals.ELEMENT_ROW);
        row.setAttribute(Globals.ATTRIBUTE_ROW_ID, daoRow.getRowId() );
        row.setAttribute(Globals.ATTRIBUTE_ROW_STATE, String.valueOf( daoRow.getRowState().ordinal())) ;

        if( daoRow.getBeforeImage() != null) {

            //String rowState = NamingHelper.getFieldValue(daoRow.getBeforeImage(), Globals.ROW_STATE);
            row.appendChild( getRowImageXML(schema, document, RowImageType.bi
                    , daoRow.getBeforeImage().getRowId() != null ? daoRow.getBeforeImage().getRowId() : daoRow.getRowId()
                    , NamingHelper.getFieldValue(daoRow.getBeforeImage(), Globals.ROW_STATE, schema, getLocalizationProperties())
                    , daoRow.getBeforeImage()));
        }
        

        if( daoRow.getRowData() != null ) {
            row.appendChild( getRowImageXML(schema, document, RowImageType.ai, daoRow.getRowId(), String.valueOf(daoRow.getRowState().ordinal()), daoRow.getRowData()));
        }

        return row;
    }


    private Element getRowImageXML(IDAOSchema<?> schema, Document document, RowImageType imageType, String rowId, String rowState, Object data ) {
        Element fld, row = document.createElement(Globals.ELEMENT_ROWIMAGE);
        row.setAttribute( Globals.ATTRIBUTE_ROWIMAGE_TYPE, imageType.name());

        
        Iterator<String> columnIterator = schema.getColumnNames().iterator();
        String currentColumnName;
        while( columnIterator.hasNext() ) {
            currentColumnName = (String) columnIterator.next();
            
            fld = document.createElement( Globals.ELEMENT_ROWFLD );
            fld.setAttribute( Globals.ATTRIBUTE_FLD_NAME, currentColumnName );

            if( Globals.ROW_STATE.equals(currentColumnName ) ) {
                fld.appendChild( document.createTextNode( rowState ));
            }else if( Globals.ROW_ID.equals(currentColumnName ) ) {
                fld.appendChild( document.createTextNode( rowId ));
            } else {
                fld.appendChild( document.createTextNode( NamingHelper.getFieldValue(data, currentColumnName, schema, getLocalizationProperties())) );
            }
            
            row.appendChild(fld);
        }
        return row;
    }


	private IDSSchema getSchema(final String dsURN, final String id,
			final Element comm, final Element properties,
			final List<Element> tables, final List<Element> relations) {

		IDSSchema dsSchema = new IDSSchema() {

			public String getId() {
				return id;
			}

			public boolean hasFilteredChildren() {
				Object obj = XMLUtil.getProperty(properties,
						Globals.PROP_FILTEREDCHILDREN, Boolean.class);
				if (obj != null) {
					return (Boolean) obj;
				}
				return false;
			}

			public boolean hasAtomicChanges() {
				Object obj = XMLUtil.getProperty(properties,
						Globals.PROP_ATOMICCHANGES, Boolean.class);
				if (obj != null) {
					return (Boolean) obj;
				}
				return false;
			}

			public String getURN() {
				return dsURN;
			}

			public Enumeration<IDSRelation> getRelations() {
				return QuarixAppConnector.this.getRelations(relations);
			}

			public int getMargin() {
				Object obj = XMLUtil.getProperty(properties,
						Globals.PROP_MARGIN, Integer.class);
				if (obj != null) {
					return (Integer) obj;
				}
				return 0;
			}

			public IDAOSchema<?> getDataObjectSchema(String dataObjectId) {
				return daoSchemaPool.get(dataObjectId);
			}

			public Enumeration<String> getDataObjectIds() {
				return Collections.enumeration(daoSchemaPool.keySet());
			}
			
			public int getDataObjectsCount() {
				return daoSchemaPool.keySet().size();
			}

			public long getBatchSize() {
				Object obj = XMLUtil.getProperty(properties,
						Globals.PROP_BATCHSIZE, Long.class);
				if (obj != null) {
					return (Long) obj;
				}
				return 0;
			}

			public boolean isDynamic() {
				Enumeration<String> daoIds = getDataObjectIds();
				while (daoIds.hasMoreElements()) {
					if (getDataObjectSchema(daoIds.nextElement()).isDynamic())
						return true;
				}

				return false;
			}
			
			public boolean isReadOnly() {
				Object obj = XMLUtil.getProperty(properties,
						Globals.PROP_READONLY, Boolean.class);
				if (obj != null) {
					return (Boolean) obj;
				}
				
				return false;
			}

			final Map<String, IDAOSchema<?>> daoSchemaPool = getDaoSchemas(this,
					properties, tables);
		};

		return dsSchema;
	}

	private Map<String, IDAOSchema<?>> getDaoSchemas(IDSSchema dsSchema,
			final Element properties, List<Element> tables) {

		final Map<String, IDAOSchema<?>> map = new HashMap<String, IDAOSchema<?>>();

		for (final Element table : tables) {

			DAOSchema<?> daoSchema = new DAOSchema();

			daoSchema.initDAOSchemaSettingsFromXML(properties, table);
			
			final String id = table.getAttribute(Globals.ATTRIBUTE_ID);
			
			map.put(id, daoSchema);

		}

		return map;
	}

	private Enumeration<IDSRelation> getRelations(List<Element> relations) {

		List<IDSRelation> ret = new ArrayList<IDSRelation>();
		for (final Element relation : relations) {

			final String mapping;

			String expression = "child::"
					+ XMLUtil.getLowerCaseExpr(Globals.ELEMENT_MAPPING);
			List<Element> vals = XMLUtil.xpathQuery(expression, relation);
			if (vals.size() > 0) {
				mapping = XMLUtil.getElementValue(vals.get(0));
			} else {
				mapping = null;
			}

			IDSRelation idsRelation = new IDSRelation() {

				public String getChildColumnName() {
					if (mapping != null) {
						if (mapping.indexOf(":") < 0) {
							return mapping;
						} else {
							return mapping.substring(mapping.indexOf(":") + 1);
						}
					}
					return null;
				}

				public String getChildDAOId() {
					String expression = "child::"
							+ XMLUtil.getLowerCaseExpr(Globals.ELEMENT_CHILD);
					List<Element> vals = XMLUtil.xpathQuery(expression,
							relation);
					if (vals.size() > 0) {
						return XMLUtil.getElementValue(vals.get(0));
					}
					return null;
				}

				public String getParentColumnName() {
					if (mapping != null) {
						if (mapping.indexOf(":") < 0) {
							return mapping;
						} else {
							return mapping.substring(0, mapping.indexOf(":"));
						}
					}
					return null;
				}

				public String getParentDAOId() {
					String expression = "child::"
							+ XMLUtil.getLowerCaseExpr(Globals.ELEMENT_PARENT);
					List<Element> vals = XMLUtil.xpathQuery(expression,
							relation);
					if (vals.size() > 0) {
						return XMLUtil.getElementValue(vals.get(0));
					}
					return null;
				}

			};

			ret.add(idsRelation);
		}

		return Collections.enumeration(ret);
	}

    public LocalizationProperties getLocalizationProperties() {
        return getAppServerAction().getLocalizationProperties();
    }
    
    
    protected Class<?> locateLocalSchema(String urn) {

        Map<String, Set<String>> annotationIndex = annotationDB.getAnnotationIndex();

        if( annotationIndex.containsKey(GeneratorMeta.class.getName()) ) {
            Set<String> annotatedClassesSet = annotationIndex.get(GeneratorMeta.class.getName());
            Class<?> cls = null;
            for( String className : annotatedClassesSet ) {
                try {
                    cls = Class.forName(className);
                    GeneratorMeta annotation = cls.getAnnotation(GeneratorMeta.class);
                    if (annotation != null && annotation.urn().equals(urn)) {
                        if (cls.getSuperclass().equals(DSSchema.class)) {
                            return (Class<?>) cls;
                        }
                    }
                    
                } catch (ClassNotFoundException e) {
                    logger.warn("Found annotated class " + className + " but can not load it, ignoring class");
                }
            }
        }
        
        return null;
    }
 
    protected List<String> getPackages(String packageStr) {
        List<String> results = new LinkedList<String>();
        
        if( packageStr == null ) return results;
        
        StringTokenizer tokenizer = new StringTokenizer(packageStr, ":;");
        while( tokenizer.hasMoreTokens() ) {
            results.add( tokenizer.nextToken() );
        }
        
        if( results.size() == 0 ) results.add(packageStr);
        return results;
    }

    public String getAppName() {
    	return appName;
    }
    
    public IConfig getConfig() {
    	return this.config;
    }
 }

