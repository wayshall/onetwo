package org.onetwo.common.web.s2.tag.webtag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.onetwo.common.web.s2.tag.WebUIClosingTag;
import org.onetwo.common.web.s2.tag.webtag.compenent.Tree;

import com.opensymphony.xwork2.util.ValueStack;

public class TreeTag extends WebUIClosingTag {
	
	private String dataSource;
	private String treeType;
	private String treePath;

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new Tree(stack, req, res);
	}

    protected void populateParams() {
        super.populateParams();

        Tree tree = ((Tree) component);
        tree.setDataSource(dataSource);
        tree.setTreeType(treeType);
        tree.setTreePath(treePath);
    }
    

    public int doAfterBody() throws JspException {
        boolean again = component.end(pageContext.getOut(), getBody());

        if (again) {
            return EVAL_BODY_AGAIN;
        } else {
            if (bodyContent != null) {
                try {
                    bodyContent.writeOut(bodyContent.getEnclosingWriter());
                } catch (Exception e) {
                    throw new JspException(e.getMessage());
                }
            }
            return SKIP_BODY;
        }
    }
	
    public int doEndTag() throws JspException {
    	this.getComponent().endTag(pageContext.getOut(), getBody());
        component = null;
        return EVAL_PAGE;
    }

    public Tree getComponent() {
        return ((Tree) component);
    }


	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}


}
