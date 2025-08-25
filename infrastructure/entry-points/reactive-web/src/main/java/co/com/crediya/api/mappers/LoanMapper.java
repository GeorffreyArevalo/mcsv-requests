package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.loan.CreateLoanRequest;
import co.com.crediya.api.dtos.loan.LoanResponse;
import co.com.crediya.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LoanMapper {


    @Mapping(
            source = "idTypeLoan",
            target = "idTypeLoan"
    )
    Loan createRequestToModel(CreateLoanRequest request, Long idTypeLoan);
    LoanResponse  modelToResponse(Loan model);


}
