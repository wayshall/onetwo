package org.onetwo.common.utils;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Optional;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;

public class NetUtils {
	public static final String LOCAL_IP = "127.0.0.1";

	public static String getHostAddress(){
		return getInetAddress().map(i->i.getHostAddress()).orElse(LOCAL_IP);
	}
	public static String getHostName(){
		return getInetAddress().map(i->i.getHostName()).orElse(LOCAL_IP);
	}
	public static InetAddress getInetAddress(String address, InetAddress def){
		try {
			return InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			JFishLoggerFactory.getCommonLogger().error(NetUtils.class.getSimpleName()+".getInetAddress UnknownHost: "+address);
			return def;
		}
	}

	public static Optional<InetAddress> getInetAddress(){
		try {
			return Optional.ofNullable(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			return Optional.empty();
		}
	}

	public static String getLocalHostLANIp(){
		try {
			return getLocalHostLANAddress().getHostAddress();
		} catch (Exception e) {
			JFishLoggerFactory.getCommonLogger().error(NetUtils.class.getSimpleName()+".getInetAddress UnknownHost: ", e);
			return LOCAL_IP;
		}
	}
	
	public static String getHost(String url){
		try {
			return new URL(url).getHost();
		} catch (MalformedURLException e) {
			throw new BaseException("error url: " + url);
		}
	}
	
	public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
	
	private NetUtils(){
	}

}
