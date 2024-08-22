package co.reinhold.testworkconference.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.reinhold.testworkconference.config.SharedMapperConfig;
import co.reinhold.testworkconference.dto.ConferenceCreateRequest;
import co.reinhold.testworkconference.dto.ConferenceDto;
import co.reinhold.testworkconference.model.Conference;

@Mapper(config = SharedMapperConfig.class)
public abstract class ConferenceMapper {

    public abstract ConferenceDto toDto(Conference conference);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "participants", ignore = true)
    public abstract Conference toEntity(ConferenceCreateRequest conferenceCreateRequest);

}
