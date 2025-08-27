package co.com.crediya.api.rest.loan;

import co.com.crediya.api.dtos.loan.CreateLoanRequest;
import co.com.crediya.api.dtos.loan.LoanResponse;
import co.com.crediya.api.exception.model.CustomError;
import co.com.crediya.api.mappers.LoanMapper;
import co.com.crediya.api.util.HandlersUtil;
import co.com.crediya.usecase.loan.LoanServicePort;
import co.com.crediya.usecase.typeloan.TypeLoanServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanHandler {

    private final LoanServicePort loanServicePort;
    private final TypeLoanServicePort typeLoanServicePort;
    private final Validator validator;

    private final LoanMapper loanMapper;



    @Operation( tags = "Loans", operationId = "saveLoan", description = "Save a request of loan", summary = "Save a request of loan",
            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateLoanRequest.class ) ) ),
            responses = { @ApiResponse( responseCode = "201", description = "Loan saved successfully.", content = @Content( schema = @Schema( implementation = LoanResponse.class ) ) ),
                    @ApiResponse( responseCode = "400", description = "Request body is not valid.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) ),
                    @ApiResponse( responseCode = "404", description = "User document sent is not found.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) )
            }
    )
    public Mono<ServerResponse> listenSaveLoan(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateLoanRequest.class)
                .doOnNext( loanRequest -> log.info("Saving loan request={}", loanRequest) )
                .flatMap( loanRequest -> {
                    Errors errors = new BeanPropertyBindingResult(loanRequest, CreateLoanRequest.class.getName());
                    validator.validate(loanRequest, errors);

                    System.out.println("Hay errors: " + errors.hasErrors());

                    if( errors.hasErrors() ) return ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(HandlersUtil.buildBodyResponse(
                                false, HttpStatus.BAD_REQUEST.value(), "error", HandlersUtil.getFieldErrors(errors)
                        ));

                    return typeLoanServicePort.findByCode(loanRequest.codeTypeLoan())
                        .map( typeLoan -> loanMapper.createRequestToModel(loanRequest, typeLoan.getId()))
                        .doOnError(throwable -> log.warn("Error saving loan={}", loanRequest))
                        .flatMap( loanServicePort::saveLoan )
                        .map( loanMapper::modelToResponse )
                        .flatMap( savedLoan ->
                            ServerResponse.created(URI.create(""))
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(HandlersUtil.buildBodyResponse(true, HttpStatus.CREATED.value(), "data", savedLoan))
                        );
                });



    }
}
