/**   
 * <p>Copyright (c) 2016 Beijing AutoServe Software Ltd.</p>
 * <p>All Rights Reserved.</p>
 * 
 * <p>公司：北京奥拓思维软件有限公司</p>
 *
 * <p>文件：DcsPoolFactory.java</p>
 * <p>作者：xudi</p>
 * <p>时间：2017-01-20 10:06:03</p>
 */

package com.rytc.common.util.doc;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.rytc.common.util.Constant;

import application.dcs.Convert;

/**
 * <b>类名：</b>DcsPoolFactory<br>
 * <b>描述：</b>DCS转换对象池<br>
 * 
 * <b>作者：</b>xudi<br>
 * <b>时间：</b>2017-01-20 10:06:03<br>
 * 
 */
public class DcsPoolFactory implements PooledObjectFactory<Convert> {

	@Override
	public PooledObject<Convert> makeObject() throws Exception {
		//用做测试
//		String filePath= DcsPoolFactory.class.getResource("/doc").getPath();
		//发布环境
		String filePath= this.getClass().getClassLoader().getResource("/doc").getPath();
		Convert convert = new Convert(filePath);
		convert.setTempPath(Constant.FILEPATH);
		convert.setAutoDeleteTempFiles(true);
		return new DefaultPooledObject<Convert>(convert);
	}

	@Override
	public void destroyObject(PooledObject<Convert> p) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateObject(PooledObject<Convert> p) {
		return true;
	}

	@Override
	public void activateObject(PooledObject<Convert> p) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void passivateObject(PooledObject<Convert> p) throws Exception {
		// TODO Auto-generated method stub

	}

}
