package com.bluersw.source;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.X509TrustManager;

/**
 * 信任所有证书，只需实现X509TrustManager并自动生成所有方法即可完成。  To trust all certificates, you only need to implement X509TrustManager and automatically generate all methods to complete.
 * @author sunweisheng
 */
public class HttpsTrustManager implements X509TrustManager {

	private static final Logger LOGGER = Logger.getLogger(HttpsTrustManager.class.getName());

	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
		LOGGER.log(Level.SEVERE, String.format("checkClientTrusted,s: %s", s));
	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
		LOGGER.log(Level.SEVERE, String.format("checkServerTrusted,s: %s", s));
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}
