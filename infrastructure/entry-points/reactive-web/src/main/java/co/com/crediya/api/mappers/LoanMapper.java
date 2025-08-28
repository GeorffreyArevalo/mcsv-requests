package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.loan.CreateLoanRequestDTO;
import co.com.crediya.api.dtos.loan.LoanResponseDTO;
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
    Loan createRequestToModel(CreateLoanRequestDTO request, Long idTypeLoan);
    LoanResponseDTO modelToResponse(Loan model);


}
