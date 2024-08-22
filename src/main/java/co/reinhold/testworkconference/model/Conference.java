package co.reinhold.testworkconference.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "conferences")
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Future
    @NotNull
    private LocalDateTime startDate;

    @Future
    @NotNull
    private LocalDateTime endDate;

    private boolean active;

    private int roomCapacity;

    @ToString.Exclude
    @OneToMany(mappedBy = "conference",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @JsonBackReference("conference-participate")
    private Set<Participant> participants = new HashSet<>();

    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setConference(this);
    }

    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        participant.setConference(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Conference that))
            return false;
        return Objects.equals(name, that.name) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startDate, endDate);
    }

    public boolean isRoomAvailable() {
        return this.roomCapacity > this.participants.size();
    }

}
