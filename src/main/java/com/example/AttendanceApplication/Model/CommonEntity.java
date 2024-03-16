package com.example.AttendanceApplication.Model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class CommonEntity {

    @Column(name = "del_flag", columnDefinition = "boolean default false" )
    public boolean delFlag;

    @Column(name = "record_version", columnDefinition = "INTEGER default 0")
    @Version
    public int recordVersion;
}
