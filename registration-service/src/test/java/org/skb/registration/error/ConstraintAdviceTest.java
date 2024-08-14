package org.skb.registration.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skb.registration.errors.ConstraintAdvice;
import org.skb.registration.errors.ErrorResponse;
import org.skb.registration.errors.RegistrationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConstraintAdviceTest {

    @InjectMocks
    private ConstraintAdvice constraintAdvice;

    @Test
    void handleMethodArgumentNotValid_shouldReturnBadRequestWithErrorMessages() {
        MethodParameter methodParameter = mock(MethodParameter.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError(
                "objectName",
                "fieldName",
                "defaultMessage")));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ErrorResponse> response = constraintAdvice.handleMethodArgumentNotValid(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("ValidationError: defaultMessage");
    }

    @Test
    void handleRegistration_shouldReturnConflictWithErrorMessage() {
        String errorMessage = "User already exists";
        RegistrationException exception = new RegistrationException(errorMessage);

        ResponseEntity<ErrorResponse> response = constraintAdvice.handleRegistration(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(errorMessage);
    }
}
