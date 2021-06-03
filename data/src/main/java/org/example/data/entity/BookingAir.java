package org.example.data.entity;
//

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class BookingAir {
    @NotNull
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(insertable = false, updatable = false)
    private UUID id;
    private String idOrder;

    @ManyToOne
    @JoinColumn(name="person_id", nullable=false)
    private Person person;

    @Column(insertable = true, updatable = false)
    private LocalDateTime created;

    @PrePersist
    void onCreate() {
        this.setCreated(LocalDateTime.now());
    }
}
