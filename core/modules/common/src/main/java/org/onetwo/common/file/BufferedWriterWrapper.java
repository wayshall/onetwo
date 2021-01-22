package org.onetwo.common.file;

import java.io.BufferedWriter;
import java.io.IOException;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;

/**
 * @author weishao zeng
 * <br/>
 */

public class BufferedWriterWrapper {
	final private BufferedWriter bufferedWriter;
	private int lineCount = 0;

	public BufferedWriterWrapper(BufferedWriter writer) {
		super();
		this.bufferedWriter = writer;
	}
	
	public BufferedWriterWrapper write(String str) {
		try {
			this.bufferedWriter.write(str);
		} catch (IOException e) {
			throw new BaseException("write string error: " + e.getMessage(), e);
		}
		return this;
	}

	public BufferedWriterWrapper writeLine(String str) {
		write(str);
		newLine();
		return this;
	}

	public BufferedWriterWrapper newLine() {
		try {
			this.bufferedWriter.newLine();
			this.lineCount++;
		} catch (IOException e) {
			throw new BaseException("write newLine error: " + e.getMessage(), e);
		}
		return this;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}
	
	public void closeQuietly() {
		IOUtils.closeQuietly(bufferedWriter);
	}

	public int getLineCount() {
		return lineCount;
	}
	
}
