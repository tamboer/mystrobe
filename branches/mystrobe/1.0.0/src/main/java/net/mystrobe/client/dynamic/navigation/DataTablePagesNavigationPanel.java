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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.WicketDSRuntimeException;
import net.mystrobe.client.DataSourceAdaptor.AppendPosition;
import net.mystrobe.client.navigator.IDataTableNavigatorListener;
import net.mystrobe.client.navigator.IDataTableNavigatorSource;
import net.mystrobe.client.navigator.IDataTableNavigatorListener.DataTableNavigationDirection;
import net.mystrobe.client.navigator.IDataTableNavigatorListener.DataTableNavigationState;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Data table pages navigation component.</br>
 * 
 * <p>
 * Displays a page navigation panel with page numbers.
 * Number of visible pages numbers at a time can be configured
 *  through a constructor parameter.</p> 
 * 
 * Has to be linked to a {@link IDataTableNavigatorListener} to work properly.</br>
 * 
 * @author TVH Group NV
 *
 * @param <T> Data object type.
 */
public class DataTablePagesNavigationPanel<T extends IDataBean> extends AbstractDataTableNavigationPanel<T> implements IDataTableNavigatorSource<T> {
	
	private static final long serialVersionUID = -4169615937229683445L;
	
	protected static final Logger logger = LoggerFactory.getLogger(DataTablePagesNavigationPanel.class);

	public static final String DEFAULT_BUSY_INDICATOR = "busy_indicator";
	
	private static final String NAVIGATOR_ID= "wmcNavigator";
	
	protected WebMarkupContainer wmcNavigator;
	
	protected WebMarkupContainer wmcNoFirstPage, wmcFirst, wmcPrevious;
	
	protected WebMarkupContainer wmcNoPreviousPage, wmcNoNext, wmcNext, wmcNoLast, wmcLast;
	
	protected Label dotsLabel;
	
	/**
	 * Previous Page Number 
	 */
	protected int previousPageNumber = -1;
	
	protected int visiblePagesCount = 4;
	
	protected ListView<PageNavigation> previousPagesLinks;
	
	protected ListView<PageNavigation> nextPagesLinks;
	
	protected TreeSet<PageNavigation> pageNavigationSet = new TreeSet<PageNavigation>();
	
	protected boolean canNavigateToLastPage = false;

	public DataTablePagesNavigationPanel(String id, final int pageSize, int visiblePagesCount) {
		super(id, pageSize);
		
		this.visiblePagesCount = visiblePagesCount;
		
		wmcNavigator = new WebMarkupContainer(NAVIGATOR_ID);
	  
	    wmcNavigator.add(new Label("pagenumber", new LoadableDetachableModel<String>() {
	        
					private static final long serialVersionUID = 1L;
		
						@Override
			        	protected String load() {
			        		return String.valueOf(currentPageNumber);
			        	}
			    	})
	    		{
	    	
				private static final long serialVersionUID = 1L;
				
				@Override
	            public boolean isVisible() {
	            	return currentPageNumber > 0;
	            }
	    	});
	    
        dotsLabel = new Label("dots","...") {
        
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
       		 	
				if (canNavigateToLastPage && !nextPagesLinks.getList().isEmpty()) {
					int lastVisibleNumber = nextPagesLinks.getList().get(nextPagesLinks.getList().size() - 1).getPageNumber();
					int highestPageNumber = pageNavigationSet.last().getPageNumber();
					
					return !(lastVisibleNumber == highestPageNumber);
					
				} else {
					return navigationState.equals(DataTableNavigationState.FirstPage) ||
   		 				navigationState.equals(DataTableNavigationState.NotFirstOrLast);
				}
				
				
        	}
        };
        
        dotsLabel.setRenderBodyOnly(true);
        wmcNavigator.add(dotsLabel) ;
        
	    wmcNoFirstPage = new WebMarkupContainer("nofirst") {
	        
			private static final long serialVersionUID = 665062971689906438L;

			@Override
	        public boolean isVisible() {
	            return navigationState.equals(DataTableNavigationState.FirstPage) ||
   		 			navigationState.equals(DataTableNavigationState.FirstAndLastPage);
	        }
	    };
	    wmcNavigator.add(wmcNoFirstPage);
	    
