package com.etouch.taf.infra.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;


/**
 * The Class MailSubjectSearch.
 */
public class MailSubjectSearch extends SearchTerm implements IMailSearchCriteria {

	/** The log. */
	private static Log log = LogUtil.getLog(EmailValidator.class);

	/** The prop. */
	private Properties prop;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.mail.search.SearchTerm#match(javax.mail.Message)
	 */
	@Override
	public boolean match(Message message) {
		try {

			String subjectLike = prop.getProperty(IEMailConstantUtil.MATCH_MAIL_SUBJECT);

			if (message.getSubject() != null && message.getSubject().contains(subjectLike)) {
				return true;
			}
		} catch (MessagingException ex) {
			log.debug("MessagingException", ex);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.infra.mail.IMailSearchCriteria#isMatch(javax.mail.Message,
	 * java.util.Properties)
	 */
	public boolean isMatch(Message message, Properties prop) {
		
		this.prop = prop;
		return match(message);
	}

}
