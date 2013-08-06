package org.onetwo.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;



public class DateConverter implements Converter {
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        }
        if(type==value.getClass())
        	return value;
        if (type == Timestamp.class) {
            return convertToDate(type, value, "yyyy-MM-dd HH:mm:ss");
        } else if (type == Date.class) {
            return convertToDate(type, value, "yyyy-MM-dd");
        } else if (type == String.class) {
            return convertToString(type, value);
        }

        throw new ConversionException("Could not convert " +
                                      value.getClass().getName() + " to " +
                                      type.getName());
    }

    protected Object convertToDate(Class type, Object value, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        if (value instanceof String) {
            try {
                if (StringUtils.isEmpty(value.toString())) {
                    return null;
                }

                Date date = df.parse((String) value);
                if (type.equals(Timestamp.class)) {
                    return new Timestamp(date.getTime());
                }
                return date;
            } catch (Exception pe) {
                pe.printStackTrace();
                throw new ConversionException("Error converting String to Date");
            }
        }

        throw new ConversionException("Could not convert " +
                                      value.getClass().getName() + " to " +
                                      type.getName());
    }

    protected Object convertToString(Class type, Object value) {        

        if (value instanceof Date) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (value instanceof Timestamp) {
                df = new SimpleDateFormat("HH:mm:ss");
            } 
    
            try {
                return df.format(value);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ConversionException("Error converting Date to String");
            }
        } else {
            return value.toString();
        }
    }
}
