package com.outty.backend.profile.entity;

import com.outty.backend.profile.entity.enums.AdventureType;
import com.outty.backend.profile.entity.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "profile_adventures",
        uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "adventure_type"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileAdventure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "adventure_type", nullable = false, length = 50)
    private AdventureType adventureType;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_level", nullable = false, length = 50)
    private SkillLevel skillLevel;
}
