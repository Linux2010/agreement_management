/**   
 * <p>Copyright (c) 2016 Beijing AutoServe Software Ltd.</p>
 * <p>All Rights Reserved.</p>
 * 
 * <p>公司：北京奥拓思维软件有限公司</p>
 *
 * <p>文件：DcsPool.java</p>
 * <p>作者：xudi</p>
 * <p>时间：2017-01-21 22:40:03</p>
 */

package com.rytc.common.util.doc;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import application.dcs.Convert;

/**
 * <b>类名：</b>DcsPool<br>
 * <b>描述：</b>TODO<br>
 * 
 * <b>作者：</b>xudi<br>
 * <b>时间：</b>2017-01-21 22:40:03<br>
 * 
 */
public class DcsPool extends GenericObjectPool<Convert> {

	/**
	 * 使用默认配置
	 */
	public DcsPool() {
		super(new DcsPoolFactory());
	}

	/**
	 * 自定义配置
	 */
	public DcsPool(GenericObjectPoolConfig config) {
		super(new DcsPoolFactory(), config);
	}

}
