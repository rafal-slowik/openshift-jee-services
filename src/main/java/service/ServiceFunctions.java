package service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

/**
 * Service contains the functions which are used by restful service.
 * 
 * @author Rafal Slowik, rafal2308@gmail.com
 *
 */
@ApplicationScoped
public class ServiceFunctions implements Serializable {

	private static final long serialVersionUID = -2518438330053513663L;

	/**
	 * Reverse the letters for each word of the sentence without changing the
	 * order of the sentence.
	 * 
	 * @param sentence
	 *            - must not be <code>null</code>
	 * @return reversed sentence <br>
	 * @throws IllegalArgumentException
	 *             when the sentence is <code>null</code>
	 */
	public StringBuilder reverseSentence(String sentence) {
		if (sentence == null) {
			throw new IllegalArgumentException("Parameter must not be null!");
		}
		StringBuilder sb = new StringBuilder();
		String[] words = sentence.split(" ");

		for (int i = 0; i < words.length; i++) {
			if (words[i].length() > 1) {
				reverseWord(words[i], sb);
			} else {
				sb.append(words[i]);
			}
			if (i < words.length - 1) {
				sb.append(" ");
			}
		}
		return sb;
	}

	/**
	 * Calculate the n-th element of Fibonacci sequence. The parameter value
	 * should be in range between <b>0 and 92</b>, otherwise the
	 * <code>long</code> type range will be exceeded and the result will be
	 * incorrect.
	 * 
	 * @param n
	 *            - what element of the Fibonacci sequence should be calculated
	 *            .
	 * @return value of n-th element of Fibonacci sequence <br>
	 * @throws IllegalArgumentException
	 *             when the parameter is greater than 92 or less than 0
	 */
	public long getFibonacciOf(long n) {
		long elementToFind = n;
		if (elementToFind > 92 || elementToFind < 0) {
			StringBuilder sb = new StringBuilder(
					"Parameter must be in range 0 (inclusive) and 92 (inclusive) but was ");
			sb.append(elementToFind);
			throw new IllegalArgumentException(sb.toString());
		}

		long a;
		long b;
		long c;
		if (elementToFind < 2)
			return elementToFind;
		for (a = 0, b = 1; elementToFind > 1; elementToFind--) {
			c = a + b;
			a = b;
			b = c;
		}
		return b;
	}

	private void reverseWord(String s, StringBuilder sb) {
		for (int j = s.length() - 1; j >= 0; j--) {
			char c = s.charAt(j);
			sb.append(c);
		}
	}
}
