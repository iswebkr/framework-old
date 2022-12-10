package kr.isweb.cmmn.util.cmmn;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.isweb.cmmn.util.egov.EgovResourceCloseHelper;

public class CmmnImageUtil {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Path sourceImageFile = null;

	private int thumbnailImageWidth = 0;

	private int thumbnailImageHeight = 0;

	private String watermarkText = null;

	private String watermarkTextFontString = Font.SANS_SERIF;

	private int watermarkTextFontSize = 100;

	private File watermarkImage;

	private int watermarkImageWidth = 0;

	private int watermarkImageHeight = 0;

	private float watermarkConstantAlpha = 0.4f;

	private float quality = 1.0f;

	private Map<String, Object> resultMap;

	public CmmnImageUtil init() {
		resultMap = new HashMap<>();
		return this;
	}

	public CmmnImageUtil setSourceImageFile(Path sourceImageFile) {

		this.sourceImageFile = sourceImageFile;

		BufferedImage originalBufferedImage = null;

		try {
			File sourceFile = getSourceImageFile().toFile();
			originalBufferedImage = ImageIO.read(sourceFile);

			if(this.watermarkImageWidth <= 0) {
				this.watermarkImageWidth  = originalBufferedImage.getWidth();
			}

			if(this.watermarkImageHeight <= 0) {
				this.watermarkImageHeight = originalBufferedImage.getHeight();
			}

			if(this.thumbnailImageWidth <= 0) {
				this.thumbnailImageWidth = (originalBufferedImage.getWidth() * 30) / 100;
				this.thumbnailImageHeight = this.thumbnailImageWidth;
			}
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		} finally {
			originalBufferedImage = null;
		}

		return this;
	}

	public CmmnImageUtil setThumbnailImageWidth(int thumbnailImageWidth) {
		this.thumbnailImageWidth = thumbnailImageWidth;
		return this;
	}

	public CmmnImageUtil setThumbnailImageHeight(int thumbnailImageHeight) {
		this.thumbnailImageHeight = thumbnailImageHeight;
		return this;
	}

	public CmmnImageUtil setWatermarkImage(File watermarkImage) {
		this.watermarkImage = watermarkImage;
		return this;
	}

	public CmmnImageUtil setWatermarkText(String watermarkText) {
		this.watermarkText = watermarkText;
		return this;
	}

	public CmmnImageUtil setWatermarkImageWidth(int watermarkImageWidth) {
		this.watermarkImageWidth = watermarkImageWidth;
		return this;
	}

	public CmmnImageUtil setWatermarkImageHeight(int watermarkImageHeight) {
		this.watermarkImageHeight = watermarkImageHeight;
		return this;
	}

	public CmmnImageUtil setWatermarkConstantAlpha(float watermarkConstantAlpha) {
		this.watermarkConstantAlpha = watermarkConstantAlpha;
		return this;
	}

	public CmmnImageUtil setQuality(float quality) {
		if(quality <= 0 || quality > 1) {
			quality = 1.0f;
		}
		this.quality = quality;
		return this;
	}

	public Path getSourceImageFile() {
		if(sourceImageFile != null) {
			return sourceImageFile;
		} else {
			return null;
		}
	}

	public int getThumbnailImageWidth() {
		return thumbnailImageWidth;
	}

	public int getThumbnailImageHeight() {
		return thumbnailImageHeight;
	}

	public String getWatermarkText() {
		return watermarkText;
	}

	public String getWatermarkTextFontString() {
		return watermarkTextFontString;
	}

	public int getWatermarkTextFontSize() {
		return watermarkTextFontSize;
	}

	public File getWatermarkImage() {
		return watermarkImage;
	}

	public int getWatermarkImageWidth() {
		return watermarkImageWidth;
	}

	public int getWatermarkImageHeight() {
		return watermarkImageHeight;
	}

	public float getWatermarkConstantAlpha() {
		return watermarkConstantAlpha;
	}

	public float getQuality() {
		return quality;
	}

	public String result() {
		return new Gson().toJson(resultMap);
	}

	public CmmnImageUtil makeThumbnail() {
		resultMap = new HashMap<>();
		try {
			if(getSourceImageFile() != null) {
				String baseName = FilenameUtils.getBaseName(getSourceImageFile().getFileName().toString());
				String extension = FilenameUtils.getExtension(getSourceImageFile().getFileName().toString());
				String thumbnailFileName = baseName + "_" + String.valueOf(getThumbnailImageWidth()) + "x" + String.valueOf(getThumbnailImageHeight()) + "." + extension;
				File sourceFile = getSourceImageFile().toRealPath().toFile();
				File targetFile = getSourceImageFile().getParent().toRealPath().resolve(thumbnailFileName).toFile();

				Map<String, Object> thumbMap = makeThumbnailImage(sourceFile, targetFile);

				if(!StringUtils.isEmpty(getWatermarkText()) && (getWatermarkImageWidth() > 0 && getWatermarkImageHeight() > 0)) {
					Map<String, Object> map =  applyTextWatermark(targetFile, targetFile, getWatermarkText());
					thumbMap.put("watermarkTextFile", map);
				}

				if(getWatermarkImage() != null && (getWatermarkImageWidth() > 0 && getWatermarkImageHeight() >0)) {
					Map<String, Object> map = applyImageWatermark(targetFile, targetFile, getWatermarkImage());
					thumbMap.put("watermarkImageFile", map);
				}
				resultMap.put("thumbnail", thumbMap);
			}
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		}
		return this;
	}

