package co.com.crediya.sqs.sender.mappers;

import co.com.crediya.port.queue.messages.MessageNotificationQueue;
import co.com.crediya.sqs.sender.dtos.MessageNotificationSqsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface SqsMapper {

    @Mappings(value = {
            @Mapping( target = "name", source = "clientName"),
            @Mapping( target = "lastName", source = "clientLastName")
    })
    MessageNotificationSqsDTO messageToSqsDto(MessageNotificationQueue messageNotificationQueue);

}
