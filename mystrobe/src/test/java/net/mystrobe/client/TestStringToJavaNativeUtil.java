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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.Assert;

import net.mystrobe.client.util.StringToJavaNativeUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * @author TVH Group NV
 */
@RunWith(JUnit4.class)
public class TestStringToJavaNativeUtil {


    @Test
    public void testParseInt1() {
        Assert.assertEquals( 12, StringToJavaNativeUtil.parseInt(" 12"));
    }

    @Test
    public void testParseInt2() {
        Assert.assertEquals( -12, StringToJavaNativeUtil.parseInt("-12"));
    }

    @Test
    public void testParseInt3() {
        Assert.assertEquals( -12, StringToJavaNativeUtil.parseInt(" -12"));
    }


    @Test
    public void testParseInt4() {
        Assert.assertEquals( -12, StringToJavaNativeUtil.parseInt(" -12 $"));
    }


    @Test
    public void testParseInt5() {
        Assert.assertEquals( 12, StringToJavaNativeUtil.parseInt("USD 12 $"));
    }

    @Test
    public void testParseDouble1() {
        Assert.assertEquals( 12d, StringToJavaNativeUtil.parseDouble(" 12", '.' , ',').doubleValue());
    }

    @Test
    public void testParseDouble2() {
        Assert.assertEquals( -12d, StringToJavaNativeUtil.parseDouble("-12", '.' , ',').doubleValue());
    }

    @Test
    public void testParseDouble3() {
        Assert.assertEquals( -12d, StringToJavaNativeUtil.parseDouble(" -12", '.' , ',').doubleValue());
    }


    @Test
    public void testParseDouble6() {
        Assert.assertEquals( 12d, StringToJavaNativeUtil.parseDouble(" 12.0", '.' , ',').doubleValue());
    }

    @Test
    public void testParseDouble7() {
        Assert.assertEquals( -12d, StringToJavaNativeUtil.parseDouble("-12.0", '.' , ',').doubleValue());
    }

    @Test
    public void testParseDouble8() {
        Assert.assertEquals( -12d, StringToJavaNativeUtil.parseDouble(" -12.0", '.' , ',').doubleValue());
    }


    @Test
    public void testParseDouble11() {
        Assert.assertEquals( -12d, StringToJavaNativeUtil.parseDouble(" -12.", '.' , ',').doubleValue());
    }

    @Test
    public void testParseDouble12() {
        Assert.assertEquals( 62.62d, StringToJavaNativeUtil.parseDouble(" 62.62", '.' , ',').doubleValue());
    }

    @Test
    public void testParseDouble13() {
        Assert.assertEquals( 0.297000000d, StringToJavaNativeUtil.parseDouble("0.297000000", '.' , ',').doubleValue());
    }

    @Test
    public void testParseDouble14() {
        Assert.assertEquals( 3.367003367d, StringToJavaNativeUtil.parseDouble("3.367003367", '.' , ',').doubleValue());
    }

    @Test
    public void testParseDouble17() {
        Assert.assertEquals( 12000.13d, StringToJavaNativeUtil.parseDouble("12.000,13", ',' , '.').doubleValue());
    }
    
    @Test
    public void testParseDouble18() {
        Assert.assertEquals( 0.02d, StringToJavaNativeUtil.parseDouble("0.02", '.' , ',').doubleValue());
    }
    
    @Test
    public void testDate1() {
        Assert.assertEquals( (new GregorianCalendar(2010, 11, 31)).getTime(), StringToJavaNativeUtil.parseDate("31/12/10", "99/99/99", "dmy", ','));
    }

    @Test
    public void testDate2() {
        Assert.assertEquals( (new GregorianCalendar(2010, 11, 31)).getTime(), StringToJavaNativeUtil.parseDate("31/12/2010", "99/99/9999", "dmy", ','));
    }

    @Test
    public void testDate3() {
        Assert.assertEquals( (new GregorianCalendar(2010, 11, 31)).getTime(), StringToJavaNativeUtil.parseDate("31-12-2010", "99-99-9999", "dmy", ','));
    }

    @Test
    public void testDate4() {
        Assert.assertEquals( (new GregorianCalendar(2010, 11, 31)).getTime(), StringToJavaNativeUtil.parseDate("31-12-2010", "99-99-99", "dmy",','));
    }


    @Test
    public void testDate5() {
        Assert.assertEquals( (new GregorianCalendar(2010, 11, 31)).getTime(), StringToJavaNativeUtil.parseDate("12/31/10", "99/99/99", "mdy", ','));
    }

    @Test
    public void testDate6() {
        Assert.assertEquals( (new GregorianCalendar(2010, 11, 31)).getTime(), StringToJavaNativeUtil.parseDate("12/31/2010", "99/99/9999", "mdy", ','));
    }

    @Test
    public void testDate7() {
        Assert.assertEquals( (new GregorianCalendar(2010, 11, 31)).getTime(), StringToJavaNativeUtil.parseDate("12-31-2010", "99-99-9999", "mdy", ','));
    }

    @Test
    public void testDate8() {
        Assert.assertEquals( (new GregorianCalendar(2010, 11, 31)).getTime(), StringToJavaNativeUtil.parseDate("12-31-2010", "99-99-99", "mdy" , ','));
    }

    @Test
    public void testDate9() {
        Assert.assertEquals( (new GregorianCalendar(2011, 0, 1)).getTime(), StringToJavaNativeUtil.parseDate("01-01-11", "99-99-99", "mdy", ','));
    }
    
    //99/99/9999 hh:mm:ss.sss+hh:mm ; 05-05-2002 07:15:03-05:00;
    
