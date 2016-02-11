package com.js.vmm.util;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class MyHttpRequest {
	private static String urlPath = "http://www.tngou.net/api/drug/code";
	
	public static String sendGet(String code) {
		/* ����HTTP Get���� */
		String strResult = null;
		HttpGet httpRequest = new HttpGet(urlPath+"?code="+code);
		try {
			/* �������󲢵ȴ���Ӧ */
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			/* ��״̬��Ϊ200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* �� */
				strResult = EntityUtils.toString(httpResponse
						.getEntity());
				/* ȥû���õ��ַ� */
			} else {
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (IOException e) {
			strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (Exception e) {
			strResult = e.getMessage().toString();
			e.printStackTrace();
		}
		return strResult;
	}
}
