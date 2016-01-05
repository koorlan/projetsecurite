/**
 * 
 */
package response;

import java.util.ArrayList;

/**
 * @author lisa
 *
 */
public class ResponseModel {
	
	private boolean isFormatted;
	private final String regex = 
		"(/BEG/{1})(1{1})::([0-1]{1})(/END/{1})";
	
	public ResponseModel()
	{
		this.setFormatted(false);
	}

	public boolean isFormatted() {
		return isFormatted;
	}

	public void setFormatted(boolean isFormatted) {
		this.isFormatted = isFormatted;
	}

	public String getRegex() {
		return regex;
	}
}
