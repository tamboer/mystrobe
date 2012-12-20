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

import java.io.File;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.string.StringValue;

/**
 * @author TVH Group NV
 */
public abstract class UploadPanel extends Panel {
	
	private InlineFrame uploadIFrame = null;
	
	public UploadPanel(String id) {
		super(id);
		addOnUploadedCallback();
		
		setOutputMarkupId(true);
	}
	
	/**
	* Called when the upload load is uploaded and ready to be used
	* Return the url of the new uploaded resource
	* @param upload {@link FileUpload}
	*/
	public abstract String onFileUploaded(FileUpload upload);
	
	/**
	* Called once the upload is finished and the traitment of the
	* {@link FileUpload} has been done in {@link UploadPanel#onFileUploaded}
	* @param target an {@link AjaxRequestTarget}
	* @param fileName name of the file on the client side
	* @param newFileUrl Url of the uploaded file
	*/
	public abstract void onUploadFinished(AjaxRequestTarget target, String filename, String newFileUrl);
	
	@Override
	protected void onBeforeRender() {
	
		super.onBeforeRender();
		
		if (uploadIFrame == null) {
			// the iframe should be attached to a page to be able to get its pagemap,
			// that's why i'm adding it in onBeforRender
			addUploadIFrame();
		}
	}
	
	/**
	* Create the iframe containing the upload widget
	*
	*/
	private void addUploadIFrame() {
		
		IPageLink iFrameLink = new IPageLink() {
	
			@Override
			public Page getPage() {
				
				return new UploadIFrame() {
					
					@Override
					protected String getOnUploadedCallback() {
						return "onUpload_" + UploadPanel.this.getMarkupId();
					}
	
					@Override
					protected String manageInputSream(FileUpload upload) {
						return UploadPanel.this.onFileUploaded(upload);
					}
				};
			}
	
			@Override
			public Class<UploadIFrame> getPageIdentity() {
				return UploadIFrame.class;
			}
		};
	
		uploadIFrame = new InlineFrame("upload", iFrameLink);
	
		add(uploadIFrame);
	}
	
	/**
	* Hackie method allowing to add a javascript in the page defining the
	* callback called by the innerIframe
	*
	*/
	private void addOnUploadedCallback() {
	
		final OnUploadedBehavior onUploadBehavior = new OnUploadedBehavior();
		add(onUploadBehavior);
		add(new WebComponent("onUploaded") {
			
			@Override
			public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
	
				// calling it through setTimeout we ensure that the callback is called
				// in the proper execution context, that is the parent frame
				replaceComponentTagBody(markupStream, openTag,
						"function onUpload_" + UploadPanel.this.getMarkupId() +
						"(clientFileName, newFileUrl) {window.setTimeout(function() { " +
						onUploadBehavior.getCallback() + " }, 0 )}");
			}
		});
	}
	
	private class OnUploadedBehavior extends AbstractDefaultAjaxBehavior {
	
		public String getCallback() {
	
			return generateCallbackScript(
	
				"wicketAjaxGet('" + getCallbackUrl() +
				"&amp;amp;newFileUrl=' + encodeURIComponent(newFileUrl)" +
				" + '&amp;amp;clientFileName=' + encodeURIComponent(clientFileName)").toString();
		}
	
		
		@Override
		protected void respond(AjaxRequestTarget target) {
			StringValue clientFileName = getRequest().getRequestParameters().getParameterValue("clientFileName");
			StringValue newFileUrl = getRequest().getRequestParameters().getParameterValue("newFileUrl");
			UploadPanel.this.onUploadFinished(target,clientFileName.toString() , newFileUrl.toString());
		}
	};
	
	public boolean checkFileExists(File newFile ) {
		if (newFile.exists()) {
            if (!Files.remove(newFile)) {
                throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
            }
            return true;
        }
		return false;
	}
	
}
