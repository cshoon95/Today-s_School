//계정 정보 gmail
package util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Gmail extends Authenticator { //
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication("cshoon80@gmail.com", "tngnsl421!");
	}
}
