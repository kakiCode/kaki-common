package org.aprestos.labs.data.common.influxdb;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PointDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long timestamp;
	private String measurement;
	private Map<String, String> tags;
	private Map<String, Object> fields;
	
	public PointDto() { 
		tags = new HashMap<String, String>();
		fields = new HashMap<String, Object>();
	}
	
	public PointDto(@JsonProperty("timestamp") Long timestamp, @JsonProperty("measurement") String measurement, 
			@JsonProperty("tags") Map<String, String> tags, @JsonProperty("fields") Map<String, Object> fields) {
		this.timestamp = timestamp;
		this.measurement = measurement;
		this.tags = tags;
		this.fields = fields;
	}
	
	@JsonProperty("timestamp")
	public Long getTimestamp() {
		return timestamp;
	}
	
	@JsonProperty("timestamp")
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	@JsonProperty("measurement")
	public String getMeasurement() {
		return measurement;
	}
	@JsonProperty("measurement")
	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	@JsonProperty("tags")
	public Map<String, String> getTags() {
		return new HashMap<String,String>(tags);
	}
	
	@JsonProperty("fields")
	public Map<String, Object> getFields() {
		return new HashMap<String, Object>(fields);
	}
	
	public void addField(String key, Object value){
		if (value instanceof Number) {
	        if (value instanceof Byte) {
	          value = ((Byte) value).doubleValue();
	        } else if (value instanceof Short) {
	          value = ((Short) value).doubleValue();
	        } else if (value instanceof Integer) {
	          value = ((Integer) value).doubleValue();
	        } else if (value instanceof Long) {
	          value = ((Long) value).doubleValue();
	        } else if (value instanceof BigInteger) {
	          value = ((BigInteger) value).doubleValue();
	        }
	      }
		fields.put(key, value);
	}
	
	public void addTag(String key, String value){
		tags.put(key, value);
	}
	
	public String toString(){
		StringBuffer buff = new StringBuffer(this.measurement);
		if(!tags.isEmpty())
			for(Map.Entry<String, String> e:tags.entrySet()){
				buff.append(",");
				buff.append(String.format("%s=%s", e.getKey(), e.getValue()));
			}
		if(!fields.isEmpty()){
			buff.append(" ");
			for(Map.Entry<String, Object> e:fields.entrySet()){
				buff.append(String.format("%s=%s", e.getKey(), e.getValue()));
				buff.append(",");
			}
			buff.deleteCharAt(buff.length()-1);
		}
		buff.append(" ");
		buff.append(timestamp);
		return buff.toString();
	}
	

}
