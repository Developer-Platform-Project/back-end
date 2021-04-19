package com.project.devidea.modules.content.study;

import com.project.devidea.modules.account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(indexes=@Index(columnList = "study_id,member_id"),
        uniqueConstraints=@UniqueConstraint(columnNames ={"study_id","member_id"}))
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of="id")
public class StudyMember {

    @Id
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "mySeq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @Column(name="study_meber_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Account member;

    @Setter
    @Enumerated(EnumType.STRING)
    private StudyRole role;

    private LocalDateTime JoinDate;
    @Override
    public String toString(){
         return member.toString();
    }


}