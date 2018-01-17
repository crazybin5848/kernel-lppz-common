package com.lppz.elasticsearch.result;

public class ResultStatsSearchAgg extends ResultSearchAgg{
	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	private double min=-1;
	private double max=-1;
	private double avg=-1;
	private double sum=-1;
	private long count=-1l;
}
