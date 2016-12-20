package org.onetwo.common.utils.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class SynonymsMap {
	public static final String UTF8_BOM = "\uFEFF";
	
	/****
	 * 羊城, 花城   广州
		广州市    广州
		清远市   清远
	 * @return
	 */
	public static SynonymsMap buildFromClassPath(String path){
		SynonymsMap map = new SynonymsMap();
		map.loadFromFile(path);
		return map;
	}
	
	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map<String, Set<String>> listMapper;
	
	public SynonymsMap() {
		super();
//		this.listMapper = Multimaps.newSetMultimap(new HashMap<String, Collection<String>>(), ()->new HashSet<String>());
		this.listMapper = new HashMap<>();
	}
	
	public int size() {
		return listMapper.size();
	}

	public boolean isEmpty() {
		return listMapper.isEmpty();
	}

	public boolean containsKey(Object key) {
		return listMapper.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return listMapper.containsValue(value);
	}

	public Collection<String> get(String key) {
		return listMapper.get(key);
	}

	public boolean equals(Object obj) {
		return listMapper.equals(obj);
	}

	public int hashCode() {
		return listMapper.hashCode();
	}

	private List<String> readLines(String filePath) throws IOException{
		return readLines(()->{
			return FileUtils.getResourceAsStream(filePath);
		});
	}

	private List<String> readLines(Supplier<InputStream> inputSupplier) throws IOException{
		List<String> lines = Lists.newArrayList();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputSupplier.get(), "UTF-8"))) {
			String line = null;
			while((line = reader.readLine())!=null){
				if(line.startsWith(UTF8_BOM)){
					line = line.substring(1);
				}
				lines.add(line);
			}
		};
		return lines;
	}
	
	private void loadFromFile(String filePath){
		try {
			List<String> lines = readLines(filePath);
			lines.stream().forEach(line->{
				String[] kv = StringUtils.split(line, "=>");
				if(kv.length!=2)
					return ;
				String[] values = StringUtils.split(kv[0], ",");
				Set<String> valuesOfSet = Stream.of(values)
												.filter(StringUtils::isNotBlank)
												.map(StringUtils::trim)
												.collect(Collectors.toSet());
				String key = kv[1].trim();
				if(this.listMapper.containsKey(key)){
					Collection<String> colValues = this.listMapper.get(key);
					valuesOfSet.stream().forEach(e->{
						if(!colValues.contains(e)){
							colValues.add(e);
						}
					});
				}else{
					valuesOfSet.add(key);
					valuesOfSet.stream().forEach(e->this.listMapper.put(e, valuesOfSet));
				}
			});
		} catch (Exception e) {
			logger.error("read file[{}] error!", e);
		}
	}

}
