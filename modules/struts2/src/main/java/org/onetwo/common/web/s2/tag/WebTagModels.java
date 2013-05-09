package org.onetwo.common.web.s2.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.util.ValueStack;

public class WebTagModels {
    protected ValueStack stack;
    protected HttpServletRequest req;
    protected HttpServletResponse res;

    protected EditorModel editor;
    protected ParseModel parse;
    
    public WebTagModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        this.stack = stack;
        this.req = req;
        this.res = res;
    }

	public EditorModel getEditor() {
		if(editor==null)
			editor = new EditorModel(stack, req, res);
		return editor;
	}

	public void setEditor(EditorModel editor) {
		this.editor = editor;
	}

	public ParseModel getParse() {
		if(parse==null)
			parse = new ParseModel(stack, req, res);
		return parse;
	}

	public void setParse(ParseModel parse) {
		this.parse = parse;
	}
    
}
