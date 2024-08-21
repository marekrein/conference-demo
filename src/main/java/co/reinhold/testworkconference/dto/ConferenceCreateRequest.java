package co.reinhold.testworkconference.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConferenceCreateRequest(@NotBlank @Size(max = 100) String name,
                                      @NotNull LocalDateTime startDate,
                                      @NotNull LocalDateTime endDate) {}
