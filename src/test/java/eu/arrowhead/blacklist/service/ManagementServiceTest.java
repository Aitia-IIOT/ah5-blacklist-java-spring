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
package eu.arrowhead.blacklist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import eu.arrowhead.blacklist.jpa.entity.Entry;
import eu.arrowhead.blacklist.jpa.service.EntryDbService;
import eu.arrowhead.blacklist.service.dto.DTOConverter;
import eu.arrowhead.blacklist.service.dto.NormalizedBlacklistQueryRequestDTO;
import eu.arrowhead.blacklist.service.validation.Validation;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.InternalServerError;
import eu.arrowhead.common.service.PageService;
import eu.arrowhead.dto.BlacklistQueryRequestDTO;

@ExtendWith(MockitoExtension.class)
public class ManagementServiceTest {

	//=================================================================================================
	// members

	@InjectMocks
	private ManagementService service;

	@Mock
	private Validation validator;

	@Mock
	private DTOConverter dtoConverter;

	@Mock
	private EntryDbService dbService;

	@Mock
	private PageService pageService;

	@Mock
	private WhitelistService whitelistService;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testQueryOriginNull() {
		final Throwable ex = assertThrows(
				IllegalArgumentException.class,
				() -> service.query(null, null));

		assertEquals("origin is empty", ex.getMessage());
	}

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testQueryOriginEmpty() {
		final Throwable ex = assertThrows(
				IllegalArgumentException.class,
				() -> service.query(null, ""));

		assertEquals("origin is empty", ex.getMessage());
	}

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testQueryInternalServerError() {
		final BlacklistQueryRequestDTO request = new BlacklistQueryRequestDTO(null, null, null, null, null, null, null);
		final NormalizedBlacklistQueryRequestDTO normalized = new NormalizedBlacklistQueryRequestDTO(null, null, null, null, null, null, null);
		final PageRequest pageRequest = PageRequest.of(0, 1000, Direction.DESC, "systemName");

		when(validator.validateAndNormalizeBlacklistQueryRequestDTO(request, "origin")).thenReturn(normalized);
		when(pageService.getPageRequest(null, Direction.DESC, Entry.SORTABLE_FIELDS_BY, Entry.DEFAULT_SORT_FIELD, "origin")).thenReturn(pageRequest);
		when(dbService.getPageByFilters(pageRequest, null, null, null, null, null, null)).thenThrow(new InternalServerError("test"));

		final ArrowheadException ex = assertThrows(
				InternalServerError.class,
				() -> service.query(request, "origin"));

		assertEquals("test", ex.getMessage());
		assertEquals("origin", ex.getOrigin());

		verify(validator).validateAndNormalizeBlacklistQueryRequestDTO(request, "origin");
		verify(pageService).getPageRequest(null, Direction.DESC, Entry.SORTABLE_FIELDS_BY, Entry.DEFAULT_SORT_FIELD, "origin");
		verify(dbService).getPageByFilters(pageRequest, null, null, null, null, null, null);
	}
}