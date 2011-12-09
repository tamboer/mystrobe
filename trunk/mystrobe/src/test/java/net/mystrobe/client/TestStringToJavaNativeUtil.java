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
    public void testParseFloat1() {
        Assert.assertEquals( 12f, StringToJavaNativeUtil.parseFloat(" 12", '.').floatValue());
    }

    @Test
    public void testParseFloat2() {
        Assert.assertEquals( -12f, StringToJavaNativeUtil.parseFloat("-12", '.').floatValue());
    }

    @Test
    public void testParseFloat3() {
        Assert.assertEquals( -12f, StringToJavaNativeUtil.parseFloat(" -12", '.').floatValue());
    }


    @Test
    public void testParseFloat4() {
        Assert.assertEquals( -12f, StringToJavaNativeUtil.parseFloat(" -12 $", '.').floatValue());
    }


    @Test
    public void testParseFloat5() {
        Assert.assertEquals( 12f, StringToJavaNativeUtil.parseFloat("USD 12 $", '.').floatValue());
    }

    @Test
    public void testParseFloat6() {
    	Assert.assertEquals( 12.0f, StringToJavaNativeUtil.parseFloat(" 12.0", '.').floatValue());
    }

    @Test
    public void testParseFloat7() {
        Assert.assertEquals( -12f, StringToJavaNativeUtil.parseFloat("-12.0", '.').floatValue());
    }

    @Test
    public void testParseFloat8() {
        Assert.assertEquals( -12f, StringToJavaNativeUtil.parseFloat(" -12.0", '.').floatValue());
    }


    @Test
    public void testParseFloat9() {
        Assert.assertEquals( -12f, StringToJavaNativeUtil.parseFloat(" -12.0 $", '.').floatValue());
    }


    @Test
    public void testParseFloat10() {
        Assert.assertEquals( 12f, StringToJavaNativeUtil.parseFloat("USD 12.000 $", '.').floatValue());
    }

    @Test
    public void testParseFloat11() {
        Assert.assertEquals( -12f, StringToJavaNativeUtil.parseFloat(" -12.", '.').floatValue());
    }

    @Test
    public void testParseFloat12() {
        Assert.assertEquals( 62.62f, StringToJavaNativeUtil.parseFloat(" 62.62", '.').floatValue());
    }

    @Test
    public void testParseFloat13() {
        Assert.assertEquals( 0.297000000f, StringToJavaNativeUtil.parseFloat("0.297000000", '.').floatValue());
    }

    @Test
    public void testParseFloat14() {
        Assert.assertEquals( 3.367003367f, StringToJavaNativeUtil.parseFloat("3.367003367", '.').floatValue());
    }

    @Test
    public void testParseFloat15() {
        Assert.assertEquals( 1348.1481481481481f, StringToJavaNativeUtil.parseFloat("$ 1,348.1481481481481 %", '.').floatValue());
    }

    @Test
    public void testParseFloat16() {
        Assert.assertEquals( 1797693134862315800.1797693134862315800f, StringToJavaNativeUtil.parseFloat("$ 1797693134862315800.1797693134862315800 %", '.').floatValue());
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
    public void testParseDouble4() {
        Assert.assertEquals( -12d, StringToJavaNativeUtil.parseDouble(" -12 $", '.' , ',') .doubleValue());
    }


    @Test
    public void testParseDouble5() {
        Assert.assertEquals( 12d, StringToJavaNativeUtil.parseDouble("USD 12 $", '.' , ',').doubleValue());
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
    public void testParseDouble9() {
        Assert.assertEquals( -12d, StringToJavaNativeUtil.parseDouble(" -12.0 $", '.' , ',').doubleValue());
    }


    @Test
    public void testParseDouble10() {
        Assert.assertEquals( 12d, StringToJavaNativeUtil.parseDouble("USD 12.000 $", '.' , ',').doubleValue());
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
    public void testParseDouble15() {
        Assert.assertEquals( 1348.1481481481481d, StringToJavaNativeUtil.parseDouble("$ 1,348.1481481481481 %", '.' , ',').doubleValue());
    }																					 

    @Test
    public void testParseDouble16() {
        Assert.assertEquals( 1797693134862315800.1797693134862315800d, StringToJavaNativeUtil.parseDouble("$ 1797693134862315800.1797693134862315800 %", '.' , ',').doubleValue());
    }


    @Test
    public void testParseDouble17() {
        Assert.assertEquals( 12000.13d, StringToJavaNativeUtil.parseDouble("12.000,13", ',' , '.').doubleValue());
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
        GregorianCalendar calendar = new GregorianCalendar(2002, 4, 5, 7, 15, 3);
    	
        String formattedDate = StringToJavaNativeUtil.formatDate(calendar.getTime(),  "99/99/9999 hh:mm:ss", "mdy", ',');
    	
    	Assert.assertEquals( "Formatted date" , "05/05/2002 07:15:03", formattedDate);
    }
    
    @Test
    public void testDate15() {
        GregorianCalendar calendar = new GregorianCalendar(2002, 4, 5, 7, 15, 3);
    	calendar.set(GregorianCalendar.MILLISECOND, 123);
    	//calendar.setTimeZone(TimeZone.getTimeZone("GMT-05:00"));
    	
    	String formattedDate =  StringToJavaNativeUtil.formatDate(calendar.getTime(), "99/99/9999 hh:mm:ss.sss", "mdy", ',');
    	
    	Assert.assertEquals("Formatted date milliseconds ", "05/05/2002 07:15:03,123", formattedDate);
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