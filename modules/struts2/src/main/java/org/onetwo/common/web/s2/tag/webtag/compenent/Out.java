package org.onetwo.common.web.s2.tag.webtag.compenent;

import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.xwork.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsException;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.s2.tag.FCKUtils;
import org.onetwo.common.web.s2.tag.WebUIClosingBean;
import org.onetwo.common.web.utils.Tool;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("unchecked")
public class Out extends WebUIClosingBean {
	private static final Logger LOG = Logger.getLogger(Out.class);

	public static final String DATA_KEY = "data";
	
	public Out(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	private String defaultValue;
	private String value;
	private boolean escapeHtml = true;
	private boolean escapeJavaScript = false;
	private boolean escapeXml = false;
	private boolean escapeCsv = false;

	protected Integer textLength;
	protected String condition;
	protected String otherwise;
	protected String format;
	protected Integer dataIndex;
	protected boolean filterHTML;

	@StrutsTagAttribute(description = "The default value to be used if <u>value</u> attribute is null")
	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@StrutsTagAttribute(description = "Deprecated. Use 'escapeHtml'. Whether to escape HTML", type = "Boolean", defaultValue = "true")
	public void setEscape(boolean escape) {
		this.escapeHtml = escape;
	}

	@StrutsTagAttribute(description = "Whether to escape HTML", type = "Boolean", defaultValue = "true")
	public void setEscapeHtml(boolean escape) {
		this.escapeHtml = escape;
	}

	@StrutsTagAttribute(description = "Whether to escape Javascript", type = "Boolean", defaultValue = "false")
	public void setEscapeJavaScript(boolean escapeJavaScript) {
		this.escapeJavaScript = escapeJavaScript;
	}

	@StrutsTagAttribute(description = "Value to be displayed", type = "Object", defaultValue = "&lt;top of stack&gt;")
	public void setValue(String value) {
		this.value = value;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@StrutsTagAttribute(description = "Whether to escape CSV (useful to escape a value for a column)", type = "Boolean", defaultValue = "false")
	public void setEscapeCsv(boolean escapeCsv) {
		this.escapeCsv = escapeCsv;
	}

	@StrutsTagAttribute(description = "Whether to escape XML", type = "Boolean", defaultValue = "false")
	public void setEscapeXml(boolean escapeXml) {
		this.escapeXml = escapeXml;
	}

	public void setTextLength(Integer textLength) {
		this.textLength = textLength;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	protected boolean checkPopValue() {
		if (dataIndex == null)
			return false;
		FetchData fd = (FetchData) this.findAncestor(FetchData.class);
		if (fd != null && fd.datas != null && List.class.isAssignableFrom(fd.datas.getClass())) {
			List datalist = (List) fd.datas;
			if(dataIndex<0 || dataIndex>datalist.size()-1)
				throw new IndexOutOfBoundsException("size:"+datalist.size()+", index:"+dataIndex+", datasource:"+fd.dataSource);
			Object m = datalist.get(dataIndex);
			if (m != null) {
				getStack().getContext().put(DATA_KEY, m);
				getStack().push(m);
				return true;
			}
		}
		return false;
	}

	public boolean start(Writer writer) {
		super.evaluateParams();

		Object actualValue = null;

		if (value == null) {
			value = "top";
		} else {
			value = stripExpressionIfAltSyntax(value);
		}

		boolean popValue = false;
		try {

			// exception: don't call findString(), since we don't want the
			// expression parsed in this one case. it really
			// doesn't make sense, in fact.
			popValue= checkPopValue();
			try {
				actualValue = getStack().findValue(value, true);
			} catch (Exception e) {
				//return false;
			}
			
			if (this.condition != null) {
				boolean cond = (Boolean) getStack().findValue(this.condition);
				if (!cond) {
					actualValue = getStack().findValue(this.otherwise, throwExceptionOnELFailure);
				}
			}

			if (actualValue != null) {
				if (Date.class.isAssignableFrom(actualValue.getClass())) {
					actualValue = DateUtil.format(this.format, (Date) actualValue);
				}
				if (Number.class.isAssignableFrom(actualValue.getClass()) && format != null) {
					DecimalFormat df = new DecimalFormat();
					df.applyLocalizedPattern(this.format);
					actualValue = df.format(actualValue);
				}
			}

			if(template != null && template.length() > 0) {
				getStack().getContext().put(DATA_KEY, actualValue);
				mergeTemplate(writer, buildTemplateName(template, getDefaultTemplate()));
			} else if (actualValue != null && StringUtils.isNotBlank(actualValue.toString())) {
				writer.write(prepare(actualValue.toString()));
			} else if (defaultValue != null) {
				writer.write(prepare(defaultValue));
			}
		} catch (Exception e) {
			LOG.info("Could not print out value '" + value + "'", e);
		} finally {
			if (popValue)
				getStack().pop();
		}

		return true;
	}

	@Override
	public boolean end(Writer writer, String body) {
		evaluateParams();
        try {
            super.end(writer, body, false);
        } catch (Exception e) {
            throw new StrutsException(e);
        }
        finally {
            popComponentStack();
        }

        return false;
	}
	
	private String prepare(String value) {
		String result = FCKUtils.decodeFckText(value);
		if(filterHTML) {
			result = Tool.getInstance().filterHtml(result);
		}
		if (this.textLength != null && this.textLength > 0) {
			result = MyUtils.subString(result, textLength);
		}
		if (escapeHtml) {
			result = StringEscapeUtils.escapeHtml(result);
		}
		if (escapeJavaScript) {
			result = StringEscapeUtils.escapeJavaScript(result);
		}
		if (escapeXml) {
			result = StringEscapeUtils.escapeXml(result);
		}
		if (escapeCsv) {
			result = StringEscapeUtils.escapeCsv(result);
		}

		return result;
	}

	public void setOtherwise(String otherwise) {
		this.otherwise = otherwise;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setDataIndex(Integer dataIndex) {
		this.dataIndex = dataIndex;
	}

	public void setFilterHTML(boolean filterHTML) {
		this.filterHTML = filterHTML;
	}

}
