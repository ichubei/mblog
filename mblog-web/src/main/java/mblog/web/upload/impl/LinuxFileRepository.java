/**
 * 
 */
package mblog.web.upload.impl;

import java.io.File;
import java.io.IOException;

import mblog.core.context.AppContext;
import mblog.core.utils.FileNameUtils;
import mblog.web.upload.Repository;
import mtons.modules.utils.GMagickUtils;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author langhsu
 *
 */
public class LinuxFileRepository extends AbstractFileRepository implements Repository {
	@Autowired
	private AppContext appContext;
	
	@Override
	public String store(MultipartFile file, String basePath) throws IOException {
		validateFile(file);
		
		String realPath = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		
		File temp = new File(realPath + basePath + path);
		if (!temp.getParentFile().exists()) {
			temp.getParentFile().mkdirs();
		}
		file.transferTo(temp);
		return basePath + path;
	}
	
	@Override
	public String store(File file, String basePath) throws IOException {
		String root = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));
		
		File dest = new File(root + basePath + path);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		FileUtils.copyDirectory(file, dest);
		return basePath + path;
	}

	@Override
	public String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
		validateFile(file);
		
		String realPath = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		
		File temp = new File(realPath + appContext.getTempDir() + path);
		if (!temp.getParentFile().exists()) {
			temp.getParentFile().mkdirs();
		}
		file.transferTo(temp);
		
		String dest = realPath + basePath + path;
		
		GMagickUtils.scaleImageByWidth(temp.getAbsolutePath(), dest, maxWidth);
		
		temp.delete();
		return basePath + path;
	}

	@Override
	public String storeScale(File file, String basePath, int maxWidth) throws Exception {
		String root = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));
		
		String dest = root + basePath + path;
		GMagickUtils.scaleImageByWidth(file.getAbsolutePath(), dest, maxWidth);
		return basePath + path;
	}

}
