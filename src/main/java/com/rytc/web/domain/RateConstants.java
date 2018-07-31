package com.rytc.web.domain;

public class RateConstants {
	private int serialNo;
	private String arp;
	private String actRate;

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getArp() {
		return arp;
	}

	public void setArp(String arp) {
		this.arp = arp;
	}

	public String getActRate() {
		return actRate;
	}

	public void setActRate(String actRate) {
		this.actRate = actRate;
	}

	public RateConstants(int serialNo, String arp, String actRate) {
		super();
		this.serialNo = serialNo;
		this.arp = arp;
		this.actRate = actRate;
	}
}