	public CmmnImageUtil makeApplyTextWatermarkImage() {
		resultMap = new HashMap<>();

		try {
			if(getSourceImageFile() != null) {
				File sourceFile = getSourceImageFile().toRealPath().toFile();
				File targetFile = sourceFile;
				if(!StringUtils.isEmpty(getWatermarkText()) && (getWatermarkImageWidth() > 0 && getWatermarkImageHeight() > 0)) {
					Map<String, Object> map = applyTextWatermark(sourceFile, targetFile, watermarkText);
					resultMap.put("watermarkTextFile", map);
				}
			}
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		}

		return this;
	}

	public CmmnImageUtil makeApplyWatermarkImage() {
		resultMap = new HashMap<>();
		try {
			if(getSourceImageFile() != null) {
				File sourceFile = getSourceImageFile().toRealPath().toFile();
				File targetFile = sourceFile;
				if(getWatermarkImage() != null && (getWatermarkImageWidth() > 0 && getWatermarkImageHeight() >0)) {
					Map<String, Object> map = applyImageWatermark(sourceFile,  targetFile,  getWatermarkImage());
					resultMap.put("watermarkImageFile", map);
				}
			}
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		}
		return this;
	}

	private Map<String, Object> makeThumbnailImage(File source, File destination) {
		Map<String, Object> thumbMap = new HashMap<>();
		FileOutputStream fileOutputStream = null;
		ImageOutputStream imageOutputStream = null;

		try {
			String extension = FilenameUtils.getExtension(source.getName());
			BufferedImage originalBufferedImage = ImageIO.read(source);

			int imageType = "png".equalsIgnoreCase(extension) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

			double orgImageWidth = originalBufferedImage.getWidth(null);
			double orgImageHeight = originalBufferedImage.getHeight(null);

			int newImageWidth = thumbnailImageWidth;
			int newImageHeight = thumbnailImageHeight;

			if(orgImageWidth < orgImageHeight) {
				newImageWidth = (int)((orgImageWidth / orgImageHeight) * getThumbnailImageWidth());
			} else {
				newImageHeight = (int)((orgImageHeight / orgImageWidth) * getThumbnailImageHeight());
			}

			BufferedImage thumbnailImage = new BufferedImage(newImageWidth, newImageHeight, imageType);

			Graphics2D g = thumbnailImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g.drawImage(originalBufferedImage, 0, 0, newImageWidth, newImageHeight, null);
			g.setComposite(AlphaComposite.Src);
			g.dispose();

			int x = (thumbnailImage.getWidth() - newImageWidth) / 2;
			int y = (thumbnailImage.getHeight() - newImageHeight) / 2;

			if(extension.equalsIgnoreCase("jpg")) {
				ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("png").next();

				ImageWriteParam iwp = new JPEGImageWriteParam(Locale.getDefault());
				iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				iwp.setCompressionQuality(quality);

				fileOutputStream = new FileOutputStream(destination);

				imageOutputStream = ImageIO.createImageOutputStream(fileOutputStream);
				writer.setOutput(imageOutputStream);

				IIOImage image = new IIOImage(thumbnailImage, null, null);
				writer.write(null, image, iwp);

				writer.dispose();
			} else {
				BufferedImage thumbnailBufferedImage = thumbnailImage.getSubimage(x, y, newImageWidth, newImageHeight);
				ImageIO.write(thumbnailBufferedImage, extension,  destination);
			}

			Path destinationPath = Paths.get(destination.getCanonicalPath());

			thumbMap.put("thumbnailImageFileName",  destination.getName());
			thumbMap.put("thumbnailImageFileExtension",  extension);
			thumbMap.put("thumbnailImageFilePath",  destination.getCanonicalPath());
			thumbMap.put("thumbnailImageFileParentPath",  destinationPath.getParent().toFile().getCanonicalPath());
			thumbMap.put("thumbnailImageFileType",  Files.probeContentType(destinationPath));
			thumbMap.put("thumbnailImageFileSize",  destination.length());
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		} finally {
			EgovResourceCloseHelper.close(imageOutputStream);
			EgovResourceCloseHelper.close(fileOutputStream);
		}

		return thumbMap;
	}

