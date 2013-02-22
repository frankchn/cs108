/**
 * Kludge to support Scriptlets within the New JSP Tags
 * 
 * Source: http://stackoverflow.com/questions/7165715/jsp-tags-scriptlet-how-to-enable-scriptlet
 */

package quiz.website;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class PushTag extends BodyTagSupport {

	private static final long serialVersionUID = 3171803347907865619L;
	private String key;

	@Override public int doAfterBody() throws JspException {
        pageContext.getRequest().setAttribute(key, getBodyContent().getString());
        return super.doAfterBody();
    }
    
	public String getKey() {
        return key;
    }
    
	public void setKey(String key) {
        this.key = key;
    }
	
}
