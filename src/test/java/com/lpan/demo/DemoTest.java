package com.lpan.demo;

import java.util.Random;

import org.junit.jupiter.api.Test;

public class DemoTest {
	
	@Test
	public void testRandom() {
		Random random = new Random();
		System.out.println(random.nextInt(100));
	}

}
