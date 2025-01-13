package eu.arrowhead.blacklist.jpa.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import eu.arrowhead.blacklist.jpa.entity.Entry;
import eu.arrowhead.common.jpa.RefreshableRepository;

@Repository
public interface EntryRepository extends RefreshableRepository<Entry, Long>  {

	//=================================================================================================
	// methods
	//-------------------------------------------------------------------------------------------------
	public List<Entry> findAllBySystemNameIn(final List<String> names);
	
	//-------------------------------------------------------------------------------------------------
	public Page<Entry> findAllByIdIn(final List<Long> ids, Pageable pageble);
}
