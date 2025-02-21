package com.eazybytes.accounts.controller;


import com.eazybytes.accounts.constants.AccountConstants;
import com.eazybytes.accounts.dto.CustomerDTO;
import com.eazybytes.accounts.dto.ErrorResponseDto;
import com.eazybytes.accounts.dto.ResponseDTO;
import com.eazybytes.accounts.service.IAccountService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    private IAccountService accountService;

    @Operation(summary = "Create Account REST API",description = "description")
    @ApiResponses(
            {@ApiResponse(responseCode = "201", description = "description"),

            @ApiResponse(responseCode = "400", description = "400"),

            @ApiResponse(responseCode = "500", description = "Internal Server Error"
                    ,content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
            ))
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createAccount(@Valid @RequestBody CustomerDTO customerDTO){
        accountService.createAccount(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(AccountConstants.STATUS_201,AccountConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDTO> fetchCustomerData(@Pattern(regexp = "(^$|[0-9]{10})",message = "Phone number must 10 digits")
                                                             @RequestParam String mobileNumber){
        CustomerDTO customerDTO = accountService.fetchData(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);

    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateAccount(@Valid @RequestBody CustomerDTO customerDTO){
        boolean isUpdated = accountService.updateAccount(customerDTO);
        if(isUpdated)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(AccountConstants.STATUS_200,AccountConstants.MESSAGE_200));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(AccountConstants.STATUS_500,AccountConstants.MESSAGE_500));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteAccount(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})",message = "Phone number must 10 digits") String mobileNumber){
        boolean isDeleted = accountService.deleteAccount(mobileNumber);
        if(isDeleted)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(AccountConstants.STATUS_200,AccountConstants.MESSAGE_200));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(AccountConstants.STATUS_500,AccountConstants.MESSAGE_500));
    }


    @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo(){
        logger.debug("getBuildInfo");
        throw new NullPointerException();
//        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @RateLimiter(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity.status(HttpStatus.OK).body("Java 8 \n");
    }


    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable){
        logger.debug("getBuildInfoFallBack");
        return ResponseEntity.status(HttpStatus.OK).body("build info fall back ne");
    }

    public ResponseEntity<String> getJavaVersionFallback(Throwable throwable){
        logger.debug("getJavaVersionFallback");
        return ResponseEntity.status(HttpStatus.OK).body("java version fall back ne");
    }
}