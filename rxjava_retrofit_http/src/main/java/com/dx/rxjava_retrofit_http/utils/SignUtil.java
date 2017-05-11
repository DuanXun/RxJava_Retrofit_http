package com.dx.rxjava_retrofit_http.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignUtil {
	private static final char last2byte = (char) Integer.parseInt("00000011", 2);
	private static final char last4byte = (char) Integer.parseInt("00001111", 2);
	private static final char last6byte = (char) Integer.parseInt("00111111", 2);
	private static final char lead6byte = (char) Integer.parseInt("11111100", 2);
	private static final char lead4byte = (char) Integer.parseInt("11110000", 2);
	private static final char lead2byte = (char) Integer.parseInt("11000000", 2);
	private static final char[] encodeTable = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	public static boolean verify(String data, String key, String sign, int signType){
		return sign.equals(createSign(data, key, signType));
	}
	
	public static String createSign(String data, String key, int signType) {
		if(signType==1){
			return appSign(data, key);//就是计算手持端游戏需要的签名
		}else{
			return websign(data, key);//当游戏类型是web游戏的时候，签名是这样算的
		}
	}

	/**
	 * 获取签名字段
	 * @param data
	 * @param key
	 * @return
	 */
	public static String appSign(String data, String key){
		byte[] byteHMAC = null;
		String urlEncoder = "";
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
			if (byteHMAC != null) {
				String oauth = appencode(byteHMAC);
				if (oauth != null) {
					urlEncoder = URLEncoder.encode(oauth, "utf8");
				}
			}
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return urlEncoder;
	}

	private static String appencode(byte[] from) {
		StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
		int num = 0;
		char currentByte = 0;
		for (int i = 0; i < from.length; i++) {
			num = num % 8;
			while (num < 8) {
				switch (num) {
				case 0:
					currentByte = (char) (from[i] & lead6byte);
					currentByte = (char) (currentByte >>> 2);
					break;
				case 2:
					currentByte = (char) (from[i] & last6byte);
					break;
				case 4:
					currentByte = (char) (from[i] & last4byte);
					currentByte = (char) (currentByte << 2);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead2byte) >>> 6;
					}
					break;
				case 6:
					currentByte = (char) (from[i] & last2byte);
					currentByte = (char) (currentByte << 4);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead4byte) >>> 4;
					}
					break;
				}
				to.append(encodeTable[currentByte]);
				num += 6;
			}
		}
		if (to.length() % 4 != 0) {
			for (int i = 4 - to.length() % 4; i > 0; i--) {
				to.append("=");
			}
		}
		return to.toString();
	}
	
	private static String websign(String data, String key) {
		String sData = "";
		try {
//			String signData = MD5Util.md5Encode(data + key);
			String signData = MD5Util.MD5(data + key);
			sData = signData.substring(2, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}

	/**
	 * post 方式获取需要签名的字段
	 * @param params
	 * @return
	 */
	public static String getNeedSignData(Map<String, Object> params){
		JSONObject obj = new JSONObject();
		Map.Entry entry;
		for(Iterator iterator = params.entrySet().iterator(); iterator.hasNext();)
		{
			entry = (Map.Entry)iterator.next();
			try {
				obj.put(entry.getKey().toString(),entry.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return obj.toString();
	}

	/**
	 * get 方式拼接需要签名的url
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getNeedSignUrl(String url,Map<String, Object> params){
		if (params == null || params.size() < 1) {
			return url;
		}
		String result = "";
		StringBuffer sb = new StringBuffer();
		sb.append(url);
		if (url != null) {
			sb.append("?");
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key != null && !key.trim().equals("") && value != null) {
					sb.append(entry.getKey());
					sb.append("=");
					sb.append(entry.getValue());
					sb.append("&");
				}
			}
			if (sb.toString().endsWith("&")) {
				result = sb.toString().substring(0,
						sb.toString().length() - 1);
			} else {
				result = sb.toString();
			}
		}

		return result;
	}

	/**
	 * 生成头文件配置参数
	 * @param heads 当heads参数为null时，生成默认头文件配置
	 * @param needSignData  需要签名的字符串体
	 * @return
	 */
	public static Map<String, String> getHeaders(Map<String,String> heads,String needSignData) {
		Log.i("yuzhentao","needSignData:"+needSignData);
		if(heads==null||heads.isEmpty()){
			heads = new HashMap<>();
		}
		heads.put("debug", "true");
		heads.put("appkey", "34fdsf34dserefdf");
		heads.put("sign", getSignData(needSignData,"49947f2a4f604129bd29c8ffd7400266"));
		heads.put("Content-Type", "application/json");

		Log.i("yuzhentao","Headers:"+heads.toString());
		return heads;
	}

	/**
	 * 对需要签名的部分签名
	 *
	 * @param data
	 * @return
	 */
	public static String getSignData(String data, String privateKey) {

		String sign = null;
		Log.i("yuzhentao","getSignData:"+data+"   privateKey:"+privateKey);
		try {
			sign = appSign(data ,privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("yuzhentao","sign result:"+sign);
		return sign;
	}
}
