package co.reinhold.testworkconference.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConferenceDto(@NotBlank @Size(min = 2, max = 100) String name,
                            @NotNull LocalDateTime startDate,
                            @NotNull LocalDateTime endDate) {}
