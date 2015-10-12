package com.cloudbean.trackerUtil;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SessionIdentifierGenerator {
	public static String nextSessionId() {
	    return new BigInteger(130, new SecureRandom()).toString(32);
	}
}
