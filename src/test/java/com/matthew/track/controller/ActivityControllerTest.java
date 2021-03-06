package com.matthew.track.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.matthew.track.model.activity.Activity;
import com.matthew.track.model.activity.Event;
import com.matthew.track.model.activity.Rating;
import com.matthew.track.model.activity.ActivityDTO;
import com.matthew.track.service.impl.ActivityServiceImpl;
import com.matthew.track.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ActivityControllerTest {

    String BASE_URL = "/api/v1/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private ActivityServiceImpl activityService;

    @MockBean
    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(roles={"USER"})
    void shouldGetActivityById() throws Exception { // TODO add unhappy path test

        // given
        long ACTIVITY_ID = 1;

        Activity activity = new Activity();
        activity.setId(ACTIVITY_ID);
        activity.setTitle("Test Activity");
        activity.setDescription("Test Description");
        activity.setCreated(new Date());

        // when
        when(activityService.getActivityById(ACTIVITY_ID)).thenReturn(activity);

        // then
        this.mockMvc.perform(get(BASE_URL+"/activities/"+ACTIVITY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACTIVITY_ID))
                .andReturn();
    }

    @Test
    @WithMockUser(roles={"USER"})
    void shouldGetAllActivities() throws Exception { // TODO add unhappy path test
        // given
        long ACTIVITY_ID_ONE = 1;
        Activity activity_one = new Activity();
        activity_one.setId(ACTIVITY_ID_ONE);
        activity_one.setTitle("Test Activity");
        activity_one.setDescription("Test Description");
        activity_one.setCreated(new Date());

        long ACTIVITY_ID_TWO = 2;
        Activity activity_two = new Activity();
        activity_two.setId(ACTIVITY_ID_TWO);
        activity_two.setTitle("Test Activity");
        activity_two.setDescription("Test Description");
        activity_two.setCreated(new Date());

        List<Activity> activityList = List.of(activity_one, activity_two);

        // when
        when(activityService.getAllActivities()).thenReturn(activityList);

        // then
        this.mockMvc.perform(get(BASE_URL+"/activities/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ACTIVITY_ID_ONE))
                .andExpect(jsonPath("$[1].id").value(ACTIVITY_ID_TWO))
                .andReturn();
    }

    @Test
    @WithMockUser(roles={"USER"})
    void shouldAddActivity() throws Exception { // TODO add unhappy path test

        // given
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setTitle("Test Title");
        activityDTO.setDescription("Test Description");

        Activity activityToSave = convertToEntity(activityDTO);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        String requestJson = objectWriter.writeValueAsString(activityDTO);

        // when
        when(activityService.saveActivity(any(Activity.class))).thenReturn(activityToSave);

        // then
        this.mockMvc.perform(post(BASE_URL+"/activities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @WithMockUser(roles={"USER"})
    void shouldUpdateActivity() throws Exception { // TODO add unhappy path test

        // given
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setTitle("Test Title");
        activityDTO.setDescription("Test Description");

        Activity activityToSave = convertToEntity(activityDTO);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        String requestJson = objectWriter.writeValueAsString(activityDTO);

        // when
        when(activityService.saveActivity(any(Activity.class))).thenReturn(activityToSave);

        // then
        this.mockMvc.perform(put(BASE_URL+"/activities/update")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(roles={"USER"})
    void shouldDeleteActivity() throws Exception { // TODO add unhappy path test
        // given
        long ACTIVITY_ID = 1;
        Activity activity = new Activity();
        activity.setTitle("Test Title");
        activity.setDescription("Test Description");
        activity.setCreated(new Date());
        activity.setId(ACTIVITY_ID);

        // when
        when(activityService.getActivityById(ACTIVITY_ID)).thenReturn(activity);

        // then
        this.mockMvc.perform(delete(BASE_URL+"/activities/delete/"+ACTIVITY_ID))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(roles={"USER"})
    @Disabled
    void uploadFile() {
    }

    @Test
    @WithMockUser(roles={"USER"})
    void shouldAddEvent() throws Exception { // TODO add unhappy path test

        // given
        long ACTIVITY_ID = 1;
        Activity activity = new Activity();
        activity.setTitle("Test Title");
        activity.setDescription("Test Description");
        activity.setCreated(new Date());
        activity.setId(ACTIVITY_ID);

        long EVENT_ID = 1;
        Event event = new Event();
        event.setTitle("Test Title");
        event.setDescription("Test Description");
        event.setRating(Rating.EXCELLENT);
        event.setId(EVENT_ID);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        String requestJson = objectWriter.writeValueAsString(event);

        // when
        when(activityService.getActivityById(ACTIVITY_ID)).thenReturn(activity);

        // then
        this.mockMvc.perform(post(BASE_URL+"/activities/"+ACTIVITY_ID+"/event/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
    }

    private ActivityDTO convertToDto(Activity activity) {
        ActivityDTO activityDTO = modelMapper.map(activity, ActivityDTO.class);
        activityDTO.setEvents(activity.getEvents());

        return activityDTO;
    }

    private Activity convertToEntity(ActivityDTO activityDTO) throws ParseException {
        Activity activity = modelMapper.map(activityDTO, Activity.class);

        return activity;
    }


}