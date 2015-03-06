/*
 * Copyright (c) Constantin Mircea Moisei 2005 for AjaxAnywhere
 * 
 * $Header: /cvsroot/ajaxanywhere/ajaxAnywhere/src/java/org/ajaxanywhere/LinkTag.java,v 1.6 2006/05/28 22:47:36 shevit Exp $ 
 * $Revision: 1.6 $ 
 * $Date: 2006/05/28 22:47:36 $
 * 
 */
package org.ajaxanywhere;


import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 *  <b>org.ajaxanywhere.LinkTag</b> <br>
 * 
 * It's for the failover mechanism. It's going to be transparent for the user how it works.
 * 
 * <p><u>Usage</u></p>
 * <code>
 * <aa:link href="/some.url" zones="content,menu"/>Ajax Link<aa:link><br>
 * <aa:link href="/some.url" zones="content,menu" functionName="ajaxanywhere.getAJAXAndClean" />Ajax Link<aa:link><br>
 * <aa:link  href="/some.url" zones="zone1, zone2" functionName="openLinkTop" style="color:red" styleClass="class">Ajax Link</aa:link>
 * </code>
 * 
 * <p>The tag will expand to <br>
 * <a href="/some.url" onclick="ajaxanywhere.getAjax( '/some.url', 'content,menu');return false;">Ajax Link</a><br>
  * <a href="/some.url" onclick="ajaxanywhere.getAJAXAndClean( '/some.url', 'content,menu');return false;">Ajax Link</a><br>
 *  <a href="/some.url" onclick="openLinkTop( '/some.url', 'content,menu');return false;" style="color:red" class="class">Ajax Link</a><br>
 * 
 * </p>
 * 
 * <p>getAJAXAndClean and openLinkTop are given as an example and are not a part of AjajxAnywhere distribution</p>
 * 
 * @author Constantin Moisei
 * @version $Revision: 1.6 $
 * @since Feb 18, 2006 10:48:52 AM
 * 
 * @jsp.tag name = "link"
 *
 */
public class LinkTag extends BodyTagSupport //org.apache.taglibs.standard.tag.el.core.UrlTag
{

    /**
     * Comment for <code>serialVersionUID</code> long
     */
    private static final long serialVersionUID = 1927146406417618943L;

    public static final String LINK_HTML_START = "<a href=\"{0}\" onclick=\"{1}\" {2} >";
    public static final String LINK_HTML_END = "</a>";
    public static final String DEFAULT_FUNCTION_NAME = "ajaxAnywhere.getAJAX";

    //attributes
    private String functionName;
    private String zones;
    private String href;
    private String styleClass;
    private String style;
    

    public LinkTag( )
    {
        super();
        functionName = DEFAULT_FUNCTION_NAME;
    }

    /**
     * @return Returns the function String.
     */
    public String getFunctionName()
    {
        return functionName;
    }

    /**
     * @param function
     *            The function to set. Type String
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     */
    public void setFunctionName( String function )
    {
        this.functionName = function;
    }

    private Object buildOnClick()
    {
        StringBuffer strbuff = new StringBuffer( this.getFunctionName() );
        strbuff.append( "('" );
        strbuff.append( buildUrl() );
        strbuff.append( "', '" );
        strbuff.append( this.getZones() );
        strbuff.append( "')" );
        return strbuff.toString();
    }
    /**
     * 
     * @return
     * Object
     */
    private Object buildOtherAttributes()
    {
        StringBuffer strbuff = new StringBuffer( );

        if ( style != null && style.trim().length() > 0 )
        {
            strbuff.append( "style=\"" );
            strbuff.append( style );
            strbuff.append( "\"" );
            strbuff.append( " " );            
        }
        
        if ( styleClass != null && styleClass.trim().length() > 0 )
        {
            strbuff.append( "class=\"" );
            strbuff.append( styleClass );
            strbuff.append( "\"" );
            strbuff.append( " " );            
        }
       
        return strbuff.toString();
    }

    /**
     * @return Returns the zones String.
     */
    public String getZones()
    {
        return zones;
    }

    /**
     * @param zones
     *            The zones to set. Type String
     * @jsp.attribute required="true"
     * rtexprvalue="true"
     * type="java.lang.String"
     */
    public void setZones( String zones )
    {
        this.zones = zones;
    }
    
    /**
     * 
     */
    public int doStartTag() throws JspException
    {
        try
        {
            Object[] arguments = {  buildUrl(), buildOnClick(), buildOtherAttributes() };
            String html = MessageFormat.format( LINK_HTML_START, arguments );
            pageContext.getOut().print( html );
        }
        catch ( Exception ex )
        {
            throw new JspException( ex );
        }
        return EVAL_BODY_BUFFERED;//EVAL_BODY_TAG;
    }
    
    
   

     /**
      * Build url using encodeURL and <code>href</code>
      * @return
      * Object
      */   
    private Object buildUrl()
    {
        HttpServletRequest request = ( HttpServletRequest ) pageContext.getRequest();
        return ( ( HttpServletResponse ) this.pageContext.getResponse() ).encodeURL( request.getContextPath() + "/" + href );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.taglibs.standard.tag.common.core.UrlSupport#doEndTag()
     * 
     * @return @throws JspException
     * 
     */
    public int doEndTag() throws JspException
 {
        
        try
        {
            BodyContent bc = getBodyContent();
            // get the bodycontent as string
            String body = bc.getString();
            // getJspWriter to output content
            JspWriter out = bc.getEnclosingWriter();
            if ( body != null )
            {
                out.print( body );
                pageContext.getOut().print( LINK_HTML_END );
            }
        }
        catch ( Exception e )
        {
            throw new JspException( e );
        }
        // return SKIP_BODY;
        return EVAL_PAGE;
    }

    


    
    /**
     * @return Returns the style String.
     */
    public String getStyle()
    {
        return style;
    }

    
    /**
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     * @param style The style to set. Type String
     */
    public void setStyle( String style )
    {
        this.style = style;
    }

    
    /**
     * @return Returns the styleClass String.
     */
    public String getStyleClass()
    {
        return styleClass;
    }

    
    /**
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     * @param styleClass The styleClass to set. Type String
     */
    public void setStyleClass( String styleClass )
    {
        this.styleClass = styleClass;
    }

    
    /**
     * @return Returns the href String.
     */
    public String getHref()
    {
        return href;
    }

    
    /**
     * @param href The href to set. Type String
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     */
    public void setHref( String href )
    {
        this.href = href;
    }

}

