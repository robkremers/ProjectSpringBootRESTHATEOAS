package com.rkremers.rest;

public class TestThisClass {

	public TestThisClass() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		System.out.println();

		TestThisClass self = new TestThisClass();
		self.printMessage();
		
//		TestInput testInput = new TestInput();
		
	}
	
	public void printMessage() {
		System.out.println("this.Class = " + this.getClass() );
	}	
	
	protected class TestInput {
		
		public TestInput() {}
		
		public void printMessage() {
			System.out.println("this.Class = " + this.getClass() );
		}
	}

}
