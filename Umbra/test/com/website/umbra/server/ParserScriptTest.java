package com.website.umbra.server;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class ParserScriptTest {
	
	private ParserScript ps;
	
	@Before
	public void setup() {
		ps = new ParserScript();
	}

	@Test // Empty Crime List, expect 0 crimes
	public void testZeroCrimes() {
		try {
			URL masterFile = new File("test/testfiles/test0.txt").toURI().toURL();
			int latestMonth = 0;
			ps.parseMasterFileTest(masterFile, latestMonth);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		assertTrue(ps.getCrimesTest().size() == 0);
	}
	
	@Test // 6 properly formated crimes, expect 6/6 crimes
	public void testFullCrimes() {
		try {
			URL masterFile = new File("test/testfiles/test1.txt").toURI().toURL();
			int latestMonth = 0;
			ps.parseMasterFileTest(masterFile, latestMonth);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		assertTrue(ps.getCrimesTest().size() == 6);
	}
	
	@Test // Less than 5 fields (crimeType, year, month, address, latlon), expect 0/1 
	public void testLessFieldsCrimes() {
		try {
			URL masterFile = new File("test/testfiles/test2.txt").toURI().toURL();
			int latestMonth = 0;
			ps.parseMasterFileTest(masterFile, latestMonth);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		assertTrue(ps.getCrimesTest().size() == 0);
	}
	
	@Test // More than 5 fields (crimeType, year, month, address, latlon), expect 0/1 
	public void testMoreFieldsCrimes() {
		try {
			URL masterFile = new File("test/testfiles/test3.txt").toURI().toURL();
			int latestMonth = 0;
			ps.parseMasterFileTest(masterFile, latestMonth);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		assertTrue(ps.getCrimesTest().size() == 0);
	}
	
}
