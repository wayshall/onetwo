package org.onetwo.ext.es;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.io.ClassPathResource;

public abstract class ESUtils {


	public static Map<Object, Object> readIndexMapping(String indexName){
		return readMapping("mapping/"+indexName+"-mapping.json");
	}
	
	public static Map<Object, Object> readMapping(String path){
		ClassPathResource resource = new ClassPathResource(path);
		try {
			Map<Object, Object> data = JsonMapper.ignoreNull().fromJson(resource.getInputStream(), Map.class);
			return data;
		} catch (IOException e) {
			throw new RuntimeException("read mapping error.", e);
		}
	}

	public static Optional<EsNode> getFirstNode(String nodestr){
		return parseEsNodes(nodestr).stream().findFirst();
	}
	
	public static List<EsNode> parseEsNodes(String nodestr){
		String[] nodes = StringUtils.split(nodestr, ",");
		return Stream.of(nodes).map(str->{
			String[] hostAndPort = StringUtils.split(str, ":");
			return new EsNode(hostAndPort[0], hostAndPort.length>1?hostAndPort[1]:"9200");
		})
		.collect(Collectors.toList());
	}
	
	public static boolean isValidGeoPoint(GeoPointFixer point){
		if (point.getLat() > 90.0 || point.getLat() < -90.0) {
            return false;
        }
        if (point.getLon() > 180.0 || point.getLon() < -180) {
            return false;
        }
        return true;
	}
	
	public static class EsNode {
		final private String host;
		final private String port;
		public EsNode(String host, String port) {
			super();
			this.host = host;
			this.port = port;
		}
		public String getHost() {
			return host;
		}
		public String getPort() {
			return port;
		}
		@Override
		public String toString() {
			return "EsNode [host=" + host + ", port=" + port + "]";
		}
		
	}
	private ESUtils(){
	}

}
