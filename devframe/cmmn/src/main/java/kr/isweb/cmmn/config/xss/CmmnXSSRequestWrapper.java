package kr.isweb.cmmn.config.xss;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class CmmnXSSRequestWrapper extends HttpServletRequestWrapper {
	private byte[] rawData;
    private HttpServletRequest request;
    private ResettableServletInputStream servletStream;

	public CmmnXSSRequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
		this.servletStream = new ResettableServletInputStream();
	}

	public void resetInputStrea(byte[] newRawData) {
		 rawData = newRawData;
		 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(newRawData);
		 servletStream.stream = byteArrayInputStream;
	}

	@Override
    public ServletInputStream getInputStream() throws IOException {
		ByteArrayInputStream byteArrayInputStream = null;
        if (rawData == null) {
			rawData = IOUtils.toByteArray(request.getReader(), StandardCharsets.UTF_8);
			byteArrayInputStream = new ByteArrayInputStream(rawData);
			servletStream.stream = byteArrayInputStream;
        }
        return servletStream;
    }

	@Override
    public BufferedReader getReader() throws IOException {
		InputStreamReader inputStreamReader = null;
		ByteArrayInputStream byteArrayInputStream = null;
		BufferedReader bufferReader = null;
        if (rawData == null) {
            rawData = IOUtils.toByteArray(request.getReader(), StandardCharsets.UTF_8);
            byteArrayInputStream = new ByteArrayInputStream(rawData);
            servletStream.stream = byteArrayInputStream;
            inputStreamReader = new InputStreamReader(servletStream);
            bufferReader = new BufferedReader(inputStreamReader);
        }
        return bufferReader;
    }

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		return CmmnXSSUtils.stripXSS(value);
	}

	@Override
	public String[] getParameterValues(String name) {
		String [] values = super.getParameterValues(name);
		if(values != null && values.length > 0) {
			int count = values.length;
			String [] encodedValues = new String [count];
			for(int i = 0; i < count; i++) {
				encodedValues[i] = CmmnXSSUtils.stripXSS(values[i]);
			}
			return encodedValues;
		}
		return null;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		return CmmnXSSUtils.stripXSS(value);
	}

	private class ResettableServletInputStream extends ServletInputStream {

		private InputStream stream;

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
		}

		@Override
		public int read() throws IOException {
			return stream.read();
		}
	}
}
