package eu.arrowhead.blacklist.jpa.repository;

import org.springframework.stereotype.Repository;

import eu.arrowhead.blacklist.jpa.entity.Logs;
import eu.arrowhead.common.jpa.LogEntityRepository;

@Repository
public interface LogsRepository extends LogEntityRepository<Logs> {
}
