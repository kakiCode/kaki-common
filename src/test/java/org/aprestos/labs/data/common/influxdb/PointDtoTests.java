package org.aprestos.labs.data.common.influxdb;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class PointDtoTests {
	
	private Long ts = System.currentTimeMillis();
	
	@Test
	public void test_010() {

		PointDto point = getPoint();
		Assert.assertEquals("test,a=x,b=y a=1.0,b=2.0 " + ts, point.toString());

	}
	
	@Test
	public void test_020() throws JsonParseException, JsonMappingException, IOException {

		PointDto p0 = getPoint();
		ObjectMapper objMapper = new ObjectMapper();
		String json = objMapper.writeValueAsString(p0);
		
		PointDto p1 = objMapper.readValue(json, PointDto.class);
		
		Assert.assertEquals(p1.toString(), p0.toString());

	}
	
	private PointDto getPoint(){
		PointDto point = new PointDto(ts, "test", new HashMap<String, String>(), new HashMap<String, Object>());
		point.addTag("a", "x");
		point.addTag("b", "y");
		point.addField("a", 1);
		point.addField("b", 2);
		return point;
	}

}
