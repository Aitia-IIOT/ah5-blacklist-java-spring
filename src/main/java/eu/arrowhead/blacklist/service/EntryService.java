package eu.arrowhead.blacklist.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.arrowhead.blacklist.jpa.repository.EntryRepository;

@Service
public class EntryService {
	
	//=================================================================================================
	// members
	
	@Autowired
	private EntryRepository entryRepo;
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	//=================================================================================================
	// methods

}
