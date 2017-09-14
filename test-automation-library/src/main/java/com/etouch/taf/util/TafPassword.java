/*
 * 
 */
package com.etouch.taf.util;

import org.apache.commons.logging.Log;

import com.microsoft.tfs.util.base64.Base64;


/**
 * The Class encrypt_decrypt.
 */
public class TafPassword{

	/** The log. */
	private static Log log = LogUtil.getLog(TafPassword.class);

	
	public String encrypted=null;
	public String decrypted=null;
	
	public byte[] passwordByte=null;
	

	public byte[] getPasswordByte() {
		return passwordByte;
	}


	public void setPasswordByte(byte[] passwordByte) {
		this.passwordByte = passwordByte;
	}

public String getEncrypted() {
		return encrypted;
	}


	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}


public String getDecrypted() {
		return decrypted;
	}


	public void setDecrypted(String value) {
		this.decrypted = value;
	}

	/*
	 * Gets the encoded value.
	 *
	 * @param value the value
	 * @return the encoded value
	 */
	public void encryptPassword(String value){
		String orig = value;
		 byte[] encoded = Base64.encodeBase64(orig.getBytes());
		 this.setEncrypted(new String(encoded));
	}

	/**
	 * Gets the decoded value.
	 *
	 * @param value1 the value1
	 * @return the decoded value
	 */
	public void decryptPassword(String password){
		byte[] decoded = Base64.decodeBase64(password.getBytes());
		this.setDecrypted(new String(decoded));
    }
	
	
}