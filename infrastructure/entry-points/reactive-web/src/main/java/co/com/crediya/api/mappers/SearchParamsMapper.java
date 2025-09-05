package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.loan.SearchLoansRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SearchParamsMapper {


    SearchLoansRequestDTO queryParamsToLoanRequest(MultiValueMap<String, String> params);

    default String map(List<String> values) {
        return (values != null && !values.isEmpty()) ? values.getFirst() : null;
    }

    default Integer mapToInt(List<String> values) {
        return (values != null && !values.isEmpty()) ? Integer.parseInt(values.getFirst()) : null;
    }

}
