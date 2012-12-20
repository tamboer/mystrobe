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
 package net.mystrobe.client.dynamic.navigation;

import java.io.Serializable;


/**
 * Page navigation information class.
 * 
 * @author TVH Group NV
 */
public class PageNavigation implements Comparable<PageNavigation>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Page number to navigate to
	 */
	private Integer pageNumber;
	
	/**
	 * First page data row Id  
	 */
	private String nextPageFirstRowId;
	
	/**
	 * First page data row Id  
	 */
	private String previousPageLastRowId;

	
	public PageNavigation(int pageNumber, String nextPageFirstRowId, String previousPageLastRowId) {
		this.pageNumber = pageNumber;
		this.nextPageFirstRowId = nextPageFirstRowId;
		this.previousPageLastRowId = previousPageLastRowId;
	}

	/**
	 * @return the pageNumber
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return the pageStartRowId
	 */
	public String getNextPageFirstRowId() {
		return nextPageFirstRowId;
	}

	/**
	 * @param pageStartRowId the pageStartRowId to set
	 */
	public void setNextPageFirstRowId(String pageStartRowId) {
		this.nextPageFirstRowId = pageStartRowId;
	}
	
	/**
	 * @return the previousPageLastRowId
	 */
	public String getPreviousPageLastRowId() {
		return previousPageLastRowId;
	}

	/**
	 * @param previousPageLastRowId the previousPageLastRowId to set
	 */
	public void setPreviousPageLastRowId(String previousPageLastRowId) {
		this.previousPageLastRowId = previousPageLastRowId;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pageNumber == null) ? 0 : pageNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageNavigation other = (PageNavigation) obj;
		if (pageNumber == null) {
			if (other.pageNumber != null)
				return false;
		} else if (!pageNumber.equals(other.pageNumber))
			return false;
		return true;
	}

	public int compareTo(PageNavigation o) {
		if ( o == null) {
			return 1;
		}
		
		PageNavigation other = (PageNavigation) o;
		return this.pageNumber.compareTo(other.getPageNumber());
	}

	@Override
	public String toString() {
		return "PageNavigation [nextPageFirstRowId=" + nextPageFirstRowId
				+ ", pageNumber=" + pageNumber + ", previousPageLastRowId="
				+ previousPageLastRowId + "]";
	}
	
	
}


