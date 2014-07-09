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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author TVH Group NV
 */
public class XMLUtil {

	private static Logger logger = LoggerFactory.getLogger(XMLUtil.class);

	public static List<Element> xpathQuery(String expression, Node elem) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		List<Element> ret = new ArrayList<Element>();
		try {
			NodeList nl = (NodeList) xpath.evaluate(expression, elem,
					XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				ret.add((Element) nl.item(i));
			}
		} catch (Exception e) {
			logger.error("\n\nXPath Error:\n  Expression: " + expression
					+ "\n  Element: " + elem + "\n" + e.getMessage());
		}
		return ret;
	}

	public static String getElementValue(Element elem) {
		String ret = null;
		if (elem != null) {
			NodeList nl = elem.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node _node = nl.item(i);
				if (_node != null && _node.getNodeType() == Node.TEXT_NODE) {
					ret = _node.getNodeValue();
					break;
				}
			}
			if (ret == null)
				logger.warn("No value found for the element: "
						+ elem.getTagName());
		}
		return ret;
	}

	public static String elementToString(Element element) {
		DOMSource domSource = new DOMSource(element);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return writer.toString();
	}

	public static Object getProperty(Element properties, String attribute,
			Class<?> type) {
		Object ret = null;
		String expression = "child::" + getLowerCaseExpr(Globals.ELEMENT_PROP)
				+ "[@" + Globals.ATTRIBUTE_NAME + "='" + attribute + "']";
		List<Element> props = xpathQuery(expression, properties);
		if (props.size() > 0) {
			String value = getElementValue(props.get(0)).trim();
			if (type.equals(Integer.class)) {
				ret = Integer.parseInt(value);
			} else if (type.equals(Long.class)) {
				ret = Long.parseLong(value);
			} else if (type.equals(String.class)) {
				ret = value;
			} else if (type.equals(Boolean.class)) {
				ret = new Boolean(value);
			}
		}
		return ret;
	}

	public static String getLowerCaseExpr(String value) {
		return "*[translate(name(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '"
				+ value.toLowerCase() + "']";
	}


}
