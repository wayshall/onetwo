package org.onetwo.common.db.filequery;

import java.util.List;
import java.util.Properties;

import org.onetwo.common.db.filequery.NamespacePropertiesFileManagerImpl.JFishPropertyConf;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.utils.propconf.ResourceAdapter;

/****
 * --@queryname
 * --@queryname.parser = template
 * 
 * @author way
 *
 * @param <T>
 */
public class OneCommentBasedSqlFileParser<T extends NamespaceProperty> extends DefaultSqlFileParser<T> implements SqlFileParser<T> {

//	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Override
	public void parseToNamespaceProperty(JFishPropertyConf<T> conf, PropertiesNamespaceInfo<T> np, ResourceAdapter<?> file) {
		JFishPropertiesData jproperties = loadSqlFile(conf, file);
		if(jproperties==null){
			return ;
		}
		logger.info("build [{}] sql file : {}", np.getNamespace(), file.getName());
		try {
			this.buildPropertiesAsNamedInfos(np, file, jproperties, conf.getPropertyBeanClass());
		} catch (Exception e) {
			throw new BaseException("build named info error in " + file.getName() + " : " + e.getMessage(), e);
		}
	}
	protected JFishPropertiesData loadSqlFile(JFishPropertyConf<T> conf, ResourceAdapter<?> f){
//		String fname = FileUtils.getFileNameWithoutExt(f.getName());
		if(!f.getName().endsWith(conf.getPostfix())){
			logger.info("file["+f.getName()+" is not a ["+conf.getPostfix()+"] file, ignore it.");
			return null;
		}
		
		JFishPropertiesData jpData = null;
		Properties pf = new Properties();
		Properties config = new Properties();
		try {
			List<String> fdatas = readResourceAsList(f);
			String key = null;
			StringBuilder value = null;
//			String line = null;

			boolean matchConfig = false;
			boolean matchName = false;
			boolean multiCommentStart = false;
			for(int i=0; i<fdatas.size(); i++){
				final String line = fdatas.get(i).trim();
				/*if(line.startsWith(COMMENT)){
					continue;
				}*/
				
				if(line.startsWith(MULTIP_COMMENT_START)){
					multiCommentStart = true;
					continue;
				}else if(line.endsWith(MULTIP_COMMENT_END)){
					multiCommentStart = false;
					continue;
				}
				if(multiCommentStart){
					continue;
				}
				if(line.startsWith(NAME_PREFIX)){//@开始到=结束，作为key，其余部分作为value
					if(value!=null){
						if(matchConfig){
							config.setProperty(key, value.toString());
						}else{
							pf.setProperty(key, value.toString());
						}
						matchName = false;
						matchConfig = false;
					}
					int eqIndex = line.indexOf(EQUALS_MARK);
					if(eqIndex==-1)
						LangUtils.throwBaseException("the jfish sql file lack a equals mark : " + line);
					
					if(line.startsWith(CONFIG_PREFIX)){
						matchConfig = true;
						key = line.substring(CONFIG_PREFIX.length(), eqIndex).trim();
					}else{
						matchName = true;
						key = line.substring(NAME_PREFIX.length(), eqIndex).trim();
					}
					value = new StringBuilder();
					value.append(line.substring(eqIndex+EQUALS_MARK.length()));
					value.append(" ");
				}else if(line.startsWith(COMMENT)){
					continue;
				}else{
					if(!matchName)
						continue;
//						if(value==null)
//							LangUtils.throwBaseException("can not find the key for value : " + line);
					value.append(line);
					value.append(" ");
				}
			}
			if(StringUtils.isNotBlank(key) && value!=null){
				pf.setProperty(key, value.toString());
			}

//			jp.setProperties(new JFishProperties(pf));
//			jp.setConfig(new JFishProperties(config));
			
			jpData = new JFishPropertiesData(new JFishProperties(pf), new JFishProperties(config));
			System.out.println("loaded jfish file : " + f.getName());
		} catch (Exception e) {
			LangUtils.throwBaseException("load jfish file error : " + f, e);
		}
		return jpData;
	}

}
