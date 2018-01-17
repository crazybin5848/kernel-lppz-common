package com.lppz.configuration.jedis;
public class JedisClusterPool{
	public String getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(String maxTotal) {
		this.maxTotal = maxTotal;
	}
	public String getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(String maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public boolean isTestOnReturn() {
		return testOnReturn;
	}
	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}
	public boolean isBlockWhenExhausted() {
		return blockWhenExhausted;
	}
	public void setBlockWhenExhausted(boolean blockWhenExhausted) {
		this.blockWhenExhausted = blockWhenExhausted;
	}
	public String getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}
	public void setNumTestsPerEvictionRun(String numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}
	private String maxTotal;
	private String maxIdle;
	public String getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}
	private String maxWaitMillis;
	private boolean testOnBorrow;
	private boolean testOnReturn;
	private boolean blockWhenExhausted;
	private String numTestsPerEvictionRun;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JedisClusterPool [maxTotal=");
		builder.append(maxTotal);
		builder.append(", maxIdle=");
		builder.append(maxIdle);
		builder.append(", maxWaitMillis=");
		builder.append(maxWaitMillis);
		builder.append(", testOnBorrow=");
		builder.append(testOnBorrow);
		builder.append(", testOnReturn=");
		builder.append(testOnReturn);
		builder.append(", blockWhenExhausted=");
		builder.append(blockWhenExhausted);
		builder.append(", numTestsPerEvictionRun=");
		builder.append(numTestsPerEvictionRun);
		builder.append("]");
		return builder.toString();
	}
}
