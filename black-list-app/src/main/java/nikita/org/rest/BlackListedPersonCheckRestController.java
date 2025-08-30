package nikita.org.rest;

import com.google.common.base.Stopwatch;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nikita.org.core.api.BlackListedPersonCoreCommand;
import nikita.org.core.api.BlackListedPersonCoreResult;
import nikita.org.core.service.BlackListedPersonService;
import nikita.org.dto.BlackListedPersonCheckRequest;
import nikita.org.dto.BlackListedPersonCheckResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping("/blacklist/person/check")
public class BlackListedPersonCheckRestController {

    private final DtoConverter dtoConverter;
    private final BlackListedPersonService blackListedPersonService;

    @PostMapping(path = "/",
            consumes = "application/json",
            produces = "application/json")
    public BlackListedPersonCheckResponse checkPerson(@RequestBody BlackListedPersonCheckRequest request) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        BlackListedPersonCheckResponse response = processRequest(request);
        stopwatch.stop();
        log.info("Request processed in {} ms", stopwatch.elapsed().toMillis());
        log.info("Request blackListedPersonCheckRequest {},", response);
        return response;
    }

    private BlackListedPersonCheckResponse processRequest(BlackListedPersonCheckRequest request) {
       // requestLogger.log(request);
        log.info("Request blackListedPersonCheckRequest {}", request);
        BlackListedPersonCoreCommand coreCommand = dtoConverter.buildCoreCommand(request);
        BlackListedPersonCoreResult coreResult = blackListedPersonService.check(coreCommand);
        BlackListedPersonCheckResponse response = dtoConverter.buildResponse(coreResult);
      //  responseLogger.log(response);
        log.info("Response blackListedPersonCheckResponse {}", response);
        return response;
    }

}
