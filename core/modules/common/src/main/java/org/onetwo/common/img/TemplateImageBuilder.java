package org.onetwo.common.img;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.onetwo.common.img.TemplateImage.DefinedData;
import org.onetwo.common.img.TemplateImage.DefinedDrawHandler;
import org.onetwo.common.img.TemplateImage.Horizontal;
import org.onetwo.common.img.TemplateImage.ImageDefinedData;
import org.onetwo.common.img.TemplateImage.ImageDrawing;
import org.onetwo.common.img.TemplateImage.TextDefinedData;
import org.onetwo.common.img.TemplateImage.TextDrawing;

import com.google.common.collect.Maps;

public class TemplateImageBuilder {
	public static TemplateImageBuilder newBuilder(String templatePath){
		TemplateImageBuilder templateImage = new TemplateImageBuilder(templatePath);
		return templateImage;
	}

	private String imagePath;
	private HashSetValuedHashMap<String, DefinedData<?>> defineTables = new HashSetValuedHashMap<>();
	private Map<Class<? extends DefinedData<?>>, DefinedDrawHandler> definedDrawHandlers = Maps.newHashMap();
	private boolean ignoreDrawingIfNoData = true;
	

	public TemplateImageBuilder(String templatePath) {
		this.imagePath = templatePath;
		registerDrawHandler(ImageDefinedData.class, new ImageDrawing());
		registerDrawHandler(TextDefinedData.class, new TextDrawing());
	}
	
	public TemplateImage build(){
		return new TemplateImage(this);
	}

	public TemplateImageBuilder ignoreDrawingIfNoData(boolean ignoreDrawingIfNoData) {
		this.ignoreDrawingIfNoData = ignoreDrawingIfNoData;
		return this;
	}
	
	final public TemplateImageBuilder registerDrawHandler(Class<? extends DefinedData<?>> dataClass, DefinedDrawHandler drawing){
		this.definedDrawHandlers.put(dataClass, drawing);
		return this;
	}
	
	public TemplateImageBuilder addImageDefined(String name, int x, int y, int width, int height){
		ImageDefinedData img = new ImageDefinedData(x, y, width, height);
		return addDefined(name, img);
	}
	public TemplateImageBuilder addTextDefined(String name, int x, int y, Color color, Font font){
		TextDefinedData text = new TextDefinedData(x, y, color, font);
		return addDefined(name, text);
	}
	public TemplateImageBuilder addTextDefined(String name, Horizontal horizontal, int y, Color color, Font font){
		TextDefinedData text = new TextDefinedData(horizontal, y, color, font);
		return addDefined(name, text);
	}
	
	public TemplateImageBuilder addDefined(String name, DefinedData<?> defined){
		defineTables.put(name, defined);
		return this;
	}

	public String getImagePath() {
		return imagePath;
	}

	public HashSetValuedHashMap<String, DefinedData<?>> getDefineTables() {
		return defineTables;
	}

	public Map<Class<? extends DefinedData<?>>, DefinedDrawHandler> getDefinedDrawHandlers() {
		return definedDrawHandlers;
	}
	public boolean isIgnoreDrawingIfNoData() {
		return ignoreDrawingIfNoData;
	}
	
	
}
