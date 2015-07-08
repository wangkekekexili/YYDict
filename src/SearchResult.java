
public class SearchResult {
	private boolean hasResult;
	private String content;
	
	public SearchResult(boolean hasResult, String content) {
		this.hasResult = hasResult;
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	public boolean hasResult() {
		return hasResult;
	}
	
}
