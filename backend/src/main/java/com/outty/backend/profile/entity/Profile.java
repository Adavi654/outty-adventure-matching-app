package com.outty.backend.profile.entity;

import com.outty.backend.auth.entity.User;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.outty.backend.profile.entity.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 50)
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(nullable = false, length = 500)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "interested_in", nullable = false, length = 20)
    private InterestedIn interestedIn;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_goal", nullable = false, length = 100)
    private RelationshipGoal relationshipGoal;

    @ElementCollection
    @CollectionTable(name = "profile_photos", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "photo_url", nullable = false)
    @Builder.Default
    private List<String> photos = new ArrayList<>();
  
    @Column(name = "instagram_url", nullable = true, length = 255)
    private String instagramUrl;

    @Column(name = "facebook_url", nullable = true, length = 255)
    private String facebookUrl;

    @Column(name = "x_url", nullable = true, length = 255)
    private String xUrl;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProfileAdventure> adventures = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
