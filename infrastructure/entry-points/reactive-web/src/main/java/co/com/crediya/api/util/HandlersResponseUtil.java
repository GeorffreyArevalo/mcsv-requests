package co.com.crediya.api.util;

import co.com.crediya.api.dtos.CrediyaResponseDTO;
import co.com.crediya.api.dtos.PageableResponseDTO;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class HandlersResponseUtil {


    public static <T> CrediyaResponseDTO<T> buildBodySuccessResponse(String code, T data ) {
        return CrediyaResponseDTO
                .<T>builder()
                .message("Operation successful!")
                .data(data)
                .code(code)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static CrediyaResponseDTO buildBodyFailureResponse(String code, String message, List<String> errors ) {
        return CrediyaResponseDTO
                .builder()
                .message(message)
                .errors(errors)
                .code(code)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> PageableResponseDTO<T> buildBodySuccessPageableResponse(
            String code, List<T> data, int size, int page, long total
    ) {
        return PageableResponseDTO
                .<T>builder()
                .message("Operation successful!")
                .data(data)
                .page(page)
                .size(size)
                .code(code)
                .total(total)
                .timestamp(LocalDateTime.now())
                .build();
    }




}
