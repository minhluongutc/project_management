package datn.backend.controllers;

import datn.backend.dto.UserCalendarRequestDTO;
import datn.backend.service.UserCalendarService;
import datn.backend.service.jpa.UserCalendarServiceJPA;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCalenderController {
    final UserCalendarService userCalendarService;

    final UserCalendarServiceJPA userCalendarServiceJPA;

    @GetMapping(value = "/user-calender", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserCalender(Authentication authentication,
                                                  String projectId) {
        Object result = userCalendarServiceJPA.getUserCalendars(authentication, projectId);
        return ResponseUtils.getResponseEntity(result);
    }

    @GetMapping(value = "/user-calender/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserCalenderById(Authentication authentication,
                                                      @PathVariable String id) {
        Object result = userCalendarServiceJPA.getUserCalendarById(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/user-calender", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertUserCalender(Authentication authentication,
                                                     @RequestBody UserCalendarRequestDTO dto) {
        Object result = userCalendarService.insertUserCalendar(authentication, dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "/user-calender/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUserCalender(Authentication authentication,
                                                     @RequestBody UserCalendarRequestDTO dto,
                                                     @PathVariable String id) {
        Object result = userCalendarService.updateUserCalendar(authentication, dto, id);
        return ResponseUtils.getResponseEntity(result);
    }

    @DeleteMapping(value = "/user-calender/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteUserCalender(Authentication authentication, @PathVariable String id) {
        Object result = userCalendarService.deleteUserCalendar(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }
}
