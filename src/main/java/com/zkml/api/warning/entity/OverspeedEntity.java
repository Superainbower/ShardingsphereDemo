package com.zkml.api.warning.entity;

import lombok.Data;

@Data
public class OverspeedEntity {
    int id;
    String car_no;
    String begin_address;
    String end_address;
    String begin_time;
    String end_time;
    double speed;
    int warn_seconds;
    int max_speed;
    int max_time;
}
