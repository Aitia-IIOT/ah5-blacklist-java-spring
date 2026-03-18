/*******************************************************************************
 *
 * Copyright (c) 2026 AITIA
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 *
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  	AITIA - implementation
 *  	Arrowhead Consortia - conceptualization
 *
 *******************************************************************************/
package eu.arrowhead.blacklist.jpa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import eu.arrowhead.blacklist.jpa.repository.EntryRepository;

@ExtendWith(MockitoExtension.class)
public class EntryDbServiceTest {

	//=================================================================================================
	// members

	@InjectMocks
	private EntryDbService dbService;

	@Mock
	private EntryRepository entryRepo;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testCreateBulkListNull() {
		final Throwable ex = assertThrows(
				IllegalArgumentException.class,
				() -> dbService.createBulk(null, null));

		assertEquals("candidates list is null", ex.getMessage());
	}
}