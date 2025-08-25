package co.com.crediya.api.rest.loan;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.loan.CreateLoanRequest;
import co.com.crediya.api.dtos.loan.LoanResponse;
import co.com.crediya.api.exception.model.CustomError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class LoanRouterRest {

    private final PathsConfig pathsConfig;


    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/loans",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE,
                    },
                    method = RequestMethod.POST,
                    beanClass = LoanHandler.class,
                    beanMethod = "listenSaveLoan",
                    operation = @Operation( tags = "Loans", operationId = "saveLoan", description = "Save a request of loan", summary = "Save a request of loan",
                            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateLoanRequest.class ) ) ),
                            responses = { @ApiResponse( responseCode = "201", description = "Loan saved successfully.", content = @Content( schema = @Schema( implementation = LoanResponse.class ) ) ),
                                    @ApiResponse( responseCode = "400", description = "Request body is not valid.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) ),
                                    @ApiResponse( responseCode = "404", description = "User document sent is not found.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(LoanHandler handler) {
        return route(POST(pathsConfig.getLoans()), handler::listenSaveLoan);
    }
}
