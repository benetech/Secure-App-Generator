/*

Martus(TM) is a trademark of Beneficent Technology, Inc. 
This software is (c) Copyright 2015, Beneficent Technology, Inc.

Martus is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later
version with the additions and exceptions described in the
accompanying Martus license file entitled "license.txt".

It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, including warranties of fitness of purpose or
merchantability.  See the accompanying Martus License and
GPL license for more details on the required license terms
for this software.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.

*/

package org.benetech.secureapp.generator;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.martus.util.TestCaseEnhanced;

public class TestImageResize extends TestCaseEnhanced
{
	public TestImageResize(String name)
	{
		super(name);
	}

	public void testResizeLargePngImage() throws Exception
	{
		final int size = 36;
		File source = new File("./src/test/java/org/benetech/secureapp/generator/largeImage.png");
		assertTrue("large image not found?", source.exists());
		BufferedImage bufferedImage = ImageIO.read(source);
		BufferedImage scaledImg = Scalr.resize(bufferedImage, Scalr.Mode.AUTOMATIC, size, size);
		assertEquals("Original image height incorrect?", 587, bufferedImage.getHeight());
		assertEquals("Original image width incorrect?", 303, bufferedImage.getWidth());
		assertEquals("Image height not scaled down?", 36, scaledImg.getHeight());
		assertEquals("Image width not scaled down?", 19, scaledImg.getWidth());
		File destination = File.createTempFile("testLargeImage", ".png");
		destination.deleteOnExit();
		ImageIO.write(scaledImg, "png", destination);
		assertTrue("File not saved?", destination.exists());
		BufferedImage retrievedImage = ImageIO.read(destination);
		assertEquals("retrieved Image height not scaled down?", 36, retrievedImage.getHeight());
		assertEquals("retrieved Image width not scaled down?", 19, retrievedImage.getWidth());
	}
	
	public void testResizeSmallJpegImage() throws Exception
	{
		final int size = 36;
		File source = new File("./src/test/java/org/benetech/secureapp/generator/smallImage.jpg");
		assertTrue("small image not found?", source.exists());
		BufferedImage bufferedImage = ImageIO.read(source);
		BufferedImage scaledImg = Scalr.resize(bufferedImage, Scalr.Mode.AUTOMATIC, size, size);
		assertEquals("Original image height incorrect?", 22, bufferedImage.getHeight());
		assertEquals("Original image width incorrect?", 22, bufferedImage.getWidth());
		assertEquals("Image height not scaled up?", 36, scaledImg.getHeight());
		assertEquals("Image width not scaled up?", 36, scaledImg.getWidth());
		File destination = File.createTempFile("testSmallImage", ".png");
		destination.deleteOnExit();
		ImageIO.write(scaledImg, "png", destination);
		assertTrue("File not saved as png?", destination.exists());
		BufferedImage retrievedImage = ImageIO.read(destination);
		assertEquals("retrieved Image height not scaled up?", 36, retrievedImage.getHeight());
		assertEquals("retrieved Image width not scaled up?", 36, retrievedImage.getWidth());
	}
	
	public void testBuildingImages() throws Exception
	{
		verifyResize(BuildingApkController.APK_NODPI_SIZE);
		verifyResize(BuildingApkController.APK_MDPI_SIZE);
		verifyResize(BuildingApkController.APK_HDPI_SIZE);
		verifyResize(BuildingApkController.APK_XHDPI_SIZE);
		verifyResize(BuildingApkController.APK_XXHDPI_SIZE);
	}

	private void verifyResize(final int size) throws IOException
	{
		String originalImageLocation = "./src/test/java/org/benetech/secureapp/generator/smallImage.jpg";
		File temp = File.createTempFile("testBuildingImages", "test");
		File destination = BuildingApkController.resizeAndSavePngImage(temp.getParentFile(), originalImageLocation, size, "ImageTest");
		temp.delete();
		destination.deleteOnExit();
		assertTrue("File not saved as png?", destination.exists());
		BufferedImage retrievedImage = ImageIO.read(destination);
		assertEquals("retrieved Image height not scaled correctly?", size, retrievedImage.getHeight());
		assertEquals("retrieved Image width not scaled correctly?", size, retrievedImage.getWidth());
		InputStream is = new BufferedInputStream(new FileInputStream(destination));
		String mimeType = URLConnection.guessContentTypeFromStream(is);
		is.close();
		assertTrue(mimeType.contains("image/png"));
	}

}
