import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.anuragroy"})
@EntityScan(basePackages = {"com.anuragroy"})
@ComponentScan(basePackages = {"com.anuragroy"})
public class AssetManagementBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetManagementBootApplication.class, args);
	}

}
