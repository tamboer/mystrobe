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
 package net.mystrobe.client.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.mystrobe.client.WicketDSRuntimeException;
import net.mystrobe.client.connector.quarixbackend.NamingHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TVH Group NV
 */
public class StringToJavaNativeUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(StringToJavaNativeUtil.class);

	public static int SCALE = 2;
	
	private static ThreadLocal<Map<String, TimeZoneAwareDateFormatter>> dateFormatCacheMap = new ThreadLocal<Map<String, TimeZoneAwareDateFormatter>>();
    
	/**
	 * We need the formats to be able to send time formatted 
	 * 	values as filters where no schema is available.<br/>
	 * 
	 * Map holds all server time zone date formats. 
	 */
	public static Map<String,String> timeZoneFormatsMap = new HashMap<String, String>(4); 
	
	static {
		String dateTimeTzFormatDotNumericalSeparator = "99/99/9999 hh:mm:ss.sss+hh:mm";
		String dateTimeTzFormatCommaNumericalSeparator = "99/99/9999 hh:mm:ss,sss+hh:mm";
		
		StringBuilder keyBuilderDotSeparator = new StringBuilder("dmy").append("."); 
		StringBuilder keyBuilderDotSeparator2 = new StringBuilder("mdy").append("."); 
		
		timeZoneFormatsMap.put(keyBuilderDotSeparator.toString(), dateTimeTzFormatDotNumericalSeparator); 
		timeZoneFormatsMap.put(keyBuilderDotSeparator2.toString(), dateTimeTzFormatDotNumericalSeparator); 
		
		StringBuilder keyBuilderCommaSeparator = new StringBuilder("dmy").append(","); 
		StringBuilder keyBuilderCommaSeparator2 = new StringBuilder("mdy").append(","); 
		
		timeZoneFormatsMap.put(keyBuilderCommaSeparator.toString(), dateTimeTzFormatCommaNumericalSeparator); 
		timeZoneFormatsMap.put(keyBuilderCommaSeparator2.toString(), dateTimeTzFormatCommaNumericalSeparator);
	}
    
	public static int parseInt(String s) {
        if (s == null) {
            throw new NumberFormatException("Null string");
        }
        s = s.trim();
        
        // Check for a sign.
        int num = 0;
        int sign = -1;
        final int len = s.length();
        final char ch = s.charAt(0);
        if (ch == '-') {
            if (len == 1) {
                throw new NumberFormatException("Missing digits:  " + s);
            }
            sign = 1;
        } else {
            final int d = ch - '0';
            if (d >= 0 && d <= 9) {
                num = -d;
            }            
        }

        // Build the number.
        final int max = (sign == -1) ? -Integer.MAX_VALUE : Integer.MIN_VALUE;
        final int multmax = max / 10;
        int i = 1;
        while (i < len) {
            int d = s.charAt(i++) - '0';
            if (d >= 0 && d <= 9) {
                if (num < multmax) {
                    throw new NumberFormatException("Over/underflow:  " + s);
                }
                num *= 10;
                if (num < (max + d)) {
                    throw new NumberFormatException("Over/underflow:  " + s);
                }
                num -= d;
            }
        }
        return sign * num;
    }



    public static long parseLong(String s) {
        if (s == null) {
            throw new NumberFormatException("Null string");
        }
        s = s.trim();
        
        // Check for a sign.
        long num = 0L;
        int sign = -1;
        final int len = s.length();
        final char ch = s.charAt(0);
        if (ch == '-') {
            if (len == 1) {
                throw new NumberFormatException("Missing digits:  " + s);
            }
            sign = 1;
        } else {
            final int d = ch - '0';
            if (d >= 0 && d <= 9) {
                num = -d;
            }
        }

        // Build the number.
        final long max = (sign == -1) ? - Long.MAX_VALUE: Long.MIN_VALUE;
        final long multmax = max / 10;
        int i = 1;
        while (i < len) {
            int d = s.charAt(i++) - '0';
            if (d >= 0 && d <= 9) {
                if (num < multmax) {
                    throw new NumberFormatException("Over/underflow:  " + s);
                }
                num *= 10;
                if (num < (max + d)) {
                    throw new NumberFormatException("Over/underflow:  " + s);
                }
                num -= d;
            }
        }
        return  (sign * num);
    }

    public static BigDecimal parseDouble(String s, char decimalPoint, char thousandsSeparator) {
        if (s == null) {
            throw new NumberFormatException("Null string");
        }
        s = s.trim();

        int pos = s.indexOf(decimalPoint);
        int scale = s.substring(pos+1).length();
        
        String decimalFormatSymbolsKey = (new StringBuilder(thousandsSeparator).append(decimalPoint).toString());
    	Map<String, DecimalFormat> decimalFormatsMap = NamingHelper.getDecimalFormatsMap();
    	
    	DecimalFormat decimalFormat; 
    	
    	if (decimalFormatsMap.containsKey(decimalFormatSymbolsKey)) {
    		decimalFormat = decimalFormatsMap.get(decimalFormatSymbolsKey);
    	} else {
    		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        	symbols.setGroupingSeparator(thousandsSeparator);
        	symbols.setDecimalSeparator(decimalPoint);
        	
        	decimalFormat = new DecimalFormat();
        	decimalFormat.setDecimalFormatSymbols(symbols);
        	decimalFormatsMap.put(decimalFormatSymbolsKey, decimalFormat);
        }
    	
    	try {
			Number number =  decimalFormat.parse(s);
			BigDecimal result = new BigDecimal(number.doubleValue()).setScale(scale > SCALE ? scale : SCALE, BigDecimal.ROUND_HALF_UP);
			
			return result;
		
    	} catch (ParseException e) {
			logger.error("Uanble to parse number value:" + s, e);
			throw new WicketDSRuntimeException(e);
		}
    }


   public static TimeZoneAwareDateFormatter getDateFormatter(String format, String dateFormat, char decimalSeparator) {
    	
    	if ( format == null || dateFormat == null ) {
            throw new IllegalArgumentException("Null argument");
        }
        if (!"dmy".equalsIgnoreCase(dateFormat) && !"mdy".equalsIgnoreCase(dateFormat)) {
            throw new IllegalArgumentException("Incorect date format " + dateFormat);
        }
        
        if (dateFormatCacheMap.get() == null) {
        	dateFormatCacheMap.set(new HashMap<String, TimeZoneAwareDateFormatter>());
        }
        
        if (dateFormatCacheMap.get().containsKey(format + dateFormat)) {
            return dateFormatCacheMap.get().get(format + dateFormat);
        }
        
        
        String [] dateParts = format.split("\\s");
        
        char[] dateMask = dateParts[0].toCharArray();
        int idx = 0;
        dateFormat = dateFormat.toLowerCase();
        for (char ch : dateFormat.toCharArray()) {
            while (idx < dateMask.length && dateMask[idx] == '9') {
                switch( ch)  {
                    case 'm': dateMask[idx] = 'M'; break;
                    default: dateMask[idx] = ch;
                }
                idx++;
            }
            idx++;
        }
        
        char[] timeMask = null; 
        boolean timezone = false;
        if (dateParts.length >= 2 ) {
        	
        	String [] timeParts = dateParts[1].split("\\+|-");
        	timeMask = timeParts[0].toCharArray();
        	
        	idx = 0;
        	boolean milisecondsSeparatorParsed = false;
        	
        	for (char ch : timeParts[0].toCharArray()) {
        		switch( ch)  {
	                case 'h': timeMask[idx] = 'H'; 
	                	break;
	                case '.' :
	                case ',' :
	                	timeMask[idx] = decimalSeparator;
	                	milisecondsSeparatorParsed = true;
	                	break;
	                case 's' :
	                	if (milisecondsSeparatorParsed) {
	                		timeMask[idx] = 'S';
	                	}
	            }
        		idx++;
        	}
        	
        	if (timeParts.length == 2) {
        		timezone = true;
        	}
        }
        
        StringBuilder datePattern = new StringBuilder(String.valueOf(dateMask));
        if (timeMask != null) {
        	datePattern.append(" ").append(String.valueOf(timeMask));
        }
        
        if (timezone) {
        	datePattern.append("Z");
        }
        
        TimeZoneAwareDateFormatter sDateFormat = new TimeZoneAwareDateFormatter(datePattern.toString(), timezone);
        String dateFormatKey = (new StringBuilder(format)).append(dateFormat).append(decimalSeparator).toString();
        dateFormatCacheMap.get().put(dateFormatKey, sDateFormat);
        
        return sDateFormat;
    }
    
    public static Date parseDateFromType(String stringDate, String type) {
    	Date res = null;
    	try {
    		if (stringDate == null || type == null) {
                throw new IllegalArgumentException("Null argument");
            }
            
            boolean isTimeZoneAware = false; 
            String dateFormatPattern = "yyyy-MM-dd";
            if ("datetime".equals(type)) {
            	dateFormatPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            } else if ("datetime-tz".equals(type)) {
            	isTimeZoneAware = true;
            	dateFormatPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
            } 
            
            TimeZoneAwareDateFormatter dateFormat = null;
            
            if (dateFormatCacheMap.get() == null) {
            	dateFormatCacheMap.set(new HashMap<String, TimeZoneAwareDateFormatter>());
            }
            
            if (dateFormatCacheMap.get().containsKey(dateFormatPattern)) {
        		dateFormat =  dateFormatCacheMap.get().get(dateFormatPattern);
            }
            
            if (dateFormat == null) {
            	dateFormat = new TimeZoneAwareDateFormatter(dateFormatPattern, isTimeZoneAware);
            	dateFormatCacheMap.get().put(dateFormatPattern, dateFormat);
            }
            
            //This is a hack so that we can use standard date formatter to parse the input time zone strings
            if (isTimeZoneAware) {
            	int index = stringDate.lastIndexOf(":");
            	if ((stringDate.length() - index) < 5) {
            		StringBuilder sb = new StringBuilder(stringDate);
            		sb.replace(index, index + 1, "");
            		res = dateFormat.parse(sb.toString());
            	} else {
            		res = dateFormat.parse(stringDate);
            	}
            } else {
            	res = dateFormat.parse(stringDate);
            }
        } catch (ParseException ex) {
            LoggerFactory.getLogger(StringToJavaNativeUtil.class.getName()).error( null, ex);
        }
        return res;
    }
    
    
    public static Date parseDate(String s, String format, String dateFormat, char numericalSeparator) {
        try {
            if (s == null || format == null || dateFormat == null) {
                throw new IllegalArgumentException("Null argument");
            }
            if (!"dmy".equalsIgnoreCase(dateFormat) && !"mdy".equalsIgnoreCase(dateFormat)) {
                throw new IllegalArgumentException("Incorect date format " + dateFormat);
            }
            Date res = null;
            TimeZoneAwareDateFormatter sDateFormat = getDateFormatter(format, dateFormat, numericalSeparator);
            
            //This is a hack so that we can use standard date formatter to parse the input time zone strings
            if (sDateFormat.isTimeZoneAware()) {
            	int index = s.lastIndexOf(":");
            	if ((s.length() - index) < 5) {
            		StringBuilder sb = new StringBuilder(s);
            		sb.replace(index, index + 1, "");
            		res = sDateFormat.parse(sb.toString());
            	} else {
            		res = sDateFormat.parse(s);
            	}
            } else {
            	res = sDateFormat.parse(s);
            }
            
            return res;
            
        } catch (ParseException ex) {
            LoggerFactory.getLogger(StringToJavaNativeUtil.class.getName()).error( null, ex);
        }
        return null;
    }
    
    /**
     * Format date.
     * 
     * @param date Date to format.
     * @param format Date format.
     * @param dateFormat 'dmy' or 'mdy' month and day order server format.  
     * @param numericalSeparator Decimal separator.
     * 
     * @return Formatted date.
     */
    public static String formatDate(Date date, String format, String dateFormat, char numericalSeparator) {
    	 try {
             if (date == null || format == null || dateFormat == null) {
                 throw new IllegalArgumentException("Null argument");
             }
             if (!"dmy".equalsIgnoreCase(dateFormat) && !"mdy".equalsIgnoreCase(dateFormat)) {
                 throw new IllegalArgumentException("Incorect date format " + dateFormat);
             }
             
             TimeZoneAwareDateFormatter sDateFormat = getDateFormatter(format, dateFormat, numericalSeparator);
             String res = sDateFormat.format(date);
             
             //This is a hack so that we can use standard date formatter to parse the input time zone strings
             if (sDateFormat.isTimeZoneAware()) {
            	 StringBuilder sb = new StringBuilder();
            	 sb.append(res.substring(0, res.length()-2)).append(":").append(res.substring(res.length()-2, res.length()));
            	 return sb.toString();
             } else {
            	 return res;
             }
         } catch (Exception ex) {
             LoggerFactory.getLogger(StringToJavaNativeUtil.class.getName()).error( null, ex);
         }
         return null;
    }
    
    private static class TimeZoneAwareDateFormatter extends SimpleDateFormat {
    	
    	private static final long serialVersionUID = 1L;
		
    	private boolean isTimeZoneAware = false;
    	
    	public TimeZoneAwareDateFormatter(String pattern , boolean isTimeZoneAware) {
    		super(pattern);
    		this.isTimeZoneAware = isTimeZoneAware;
    	}

		/**
		 * @return the isTimeZoneAware
		 */
		public boolean isTimeZoneAware() {
			return isTimeZoneAware;
		}
    }
}
