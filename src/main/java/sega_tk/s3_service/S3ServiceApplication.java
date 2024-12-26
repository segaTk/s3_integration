package sega_tk.s3_service;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class S3ServiceApplication {

	public static void main(String[] args) {
		new  SpringApplicationBuilder(S3ServiceApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}
}
