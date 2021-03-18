package org.onetwo.boot.module.rxtx;

import java.nio.ByteBuffer;
import java.util.List;

import org.onetwo.common.utils.LangUtils;

import com.google.common.collect.Lists;

/**
 * @author weishao zeng
 * <br/>
 */

abstract public class BaseSeriaCommand<T> extends SimpleSerialPortEventListener implements SeriaDeivceCommand<T> {

	private List<byte[]> datas = Lists.newArrayList();

	synchronized protected void receiveData(byte[] data) {
		logger.info("receiveData: {}", LangUtils.toHex(data));
		this.datas.add(data);
	}

	@Override
	synchronized public T decodeData() {
		byte[] bytes = toBytes(datas);
		try {
			return dataToObject(bytes);
		} finally {
			clearData();
		}
	}
	
	abstract protected T dataToObject(byte[] datas);
	
	final protected byte[] toBytes(List<byte[]> datas) {
		int size = datas.stream().mapToInt(d -> d.length).sum();
		ByteBuffer buf = ByteBuffer.allocate(size);
		for (byte[] bytes : datas) {
			buf.put(bytes);
		}
		buf.flip();
		byte[] bufData = buf.array();
		return bufData;
	}
	
	public void clearData() {
		this.datas.clear();
	}
	
}