	    wmcFirst = new DefaultBusyAjaxIndicatingLink<Void>("first") {
	
	        private static final long serialVersionUID = -4867460598048123646L;

			@Override
	        public void onClick(AjaxRequestTarget target) {
	        	currentPageNumber = 1;
				
				if (pageNavigationSet.first().getPageNumber() == 1) {
					//if first page is available than just navigate to it
					navigationListener.previousPageData(DataTablePagesNavigationPanel.this,
							pageNavigationSet.first().getNextPageFirstRowId(), pageSize, currentPageNumber, false);
				} else {
					navigationListener.resetDataTableNavigation(DataTablePagesNavigationPanel.this, pageSize);
				}
			
	        	navigationListener.moveToRow(DataTablePagesNavigationPanel.this.firstDataRowId);
	        	target.add(DataTablePagesNavigationPanel.this);
	        	onRefreshContent(target);
	        }
	
	        public boolean isVisible() {
	            return navigationState.equals(DataTableNavigationState.LastPage) ||
		 			navigationState.equals(DataTableNavigationState.NotFirstOrLast);
	        }
	    };
	    wmcNavigator.add(wmcFirst);
	
	    wmcNoPreviousPage = new WebMarkupContainer("noprev") {
	         
			private static final long serialVersionUID = 2727097156844338363L;

			@Override
	        public boolean isVisible() {
	             return navigationState.equals(DataTableNavigationState.FirstPage) ||
		 			navigationState.equals(DataTableNavigationState.FirstAndLastPage);
	        }
	    };
	    wmcNavigator.add(wmcNoPreviousPage);
	    
	    wmcPrevious = new DefaultBusyAjaxIndicatingLink<Void>("prev") {
	
	        private static final long serialVersionUID = 7668111859529788886L;

			@Override
	        public void onClick(AjaxRequestTarget target) {
        		currentPageNumber--;
				navigationListener.previousPageData(DataTablePagesNavigationPanel.this, firstDataRowId, pageSize, currentPageNumber, false);
				navigationListener.moveToRow(DataTablePagesNavigationPanel.this.firstDataRowId);
				target.add(DataTablePagesNavigationPanel.this);
				onRefreshContent(target);
			}
	
	        @Override
	        public boolean isVisible() {
	            return navigationState.equals(DataTableNavigationState.LastPage) ||
	 			navigationState.equals(DataTableNavigationState.NotFirstOrLast);
	        }
	    };
	    wmcNavigator.add(wmcPrevious);
	    
	    wmcNoNext =  new WebMarkupContainer("nonext") {
	         
			private static final long serialVersionUID = 2722150729138068212L;

			@Override
	        public boolean isVisible() {
	        	 return navigationState.equals(DataTableNavigationState.LastPage) ||
	 				navigationState.equals(DataTableNavigationState.FirstAndLastPage);
	        }
	    };
	    wmcNavigator.add(wmcNoNext);
	   
	    wmcNext = new DefaultBusyAjaxIndicatingLink<Void>("next") {
	
	        private static final long serialVersionUID = 3639428717937812505L;

			@Override
	        public void onClick(AjaxRequestTarget target) {
				currentPageNumber++;
				navigationListener.nextPageData(DataTablePagesNavigationPanel.this, lastDataRowId, pageSize, currentPageNumber, false);
				navigationListener.moveToRow(DataTablePagesNavigationPanel.this.firstDataRowId);
				target.add(DataTablePagesNavigationPanel.this);
				onRefreshContent(target);
			}
	
	        @Override
	        public boolean isVisible() {
	        	return navigationState.equals(DataTableNavigationState.FirstPage) ||
	 				navigationState.equals(DataTableNavigationState.NotFirstOrLast);
	        }
	    };
	    wmcNavigator.add(wmcNext);
	    
	    wmcNoLast =  new WebMarkupContainer("nolast") {
	         
			private static final long serialVersionUID = 6970652385761901463L;

			@Override
	        public boolean isVisible() {
	        	 return !canNavigateToLastPage || navigationState.equals(DataTableNavigationState.LastPage) ||
	 				navigationState.equals(DataTableNavigationState.FirstAndLastPage);
	        }
	    };
	    wmcNavigator.add(wmcNoLast);
	    
