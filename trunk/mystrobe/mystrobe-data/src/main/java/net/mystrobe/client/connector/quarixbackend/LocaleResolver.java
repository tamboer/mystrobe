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

import java.util.HashMap;
import java.util.Locale;

/**
 * Face conversia dintre liba si locale;
 * @author TVH Group NV
 * @since 0.9
 */
public class LocaleResolver {

    protected static HashMap<String,Locale> localeCache = new HashMap<String,Locale>(); 
    protected static HashMap<String,Locale> localeCountryCache = new HashMap<String,Locale>();
    protected static HashMap<String,Locale> isoLocaleCache = new HashMap<String,Locale>();
    protected static String[] countryNames = null;
    protected static HashMap<String,Locale> localeContryLang = new HashMap<String,Locale>();
    
    static {
        java.util.Locale[] locales = java.util.Locale.getAvailableLocales();
        for( int idx = 0; idx < locales.length; idx ++) {
            if(!locales[idx].getCountry().equals("")) {
                localeContryLang.put(locales[idx].getLanguage(), locales[idx]);
            }            
        }
    }
    
    
    public static Locale getFullLocaleFromLanguage(String language) {
        Locale result = (Locale)localeContryLang.get(language);
        if( result == null ) {
            result = getLocale(language);
            if( result.getCountry().equals("")) result = Locale.US;
        }
        
        return result;
    }
    
    public static synchronized Locale getLocale(String language) {
        if( language == null || language.trim().equals("")) return null;
        
        if( localeCache.containsKey(language)) return (Locale) localeCache.get(language);
        
        Locale result = new Locale(language);
        localeCache.put(language, result);
        return result;
    }
    
    public static synchronized Locale getLocaleFromIso(String language) {
        if( language == null || language.trim().equals("")) return null;
        
        if( isoLocaleCache.containsKey(language)) return (Locale) isoLocaleCache.get(language);
        
        Locale result = new Locale(language);
        isoLocaleCache.put(language, result);
        return result;
    }
    
    public static synchronized Locale getLocaleFromCountry( String countryCode) {
        if( countryCode == null || countryCode .trim().equals("")) return null;
        
        if( localeCountryCache.containsKey(countryCode)) return (Locale) localeCountryCache.get(countryCode);
        
        Locale result = new Locale("",countryCode);
        localeCountryCache.put(countryCode, result);
        return result;
    }    
    
    public static synchronized String[] getCountryNames() {
        if( countryNames != null ) return countryNames;        
        
        String[] isoLanguageCodes = Locale.getISOCountries();
        countryNames = new String[isoLanguageCodes.length];
        
        for( int idx = 0 ; idx < isoLanguageCodes.length; idx++) {
            countryNames[idx] = LocaleResolver.getLocaleFromCountry(isoLanguageCodes[idx]).getDisplayCountry();
        }
        
        return countryNames;
    }
    
}
