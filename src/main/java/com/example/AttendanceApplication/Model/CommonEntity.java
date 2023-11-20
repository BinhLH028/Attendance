package com.example.AttendanceApplication.Model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

public abstract class CommonEntity {

    @Column(name = "del_flag", columnDefinition = "boolean default false" )
    public boolean delFlag;
    @Column(name = "record_version", columnDefinition = "INTERGER default 0")
    public int recordVersion;
}
