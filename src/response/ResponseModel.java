/**
 * 
 */
package response;

/**
 * @author lisa
 *
 */
public class ResponseModel {
	
	private boolean isFormatted;
	private final String regex = 
		"(.BEG.)([0-1]{1})::([0-1]{1})::(.{0,})::(.{0,})::(.{0,})::(.{0,})::(.END.)";
	
	
	
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
