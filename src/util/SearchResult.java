package util;

public class SearchResult {
	private boolean hasResult;
	private String content;
	private String audioFileName;
	
	public SearchResult(boolean hasResult, String content) {
		this.hasResult = hasResult;
		this.content = content;
	}
	
	public SearchResult(boolean hasResult, 
			String content, String audioFileName) {
		this.hasResult = hasResult;
		this.content = content;
		this.audioFileName = audioFileName;
	}
	
	public String getContent() {
		return content;
	}
	
	public boolean hasResult() {
		return hasResult;
	}
	
	public String getAudioFileName() {
		return audioFileName;
	}
	
}
