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

import net.mystrobe.client.connector.quarixbackend.json.DSResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.LoggerFactory;


/**
 * @author TVH Group NV
 */
@RunWith(JUnit4.class)
public class JSONParserTest {



	@Test
	public void hasLastRow() {
            try {
                String json = "{\"tables\": [{\"id\": \"tt-state\",\"rows\":[[\"0x00000000000002f0\",\"0\",\"AK\",\"Alaska\",\"West\"],[\"0x00000000000002f4\",\"0\",\"CA\",\"California\",\"West\"],[\"0x00000000000002f5\",\"0\",\"CO\",\"Colorado\",\"West\"],[\"0x00000000000002f6\",\"0\",\"CT\",\"Connecticut\",\"East\"],[\"0x00000000000002f7\",\"0\",\"DC\",\"Dst of Columbia\",\"East\"],[\"0x00000000000002f8\",\"0\",\"DE\",\"Delaware\",\"East\"],[\"0x00000000000002f9\",\"0\",\"FL\",\"Florida\",\"East\"],[\"0x00000000000002fa\",\"0\",\"GA\",\"Georgia\",\"East\"],[\"0x00000000000002fb\",\"0\",\"HI\",\"Hawaii\",\"West\"],[\"0x00000000000002fc\",\"0\",\"IA\",\"Iowa\",\"Central\"]], \"info\":{\"changesOnly\": true , \"hasFirstRow\": true}}]}";
                ObjectMapper mapper = new ObjectMapper();
                mapper.readValue(json, DSResponse.class);
            } catch (IOException ex) {
                LoggerFactory.getLogger(JSONParserTest.class.getName()).error( null, ex);
            }
    }


	@Test
	public void hasLastRow2() {
            try {
                String json = "{\"tables\": [{\"id\": \"tt-state\",\"rows\":[[\"0x00000000000002f0\",\"0\",\"AK\",\"Alaska\",\"West\"],[\"0x00000000000002f4\",\"0\",\"CA\",\"California\",\"West\"],[\"0x00000000000002f5\",\"0\",\"CO\",\"Colorado\",\"West\"],[\"0x00000000000002f6\",\"0\",\"CT\",\"Connecticut\",\"East\"],[\"0x00000000000002f7\",\"0\",\"DC\",\"Dst of Columbia\",\"East\"],[\"0x00000000000002f8\",\"0\",\"DE\",\"Delaware\",\"East\"],[\"0x00000000000002f9\",\"0\",\"FL\",\"Florida\",\"East\"],[\"0x00000000000002fa\",\"0\",\"GA\",\"Georgia\",\"East\"],[\"0x00000000000002fb\",\"0\",\"HI\",\"Hawaii\",\"West\"],[\"0x00000000000002fc\",\"0\",\"IA\",\"Iowa\",\"Central\"]] , \"messages\": [{\"type\": \"1\", \"code\": \"100\" , \"msg\": \"Contact person not found in the system\"}] , \"info\":{\"changesOnly\": true , \"hasFirstRow\": true}}] , \"messages\": [{\"type\": \"0\", \"code\": \"101\" , \"msg\": \"Contact person not found in the system\"}]}";
                ObjectMapper mapper = new ObjectMapper();
                DSResponse result = mapper.readValue(json, DSResponse.class);
                
                if (result.getDSMessages() != null && !result.getDSMessages().isEmpty()) {
                	System.out.println( "Result messages :  " + result.getDSMessages().size());
                }
               
                
            } catch (IOException ex) {
                LoggerFactory.getLogger(JSONParserTest.class.getName()).error( null, ex);
            }
    }
	
}
