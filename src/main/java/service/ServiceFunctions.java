package service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServiceFunctions implements Serializable {

	private static final long serialVersionUID = -2518438330053513663L;

	public StringBuilder reverseSentence(String sentence) {
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

	public void reverseWord(String s, StringBuilder sb) {
		for (int j = s.length() - 1; j >= 0; j--) {
			char c = s.charAt(j);
			sb.append(c);
		}
	}

	public long getFibonacciOf(long n) {
		long a, b, c;
		if (n < 2)
			return n;
		for (a = 0, b = 1; n > 1; n--) {
			c = a + b;
			a = b;
			b = c;
		}
		return b;
	}
}
