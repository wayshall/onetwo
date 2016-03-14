package org.onetwo.common.tree;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlType(name="TreeModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class DefaultTreeModel extends AbstractTreeModel<DefaultTreeModel> {

	public DefaultTreeModel() {
	    super();
    }

	public DefaultTreeModel(Object id, String name, Object parentId, Comparable<Object> sort) {
	    super(id, name, parentId, sort);
    }

	public DefaultTreeModel(Object id, String name, Object parentId) {
	    super(id, name, parentId);
    }

	public DefaultTreeModel(Object id, String name) {
	    super(id, name);
    }
	
	public void setSort(Comparable<?> sort){
		this.sort = sort;
	}
 
}
