package com.jex.utils.cache;

import com.jex.utils.prop.PropertiesUtils;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MemcachedUtils {

	private static final Logger logger = LogManager.getLogger(MemcachedUtils.class);

	private static final String FILE_PATH = "/memcached.properties";

	private static String[] servers = { "127.0.0.1:11211" };
	private static Integer[] weights = { 1 };
	private static final Map<String, String> serverConfig = new HashMap<String, String>();

	// 是否启用memcached
	private static final AtomicBoolean isUsed = new AtomicBoolean();

	private static SockIOPool pool;

	private static class InstanceHolder {
		private static final MemcachedUtils instance = new MemcachedUtils();
	}

	private MemcachedUtils() {
		logger.info("=================== 初始化配置文件 ===================");
		initConfig();
		logger.info("=================== 初始化 memcached ===============");
		initMemcached();
	}

	public static MemcachedUtils getInstance() {
		return InstanceHolder.instance;
	}

	/**
	 * 初始化参数配置
	 */
	private void initConfig() {
		String[] sers = PropertiesUtils.getProperty(FILE_PATH, "memcached.servers").split(",");
		String[] wgts = PropertiesUtils.getProperty(FILE_PATH, "memcached.weights").split(",");
		List<Integer> serverUsed = new ArrayList<Integer>();

		TelnetClient telnet = new TelnetClient();
		for (int i = 0, len = sers.length; i < len; i++) {
			int seg = sers[i].indexOf(":");
			try {
				telnet.connect(sers[i].substring(0, seg), Integer.parseInt(sers[i].substring(seg + 1)));
				telnet.disconnect();
				serverUsed.add(i);
			} catch (Exception e) {
			}
		}
		int serverCount = serverUsed.size();
		if (serverCount == 0) {
			isUsed.set(false);
			return;
		}

		servers = new String[serverCount];
		weights = new Integer[serverCount];
		for (int i = 0; i < serverCount; i++) {
			servers[i] = sers[serverUsed.get(i)];
			weights[i] = Integer.parseInt(wgts[serverUsed.get(i)]);
		}

		serverConfig.put("initConn", PropertiesUtils.getProperty(FILE_PATH, "memcached.initConn", "8"));
		serverConfig.put("minConn", PropertiesUtils.getProperty(FILE_PATH, "memcached.minConn", "8"));
		serverConfig.put("maxConn", PropertiesUtils.getProperty(FILE_PATH, "memcached.maxConn", "32"));
		serverConfig.put("maxIdle", PropertiesUtils.getProperty(FILE_PATH, "memcached.maxIdle", "8"));
		serverConfig.put("maintSleep", PropertiesUtils.getProperty(FILE_PATH, "memcached.maintSleep", "30000"));
		serverConfig.put("socketConnTO", PropertiesUtils.getProperty(FILE_PATH, "memcached.socketConnTO", "0"));
		serverConfig.put("socketTO", PropertiesUtils.getProperty(FILE_PATH, "memcached.socketTO", "10000"));
	}

	private void initMemcached() {
		if (!isUsed.get()) {
			logger.error("=================== 初始化 memcached 失败 ===============");
			return;
		}

		pool = SockIOPool.getInstance();

		// 服务器地址
		pool.setServers(servers);
		// 权重
		pool.setWeights(weights);

		// 初始连接数
		pool.setInitConn(Integer.parseInt(serverConfig.get("initConn")));
		// 最小连接数
		pool.setMinConn(Integer.parseInt(serverConfig.get("minConn")));
		// 最大连接数
		pool.setMaxConn(Integer.parseInt(serverConfig.get("maxConn")));
		// 最大空闲连接数
		pool.setMaxIdle(Integer.parseInt(serverConfig.get("maxIdle")));

		// 守护线程的休眠时间
		pool.setMaintSleep(Long.parseLong(serverConfig.get("maintSleep")));

		// 关闭nagle算法
		pool.setNagle(false);
		// 连接操作的超时时间, 0为不限制
		pool.setSocketConnectTO(Integer.parseInt(serverConfig.get("socketConnTO")));
		// 读取操作的超时时间
		pool.setSocketTO(Integer.parseInt(serverConfig.get("socketTO")));

		pool.initialize();
	}

	/**
	 * 重新加载memcached客户端
	 * @return
	 */
	public synchronized boolean reloadMemcached() {
		if (pool != null) {
			pool.shutDown();
			pool = null;
		}
		logger.info("=================== 初始化配置文件 ===================");
		initConfig();
		logger.info("=================== 初始化 memcached ===============");
		initMemcached();
		return isUsed.get();
	}

	/**
	 * 获取memcached客户端
	 * @return
	 */
	public MemCachedClient getMemCachedClient() {
		return new MemCachedClient();
	}

	/**
	 * 是否启用memcached
	 * @return
	 */
	public boolean isUsedMemcached() {
		return isUsed.get();
	}

	/**
	 * 插入新记录, 前提是记录的Key在缓存中不存在
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, Object value) {
		if (this.isUsedMemcached()) {
			return this.getMemCachedClient().add(key, value);
		}
		return false;
	}

	/**
	 * 插入新记录, 前提是记录的Key在缓存中不存在
	 * @param key
	 * @param value
	 * @param date 超时日期
	 * @return
	 */
	public boolean add(String key, Object value, Date date) {
		if (this.isUsedMemcached()) {
			return this.getMemCachedClient().add(key, value, date);
		}
		return false;
	}

	/**
	 * 插入新记录或更新已有记录, 记录的Key在缓存中不存在则插入否则更新
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, Object value) {
		if (this.isUsedMemcached()) {
			return this.getMemCachedClient().set(key, value);
		}
		return false;
	}

	/**
	 * 插入新记录或更新已有记录, 记录的Key在缓存中不存在则插入否则更新
	 * @param key
	 * @param value
	 * @param date 超时日期
	 * @return
	 */
	public boolean set(String key, Object value, Date date) {
		if (this.isUsedMemcached()) {
			return this.getMemCachedClient().set(key, value, date);
		}
		return false;
	}

	/**
	 * 更新已有记录, 前提是记录的Key在缓存中已经存在
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean replace(String key, Object value) {
		if (this.isUsedMemcached()) {
			return this.getMemCachedClient().replace(key, value);
		}
		return false;
	}

	/**
	 * 更新已有记录, 前提是记录的Key在缓存中已经存在
	 * @param key
	 * @param value
	 * @param date 超时日期
	 * @return
	 */
	public boolean replace(String key, Object value, Date date) {
		if (this.isUsedMemcached()) {
			return this.getMemCachedClient().replace(key, value, date);
		}
		return false;
	}

	/**
	 * 返回单条记录
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(String key, Class<T> clazz) {
		if (this.isUsedMemcached()) {
			return clazz.cast(this.getMemCachedClient().get(key));
		}
		return null;
	}

	/**
	 * 返回多条记录
	 * @param keys
	 * @return
	 */
	public Map<String, Object> get(String[] keys) {
		if (this.isUsedMemcached()) {
			return this.getMemCachedClient().getMulti(keys);
		}
		return null;
	}

	/**
	 * 删除记录, 执行该方法之后, 使用stats的统计结果会同步更新
	 * @param key
	 * @return
	 */
	public boolean delete(String key) {
		if (this.isUsedMemcached()) {
			return this.getMemCachedClient().delete(key);
		}
		return false;
	}

	/**
	 * 清空全部缓存数据, 执行该方法之后, 使用stats的统计结果不会马上发生变化, 每get一个不存在的item之后, 该item的值才会被动清空
	 * @return
	 */
	public boolean flushAll() {
		if (this.isUsedMemcached()) {
			return this.getMemCachedClient().flushAll();
		}
		return false;
	}

}
