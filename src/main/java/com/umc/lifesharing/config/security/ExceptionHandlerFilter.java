package com.umc.lifesharing.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.lifesharing.apiPayload.code.ErrorReasonDTO;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.GeneralException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 다음 filter Chain에 대한 실행 (filter-chain의 마지막에는 Dispatcher Servlet이 실행된다.
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 토큰의 유효기간 만료
            log.info("토큰의 유효기간 만료");
            setErrorResponse(response, ErrorStatus.TOKEN_INVALID);
        } catch (JwtException | IllegalArgumentException e) {
            // 유효하지 않은 토큰
            log.info("유효하지 않은 토큰");
            setErrorResponse(response, ErrorStatus.TOKEN_INVALID);
        } catch (NoSuchElementException | UsernameNotFoundException e) {
            // 사용자를 찾을 수 없음
            log.info("사용자를 찾을 수 없음");
            setErrorResponse(response, ErrorStatus.USER_NOT_FOUNDED);
        } catch (ArrayIndexOutOfBoundsException e) {
            // 토큰을 추출할 수 없음
            log.info("토큰을 추출할 수 없음");
            setErrorResponse(response, ErrorStatus.TOKEN_INVALID);
        } catch (NullPointerException e) {
            // 알 수 없는 에러
            log.info("Null Pointer Exception 발생");
            setErrorResponse(response, ErrorStatus.UNKNOWN_ERROR);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorStatus errorStatus) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorStatus.getHttpStatus().value());

        GeneralException generalException = new GeneralException(errorStatus);
        ErrorReasonDTO errorResponseDto = generalException.getErrorReason();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
        }catch (IOException e){
            log.error("응답 작성 중 에러 발생", e);
            e.printStackTrace();
        }
    }
}
