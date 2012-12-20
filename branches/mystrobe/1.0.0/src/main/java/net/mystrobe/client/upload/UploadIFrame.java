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
 package net.mystrobe.client.upload;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * @author TVH Group NV
 */
public abstract class UploadIFrame extends WebPage {
	
	private static final long serialVersionUID = -7821536912608006246L;

	private boolean uploaded = false;
	
	private FileUploadField uploadField;
	private String newFileUrl;
	
	public UploadIFrame() {
		add(new UploadForm("form"));
		addOnUploadedCallback();
	}
	
	/**
	* return the callback url when upload is finished
	* @return callback url when upload is finished
	*/
	protected abstract String getOnUploadedCallback();
	
	/**
	* Called when the input stream has been uploaded and when it is available
	* on server side
	* return the url of the uploaded file
	* @param upload fileUpload
	*/
	protected abstract String manageInputSream(FileUpload upload);
	
	private class UploadForm extends Form {
		public UploadForm(String id) {
			super(id);
			uploadField = new FileUploadField("file");
			add(uploadField);
			
			add(new AjaxLink("submit"){
				
				@Override 
				public void onClick(AjaxRequestTarget ajaxrequesttarget) {
						ajaxrequesttarget.appendJavaScript(new StringBuilder("showProgressWheel()"));
					}
				});
		}
		
		@Override
		public void onSubmit() {
			FileUpload upload = uploadField.getFileUpload();
			newFileUrl = manageInputSream(upload);
			//file is now uploaded, and the IFrame will be reloaded, during
			//reload we need to run the callback
			uploaded = true;
		}
	}
	
	private void addOnUploadedCallback() {
		
		
		add(new WebComponent("onUploaded") {
		
			@Override
			public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
				if (uploaded) {
					if (uploadField.getFileUpload() != null){
						replaceComponentTagBody(markupStream, openTag,
						"window.parent." + getOnUploadedCallback() + "('" +
						uploadField.getFileUpload().getClientFileName() + "','" +
						newFileUrl +"')");
					}
					uploaded = false;
				}
			}
		});
	
	}

}

