package org.onetwo.common;

import java.util.LinkedList;

public class VariableByte {
	static class Byte {
		int[] abyte;

		Byte() {
			abyte = new int[8];
		}

		public void readInt(int n) {
			// n must be less than 128 !!
			String bin = Integer.toBinaryString(n);
			for (int i = 0; i < (8 - bin.length()); i++) {
				abyte[i] = 0;
			}
			for (int i = 0; i < bin.length(); i++) {
				abyte[i + (8 - bin.length())] = bin.charAt(i) - 48; // ASCII
																	// code for
																	// '0' is 48
			}
			// System.out.println(" Byte ***** " + this.toString());
		}

		public void switchFirst() {
			abyte[0] = 1;
		}

		public int toInt() {
			// System.out.println(" Byte ***** " + this.toString());
			int res = 0;
			for (int i = 0; i < 8; i++) {
				res += abyte[i] * Math.pow(2, (7 - i));
			}
			// System.out.println(" Value ***** " + res);
			return res;
		}

		public String toString() {
			String res = "";
			for (int i = 0; i < 8; i++) {
				res += abyte[i];
			}
			return res;
		}
	}

	public static LinkedList<Byte> vbEncode(LinkedList<Integer> numbers) {
		LinkedList<Byte> code = new LinkedList<Byte>();
		while (numbers.size() > 0) {
			int n = numbers.poll();
			code.addAll(vbEncodeNumber(n));
		}
		return code;
	}

	public static LinkedList<Byte> vbEncodeNumber(int n) {
		LinkedList<Byte> bytestream = new LinkedList<Byte>();
		int num = n;
		while (true) {
			Byte b = new Byte();
			b.readInt(num % 128);
			bytestream.addFirst(b);
			if (num < 128) {
				break;
			}
			num /= 128; // right-shift of length 7 (128 = 2^7)
		}
		Byte last = bytestream.get(bytestream.size() - 1); // retrieving the
															// last byte
		last.switchFirst(); // setting the continuation bit to 1
		return bytestream;
	}

	public static LinkedList<Integer> vbDecode(LinkedList<Byte> code) {
		LinkedList<Integer> numbers = new LinkedList<Integer>();
		int n = 0;
		for (int i = 0; !(code.isEmpty()); i++) {
			Byte b = code.poll(); // read leading byte
			// System.out.println(" Reading byte " + b.toString() );
			int bi = b.toInt(); // decimal value of this byte
			if (bi < 128) { // continuation bit is set to 0
				n = 128 * n + bi;
			} else { // continuation bit is set to 1
				n = 128 * n + (bi - 128);
				numbers.add(n); // number is stored
				n = 0; // reset
			}
		}
		return numbers;
	}

	public static void main(String[] args) {
		LinkedList<Integer> test = new LinkedList<Integer>();
		test.add(5);
		test.add(824);
		test.add(1234);
		System.out.println("Input values: 5 - 824 - 1234");
		LinkedList<Byte> code = vbEncode(test);
		System.out.println("Variable-byte code:");
		for (int i = 0; i < code.size(); i++) {
			System.out.print(code.get(i).toString() + " ");
		}
		System.out.println();
		LinkedList<Integer> decode = vbDecode(code);
		System.out.println("After decoding:");
		for (int i = 0; i < decode.size(); i++) {
			System.out.print(decode.get(i) + " ");
		}
		System.out.println();
	}
}