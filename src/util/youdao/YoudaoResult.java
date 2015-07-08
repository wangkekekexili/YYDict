package util.youdao;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Represent a result from Youdao dict api.
 * 
 * <p>Fields are public just for Jackson data binding.</p>
 * 
 * @author kwang
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YoudaoResult {
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class YoudaoBasic {
		public String[] explains;
	}
	
	public String query;
	public YoudaoErrorCode errorCode;
	public YoudaoBasic basic;
	
}
