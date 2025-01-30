package org.unibl.etf.emobility_hub;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.unibl.etf.emobility_hub.models.domain.entity.ClientEntity;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;

@SpringBootApplication
public class EMobilityHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(EMobilityHubApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        // Ako ne zna da mapira neka preskoči
        mapper.getConfiguration().setAmbiguityIgnored(true);

        // Specifično mapiranje za ClientEntity -> ClientResponse
        mapper.typeMap(ClientEntity.class, ClientResponse.class)
                .addMappings(m -> m.map(
                        src -> src.getAvatarImage() != null ? src.getAvatarImage().replace("\\", "/") : null,
                        ClientResponse::setImage
                ));

        // Globalni konverter za sva String polja
        mapper.addConverter(context -> {
            String source = context.getSource();
            return source != null ? source.replace("\\", "/") : null;
        }, String.class, String.class);
        return mapper;
    }
}
