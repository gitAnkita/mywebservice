package hello;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class BaseCacheConfig {

		private String system = "aerospike";

		private String host = "localhost";

		private String port = "3000";

		private String defaultTTL = "120";

		private String namespace = "switch";

		private String set = "mywebservice";

		private String bin = "demo";
}
