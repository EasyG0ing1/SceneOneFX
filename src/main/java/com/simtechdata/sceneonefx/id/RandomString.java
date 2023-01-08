package com.simtechdata.sceneonefx.id;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomString {

	public static String get(int length) {
		SecureRandom random = new SecureRandom();
		byte[] seed = random.generateSeed(100);
		random.setSeed(seed);
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return Base64.getEncoder().encodeToString(randomBytes);
	}
}
