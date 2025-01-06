package eu.arrowhead.blacklist.jpa.repository;

import org.springframework.stereotype.Repository;

import eu.arrowhead.blacklist.jpa.entity.Entry;
import eu.arrowhead.common.jpa.RefreshableRepository;

@Repository
public interface EntryRepository extends RefreshableRepository<Entry, Long>  {

}
