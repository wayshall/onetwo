package org.onetwo.boot.module.rxtx.weigh;

import java.math.BigDecimal;
import java.util.Map;

import org.onetwo.boot.module.rxtx.BaseSeriaCommand;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.BinaryUtils;
import org.onetwo.common.utils.LangUtils;

import com.google.common.collect.Maps;

/**
 * @author weishao zeng
 * <br/>
 */

public class GetAllWeighCommand extends BaseSeriaCommand<Map<String, ModuleWeighInfo>> {
	private static final String GET_WEIGH_VALUE = "00 05 02 05 0C";

	/***
	 * 返回结果的数据长度
	 */
	final private int dataLength = 9;

	@Override
	public String getCommand() {
		return GET_WEIGH_VALUE.replace(" ", "");
	}

	/****
	 * @return 返回结果：模块编号 -> 重量信息
	 */
	@Override
	protected Map<String, ModuleWeighInfo> dataToObject(byte[] datas) {
		int size = datas.length;
		if (size%this.dataLength!=0) {
			throw new BaseException("数据包不完整, size: " + size + ", data length: " + this.dataLength);
		}
		
		Map<String, ModuleWeighInfo> results = Maps.newHashMap();
		int startIndex = 0;
		while (size >= (startIndex + this.dataLength)) {
			byte[] data = new byte[this.dataLength];
			System.arraycopy(datas, startIndex, data, 0, dataLength);
			ModuleWeighInfo info = dataToWeighInfo(data);
			results.put(info.getModuleNo(), info);
			startIndex = startIndex + this.dataLength;
		}
		return results;
	}

	/***
	 * n  06  02  St  X4  X3  X2  X1  Version LCR  
X3 X2 X1是分度数，X4是分度值代号(最高位是负号)。
  重量=分度数*分度值
	 * @author weishao zeng
	 * @param data
	 * @return
	 */
	protected ModuleWeighInfo dataToWeighInfo(byte[] data) {
		ModuleWeighInfo info = new ModuleWeighInfo();
		String moduleNo = LangUtils.toHex(data[0]);
		info.setModuleNo(moduleNo);
		
		int weigh = BinaryUtils.bytesToInt(new byte[] {data[5], data[6], data[7]});
		// 最高位为1是负数
		if (BinaryUtils.getBitValue(data[4], 7)==1) {
			weigh = -weigh;
		}
		int division = BinaryUtils.getLowBits(data[4]);
		WeighDivisionValues divisionValue = WeighDivisionValues.values()[division];
		info.setDivision(divisionValue);
		BigDecimal weighValue = divisionValue.getDivision().multiply(new BigDecimal(weigh));
		info.setWeigh(weighValue);
		return info;
	}
	
	
}
