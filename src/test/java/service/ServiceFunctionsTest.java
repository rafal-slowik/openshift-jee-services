package service;

import static org.junit.Assert.*;

import org.junit.Test;

public class ServiceFunctionsTest {

	private ServiceFunctions serviceFunctions = new ServiceFunctions();

	@Test
	public void reverseSentence() {
		String sentence = "some words to test";
		String result = "emos sdrow ot tset";
		StringBuilder sb = serviceFunctions.reverseSentence(sentence);
		assertEquals(addQuoteToString(result), addQuoteToString(sb.toString()));
	}

	@Test
	public void reverseSentenceNull() {
		boolean isException = false;
		try {
			serviceFunctions.reverseSentence(null);
			fail("Should not be here!");
		} catch (IllegalArgumentException ex) {
			isException = true;
		}
		assertEquals(true, isException);
	}

	@Test
	public void reverseSentenceEmpty() {
		StringBuilder sb = serviceFunctions.reverseSentence("");
		assertEquals(addQuoteToString(""), addQuoteToString(sb.toString()));
	}

	@Test
	public void getFibonacciTest93() {
		boolean isException = false;
		try {
			serviceFunctions.getFibonacciOf(93);
		} catch (IllegalArgumentException ex) {
			isException = true;
		}
		assertEquals(true, isException);
	}

	@Test
	public void getFibonacciTest76() {
		long expectedResult = 3416454622906707l;
		long result = serviceFunctions.getFibonacciOf(76);
		assertEquals(expectedResult, result);
	}

	@Test
	public void getFibonacciTestMinusOne() {
		boolean isException = false;
		try {
			serviceFunctions.getFibonacciOf(-1);
		} catch (IllegalArgumentException ex) {
			isException = true;
		}
		assertEquals(true, isException);
	}

	private String addQuoteToString(String s) {
		return "\"" + s + "\"";
	}
}
