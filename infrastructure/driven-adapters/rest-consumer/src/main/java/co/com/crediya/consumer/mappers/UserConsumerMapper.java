package co.com.crediya.consumer.mappers;

import co.com.crediya.consumer.dtos.user.UserResponse;
import co.com.crediya.port.consumers.model.UserConsumer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserConsumerMapper {

    UserConsumer toUserConsumer(UserResponse userConsumer);


}
