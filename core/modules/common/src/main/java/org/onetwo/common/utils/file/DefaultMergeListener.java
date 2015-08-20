package org.onetwo.common.utils.file;



public class DefaultMergeListener implements MergeFileListener {
	@Override
	public void onFileStart(MergeFileContext context) throws Exception {
		String fileName = FileUtils.getFileName(context.getFile().getPath());
		System.out.println("merege file["+context.getFileIndex()+"] : " + fileName);
		context.getWriter().write("\n");
	}
	
	@Override
	public void writeLine(MergeFileContext context, String line, int lineIndex) throws Exception{
		context.getWriter().write(line + "\n");
	}

	@Override
	public void onFileEnd(MergeFileContext context) throws Exception {
		context.getWriter().write("\n\n\n");
	}

	@Override
	public void onStart(MergeFileContext context) throws Exception {
	}

	@Override
	public void onEnd(MergeFileContext context) throws Exception {
	}
	
}
