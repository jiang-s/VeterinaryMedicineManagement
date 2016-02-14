package com.js.vmm.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.js.vmm.data.JsonDesc;
import com.js.vmm.data.Record;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class ParseJson {
	
	public static JsonDesc getRecordFromJsonString(String s) {
		JsonDesc jsonDesc = new JsonDesc();
		JSONObject med;
		try {
			med = new JSONObject(s);
			if(med.get("img") != null)
				jsonDesc.setImg((String) med.get("img"));
			if(med.get("name") != null)
				jsonDesc.setName((String) med.get("name"));
			if(med.get("factory") != null)
				jsonDesc.setFactory((String) med.get("factory"));
			if(med.get("description") != null)
				jsonDesc.setDescription((String) med.get("description"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonDesc;
	}

	public static String recordToJSON(Record record) {
		JSONStringer jsonText = new JSONStringer();  
		try {
			jsonText.object();
			jsonText.key("name").value(record.getName());
			jsonText.key("date").value(record.getDate());
			jsonText.key("barcode").value(record.getBarcode());
			jsonText.key("inprice").value(record.getInprice());
			jsonText.key("outprice").value(record.getOutprice());
			jsonText.key("specification").value(record.getSpecification());
			jsonText.key("jsonDesc").value(record.getJsonDesc());
		    jsonText.endObject(); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return jsonText.toString();
	}
	
}
