package com.speedreader;

public class Test {
	public static void main(String args[]){
		Reader reader = new Reader(4, "ivanthefool.txt");
		System.out.println("------------------");
		System.out.println("Chunked Text Below");
		System.out.println("------------------");
		reader.printStoryInChunks();
	}
}
