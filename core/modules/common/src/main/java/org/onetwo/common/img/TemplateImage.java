package org.onetwo.common.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.CUtils;

import com.google.common.collect.Maps;


public class TemplateImage {
	
	public static TemplateImage create(String templatePath){
		TemplateImage templateImage = new TemplateImage(templatePath);
		return templateImage;
	}
	
	final private String imagePath;
	final private HashSetValuedHashMap<String, DefinedData<?>> defineTables = new HashSetValuedHashMap<>();
	final private Map<Class<? extends DefinedData<?>>, DefinedDrawHandler> definedDrawHandlers = Maps.newHashMap();
	final private String format;
	private boolean ignoreDrawingIfNoData = true;

	public TemplateImage(String imagePath) {
		super();
		this.imagePath = imagePath;
		this.format = FileUtils.getExtendName(imagePath);
		this.registerDefaultDrawHandlers();
	}
	
	final private void registerDefaultDrawHandlers(){
		registerDrawHandler(ImageDefinedData.class, new ImageDrawing());
		registerDrawHandler(TextDefinedData.class, new TextDrawing());
	}

	public TemplateImage ignoreDrawingIfNoData(boolean ignoreDrawingIfNoData) {
		this.ignoreDrawingIfNoData = ignoreDrawingIfNoData;
		return this;
	}

	public File drawTo(String savePath, Object...params){
		byte[] bytes = draw(params);
		File file = new File(savePath);
		FileUtils.writeByteArrayToFile(file, bytes);
		/*FileOutputStream output;
		try {
			output = new FileOutputStream(new File(savePath));
			IOUtils.write(bytes, output);
		} catch (Exception e) {
			throw new RuntimeException("write image error: " + e.getMessage());
		}*/
		return file;
	}
	
	public byte[] draw(Object...params){
		Map<String, Object> paramMap = CUtils.asMap(params);
		BufferedImage imageBuf = ImageUtils.readBufferedImageFromPath(imagePath);
		Graphics graphic = imageBuf.getGraphics();
		try {
			executeDrawing(imageBuf, graphic, paramMap);
		} catch (NoSuchElementException e) {
			throw e;
		}catch (Exception e) {
			throw new RuntimeException("drawing error: " + e.getMessage());
		} finally{
			graphic.dispose();
		}
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try {
			ImageIO.write(imageBuf, format, bo);
		} catch (IOException e) {
			throw new RuntimeException("write image error: " + e.getMessage());
		}
		return bo.toByteArray();
	}
	
	@SuppressWarnings("unchecked")
	protected void executeDrawing(BufferedImage imageBuf, Graphics graphic, Map<String, Object> paramMap){
		this.defineTables.keySet().forEach(key->{
			Collection<? extends DefinedData<?>> datas = this.defineTables.get(key);
			datas.stream().forEach(data->{
				Object val = paramMap.get(key);
				if(val==null && ignoreDrawingIfNoData){
					return ;
				}
				if(val instanceof Supplier){
					val = ((Supplier<?>)val).get();
				}
				GraphicsContext g = new GraphicsContext(imageBuf, graphic, val);
				getDefinedDrawing((Class<? extends DefinedData<?>>)data.getClass()).draw(g, data);
			});
		});
	}
	
	public DefinedDrawHandler getDefinedDrawing(Class<? extends DefinedData<?>> dataClass){
		DefinedDrawHandler drawing = this.definedDrawHandlers.get(dataClass);
		if(drawing==null){
			throw new NoSuchElementException("no drawing found for: " + dataClass);
		}
		return drawing;
	}
	
	final public TemplateImage registerDrawHandler(Class<? extends DefinedData<?>> dataClass, DefinedDrawHandler drawing){
		this.definedDrawHandlers.put(dataClass, drawing);
		return this;
	}
	
	public TemplateImage addImageDefined(String name, int x, int y, int width, int height){
		ImageDefinedData img = new ImageDefinedData(x, y, width, height);
		return addDefined(name, img);
	}
	public TemplateImage addTextDefined(String name, int x, int y, Color color, Font font){
		TextDefinedData text = new TextDefinedData(x, y, color, font);
		return addDefined(name, text);
	}
	public TemplateImage addTextDefined(String name, Horizontal horizontal, int y, Color color, Font font){
		TextDefinedData text = new TextDefinedData(horizontal, y, color, font);
		return addDefined(name, text);
	}
	
	public TemplateImage addDefined(String name, DefinedData<?> defined){
		defineTables.put(name, defined);
		return this;
	}
	
	public class GraphicsContext {
		final private BufferedImage bufferedImage;
		final private Graphics graphics;
		final private Object data;
		private int horizontalSpaceSize = 5;

		public GraphicsContext(BufferedImage bufferedImage, Graphics graphics, Object data) {
			super();
			this.bufferedImage = bufferedImage;
			this.graphics = graphics;
			this.data = data;
		}
		
		public int getHorizontalSpaceSize() {
			return horizontalSpaceSize;
		}

		public int getImageWidth(){
			return bufferedImage.getWidth();
		}
		
		public int getImageHeight(){
			return bufferedImage.getHeight();
		}

		public int getFontHeight(String text, Font font){
			float height = font.getLineMetrics(text, getFontRenderContext()).getHeight();
			return Float.valueOf(height).intValue();
		}

