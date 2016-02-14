package com.js.vmm.data;

public class Record {
	
	// Ö÷¼ü
	int num;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}

	private String name = "";
	private String date = "";
	private boolean synState = false;
	private String barcode = "";
	private int inprice = 0;
	private int outprice = 0;
	private String specification = "";
	
	private String jsonDesc = "";

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public int getInprice() {
		return inprice;
	}
	public void setInprice(int inprice) {
		this.inprice = inprice;
	}
	public int getOutprice() {
		return outprice;
	}
	public void setOutprice(int outprice) {
		this.outprice = outprice;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isSynState() {
		return synState;
	}
	public void setSynState(boolean synState) {
		this.synState = synState;
	}
	public String getJsonDesc() {
		return jsonDesc;
	}
	public void setJsonDesc(String jsonDesc) {
		this.jsonDesc = jsonDesc;
	}
	
}
