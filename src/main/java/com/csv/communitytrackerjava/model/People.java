package com.csv.communitytrackerjava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "people")
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peopleid", updatable = false, nullable = false)
    private Integer peopleId;

    @Column(name = "projectid")
    private Integer projectId;
    
    @Column(name = "cognizantid")
    private Integer cognizantId;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "middlename")
    private String middleName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "jobleveldesc")
    private String jobLevelDesc;

    @Column(name = "communityname")
    private String communityName;

    @Column(name = "projectdesc")
    private String projectDesc;

    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

}
