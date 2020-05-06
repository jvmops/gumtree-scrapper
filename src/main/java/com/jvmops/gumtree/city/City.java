package com.jvmops.gumtree.city;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
@Builder
public class City {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String name;
    private String urlCode;
    @Builder.Default
    private Set<String> notifications = new HashSet<>();
    @CreatedDate
    private LocalDateTime creationTime;
    @LastModifiedDate
    private LocalDateTime modificationTime;

    public City(String name) {
        this.name = name;
    }

    public boolean subscribe(String email) {
        return notifications.add(email);
    }
}
