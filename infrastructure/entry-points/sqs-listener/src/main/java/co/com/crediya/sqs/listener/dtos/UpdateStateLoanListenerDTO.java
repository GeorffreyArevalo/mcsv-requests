package co.com.crediya.sqs.listener.dtos;

public record UpdateStateLoanListenerDTO(
        Long idLoan,
        String stateLoan
) {
}