	private Map<String, Object> applyTextWatermark(File source, File destination, String watermarkText) {
		Map<String, Object> watermarkTxtMap = new HashMap<String, Object>();
		ImageOutputStream imageOutputStream = null;
		FileOutputStream fileOutputStream = null;

		try {
			String extension = FilenameUtils.getExtension(source.getName());
			BufferedImage image = ImageIO.read(source);

			int imageType = "png".equalsIgnoreCase(extension) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
			BufferedImage watermarkdImage = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

			Graphics2D g = (Graphics2D) watermarkdImage.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g.drawImage(image, 0, 0, null);

			AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getWatermarkConstantAlpha());
			g.setComposite(comp);
			g.setColor(Color.WHITE);
			g.setFont(new Font(getWatermarkTextFontString(), Font.BOLD, getWatermarkTextFontSize()));
			FontMetrics fontMetrics = g.getFontMetrics();
			Rectangle2D rect = fontMetrics.getStringBounds(watermarkText, g);

			int centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
			int centerY = image.getHeight() / 2;

			g.drawString(watermarkText, centerX, centerY);
			g.dispose();

			if(extension.equalsIgnoreCase("jpg")) {
				ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("png").next();

				ImageWriteParam iwp = new JPEGImageWriteParam(Locale.getDefault());
				iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				iwp.setCompressionQuality(quality);

				fileOutputStream = new FileOutputStream(destination);

				imageOutputStream = ImageIO.createImageOutputStream(fileOutputStream);
				writer.setOutput(imageOutputStream);

				IIOImage iioimage = new IIOImage(watermarkdImage, null, null);
				writer.write(null, iioimage, iwp);

				writer.dispose();
			} else {
				ImageIO.write(watermarkdImage, extension, destination);
			}

			Path destinationPath = Paths.get(destination.getCanonicalPath());

			watermarkTxtMap.put("watermarkTextFileName",  destination.getName());
			watermarkTxtMap.put("watermarkTextFileExtention",  extension);
			watermarkTxtMap.put("watermarkTextFilePath",  destination.getCanonicalPath());
			watermarkTxtMap.put("watermarkTextFileParentPath",  destinationPath.getParent().toFile().getCanonicalPath());
			watermarkTxtMap.put("watermarkTextFileType",  Files.probeContentType(destinationPath));
			watermarkTxtMap.put("watermarkTextFileSize",  destination.length());
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		} finally {
			EgovResourceCloseHelper.close(imageOutputStream);
			EgovResourceCloseHelper.close(fileOutputStream);
		}

		return watermarkTxtMap;
	}

	private Map<String, Object> applyImageWatermark(File source, File destination, File watermark) {
		Map<String, Object> watermarkImageMap = new HashMap<String, Object>();
		FileOutputStream fileOutputStream = null;
		ImageOutputStream imageOutputStream = null;

		try {
			String extension = FilenameUtils.getExtension(source.getName());
			BufferedImage image = ImageIO.read(source);
			BufferedImage overlay = resizeImage(ImageIO.read(watermark), getWatermarkImageWidth(), getWatermarkImageHeight());

			int imageType = "png".equalsIgnoreCase(extension) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
			BufferedImage watermarkdImage = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

			Graphics2D g = (Graphics2D) watermarkdImage.getGraphics();
			g.drawImage(image, 0, 0, null);

			AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getWatermarkConstantAlpha());
			g.setComposite(comp);

			int centerX = image.getWidth() / 2;
			int centerY = image.getHeight() / 2;

			g.drawImage(overlay, centerX, centerY, null);
			g.dispose();

			if(extension.equalsIgnoreCase("jpg")) {
				ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("png").next();

				ImageWriteParam iwp = new JPEGImageWriteParam(Locale.getDefault());
				iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				iwp.setCompressionQuality(quality);

				fileOutputStream = new FileOutputStream(destination);

				imageOutputStream = ImageIO.createImageOutputStream(fileOutputStream);
				writer.setOutput(imageOutputStream);

				IIOImage iioimage = new IIOImage(watermarkdImage, null, null);
				writer.write(null, iioimage, iwp);

				writer.dispose();
			} else {
				ImageIO.write(watermarkdImage, extension, destination);
			}

			Path destinationPath = Paths.get(destination.getCanonicalPath());

			watermarkImageMap.put("watermarkImageFileName",  destination.getName());
			watermarkImageMap.put("watermarkImageFileExtension",  extension);
			watermarkImageMap.put("watermarkImageFilePath",  destination.getCanonicalPath());
			watermarkImageMap.put("watermarkImageFileParentPath",  destinationPath.getParent().toFile().getCanonicalPath());
			watermarkImageMap.put("watermarkImageFileType",  Files.probeContentType(destinationPath));
			watermarkImageMap.put("watermarkImageFileSize",  destination.length());
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		} finally {
			EgovResourceCloseHelper.close(imageOutputStream);
			EgovResourceCloseHelper.close(fileOutputStream);
		}

		return watermarkImageMap;
	}

	private BufferedImage resizeImage(BufferedImage bufferedImage, int width, int height) {
		Image tmpImage = bufferedImage.getScaledInstance(width,  height,  Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmpImage, 0, 0, null);
		g2d.dispose();
		return resized;
	}
}
