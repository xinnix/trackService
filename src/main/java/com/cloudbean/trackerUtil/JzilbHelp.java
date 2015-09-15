package com.cloudbean.trackerUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

import com.jcraft.jzlib.*;
import com.jcraft.jzlib.ZOutputStream;
import com.jcraft.jzlib.ZInputStream;

/**
 * 
 * @ClassName: JzilbHelp
 * @Description: TODO (内存数据压缩解压)
 * @author miao
 * @date 2012-11-13 上午1:20:48
 * 
 */
public class JzilbHelp {

	/**
	 * 压缩数据
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] jzlib(byte[] object) {

		byte[] data = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ZOutputStream zOut = new ZOutputStream(out,
					JZlib.Z_DEFAULT_COMPRESSION);
			DataOutputStream objOut = new DataOutputStream(zOut);
			objOut.write(object);
			objOut.flush();
			zOut.close();
			data = out.toByteArray();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 
	 * @Title: unjzlib
	 * @Description: TODO(解压)
	 * @param @param object
	 * @param @return
	 * @return byte[] 返回类型
	 * @throws
	 */
	public static byte[] unjzlib(byte[] object) {
		byte[] data = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(object);
			ZInputStream zIn = new ZInputStream(in);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = zIn.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			data = baos.toByteArray();
			baos.flush();
			baos.close();
			zIn.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;

	}
}
