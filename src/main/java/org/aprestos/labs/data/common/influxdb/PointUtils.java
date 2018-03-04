package org.aprestos.labs.data.common.influxdb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.influxdb.dto.Point;

public class PointUtils {

	private static ObjectMapper objMapper;
	
	private static ObjectMapper getMapper(){
		if(null == objMapper)
			objMapper = new ObjectMapper();
		return objMapper;
	}
	
	public static String toJson(PointDto point) throws Exception {
		return getMapper().writeValueAsString(point);
	}
	
	public static PointDto fromJson(String p) throws Exception{
		return getMapper().readValue(p, PointDto.class);
	}
	
	public static byte[] toBytes(PointDto point) throws Exception {
		return getMapper().writeValueAsBytes(point);
	}
	
	public static PointDto fromBytes(byte[] p) throws Exception{
		return getMapper().readValue(p, PointDto.class);
	}
	
	public static Point pointDto2Point(PointDto p) throws NotImplementedException {
		
		Point.Builder builder = Point.measurement(p.getMeasurement()).time(p.getTimestamp(), TimeUnit.MILLISECONDS);
		if(!p.getFields().isEmpty()){
			for( Map.Entry<String, Object> entry: p.getFields().entrySet() ){
				Object value = entry.getValue();
				String field = entry.getKey();
				if ( value instanceof Number ) {
					if ( value instanceof Integer )
						builder.addField(field, (Integer)value);
					else
						builder.addField(field, (Double)value);
				}
				else if ( value instanceof String )
					builder.addField(field, (String)value);
				else
					throw new NotImplementedException("!!! only Number and String types allowed in fields, at this moment !!!");
			}
		}
		
		if(!p.getTags().isEmpty())
			builder.tag(p.getTags());

		return builder.build();
		
	}
	
	public static Event createPointEvent(final double value, final String measurement) throws Exception {
	    Event result = null;
	      final Map<String, String> headers = new HashMap<String, String>();
	      long ts = new Date().getTime();
	      Map<String, String> tags = new HashMap<String, String>();
	      tags.put("name", "dummy_percentage");
	      Map<String, Object> values = new HashMap<String, Object>();
	      values.put("value", value);
	      headers.put("timestamp", Long.toString(ts));
	      PointDto point = new PointDto(ts, measurement, tags, values);
	      result = EventBuilder.withBody(PointUtils.toBytes(point), headers);
	    return result;
	  }
	

}
