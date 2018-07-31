/**   
 * <p>Copyright (c) 2016 Beijing AutoServe Software Ltd.</p>
 * <p>All Rights Reserved.</p>
 * 
 * <p>公司：北京奥拓思维软件有限公司</p>
 *
 * <p>文件：PoolFactory.java</p>
 * <p>作者：xudi</p>
 * <p>时间：2017-01-22 19:54:12</p>
 */

package com.rytc.common.util.doc;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * <b>类名：</b>PoolFactory<br>
 * <b>描述：</b>TODO<br>
 * 
 * <b>作者：</b>xudi<br>
 * <b>时间：</b>2017-01-22 19:54:12<br>
 * 
 */
public class PoolFactory {
	public static DcsPool createDcsPool() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMinIdle(3);
		poolConfig.setMaxTotal(5);
		DcsPool dcsPool = new DcsPool();
		return dcsPool;
	}
}
