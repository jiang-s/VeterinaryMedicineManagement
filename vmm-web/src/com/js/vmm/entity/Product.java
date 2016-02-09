package com.js.vmm.entity;

import java.sql.Timestamp;

public class Product {

	int pId;
	String pName;
	String pSpecification;
	String pManufacturer;
	Timestamp pDate;
	double pInPrice;
	double pOutPrice;
	
	public int getpId() {
		System.out.println("get id: "+pId);
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpSpecification() {
		return pSpecification;
	}
	public void setpSpecification(String pSpecification) {
		this.pSpecification = pSpecification;
	}
	public String getpManufacturer() {
		return pManufacturer;
	}
	public void setpManufacturer(String pManufacturer) {
		this.pManufacturer = pManufacturer;
	}
	public Timestamp getpDate() {
		return pDate;
	}
	public void setpDate(Timestamp pDate) {
		this.pDate = pDate;
	}
	public double getpInPrice() {
		return pInPrice;
	}
	public void setpInPrice(double pInPrice) {
		this.pInPrice = pInPrice;
	}
	public double getpOutPrice() {
		return pOutPrice;
	}
	public void setpOutPrice(double pOutPrice) {
		this.pOutPrice = pOutPrice;
	}
	
}
