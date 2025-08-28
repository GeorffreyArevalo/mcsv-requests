package co.com.crediya.api.rest.loan;

import co.com.crediya.api.dtos.CrediyaResponseDTO;
import co.com.crediya.api.dtos.loan.CreateLoanRequestDTO;
import co.com.crediya.api.dtos.loan.LoanResponseDTO;
import co.com.crediya.api.mappers.LoanMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.usecase.loan.LoanUseCase;
import co.com.crediya.usecase.typeloan.TypeLoanUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanHandler {

    private final LoanUseCase loanUseCase;
    private final TypeLoanUseCase typeLoanUseCase;

    private final ValidatorUtil validatorUtil;
    private final LoanMapper loanMapper;



    @Operation( tags = "Loans", operationId = "saveLoan", description = "Save a request of loan", summary = "Save a request of loan",
            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateLoanRequestDTO.class ) ) ),
            responses = { @ApiResponse( responseCode = "201", description = "Loan saved successfully.", content = @Content( schema = @Schema( implementation = LoanResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "400", description = "Request body is not valid.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "404", description = "User document sent is not found.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) )
            }
    )
    public Mono<ServerResponse> listenSaveLoan(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateLoanRequestDTO.class)
                .doOnNext(loanRequest -> log.info("Saving loan request={}", loanRequest))
                .flatMap( validatorUtil::validate )
                .flatMap( loanRequest ->
                        typeLoanUseCase.findByCode(loanRequest.codeTypeLoan())
                                .map( typeLoan -> loanMapper.createRequestToModel(loanRequest, typeLoan.getId()) )
                )
                .flatMap( loanUseCase::saveLoan )
                .map( loanMapper::modelToResponse )
                .flatMap( savedLoan ->
                        ServerResponse.created(URI.create(""))
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.CREATED.status(), savedLoan) )
                );



    }
}
