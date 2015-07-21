package org.onetwo.easyui;

import java.util.List;

import org.onetwo.common.utils.TreeBuilder;
import org.onetwo.common.utils.map.MappableMap.MappingValueFunc;
import org.onetwo.common.utils.map.ObjectMappingBuilder;
import org.onetwo.easyui.EasyBuilder.SimpleEasyBuilder;

final public class EasyModel {
	public static <T> SimpleEasyBuilder<T> newSimpleBuilder(Class<T> clazz){
		return new SimpleEasyBuilder<T>();
	}
	public static <T> EasyTreeBuilder<T> newTreeBuilder(Class<T> clazz){
		return new EasyTreeBuilder<T>();
	}
	public static <T> EasyChildrenTreeModelBuilder<T> newChildrenTreeBuilder(Class<T> clazz){
		return new EasyChildrenTreeModelBuilder<T>(ObjectMappingBuilder.newBuilder(clazz, EasyChildrenTreeModel.class));
	}
	public static <T> EasyComboBoxBuilder<T> newComboBoxBuilder(Class<T> clazz){
		return new EasyComboBoxBuilder<T>();
	}
	
	public static class EasyChildrenTreeModelBuilder<E> {
		private ObjectMappingBuilder<E, EasyChildrenTreeModel> builder;

		public EasyChildrenTreeModelBuilder(
				ObjectMappingBuilder<E, EasyChildrenTreeModel> builder) {
			super();
			this.builder = builder;
		}

		public EasyChildrenTreeModelBuilder<E> mapId(String fieldName){
			builder.addMapping("id", fieldName);
			return this;
		}

		public EasyChildrenTreeModelBuilder<E> mapText(String fieldName){
			builder.addMapping("text", fieldName);
			return this;
		}

		public EasyChildrenTreeModelBuilder<E> mapParentId(String fieldName){
			builder.addMapping("parentId", fieldName);
			return this;
		}
		
		public EasyChildrenTreeModel build(List<E> sourceObjects, String rootNode){
			List<EasyChildrenTreeModel> modelList = builder.bindValues(sourceObjects);
			List<EasyChildrenTreeModel> rootTree = new TreeBuilder<>(modelList)
														.rootIds(rootNode)
														.buidTree();
		    return rootTree.get(0);
		}
		
	}
	public static class EasyTreeBuilder<E> extends EasyBuilder<EasyTreeBuilder<E>, E>{
		
		public EasyTreeBuilder<E> mapId(String fieldName){
			addMapping("id", fieldName);
			return this;
		}
		
		public EasyTreeBuilder<E> mapState(String fieldName){
			addMapping("state", fieldName);
			return this;
		}
		
		public EasyTreeBuilder<E> mapText(String fieldName){
			addMapping("text", fieldName);
			return this;
		}
		
		/***
		 * true: open
		 * false: closed
		 * @param stateFunc
		 * @return
		 */
		public EasyTreeBuilder<E> mapIsStateOpen(MappingValueFunc<E, Boolean> stateFunc){
			addMapping("state", src->stateFunc.mapping(src)?"open":"closed");
//			addMapping("state", new StateMappingValueFunc(stateFunc));
			return this;
		}

	}
	
	
	public static class EasyComboBoxBuilder<E> extends EasyBuilder<EasyComboBoxBuilder<E>, E>{
		public EasyComboBoxBuilder<E> mapValue(String fieldName){
			addMapping("value", fieldName);
			return this;
		}

		public EasyComboBoxBuilder<E> mapText(String fieldName){
			addMapping("text", fieldName);
			return this;
		}
		public EasyComboBoxBuilder<E> mapSelected(MappingValueFunc<E, Boolean> selected){
			addMapping("selected", selected);
			return this;
		}
	}
	
	private EasyModel(){}

}
