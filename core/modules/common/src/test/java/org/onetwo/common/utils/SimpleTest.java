package org.onetwo.common.utils;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;
import org.onetwo.common.date.DateUtils;

public class SimpleTest {
	
	@Test
	public void test(){
		String str1 = "iid=22878612236&device_id=46503731872&ac=wifi&channel=meizu&aid=32&app_name=video_article&version_code=627&version_name=6.2.7&device_platform=android&ab_version=249703%2C221019%2C236847%2C241077%2C249883%2C249510%2C248462%2C229578%2C235216%2C249817%2C249631%2C235963%2C250931%2C236030%2C239019%2C247773%2C252404%2C235684%2C249824%2C249815%2C252010%2C252594%2C249830%2C248257%2C150151&ssmix=a&device_type=m3+note&device_brand=Meizu&language=zh&os_api=22&os_version=5.1&uuid=862143030676147&openudid=70fc30601792caf7&manifest_version_code=227&resolution=1080*1920&dpi=480&update_version_code=6272&_rticket=1515583161361";
		String str2 = "iid=22878612236&device_id=46503731872&ac=wifi&channel=meizu&aid=32&app_name=video_article&version_code=627&version_name=6.2.7&device_platform=android&ab_version=249703%2C221019%2C236847%2C241077%2C249883%2C249510%2C248462%2C229578%2C235216%2C249817%2C249631%2C235963%2C250931%2C236030%2C239019%2C247773%2C252404%2C235684%2C249824%2C249815%2C252010%2C252594%2C249830%2C248257%2C150151&ssmix=a&device_type=m3+note&device_brand=Meizu&language=zh&os_api=22&os_version=5.1&uuid=862143030676147&openudid=70fc30601792caf7&manifest_version_code=227&resolution=1080*1920&dpi=480&update_version_code=6272&_rticket=1515583400989";
		assertEquals(str1, str2);
	}

}
