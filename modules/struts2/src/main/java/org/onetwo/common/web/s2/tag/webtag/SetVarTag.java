package org.onetwo.common.web.s2.tag.webtag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.SetTag;
import org.onetwo.common.web.s2.tag.webtag.compenent.SetVar;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class SetVarTag extends SetTag{

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new SetVar(stack);
    }

}
