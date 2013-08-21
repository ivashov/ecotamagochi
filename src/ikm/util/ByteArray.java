package ikm.util;

import java.io.UnsupportedEncodingException;

public class ByteArray {
	private byte[] arr;
	private int idx;
	
	public ByteArray(byte[] arr) {
		setArray(arr);
	
	}
	
	public final void setArray(byte[] arr) {
		this.arr = arr;
		idx = 0;
	}
	
	public void writeInt(int v) {
		arr[idx++] = (byte) (v >>> 24);
		arr[idx++] = (byte) (v >>> 16);
		arr[idx++] = (byte) (v >>> 8);
		arr[idx++] = (byte) v;
	}
	
	public int readInt() {
		int d = ((0xff & arr[idx]) << 24) 
				| ((0xff & arr[idx + 1]) << 16)
				| ((0xff & arr[idx + 2]) << 8) 
				| ((0xff & arr[idx + 3]));
		idx += 4;
		return d;
	}
	
	public void writeLong(long v) {
		arr[idx++] = (byte) (v >>> 56);
		arr[idx++] = (byte) (v >>> 48);
		arr[idx++] = (byte) (v >>> 40);
		arr[idx++] = (byte) (v >>> 32);
		arr[idx++] = (byte) (v >>> 24);
		arr[idx++] = (byte) (v >>> 16);
		arr[idx++] = (byte) (v >>> 8);
		arr[idx++] = (byte) v;
	}
	
	public long readLong() {
		long d = ((0xffl & arr[idx]) << 56) 
				| ((0xffl & arr[idx + 1]) << 48)
				| ((0xffl & arr[idx + 2]) << 40) 
				| ((0xffl & arr[idx + 3]) << 32)
				| ((0xffl & arr[idx + 4]) << 24) 
				| ((0xffl & arr[idx + 5]) << 16)
				| ((0xffl & arr[idx + 6]) << 8) 
				| ((0xffl & arr[idx + 7]));
		idx += 8;
		return d;
	}
	
	public void writeString(String str) {
		byte[] barr = null;
		try {
			barr = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			barr = str.getBytes();
		}
		writeInt(barr.length);
		for (int i = 0; i < barr.length; i++) {
			arr[i + idx] = barr[i];
		}
		idx += barr.length;
	}
	
	public String readString() {
		int len = readInt();
		String ret = null;
		try {
			ret = new String(arr, idx, len, "utf-8");
		} catch (UnsupportedEncodingException e) {
			ret = new String(arr, idx, len);
		}
		idx += len;
		return ret;
	}
		
	public void resetIdx() {
		idx = 0;
	}
	
	public int length() {
		return idx;
	}
}
