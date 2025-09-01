package co.com.crediya.consumer.mappers;

import co.com.crediya.consumer.dtos.user.UserConsumerResponseDTO;
import co.com.crediya.model.Loan;
import co.com.crediya.port.consumers.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

class UserConsumerMapperTest {

    private UserConsumerMapper mapper = Mappers.getMapper(UserConsumerMapper.class);

    private final UserConsumerResponseDTO userConsumerResponseDTO  =
            UserConsumerResponseDTO.builder()
                    .name("Julian")
                    .lastName("Arevalo")
                    .basePayment(BigDecimal.TEN)
                    .dateOfBirth(LocalDate.now())
                    .document("131546123")
                    .phone("123456789")
                    .email("julian@arevalo.com")
                    .build();


    @Test
    void testConsumerToModel() {

        Mono<User> result = Mono.fromCallable(() -> mapper.consumerToModel(userConsumerResponseDTO));

        StepVerifier.create(result)
                .expectNextMatches( userResponse ->
                        userResponse.getDocument().equals(userConsumerResponseDTO.getDocument())
                        && userResponse.getBasePayment().equals(userConsumerResponseDTO.getBasePayment())
                        && userResponse.getName().equals(userConsumerResponseDTO.getName())

                )
                .verifyComplete();

    }

}
