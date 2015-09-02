package com.livingblast.mcwebpanel.utils;

public class MustacheTokenUtil {

	public static String replaceTokens(String... args) {

		String retval = null;
		
		if (args != null && args.length > 1) {
			retval = args[0];
			int len = args.length;
			for (int x = 1; x < len; x++) {
				int startingMustache = retval.indexOf("{");
				int endingMustache = retval.indexOf("}");
				if (startingMustache < 0 || endingMustache < 0) {
					break;
				}
				retval = retval.replaceFirst("\\{[^\\{\\}]*\\}", args[x]);
			}
		}
		return retval;
	}
}
