package com.github.fullautomation.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;

public class ValidateFiles {

	public void assertFilesAreCommon(File file1, File file2) throws FileNotFoundException, IOException {
		Assert.assertTrue(IOUtils.contentEquals(new FileInputStream(file1), new FileInputStream(file2)), "Both files are not equal");
		Assert.assertEquals(file1.length(), file2.length(), "Size of the files are not equal");
	}
	
}
