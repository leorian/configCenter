package org.luotian.open.configuration;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility of stream operation.
 * <p>
 * Since there is no such utility in stream operation in apache commons lib, we
 * have to invent our own wheel.
 * 
 * @author LIU Fangran
 * 
 */
public class StreamUtil {
    /**
	 * convert input stream into byte array.
	 * 
	 * @param ois
	 * @return
	 * @throws IOException
	 */
	public static byte[] inputStreamToBytes(InputStream ois) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(ois);
		byte[] tmpbuf = new byte[2000];
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4000);

		int readbytes = 0;

		while ((readbytes = bufferedInputStream.read(tmpbuf)) != -1) {
			outputStream.write(tmpbuf, 0, readbytes);
		}

		return outputStream.toByteArray();

	}
}
