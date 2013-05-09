package org.onetwo.common.utils.json;

import java.io.File;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.VariableExpositor;

public class JSONSupportConfig extends PropConfig{
	
	private JSONSupportConfig(String configName, boolean cacheable) {
		super(configName, cacheable);
	}

	private JSONSupportConfig(String name, File configFile, boolean cacheable) {
		super(name, configFile, cacheable);
	}

	private JSONSupportConfig(String configName, VariableExpositor expositor) {
		super(configName, expositor);
	}

	private JSONSupportConfig(String configName) {
		super(configName);
	}

	public JSONArray getJSONArray(String key) {
		String json = this.getVariable(key);

		//cache
		JSONArray result = (JSONArray) getFromCache(key);
		if(result!=null)
			return result;
		
		try {
			result = JSONUtils.getJSONArray(json);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(e);
		}
		
		putInCache(key, result);
		
		return result;
	}

	public JSONObject getJSONObject(String key) {
		String json = this.getVariable(key);

		//cache
		JSONObject result = (JSONObject) getFromCache(key);
		if(result!=null)
			return result;
		
		try {
			result = JSONUtils.getJsonData(json);
		} catch (Exception e) {
			throw new BaseException(e);
		}
		
		putInCache(key, result);
		
		return result;
	}
}
