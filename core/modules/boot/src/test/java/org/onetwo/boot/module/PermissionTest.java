package org.onetwo.boot.module;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

/**
 * @author wayshall
 * <br/>
 */
public class PermissionTest {
	
	@Test
	public void test(){
		int p1 = 1;
		int p2 = 2;
		int p3 = 4;
		int p4 = 8;
		int p = 5;//4+1
		System.out.println(LangUtils.padLeft(Integer.toBinaryString(p1), 4, "0"));
		System.out.println(LangUtils.padLeft(Integer.toBinaryString(p2), 4, "0"));
		System.out.println(LangUtils.padLeft(Integer.toBinaryString(p3), 4, "0"));
		System.out.println(LangUtils.padLeft(Integer.toBinaryString(p4), 4, "0"));
		System.out.println(LangUtils.padLeft(Integer.toBinaryString(p), 4, "0"));
		boolean hasPerm = (p & p1)==p1;
		System.out.println("hasPerm : " + hasPerm);
		hasPerm = (p & p2)==p2;
		System.out.println("hasPerm : " + hasPerm);
	}
	
	@Test
	public void testPermission(){
		//p最大31位，因为int为4个字节,4*8=32,1位是符号位:32-1
		int p1 = 0;//1;
		int p2 = 1;//2;
		int p3 = 2;//4;
		int p4 = 3;//8;
		int p5 = 4;//16;
		int p6 = 5;//32;
		int p7 = 6;//64;
		int p8 = 7;//128;
		//4+2+1=7;
		BitPermissions p = new BitPermissions(7);
		assertThat(p.hasPermission(p1)).isTrue();
		assertThat(p.hasPermission(p2)).isTrue();
		assertThat(p.hasPermission(p3)).isTrue();
		assertThat(p.hasPermission(p4)).isFalse();
		
		//128+16=144;
		p = new BitPermissions(144);
		assertThat(p.hasPermission(p1)).isFalse();
		assertThat(p.hasPermission(p2)).isFalse();
		assertThat(p.hasPermission(p3)).isFalse();
		assertThat(p.hasPermission(p4)).isFalse();
		assertThat(p.hasPermission(p5)).isTrue();
		assertThat(p.hasPermission(p6)).isFalse();
		assertThat(p.hasPermission(p7)).isFalse();
		assertThat(p.hasPermission(p8)).isTrue();
		
		p.add(p1, p2);
		assertThat(p.hasPermission(p1)).isTrue();
		assertThat(p.hasPermission(p2)).isTrue();
		assertThat(p.hasPermission(p3)).isFalse();

		p.del(p2);
		p.add(p3);
		assertThat(p.hasPermission(p1)).isTrue();
		assertThat(p.hasPermission(p2)).isFalse();
		assertThat(p.hasPermission(p3)).isTrue();
		

		p.add(30);
		assertThat(p.hasPermission(p1)).isTrue();
		assertThat(p.hasPermission(p2)).isFalse();
		assertThat(p.hasPermission(p3)).isTrue();
		assertThat(p.hasPermission(p4)).isFalse();
		assertThat(p.hasPermission(p5)).isTrue();
		assertThat(p.hasPermission(p6)).isFalse();
		assertThat(p.hasPermission(p7)).isFalse();
		assertThat(p.hasPermission(p8)).isTrue();
	}

	public static class BitPermissions {
		//
		int currentPermission;
		
		public BitPermissions(int currentPermission) {
			super();
			this.currentPermission = currentPermission;
		}
		public boolean hasPermission(int... permissions){
			int p = 0;
			for (int i = 0; i < permissions.length; i++) {
				p = p + pow(permissions[i]);
			}
			return (currentPermission & p) == p;
		}
		public void add(int... permissions){
			int p = this.currentPermission;
			for (int i = 0; i < permissions.length; i++) {
				p = p | pow(permissions[i]);
			}
			this.currentPermission = p;
		}
		public void del(int... permissions){
			int p = this.currentPermission;
			for (int i = 0; i < permissions.length; i++) {
				p = p & (~pow(permissions[i]));
			}
			this.currentPermission = p;
		}
		
		private int pow(int p){
			if(p>=31){
				throw new RuntimeException("error permission : " + p);
			}
			//int 的最大值是2**31-1
			return (int)Math.pow(2, p);
		}

		public int getCurrentPermission() {
			return currentPermission;
		}

		public void setCurrentPermission(int currentPermission) {
			this.currentPermission = currentPermission;
		}
		
	}
}
