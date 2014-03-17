package com.devgeekslab.calcfactorial;

/**
 * Holds results from Native C module
 * @author Ankit Sing
 *
 */
public class ResultContent {
	int[] result;
	long size;
	
	
	public int[] getFactResult() {
		return result;
	}
	
	public void setFactResult(int[] factResult) {
		this.result = factResult;
	}
	
	public long getLength() {
		return size;
	}
	
	public void setLength(long length) {
		this.size = length;
	}
	
}
