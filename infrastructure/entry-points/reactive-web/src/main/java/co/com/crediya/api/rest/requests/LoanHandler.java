package co.com.crediya.api.rest.requests;

import co.com.crediya.api.dtos.loan.CreateLoanRequest;
import co.com.crediya.api.mappers.LoanMapper;
import co.com.crediya.usecase.loan.LoanServicePort;
import co.com.crediya.usecase.typeloan.TypeLoanServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
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

    private final LoanMapper loanMapper;
    private final TransactionalOperator transactionalOperator;


    public Mono<ServerResponse> listenSaveLoan(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateLoanRequest.class)
                .doOnNext( loanRequest -> log.info("Saving loan request={}", loanRequest) )
                .flatMap( loanRequest ->
                                typeLoanServicePort.findByCode(loanRequest.codeTypeLoan())
                                .map( typeLoan -> loanMapper.createRequestToModel(loanRequest, typeLoan.getId()))
                                .doOnError(throwable -> log.warn("Error saving loan={}", loanRequest))
                )
                .flatMap( loanServicePort::saveLoan )
                .map( loanMapper::modelToResponse )
                .flatMap( savedLoan ->
                    ServerResponse.created(URI.create(""))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(savedLoan)
                ).as( transactionalOperator::transactional );
    }
}
