package com.csv.communitytrackerjava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Project extends BaseAuditClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer projectId;

    @Column(length = 100, nullable = false)
    private String projectDesc;

    @Column(length = 100, unique = true, nullable = false)
    private String projectCode;

    @Column(nullable = false)
    private Boolean isActive;



}
