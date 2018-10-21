package com.wx.ioc.wxdemo.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
 
import net.coobird.thumbnailator.Thumbnails;
 
public class ImageZoomServiceImpl implements ImageZoomService {
 
	@Override
	public void imgThumb(String source, String output, int width, int height) {
		try {
			Thumbnails.of(source).size(width, height).toFile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public void imgThumb(File source, String output, int width, int height) {
		try {
			Thumbnails.of(source).size(width, height).toFile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public void imgThumb(URL source, String output, int width, int height) {
		try {
			Thumbnails.of(source).size(width, height).toFile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public String imgThumbOutputStream(URL source, int width, int height) {
		ByteArrayOutputStream baos = null;
		String binary = null;
		try {
			baos = new ByteArrayOutputStream();
			Thumbnails.of(source).size(width, height).toOutputStream(baos);
			binary = Base64.encode(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return binary;
	}
 
	@Override
	public void imgScale(String source, String output, double scale) {
		try {
			Thumbnails.of(source).scale(scale).toFile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public void imgScale(File source, String output, double scale) {
		try {
			Thumbnails.of(source).scale(scale).toFile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public void imgScale(URL source, String output, double scale) {
		try {
			Thumbnails.of(source).scale(scale).toFile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public String imgScaleOutputStream(URL source, double scale) {
		ByteArrayOutputStream baos = null;
		String binary = null;
		try {
			baos = new ByteArrayOutputStream();
			Thumbnails.of(source).scale(scale).toOutputStream(baos);
			binary = Base64.encode(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return binary;
	}
 
	@Override
	public void imgNoScale(String source, String output, int width, int height, boolean keepAspectRatio) {
		try {
			Thumbnails.of(source).size(width, height).keepAspectRatio(keepAspectRatio).toFile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public void imgNoScale(File source, String output, int width, int height, boolean keepAspectRatio) {
		try {
			Thumbnails.of(source).size(width, height).keepAspectRatio(keepAspectRatio).toFile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public void imgNoScale(URL source, String output, int width, int height, boolean keepAspectRatio) {
		try {
			Thumbnails.of(source).size(width, height).keepAspectRatio(keepAspectRatio).toFile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public String imgNoScaleOutputStream(URL source, int width, int height, boolean keepAspectRatio) {
		ByteArrayOutputStream baos = null;
		String binary = null;
		try {
			baos = new ByteArrayOutputStream();
			Thumbnails.of(source).size(width, height).keepAspectRatio(keepAspectRatio).toOutputStream(baos);
			binary = Base64.encode(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return binary;
	}
 
	@Override
	public OutputStream imgOutputStream(String source, String output, int width, int height) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(output);
			Thumbnails.of(source).scale(width, height).toOutputStream(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		return os;
	}
 
	@Override
	public OutputStream imgOutputStream(File source, String output, int width, int height) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(output);
			Thumbnails.of(source).scale(width, height).toOutputStream(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os;
	}
}
