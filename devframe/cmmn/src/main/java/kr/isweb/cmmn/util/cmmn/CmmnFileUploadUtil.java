package kr.isweb.cmmn.util.cmmn;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import egovframework.rte.fdl.cmmn.exception.EgovBizException;

public class CmmnFileUploadUtil extends CmmnImageUtil {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String jsonResult;

	@SuppressWarnings("unchecked")
	public CmmnFileUploadUtil attempUpload(String savePath, MultipartFile[] files) throws EgovBizException {

		Gson gson = new Gson();
		List<Map<String, Object>> fileList = new ArrayList<>();

		LocalDateTime date = LocalDateTime.now(ZoneId.systemDefault());
		String year = String.valueOf(date.getYear());
		String month = String.format("%02d", date.getMonthValue());
		String day = String.format("%02d", date.getDayOfMonth());
		String time = String.format("%02d", date.getHour()) + String.format("%02d", date.getMinute());
		Path targetPath = Paths.get(savePath, year, month, day, time);

		try {
			if (!Files.exists(targetPath)) {
				Files.createDirectories(targetPath);
			}

			for (MultipartFile file : files) {
				Map<String, Object> map = new ConcurrentHashMap<>();
				if(file != null) {
					String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
					String extension = StringUtils.getFilenameExtension(originalFilename);
					String saveFilename = createNewFileName(extension);

					long size = file.getSize();

					if (originalFilename.contains("..")) {
						deleteFileInList(fileList);
						throw new EgovBizException("Cannot store file with relative path outside current directory " + originalFilename);
					} else {
						InputStream inputStream = file.getInputStream();
						Path targetSaveFile = targetPath.resolve(saveFilename);
						Files.copy(inputStream, targetSaveFile, StandardCopyOption.REPLACE_EXISTING);
						inputStream.close();

						map.put("originalFilename", originalFilename);
						map.put("extension", extension);
						map.put("saveFilename", saveFilename);
						map.put("size", size);
						map.put("contentType", file.getContentType());
						map.put("parentPath", targetSaveFile.getParent().toFile().getCanonicalPath());
						map.put("absolutePath", targetSaveFile.toFile().getCanonicalPath());

						// if there is contain "image" in contentType
						if (file.getContentType().contains("image")) {
							String jsonWmTxt = setSourceImageFile(targetSaveFile).makeApplyTextWatermarkImage().result();
							String jsonWmImg = setSourceImageFile(targetSaveFile).makeApplyWatermarkImage().result();
							String jsonThumbnail = setSourceImageFile(targetSaveFile).makeThumbnail().result();

							Map<String, Object> mapWmTxt = gson.fromJson(jsonWmTxt, HashMap.class);
							Map<String, Object> mapWmImg = gson.fromJson(jsonWmImg, HashMap.class);
							Map<String, Object> mapThumb = gson.fromJson(jsonThumbnail, HashMap.class);

							if(MapUtils.isNotEmpty(mapWmTxt)) {
								map.putAll(mapWmTxt);
							}
							if(MapUtils.isNotEmpty(mapWmImg)) {
								map.putAll(mapWmImg);
							}
							if(MapUtils.isNotEmpty(mapThumb)) {
								map.putAll(mapThumb);
							}
						}
						if(MapUtils.isNotEmpty(map)) {
							fileList.add(map);
						}
					}
				}
			}
		} catch (JsonSyntaxException | EgovBizException | IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
			deleteFileInList(fileList);
		} finally {
			this.jsonResult = new Gson().toJson(fileList);
		}

		return this;
	}

	public void deleteFileInList(List<Map<String, Object>> list) {
		list.stream().forEach(map -> {
			Path path = Paths.get(map.get("absolutePath").toString());
			try {
				// delete file
				if (path.isAbsolute()) {
					Files.deleteIfExists(path);
				}

				// delete directory
				if(Files.isDirectory(path)) {
					try (Stream<Path> entries = Files.list(path)) {
						if(!entries.findFirst().isPresent()) {
							Files.delete(path);
						}
					}
				}
			} catch (IOException e) {
				if(logger.isErrorEnabled()) {
					logger.error(e.getMessage());
				}
			}
		});
	}

	public String getJsonResultStr() {
		return jsonResult;
	}

	public List<Map<String, Object>> getJsonResultList() {
		Type type = new TypeToken<ArrayList<Map<String, Object>>>() {
		}.getType();
		return new Gson().fromJson(jsonResult, type);
	}

	public Stream<Path> getFileList(String targetPath) {
		Path path = Paths.get(targetPath);
		Stream<Path> fileList = null;
		try {
			fileList = Files.walk(path, 1).filter(p -> !p.equals(path)).map(p -> path.relativize(p));
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		}
		return fileList;
	}

	public void deleteAll(String targetPath) {
		FileSystemUtils.deleteRecursively(Paths.get(targetPath).toFile());
	}

	private String createNewFileName(String extension) {
		return ThreadLocal.withInitial(() -> UUID.randomUUID().toString()).get() + "." + extension;
	}
}
