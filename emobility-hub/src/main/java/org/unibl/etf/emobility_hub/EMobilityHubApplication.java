package org.unibl.etf.emobility_hub;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EMobilityHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(EMobilityHubApplication.class, args);
	}

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        // Ako ne zna da mapira neka preskoči
        mapper.getConfiguration().setAmbiguityIgnored(true);
        return  mapper;
    }
}