		public int getFontWidth(String text, Font font){
			int width = graphics.getFontMetrics(font).stringWidth(text);
			return width;
		}

		public FontRenderContext getFontRenderContext(){
			Graphics2D g2 = (Graphics2D) this.graphics;
			FontRenderContext frc = g2.getFontRenderContext();
			return frc;
		}

		public Object getData() {
			return data;
		}
		public Graphics getGraphics() {
			return graphics;
		}
		
	}
	
	public static interface DefinedDrawHandler {
		void draw(GraphicsContext g, DefinedData<?> data);
	}

	@SuppressWarnings("unchecked")
	public static abstract class TypeDefinedDrawing<T> implements DefinedDrawHandler {
		public void draw(GraphicsContext graphic, DefinedData<?> data){
			this.doDraw(graphic, (T)data);
		}
		abstract protected void doDraw(GraphicsContext graphic, T data);
	}
	
	public static class ImageDrawing  extends TypeDefinedDrawing<ImageDefinedData> {
		@Override
		public void doDraw(GraphicsContext graphic, ImageDefinedData data) {
			Image img = null;
			if(data.getWidth()!=null && data.getHeight()!=null){
				BufferedImage imgBuf = ImageUtils.readBufferedImageFromPath(graphic.getData().toString());
				img = imgBuf.getScaledInstance(data.getWidth(), data.getHeight(), Image.SCALE_SMOOTH);
			}else{
				img = ImageUtils.createImageIconFromPath(graphic.getData().toString()).getImage();
			}
			int x = data.getX();
			int y = data.getY();
			if(data.getHorizontal()!=null){
				x = data.getHorizontal().getX(graphic, data.getWidth(graphic, img));
			}
			graphic.graphics.drawImage(img, x, y, null);
		}
	}
	
	public static class TextDrawing  extends TypeDefinedDrawing<TextDefinedData> {
		@Override
		public void doDraw(GraphicsContext graphic, TextDefinedData data) {
			if(data.getColor()!=null){
				graphic.graphics.setColor(data.getColor());
			}
			if(data.getFont()!=null){
				graphic.graphics.setFont(data.getFont());
			}
			String text = graphic.getData().toString();
			String[] strs = StringUtils.split(text, "\n");
			int x = data.getX();
			int y = data.getY();
			if(data.getHorizontal()!=null){
				x = data.getHorizontal().getX(graphic, data.getWidth(graphic, text));
			}
			for (int i = 0; i < strs.length; i++) {
				graphic.graphics.drawString(strs[i], x, y);
				y = y + Float.valueOf(graphic.getFontHeight(strs[i], data.getFont())).intValue();
			}
		}
	}
	
	public static class ImageDefinedData extends DefinedData<Image> {
		final private Integer width;
		final private Integer height;


		public ImageDefinedData(Horizontal horizontal, int y, Integer width, Integer height) {
			super(horizontal, 0, y);
			this.width = width;
			this.height = height;
		}


		public ImageDefinedData(int x, int y, Integer width, Integer height) {
			super(null, x, y);
			this.width = width;
			this.height = height;
		}

		public Integer getWidth() {
			return width;
		}

		public Integer getHeight() {
			return height;
		}

		@Override
		public int getWidth(GraphicsContext graphic, Image data) {
			return data.getWidth(null);
		}

	}
	
	public static class TextDefinedData extends DefinedData<String> {
		final private Color color;
		final private Font font;
		
		
		public TextDefinedData(Horizontal horizontal, Integer y, Color color, Font font) {
			super(horizontal, 0, y);
			this.color = color;
			this.font = font;
			this.horizontal = horizontal;
		}

		public TextDefinedData(Integer x, Integer y, Color color, Font font) {
			super(null, x, y);
			this.color = color;
			this.font = font;
			this.horizontal = null;
		}

		public Color getColor() {
			return color;
		}

		public Font getFont() {
			return font;
		}

		public Horizontal getHorizontal() {
			return horizontal;
		}

		@Override
		public int getWidth(GraphicsContext graphic, String data) {
			int fontWidth = graphic.getFontWidth(graphic.getData().toString(), getFont());
			return fontWidth;
		}
		
	}
	
	abstract public static class DefinedData<T> {
		final private Integer x;
		final private Integer y;
		protected Horizontal horizontal;
		
		public DefinedData(Horizontal horizontal, int x, int y) {
			super();
			this.x = x;
			this.y = y;
			this.horizontal = horizontal;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public int getWidth(GraphicsContext graphic, T data){
			throw new UnsupportedOperationException("subclass has not implement!");
		}
		public Horizontal getHorizontal() {
			return horizontal;
		}
	}
	
	public static enum Horizontal {
		CENTER{
			int getX(GraphicsContext graphic, int drawObjectWidth){
				int width = graphic.getImageWidth();
				return (width-drawObjectWidth)/2;
			}
		},
		LEFT{
			int getX(GraphicsContext graphic, int drawObjectWidth){
				return graphic.getHorizontalSpaceSize();
			}
		},
		RIGHT{
			int getX(GraphicsContext graphic, int drawObjectWidth){
				return graphic.getImageWidth()-drawObjectWidth-graphic.getHorizontalSpaceSize();
			}
		};
		
		abstract int getX(GraphicsContext graphic, int drawObjectWidth);
	}

}
