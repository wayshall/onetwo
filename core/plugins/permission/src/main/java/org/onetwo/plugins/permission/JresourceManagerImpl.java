package org.onetwo.plugins.permission;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.TreeBuilder;
import org.onetwo.common.utils.TreeModel;
import org.onetwo.common.utils.TreeModelCreator;
import org.onetwo.common.utils.TreeUtils;
import org.slf4j.Logger;

public class JresourceManagerImpl {
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private Map<String, JResourceInfo> keyResources;
	private Map<String, JResourceInfo> idResources;
	private List<RescourceTreeModel> resourceTree;
	
	public JresourceManagerImpl(Map<String, JResourceInfo> jresources) {
		super();
		Map<String, JResourceInfo> keyResources = LangUtils.newHashMap(jresources.size());
		Map<String, JResourceInfo> idResources = LangUtils.newHashMap(jresources.size());
		for(JResourceInfo info : jresources.values()){
			if(jresources.containsKey(info.getId())){
				JResourceInfo oinfo = jresources.get(info.getId());
				if(info.isAssembleTag()){
					oinfo.addAssemble(info);
					info = oinfo;
				}else{
					info.addAssemble(oinfo);
				}
			}
			idResources.put(info.getId(), info);
		}
		this.idResources = Collections.unmodifiableMap(idResources);
		this.keyResources = Collections.unmodifiableMap(keyResources);
		
		List<JResourceInfo> datas = LangUtils.asList(jresources.values());
		TreeBuilder<RescourceTreeModel, JResourceInfo> treeBuilder = TreeUtils.newBuilder(datas, new TreeModelCreator<RescourceTreeModel, JResourceInfo>() {

			@Override
			public RescourceTreeModel createTreeModel(JResourceInfo obj) {
				return new RescourceTreeModel(obj);
			}
			
		});
		this.resourceTree = treeBuilder.buidTree();
		logger.info("tree: \n {} ", resourceTree);
	}
	
	public JResourceInfo getResourceInfoByKey(String key){
		return keyResources.get(key);
	}
	
	public JResourceInfo getResourceInfoById(String id){
		return idResources.get(id);
	}
	
	public static class RescourceTreeModel implements TreeModel<RescourceTreeModel> {

		private final JResourceInfo info;
		private final RescourceTreeModel parent;
		private List<RescourceTreeModel> children;
		
		public RescourceTreeModel(JResourceInfo info) {
			super();
			this.info = info;
			RescourceTreeModel parent = null;
			if(info.getParent()!=null)
				parent = new RescourceTreeModel(info.getParent());
			this.parent = parent;
		}

		@Override
		public void addChild(RescourceTreeModel node) {
			info.addChild(node.getInfo());
			if(children==null)
				children = LangUtils.newArrayList();
			this.children.add(node);
		}

		@Override
		public Object getParentId() {
			if(info.getParent()==null)
				return null;
			return info.getParent().getId();
		}

		@Override
		public Object getId() {
			return info.getId();
		}

		@Override
		public String getName() {
			return info.getLabel();
		}

		@Override
		public Comparable<?> getSort() {
			return info.getId();
		}

		public List<RescourceTreeModel> getChildren() {
			return children;
		}

		public JResourceInfo getInfo() {
			return info;
		}
		
		public RescourceTreeModel getParent(){
			return parent;
		}
		
		public String toString(){
			StringBuilder str = new StringBuilder();
			TreeUtils.buildString(str, this, "--");
			return str.toString();
		}
	}


}
