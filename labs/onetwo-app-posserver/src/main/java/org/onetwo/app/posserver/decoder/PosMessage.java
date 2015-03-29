package org.onetwo.app.posserver.decoder;

public class PosMessage {
	//报文识别标识, 6, char, GDSBKT
	private String identification;
	//信息类型码, 2, hex
	private String typeCode;
	//连接结束标志, 1, hex,00 收到报文后不结束, 01 收到报文后结束
	private String keepAlive;
	//报文版本号, 1, bcd, 01
	private byte version;
	//报文体加密算法, 1, hex, 0x00 3DES 加密, 0x01 3DES MAC 加密
	private String encipher;
	//终端流水号, 4, hex, 高位在后，即小端，倒序
	private String sn;
	//终端编号, 4, bcd,
	private String posCode;
	//报文长度 , 2, hex
	

	
}
