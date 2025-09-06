package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.loan.SearchLoansRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class SearchParamsMapperLoanTest {

    private final SearchParamsMapper mapper = Mappers.getMapper(SearchParamsMapper.class);

    private MultiValueMap<String, String> params;

    @BeforeEach
    void setUp() {
        params = new LinkedMultiValueMap<>();
        params.add("size", "10");
        params.add("page", "0");
        params.add("state", "APROVADA");
    }

    @Test
    void testQueryParamsToLoanRequest() {
        Mono<SearchLoansRequestDTO> result = Mono.fromCallable(() -> mapper.queryParamsToLoanRequest(params));

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.size().equals(10) &&
                                dto.page().equals(0) &&
                                dto.state().equals("APROVADA")
                )
                .verifyComplete();
    }

    @Test
    void testMapWithValues() {
        List<String> values = List.of("PENDING");

        Mono<String> result = Mono.fromCallable(() -> mapper.map(values));

        StepVerifier.create(result)
                .expectNext("PENDING")
                .verifyComplete();
    }

    @Test
    void testMapToIntWithValues() {
        List<String> values = List.of("999");

        Mono<Integer> result = Mono.fromCallable(() -> mapper.mapToInt(values));

        StepVerifier.create(result)
                .expectNext(999)
                .verifyComplete();
    }

    @Test
    void testQueryParamsToLoanRequestWithEmptyParams() {
        MultiValueMap<String, String> emptyParams = new LinkedMultiValueMap<>();

        Mono<SearchLoansRequestDTO> result = Mono.fromCallable(() -> mapper.queryParamsToLoanRequest(emptyParams));

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.size() == null &&
                                dto.page() == null &&
                                dto.state() == null
                )
                .verifyComplete();
    }
}
