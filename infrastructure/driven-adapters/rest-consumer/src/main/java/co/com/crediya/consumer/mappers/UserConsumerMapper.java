package co.com.crediya.consumer.mappers;

import co.com.crediya.consumer.dtos.user.UserConsumerResponseDTO;
import co.com.crediya.port.consumers.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserConsumerMapper {

    User consumerToModel(UserConsumerResponseDTO userConsumer);


}
