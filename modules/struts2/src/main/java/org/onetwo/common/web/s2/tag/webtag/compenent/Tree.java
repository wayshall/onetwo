package org.onetwo.common.web.s2.tag.webtag.compenent;

import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsException;
import org.onetwo.common.utils.TreeBuilder;
import org.onetwo.common.utils.TreeModel;
import org.onetwo.common.web.s2.tag.RowType;
import org.onetwo.common.web.s2.tag.WebUIClosingBean;
import org.onetwo.common.web.s2.tag.component.StrutsTable;
import org.onetwo.common.web.s2.tag.component.StrutsTable.TableRowable;

import com.opensymphony.xwork2.util.ValueStack;

public class Tree extends WebUIClosingBean implements TableRowable {

	public static class TreeNodeInfo {
		TreeModel treeNode;
		TreeNodeInfo preTreeNodeInfo;
		Iterator<TreeModel> iterator;
		
		public TreeNodeInfo(TreeModel currentModel, TreeNodeInfo preTreeNodeInfo) {
			this.treeNode = currentModel;
			if (this.treeNode.getChildren() != null){
				this.iterator = this.treeNode.getChildren().iterator();
			}
			this.preTreeNodeInfo = preTreeNodeInfo;
		}

		public boolean hasNext() {
			if (this.iterator == null)
				return false;
			return this.iterator.hasNext();
		}

		public TreeModel next() {
			return this.iterator.next();
		}

	}

	public static enum TreeType {
		simple, xtree
	}

	protected StrutsTable parentTable;

	private String dataSource;
	private String treePath = "" ;
	private TreeType treeType = TreeType.simple;
	private TreeModel treeModel;

	private TreeNodeInfo currentNodeInfo;

	public Tree(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		parentTable = findAncestorByType(StrutsTable.class);
		if (this.parentTable != null) {
			addParameter("parentTable", parentTable);
			this.addParameter("tableName", parentTable.getParameters().get("name"));
			this.addParameter("formName", parentTable.getParameters().get("formName"));
		}
	}

	@Override
	public StrutsTable getParentTable() {
		return parentTable;
	}

	@Override
	public boolean isHeader() {
		return false;
	}

	public String getType() {
		return RowType.TREE;
	}

	protected void evaluateExtraParams() {
		if(this.treeModel==null){
			Object modelData = null;
			super.evaluateExtraParams();
			
			modelData = this.findValue(dataSource);
			if(this.getParentTable()!=null){
				setDefaultUITheme(this.getParentTable().getTheme());
				if(modelData==null)
					modelData = this.getParentTable().getPage().getResult();
			}
			if (modelData instanceof List) {
				List mt = (List) modelData;
				if(mt.size()==1)
					treeModel = (TreeModel) mt.get(0);
				else{
					treeModel = new TreeModel(0l, "root");
					treeModel.setChildren(mt);
				}
			} else {
				treeModel = (TreeModel) modelData;
			}
			this.addParameter("dataSource", this.dataSource);
			this.addParameter("treePath", this.treePath);
		}
		addParameter("isHeader", this.isHeader());
		addParameter("isIterator", false);
		addParameter("isPage", false);
		addParameter("isRow", false);
//		this.addParameter("treeModel", treeModel);
		
	}

	public boolean endTag(Writer writer, String body) {
		return super.end(writer, body);
	}

	@Override
	public boolean start(Writer writer) {
        evaluateParams();
        
		if (treeModel == null)
			return false;
		
		this.currentNodeInfo = new TreeNodeInfo(treeModel, null);
		this.getStack().push(this.currentNodeInfo.treeNode);
		this.putInContext(currentNodeInfo.treeNode);
		this.addParameter("currentModel", this.currentNodeInfo.treeNode);

        try {
            mergeTemplate(writer, buildTemplateName(template, getDefaultOpenTemplate()));
        } catch (Exception e) {
            e.printStackTrace();
        }
		return true;
	}

	public boolean end(Writer writer, String body) {
		if (TreeType.xtree.equals(treeType)) {
			return super.end(writer, body);
		}
		if (this.currentNodeInfo == null) {
			return false;
		}
//		System.out.println("name: " + this.currentNodeInfo.treeNode.getName());

		try {
			super.end(writer, body, false);
			mergeTemplate(writer, buildTemplateName(template, getDefaultTemplate()));
		} catch (Exception e) {
			throw new StrutsException(e);
		}

		while(true){
			if (this.currentNodeInfo.hasNext()) {
				this.currentNodeInfo = new TreeNodeInfo(this.currentNodeInfo.next(), this.currentNodeInfo);
				break;
			} else {
				this.currentNodeInfo = this.currentNodeInfo.preTreeNodeInfo;
				if(this.currentNodeInfo==null)
					return false;
			}
		}
		

		ValueStack stack = getStack();
		stack.pop();
		stack.push(currentNodeInfo.treeNode);
		this.putInContext(currentNodeInfo.treeNode);
		
		evaluateParams();

		super.start(writer);
		return true;
	}
	
	protected void putInContext(Object value) {
		if (StringUtils.isNotBlank(name))
			stack.getContext().put(name, value);
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public void setTreeType(String type) {
		if (StringUtils.isNotBlank(type))
			this.treeType = TreeType.valueOf(type);
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}


	public static void main(String[] args) {

		TreeModel t1 = new TreeModel(1, "中国");
		
		TreeModel t2 = new TreeModel(2, "广东", 1);
		
		TreeModel t3 = new TreeModel(3, "广西", 1);
		
		TreeModel t4 = new TreeModel(4, "广州", 2);
		
		TreeModel t5 = new TreeModel(5, "桂林", 3);
		
		List list = Arrays.asList(t1, t2, t3, t4, t5);
		
		TreeBuilder tb = new TreeBuilder(list);
		TreeModel treeModel = (TreeModel)tb.buidTree().get(0);
		List<TreeModel> trlist = treeModel.toList();
		
		for(TreeModel tm : trlist){
			System.out.println(tm.getName());
		}
		
	}

}
