package es.inteco.crawler.dao;

import java.io.Serializable;

/**
 * The Class ProxyForm.
 */
public class ProxyForm  implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2792056465019141849L;
	
	/** The status. */
	private Integer status;
	
	/** The url. */
	private String url;
	
	/** The port. */
	private String port;
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	
	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(String port) {
		this.port = port;
	}

}
