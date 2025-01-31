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
	public List<Entry> findAllBySystemName(final String name);

	//-------------------------------------------------------------------------------------------------
	public List<Entry> findAllBySystemNameAndActive(final String name, final boolean active);

	//-------------------------------------------------------------------------------------------------
	public Page<Entry> findAllByIdIn(final List<Long> ids, final Pageable pageable);
}