	    wmcLast = new DefaultBusyAjaxIndicatingLink<Void>("last") {
	    	
	        private static final long serialVersionUID = 3639428717937812505L;

			@Override
	        public void onClick(AjaxRequestTarget target) {
				currentPageNumber = pageNavigationSet.last().getPageNumber();
				navigationListener.nextPageData(DataTablePagesNavigationPanel.this, pageNavigationSet.last().getPreviousPageLastRowId(), pageSize, currentPageNumber, false);
				navigationListener.moveToRow(DataTablePagesNavigationPanel.this.firstDataRowId);
				target.add(DataTablePagesNavigationPanel.this);
				onRefreshContent(target);
			}
	
	        @Override
	        public boolean isVisible() {
	        	return canNavigateToLastPage && !(navigationState.equals(DataTableNavigationState.LastPage) ||
		 				navigationState.equals(DataTableNavigationState.FirstAndLastPage));
	        }
	    };
	    
	    wmcNavigator.add(wmcLast);
	   
	    addPreviousPageLinks();
	    
	    add(wmcNavigator);
	}
	
	protected void addPreviousPageLinks() {
		
		previousPagesLinks = new ListView<PageNavigation>("previousPages", new ArrayList<PageNavigation>()) {

			private static final long serialVersionUID = -3404489519083797364L;

			@Override
			protected void populateItem(ListItem<PageNavigation> item) {
				
				final PageNavigation pageNavigation = item.getModelObject();
				
				AjaxLink<Void> previousPageLink = new DefaultBusyAjaxIndicatingLink<Void>("previousPageNumber") {

		    		private static final long serialVersionUID = 7966128523124624494L;

					@Override
		            public void onClick(AjaxRequestTarget target) {
						currentPageNumber = pageNavigation.getPageNumber();
						navigationListener.previousPageData(DataTablePagesNavigationPanel.this, pageNavigation.getNextPageFirstRowId(), pageSize, currentPageNumber, false);
						navigationListener.moveToRow(firstDataRowId);
						target.add(DataTablePagesNavigationPanel.this);
						onRefreshContent(target);
					}
					
					@Override
					public boolean isVisible() {
						return pageNavigation.getPageNumber() < currentPageNumber;
					}
				};
		        
		        Label previousPageLabel = new  Label("previousPageLabel",  String.valueOf(pageNavigation.getPageNumber()));
				previousPageLabel.setRenderBodyOnly(true);
		        
		        previousPageLink.add(previousPageLabel);
		        previousPageLink.setOutputMarkupId(true);
		        
		        item.add(previousPageLink);
			}

			@Override
	        public boolean isVisible() {
	        	return currentPageNumber >= 1;
	        }
		};
		previousPagesLinks.setOutputMarkupId(true);
		wmcNavigator.add(previousPagesLinks);
		
		nextPagesLinks = new ListView<PageNavigation>("nextPages", new ArrayList<PageNavigation>()) {

			private static final long serialVersionUID = -3404489519083797364L;

			@Override
			protected void populateItem(ListItem<PageNavigation> item) {
				
				final PageNavigation pageNavigation = item.getModelObject();
				
				AjaxLink<Void> nextPageLink = new DefaultBusyAjaxIndicatingLink<Void>("nextPagesNumber") {

		    		private static final long serialVersionUID = 5569506469352783495L;

					@Override
		            public void onClick(AjaxRequestTarget target) {
						currentPageNumber = pageNavigation.getPageNumber();
						navigationListener.nextPageData(DataTablePagesNavigationPanel.this, pageNavigation.getPreviousPageLastRowId(), pageSize, currentPageNumber, false);
						navigationListener.moveToRow(firstDataRowId);
						target.add(DataTablePagesNavigationPanel.this);
						onRefreshContent(target);
					}
					
					@Override
					public boolean isVisible() {
						return (pageNavigation.getPageNumber() - currentPageNumber) >= 1;
					}
		        };
		        
		        Label nextPageLabel = new  Label("nextPagesLabel",  String.valueOf(pageNavigation.getPageNumber()));
				nextPageLabel.setRenderBodyOnly(true);
				
		        nextPageLink.add(nextPageLabel);
		        nextPageLink.setOutputMarkupId(true);
		        
		        item.add(nextPageLink);
			}
		};
		nextPagesLinks.setOutputMarkupId(true);
		wmcNavigator.add(nextPagesLinks);
	}
	
	public void onRefreshContent(AjaxRequestTarget target){
		
	}
	
	
	/**
	 * Update visible navigation pages.<br/>
	 * 
	 * Method computes previous and next navigation pages to display
	 *  according to <tt>visiblePagesCount</tt> current cursor position 
	 *  and available next / previous pages.     
	 */
	protected void computeVisibleNavigationPages(DataTableNavigationState newNavigationState, DataTableNavigationDirection navigationDirection) {
		
		logger.debug("Compute visible pages: " + this.pageNavigationSet);
		
		//Find navigation pages to display  
		NavigableSet<PageNavigation> previousVisiblePages = this.pageNavigationSet.headSet(new PageNavigation(currentPageNumber, null, null), false);
		NavigableSet<PageNavigation> nextVisiblePages = this.pageNavigationSet.tailSet(new PageNavigation(currentPageNumber + 1, null, null), true);
		
		boolean displaySamePageNumbers = false;
		
		if (DataTableNavigationDirection.Next.equals(navigationDirection)) {
			List<PageNavigation> currentNextVisiblePages = this.nextPagesLinks.getModelObject();
			if (currentNextVisiblePages != null && !currentNextVisiblePages.isEmpty()) {
				if (currentNextVisiblePages.get(currentNextVisiblePages.size() - 1).getPageNumber() > currentPageNumber ||
						(DataTableNavigationState.LastPage.equals(newNavigationState))) {
					
					if (nextVisiblePages.size() > 0) {
						nextVisiblePages = nextVisiblePages.headSet((new PageNavigation(currentNextVisiblePages.get(currentNextVisiblePages.size() - 1).getPageNumber(), null, null)), true);
					}
					
					//set previous pages
					List<PageNavigation> currentPreviousVisiblePages = this.previousPagesLinks.getModelObject();
					if (currentPreviousVisiblePages != null && !currentPreviousVisiblePages.isEmpty() && !previousVisiblePages.isEmpty() )  {
						previousVisiblePages = previousVisiblePages.tailSet(new PageNavigation(currentPreviousVisiblePages.get(0).getPageNumber(), null, null), true);
					}
					
					if (visiblePagesCount < (nextVisiblePages.size() + previousVisiblePages.size())) {
						if (DataTableNavigationState.FirstPage.equals(newNavigationState)) {
							nextVisiblePages = nextVisiblePages.headSet(new PageNavigation(visiblePagesCount, null, null), true);
						} else if (!previousVisiblePages.isEmpty()) {
							int previousPagesSize =  visiblePagesCount - nextVisiblePages.size();
							previousVisiblePages = previousVisiblePages.tailSet(new PageNavigation(currentPageNumber - previousPagesSize, null, null), true);
						}
					}
					displaySamePageNumbers = true;
				}
			}
		}
	
		if (!displaySamePageNumbers && DataTableNavigationDirection.Previous.equals(navigationDirection)) {
			List<PageNavigation> currentPreviousVisiblePages = this.previousPagesLinks.getModelObject();
			if (currentPreviousVisiblePages != null && !currentPreviousVisiblePages.isEmpty()) {
				if ((currentPreviousVisiblePages.get(0).getPageNumber() < currentPageNumber) || currentPageNumber == 1) {
					if (previousVisiblePages.size() > 0) {
						previousVisiblePages = previousVisiblePages.tailSet(new PageNavigation(currentPreviousVisiblePages.get(0).getPageNumber(), null, null), true);
					}
					//set next pages
					List<PageNavigation> currentNextVisiblePages = this.nextPagesLinks.getModelObject();
					if (currentNextVisiblePages != null && !currentNextVisiblePages.isEmpty() && !nextVisiblePages.isEmpty()) {
						nextVisiblePages = nextVisiblePages.headSet((new PageNavigation(currentNextVisiblePages.get(currentNextVisiblePages.size() - 1).getPageNumber(), null, null)), true);
					}
					
					if (visiblePagesCount < (nextVisiblePages.size() + previousVisiblePages.size())) {
						int nextPagesSize =  visiblePagesCount - previousVisiblePages.size();
						nextVisiblePages = nextVisiblePages.headSet((new PageNavigation(currentPageNumber + nextPagesSize, null, null)), true);
					}
					
					displaySamePageNumbers = true;
				}
			}
		}
		
		if (!displaySamePageNumbers ) {
			if (DataTableNavigationDirection.Next.equals(navigationDirection)) {
				if (nextVisiblePages.size() > 1 && DataTableNavigationDirection.Next.equals(navigationDirection)) {
					nextVisiblePages = nextVisiblePages.headSet(new PageNavigation(currentPageNumber + 1, null, null), true);
				}
				
				int previousPagesCount = this.visiblePagesCount - nextVisiblePages.size();
				previousPagesCount = Math.min(previousPagesCount, previousVisiblePages.size());
						
 				previousVisiblePages = previousVisiblePages.tailSet(new PageNavigation(currentPageNumber - 1 - previousPagesCount, null, null), false);
						//nextVisiblePages = nextVisiblePages.headSet(new PageNavigation(currentPageNumber + 1 + nextPagesCount, null, null), false);
			} else {
				
				if (previousVisiblePages.size() > 1 && DataTableNavigationDirection.Previous.equals(navigationDirection) ) {
					previousVisiblePages = previousVisiblePages.tailSet(new PageNavigation(currentPageNumber - 1, null, null), true);
				}
				
				int nextPagesCount = Math.min(this.visiblePagesCount - previousVisiblePages.size(), nextVisiblePages.size());
				nextVisiblePages = nextVisiblePages.headSet(new PageNavigation(currentPageNumber + 1 + nextPagesCount, null, null), false);
			}
		}
		
		this.previousPagesLinks.setList(new ArrayList<PageNavigation>(previousVisiblePages));
		this.nextPagesLinks.setList(new ArrayList<PageNavigation>(nextVisiblePages));
		
		logger.debug("Compute visible pages, previous: " + this.previousPagesLinks.getList());
		logger.debug("Compute visible pages, next: " + this.nextPagesLinks.getList());
	}
	
	
	@Override
	public void updateNavigatorState(DataTableNavigationState navigationState, DataTableNavigationDirection navigationDirection, String firstRowId, String lastRowId) {
		
		if (DataTableNavigationState.FirstPage.equals(navigationState) || 
				DataTableNavigationState.FirstAndLastPage.equals(navigationState)) {
			this.currentPageNumber = 1;
		}
		
		//set next / previous visible pages
		computeVisibleNavigationPages(navigationState, navigationDirection);
		
		//update previous page number
		this.previousPageNumber = this.currentPageNumber;
		
		super.updateNavigatorState(navigationState, navigationDirection, firstRowId, lastRowId);
	}
	
	@Override
	public void onDataBufferReplaced(List<T> newDataBuffer, Map<String, T> bufferRowIdsMap, boolean hasFirstRow, boolean hasLastRow) {
		
		pageNavigationSet.clear();
		
		this.canNavigateToLastPage = hasLastRow;
		
		buildNavigatorPages(newDataBuffer, AppendPosition.REPLACE, hasFirstRow, hasLastRow);
		
		super.onDataBufferReplaced(newDataBuffer, bufferRowIdsMap, hasFirstRow, hasLastRow);
		this.previousPageNumber = -1;
		
		logger.debug("Pages after removal: " + this.pageNavigationSet);
	}

	@Override
	public void onDataBufferChanged(List<T> removedData, Map<String, T> removedRowsMap,  
			List<T> newDataBuffer, AppendPosition appendPosition, boolean hasFirstRow, boolean hasLastRow) {
		
		switch(appendPosition) {
			case BEGINING:
				if (removedRowsMap != null ){
					//remove pages where rowId no longer in the buffer 
					int smallestRemovedPageNumber = Integer.MAX_VALUE;
					for (Iterator<PageNavigation> pagesIterator = this.pageNavigationSet.descendingIterator(); pagesIterator.hasNext();) {
						PageNavigation pageNavigation = pagesIterator.next();
						if (removedRowsMap.containsKey(pageNavigation.getNextPageFirstRowId()) || pageNavigation.getNextPageFirstRowId() == null) { 
							if (removedRowsMap.containsKey(pageNavigation.getPreviousPageLastRowId())) {
								if ( pageNavigation.getPageNumber() < smallestRemovedPageNumber) {
									smallestRemovedPageNumber = pageNavigation.getPageNumber();
								}
								//remove page navigation info as it is not valid anymore
								pagesIterator.remove();
							} else {
								pageNavigation.setNextPageFirstRowId(null);
							}
						} 
					}
					
					//when only one element is deleted and it matches any of the page navigation info
					// remove all pages after that
					if (smallestRemovedPageNumber < Integer.MAX_VALUE ) {
						this.pageNavigationSet.tailSet(new PageNavigation(smallestRemovedPageNumber, null, null), true).clear();
					}
				}
				logger.debug("Append.BEGINNING Pages after removal: " + this.pageNavigationSet);
				break;
			case END:
				//remove pages where rowId no longer in the buffer 
				if (removedRowsMap != null ) {
					int highestRemovedPageNumber = Integer.MIN_VALUE;
					for (Iterator<PageNavigation> pagesIterator = pageNavigationSet.iterator(); pagesIterator.hasNext();) {
						PageNavigation pageNavigation = pagesIterator.next();
						if (removedRowsMap.containsKey(pageNavigation.getPreviousPageLastRowId()) || pageNavigation.getPreviousPageLastRowId() == null) {
							if (removedRowsMap.containsKey(pageNavigation.getNextPageFirstRowId())) {
								if (pageNavigation.getPageNumber() > highestRemovedPageNumber) {
									highestRemovedPageNumber = pageNavigation.getPageNumber();
								}
								pagesIterator.remove();
							} else {
								pageNavigation.setPreviousPageLastRowId(null);
							}
						}  
					}
					
					//when only one element is deleted and it matches any of the page navigation info
					// remove all pages before that
					if (highestRemovedPageNumber > Integer.MIN_VALUE ) {
						this.pageNavigationSet.headSet(new PageNavigation(highestRemovedPageNumber, null, null), true).clear();
					}
				}
				logger.debug("Append.END Pages after removal: " + this.pageNavigationSet);
				break;
		}
		
		this.canNavigateToLastPage = hasLastRow;
	
		buildNavigatorPages(newDataBuffer, appendPosition, hasFirstRow, hasLastRow);
		
		super.onDataBufferChanged(removedData, removedRowsMap, newDataBuffer, appendPosition, hasFirstRow, hasLastRow);
	}
	
	protected int findRowPositionInList(String rowId, List<T> rows) {
		
		int position = 0;
		
		for (T row : rows) {
			if (rowId.equals(row.getRowId())) {
				break;
			}
			position++;
		}
		if (!(position < rows.size())) {
			throw new WicketDSRuntimeException("Can not find navigator page rowId in data buffer.");
		}
		
		return position;
	}
	
	protected void buildNavigatorPages(List<T> dataBuffer, AppendPosition appendPosition, boolean hasFirstRow, boolean hasLastRow) {
		
		int currentPosition = 0;
		int pageNumber = 1;
		
		switch(appendPosition) {
			case BEGINING:
				boolean onFirstPage = false;
				PageNavigation firstPage =  this.pageNavigationSet.first();
				
				if (firstPage != null && firstPage.getPreviousPageLastRowId() == null) {
					currentPosition = findRowPositionInList(firstPage.getNextPageFirstRowId(), dataBuffer) - this.pageSize;
					pageNumber = firstPage.getPageNumber();
					
					PageNavigation lastBeforeFirstPage  = this.pageNavigationSet.higher(firstPage);
					if (lastBeforeFirstPage != null && lastBeforeFirstPage.getPreviousPageLastRowId() == null) { 
						lastBeforeFirstPage.setPreviousPageLastRowId(dataBuffer.get(currentPosition + this.pageSize - 1).getRowId());
					}
					
					onFirstPage = true;
				} else if (firstPage != null) {
					currentPosition = findRowPositionInList(firstPage.getPreviousPageLastRowId(), dataBuffer) - this.pageSize + 1;
					pageNumber = firstPage.getPageNumber() - 1;
				}
				
				while( (currentPosition + this.pageSize) >= 0 && (pageNumber > 0)) {
					String nextPageFirstRowId = (currentPosition + this.pageSize) < dataBuffer.size() ? 
							dataBuffer.get(currentPosition + this.pageSize).getRowId() : null;
					String previousPageLastRowId = currentPosition - 1 >= 0 ? dataBuffer.get(currentPosition - 1).getRowId() : null;
					
					if (onFirstPage) {
						firstPage.setPreviousPageLastRowId(previousPageLastRowId);
						onFirstPage = false;
						logger.debug("On first page");
					} else {
						this.pageNavigationSet.add(new PageNavigation(pageNumber, nextPageFirstRowId, previousPageLastRowId));
					}
					
					currentPosition -= this.pageSize;
					pageNumber--; 
				}
				break;
			
			case END:
				boolean onLastPage = false;
				PageNavigation lastPage =  this.pageNavigationSet.last();
				if (lastPage != null && lastPage.getNextPageFirstRowId() == null) {
					//find position for lastPage.getPreviousPageLastRowId 
					currentPosition = findRowPositionInList(lastPage.getPreviousPageLastRowId(), dataBuffer ) + 1;
					pageNumber = lastPage.getPageNumber();
					
					PageNavigation firstBeforeLastPage  = this.pageNavigationSet.lower(lastPage);
					if (firstBeforeLastPage != null && firstBeforeLastPage.getNextPageFirstRowId() == null) {
						firstBeforeLastPage.setNextPageFirstRowId(dataBuffer.get(currentPosition).getRowId());
					}
					
					onLastPage = true; 
				} else if (lastPage != null) {
					currentPosition = findRowPositionInList(lastPage.getNextPageFirstRowId(), dataBuffer);
					pageNumber = lastPage.getPageNumber() + 1;
				}
				
				while (dataBuffer.size() > currentPosition ) {
					
					String nextPageFirstRowId = (currentPosition + this.pageSize) < dataBuffer.size() ? 
							dataBuffer.get(currentPosition + this.pageSize).getRowId() : null;
					String previousPageLastRowId = currentPosition - 1 >= 0 ? dataBuffer.get(currentPosition - 1).getRowId() : null;
					
					if (onLastPage) {
						lastPage.setNextPageFirstRowId(nextPageFirstRowId);
						onLastPage = false;
						logger.debug("On last page"); 
					} else {
						this.pageNavigationSet.add(new PageNavigation(pageNumber, nextPageFirstRowId, previousPageLastRowId));
					}
					
					currentPosition += this.pageSize;
					pageNumber++; 
				}
				
				break;
				
			case REPLACE:
				currentPosition = 0;
				pageNumber = 1;
				
				while (dataBuffer.size() > currentPosition ) {
					String nextPageFirstRowId = (currentPosition + this.pageSize) < dataBuffer.size() ? 
							dataBuffer.get(currentPosition + this.pageSize).getRowId() : null;
					String previousPageLastRowId = currentPosition - 1 >= 0 ? dataBuffer.get(currentPosition - 1).getRowId() : null;
					
					this.pageNavigationSet.add(new PageNavigation(pageNumber, nextPageFirstRowId, previousPageLastRowId));
					
					currentPosition += this.pageSize;
					pageNumber++; 
				}
				
				break;	
		}
		
		logger.debug("Calculated pages: " + this.pageNavigationSet);
		
	}
	
	@Override
	public void updateNavigatorDataChanged(DataTableNavigationDirection navigationDirection, 
			String firstRowId, int newPageSize, int newPageNumber) {
		
		if (this.firstDataRowId != null) {
			this.previousPageNumber =  this.currentPageNumber;
		}
		
		PageNavigation pageToNavigate = this.navigationListener.findNavigatorRepositionInfo(firstRowId, this.firstDataRowId, currentPageNumber, pageSize, navigationDirection,
				newPageSize, newPageNumber); 
		
		if (pageToNavigate.getPageNumber() < currentPageNumber ) {
			this.currentPageNumber = pageToNavigate.getPageNumber();
			this.navigationListener.previousPageData(this, pageToNavigate.getNextPageFirstRowId(), pageSize, this.currentPageNumber, true);
		} else if (pageToNavigate.getPageNumber() > currentPageNumber) {
			this.currentPageNumber = pageToNavigate.getPageNumber();
			this.navigationListener.nextPageData(this, pageToNavigate.getPreviousPageLastRowId(), pageSize, this.currentPageNumber, true);
		}
		
	}
	
	/**
	 * Class for ajax links links.
	 * 
	 * @param <T>
	 */
	public abstract class DefaultBusyAjaxIndicatingLink<T> extends AjaxLink<T> implements IAjaxIndicatorAware{
		
		private static final long serialVersionUID = -4534487387125712738L;

		public DefaultBusyAjaxIndicatingLink(String id) {
			super(id);
		}
	
		public DefaultBusyAjaxIndicatingLink(String id, IModel<T> model) {
			super(id, model);
		}

		public String getAjaxIndicatorMarkupId() {
			return DEFAULT_BUSY_INDICATOR;
		}
	}
}

