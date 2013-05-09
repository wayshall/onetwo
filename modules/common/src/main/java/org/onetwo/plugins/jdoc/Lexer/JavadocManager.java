package org.onetwo.plugins.jdoc.Lexer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.file.FileCallback;
import org.onetwo.common.utils.file.FileScanner;
import org.onetwo.plugins.jdoc.Lexer.defined.JavaClassDefineImpl;
import org.onetwo.plugins.jdoc.Lexer.parser.JavaSourceParser;
import org.onetwo.plugins.jdoc.data.JClassDoc;
import org.onetwo.plugins.jdoc.data.JMethodDoc;
import org.slf4j.Logger;

public class JavadocManager {

	public static class JFileState {
		private File file;
		private long lastModified;
		private JSourceParser parser;
		
		public JFileState(File file, JSourceParser parser) {
			super();
			this.file = file;
			this.lastModified = file.lastModified();
			this.parser = parser;
		}
		
		public boolean isModified(){
			long newModified = file.lastModified();
			boolean rs = newModified>this.lastModified;
			if(rs){
				this.lastModified = newModified;
			}
			return rs;
		}

		public JSourceParser getParser() {
			return parser;
		}
		
	}
	
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private FileScanner docScaner;
	private JavaDefinedMapper<?> mapper = new JavaDefinedMapperImpl();
	private String[] pathToScan;
//	@Autowired
//	private TextMarkReader textMarkReader;

	private Map<String, JFileState> fileStateCaches = new HashMap<String, JFileState>();
	private Map<String, JClassDoc> classDocs;
	
	private Map<String, JMethodDoc> methodDocsKeyMap;
	
	private Map<String, JMethodDoc> methodDocsNameMap;
	private boolean debug = true;
	
	public JMethodDoc findMethodDocByKey(String key){
//		this.checkCache();
		JMethodDoc mdoc = this.methodDocsKeyMap.get(key);
		if(mdoc!=null)
			return checkUpdate(mdoc);
		for(String mkey : methodDocsKeyMap.keySet()){
			if(mkey.startsWith(key)){
				mdoc = methodDocsKeyMap.get(mkey);
				break;
			}
		}
		if(mdoc==null)
			return null;
		return checkUpdate(mdoc);
	}
	
	public JMethodDoc findMethodDocByName(String name){
//		this.checkCache();
		JMethodDoc mdoc = this.methodDocsNameMap.get(name);
		if(mdoc!=null)
			return checkUpdate(mdoc);
		for(String mkey : methodDocsNameMap.keySet()){
			if(mkey.startsWith(name)){
				mdoc = methodDocsNameMap.get(mkey);
				break;
			}
		}
		return checkUpdate(mdoc);
	}
	
	
	public JClassDoc findClassDocByName(String name){
		JClassDoc jcd = this.classDocs.get(name);
		return checkUpdate(jcd);
	}
	public Collection<JClassDoc> getClassDocs(){
		return new ArrayList<JClassDoc>(classDocs.values());
	}
	
	/*private void checkCache(){
		boolean flag = true;
		if(flag){
			this.startScanDoc();
		}
	}*/
	
	protected JClassDoc buildJClassDoc(File source){
		JSourceParser parser = new JavaSourceParser(source);
		JClassDoc jcdoc = (JClassDoc)this.mapper.map((JavaClassDefineImpl)parser.parse(null));
		if(jcdoc!=null){
			JFileState state = new JFileState(source, parser);
			this.fileStateCaches.put(jcdoc.getFullName(), state);
		}
		return jcdoc;
	}
	

	protected JMethodDoc checkUpdate(JMethodDoc jmd){
		JClassDoc jcd = this.checkUpdate(jmd.getClassDoc());
		if(jcd==null)
			return jmd;
		
		JMethodDoc mdoc = jcd.getMethod(jmd.getFullName());
		
		methodDocsKeyMap.put(mdoc.getKey(), mdoc);
		methodDocsNameMap.put(mdoc.getFullName(), mdoc);
		
		return mdoc;
	}
	
	protected JClassDoc checkUpdate(JClassDoc jcd){
		if(jcd==null || LangUtils.isEmpty(fileStateCaches))
			return jcd;
		
		JFileState fileState = this.fileStateCaches.get(jcd.getFullName());
		if(fileState==null){
			return jcd;
		}
		if(fileState.isModified()){
			JClassDoc newDoc = (JClassDoc)this.mapper.map((JavaClassDefineImpl)fileState.getParser().reparse(null));
			if(newDoc!=null){
				logger.info("already reparse java doc : " + jcd.getFullName());
				this.classDocs.put(newDoc.getFullName(), newDoc);
				return newDoc;
			}
		}
		return jcd;
	}
	
//	@PostConstruct
	public void startScanDoc(){
		List<JClassDoc> jclassDocs = docScaner.scan(new FileCallback<JClassDoc>() {

			@Override
			public JClassDoc doWithCandidate(File source, int count) {
				if(debug){
					System.out.println("find file: " + source);
				}

				return buildJClassDoc(source);
			}
			
		}, pathToScan);

		this.putIntoCaches(jclassDocs);
		
		debug = false;
	}
	

	public void setJClassDoc(List<JClassDoc> jclassDocs){
		this.putIntoCaches(jclassDocs);
	}
	
	protected void putIntoCaches(List<JClassDoc> jclassDocs){
		classDocs = new HashMap<String, JClassDoc>();
		methodDocsKeyMap = new HashMap<String, JMethodDoc>();
		methodDocsNameMap = new HashMap<String, JMethodDoc>();
		
		if(debug)
			logger.info("==================java doc========================");
		for(JClassDoc clsDoc : jclassDocs){
			classDocs.put(clsDoc.getFullName(), clsDoc);
			
			if(debug)
				logger.info("class: " + clsDoc.getName());
			for(JMethodDoc methodDoc : clsDoc.getMethods()){
				methodDocsKeyMap.put(methodDoc.getKey(), methodDoc);
				methodDocsNameMap.put(methodDoc.getFullName(), methodDoc);
				if(debug)
					logger.info("       method: " + methodDoc.getName());
			}
		}
		if(debug)
			logger.info("==================java doc========================");
	}

	public FileScanner getDocScaner() {
		return docScaner;
	}

	public void setDocScaner(FileScanner docScaner) {
		this.docScaner = docScaner;
	}

	public JavaDefinedMapper<?> getMapper() {
		return mapper;
	}

	public void setMapper(JavaDefinedMapper<?> mapper) {
		this.mapper = mapper;
	}

	public void setPathToScan(String... pathToScan) {
		this.pathToScan = pathToScan;
	}

	
	
}
