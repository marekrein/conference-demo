package co.reinhold.testworkconference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.reinhold.testworkconference.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {}
