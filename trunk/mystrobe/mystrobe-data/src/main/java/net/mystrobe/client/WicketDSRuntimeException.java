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
 package net.mystrobe.client;

import net.mystrobe.client.connector.quarixbackend.json.Message;

/**
 * @author TVH Group NV
 */
public class WicketDSRuntimeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Message exceptionBLMessage;

    /**
     * 
     */
    public WicketDSRuntimeException() {
        super("Unknown runtime exception!");
    }
    
    /**
     * 
     */
    public WicketDSRuntimeException(Message exceptionBLMessage) {
        super(exceptionBLMessage.getMessage());
        this.exceptionBLMessage = exceptionBLMessage;
    }

    /**
     * @param message
     */
    public WicketDSRuntimeException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public WicketDSRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public WicketDSRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public Message getBLErorMessage() {
		return this.exceptionBLMessage;
	}

}
