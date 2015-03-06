package org.ajaxanywhere.jsf;

import org.ajaxanywhere.AAUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


public class ZoneUIComponent extends UIComponentBase {
    private static final String COMPONENT_FAMILY = "AjaxAnywhereFamily";

    public ZoneUIComponent() {
        setTransient(false);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public void encodeBegin(FacesContext context)
            throws IOException {
        if (context == null) throw new IllegalArgumentException("context is null");
        if (!isRendered()) return;
        if (getId() == null)
            throw new IllegalArgumentException("name attribute is null");

        if (canSkipRendering(this, context))
            return;

        ResponseWriter writer = context.getResponseWriter();
        writer.write(AAUtils.getZoneStartDelimiter(getCompositeId()));

    }

    private String getCompositeId() {
        ValueBinding vb = getValueBinding("idSuffix");
        String compId =  vb == null ? getId() : getId()+vb.getValue(getFacesContext());
    	return compId;
	}

	public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext context)
            throws IOException {	
        if (context == null) throw new IllegalArgumentException("context is null");
        if (canSkipRendering(this, context))
            return;

         renderChildren(context, this);
    }

    public void encodeEnd(FacesContext context)
            throws IOException {
        if (context == null) throw new IllegalArgumentException("context is null");
        if (!isRendered()) return;
        if (canSkipRendering(this, context))
            return;

        ResponseWriter writer = context.getResponseWriter();
        writer.write(AAUtils.getZoneEndDelimiter(getCompositeId()));
    }


    private static void renderChildren(FacesContext facesContext, UIComponent component)
            throws IOException {
        if (component.getChildCount() > 0) {
            for (Iterator it = component.getChildren().iterator(); it.hasNext();) {
                UIComponent child = (UIComponent) it.next();
                renderComponent(facesContext, child);
            }
        }
    }


    private static void renderComponent(FacesContext facesContext, UIComponent child)
            throws IOException {

        if (!child.isRendered()) {
            return;
        }

        child.encodeBegin(facesContext);
        if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
        } else {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }

    private static boolean canSkipRendering(UIComponent component, FacesContext context) {
        return noIncludedSubZones(component, context) && notInsideIncludedZone(component, context);
    }
    /**
     * checks if this zones is surrently inside another included zone
     */
    private static boolean notInsideIncludedZone(UIComponent component, FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestMap();
        UIComponent parent = component.getParent();
        while (parent != null && ! (parent instanceof UIViewRoot)) {
            if (isRefreshZone(parent, requestMap))
                return false;
            parent = parent.getParent();
        }
        return true;
    }

    /**
     * checks if there are any sun-zones to include
     */
    private static boolean noIncludedSubZones(UIComponent component, FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestMap();
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        if (! AAUtils.isAjaxRequest(requestParameterMap) )
            return false;
        if (isRefreshZone(component, requestMap))
            return false;
        else {
            for (Iterator iterator = component.getChildren().iterator(); iterator.hasNext();) {
                UIComponent child = (UIComponent) iterator.next();
                if (!canSkipRendering(child, context))
                    return false;
            }
            return true;
        }
    }

	private static boolean isRefreshZone(UIComponent component, Map requestMap) 
	{
		if (component instanceof ZoneUIComponent==false)
			return false;
		
		ZoneUIComponent c = (ZoneUIComponent)component;
		for (Iterator it = AAUtils.getZonesToRefresh(requestMap).iterator();it.hasNext();)
		{
			String zoneName = (String)it.next();
			if (c.getValueBinding("idSuffix")!=null)
			{
				if (zoneName.startsWith(c.getId()))
					return true;				
			}
			else
			{
				if (zoneName.equals(c.getId()))
					return true;				
			}
		}
		return false;
	}
}
