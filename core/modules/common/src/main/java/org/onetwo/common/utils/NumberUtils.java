package org.onetwo.common.utils;

/***********
 * copy from apache common lang
 * 
 * @author wayshall
 *
 */
public abstract class NumberUtils {
	
	public static Long getLong(Object val, Long def){
		if(val==null)
			return def;
		if(!(val instanceof Long)){
			try {
				return Long.parseLong(val.toString());
			} catch (Exception e) {
				return def;
			}
		}else{
			return (Long) val;
		}
	}
	
	public static Integer getInteger(Object val, Integer def){
		if(val==null)
			return def;
		if(!(val instanceof Integer)){
			try {
				return Integer.parseInt(val.toString());
			} catch (Exception e) {
				return def;
			}
		}else{
			return (Integer) val;
		}
	}

    public static double toDouble(String str) {
        return toDouble(str, 0.0d);
    }
    
    public static double toDouble(String str, double defaultValue) {
      if (str == null) {
          return defaultValue;
      }
      try {
          return Double.parseDouble(str);
      } catch (NumberFormatException nfe) {
          return defaultValue;
      }
    }
	
    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    public static long toLong(String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
    
    public static int toInt(String str) {
        return toInt(str, 0);
    }

    public static int toInt(String str, int defaultValue) {
        if(str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
    public static float toFloat(String str) {
        return toFloat(str, 0.0f);
    }

    public static float toFloat(String str, float defaultValue) {
      if (str == null) {
          return defaultValue;
      }     
      try {
          return Float.parseFloat(str);
      } catch (NumberFormatException nfe) {
          return defaultValue;
      }
    }
	public static void main(String[] args){
		System.out.println(getLong("1a", 0l));
	}
}