    @Test
    public void testDate10() {
        
    	GregorianCalendar calendar = new GregorianCalendar(2002, 4, 5, 7, 15, 3);
    	//calendar.set(GregorianCalendar.MILLISECOND, 123);
    	
    	Date parsedDate = StringToJavaNativeUtil.parseDate("05/05/2002 07:15:03", "99/99/9999 hh:mm:ss", "mdy", ',');
    	
    	Assert.assertEquals( calendar.getTime(), parsedDate);
    	
    	//Assert.assertEquals( 1, 1);
    }
    
    @Test
    public void testDate11() {
        GregorianCalendar calendar = new GregorianCalendar(2002, 4, 5, 7, 15, 3);
    	calendar.set(GregorianCalendar.MILLISECOND, 123);
    	//calendar.setTimeZone(TimeZone.getTimeZone("GMT+05:00"));
    	Date parsedDate = StringToJavaNativeUtil.parseDate("05/05/2002 07:15:03,123", "99/99/9999 hh:mm:ss.sss", "mdy", ',');
    	Assert.assertEquals( calendar.getTime(), parsedDate);
    }
    
    @Test
    public void testDate12() {
        GregorianCalendar calendar = new GregorianCalendar(2002, 4, 5, 7, 15, 3);
    	calendar.set(GregorianCalendar.MILLISECOND, 123);
    	calendar.setTimeZone(TimeZone.getTimeZone("GMT+05:00"));
    	Date parsedDate = StringToJavaNativeUtil.parseDate("05/05/2002 07:15:03.123+0500", "99/99/9999 hh:mm:ss.sss+hh:mm", "mdy", '.');
    	
    	Assert.assertEquals( calendar.getTime(), parsedDate);
    }
    
    @Test
    public void testDate13() {
        GregorianCalendar calendar = new GregorianCalendar(2011, 4, 5, 0, 0, 0);
    	//calendar.set(GregorianCalendar.MILLISECOND, 123);
    	calendar.setTimeZone(TimeZone.getTimeZone("GMT+03:00"));
    	Date parsedDate = StringToJavaNativeUtil.parseDate("05/05/2011 00:00:00,000+0300", "99/99/9999 hh:mm:ss.sss+hh:mm", "mdy", ',');
    	
    	Assert.assertEquals( calendar.getTime(), parsedDate);
    }
    
    @Test
    public void testDate14() {
    	
    	String date = "2012-03-27T10:16:30.674";
    	String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    	
    	SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
    	Date parsedDate = null;
		try {
			parsedDate = dateFormatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	GregorianCalendar calendar = new GregorianCalendar(2012, 2, 27, 10, 16, 30);
    	calendar.set(GregorianCalendar.MILLISECOND, 674);
    	
    	Assert.assertEquals( calendar.getTime(), parsedDate);
    	
    	
    	date = "2012-03-27T10:16:30.674+03:00";
    	dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    	
    	SimpleDateFormat dateFormatterTimeZone = new SimpleDateFormat(dateFormat);
    	parsedDate = null;
		try {
			parsedDate = dateFormatterTimeZone.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	calendar.setTimeZone(TimeZone.getTimeZone("GMT+03:00"));
    	
    	Assert.assertEquals( calendar.getTime(), parsedDate);
    	
    	
//        GregorianCalendar calendar = new GregorianCalendar(2002, 4, 5, 7, 15, 3);
//    	
//        String formattedDate = StringToJavaNativeUtil.formatDate(calendar.getTime(),  "99/99/9999 hh:mm:ss", "mdy", ',');
//    	
//    	Assert.assertEquals( "Formatted date" , "05/05/2002 07:15:03", formattedDate);
    }
    
    @Test
    public void testDate15() {
        GregorianCalendar calendar = new GregorianCalendar(2002, 4, 5, 7, 15, 3);
    	calendar.set(GregorianCalendar.MILLISECOND, 123);
    	//calendar.setTimeZone(TimeZone.getTimeZone("GMT-05:00"));
    	
    	String formattedDate =  StringToJavaNativeUtil.formatDate(calendar.getTime(), "99/99/9999 hh:mm:ss.sss", "mdy", ',');
    	
    	Assert.assertEquals("Formatted date milliseconds ", "05/05/2002 07:15:03,123", formattedDate);
    }
    
    @Test
    public void testDate16() {
       
    	String date = "2012-03-27T10:16:30.674";
    	String dateFormat = "yyyy-MM-ddTHH:mm:ss.SSS";
    	
    	SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
    	Date parsedDate = null;
		try {
			parsedDate = dateFormatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	GregorianCalendar calendar = new GregorianCalendar(2012, 2, 27, 10, 16, 30);
    	calendar.set(GregorianCalendar.MILLISECOND, 674);
    	
    	Assert.assertEquals( calendar.getTime(), parsedDate);
    }
    
//    @Test
//    public void testDate16() {
//        GregorianCalendar calendar = new GregorianCalendar(2002, 4, 5, 7, 15, 3);
//    	calendar.set(GregorianCalendar.MILLISECOND, 123);
//    	calendar.setTimeZone(TimeZone.getTimeZone("GMT+03:00"));
//    	
//    	String formattedDate = StringToJavaNativeUtil.formatDate(calendar.getTime(), "99/99/9999 hh:mm:ss.sss+hh:mm", "mdy", ',');
//    	
//    	Assert.assertEquals( "Formatted date milliseconds && timezone", "05/05/2002 07:15:03,123+03:00" , formattedDate);
//    }
    //09/05/2011 13:33:27,416+03:00 
}
